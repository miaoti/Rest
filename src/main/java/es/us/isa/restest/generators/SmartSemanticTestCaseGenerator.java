package es.us.isa.restest.generators;

import ai.onnxruntime.*;
import es.us.isa.restest.configuration.pojos.*;
import es.us.isa.restest.inputs.ITestDataGenerator;
import es.us.isa.restest.inputs.perturbation.ObjectPerturbator;
import es.us.isa.restest.inputs.random.RandomStringGenerator;
import es.us.isa.restest.inputs.stateful.BodyGenerator;
import es.us.isa.restest.specification.*;
import es.us.isa.restest.testcases.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.javatuples.Pair;
import net.sf.extjwnl.dictionary.Dictionary;
import net.sf.extjwnl.data.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Smart‑Semantic v3 – Test‑Case generator
 * <p>
 * 40 %  Word2Vec semantic neighbour
 * 30 %  Distil‑BERT MLM single‑token replacement
 * 10 %  WordNet synonym/hypernym/antonym (fallback‑style)
 * 10 %  Safe ASCII fuzz
 * 10 %  Baseline passthrough
 * <p>
 * • Never mutates path‑template parameters<br>
 * • Adds advanced numeric/boolean edge exploration using Word2Vec<br>
 * • Everything optional – branches silently skipped if asset is missing.
 */
public class SmartSemanticTestCaseGenerator extends AbstractTestCaseGenerator {

    /* ------------------------------------------------------------------ */
    private static final Logger log = LogManager.getLogger(SmartSemanticTestCaseGenerator.class);

    /* ── probability mass ------------------------------------------------ */
    private static final float P_W2V      = 0.40f;
    private static final float P_BERT     = 0.30f;
    private static final float P_WORDNET  = 0.10f;
    private static final float P_FUZZ     = 0.10f;   // remainder (0.10) = baseline

    /* ── external assets ------------------------------------------------- */
    private static final String W2V_PATH   = "src/main/resources/GoogleNews-vectors-negative300.bin";
    private static final String ONNX_PATH  = "src/main/resources/ml/model.onnx";
    private static final String VOCAB_PATH = "src/main/resources/ml/vocab.txt";

    /* ── helpers --------------------------------------------------------- */
    private final RandomStringGenerator fallback =
            new RandomStringGenerator(6, 12, true, true, true);

    private final Dictionary         wordnet;   // may be null
    private final Word2Vec           w2v;       // may be null
    private final OrtSession         bert;      // may be null
    private final Map<String,Integer> vocab;    // may be null

    private final Random rand = new Random();

    /* ------------------------------------------------------------------ */
    public SmartSemanticTestCaseGenerator(OpenAPISpecification spec,
                                          TestConfigurationObject conf,
                                          int nTests) {
        super(spec, conf, nTests);

        Dictionary wn = null;
        try { wn = Dictionary.getDefaultResourceInstance(); }
        catch (Exception e) { log.info("WordNet not found – lexical branch off."); }
        wordnet = wn;

        w2v   = loadW2v();
        OrtPair p = loadBert();
        bert  = p.session;
        vocab = p.vocab;
    }

    /* ------------------------------------------------------------------ */
    @Override
    protected Collection<TestCase> generateOperationTestCases(Operation op) {
        List<TestCase> list = new ArrayList<>();
        resetOperation();
        while (hasNext()) {
            TestCase tc = generateNextTestCase(op);
            tc.setFaulty(false);
            tc.setFaultyReason("none");
            authenticateTestCase(tc);
            list.add(tc);
            updateIndexes(tc);
        }
        return list;
    }

    @Override
    public TestCase generateNextTestCase(Operation op) {
        TestCase tc = createTestCaseTemplate(op);

        if (op.getTestParameters() != null)
            for (TestParameter p : op.getTestParameters())
                if (p.getWeight() == null || rand.nextFloat() <= p.getWeight())
                    addValue(tc, p, op);

        tc.setExpectedResponse(op.getExpectedResponse());
        return tc;
    }

    @Override
    protected boolean hasNext() { return nTests < numberOfTests; }

    /* ------------------------------------------------------------------ */
    /*  PARAMETER HANDLING                                                */
    /* ------------------------------------------------------------------ */
    private void addValue(TestCase tc, TestParameter param, Operation op) {

        /* 1 ── baseline from configured generators ---------------------- */
        String base = baselineValue(param);

        /* 2 ── numeric / boolean edge exploration ----------------------- */
        if (isNumeric(param, op)) {
            tc.addParameter(param, numericEdgeSmart(base, param.getName()));
            return;
        }
        if (isBoolean(param, op)) {
            tc.addParameter(param, booleanFlip(base));
            return;
        }

        /* 3 ── string semantic augmentation ----------------------------- */
        boolean isString = isString(param, op);
        boolean inPath   = "path".equalsIgnoreCase(param.getIn());

        String v = base;

        if (isString && !inPath) {
            float r = rand.nextFloat();

            if      (r < P_W2V && w2v != null)                        v = word2vecNeighbour(base);
            else if (r < P_W2V + P_BERT && bert != null)             v = bertReplace(base);
            else if (r < P_W2V + P_BERT + P_WORDNET && wordnet!=null) v = wordnetNeighbour(base);
            else if (r < P_W2V + P_BERT + P_WORDNET + P_FUZZ)        v = safeFuzz(base);
            /* else baseline */
        }

        tc.addParameter(param, encode(v));
    }

    private String baselineValue(TestParameter p) {
        List<ITestDataGenerator> gens =
                nominalGenerators.get(Pair.with(p.getName(), p.getIn()));
        if (gens == null || gens.isEmpty()) return fallback.nextValueAsString();
        try { return getRandomGenerator(gens).nextValueAsString(); }
        catch (Exception e) { return fallback.nextValueAsString(); }
    }

    /* ───── helpers to detect param kind -------------------------------- */
    private boolean isString(TestParameter p, Operation op) {
        OpenAPIParameter meta =
                OpenAPISpecificationVisitor.findParameterFeatures(
                        op.getOpenApiOperation(), p.getName(), p.getIn());
        return meta != null && "string".equals(meta.getType());
    }
    private boolean isNumeric(TestParameter p, Operation op) {
        OpenAPIParameter meta =
                OpenAPISpecificationVisitor.findParameterFeatures(
                        op.getOpenApiOperation(), p.getName(), p.getIn());
        return meta != null && ("integer".equals(meta.getType()) || "number".equals(meta.getType()));
    }
    private boolean isBoolean(TestParameter p, Operation op) {
        OpenAPIParameter meta =
                OpenAPISpecificationVisitor.findParameterFeatures(
                        op.getOpenApiOperation(), p.getName(), p.getIn());
        return meta != null && "boolean".equals(meta.getType());
    }

    /*  NUMERIC ENHANCEMENT                                               */
    /**
     * Classic edge exploration (±1, 0, negation) enriched with Word2Vec‑driven
     * numeric analogies. Nearest neighbours of either the number token itself
     * or the parameter name are inspected for pure‑digit tokens and
     * mixed into the candidate pool, yielding domain‑aware numeric guesses
     * (e.g., «limit» → 100, 1000, 50 …).
     */
    private String numericEdgeSmart(String seed, String paramName) {
        double v;
        try { v = Double.parseDouble(seed.trim()); }
        catch (NumberFormatException e) { return seed; }

        List<String> cands = new ArrayList<>();
        /* baseline */
        cands.add(String.valueOf(v + 1));
        cands.add(String.valueOf(v - 1));
        cands.add("0");
        cands.add(String.valueOf(-v));

        /* Word2Vec numeric intuition ---------------------------------- */
        if (w2v != null) {
            // (a) neighbours of the numeric token itself ("25" → "30", "100" …)
            String numTok = String.valueOf((long) v);
            if (w2v.hasWord(numTok))
                harvestNumericNeighbours(numTok, cands);

            // (b) neighbours of the parameter name ("limit" → "100", …)
            if (paramName != null) {
                String key = paramName.toLowerCase(Locale.ROOT);
                if (w2v.hasWord(key))
                    harvestNumericNeighbours(key, cands);
            }
        }

        if (cands.isEmpty()) return seed;
        return cands.get(rand.nextInt(cands.size()));
    }

    /** Collect up to {@code k} numeric neighbours of a Word2Vec token */
    private void harvestNumericNeighbours(String token, List<String> sink) {
        for (String n : w2v.wordsNearest(token, 8))
            if (n.matches("-?\\d+")) sink.add(n);
    }

    /* ------------------------------------------------------------------ */
    /*  BOOLEAN                                                           */
    /* ------------------------------------------------------------------ */
    private String booleanFlip(String seed) {
        if ("true".equalsIgnoreCase(seed))  return "false";
        if ("false".equalsIgnoreCase(seed)) return "true";
        return seed;
    }

    /* ------------------------------------------------------------------ */
    /*  WORDNET                                                           */
    /* ------------------------------------------------------------------ */
    private String wordnetNeighbour(String seed) {
        if (wordnet == null) return seed;

        try {
            String lower = seed.toLowerCase(Locale.ROOT);
            IndexWord iw = wordnet.lookupIndexWord(POS.NOUN, lower);
            if (iw == null) iw = wordnet.lookupIndexWord(POS.ADJECTIVE, lower);
            if (iw == null) return seed;

            List<String> cands = new ArrayList<>();

            for (Synset s : iw.getSenses()) {
                cands.addAll(s.getWords().stream()
                        .map(Word::getLemma)
                        .collect(Collectors.toList()));
                for (Pointer pointer : s.getPointers()) {
                    if (pointer.getType() == PointerType.HYPERNYM ||
                            pointer.getType() == PointerType.ANTONYM) {
                        Synset target = (Synset) pointer.getTargetSynset();
                        cands.addAll(target.getWords().stream()
                                .map(Word::getLemma)
                                .collect(Collectors.toList()));
                    }
                }
            }
            cands.removeIf(w -> w.equalsIgnoreCase(lower));
            if (!cands.isEmpty())
                return tidy(cands.get(rand.nextInt(cands.size())));

        } catch (Exception ignore) { }

        /* fallback to Word2Vec if WordNet empty */
        log.debug("WordNet empty – falling back to Word2Vec");
        return word2vecNeighbour(seed);
    }

    /* ------------------------------------------------------------------ */
    /*  WORD2VEC                                                          */
    /* ------------------------------------------------------------------ */
    private String word2vecNeighbour(String seed) {
        if (w2v == null) return seed;
        String key = seed.toLowerCase(Locale.ROOT);
        if (!w2v.hasWord(key)) return seed;
        List<String> near = new ArrayList<>(w2v.wordsNearest(key, 8));
        near.removeIf(n -> n.equalsIgnoreCase(key));
        if (near.isEmpty()) return seed;
        return tidy(near.get(rand.nextInt(near.size())));
    }

    /* ------------------------------------------------------------------ */
    /*  BERT MLM                                                          */
    /* ------------------------------------------------------------------ */
    private String bertReplace(String text) {
        try {
            List<String> toks = Wordpiece.tokenize(text, vocab);
            if (toks.size() < 3) return text;

            int idx = 1 + rand.nextInt(toks.size() - 2);
            String original = toks.get(idx);
            toks.set(idx, "[MASK]");

            try (BertTensor bt = Wordpiece.fromTokens(toks, vocab);
                 OrtSession.Result r = bert.run(
                         Map.of("input_ids", bt.ids,
                                 "attention_mask", bt.mask))) {

                float[][][] logits = (float[][][]) ((OnnxTensor) r.get(0)).getValue();
                float[] scores = logits[0][idx];
                int best = argMax(scores);

                String pred = Wordpiece.id2tok(best, vocab);
                if ("[UNK]".equals(pred)) pred = original;
                toks.set(idx, pred);
                return tidy(Wordpiece.detokenize(toks));
            }

        } catch (Exception e) {
            log.debug("BERT‑MLM fail {}", e.getMessage());
        }
        return text;
    }

    private static int argMax(float[] a) {
        int i = 0; float best = a[0];
        for (int k = 1; k < a.length; k++) if (a[k] > best) { best = a[k]; i = k; }
        return i;
    }

    /* ------------------------------------------------------------------ */
    /*  SAFE ASCII FUZZ                                                   */
    /* ------------------------------------------------------------------ */
    private String safeFuzz(String s) {
        if (s.length() < 2) return s;
        StringBuilder sb = new StringBuilder(s);
        int pos = rand.nextInt(sb.length() - 1);
        if (rand.nextBoolean()) {          // duplicate
            sb.insert(pos, sb.charAt(pos));
        } else {                           // swap
            char c = sb.charAt(pos);
            sb.setCharAt(pos, sb.charAt(pos + 1));
            sb.setCharAt(pos + 1, c);
        }
        return sb.toString();
    }

    /* ------------------------------------------------------------------ */
    /*  BODY GEN                                                          */
    /* ------------------------------------------------------------------ */
    private void fillBody(TestCase tc, TestParameter p) {
        List<ITestDataGenerator> gens =
                nominalGenerators.get(Pair.with(p.getName(), p.getIn()));
        if (gens != null && !gens.isEmpty()) {
            ITestDataGenerator g = getRandomGenerator(gens);
            if (g instanceof BodyGenerator)
                tc.addParameter(p, ((BodyGenerator) g).nextValueAsString(false));
            else if (g instanceof ObjectPerturbator)
                tc.addParameter(p, ((ObjectPerturbator) g).getRandomOriginalStringObject());
            else
                tc.addParameter(p, g.nextValueAsString());
        } else tc.addParameter(p, fallback.nextValueAsString());
    }

    /* ------------------------------------------------------------------ */
    /*  ASSET LOADING                                                     */
    /* ------------------------------------------------------------------ */
    private Word2Vec loadW2v() {
        File f = new File(W2V_PATH);
        if (!f.exists()) { log.info("Word2Vec not found."); return null; }
        try {
            log.info("Loading Word2Vec …");
            return WordVectorSerializer.readWord2VecModel(f);
        } catch (Exception e) {
            log.warn("W2V load failed {}", e.toString());
            return null;
        }
    }

    private static final class OrtPair {
        final OrtSession session; final Map<String,Integer> vocab;
        OrtPair() { session = null; vocab = null; }
        OrtPair(OrtSession s, Map<String,Integer> v) { session = s; vocab = v; }
    }
    private OrtPair loadBert() {
        Path m = Paths.get(ONNX_PATH);
        Path v = Paths.get(VOCAB_PATH);
        if (!Files.exists(m) || !Files.exists(v)) {
            log.info("BERT assets missing – MLM disabled.");
            return new OrtPair();
        }
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession sess = env.createSession(m.toString(), new OrtSession.SessionOptions());
            Map<String,Integer> vc = Wordpiece.loadVocab(v);
            log.info("Distil‑BERT ready ({} tokens).", vc.size());
            return new OrtPair(sess, vc);
        } catch (Exception e) {
            log.warn("BERT init failed {}", e.toString());
            return new OrtPair();
        }
    }

    /* ------------------------------------------------------------------ */
    /*  WORDPIECE MINI‑HELPER                                             */
    /* ------------------------------------------------------------------ */
    private static final class Wordpiece {
        static Map<String,Integer> loadVocab(Path p) throws IOException {
            Map<String,Integer> m = new HashMap<>();
            try (BufferedReader br = Files.newBufferedReader(p)) {
                String l; int idx = 0;
                while ((l = br.readLine()) != null) m.put(l.trim(), idx++);
            }
            return m;
        }

        static List<String> tokenize(String s, Map<String,Integer> v) {
            List<String> out = new ArrayList<>();
            out.add("[CLS]");
            for (String w : s.toLowerCase(Locale.ROOT).split("\\s+"))
                out.add(v.containsKey(w) ? w : "[UNK]");
            out.add("[SEP]");
            return out;
        }

        static BertTensor fromTokens(List<String> toks, Map<String,Integer> v) throws OrtException {
            long[] ids = toks.stream().mapToLong(t -> v.getOrDefault(t, 100)).toArray();
            long[][] idMat = new long[1][ids.length];
            long[][] mask  = new long[1][ids.length];
            for (int i = 0; i < ids.length; i++) {
                idMat[0][i] = ids[i];
                mask [0][i] = 1;
            }
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            return new BertTensor(
                    OnnxTensor.createTensor(env, idMat),
                    OnnxTensor.createTensor(env, mask));
        }

        static String detokenize(List<String> t) {
            StringBuilder sb = new StringBuilder();
            for (String tok : t)
                if (!tok.equals("[CLS]") && !tok.equals("[SEP]"))
                    sb.append(tok.startsWith("##") ? tok.substring(2) : " " + tok);
            return sb.toString().trim();
        }

        static String id2tok(int id, Map<String,Integer> v) {
            for (Map.Entry<String,Integer> e : v.entrySet())
                if (e.getValue() == id) return e.getKey();
            return "[UNK]";
        }
    }

    private static final class BertTensor implements AutoCloseable {
        final OnnxTensor ids, mask;
        BertTensor(OnnxTensor ids, OnnxTensor mask) { this.ids = ids; this.mask = mask; }
        @Override public void close() throws OrtException { ids.close(); mask.close(); }
    }

    /* ------------------------------------------------------------------ */
    /*  STRING TIDY / ENCODE                                              */
    /* ------------------------------------------------------------------ */
    private static String tidy(String raw) {
        return decodeAll(raw).replace('_', ' ');
    }
    private static String decodeAll(String s) {
        String p = s;
        while (true) {
            String n = URLDecoder.decode(p, StandardCharsets.UTF_8);
            if (n.equals(p)) return p;
            p = n;
        }
    }
    private static String encode(String raw) {
        String ascii = Normalizer.normalize(raw, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return URLEncoder.encode(ascii, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
