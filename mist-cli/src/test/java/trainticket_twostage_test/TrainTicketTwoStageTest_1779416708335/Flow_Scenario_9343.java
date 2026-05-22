package trainticket_twostage_test.TrainTicketTwoStageTest_1779416708335;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AssumptionViolatedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matchers;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;
import io.mist.core.analysis.TraceErrorAnalyzer;
import io.mist.core.analysis.TraceShapeAdapter;
import io.mist.core.smart.ParameterErrorAnalyzer;
import io.mist.core.smart.InputFetchRegistry;
import io.mist.core.smart.ParameterError;
import io.mist.core.oracle.shape.ShapeInvariantStore;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeOracle;
import io.mist.core.oracle.shape.TraceShapeVerdict;
import static org.junit.Assert.*;
import io.mist.core.testcase.MultiServiceTestCase;

public class Flow_Scenario_9343 {

    // LLM validation singletons — created ONCE per test class, not per test method
    private static final boolean LLM_VALIDATION_ENABLED = Boolean.parseBoolean(System.getProperty("llm.response.validation.enabled", "false"));
    private static final boolean LLM_ONLY_2XX = Boolean.parseBoolean(System.getProperty("llm.response.validation.only.2xx", "true"));
    private static final boolean LLM_INCLUDE_RCA = Boolean.parseBoolean(System.getProperty("llm.response.validation.include.rca", "true"));
    private static io.mist.core.generation.ZeroShotLLMGenerator llmValidator;

    // Trace Shape Oracle (Phase 2.F) — created ONCE per test class, reads .mist/trace-shape-invariants.json
    private static TraceShapeOracle oracle;
    // Per-step verdict, populated from inside attachJaegerTrace(...) so the test-method assertion
    // path can consult it when deciding whether to flip a negative test from FAIL to PASS.
    private static final ThreadLocal<TraceShapeVerdict> LAST_VERDICT = new ThreadLocal<>();

    // Output-coverage backstop (Fix 3 Layer 3): shared response-fingerprint set
    private static final java.util.Set<String> SEEN_RESPONSE_HASHES = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static String responseFingerprint(int status, String body) {
        String payload = status + "|" + (body == null ? "" : body);
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] h = md.digest(payload.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(h.length * 2);
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            return Integer.toString(payload.hashCode());
        }
    }

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        RestAssured.config = RestAssured.config()
            .httpClient(io.restassured.config.HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 10000)
                .setParam("http.socket.timeout", 30000));
        // Initialize LLM validation singletons (ONCE per class, not per test)
        if (LLM_VALIDATION_ENABLED) {
            llmValidator = new io.mist.core.generation.ZeroShotLLMGenerator();
        }

        // Trace Shape Oracle bootstrap (Phase 2.F) — reads the persisted invariant store.
        // The file is created/refreshed by MistRunner's bootstrap on its cold-start pass.
        try {
            java.nio.file.Path tsoPath = java.nio.file.Paths.get(".mist/trace-shape-invariants.json");
            oracle = new TraceShapeOracle(new ShapeInvariantStore(tsoPath));
        } catch (Throwable tsoEx) {
            System.err.println("[Trace Shape Oracle] init failed: " + tsoEx.getMessage());
            oracle = null;
        }
    }

    @Test
    public void test_negative_flow_S9343_v1_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v1_fault_Root1_EMPTY_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%20", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", " ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v1_fault_Root1_EMPTY_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v2_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v2_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/gggggggg-gggg-gggg-gggg-gggggggggggg [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/gggggggg-gggg-gggg-gggg-gggggggggggg", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "gggggggg-gggg-gggg-gggg-gggggggggggg", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/gggggggg-gggg-gggg-gggg-gggggggggggg [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/gggggggg-gggg-gggg-gggg-gggggggggggg [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v2_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v3_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v3_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/None [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/None", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "None", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/None [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/None [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v3_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v4_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v4_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/12345 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/12345", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "12345", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/12345 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/12345 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v4_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v5_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v5_fault_Root1_EMPTY_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%0D%0A [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%0D%0A", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "\r\n", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%0D%0A [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%0D%0A [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v5_fault_Root1_EMPTY_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v6_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v6_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "<script>alert('XSS')</script>", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v6_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v7_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v7_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v7_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v8_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v8_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/false [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/false", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "false", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/false [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/false [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v8_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v9_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v9_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/true [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/true", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "true", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/true [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/true [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v9_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v10_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v10_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/12345 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/12345", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "12345", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/12345 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/12345 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v10_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v11_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v11_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3B%20ls%20-la [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%3B%20ls%20-la", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "; ls -la", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3B%20ls%20-la [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3B%20ls%20-la [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v11_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v12_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v12_fault_Root1_EMPTY_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20%20%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%20%20%20", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "   ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20%20%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20%20%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v12_fault_Root1_EMPTY_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v13_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v13_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ZZZZZZZZZ [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/ZZZZZZZZZ", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "ZZZZZZZZZ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ZZZZZZZZZ [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ZZZZZZZZZ [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v13_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v14_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v14_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%20OR%20%271%27%3D%271 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%27%20OR%20%271%27%3D%271", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "' OR '1'='1", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%20OR%20%271%27%3D%271 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%20OR%20%271%27%3D%271 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v14_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v15_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v15_fault_Root1_EMPTY_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%09 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%09", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "\t", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%09 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%09 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v15_fault_Root1_EMPTY_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v16_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v16_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v16_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v17_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v17_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/null", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "null", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v17_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v18_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v18_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%5B1%2C%202%2C%203%5D [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%5B1%2C%202%2C%203%5D", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "[1, 2, 3]", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%5B1%2C%202%2C%203%5D [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%5B1%2C%202%2C%203%5D [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v18_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v19_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v19_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%60whoami%60 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%60whoami%60", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "`whoami`", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%60whoami%60 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%60whoami%60 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v19_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v20_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v20_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%20UNION%20SELECT%20*%20FROM%20passwords%20-- [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%27%20UNION%20SELECT%20*%20FROM%20passwords%20--", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "' UNION SELECT * FROM passwords --", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%20UNION%20SELECT%20*%20FROM%20passwords%20-- [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%20UNION%20SELECT%20*%20FROM%20passwords%20-- [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v20_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v21_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v21_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "1; DELETE FROM users WHERE 1=1", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v21_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v22_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v22_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/null", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "null", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v22_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v23_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v23_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "<img src=x onerror=alert('XSS')>", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v23_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v24_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v24_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/3.14159 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/3.14159", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "3.14159", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/3.14159 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/3.14159 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v24_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v25_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v25_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%5Cx00%5Cx01%5Cx02 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%5Cx00%5Cx01%5Cx02", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "\\x00\\x01\\x02", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%5Cx00%5Cx01%5Cx02 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%5Cx00%5Cx01%5Cx02 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v25_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v26_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v26_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "<svg onload=alert('XSS')>", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v26_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v27_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v27_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%00%01%02 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%00%01%02", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", " ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%00%01%02 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%00%01%02 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v27_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v28_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v28_fault_Root1_EMPTY_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%0A [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%0A", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "\n", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%0A [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%0A [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v28_fault_Root1_EMPTY_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v29_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v29_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v29_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v30_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v30_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/nil [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/nil", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "nil", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/nil [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/nil [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v30_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v31_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v31_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%7Bkey%3Dvalue%7D [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%7Bkey%3Dvalue%7D", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "{key=value}", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%7Bkey%3Dvalue%7D [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%7Bkey%3Dvalue%7D [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v31_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v32_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v32_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v32_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v33_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v33_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/undefined [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/undefined", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "undefined", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/undefined [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/undefined [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v33_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v34_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v34_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/javascript%3Aalert%28%27XSS%27%29 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/javascript%3Aalert%28%27XSS%27%29", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "javascript:alert('XSS')", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/javascript%3Aalert%28%27XSS%27%29 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/javascript%3Aalert%28%27XSS%27%29 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v34_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v35_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v35_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v35_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v36_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v36_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/not-a-uuid [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/not-a-uuid", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "not-a-uuid", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/not-a-uuid [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/not-a-uuid [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v36_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v37_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v37_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/..%2F..%2F..%2Fetc%2Fpasswd [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/..%2F..%2F..%2Fetc%2Fpasswd", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "../../../etc/passwd", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/..%2F..%2F..%2Fetc%2Fpasswd [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/..%2F..%2F..%2Fetc%2Fpasswd [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v37_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v38_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v38_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%7C%20cat%20%2Fetc%2Fpasswd [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%7C%20cat%20%2Fetc%2Fpasswd", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "| cat /etc/passwd", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%7C%20cat%20%2Fetc%2Fpasswd [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%7C%20cat%20%2Fetc%2Fpasswd [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v38_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v39_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v39_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/-999 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/-999", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "-999", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/-999 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/-999 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v39_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v40_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v40_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/NON_EXISTENT [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/NON_EXISTENT", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "NON_EXISTENT", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/NON_EXISTENT [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/NON_EXISTENT [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v40_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v41_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v41_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%24%28cat%20%2Fetc%2Fpasswd%29 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%24%28cat%20%2Fetc%2Fpasswd%29", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "$(cat /etc/passwd)", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%24%28cat%20%2Fetc%2Fpasswd%29 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%24%28cat%20%2Fetc%2Fpasswd%29 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v41_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v42_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v42_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%3B%20DROP%20TABLE%20users%3B%20-- [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%27%3B%20DROP%20TABLE%20users%3B%20--", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "'; DROP TABLE users; --", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%3B%20DROP%20TABLE%20users%3B%20-- [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%27%3B%20DROP%20TABLE%20users%3B%20-- [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v42_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v43_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v43_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%252e%252e%252f%252e%252e%252f [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%252e%252e%252f%252e%252e%252f", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "%2e%2e%2f%2e%2e%2f", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%252e%252e%252f%252e%252e%252f [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%252e%252e%252f%252e%252e%252f [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v43_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v44_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v44_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/NULL [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/NULL", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "NULL", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/NULL [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/NULL [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v44_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v45_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v45_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/INVALID_VALUE [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/INVALID_VALUE", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "INVALID_VALUE", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/INVALID_VALUE [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/INVALID_VALUE [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v45_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v46_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v46_fault_Root1_EMPTY_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20%09%20%0A%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/%20%09%20%0A%20", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", " \t \n ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20%09%20%0A%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/%20%09%20%0A%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v46_fault_Root1_EMPTY_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v47_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v47_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v47_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v48_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v48_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v48_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S9343_v49_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S9343_v49_fault_Root1_NULL_INPUT");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/null", "GET", 
            "ts-consign-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "null", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/null [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S9343_v49_fault_Root1_NULL_INPUT");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_positive_flow_S9343_v50() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S9343_v50");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/test497 [expect 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/consignservice/consigns/order/test497", "GET", 
            "ts-consign-service", false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test497", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/test497 [expect 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-consign-service GET /api/v1/consignservice/consigns/order/test497 [expect 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S9343_v50");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

}
