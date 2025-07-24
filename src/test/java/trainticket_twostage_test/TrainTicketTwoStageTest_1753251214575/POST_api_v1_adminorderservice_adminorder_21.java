package trainticket_twostage_test.TrainTicketTwoStageTest_1753251214575;

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
                        String requestBody1 = "{\"accountId\":\"yyyy\",\"boughtDate\":\"2023-09-15\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"Testdoc#1\",\"contactsName\":\"emily brown\",\"differenceMoney\":\"123.45\",\"documentType\":\"384\",\"from\":\"Charlie Davis\",\"id\":\"abcdefg\",\"price\":\"10.99\",\"seatClass\":\"4\",\"seatNumber\":\"d78\",\"status\":\"-5\",\"to\":\"testing\",\"trainNumber\":\"printf\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-12-31t23:59:59z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"yyyy\",\"boughtDate\":\"2023-09-15\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"Testdoc#1\",\"contactsName\":\"emily brown\",\"documentType\":\"384\",\"from\":\"Charlie Davis\",\"id\":\"abcdefg\",\"price\":\"10.99\",\"seatClass\":\"4\",\"seatNumber\":\"d78\",\"status\":\"-5\",\"to\":\"testing\",\"trainNumber\":\"printf\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-12-31t23:59:59z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"Qwerty_keypad\",\"boughtDate\":\"2019-12-19\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"Testdoc#1\",\"contactsName\":\"michael davis\",\"differenceMoney\":\"-50\",\"documentType\":\"384\",\"from\":\"Jane Smith\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"10.99\",\"seatClass\":\"8\",\"seatNumber\":\"D78\",\"status\":\"-5\",\"to\":\"User@example.com\",\"trainNumber\":\"Abc123\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-01-20t14:30:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"Qwerty_keypad\",\"boughtDate\":\"2019-12-19\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"Testdoc#1\",\"contactsName\":\"michael davis\",\"documentType\":\"384\",\"from\":\"Jane Smith\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"10.99\",\"seatClass\":\"8\",\"seatNumber\":\"D78\",\"status\":\"-5\",\"to\":\"User@example.com\",\"trainNumber\":\"Abc123\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-01-20t14:30:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"xyz789\",\"boughtDate\":\"2023-09-15\",\"coachNumber\":\"BORDER_=\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"jane smith\",\"differenceMoney\":\"-50\",\"documentType\":\"2048\",\"from\":\"Bob brown\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"10.99\",\"seatClass\":\"5\",\"seatNumber\":\"C56\",\"status\":\"-4\",\"to\":\"Info(contact)@company.info\",\"trainNumber\":\"brennan\",\"travelDate\":\"2030-03-21\",\"travelTime\":\"2021-06-10t07:00:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"xyz789\",\"boughtDate\":\"2023-09-15\",\"coachNumber\":\"BORDER_=\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"jane smith\",\"documentType\":\"2048\",\"from\":\"Bob brown\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"10.99\",\"seatClass\":\"5\",\"seatNumber\":\"C56\",\"status\":\"-4\",\"to\":\"Info(contact)@company.info\",\"trainNumber\":\"brennan\",\"travelDate\":\"2030-03-21\",\"travelTime\":\"2021-06-10t07:00:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"Abc123\",\"boughtDate\":\"2022-04-30\",\"coachNumber\":\"30\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"John Doe\",\"differenceMoney\":\"0.99\",\"documentType\":\"384\",\"from\":\"Bob Brown\",\"id\":\"User_1\",\"price\":\"50.50\",\"seatClass\":\"6\",\"seatNumber\":\"c56\",\"status\":\"3\",\"to\":\"Admin@domain.org\",\"trainNumber\":\"nbc\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2024-01-20T14:30:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"Abc123\",\"boughtDate\":\"2022-04-30\",\"coachNumber\":\"30\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"John Doe\",\"documentType\":\"384\",\"from\":\"Bob Brown\",\"id\":\"User_1\",\"price\":\"50.50\",\"seatClass\":\"6\",\"seatNumber\":\"c56\",\"status\":\"3\",\"to\":\"Admin@domain.org\",\"trainNumber\":\"nbc\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2024-01-20T14:30:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"admin101\",\"boughtDate\":\"2024-02-28\",\"coachNumber\":\"-98765\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"Michael davis\",\"differenceMoney\":\"123.45\",\"documentType\":\"2048\",\"from\":\"jane smith\",\"id\":\"special+char!\",\"price\":\"10.99\",\"seatClass\":\"2\",\"seatNumber\":\"B34\",\"status\":\"-6\",\"to\":\"testing\",\"trainNumber\":\"67890\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2021-06-10T07:00:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"admin101\",\"boughtDate\":\"2024-02-28\",\"coachNumber\":\"-98765\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"Michael davis\",\"documentType\":\"2048\",\"from\":\"jane smith\",\"id\":\"special+char!\",\"price\":\"10.99\",\"seatClass\":\"2\",\"seatNumber\":\"B34\",\"status\":\"-6\",\"to\":\"testing\",\"trainNumber\":\"67890\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2021-06-10T07:00:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"Qwerty\",\"boughtDate\":\"2019-12-19\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"Abc-123-xyz\",\"contactsName\":\"Jane Smith\",\"differenceMoney\":\"-50\",\"documentType\":\"768\",\"from\":\"Jane smith\",\"id\":\"Special+char!\",\"price\":\"249.99\",\"seatClass\":\"2\",\"seatNumber\":\"B34\",\"status\":\"3\",\"to\":\"Admin@domain.org\",\"trainNumber\":\"Abc123\",\"travelDate\":\"2021-06-18\",\"travelTime\":\"2024-01-20T14:30:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"Qwerty\",\"boughtDate\":\"2019-12-19\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"Abc-123-xyz\",\"contactsName\":\"Jane Smith\",\"documentType\":\"768\",\"from\":\"Jane smith\",\"id\":\"Special+char!\",\"price\":\"249.99\",\"seatClass\":\"2\",\"seatNumber\":\"B34\",\"status\":\"3\",\"to\":\"Admin@domain.org\",\"trainNumber\":\"Abc123\",\"travelDate\":\"2021-06-18\",\"travelTime\":\"2024-01-20T14:30:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"qwerty_keyboard\",\"boughtDate\":\"2021-07-06\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"Michael davis\",\"differenceMoney\":\"-0.01\",\"documentType\":\"768\",\"from\":\"Jane Smith\",\"id\":\"abcdefg\",\"price\":\"999.99\",\"seatClass\":\"5\",\"seatNumber\":\"B34\",\"status\":\"10\",\"to\":\"admin@domain.org\",\"trainNumber\":\"yyyy\",\"travelDate\":\"2022-07-29\",\"travelTime\":\"2023-03-15t18:45:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"qwerty_keyboard\",\"boughtDate\":\"2021-07-06\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"Michael davis\",\"documentType\":\"768\",\"from\":\"Jane Smith\",\"id\":\"abcdefg\",\"price\":\"999.99\",\"seatClass\":\"5\",\"seatNumber\":\"B34\",\"status\":\"10\",\"to\":\"admin@domain.org\",\"trainNumber\":\"yyyy\",\"travelDate\":\"2022-07-29\",\"travelTime\":\"2023-03-15t18:45:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"xyz789\",\"boughtDate\":\"2019-12-19\",\"coachNumber\":\"30\",\"contactsDocumentNumber\":\"HelloWorld123\",\"contactsName\":\"Emily brown\",\"differenceMoney\":\"123.45\",\"documentType\":\"512\",\"from\":\"bob brown\",\"id\":\"7890\",\"price\":\"50.50\",\"seatClass\":\"5\",\"seatNumber\":\"d78\",\"status\":\"2048\",\"to\":\"Info(contact)@company.info\",\"trainNumber\":\"cbs\",\"travelDate\":\"2021-06-18\",\"travelTime\":\"2024-01-20T14:30:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"xyz789\",\"boughtDate\":\"2019-12-19\",\"coachNumber\":\"30\",\"contactsDocumentNumber\":\"HelloWorld123\",\"contactsName\":\"Emily brown\",\"documentType\":\"512\",\"from\":\"bob brown\",\"id\":\"7890\",\"price\":\"50.50\",\"seatClass\":\"5\",\"seatNumber\":\"d78\",\"status\":\"2048\",\"to\":\"Info(contact)@company.info\",\"trainNumber\":\"cbs\",\"travelDate\":\"2021-06-18\",\"travelTime\":\"2024-01-20T14:30:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"Qwerty_keyboard\",\"boughtDate\":\"2023-09-15\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"testdoc#1\",\"contactsName\":\"Emily Brown\",\"differenceMoney\":\"-0.01\",\"documentType\":\"768\",\"from\":\"bob brown\",\"id\":\"abcdefg\",\"price\":\"10.99\",\"seatClass\":\"6\",\"seatNumber\":\"b34\",\"status\":\"4\",\"to\":\"Test123@test.net\",\"trainNumber\":\"printf\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2023-10-05T08:00:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"Qwerty_keyboard\",\"boughtDate\":\"2023-09-15\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"testdoc#1\",\"contactsName\":\"Emily Brown\",\"documentType\":\"768\",\"from\":\"bob brown\",\"id\":\"abcdefg\",\"price\":\"10.99\",\"seatClass\":\"6\",\"seatNumber\":\"b34\",\"status\":\"4\",\"to\":\"Test123@test.net\",\"trainNumber\":\"printf\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2023-10-05T08:00:00Z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        String requestBody1 = "{\"accountId\":\"Qwerty_keypad\",\"boughtDate\":\"2021-07-06\",\"coachNumber\":\"-98765\",\"contactsDocumentNumber\":\"brennan\",\"contactsName\":\"Michael Davis\",\"differenceMoney\":\"0.99\",\"documentType\":\"1024\",\"from\":\"Jane smith\",\"id\":\"users\",\"price\":\"50.50\",\"seatClass\":\"4\",\"seatNumber\":\"b34\",\"status\":\"4\",\"to\":\"support_+49(0)123456789@service.de\",\"trainNumber\":\"abc123\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2023-03-15t18:45:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"accountId\":\"Qwerty_keypad\",\"boughtDate\":\"2021-07-06\",\"coachNumber\":\"-98765\",\"contactsDocumentNumber\":\"brennan\",\"contactsName\":\"Michael Davis\",\"documentType\":\"1024\",\"from\":\"Jane smith\",\"id\":\"users\",\"price\":\"50.50\",\"seatClass\":\"4\",\"seatNumber\":\"b34\",\"status\":\"4\",\"to\":\"support_+49(0)123456789@service.de\",\"trainNumber\":\"abc123\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2023-03-15t18:45:00z\"}";
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
                        
                        // ❌ FAILURE: Clean failure reporting without duplication
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
                        
                        // DON'T throw - let other steps execute
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting without duplication
                    System.out.println("⏭️ SKIPPING: Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
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
