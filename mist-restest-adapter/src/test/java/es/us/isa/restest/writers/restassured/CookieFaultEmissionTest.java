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
 * {@code req.cookie(name, invalidValue)} line for negative variants whose
 * {@code targetFaultParamLocation} is {@code cookie}.
 *
 * <p>Companion to {@link HeaderFaultEmissionTest}; same strategy (Option C —
 * write to a temp dir and grep the generated Java source), different
 * parameter location.
 *
 * <p>The fault path additionally exercises the "carrier-less fault emission"
 * branch: the step's {@code cookies} map is left EMPTY so the only way a
 * {@code .cookie(...)} call can appear in the output is via the writer's
 * fallback emission for header/cookie fault targets that have no positive
 * entry to replace.
 */
public class CookieFaultEmissionTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String SPEC_PATH = "src/test/resources/specifications/petstore.json";
    private static final String CONF_PATH = "src/test/resources/Petstore/fullConf.yaml";

    private static MultiServiceTestCase buildFaultyCookieCase() {
        Operation method = new Operation();
        method.setMethod("get");
        method.setTestPath("/secure/data");
        method.setOperationId("getSecureData");

        MultiServiceTestCase.StepCall step = new MultiServiceTestCase.StepCall(
                "secure-service", method, "/secure/data",
                /*pathParams */ new LinkedHashMap<>(),
                /*queryParams*/ new LinkedHashMap<>(),
                /*headers    */ new LinkedHashMap<>(),
                /*body       */ null,
                /*expected   */ 200,
                /*bodyFields */ new LinkedHashMap<>());
        step.setTopLevelRoot(true);
        step.setHierarchicalId("R1");
        // Pre-seed the cookies map so the writer's cookie-loop iterates over it
        // and the replace-with-invalid branch fires.
        step.getCookies().put("sessionId", "valid-session-cookie");

        MultiServiceTestCase tc = new MultiServiceTestCase("getSecureData_negative_v1");
        tc.setFaulty(true);
        tc.setScenarioName("CookieFaultScenario");
        tc.setTargetFaultParamLocation("cookie");
        tc.addFaultyParameter("sessionId", "__invalid_cookie_value__");
        tc.setFaultTypeCategory("SEMANTIC_MISMATCH");
        tc.setTargetFaultRootId("Root 1");
        tc.setTargetFaultRootApiPath("GET /secure/data");
        tc.addStepCall(step);
        tc.setMethod(HttpMethod.GET);
        return tc;
    }

    @Test
    public void writerEmitsInvalidCookieValueForCookieFaultVariant() throws Exception {
        String className = "CookieFaultEmissionSuite";
        String pkgName = "gen";
        File outDir = tmp.newFolder("out");

        MultiServiceRESTAssuredWriter writer = new MultiServiceRESTAssuredWriter(
                SPEC_PATH, CONF_PATH,
                outDir.getAbsolutePath(),
                className,
                pkgName,
                "http://localhost",
                false);
        // The header/cookie fault emission block is nested inside the writer's
        // Allure-reporting branch; without this flag the step body is never
        // produced and no .cookie(...) lines are emitted at all.
        writer.setAllureReport(true);

        List<TestCase> testCases = new ArrayList<>();
        testCases.add(buildFaultyCookieCase());
        writer.write(testCases);

        File generated = new File(outDir,
                pkgName + "/" + className + "/CookieFaultScenario.java");
        assertTrue("Writer must produce " + generated.getAbsolutePath(), generated.exists());

        String body = new String(Files.readAllBytes(generated.toPath()), StandardCharsets.UTF_8);

        // The core assertion: the invalid value must appear inside a .cookie(...) call.
        String expected = "req = req.cookie(\"sessionId\", \"__invalid_cookie_value__\");";
        assertTrue("Generated test must include the invalid cookie substitution:\n  expected> "
                        + expected,
                body.contains(expected));

        // The original valid cookie value must NOT also be emitted — Sniper invariant.
        String unwanted = "req = req.cookie(\"sessionId\", \"valid-session-cookie\");";
        assertFalse("Original valid cookie value must be replaced, not also emitted:\n  unwanted> "
                        + unwanted,
                body.contains(unwanted));
    }
}
