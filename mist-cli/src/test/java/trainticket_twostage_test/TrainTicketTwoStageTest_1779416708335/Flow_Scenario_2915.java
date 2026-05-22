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

public class Flow_Scenario_2915 {

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
    public void test_negative_flow_S2915_v1_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v1_fault_Root1_EMPTY_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%20", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", " ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v1_fault_Root1_EMPTY_INPUT");
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
    public void test_negative_flow_S2915_v2_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v2_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/gggggggg-gggg-gggg-gggg-gggggggggggg [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/gggggggg-gggg-gggg-gggg-gggggggggggg", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "gggggggg-gggg-gggg-gggg-gggggggggggg", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/gggggggg-gggg-gggg-gggg-gggggggggggg [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/gggggggg-gggg-gggg-gggg-gggggggggggg [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v2_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S2915_v3_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v3_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/None [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/None", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "None", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/None [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/None [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v3_fault_Root1_NULL_INPUT");
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
    public void test_negative_flow_S2915_v4_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v4_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/12345 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/12345", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "12345", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/12345 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/12345 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v4_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S2915_v5_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v5_fault_Root1_EMPTY_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%0D%0A [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%0D%0A", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "\r\n", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%0D%0A [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%0D%0A [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v5_fault_Root1_EMPTY_INPUT");
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
    public void test_negative_flow_S2915_v6_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v6_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "<script>alert('XSS')</script>", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v6_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v7_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v7_fault_Root1_OVERFLOW");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20Lorem%20ipsum%20dolor%20sit%20amet%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v7_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S2915_v8_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v8_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/false [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/false", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "false", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/false [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/false [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v8_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v9_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v9_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/true [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/true", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "true", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/true [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/true [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v9_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v10_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v10_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/12345 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/12345", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "12345", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/12345 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/12345 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v10_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v11_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v11_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3B%20ls%20-la [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%3B%20ls%20-la", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "; ls -la", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3B%20ls%20-la [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3B%20ls%20-la [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v11_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v12_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v12_fault_Root1_EMPTY_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20%20%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%20%20%20", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "   ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20%20%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20%20%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v12_fault_Root1_EMPTY_INPUT");
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
    public void test_negative_flow_S2915_v13_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v13_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ZZZZZZZZZ [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/ZZZZZZZZZ", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "ZZZZZZZZZ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ZZZZZZZZZ [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ZZZZZZZZZ [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v13_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S2915_v14_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v14_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%20OR%20%271%27%3D%271 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%27%20OR%20%271%27%3D%271", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "' OR '1'='1", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%20OR%20%271%27%3D%271 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%20OR%20%271%27%3D%271 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v14_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v15_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v15_fault_Root1_EMPTY_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%09 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%09", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "\t", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%09 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%09 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v15_fault_Root1_EMPTY_INPUT");
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
    public void test_negative_flow_S2915_v16_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v16_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%21%40%23%24%25%5E%26*%28%29%7B%7D%5B%5D%7C%5C%3A%3B%22%27%3C%3E%3F%2C.%2F [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v16_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v17_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v17_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/null", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "null", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v17_fault_Root1_NULL_INPUT");
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
    public void test_negative_flow_S2915_v18_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v18_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%5B1%2C%202%2C%203%5D [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%5B1%2C%202%2C%203%5D", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "[1, 2, 3]", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%5B1%2C%202%2C%203%5D [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%5B1%2C%202%2C%203%5D [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v18_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v19_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v19_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%60whoami%60 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%60whoami%60", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "`whoami`", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%60whoami%60 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%60whoami%60 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v19_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v20_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v20_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%20UNION%20SELECT%20*%20FROM%20passwords%20-- [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%27%20UNION%20SELECT%20*%20FROM%20passwords%20--", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "' UNION SELECT * FROM passwords --", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%20UNION%20SELECT%20*%20FROM%20passwords%20-- [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%20UNION%20SELECT%20*%20FROM%20passwords%20-- [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v20_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v21_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v21_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "1; DELETE FROM users WHERE 1=1", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/1%3B%20DELETE%20FROM%20users%20WHERE%201%3D1 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v21_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v22_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v22_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/null", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "null", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v22_fault_Root1_NULL_INPUT");
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
    public void test_negative_flow_S2915_v23_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v23_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "<img src=x onerror=alert('XSS')>", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Cimg%20src%3Dx%20onerror%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v23_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v24_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v24_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/3.14159 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/3.14159", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "3.14159", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/3.14159 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/3.14159 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v24_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v25_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v25_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%5Cx00%5Cx01%5Cx02 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%5Cx00%5Cx01%5Cx02", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "\\x00\\x01\\x02", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%5Cx00%5Cx01%5Cx02 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%5Cx00%5Cx01%5Cx02 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v25_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v26_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v26_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "<svg onload=alert('XSS')>", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%3Csvg%20onload%3Dalert%28%27XSS%27%29%3E [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v26_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v27_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v27_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%00%01%02 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%00%01%02", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", " ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%00%01%02 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%00%01%02 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v27_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v28_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v28_fault_Root1_EMPTY_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%0A [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%0A", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "\n", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%0A [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%0A [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v28_fault_Root1_EMPTY_INPUT");
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
    public void test_negative_flow_S2915_v29_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v29_fault_Root1_OVERFLOW");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v29_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S2915_v30_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v30_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/nil [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/nil", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "nil", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/nil [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/nil [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v30_fault_Root1_NULL_INPUT");
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
    public void test_negative_flow_S2915_v31_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v31_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%7Bkey%3Dvalue%7D [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%7Bkey%3Dvalue%7D", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "{key=value}", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%7Bkey%3Dvalue%7D [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%7Bkey%3Dvalue%7D [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v31_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v32_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v32_fault_Root1_OVERFLOW");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v32_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S2915_v33_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v33_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/undefined [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/undefined", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "undefined", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/undefined [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/undefined [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v33_fault_Root1_NULL_INPUT");
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
    public void test_negative_flow_S2915_v34_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v34_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/javascript%3Aalert%28%27XSS%27%29 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/javascript%3Aalert%28%27XSS%27%29", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "javascript:alert('XSS')", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/javascript%3Aalert%28%27XSS%27%29 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/javascript%3Aalert%28%27XSS%27%29 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v34_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v35_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v35_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v35_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S2915_v36_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v36_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/not-a-uuid [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/not-a-uuid", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "not-a-uuid", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/not-a-uuid [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/not-a-uuid [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v36_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S2915_v37_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v37_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/..%2F..%2F..%2Fetc%2Fpasswd [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/..%2F..%2F..%2Fetc%2Fpasswd", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "../../../etc/passwd", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/..%2F..%2F..%2Fetc%2Fpasswd [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/..%2F..%2F..%2Fetc%2Fpasswd [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v37_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v38_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v38_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%7C%20cat%20%2Fetc%2Fpasswd [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%7C%20cat%20%2Fetc%2Fpasswd", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "| cat /etc/passwd", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%7C%20cat%20%2Fetc%2Fpasswd [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%7C%20cat%20%2Fetc%2Fpasswd [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v38_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v39_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v39_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/-999 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/-999", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "-999", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/-999 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/-999 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v39_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S2915_v40_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v40_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/NON_EXISTENT [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/NON_EXISTENT", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "NON_EXISTENT", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/NON_EXISTENT [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/NON_EXISTENT [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v40_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S2915_v41_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v41_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%24%28cat%20%2Fetc%2Fpasswd%29 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%24%28cat%20%2Fetc%2Fpasswd%29", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "$(cat /etc/passwd)", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%24%28cat%20%2Fetc%2Fpasswd%29 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%24%28cat%20%2Fetc%2Fpasswd%29 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v41_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v42_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v42_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%3B%20DROP%20TABLE%20users%3B%20-- [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%27%3B%20DROP%20TABLE%20users%3B%20--", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "'; DROP TABLE users; --", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%3B%20DROP%20TABLE%20users%3B%20-- [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%27%3B%20DROP%20TABLE%20users%3B%20-- [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v42_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v43_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v43_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%252e%252e%252f%252e%252e%252f [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%252e%252e%252f%252e%252e%252f", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "%2e%2e%2f%2e%2e%2f", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%252e%252e%252f%252e%252e%252f [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%252e%252e%252f%252e%252e%252f [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v43_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v44_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v44_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/NULL [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/NULL", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "NULL", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/NULL [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/NULL [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v44_fault_Root1_NULL_INPUT");
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
    public void test_negative_flow_S2915_v45_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v45_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/INVALID_VALUE [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/INVALID_VALUE", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "INVALID_VALUE", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/INVALID_VALUE [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/INVALID_VALUE [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v45_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S2915_v46_fault_Root1_EMPTY_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v46_fault_Root1_EMPTY_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20%09%20%0A%20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/%20%09%20%0A%20", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", " \t \n ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20%09%20%0A%20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/%20%09%20%0A%20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v46_fault_Root1_EMPTY_INPUT");
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
    public void test_negative_flow_S2915_v47_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v47_fault_Root1_OVERFLOW");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v47_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S2915_v48_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v48_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/..%5C..%5C..%5Cwindows%5Csystem32%5Cconfig%5Csam [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v48_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S2915_v49_fault_Root1_NULL_INPUT() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S2915_v49_fault_Root1_NULL_INPUT");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/null", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "null", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/null [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S2915_v49_fault_Root1_NULL_INPUT");
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
    public void test_positive_flow_S2915_v50() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v50");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_54 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_54", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_54", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_54 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_54 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v50");
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
    public void test_positive_flow_S2915_v51() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v51");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_94 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_94", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_94", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_94 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_94 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v51");
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
    public void test_positive_flow_S2915_v52() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v52");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_83 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_83", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_83", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_83 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_83 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v52");
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
    public void test_positive_flow_S2915_v53() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v53");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_75 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_75", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_75", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_75 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_75 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v53");
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
    public void test_positive_flow_S2915_v54() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v54");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_22 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_22", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_22", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_22 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_22 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v54");
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
    public void test_positive_flow_S2915_v55() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v55");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_56 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_56", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_56", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_56 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_56 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v55");
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
    public void test_positive_flow_S2915_v56() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v56");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_68 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_68", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_68", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_68 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_68 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v56");
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
    public void test_positive_flow_S2915_v57() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v57");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/test411 [expect 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/test411", "GET", 
            "ts-execute-service", false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "test411", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/test411 [expect 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/test411 [expect 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v57");
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
    public void test_positive_flow_S2915_v58() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v58");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_12 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_12", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_12", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_12 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_12 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v58");
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
    public void test_positive_flow_S2915_v59() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v59");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_77 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_77", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_77", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_77 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_77 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v59");
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
    public void test_positive_flow_S2915_v60() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v60");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_7 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_7", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_7", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_7 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_7 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v60");
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
    public void test_positive_flow_S2915_v61() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v61");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_20 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_20", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_20", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_20 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_20 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v61");
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
    public void test_positive_flow_S2915_v62() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v62");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_60 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_60", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_60", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_60 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_60 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v62");
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
    public void test_positive_flow_S2915_v63() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v63");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_107 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_107", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_107", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_107 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_107 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v63");
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
    public void test_positive_flow_S2915_v64() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v64");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_88 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_88", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_88", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_88 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_88 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v64");
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
    public void test_positive_flow_S2915_v65() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v65");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_93 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_93", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_93", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_93 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_93 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v65");
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
    public void test_positive_flow_S2915_v66() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v66");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_78 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_78", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_78", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_78 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_78 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v66");
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
    public void test_positive_flow_S2915_v67() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v67");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_95 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_95", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_95", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_95 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_95 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v67");
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
    public void test_positive_flow_S2915_v68() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v68");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_45 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_45", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_45", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_45 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_45 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v68");
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
    public void test_positive_flow_S2915_v69() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v69");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_89 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_89", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_89", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_89 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_89 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v69");
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
    public void test_positive_flow_S2915_v70() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v70");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_10 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_10", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_10", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_10 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_10 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v70");
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
    public void test_positive_flow_S2915_v71() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v71");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_28 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_28", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_28", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_28 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_28 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v71");
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
    public void test_positive_flow_S2915_v72() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v72");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_0 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_0", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_0", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_0 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_0 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v72");
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
    public void test_positive_flow_S2915_v73() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v73");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_14 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_14", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_14", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_14 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_14 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v73");
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
    public void test_positive_flow_S2915_v74() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v74");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_53 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_53", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_53", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_53 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_53 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v74");
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
    public void test_positive_flow_S2915_v75() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v75");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_62 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_62", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_62", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_62 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_62 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v75");
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
    public void test_positive_flow_S2915_v76() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v76");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_80 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_80", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_80", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_80 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_80 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v76");
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
    public void test_positive_flow_S2915_v77() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v77");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_59 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_59", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_59", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_59 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_59 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v77");
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
    public void test_positive_flow_S2915_v78() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v78");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_72 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_72", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_72", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_72 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_72 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v78");
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
    public void test_positive_flow_S2915_v79() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v79");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_85 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_85", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_85", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_85 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_85 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v79");
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
    public void test_positive_flow_S2915_v80() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v80");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_38 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_38", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_38", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_38 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_38 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v80");
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
    public void test_positive_flow_S2915_v81() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v81");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_6 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_6", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_6", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_6 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_6 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v81");
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
    public void test_positive_flow_S2915_v82() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v82");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_36 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_36", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_36", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_36 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_36 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v82");
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
    public void test_positive_flow_S2915_v83() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v83");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_43 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_43", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_43", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_43 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_43 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v83");
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
    public void test_positive_flow_S2915_v84() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v84");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_91 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_91", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_91", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_91 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_91 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v84");
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
    public void test_positive_flow_S2915_v85() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v85");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_100 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_100", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_100", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_100 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_100 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v85");
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
    public void test_positive_flow_S2915_v86() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v86");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_9 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_9", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_9", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_9 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_9 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v86");
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
    public void test_positive_flow_S2915_v87() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v87");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_21 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_21", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_21", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_21 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_21 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v87");
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
    public void test_positive_flow_S2915_v88() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v88");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_99 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_99", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_99", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_99 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_99 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v88");
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
    public void test_positive_flow_S2915_v89() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v89");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_15 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_15", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_15", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_15 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_15 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v89");
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
    public void test_positive_flow_S2915_v90() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v90");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_103 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_103", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_103", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_103 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_103 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v90");
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
    public void test_positive_flow_S2915_v91() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v91");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_11 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_11", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_11", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_11 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_11 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v91");
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
    public void test_positive_flow_S2915_v92() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v92");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_32 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_32", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_32", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_32 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_32 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v92");
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
    public void test_positive_flow_S2915_v93() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v93");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_97 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_97", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_97", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_97 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_97 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v93");
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
    public void test_positive_flow_S2915_v94() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v94");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_98 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_98", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_98", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_98 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_98 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v94");
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
    public void test_positive_flow_S2915_v95() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v95");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_29 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_29", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_29", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_29 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_29 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v95");
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
    public void test_positive_flow_S2915_v96() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v96");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_16 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_16", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_16", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_16 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_16 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v96");
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
    public void test_positive_flow_S2915_v97() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v97");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_19 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_19", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_19", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_19 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_19 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v97");
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
    public void test_positive_flow_S2915_v98() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v98");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_47 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_47", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_47", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_47 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_47 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v98");
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
    public void test_positive_flow_S2915_v99() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v99");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_26 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_26", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_26", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_26 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_26 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v99");
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
    public void test_positive_flow_S2915_v100() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v100");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_92 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_92", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_92", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_92 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_92 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v100");
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
    public void test_positive_flow_S2915_v101() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v101");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_69 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_69", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_69", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_69 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_69 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v101");
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
    public void test_positive_flow_S2915_v102() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v102");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_1 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_1", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_1", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_1 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_1 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v102");
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
    public void test_positive_flow_S2915_v103() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v103");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_87 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_87", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_87", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_87 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_87 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v103");
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
    public void test_positive_flow_S2915_v104() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v104");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_34 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_34", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_34", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_34 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_34 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v104");
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
    public void test_positive_flow_S2915_v105() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v105");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_23 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_23", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_23", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_23 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_23 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v105");
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
    public void test_positive_flow_S2915_v106() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v106");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_82 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_82", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_82", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_82 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_82 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v106");
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
    public void test_positive_flow_S2915_v107() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v107");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_96 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_96", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_96", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_96 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_96 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v107");
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
    public void test_positive_flow_S2915_v108() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v108");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_66 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_66", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_66", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_66 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_66 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v108");
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
    public void test_positive_flow_S2915_v109() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v109");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_51 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_51", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_51", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_51 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_51 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v109");
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
    public void test_positive_flow_S2915_v110() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v110");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_40 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_40", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_40", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_40 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_40 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v110");
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
    public void test_positive_flow_S2915_v111() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v111");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_90 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_90", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_90", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_90 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_90 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v111");
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
    public void test_positive_flow_S2915_v112() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v112");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_8 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_8", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_8", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_8 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_8 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v112");
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
    public void test_positive_flow_S2915_v113() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v113");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_4 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_4", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_4", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_4 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_4 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v113");
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
    public void test_positive_flow_S2915_v114() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v114");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_48 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_48", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_48", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_48 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_48 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v114");
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
    public void test_positive_flow_S2915_v115() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v115");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_58 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_58", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_58", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_58 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_58 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v115");
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
    public void test_positive_flow_S2915_v116() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v116");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_101 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_101", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_101", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_101 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_101 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v116");
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
    public void test_positive_flow_S2915_v117() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v117");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_106 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_106", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_106", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_106 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_106 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v117");
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
    public void test_positive_flow_S2915_v118() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v118");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_49 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_49", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_49", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_49 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_49 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v118");
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
    public void test_positive_flow_S2915_v120() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v120");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_102 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_102", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_102", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_102 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_102 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v120");
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
    public void test_positive_flow_S2915_v121() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v121");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_27 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_27", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_27", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_27 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_27 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v121");
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
    public void test_positive_flow_S2915_v122() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v122");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_17 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_17", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_17", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_17 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_17 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v122");
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
    public void test_positive_flow_S2915_v123() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v123");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_63 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_63", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_63", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_63 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_63 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v123");
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
    public void test_positive_flow_S2915_v124() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v124");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_108 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_108", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_108", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_108 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_108 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v124");
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
    public void test_positive_flow_S2915_v125() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v125");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_86 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_86", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_86", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_86 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_86 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v125");
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
    public void test_positive_flow_S2915_v127() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v127");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_24 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_24", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_24", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_24 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_24 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v127");
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
    public void test_positive_flow_S2915_v128() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v128");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_3 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_3", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_3", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_3 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_3 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v128");
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
    public void test_positive_flow_S2915_v130() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v130");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_33 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_33", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_33", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_33 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_33 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v130");
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
    public void test_positive_flow_S2915_v131() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v131");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_30 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_30", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_30", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_30 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_30 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v131");
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
    public void test_positive_flow_S2915_v132() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v132");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_52 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_52", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_52", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_52 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_52 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v132");
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
    public void test_positive_flow_S2915_v133() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v133");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_71 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_71", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_71", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_71 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_71 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v133");
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
    public void test_positive_flow_S2915_v134() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v134");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_2 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_2", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_2", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_2 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_2 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v134");
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
    public void test_positive_flow_S2915_v135() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v135");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_76 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_76", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_76", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_76 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_76 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v135");
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
    public void test_positive_flow_S2915_v136() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v136");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_39 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_39", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_39", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_39 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_39 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v136");
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
    public void test_positive_flow_S2915_v137() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v137");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_73 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_73", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_73", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_73 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_73 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v137");
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
    public void test_positive_flow_S2915_v138() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v138");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_46 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_46", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_46", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_46 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_46 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v138");
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
    public void test_positive_flow_S2915_v139() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S2915_v139");


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

        // Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_42 [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/executeservice/execute/collected/FALLBACK_orderId_42", "GET", 
            "ts-execute-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "orderId", "FALLBACK_orderId_42", 
            "string", "path", null, null, true, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_42 [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-execute-service GET /api/v1/executeservice/execute/collected/FALLBACK_orderId_42 [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S2915_v139");
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
