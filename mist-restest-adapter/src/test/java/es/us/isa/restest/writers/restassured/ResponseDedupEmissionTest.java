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

import static org.junit.Assert.assertTrue;

/**
 * Pins the Fix 3 Layer 3 emission contract: {@link MultiServiceRESTAssuredWriter}
 * must write a class-level {@code SEEN_RESPONSE_HASHES} set, a
 * {@code responseFingerprint(int, String)} helper, and an
 * {@code isDuplicateResponse{i}} gate that short-circuits the LLM
 * validation branches in both the negative-test path (skipped before the
 * LLM call) and the positive-test path (skipped inside the LLM gate).
 */
public class ResponseDedupEmissionTest {

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
    public void writerEmitsOutputCoverageBackstop() throws Exception {
        File outDir = tmp.newFolder("out");
        MultiServiceRESTAssuredWriter w = new MultiServiceRESTAssuredWriter(
                SPEC_PATH, CONF_PATH, outDir.getAbsolutePath(),
                "DedupEmissionSuite", "gen", "http://localhost", false);
        w.setAllureReport(true);

        List<TestCase> cases = new ArrayList<>();
        cases.add(singleStepCase("DedupPositive", false));
        cases.add(singleStepCase("DedupNegative", true));
        w.write(cases);

        File pos = new File(outDir, "gen/DedupEmissionSuite/DedupPositive.java");
        File neg = new File(outDir, "gen/DedupEmissionSuite/DedupNegative.java");
        assertTrue("positive emitted: " + pos, pos.exists());
        assertTrue("negative emitted: " + neg, neg.exists());

        String posBody = new String(Files.readAllBytes(pos.toPath()), StandardCharsets.UTF_8);
        String negBody = new String(Files.readAllBytes(neg.toPath()), StandardCharsets.UTF_8);

        // Class-level fingerprint set
        assertTrue("positive carries SEEN_RESPONSE_HASHES set",
                posBody.contains("SEEN_RESPONSE_HASHES = java.util.concurrent.ConcurrentHashMap.newKeySet()"));
        assertTrue("negative carries SEEN_RESPONSE_HASHES set",
                negBody.contains("SEEN_RESPONSE_HASHES = java.util.concurrent.ConcurrentHashMap.newKeySet()"));

        // Fingerprint helper
        assertTrue("positive carries responseFingerprint helper",
                posBody.contains("static String responseFingerprint(int status, String body)"));
        assertTrue("positive uses SHA-256 inside fingerprint helper",
                posBody.contains("MessageDigest.getInstance(\"SHA-256\")"));

        // Per-step duplicate gate
        assertTrue("positive computes responseFingerprint per step",
                posBody.contains("responseFingerprint(actualStatusCode"));
        assertTrue("positive computes isDuplicateResponse per step",
                posBody.contains("isDuplicateResponse"));
        assertTrue("negative computes isDuplicateResponse per step",
                negBody.contains("isDuplicateResponse"));

        // Positive-test LLM branch must include the dup short-circuit
        assertTrue("positive LLM branch gates on !isDuplicateResponse",
                posBody.contains("!isDuplicateResponse"));

        // Negative-test LLM branch must include the dup short-circuit
        assertTrue("negative LLM branch gates on isDuplicateResponse",
                negBody.contains("else if (isDuplicateResponse"));
        assertTrue("negative branch records skip RCA for duplicate responses",
                negBody.contains("response fingerprint already validated"));
    }
}
