package es.us.isa.restest.generators;

import es.us.isa.restest.configuration.pojos.*;
import es.us.isa.restest.inputs.random.RandomStringGenerator;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.specification.OpenAPISpecificationVisitor;
import es.us.isa.restest.specification.OpenAPIParameter;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Decorator that corrupts a fraction of the test-cases produced by an inner
 * generator so they violate *inferred* inter-parameter dependencies.
 *
 * It combines two strategies:
 *   1. heuristic name matching  →  discovers likely dependency pairs
 *   2. light schema introspection →  crafts a value that is definitely wrong
 *
 */
public class SchemaGuidedDependencyMutator extends AbstractTestCaseGenerator {

    /* ───────── configuration ───────── */

    /** if {@code 0.15} then 15 % of the generated tests will be corrupted */
    private static final float DEFAULT_P_FAULT = 0.15f;

    /** literal pools for string parameters (fallback when nothing smarter helps) */
    private static final Map<String, String[]> BAD_LITERAL = Map.ofEntries(
            Map.entry("currency",  new String[]{"ZZZ", "XXX", "BAD"}),
            Map.entry("country",   new String[]{"Atlantis", "Neverland"}),
            Map.entry("code",      new String[]{"000", "999"}),
            Map.entry("lang",      new String[]{"zz", "xx"}),
            Map.entry("language",  new String[]{"zz", "xx"}),
            Map.entry("email",     new String[]{"not-an-email", "user@@"}),
            Map.entry("phone",     new String[]{"+0000", "123"}),
            Map.entry("url",       new String[]{"http://", "invalid://"}),
            Map.entry("postal",    new String[]{"00000", "XXXXX"}),
            Map.entry("zip",       new String[]{"00000", "XXXXX"})
    );

    private static final Logger log =
            LogManager.getLogger(SchemaGuidedDependencyMutator.class);

    /* ───────── fields ───────── */
    private final AbstractTestCaseGenerator inner;     // wrapped generator
    private final float pFault;                        // corruption probability
    private final RandomStringGenerator fuzz =
            new RandomStringGenerator(6, 12, true, true, true);

    /* ───────── ctor ───────── */
    public SchemaGuidedDependencyMutator(AbstractTestCaseGenerator base,
                                         OpenAPISpecification spec,
                                         TestConfigurationObject conf,
                                         int nTests,
                                         float faultyProb) {
        super(spec, conf, nTests);
        this.inner  = base;
        this.pFault = Math.max(0f, Math.min(1f, faultyProb));
    }
    public SchemaGuidedDependencyMutator(AbstractTestCaseGenerator base,
                                         OpenAPISpecification spec,
                                         TestConfigurationObject conf,
                                         int nTests) {
        this(base, spec, conf, nTests, DEFAULT_P_FAULT);
    }

    /* ───────── generation pipeline ───────── */

    @Override
    protected Collection<TestCase> generateOperationTestCases(Operation op)
            throws RESTestException {

        /* forward generator-specific initialisation */
        inner.createGenerators(op);

        // 1️  let the inner generator create its nominal tests
        Collection<TestCase> produced = inner.generateOperationTestCases(op);

        // 2  discover candidate dependency pairs
        List<DepLink> deps = discoverDependencies(op);
        if (deps.isEmpty()) return produced;

        // 3️  corrupt some of the tests
        for (TestCase tc : produced)
            if (rand.nextFloat() < pFault)
                injectFault(tc, op, deps);

        return produced;
    }

    /* iteration bookkeeping is still handled by the wrapped generator */
    @Override public TestCase generateNextTestCase(Operation op) {
        throw new IllegalStateException(
                "generateNextTestCase is unused – we override the full list");
    }
    @Override protected boolean hasNext() { return inner.hasNext(); }

    /* ═════════════════ helper logic ═════════════════ */

    /** simple holder for a dependency candidate (two parameters) */
    private static final class DepLink {
        final TestParameter a, b;
        DepLink(TestParameter a, TestParameter b) { this.a = a; this.b = b; }
    }

    /* -------- discover A↔B dependencies in a single Operation ---------- */
    private List<DepLink> discoverDependencies(Operation op) {

        List<DepLink> out = new ArrayList<>();
        List<TestParameter> ps = op.getTestParameters();
        if (ps == null || ps.size() < 2) return out;

        /* map names → parameters (lower-case) for quick search */
        Map<String, TestParameter> byName = new HashMap<>();
        for (TestParameter p : ps)
            byName.put(p.getName().toLowerCase(Locale.ROOT), p);

        /* 1. vocabulary-based pairs (heuristic name matching) */
        pick(out, byName, "currency", "country");
        pick(out, byName, "code",     "language");
        pick(out, byName, "code",     "lang");
        pick(out, byName, "region",   "capital");
        pick(out, byName, "postal",   "country");
        pick(out, byName, "zip",      "country");
        pick(out, byName, "email",    "user");
        pick(out, byName, "email",    "username");

        /* 2. range / order pairs – share a common suffix */
        rangePick(out, byName, "start", "end");
        rangePick(out, byName, "from",  "to");
        rangePick(out, byName, "min",   "max");
        rangePick(out, byName, "before","after");

        /* 3. geo coordinates */
        pick(out, byName, "lat", "lon");
        pick(out, byName, "latitude", "longitude");

        return out;
    }
    private static void pick(List<DepLink> bag,
                             Map<String, TestParameter> map,
                             String k1, String k2) {

        TestParameter p1 = find(map, k1);
        TestParameter p2 = find(map, k2);
        if (p1 != null && p2 != null)
            bag.add(new DepLink(p1, p2));
    }
    /** matches prefixes with a shared suffix: startDate ↔ endDate, min_age ↔ max_age … */
    private static void rangePick(List<DepLink> bag,
                                  Map<String, TestParameter> map,
                                  String startKey, String endKey) {

        for (TestParameter p : map.values()) {
            String n = p.getName().toLowerCase(Locale.ROOT);
            if (n.startsWith(startKey)) {
                String suffix = n.substring(startKey.length());        // "", "_date", …, keeps the underscore
                TestParameter e = find(map, endKey + suffix);
                if (e != null) { bag.add(new DepLink(p, e)); break; }
            }
        }
    }
    private static TestParameter find(Map<String, TestParameter> map, String kw) {
        for (var e : map.entrySet())
            if (e.getKey().contains(kw)) return e.getValue();
        return null;
    }

    /* -------- inject a single fault into a TestCase -------------------- */
    private void injectFault(TestCase tc,
                             Operation op,
                             List<DepLink> deps) {

        DepLink link = deps.get(rand.nextInt(deps.size()));
        /* randomly decide which side to break */
        TestParameter target = rand.nextBoolean() ? link.a : link.b;

        String badVal = craftBadValue(op, target);
        tc.addParameter(target, badVal);

        tc.setFaulty(true);
        tc.setFaultyReason("dependency");
        log.debug("Injected dependency fault: {} = {}", target.getName(), badVal);
    }

    /* -------- craft a deliberately wrong value ------------------------- */
    private String craftBadValue(Operation op, TestParameter tgt) {

        /* ①   enum parameters → return a token outside the enum set */
        OpenAPIParameter feat = OpenAPISpecificationVisitor.findParameterFeatures(
                op.getOpenApiOperation(), tgt.getName(), tgt.getIn());

        if (feat != null &&
                feat.getEnumValues() != null &&
                !feat.getEnumValues().isEmpty())
            return "INVALID_ENUM";

        String name = tgt.getName().toLowerCase(Locale.ROOT);

        /* ②   date / time order pairs – break chronology */
        if (name.contains("start") || name.contains("from") ||
                name.contains("before")|| name.contains("min"))
            return LocalDate.now().plusYears(50)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);

        if (name.contains("end") || name.contains("to") ||
                name.contains("after")|| name.contains("max"))
            return LocalDate.now().minusYears(50)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);

        /* ③   latitude / longitude – go out of range */
        if (name.contains("lat")) return "999";
        if (name.contains("lon")) return "999";

        /* ④   fall back to literal pools */
        for (var e : BAD_LITERAL.entrySet())
            if (name.contains(e.getKey()))
                return e.getValue()[rand.nextInt(e.getValue().length)];

        /* ⑤   last resort – totally random string */
        return fuzz.nextValueAsString();
    }
}
