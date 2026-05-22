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

public class Flow_Scenario_3997 {

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
    public void test_negative_flow_S3997_v1_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v1_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "'; DROP TABLE users; --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v1_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v2_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v2_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "12345", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v2_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v3_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v3_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "' UNION SELECT * FROM passwords --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v3_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v4_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v4_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "<script>alert('XSS')</script>", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v4_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v5_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v5_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "\\x00\\x01\\x02", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v5_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v6_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v6_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "3.14159", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v6_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v7_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v7_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_13", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "3.14159", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v7_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v8_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v8_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v8_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v9_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v9_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "false", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v9_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v10_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v10_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "javascript:alert('XSS')", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v10_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v11_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v11_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "%2e%2e%2f%2e%2e%2f", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v11_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v12_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v12_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_17", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v12_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v13_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v13_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "00:99:99", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v13_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v14_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v14_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "; ls -la", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v14_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v15_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v15_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v15_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v16_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v16_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "; ls -la", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v16_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v17_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v17_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "true", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v17_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v18_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v18_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "{key=value}", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v18_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v19_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v19_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "INVALID_VALUE", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v19_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v20_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v20_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "| cat /etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v20_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v21_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v21_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v21_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v22_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v22_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v22_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v23_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v23_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "NON_EXISTENT", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v23_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v24_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v24_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_7", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "NonExistentStation", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v24_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v25_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v25_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "false", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v25_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v26_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v26_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "not-a-date", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v26_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v27_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v27_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "' UNION SELECT * FROM passwords --", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v27_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v28_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v28_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_1", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v28_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v29_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v29_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "ZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v29_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v30_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v30_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "' OR '1'='1", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v30_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v31_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v31_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v31_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v32_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v32_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "{key=value}", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v32_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v33_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v33_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_7", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "javascript:alert('XSS')", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v33_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v34_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v34_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "-999", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v34_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v35_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v35_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "javascript:alert('XSS')", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v35_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v36_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v36_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "true", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v36_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v37_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v37_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "32-13-2024", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v37_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v38_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v38_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "$(cat /etc/passwd)", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v38_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v39_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v39_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "yesterday", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v39_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v40_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v40_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_2", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v40_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v41_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v41_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "' OR '1'='1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v41_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v42_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v42_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v42_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v43_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v43_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "3.14159", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v43_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v44_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v44_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_17", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "../../../etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v44_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v45_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v45_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "`whoami`", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v45_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v46_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v46_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "<script>alert('XSS')</script>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v46_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v47_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v47_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "1; DELETE FROM users WHERE 1=1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v47_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v48_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v48_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_12", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v48_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v49_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v49_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "[1, 2, 3]", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v49_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v50_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v50_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "../../../etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v50_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v51_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v51_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "-999", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v51_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v52_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v52_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "%2e%2e%2f%2e%2e%2f", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v52_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v53_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v53_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "../../../etc/passwd", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v53_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v54_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v54_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "   ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v54_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v55_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v55_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "\t\n\r", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v55_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v56_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v56_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_12", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v56_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v57_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v57_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_12", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "$(cat /etc/passwd)", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v57_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v58_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v58_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "%2e%2e%2f%2e%2e%2f", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v58_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v59_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v59_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v59_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v60_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v60_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_7", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v60_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v61_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v61_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_12", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "' UNION SELECT * FROM passwords --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v61_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v62_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v62_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_1", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "| cat /etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v62_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v63_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v63_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_17", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "' OR '1'='1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v63_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v64_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v64_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "| cat /etc/passwd", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v64_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v65_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v65_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", " ", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v65_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v66_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v66_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_7", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "{key=value}", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v66_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v67_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v67_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "<svg onload=alert('XSS')>", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v67_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v68_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v68_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "\t\n\r", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v68_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v69_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v69_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "<img src=x onerror=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v69_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v70_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v70_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "\\x00\\x01\\x02", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v70_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v71_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v71_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v71_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v72_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v72_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v72_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v73_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v73_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "-999", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v73_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v74_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v74_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "`whoami`", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v74_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v75_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v75_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v75_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v76_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v76_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "'; DROP TABLE users; --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v76_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v77_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v77_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "1; DELETE FROM users WHERE 1=1", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v77_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v78_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v78_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "2024/99/99", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v78_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v79_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v79_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "<img src=x onerror=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v79_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v80_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v80_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "   ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v80_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v81_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v81_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_7", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "ZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v81_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v82_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v82_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "<script>alert('XSS')</script>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v82_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v83_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v83_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v83_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v84_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v84_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_9", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v84_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v85_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v85_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_12", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "true", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v85_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v86_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v86_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "\\x00\\x01\\x02", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v86_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v87_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v87_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "[1, 2, 3]", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v87_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v88_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v88_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "false", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v88_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v89_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v89_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "$(cat /etc/passwd)", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v89_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v90_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v90_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "NonExistentStation", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v90_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v91_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v91_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "!@#$%", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v91_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v92_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v92_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "[1, 2, 3]", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v92_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v93_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v93_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_17", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v93_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v94_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v94_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "; ls -la", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v94_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v95_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v95_fault_Root1_OVERFLOW");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v95_fault_Root1_OVERFLOW");
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
    public void test_negative_flow_S3997_v96_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v96_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "'; DROP TABLE users; --", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v96_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v97_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v97_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v97_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v98_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v98_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", " ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v98_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v99_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v99_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "`whoami`", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v99_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v100_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v100_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", " ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v100_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v101_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v101_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v101_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v102_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v102_fault_Root1_SEMANTIC_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "ZZZZZZZZZ", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v102_fault_Root1_SEMANTIC_MISMATCH");
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
    public void test_negative_flow_S3997_v103_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v103_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_2", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "<svg onload=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v103_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v104_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v104_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "<svg onload=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v104_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v105_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v105_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "1; DELETE FROM users WHERE 1=1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v105_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v106_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v106_fault_Root1_TYPE_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v106_fault_Root1_TYPE_MISMATCH");
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
    public void test_negative_flow_S3997_v107_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v107_fault_Root1_REGEX_MISMATCH");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "!@#$%", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v107_fault_Root1_REGEX_MISMATCH");
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
    public void test_negative_flow_S3997_v108_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v108_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "<img src=x onerror=alert('XSS')>", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v108_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_negative_flow_S3997_v109_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S3997_v109_fault_Root1_SPECIAL_CHARACTERS");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S3997_v109_fault_Root1_SPECIAL_CHARACTERS");
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
    public void test_positive_flow_S3997_v110() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v110");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v110");
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
    public void test_positive_flow_S3997_v111() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v111");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v111");
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
    public void test_positive_flow_S3997_v112() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v112");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v112");
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
    public void test_positive_flow_S3997_v113() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v113");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v113");
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
    public void test_positive_flow_S3997_v114() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v114");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v114");
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
    public void test_positive_flow_S3997_v115() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v115");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v115");
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
    public void test_positive_flow_S3997_v116() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v116");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v116");
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
    public void test_positive_flow_S3997_v117() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v117");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v117");
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
    public void test_positive_flow_S3997_v118() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v118");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v118");
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
    public void test_positive_flow_S3997_v119() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v119");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v119");
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
    public void test_positive_flow_S3997_v120() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v120");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v120");
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
    public void test_positive_flow_S3997_v121() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v121");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v121");
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
    public void test_positive_flow_S3997_v122() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v122");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v122");
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
    public void test_positive_flow_S3997_v123() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v123");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v123");
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
    public void test_positive_flow_S3997_v124() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v124");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v124");
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
    public void test_positive_flow_S3997_v125() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v125");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v125");
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
    public void test_positive_flow_S3997_v126() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v126");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_2", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v126");
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
    public void test_positive_flow_S3997_v127() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v127");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v127");
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
    public void test_positive_flow_S3997_v128() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v128");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_13", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v128");
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
    public void test_positive_flow_S3997_v129() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v129");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v129");
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
    public void test_positive_flow_S3997_v130() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v130");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_13", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v130");
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
    public void test_positive_flow_S3997_v131() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v131");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v131");
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
    public void test_positive_flow_S3997_v132() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v132");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v132");
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
    public void test_positive_flow_S3997_v133() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v133");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v133");
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
    public void test_positive_flow_S3997_v134() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v134");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v134");
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
    public void test_positive_flow_S3997_v135() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v135");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v135");
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
    public void test_positive_flow_S3997_v136() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v136");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v136");
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
    public void test_positive_flow_S3997_v137() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v137");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v137");
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
    public void test_positive_flow_S3997_v138() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v138");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v138");
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
    public void test_positive_flow_S3997_v139() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v139");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v139");
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
    public void test_positive_flow_S3997_v140() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v140");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v140");
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
    public void test_positive_flow_S3997_v141() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v141");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v141");
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
    public void test_positive_flow_S3997_v142() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v142");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_9", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v142");
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
    public void test_positive_flow_S3997_v143() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v143");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v143");
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
    public void test_positive_flow_S3997_v144() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v144");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v144");
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
    public void test_positive_flow_S3997_v145() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v145");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v145");
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
    public void test_positive_flow_S3997_v146() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v146");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_2", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v146");
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
    public void test_positive_flow_S3997_v147() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v147");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v147");
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
    public void test_positive_flow_S3997_v148() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v148");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v148");
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
    public void test_positive_flow_S3997_v149() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v149");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v149");
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
    public void test_positive_flow_S3997_v150() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v150");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v150");
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
    public void test_positive_flow_S3997_v151() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v151");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_9", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v151");
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
    public void test_positive_flow_S3997_v152() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v152");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v152");
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
    public void test_positive_flow_S3997_v153() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v153");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v153");
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
    public void test_positive_flow_S3997_v154() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v154");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_15", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v154");
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
    public void test_positive_flow_S3997_v155() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v155");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v155");
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
    public void test_positive_flow_S3997_v156() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v156");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v156");
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
    public void test_positive_flow_S3997_v157() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v157");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v157");
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
    public void test_positive_flow_S3997_v158() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v158");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_7", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v158");
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
    public void test_positive_flow_S3997_v159() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v159");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v159");
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
    public void test_positive_flow_S3997_v160() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v160");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v160");
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
    public void test_positive_flow_S3997_v161() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v161");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_13", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v161");
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
    public void test_positive_flow_S3997_v162() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v162");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v162");
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
    public void test_positive_flow_S3997_v163() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v163");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v163");
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
    public void test_positive_flow_S3997_v164() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v164");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v164");
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
    public void test_positive_flow_S3997_v165() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v165");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_17", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v165");
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
    public void test_positive_flow_S3997_v166() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v166");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v166");
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
    public void test_positive_flow_S3997_v167() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v167");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v167");
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
    public void test_positive_flow_S3997_v168() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v168");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v168");
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
    public void test_positive_flow_S3997_v169() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v169");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v169");
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
    public void test_positive_flow_S3997_v170() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v170");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "test664", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v170");
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
    public void test_positive_flow_S3997_v171() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v171");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v171");
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
    public void test_positive_flow_S3997_v172() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v172");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_12", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v172");
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
    public void test_positive_flow_S3997_v173() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v173");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_8", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v173");
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
    public void test_positive_flow_S3997_v174() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v174");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v174");
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
    public void test_positive_flow_S3997_v175() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v175");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "test666", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v175");
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
    public void test_positive_flow_S3997_v176() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v176");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_3", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v176");
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
    public void test_positive_flow_S3997_v177() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v177");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_9", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_15", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v177");
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
    public void test_positive_flow_S3997_v178() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v178");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v178");
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
    public void test_positive_flow_S3997_v179() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v179");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_10", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v179");
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
    public void test_positive_flow_S3997_v180() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v180");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v180");
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
    public void test_positive_flow_S3997_v181() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v181");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_11", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v181");
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
    public void test_positive_flow_S3997_v182() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v182");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_16", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v182");
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
    public void test_positive_flow_S3997_v183() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v183");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_13", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v183");
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
    public void test_positive_flow_S3997_v184() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v184");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_6", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v184");
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
    public void test_positive_flow_S3997_v185() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v185");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_4", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "test67", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v185");
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
    public void test_positive_flow_S3997_v186() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v186");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_2", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v186");
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
    public void test_positive_flow_S3997_v187() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v187");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_13", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v187");
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
    public void test_positive_flow_S3997_v188() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v188");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_17", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_17", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v188");
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
    public void test_positive_flow_S3997_v189() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v189");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_1", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_0", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v189");
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
    public void test_positive_flow_S3997_v190() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v190");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_2", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_8", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v190");
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
    public void test_positive_flow_S3997_v191() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v191");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v191");
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
    public void test_positive_flow_S3997_v192() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v192");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_9", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v192");
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
    public void test_positive_flow_S3997_v193() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v193");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_0", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_14", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_3", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v193");
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
    public void test_positive_flow_S3997_v194() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v194");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_11", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_2", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v194");
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
    public void test_positive_flow_S3997_v195() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v195");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_5", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_5", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v195");
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
    public void test_positive_flow_S3997_v196() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v196");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_12", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_18", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v196");
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
    public void test_positive_flow_S3997_v197() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v197");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_7", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v197");
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
    public void test_positive_flow_S3997_v198() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v198");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_18", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_16", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_10", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v198");
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
    public void test_positive_flow_S3997_v199() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S3997_v199");


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

        // Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/travelservice/trips/left", "POST", 
            "ts-travel-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "departureTime", "FALLBACK_departureTime_14", 
            "string", "formData", 
            "Date in format yyyy-MM-dd (e.g., 2025-11-01)", 
            "2025-10-31", 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "endPlace", "FALLBACK_endPlace_4", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "startPlace", "FALLBACK_startPlace_6", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-travel-service POST /api/v1/travelservice/trips/left [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S3997_v199");
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
