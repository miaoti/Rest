package es.us.isa.restest.generators;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.inputs.ITestDataGenerator;
import es.us.isa.restest.inputs.perturbation.ObjectPerturbator;
import es.us.isa.restest.inputs.random.RandomStringGenerator;
import es.us.isa.restest.inputs.stateful.BodyGenerator;
import es.us.isa.restest.specification.OpenAPIParameter;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.specification.OpenAPISpecificationVisitor;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import io.swagger.v3.oas.models.media.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.javatuples.Pair;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Word2Vec-based test generator that:
 *   - For each parameter, we pick *one* generator from `nominalGenerators` at random
 *   - If the param is string and we have a local Word2Vec model, we can create synonyms.
 *   - This means if `LLMParameterGenerator` also appears in `nominalGenerators`, we might
 *     call LLM multiple times across multiple tests – not just once.
 *   - Non-string params (numbers, booleans, etc.) remain unaffected.
 */
public class Word2VecTestCaseGenerator extends AbstractTestCaseGenerator {

    private static final Logger logger = LogManager.getLogger(Word2VecTestCaseGenerator.class);

    // Paths to your local GoogleNews word2vec
    private static final String LOCAL_MODEL_BIN = "src/main/resources/GoogleNews-vectors-negative300.bin";

    private Word2Vec word2Vec;           // The loaded Word2Vec model
    private final RandomStringGenerator fallbackString;

    public Word2VecTestCaseGenerator(OpenAPISpecification spec,
                                     TestConfigurationObject conf,
                                     int nTests) {
        super(spec, conf, nTests);

        // Load local Word2Vec (if present)
        this.word2Vec = loadWord2VecLocalModel();
        this.fallbackString = new RandomStringGenerator(5,10,true,true,true);
    }

    private Word2Vec loadWord2VecLocalModel() {
        File bin = new File(LOCAL_MODEL_BIN);
        if(!bin.exists()) {
            logger.warn("Word2Vec .bin not found at: " + bin.getAbsolutePath() + ". Will fallback always.");
            return null;
        }
        try {
            logger.info("Loading Word2Vec from " + bin.getAbsolutePath());
            Word2Vec model = WordVectorSerializer.readWord2VecModel(bin);
            logger.info("Word2Vec loaded successfully!");
            return model;
        } catch(Exception e) {
            logger.error("Error reading Word2Vec model: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected Collection<TestCase> generateOperationTestCases(Operation testOperation) throws RESTestException {
        List<TestCase> testCases = new ArrayList<>();
        resetOperation();

        // e.g. we want `numberOfTests` test cases
        while(hasNext()) {
            TestCase test = generateNextTestCase(testOperation);
            // Possibly we set test to non-faulty, depends on your logic
            test.setFaulty(false);
            test.setFaultyReason("none");

            authenticateTestCase(test);
            testCases.add(test);
            updateIndexes(test);
        }
        return testCases;
    }

    @Override
    public TestCase generateNextTestCase(Operation testOperation) throws RESTestException {
        // 1) create blank test
        TestCase tc = createTestCaseTemplate(testOperation);

        // 2) fill param values from nominalGenerators
        if(testOperation.getTestParameters() != null) {
            for (TestParameter param : testOperation.getTestParameters()) {
                if (param.getWeight() == null || rand.nextFloat() <= param.getWeight()) {
                    fillParameter(tc, param, testOperation);
                }
            }
        }

        // Optionally set the expected response from config
        tc.setExpectedResponse(testOperation.getExpectedResponse());

        // Optionally call checkTestCaseValidity(tc) here if you want
        return tc;
    }

    /**
     * For each param, we pick *one* generator from nominalGenerators if available.
     * Then we call nextValueAsString().
     * If it's a "string" param, we can do an optional Word2Vec approach if we want,
     * or just call the existing generator.
     */
    private void fillParameter(TestCase test, TestParameter param, Operation op) {
        // get the list of possible generators for (paramName, paramIn)
        List<ITestDataGenerator> genList = nominalGenerators.get(Pair.with(param.getName(), param.getIn()));

        if("body".equals(param.getIn())) {
            // If it's body param, let's do a special approach
            fillBodyParameter(test, param, genList);
            return;
        }

        // Otherwise for normal param: pick one generator at random
        if(genList != null && !genList.isEmpty()) {
            ITestDataGenerator chosen = getRandomGenerator(genList);

            // If it's "LLMParameterGenerator", "RandomNumber", or anything else, we'll let it do nextValueAsString
            // But if param is "string" type, we can do a synergy approach: 50% chance we override with word2vec synonyms
            OpenAPIParameter paramFeatures = OpenAPISpecificationVisitor.findParameterFeatures(op.getOpenApiOperation(), param.getName(), param.getIn());
            if(paramFeatures != null && "string".equals(paramFeatures.getType())) {
                if(word2Vec != null && rand.nextFloat() < 0.5f) {
                    String w2vVal = pickWord2VecSynonym(param.getName());
                    test.addParameter(param, w2vVal);
                    return;
                }
            }

            // else just use the chosen generator
            String val = chosen.nextValueAsString();
            test.addParameter(param, val);

        } else {
            // fallback if no generator found
            test.addParameter(param, fallbackString.nextValueAsString());
        }
    }

    /**
     * If there's a BodyGenerator or something in genList, we pick it,
     * else fallback to some random string for the body.
     */
    private void fillBodyParameter(TestCase test, TestParameter param, List<ITestDataGenerator> genList) {
        if(genList != null && !genList.isEmpty()) {
            ITestDataGenerator chosen = getRandomGenerator(genList);
            if(chosen instanceof BodyGenerator) {
                // we pass 'false' => not mutated
                test.addParameter(param, ((BodyGenerator) chosen).nextValueAsString(false));
            } else if(chosen instanceof ObjectPerturbator) {
                test.addParameter(param, ((ObjectPerturbator) chosen).getRandomOriginalStringObject());
            } else {
                // normal fallback
                test.addParameter(param, chosen.nextValueAsString());
            }
        } else {
            // fallback
            test.addParameter(param, fallbackString.nextValueAsString());
        }
    }

    private String pickWord2VecSynonym(String paramName) {
        if (word2Vec == null) {
            return fallbackString.nextValueAsString();
        }
        String lower = paramName.toLowerCase(Locale.ROOT);

        if (!word2Vec.hasWord(lower)) {
            return fallbackString.nextValueAsString();
        }

        try {
            Collection<String> synonyms = word2Vec.wordsNearest(lower, 10);
            if (!synonyms.isEmpty()) {
                List<String> synList = new ArrayList<>(synonyms);
                // pick random from synonyms
                String chosen = synList.get(rand.nextInt(synList.size()));
                // Attempt to decode underscores and URL escaping
                String decoded = decodeUntilNoChange(chosen);

                // Then replace underscores with space
                decoded = decoded.replace('_', ' ');

                return decoded;
            }
        } catch (Exception e) {
            logger.error("Error in Word2Vec for " + lower + ": " + e.getMessage());
        }
        return fallbackString.nextValueAsString();
    }

    /**
     * Decode a string repeatedly with URLDecoder until it stops changing –
     * e.g., turning "Bishkek%2520Otunbayeva" -> "Bishkek%20Otunbayeva" -> "Bishkek Otunbayeva".
     */
    private String decodeUntilNoChange(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String prev = input;
        while (true) {
            // decode once
            String attempt = java.net.URLDecoder.decode(prev, java.nio.charset.StandardCharsets.UTF_8);

            // if it didn't change, break
            if (attempt.equals(prev)) {
                break;
            }
            // else keep decoding
            prev = attempt;
        }
        return prev;
    }


    @Override
    protected boolean hasNext() {
        return nTests < numberOfTests;
    }
}
