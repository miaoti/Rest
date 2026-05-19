package es.us.isa.restest.writers.restassured;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import es.us.isa.restest.testcases.TestCase;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Verifies that {@link MultiServiceRESTAssuredWriter} emits the Trace Shape
 * Oracle bootstrap, {@code Allure.addAttachment("Trace Shape Oracle Verdict",
 * ...)}, and the negative-variant FAIL→PASS branch into the generated
 * test class. This is the acceptance check Prompt 1 calls out: without a
 * live cluster, the demo can be skipped if the generated source carries the
 * expected emission.
 */
public class TraceShapeOracleEmissionTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String SPEC_PATH = "src/test/resources/specifications/petstore.json";
    private static final String CONF_PATH = "src/test/resources/Petstore/fullConf.yaml";

    private static MultiServiceTestCase faultyCase() {
        Operation method = new Operation();
        method.setMethod("get");
        method.setTestPath("/pet/findByStatus");
        method.setOperationId("findPetsByStatus");
        MultiServiceTestCase.StepCall step = new MultiServiceTestCase.StepCall(
                "pet-service", method, "/pet/findByStatus",
                new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(),
                null, 200, new LinkedHashMap<>());
        step.setTopLevelRoot(true);
        step.setHierarchicalId("R1");

        MultiServiceTestCase tc = new MultiServiceTestCase("findPetsByStatus_negative");
        tc.setFaulty(true);
        tc.setScenarioName("OraclePathNegative");
        tc.setFaultTypeCategory("SEMANTIC_MISMATCH");
        tc.setTargetFaultRootId("Root 1");
        tc.setTargetFaultRootApiPath("GET /pet/findByStatus");
        tc.addStepCall(step);
        tc.setMethod(HttpMethod.GET);
        return tc;
    }

    private static MultiServiceTestCase positiveCase() {
        Operation method = new Operation();
        method.setMethod("get");
        method.setTestPath("/pet/findByStatus");
        method.setOperationId("findPetsByStatus");
        MultiServiceTestCase.StepCall step = new MultiServiceTestCase.StepCall(
                "pet-service", method, "/pet/findByStatus",
                new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(),
                null, 200, new LinkedHashMap<>());
        step.setTopLevelRoot(true);
        step.setHierarchicalId("R1");
        MultiServiceTestCase tc = new MultiServiceTestCase("findPetsByStatus_positive");
        tc.setFaulty(false);
        tc.setScenarioName("OraclePathPositive");
        tc.addStepCall(step);
        tc.setMethod(HttpMethod.GET);
        return tc;
    }

    @Test
    public void writerEmitsTraceShapeOracleBlocks() throws Exception {
        File outDir = tmp.newFolder("out");
        MultiServiceRESTAssuredWriter w = new MultiServiceRESTAssuredWriter(
                SPEC_PATH, CONF_PATH, outDir.getAbsolutePath(),
                "OracleEmissionSuite", "gen", "http://localhost", false);
        w.setAllureReport(true); // oracle emission is gated by allureReport
        // No setTraceShapeOracle(...) call → writer falls back to ShapeInvariantStore()
        // default-path construction in the generated test code, exercising that branch.

        List<TestCase> cases = new ArrayList<>();
        cases.add(faultyCase());
        cases.add(positiveCase());
        w.write(cases);

        File faulty = new File(outDir, "gen/OracleEmissionSuite/OraclePathNegative.java");
        File pos = new File(outDir, "gen/OracleEmissionSuite/OraclePathPositive.java");
        assertTrue("negative variant file produced: " + faulty, faulty.exists());
        assertTrue("positive variant file produced: " + pos, pos.exists());

        String faultyBody = new String(Files.readAllBytes(faulty.toPath()), StandardCharsets.UTF_8);
        String posBody = new String(Files.readAllBytes(pos.toPath()), StandardCharsets.UTF_8);

        // 1) Imports
        assertTrue("import TraceShapeAdapter",
                faultyBody.contains("import es.us.isa.restest.analysis.TraceShapeAdapter;"));
        assertTrue("import TraceShapeOracle",
                faultyBody.contains("import io.mist.core.oracle.shape.TraceShapeOracle;"));

        // 2) Static fields
        assertTrue("private static TraceShapeOracle oracle field",
                faultyBody.contains("private static TraceShapeOracle oracle;"));
        assertTrue("LAST_VERDICT ThreadLocal field",
                faultyBody.contains("ThreadLocal<TraceShapeVerdict> LAST_VERDICT"));

        // 3) @BeforeClass bootstrap
        assertTrue("@BeforeClass should construct the oracle from the persisted store",
                faultyBody.contains("oracle = new TraceShapeOracle(new ShapeInvariantStore("));

        // 4) Allure attachment of verdict — the contractual emission Prompt 1 calls out
        String mustContainVerdictAttachment = "Allure.addAttachment(\"Trace Shape Oracle Verdict\"";
        assertTrue("Allure attachment of Trace Shape Oracle Verdict",
                faultyBody.contains(mustContainVerdictAttachment));
        assertTrue("Same attachment in positive variant",
                posBody.contains(mustContainVerdictAttachment));

        // 5) oracle.evaluate(model, rootApiKey) call
        assertTrue("oracle.evaluate(model, rootApiKey) call",
                faultyBody.contains("oracle.evaluate(model, rootApiKey)"));

        // 6) Negative variant must emit the FAIL→PASS flip on RESPONSE_ENVELOPE violation.
        assertTrue("negative variant has FAIL->PASS branch for RESPONSE_ENVELOPE",
                faultyBody.contains("RESPONSE_ENVELOPE")
                        && faultyBody.contains("negative variant PASSED via ResponseEnvelopeInvariant"));

        // 7) Positive variant must NOT carry the negative-variant flip.
        assertFalse("positive variant must not flip on RESPONSE_ENVELOPE",
                posBody.contains("negative variant PASSED via ResponseEnvelopeInvariant"));

        // 8) Positive variant must carry the explicit fail-on-violation branch.
        assertTrue("positive variant has fail-on-any-violation branch",
                posBody.contains("Positive variant failed — Trace Shape Oracle verdict has violation"));
    }
}
