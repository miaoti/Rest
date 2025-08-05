package trainticket_twostage_test.TrainTicketTwoStageTest_1754434645926;

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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"8.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"b2d5f8a9\",\"routeId\":\"0b23bd3e-876a-4af3-b920-c50a90c90b04\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"8.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"b2d5f8a9\",\"routeId\":\"0b23bd3e-876a-4af3-b920-c50a90c90b04\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"9.5\",\"firstClassPriceRate\":\"0.95\",\"id\":\"transactions).\",\"routeId\":\"train route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"9.5\",\"firstClassPriceRate\":\"0.95\",\"id\":\"transactions).\",\"routeId\":\"train route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"11.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"objects\",\"routeId\":\"delivery route). The format appears to be a UUID.\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"11.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"objects\",\"routeId\":\"delivery route). The format appears to be a UUID.\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"0.95\",\"id\":\"database records\",\"routeId\":\"bus route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"0.99\",\"firstClassPriceRate\":\"0.95\",\"id\":\"database records\",\"routeId\":\"bus route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"10.0\",\"firstClassPriceRate\":\"1.5\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"routeId\":\"9d4b6e1c-7a2f-4f3d-a1c4\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"10.0\",\"firstClassPriceRate\":\"1.5\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"routeId\":\"9d4b6e1c-7a2f-4f3d-a1c4\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"5.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"b2d5f8a9\",\"routeId\":\"0b23bd3e-876a-4af3-b920-c50a90c90b04\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"5.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"b2d5f8a9\",\"routeId\":\"0b23bd3e-876a-4af3-b920-c50a90c90b04\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"7.0\",\"firstClassPriceRate\":\"1.5\",\"id\":\"transactions).\",\"routeId\":\"train route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"7.0\",\"firstClassPriceRate\":\"1.5\",\"id\":\"transactions).\",\"routeId\":\"train route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"9.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"objects\",\"routeId\":\"delivery route). The format appears to be a UUID.\",\"trainType\":\"highspeed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"9.0\",\"firstClassPriceRate\":\"0.95\",\"id\":\"objects\",\"routeId\":\"delivery route). The format appears to be a UUID.\",\"trainType\":\"highspeed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"6.5\",\"firstClassPriceRate\":\"0.95\",\"id\":\"database records\",\"routeId\":\"bus route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"6.5\",\"firstClassPriceRate\":\"0.95\",\"id\":\"database records\",\"routeId\":\"bus route\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-basic-info-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-basic-info-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminbasicservice/adminbasic/prices\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody1 = "{\"basicPriceRate\":\"7.5\",\"firstClassPriceRate\":\"0.95\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"routeId\":\"9d4b6e1c-7a2f-4f3d-a1c4\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody1);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody1);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            Allure.step("Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)", () -> {
                Allure.parameter("🏢 Service", "ts-price-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/priceservice/prices");
                Allure.parameter("🎯 Expected Status", 201);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-price-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/priceservice/prices\n" +
                                 "🎯 **Expected**: 201\n" +
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
                    System.out.println("▶️ EXECUTING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies
                        req = req.contentType("application/json");
                        String requestBody2 = "{\"basicPriceRate\":\"7.5\",\"firstClassPriceRate\":\"0.95\",\"id\":\"06f71780-c647-465c-969f-cf8c019bc45f\",\"routeId\":\"9d4b6e1c-7a2f-4f3d-a1c4\",\"trainType\":\"High-Speed\"}";
                        req = req.body(requestBody2);
                        
                        // Add request details as single attachment
                        Allure.addAttachment("📤 Request Body", "application/json", requestBody2);
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
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
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        Allure.label("story", "test_POST_31_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
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
