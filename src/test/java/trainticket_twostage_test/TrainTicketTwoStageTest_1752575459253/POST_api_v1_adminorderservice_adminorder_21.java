package trainticket_twostage_test.TrainTicketTwoStageTest_1752575459253;

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

public class POST_api_v1_adminorderservice_adminorder_21 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_POST_21_1() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"printf\",\"boughtDate\":\"2023-01-01T00:00:00Z\",\"coachNumber\":\"9\",\"contactsDocumentNumber\":\"Abc-def-ghij\",\"contactsName\":\"John doe\",\"differenceMoney\":\"-$20.75\",\"documentType\":\"-4\",\"from\":\"houston\",\"id\":\"12345\",\"price\":\"50.00\",\"seatClass\":\"8\",\"seatNumber\":\"c34d\",\"status\":\"-2\",\"to\":\"janedoe\",\"trainNumber\":\"nmlz789\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2023-11-30T18:45\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"printf\",\"boughtDate\":\"2023-01-01T00:00:00Z\",\"coachNumber\":\"9\",\"contactsDocumentNumber\":\"Abc-def-ghij\",\"contactsName\":\"John doe\",\"documentType\":\"-4\",\"from\":\"houston\",\"id\":\"12345\",\"price\":\"50.00\",\"seatClass\":\"8\",\"seatNumber\":\"c34d\",\"status\":\"-2\",\"to\":\"janedoe\",\"trainNumber\":\"nmlz789\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2023-11-30T18:45\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_1");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_2() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"xyz789\",\"boughtDate\":\"2010-06-15t08:30:45z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"Klmn-o-pqrs\",\"contactsName\":\"charlie davis\",\"differenceMoney\":\"-$20.75\",\"documentType\":\"7654321\",\"from\":\"New York\",\"id\":\"abcde\",\"price\":\"10.99\",\"seatClass\":\"8\",\"seatNumber\":\"g89\",\"status\":\"-3\",\"to\":\"Test@example.com\",\"trainNumber\":\"XYZ1234\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2023-10-05T09:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"xyz789\",\"boughtDate\":\"2010-06-15t08:30:45z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"Klmn-o-pqrs\",\"contactsName\":\"charlie davis\",\"documentType\":\"7654321\",\"from\":\"New York\",\"id\":\"abcde\",\"price\":\"10.99\",\"seatClass\":\"8\",\"seatNumber\":\"g89\",\"status\":\"-3\",\"to\":\"Test@example.com\",\"trainNumber\":\"XYZ1234\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2023-10-05T09:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_2");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_3() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"ABC123\",\"boughtDate\":\"2023-01-01T00:00:00Z\",\"coachNumber\":\"7\",\"contactsDocumentNumber\":\"tuvw-x-yz\",\"contactsName\":\"Jane smith\",\"differenceMoney\":\"$0.99\",\"documentType\":\"-1\",\"from\":\"Houston\",\"id\":\"09876\",\"price\":\"25.49\",\"seatClass\":\"1\",\"seatNumber\":\"h6\",\"status\":\"-2\",\"to\":\"Test@example.com\",\"trainNumber\":\"Abcdefg\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2022-02-01T14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"ABC123\",\"boughtDate\":\"2023-01-01T00:00:00Z\",\"coachNumber\":\"7\",\"contactsDocumentNumber\":\"tuvw-x-yz\",\"contactsName\":\"Jane smith\",\"documentType\":\"-1\",\"from\":\"Houston\",\"id\":\"09876\",\"price\":\"25.49\",\"seatClass\":\"1\",\"seatNumber\":\"h6\",\"status\":\"-2\",\"to\":\"Test@example.com\",\"trainNumber\":\"Abcdefg\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2022-02-01T14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_3");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_4() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"nbc\",\"boughtDate\":\"2010-06-15T08:30:45Z\",\"coachNumber\":\"6\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"charlie davis\",\"differenceMoney\":\"-$20.75\",\"documentType\":\"2048\",\"from\":\"atlanta\",\"id\":\"User123\",\"price\":\"25.49\",\"seatClass\":\"4\",\"seatNumber\":\"g89\",\"status\":\"-2\",\"to\":\"Special@charter.net\",\"trainNumber\":\"Nmlz789\",\"travelDate\":\"2022-07-08\",\"travelTime\":\"2022-02-01t14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"nbc\",\"boughtDate\":\"2010-06-15T08:30:45Z\",\"coachNumber\":\"6\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"charlie davis\",\"documentType\":\"2048\",\"from\":\"atlanta\",\"id\":\"User123\",\"price\":\"25.49\",\"seatClass\":\"4\",\"seatNumber\":\"g89\",\"status\":\"-2\",\"to\":\"Special@charter.net\",\"trainNumber\":\"Nmlz789\",\"travelDate\":\"2022-07-08\",\"travelTime\":\"2022-02-01t14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_4");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_5() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"ABC123\",\"boughtDate\":\"2023-01-01t00:00:00z\",\"coachNumber\":\"456789\",\"contactsDocumentNumber\":\"tuvw-x-yz\",\"contactsName\":\"alice johnson\",\"differenceMoney\":\"-$20.75\",\"documentType\":\"2048\",\"from\":\"new york\",\"id\":\"buf\",\"price\":\"3.99\",\"seatClass\":\"3\",\"seatNumber\":\"f6\",\"status\":\"200\",\"to\":\"Test@example.com\",\"trainNumber\":\"abcdefg\",\"travelDate\":\"2024-01-01\",\"travelTime\":\"2023-10-05T09:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"ABC123\",\"boughtDate\":\"2023-01-01t00:00:00z\",\"coachNumber\":\"456789\",\"contactsDocumentNumber\":\"tuvw-x-yz\",\"contactsName\":\"alice johnson\",\"documentType\":\"2048\",\"from\":\"new york\",\"id\":\"buf\",\"price\":\"3.99\",\"seatClass\":\"3\",\"seatNumber\":\"f6\",\"status\":\"200\",\"to\":\"Test@example.com\",\"trainNumber\":\"abcdefg\",\"travelDate\":\"2024-01-01\",\"travelTime\":\"2023-10-05T09:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_5");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_6() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"buf\",\"boughtDate\":\"2024-07-04T12:00:00Z\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"**note:** the actual values provided are examples and should be replaced with real, appropriate values for the specific context in which they will be used.\",\"contactsName\":\"Jane smith\",\"differenceMoney\":\"-$20.75\",\"documentType\":\"5000\",\"from\":\"los angeles\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"3.99\",\"seatClass\":\"2\",\"seatNumber\":\"E56F\",\"status\":\"500\",\"to\":\"Test@example.com\",\"trainNumber\":\"printf\",\"travelDate\":\"2026-03-15\",\"travelTime\":\"2023-10-05t09:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"buf\",\"boughtDate\":\"2024-07-04T12:00:00Z\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"**note:** the actual values provided are examples and should be replaced with real, appropriate values for the specific context in which they will be used.\",\"contactsName\":\"Jane smith\",\"documentType\":\"5000\",\"from\":\"los angeles\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"3.99\",\"seatClass\":\"2\",\"seatNumber\":\"E56F\",\"status\":\"500\",\"to\":\"Test@example.com\",\"trainNumber\":\"printf\",\"travelDate\":\"2026-03-15\",\"travelTime\":\"2023-10-05t09:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_6");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_7() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"XYZ789\",\"boughtDate\":\"2023-01-01T00:00:00Z\",\"coachNumber\":\"9\",\"contactsDocumentNumber\":\"**note:** the actual values provided are examples and should be replaced with real, appropriate values for the specific context in which they will be used.\",\"contactsName\":\"Jane smith\",\"differenceMoney\":\"$10.50\",\"documentType\":\"1\",\"from\":\"miami\",\"id\":\"09876\",\"price\":\"25.49\",\"seatClass\":\"1\",\"seatNumber\":\"A12\",\"status\":\"-3\",\"to\":\"JaneDoe\",\"trainNumber\":\"67890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2023-11-30T18:45\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"XYZ789\",\"boughtDate\":\"2023-01-01T00:00:00Z\",\"coachNumber\":\"9\",\"contactsDocumentNumber\":\"**note:** the actual values provided are examples and should be replaced with real, appropriate values for the specific context in which they will be used.\",\"contactsName\":\"Jane smith\",\"documentType\":\"1\",\"from\":\"miami\",\"id\":\"09876\",\"price\":\"25.49\",\"seatClass\":\"1\",\"seatNumber\":\"A12\",\"status\":\"-3\",\"to\":\"JaneDoe\",\"trainNumber\":\"67890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2023-11-30T18:45\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_7");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_8() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"yyyy\",\"boughtDate\":\"2000-01-01T00:00:01Z\",\"coachNumber\":\"-987654\",\"contactsDocumentNumber\":\"Klmn-o-pqrs\",\"contactsName\":\"Charlie Davis\",\"differenceMoney\":\"-$20.75\",\"documentType\":\"2048\",\"from\":\"houston\",\"id\":\"xyz789\",\"price\":\"3.99\",\"seatClass\":\"1\",\"seatNumber\":\"G89\",\"status\":\"200\",\"to\":\"test@example.com\",\"trainNumber\":\"abcdefg\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2022-02-01t14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"yyyy\",\"boughtDate\":\"2000-01-01T00:00:01Z\",\"coachNumber\":\"-987654\",\"contactsDocumentNumber\":\"Klmn-o-pqrs\",\"contactsName\":\"Charlie Davis\",\"documentType\":\"2048\",\"from\":\"houston\",\"id\":\"xyz789\",\"price\":\"3.99\",\"seatClass\":\"1\",\"seatNumber\":\"G89\",\"status\":\"200\",\"to\":\"test@example.com\",\"trainNumber\":\"abcdefg\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2022-02-01t14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_8");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_9() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"QWE012\",\"boughtDate\":\"2024-07-04T12:00:00Z\",\"coachNumber\":\"456789\",\"contactsDocumentNumber\":\"Klmn-o-pqrs\",\"contactsName\":\"Charlie Davis\",\"differenceMoney\":\"$3,456.78\",\"documentType\":\"-1\",\"from\":\"new york\",\"id\":\"User123\",\"price\":\"3.99\",\"seatClass\":\"3\",\"seatNumber\":\"g89\",\"status\":\"-3\",\"to\":\"user123\",\"trainNumber\":\"Nmlz789\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2022-02-01t14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"QWE012\",\"boughtDate\":\"2024-07-04T12:00:00Z\",\"coachNumber\":\"456789\",\"contactsDocumentNumber\":\"Klmn-o-pqrs\",\"contactsName\":\"Charlie Davis\",\"documentType\":\"-1\",\"from\":\"new york\",\"id\":\"User123\",\"price\":\"3.99\",\"seatClass\":\"3\",\"seatNumber\":\"g89\",\"status\":\"-3\",\"to\":\"user123\",\"trainNumber\":\"Nmlz789\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2022-02-01t14:00\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_9");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_POST_21_10() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Always show in Allure report
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Add success symbols and status
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Add failure symbols and error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                Allure.parameter("📊 Final Result", "❌ FAILED");
                
                StringBuilder loginErrorDetails = new StringBuilder();
                loginErrorDetails.append("🚨 LOGIN FAILURE REPORT\n\n");
                loginErrorDetails.append("📋 LOGIN ATTEMPT:\n");
                loginErrorDetails.append("Endpoint: /api/v1/users/login\n");
                loginErrorDetails.append("Method: POST\n");
                loginErrorDetails.append("Expected Status: 200\n\n");
                loginErrorDetails.append("💥 ERROR INFO:\n");
                loginErrorDetails.append("Type: ").append(loginError.getClass().getSimpleName()).append("\n");
                loginErrorDetails.append("Message: ").append(loginError.getMessage()).append("\n\n");
                loginErrorDetails.append("⚠️ IMPACT:\n");
                loginErrorDetails.append("All subsequent steps will be skipped due to authentication failure.\n");
                
                Allure.addAttachment("🚨 Login Failure Details", "text/plain", loginErrorDetails.toString());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"accountId\":\"Abc123\",\"boughtDate\":\"2000-01-01t00:00:01z\",\"coachNumber\":\"6\",\"contactsDocumentNumber\":\"Abc-def-ghij\",\"contactsName\":\"Jane Smith\",\"differenceMoney\":\"-0.01\",\"documentType\":\"-1\",\"from\":\"New York\",\"id\":\"user123\",\"price\":\"3.99\",\"seatClass\":\"6\",\"seatNumber\":\"G89\",\"status\":\"-2\",\"to\":\"test@example.com\",\"trainNumber\":\"Abcdefg\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2023-11-30T18:45\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-admin-order-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-order-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminorderservice/adminorder\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"accountId\":\"Abc123\",\"boughtDate\":\"2000-01-01t00:00:01z\",\"coachNumber\":\"6\",\"contactsDocumentNumber\":\"Abc-def-ghij\",\"contactsName\":\"Jane Smith\",\"documentType\":\"-1\",\"from\":\"New York\",\"id\":\"user123\",\"price\":\"3.99\",\"seatClass\":\"6\",\"seatNumber\":\"G89\",\"status\":\"-2\",\"to\":\"test@example.com\",\"trainNumber\":\"Abcdefg\",\"travelDate\":\"2025-12-25\",\"travelTime\":\"2023-11-30T18:45\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Add success parameters and metrics
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Add response as prominently visible attachment
                            Allure.addAttachment("📄 Response (Status: " + actualStatus + ")", "application/json", responseBody);
                            
                            // Add SUCCESS metrics with green symbols
                            Allure.parameter("✅ Actual Status", actualStatus + " (SUCCESS)");
                            Allure.parameter("⏱️ Response Time", responseTime + " ms");
                            Allure.parameter("📊 Final Result", "✅ SUCCESS");
                        } catch (Exception e) {
                            Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Add failure symbols and error details
                        
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
                        
                        // Add FAILURE details with red symbols
                        Allure.parameter("❌ Error Category", errorCategory);
                        Allure.parameter("💥 Error Message", t.getMessage());
                        Allure.parameter("🔍 Exception Type", t.getClass().getSimpleName());
                        Allure.parameter("📊 Final Result", "❌ FAILED");
                        
                        // Create detailed error report
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                        errorDetails.append("📋 STEP INFO:\n");
                        errorDetails.append("Service: ts-order-other-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                        errorDetails.append("Expected Status: 200\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Add skip symbols and comprehensive information
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-order-other-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
                    dependencyReport.append("Expected Status: 200\n\n");
                    dependencyReport.append("⏭️ SKIP REASON:\n");
                    dependencyReport.append("Category: ").append(skipCategory).append("\n");
                    dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                    
                    // Add dependency analysis
                    if (!loginSucceeded.get()) {
                        dependencyReport.append("🔐 AUTHENTICATION STATUS: ❌ FAILED\n");
                        dependencyReport.append("Authentication is required for all API calls.\n\n");
                    }
                    Allure.addAttachment("⏭️ Dependency Analysis Report", "text/plain", dependencyReport.toString());
                }
            });
        } catch (Exception stepException) {
            // Step wrapper failed, but don't stop other steps
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
            stepResults.put(2, false);
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
        Allure.label("story", "test_POST_21_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_21_10");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
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
