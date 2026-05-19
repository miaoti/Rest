package es.us.isa.restest.generators;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A custom generator that only uses your LLMParameterGenerator-based approach,
 * ignoring any built-in fuzzing, constraint-based logic, etc.
 */
public class LLMOnlyTestCaseGenerator extends AbstractTestCaseGenerator {

    public LLMOnlyTestCaseGenerator(OpenAPISpecification spec,
                                    TestConfigurationObject conf,
                                    int nTests) {
        super(spec, conf, nTests);
    }

    @Override
    protected Collection<TestCase> generateOperationTestCases(Operation testOperation) throws RESTestException {
        List<TestCase> testCases = new ArrayList<>();

        // Reset counters for this operation
        resetOperation();

        // For example, if we want 'numberOfTests' test cases:
        while (hasNext()) {
            TestCase test = generateNextTestCase(testOperation);
            // Set authentication data, if needed
            authenticateTestCase(test);

            testCases.add(test);
            updateIndexes(test);
        }

        return testCases;
    }

    @Override
    public TestCase generateNextTestCase(Operation testOperation) throws RESTestException {
        TestCase test = generateLLMTestCase(testOperation);

        // Pull the expected status from the testOperation's config:
        test.setExpectedResponse(testOperation.getExpectedResponse());

        test.setFaulty(false);
        test.setFaultyReason("none");

        checkTestCaseValidity(test);
        return test;
    }


    /**
     * Generate a single LLM-based test case, using the nominalGenerators
     * that presumably contain "LLMParameterGenerator" for each param.
     */
    private TestCase generateLLMTestCase(Operation testOperation) {
        // 1) Create the test template
        TestCase test = createTestCaseTemplate(testOperation);

        // 2) For each parameter in the OAS, pick an LLM generator from the 'nominalGenerators' map
        if (testOperation.getTestParameters() != null) {
            for (TestParameter confParam : testOperation.getTestParameters()) {
                // If param is optional, we might skip it randomly if weight < 1
                if (confParam.getWeight() == null || rand.nextFloat() <= confParam.getWeight()) {
                    // find the generator
                    List<es.us.isa.restest.inputs.ITestDataGenerator> genList =
                            nominalGenerators.get(Pair.with(confParam.getName(), confParam.getIn()));
                    if (genList != null && !genList.isEmpty()) {
                        es.us.isa.restest.inputs.ITestDataGenerator generator = getRandomGenerator(genList);
                        // call nextValueAsString to get LLM-based value
                        String value = generator.nextValueAsString();
                        test.addParameter(confParam, value);
                    }
                }
            }
        }

        return test;
    }

    @Override
    protected boolean hasNext() {
        // We'll keep generating until we reach 'numberOfTests' for this operation
        return nTests < numberOfTests;
    }
}
