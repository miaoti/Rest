package trainticket_twostage_test.TrainTicketTwoStageTest_1752567765454;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;
import static org.junit.Assert.*;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.model.Status;

public class POST_api_v1_adminbasicservice_adminbasic_prices_31 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_POST_31_1() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"user_01\",\"routeId\":\"Route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"user_01\",\"routeId\":\"Route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"user_01\",\"routeId\":\"Route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"user_01\",\"routeId\":\"Route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_1");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_2() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"10.99\",\"id\":\"User_01\",\"routeId\":\"printf\",\"trainType\":\"freight train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"10.99\",\"id\":\"User_01\",\"routeId\":\"printf\",\"trainType\":\"freight train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"10.99\",\"id\":\"User_01\",\"routeId\":\"printf\",\"trainType\":\"freight train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"10.99\",\"id\":\"User_01\",\"routeId\":\"printf\",\"trainType\":\"freight train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_2");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_3() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"User_01\",\"routeId\":\"xyz789\",\"trainType\":\"Freight Train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"User_01\",\"routeId\":\"xyz789\",\"trainType\":\"Freight Train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"User_01\",\"routeId\":\"xyz789\",\"trainType\":\"Freight Train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"User_01\",\"routeId\":\"xyz789\",\"trainType\":\"Freight Train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_3");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_4() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"9.99\",\"id\":\"Order_2023\",\"routeId\":\"route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"9.99\",\"id\":\"Order_2023\",\"routeId\":\"route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"9.99\",\"id\":\"Order_2023\",\"routeId\":\"route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"9.99\",\"id\":\"Order_2023\",\"routeId\":\"route_with_special_characters!@#\",\"trainType\":\"Express Train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_4");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_5() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"12345\",\"routeId\":\"Route_With_Special_Characters!@#\",\"trainType\":\"Freight train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"12345\",\"routeId\":\"Route_With_Special_Characters!@#\",\"trainType\":\"Freight train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"12345\",\"routeId\":\"Route_With_Special_Characters!@#\",\"trainType\":\"Freight train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"10.99\",\"id\":\"12345\",\"routeId\":\"Route_With_Special_Characters!@#\",\"trainType\":\"Freight train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_5");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_6() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"user_01\",\"routeId\":\"Xyz789\",\"trainType\":\"Express train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"user_01\",\"routeId\":\"Xyz789\",\"trainType\":\"Express train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"user_01\",\"routeId\":\"Xyz789\",\"trainType\":\"Express train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"10.99\",\"firstClassPriceRate\":\"23.98\",\"id\":\"user_01\",\"routeId\":\"Xyz789\",\"trainType\":\"Express train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_6");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_7() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"10.99\",\"id\":\"Abcde\",\"routeId\":\"printf\",\"trainType\":\"diesel_locomotive\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"10.99\",\"id\":\"Abcde\",\"routeId\":\"printf\",\"trainType\":\"diesel_locomotive\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"10.99\",\"id\":\"Abcde\",\"routeId\":\"printf\",\"trainType\":\"diesel_locomotive\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"10.99\",\"id\":\"Abcde\",\"routeId\":\"printf\",\"trainType\":\"diesel_locomotive\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_7");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_8() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"60.50\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"buf\",\"trainType\":\"locomotive\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"60.50\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"buf\",\"trainType\":\"locomotive\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"60.50\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"buf\",\"trainType\":\"locomotive\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"60.50\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"buf\",\"trainType\":\"locomotive\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_8");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_9() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"60.50\",\"id\":\"xyz789\",\"routeId\":\"Abcde\",\"trainType\":\"express train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"60.50\",\"id\":\"xyz789\",\"routeId\":\"Abcde\",\"trainType\":\"express train\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"60.50\",\"id\":\"xyz789\",\"routeId\":\"Abcde\",\"trainType\":\"express train\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"78.32\",\"firstClassPriceRate\":\"60.50\",\"id\":\"xyz789\",\"routeId\":\"Abcde\",\"trainType\":\"express train\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_9");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_31_10() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
            });
        } catch (Throwable __t) {
            loginSucceeded.set(false);
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"9.99\",\"id\":\"buf\",\"routeId\":\"shortest_route\",\"trainType\":\"locomotive\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"9.99\",\"id\":\"buf\",\"routeId\":\"shortest_route\",\"trainType\":\"locomotive\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse1.getBody().asString();
                        int actualStatus = stepResponse1.getStatusCode();
                        long responseTime = stepResponse1.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-basic-info-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-basic-info-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminbasicservice/adminbasic/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "✅ **Expected**: 201\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"9.99\",\"id\":\"buf\",\"routeId\":\"shortest_route\",\"trainType\":\"locomotive\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"basicPriceRate\":\"1000.00\",\"firstClassPriceRate\":\"9.99\",\"id\":\"buf\",\"routeId\":\"shortest_route\",\"trainType\":\"locomotive\"}");
                    Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                       .then().log().ifValidationFails()
                       .statusCode(201)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse2.getBody().asString();
                        int actualStatus = stepResponse2.getStatusCode();
                        long responseTime = stepResponse2.getTime();
                        
                        // Add response as prominently visible attachment
                        Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                        
                        // Add key metrics as visible parameters
                        Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                        Allure.parameter("⏱️ Response Time", responseTime + " ms");
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                    // ❌ Step failed - capture detailed error information
                    String errorCategory = "Unknown";
                    if (t instanceof java.net.ConnectException) {
                        errorCategory = "🔌 Connection Failed - Service Unreachable";
                    } else if (t instanceof AssertionError) {
                        errorCategory = "❗ Assertion Failed - Unexpected Response";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorCategory = "⏰ Timeout - Service Too Slow";
                    } else {
                        errorCategory = "❓ " + t.getClass().getSimpleName();
                    }
                    
                    // Add error details as visible parameters
                    Allure.parameter("❌ Error Category", errorCategory);
                    Allure.parameter("💥 Error Message", t.getMessage());
                    Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-price-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/priceservice/prices\n");
                    errorDetails.append("Expected Status: 201\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("✅ Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-price-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/priceservice/prices\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add comprehensive test summary to Allure report
        StringBuilder summary = new StringBuilder();
        summary.append("Test Scenario Summary:\n");
        summary.append("Total Steps: ").append(totalSteps).append("\n");
        summary.append("Successful Steps: ").append(successfulSteps).append("\n");
        summary.append("Failed Steps: ").append(failedSteps).append("\n");
        summary.append("Login Status: ").append(loginSucceeded.get() ? "SUCCESS" : "FAILED").append("\n\n");
        
        // Add step-by-step breakdown
        summary.append("Step Breakdown:\n");
        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {
            String status = step.getValue() ? "✓ PASS" : "✗ FAIL";
            summary.append("  Step ").append(step.getKey()).append(": ").append(status).append("\n");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        
        // Categorize test result for better reporting
        if (!loginSucceeded.get()) {
            Allure.label("testType", "Authentication Failure");
            Allure.label("severity", "critical");
        } else if (failedSteps == 0) {
            Allure.label("testType", "Complete Success");
            Allure.label("severity", "normal");
        } else if (successfulSteps > 0) {
            Allure.label("testType", "Partial Failure");
            Allure.label("severity", "major");
        } else {
            Allure.label("testType", "Complete Failure");
            Allure.label("severity", "critical");
        }
        
        // Add scenario metadata
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_31_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_31_10");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
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
