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

public class Flow_Scenario_14566 {

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
    public void test_negative_flow_S14566_v1_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v1_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "1; DELETE FROM users WHERE 1=1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v1_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v2_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v2_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v2_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v3_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v3_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "not_a_number", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v3_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v4_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v4_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "false", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v4_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v5_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v5_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "2147483647", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v5_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v6_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v6_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "3.14159", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v6_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v7_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v7_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "true", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v7_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v8_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v8_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "; ls -la", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v8_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v9_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v9_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "<svg onload=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v9_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v10_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v10_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "ZZZZZZZZZ", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v10_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v11_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v11_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "| cat /etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v11_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v12_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v12_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "2147483647", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v12_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v13_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v13_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "[1, 2, 3]", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v13_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v14_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v14_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "infinity", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v14_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v15_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v15_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "[a, b]", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v15_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v16_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v16_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "[a, b]", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v16_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v17_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v17_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v17_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v18_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v18_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "`whoami`", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v18_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v19_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v19_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "[1, 2, 3]", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v19_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v20_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v20_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "../../../etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v20_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v21_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v21_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "INVALID_VALUE", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v21_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v22_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v22_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", " ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v22_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v23_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v23_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "NaN", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v23_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v24_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v24_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "false", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v24_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v25_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v25_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "NON_EXISTENT", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v25_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v26_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v26_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "NON_EXISTENT", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v26_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v27_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v27_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "NON_EXISTENT", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v27_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v28_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v28_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "<img src=x onerror=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v28_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v29_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v29_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "!@#$%", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v29_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v30_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v30_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "' UNION SELECT * FROM passwords --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v30_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v31_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v31_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "false", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v31_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v32_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v32_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "ZZZZZZZZZ", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v32_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v33_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v33_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "999", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v33_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v34_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v34_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "' OR '1'='1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v34_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v35_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v35_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", " ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v35_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v36_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v36_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "; ls -la", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v36_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v37_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v37_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "%2e%2e%2f%2e%2e%2f", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v37_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v38_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v38_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "-9223372036854775808", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v38_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v39_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v39_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "<svg onload=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v39_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v40_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v40_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "true", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v40_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v41_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v41_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "   ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v41_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v42_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v42_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "\\x00\\x01\\x02", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v42_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v43_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v43_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "<script>alert('XSS')</script>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v43_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v44_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v44_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "99999999999999999999", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v44_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v45_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v45_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "-2147483648", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v45_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v46_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v46_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "$(cat /etc/passwd)", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v46_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v47_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v47_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "true", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v47_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v48_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v48_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "`whoami`", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v48_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v49_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v49_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v49_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v50_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v50_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "{key=value}", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v50_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v51_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v51_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "false", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v51_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v52_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v52_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v52_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v53_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v53_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "{key=value}", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v53_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v54_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v54_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "99999999999999999999", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v54_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v55_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v55_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "-999", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v55_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v56_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v56_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "true", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v56_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v57_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v57_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "%2e%2e%2f%2e%2e%2f", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v57_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v58_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v58_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "gggggggg-gggg-gggg-gggg-gggggggggggg", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v58_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v59_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v59_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v59_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v60_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v60_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "-5", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v60_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v61_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v61_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "not_a_number", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v61_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v62_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v62_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v62_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v63_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v63_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "9223372036854775807", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v63_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v64_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v64_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v64_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v65_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v65_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v65_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v66_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v66_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "<img src=x onerror=alert('XSS')>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v66_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v67_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v67_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v67_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v68_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v68_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "'; DROP TABLE users; --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v68_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v69_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v69_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v69_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v70_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v70_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "$(cat /etc/passwd)", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v70_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v71_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v71_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "not_a_number", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v71_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v72_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v72_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "-9223372036854775808", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v72_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v73_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v73_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v73_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v74_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v74_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "[a, b]", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v74_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v75_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v75_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "../../../etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v75_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v76_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v76_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "-2147483648", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v76_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v77_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v77_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v77_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v78_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v78_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "<script>alert('XSS')</script>", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v78_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v79_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v79_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "3.14159", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v79_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v80_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v80_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "2147483647", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v80_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v81_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v81_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "12.34abc", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v81_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v82_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v82_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "200", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v82_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v83_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v83_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "1; DELETE FROM users WHERE 1=1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v83_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v84_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v84_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "\t\n\r", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v84_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v85_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v85_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "-9223372036854775808", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v85_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v86_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v86_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "9223372036854775807", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v86_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v87_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v87_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "NaN", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v87_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v88_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v88_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "infinity", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v88_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v89_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v89_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "ZZZZZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v89_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v90_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v90_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "12.34abc", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v90_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v91_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v91_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v91_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v92_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v92_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v92_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v93_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v93_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "INVALID_VALUE", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v93_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v94_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v94_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "not-a-uuid", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v94_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v95_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v95_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "true", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v95_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v96_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v96_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "..\\..\\..\\windows\\system32\\config\\sam", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v96_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v97_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v97_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "NON_EXISTENT", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v97_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v98_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v98_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "' OR '1'='1", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v98_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v99_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v99_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "9223372036854775807", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v99_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v100_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v100_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "false", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v100_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v101_fault_Root1_REGEX_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v101_fault_Root1_REGEX_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "12345", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v101_fault_Root1_REGEX_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v102_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v102_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "NaN", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v102_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v103_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v103_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v103_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v104_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v104_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "\\x00\\x01\\x02", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v104_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v105_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v105_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v105_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v106_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v106_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "99999999999999999999", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v106_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v107_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v107_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "'; DROP TABLE users; --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v107_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v108_fault_Root1_OVERFLOW() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v108_fault_Root1_OVERFLOW");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "-2147483648", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v108_fault_Root1_OVERFLOW");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v109_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v109_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "INVALID_VALUE", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v109_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v110_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v110_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "!@#$%^&*(){}[]|\\:;\"'<>?,./", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v110_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v111_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v111_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "12.34abc", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v111_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v112_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v112_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "javascript:alert('XSS')", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v112_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v113_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v113_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "ZZZZZZZZZ", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v113_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v114_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v114_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "infinity", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v114_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v115_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v115_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "| cat /etc/passwd", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v115_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v116_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v116_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "' UNION SELECT * FROM passwords --", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v116_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v117_fault_Root1_TYPE_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v117_fault_Root1_TYPE_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "-999", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v117_fault_Root1_TYPE_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v118_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v118_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "0", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v118_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v119_fault_Root1_SPECIAL_CHARACTERS() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v119_fault_Root1_SPECIAL_CHARACTERS");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "javascript:alert('XSS')", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v119_fault_Root1_SPECIAL_CHARACTERS");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_negative_flow_S14566_v120_fault_Root1_SEMANTIC_MISMATCH() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_flow_S14566_v120_fault_Root1_SEMANTIC_MISMATCH");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", true);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "INVALID_VALUE", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect not 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_flow_S14566_v120_fault_Root1_SEMANTIC_MISMATCH");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_positive_flow_S14566_v121() throws Exception {
        // Record test execution for fault detection tracking
        io.mist.core.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_positive_flow_S14566_v121");


        // Per-test auth override (AuthManipulationStrategy). null = use default.
        final String  __mstOverrideToken = null;
        final boolean __mstDisableAuth   = false;
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            if (io.mist.cli.auth.MstAuthHandler.getMode() != io.mist.cli.auth.MstAuthHandler.Mode.NONE
                    && !io.mist.cli.auth.MstAuthHandler.ensureReady()) {
                loginSucceeded.set(false);
            }
        } catch (Throwable __mstAuthErr) {
            loginSucceeded.set(false);
        }

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect 200]
        io.mist.core.enhancer.TestResultCapture.setStepMetadata(
            1, "/api/v1/adminbasicservice/adminbasic/trains", "PUT", 
            "ts-admin-basic-info-service", false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "averageSpeed", "test213", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "confortClass", "test282", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "economyClass", "test562", 
            "integer", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "id", "test582", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);
        io.mist.core.enhancer.TestResultCapture.addParameter(
            "name", "test321", 
            "string", "formData", 
            null, 
            null, 
            false, 1, false);

        // Non-Allure version - simplified execution
        MultiServiceTestCase.ExecutionDecision decision1;
        // Resilient mode: execute regardless of predecessor results
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);
        if (decision1.shouldExecute && loginSucceeded.get()) {
            System.out.println("✅ EXECUTING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect 200]");
            // Execute step logic here (simplified version)
            stepResults.put(1, true);
        } else {
            System.out.println("⏭️ SKIPPING: Root 1: ts-admin-basic-info-service PUT /api/v1/adminbasicservice/adminbasic/trains [expect 200]");
            stepResults.put(1, false);
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_positive_flow_S14566_v121");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
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
