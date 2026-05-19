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
 * Pins the symmetry between the success-path and failure-path Jaeger
 * propagation delays. Both paths must consult the same
 * {@code mst.test.jaeger.propagation.delay.ms} system property and apply
 * the same per-step pause before calling {@code attachJaegerTrace}; a
 * regression to a hardcoded {@code Thread.sleep(3000)} on either side
 * re-introduces the per-step idle floor that dominated 16h+ runs prior
 * to this fix.
 */
public class JaegerPropagationDelaySymmetryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String SPEC_PATH = "src/test/resources/specifications/petstore.json";
    private static final String CONF_PATH = "src/test/resources/Petstore/fullConf.yaml";

    private static MultiServiceTestCase singleStepCase(String name, boolean faulty) {
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

        MultiServiceTestCase tc = new MultiServiceTestCase(name);
        tc.setFaulty(faulty);
        tc.setScenarioName(name);
        tc.addStepCall(step);
        tc.setMethod(HttpMethod.GET);
        return tc;
    }

    @Test
    public void successAndFailurePathsUseSameConfigurableDelay() throws Exception {
        File outDir = tmp.newFolder("out");
        MultiServiceRESTAssuredWriter w = new MultiServiceRESTAssuredWriter(
                SPEC_PATH, CONF_PATH, outDir.getAbsolutePath(),
                "JaegerDelaySymmetrySuite", "gen", "http://localhost", false);
        w.setAllureReport(true);

        List<TestCase> cases = new ArrayList<>();
        cases.add(singleStepCase("DelayPositive", false));
        cases.add(singleStepCase("DelayNegative", true));
        w.write(cases);

        File pos = new File(outDir, "gen/JaegerDelaySymmetrySuite/DelayPositive.java");
        File neg = new File(outDir, "gen/JaegerDelaySymmetrySuite/DelayNegative.java");
        assertTrue("positive emitted: " + pos, pos.exists());
        assertTrue("negative emitted: " + neg, neg.exists());

        String posBody = new String(Files.readAllBytes(pos.toPath()), StandardCharsets.UTF_8);
        String negBody = new String(Files.readAllBytes(neg.toPath()), StandardCharsets.UTF_8);

        String propertyName = "mst.test.jaeger.propagation.delay.ms";

        // Both paths read the same system property.
        assertTrue("positive reads jaeger propagation delay property",
                posBody.contains(propertyName));
        assertTrue("negative reads jaeger propagation delay property",
                negBody.contains(propertyName));

        // Both paths use the same configurable local instead of a literal.
        assertTrue("positive sleeps on __jaegerPropagationDelayMs",
                posBody.contains("Thread.sleep(__jaegerPropagationDelayMs)"));
        assertTrue("negative sleeps on __jaegerPropagationDelayMs",
                negBody.contains("Thread.sleep(__jaegerPropagationDelayMs)"));

        // Neither path may regress to a hardcoded Thread.sleep(3000)
        // before attachJaegerTrace — that was the dominant per-step idle
        // floor on positive-heavy runs.
        assertFalse("positive must not regress to Thread.sleep(3000)",
                posBody.contains("Thread.sleep(3000)"));
        assertFalse("negative must not regress to Thread.sleep(3000)",
                negBody.contains("Thread.sleep(3000)"));
    }
}
