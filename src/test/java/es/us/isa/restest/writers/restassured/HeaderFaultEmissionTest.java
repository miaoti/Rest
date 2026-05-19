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
 * End-to-end verification that {@link MultiServiceRESTAssuredWriter} emits a
 * {@code req.header(name, invalidValue)} line for negative variants whose
 * {@code targetFaultParamLocation} is {@code header}.
 *
 * <p>Previously, header-located fault values were enrolled in the queue but
 * silently dropped at emission time, so a "negative" test sent only valid
 * values for the parameter under test. This test pins down the new behaviour
 * by writing a minimal {@link MultiServiceTestCase} through the real writer
 * into a temporary directory and inspecting the generated Java source.
 *
 * <p>Strategy <b>C</b> (end-to-end + grep) — chosen over Option A because the
 * writer's seam is deep inside {@code writeTestSuite}; reaching it via a real
 * call gives the most realistic assertion with minimal mocking.
 */
public class HeaderFaultEmissionTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String SPEC_PATH = "src/test/resources/specifications/petstore.json";
    private static final String CONF_PATH = "src/test/resources/Petstore/fullConf.yaml";

    /**
     * Build a one-step {@link MultiServiceTestCase} where the targeted fault
     * parameter is the header {@code X-API-Key} and the invalid value is
     * {@code __invalid_header_value__}. The first step carries the header in
     * its {@code headers} map so the writer's header-loop hits the replace
     * branch.
     */
    private static MultiServiceTestCase buildFaultyHeaderCase() {
        Operation method = new Operation();
        method.setMethod("get");
        method.setTestPath("/secure/data");
        method.setOperationId("getSecureData");

        // Positive value for X-API-Key is provided so the loop iterates and the
        // replace-with-invalid branch fires. The bug being fixed is that the
        // invalid value used to be discarded at this point.
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("X-API-Key", "valid-key-value");

        MultiServiceTestCase.StepCall step = new MultiServiceTestCase.StepCall(
                "secure-service", method, "/secure/data",
                /*pathParams */ new LinkedHashMap<>(),
                /*queryParams*/ new LinkedHashMap<>(),
                /*headers    */ headers,
                /*body       */ null,
                /*expected   */ 200,
                /*bodyFields */ new LinkedHashMap<>());
        // Top-level root flag must be true: the fault-emission block guards on
        // step.isTopLevelRoot() so only root steps get the substitution.
        step.setTopLevelRoot(true);
        step.setHierarchicalId("R1");

        MultiServiceTestCase tc = new MultiServiceTestCase("getSecureData_negative_v1");
        tc.setFaulty(true);
        tc.setScenarioName("HeaderFaultScenario");
        tc.setTargetFaultParamLocation("header");
        tc.addFaultyParameter("X-API-Key", "__invalid_header_value__");
        tc.setFaultTypeCategory("OVERFLOW");
        tc.setTargetFaultRootId("Root 1");
        tc.setTargetFaultRootApiPath("GET /secure/data");
        tc.addStepCall(step);
        // Use POST as dummy super-class verb; only the step's verb matters for emission.
        tc.setMethod(HttpMethod.GET);
        return tc;
    }

    @Test
    public void writerEmitsInvalidHeaderValueForHeaderFaultVariant() throws Exception {
        String className = "HeaderFaultEmissionSuite";
        String pkgName = "gen";
        File outDir = tmp.newFolder("out");

        MultiServiceRESTAssuredWriter writer = new MultiServiceRESTAssuredWriter(
                SPEC_PATH, CONF_PATH,
                outDir.getAbsolutePath(),
                className,
                pkgName,
                "http://localhost",
                false /*logToFile*/);
        // The header/cookie fault emission block is nested inside the writer's
        // Allure-reporting branch; without this flag the step body is never
        // produced and no .header(...) lines are emitted at all.
        writer.setAllureReport(true);

        List<TestCase> testCases = new ArrayList<>();
        testCases.add(buildFaultyHeaderCase());
        writer.write(testCases);

        // The writer lays files out as <outDir>/<packagePath>/<className>/<scenarioFile>.java
        File generated = new File(outDir,
                pkgName + "/" + className + "/HeaderFaultScenario.java");
        assertTrue("Writer must produce " + generated.getAbsolutePath(), generated.exists());

        String body = new String(Files.readAllBytes(generated.toPath()), StandardCharsets.UTF_8);

        // The core assertion: the invalid value must appear inside a .header(...) call,
        // not just as a comment or a string literal in some Allure metadata. The literal
        // form emitted by the writer is `req = req.header("X-API-Key", "<invalidValue>");`.
        String expected = "req = req.header(\"X-API-Key\", \"__invalid_header_value__\");";
        assertTrue("Generated test must include the invalid header substitution:\n  expected> "
                        + expected + "\n  --- file head ---\n"
                        + body.substring(0, Math.min(body.length(), 2000)),
                body.contains(expected));

        // The original *valid* value must NOT also be emitted in a header call for the
        // same header name — that would be a double emission (Sniper invariant: exactly
        // one substitution; the original value is replaced, not appended).
        String unwanted = "req = req.header(\"X-API-Key\", \"valid-key-value\");";
        assertFalse("Original valid header value must be replaced, not also emitted:\n  unwanted> "
                        + unwanted,
                body.contains(unwanted));
    }
}
