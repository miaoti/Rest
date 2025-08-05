package trainticket_twostage_test.TrainTicketTwoStageTest_1754432989522;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AssumptionViolatedException;
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f\",\"boughtDate\":\"2013-05-04 09:00:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"2135488099312X\",\"contactsName\":\"John Doe\",\"differenceMoney\":\"10000.0\",\"documentType\":\"1\",\"from\":\"shanghai\",\"id\":\"12345\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat Service\",\"status\":\"1\",\"to\":\"PDX\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f\",\"boughtDate\":\"2013-05-04 09:00:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"2135488099312X\",\"contactsName\":\"John Doe\",\"documentType\":\"1\",\"from\":\"shanghai\",\"id\":\"12345\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat Service\",\"status\":\"1\",\"to\":\"PDX\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"1234567890\",\"boughtDate\":\"2023-10-26\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"2135488099312X (duplicate - avoiding)\",\"contactsName\":\"fdse_microservice\",\"differenceMoney\":\"12345.78\",\"documentType\":\"1\",\"from\":\"suzhou\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"price\":\"8.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat E5\",\"status\":\"suzhou\",\"to\":\"beijing\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"1234567890\",\"boughtDate\":\"2023-10-26\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"2135488099312X (duplicate - avoiding)\",\"contactsName\":\"fdse_microservice\",\"documentType\":\"1\",\"from\":\"suzhou\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"price\":\"8.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat E5\",\"status\":\"suzhou\",\"to\":\"beijing\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"abcde123-4567-89ab-cdef-1234\",\"boughtDate\":\"2013-05-04 17:51:52\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"345678901234567\",\"contactsName\":\"trainticket_notify@163.com\",\"differenceMoney\":\"100\",\"documentType\":\"1\",\"from\":\"taiyuan\",\"id\":\"ghijklm-9876-5432-10fe-d7c8b9a12\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 99\",\"status\":\"shanghai\",\"to\":\"ORD\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"abcde123-4567-89ab-cdef-1234\",\"boughtDate\":\"2013-05-04 17:51:52\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"345678901234567\",\"contactsName\":\"trainticket_notify@163.com\",\"documentType\":\"1\",\"from\":\"taiyuan\",\"id\":\"ghijklm-9876-5432-10fe-d7c8b9a12\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 99\",\"status\":\"shanghai\",\"to\":\"ORD\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"xyz12345-uvwqr-stuvwx-yzabcd-efghijk\",\"boughtDate\":\"2013-05-04 12:00:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"98765432101234\",\"contactsName\":\"customer_success_ops\",\"differenceMoney\":\"100\",\"documentType\":\"1\",\"from\":\"wuhan\",\"id\":\"98765432-bcdef-4321-fedcba98-76543210fedc\",\"price\":\"2.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 789\",\"status\":\"taiyuan\",\"to\":\"LAX\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"xyz12345-uvwqr-stuvwx-yzabcd-efghijk\",\"boughtDate\":\"2013-05-04 12:00:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"98765432101234\",\"contactsName\":\"customer_success_ops\",\"documentType\":\"1\",\"from\":\"wuhan\",\"id\":\"98765432-bcdef-4321-fedcba98-76543210fedc\",\"price\":\"2.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 789\",\"status\":\"taiyuan\",\"to\":\"LAX\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f\",\"boughtDate\":\"2013-05-04 15:51:52\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"65432109876543\",\"contactsName\":\"technical_support_dept\",\"differenceMoney\":\"30000.00\",\"documentType\":\"1\",\"from\":\"xian\",\"id\":\"pqrstuvw-1234-5678-9abc-def012345678\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 3A\",\"status\":\"guangzhou\",\"to\":\"MIA\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f\",\"boughtDate\":\"2013-05-04 15:51:52\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"65432109876543\",\"contactsName\":\"technical_support_dept\",\"documentType\":\"1\",\"from\":\"xian\",\"id\":\"pqrstuvw-1234-5678-9abc-def012345678\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 3A\",\"status\":\"guangzhou\",\"to\":\"MIA\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"abcde123-4567-89ab-cdef-1234\",\"boughtDate\":\"2013-05-04 10:30:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"123456789012345\",\"contactsName\":\"billing_inquiries_team\",\"differenceMoney\":\"8999.99\",\"documentType\":\"1\",\"from\":\"guangzhou\",\"id\":\"xyz1234-5678-9abc-def0-123456789xyz\",\"price\":\"4.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 14\",\"status\":\"chengdu\",\"to\":\"ATL\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"abcde123-4567-89ab-cdef-1234\",\"boughtDate\":\"2013-05-04 10:30:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"123456789012345\",\"contactsName\":\"billing_inquiries_team\",\"documentType\":\"1\",\"from\":\"guangzhou\",\"id\":\"xyz1234-5678-9abc-def0-123456789xyz\",\"price\":\"4.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 14\",\"status\":\"chengdu\",\"to\":\"ATL\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"xyz12345-uvwqr-stuvwx-yzabcd-efghijk\",\"boughtDate\":\"2013-05-04 11:15:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"876543210987654\",\"contactsName\":\"marketing_communications\",\"differenceMoney\":\"100\",\"documentType\":\"1\",\"from\":\"chengdu\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 45\",\"status\":\"dalian\",\"to\":\"BCN\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-15\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"xyz12345-uvwqr-stuvwx-yzabcd-efghijk\",\"boughtDate\":\"2013-05-04 11:15:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"876543210987654\",\"contactsName\":\"marketing_communications\",\"documentType\":\"1\",\"from\":\"chengdu\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"price\":\"19.99\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 45\",\"status\":\"dalian\",\"to\":\"BCN\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-15\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f\",\"boughtDate\":\"2013-05-04 19:10:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"901234567890123\",\"contactsName\":\"service_alerts\",\"differenceMoney\":\"15000.25\",\"documentType\":\"1\",\"from\":\"dalian\",\"id\":\"ghijklm-9876-5432-10fe-d7c8b9a12\",\"price\":\"10. 16.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 102\",\"status\":\"shenzhen\",\"to\":\"SFO\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f\",\"boughtDate\":\"2013-05-04 19:10:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"901234567890123\",\"contactsName\":\"service_alerts\",\"documentType\":\"1\",\"from\":\"dalian\",\"id\":\"ghijklm-9876-5432-10fe-d7c8b9a12\",\"price\":\"10. 16.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 102\",\"status\":\"shenzhen\",\"to\":\"SFO\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"abcde123-4567-89ab-cdef-1234\",\"boughtDate\":\"2013-05-04 16:45:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"789012345678901\",\"contactsName\":\"data_integrity_monitor\",\"differenceMoney\":\"75000.00\",\"documentType\":\"1\",\"from\":\"chongqing\",\"id\":\"98765432-bcdef-4321-fedcba98-76543210fedc\",\"price\":\"64. 128.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat D10\",\"status\":\"chongqing\",\"to\":\"JFK\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"abcde123-4567-89ab-cdef-1234\",\"boughtDate\":\"2013-05-04 16:45:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"789012345678901\",\"contactsName\":\"data_integrity_monitor\",\"documentType\":\"1\",\"from\":\"chongqing\",\"id\":\"98765432-bcdef-4321-fedcba98-76543210fedc\",\"price\":\"64. 128.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat D10\",\"status\":\"chongqing\",\"to\":\"JFK\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
        // 🔐 STEP 0: Authentication - Clean reporting
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
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"accountId\":\"xyz12345-uvwqr-stuvwx-yzabcd-efghijk\",\"boughtDate\":\"2013-05-04 18:30:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"456789012345678\",\"contactsName\":\"support_team_contact\",\"differenceMoney\":\"5000.50\",\"documentType\":\"1\",\"from\":\"shanghai\",\"id\":\"pqrstuvw-1234-5678-9abc-def012345678\",\"price\":\"25. 50.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 22B\",\"status\":\"1\",\"to\":\"SEA\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"xyz12345-uvwqr-stuvwx-yzabcd-efghijk\",\"boughtDate\":\"2013-05-04 18:30:00\",\"coachNumber\":\"12345\",\"contactsDocumentNumber\":\"456789012345678\",\"contactsName\":\"support_team_contact\",\"documentType\":\"1\",\"from\":\"shanghai\",\"id\":\"pqrstuvw-1234-5678-9abc-def012345678\",\"price\":\"25. 50.0\",\"seatClass\":\"1\",\"seatNumber\":\"Seat 22B\",\"status\":\"1\",\"to\":\"SEA\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-03-10\",\"travelTime\":\"1h 30m\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse2.getBody().asString();
                            int actualStatus = stepResponse2.getStatusCode();
                            long responseTime = stepResponse2.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(2, false);
                        System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Clean failure reporting with proper Allure status
                        String errorType = t.getClass().getSimpleName();
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                        }
                        
                        // Single failure status parameter
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        
                        // Single error attachment
                        Allure.addAttachment("💥 Error Details", "text/plain", "Error: " + errorType + "\nMessage: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step failed: " + errorType, t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 2 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 2 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200): " + stepException.getMessage());
                stepResults.put(2, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_POST_21_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
