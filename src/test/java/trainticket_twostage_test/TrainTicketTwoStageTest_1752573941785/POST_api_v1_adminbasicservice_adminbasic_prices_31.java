package trainticket_twostage_test.TrainTicketTwoStageTest_1752573941785;

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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"234.78\",\"firstClassPriceRate\":\"23.99\",\"id\":\"12345\",\"routeId\":\"unique_route_007\",\"trainType\":\"passengers\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"234.78\",\"firstClassPriceRate\":\"23.99\",\"id\":\"12345\",\"routeId\":\"unique_route_007\",\"trainType\":\"passengers\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"10.95\",\"firstClassPriceRate\":\"45.678\",\"id\":\"opqrst\",\"routeId\":\"XYZ789\",\"trainType\":\"Express\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"10.95\",\"firstClassPriceRate\":\"45.678\",\"id\":\"opqrst\",\"routeId\":\"XYZ789\",\"trainType\":\"Express\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"10000.00\",\"firstClassPriceRate\":\"23.99\",\"id\":\"67890\",\"routeId\":\"abcde\",\"trainType\":\"freight\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"10000.00\",\"firstClassPriceRate\":\"23.99\",\"id\":\"67890\",\"routeId\":\"abcde\",\"trainType\":\"freight\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"45.678\",\"id\":\"12345\",\"routeId\":\"XYZ789\",\"trainType\":\"pasenger\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"45.678\",\"id\":\"12345\",\"routeId\":\"XYZ789\",\"trainType\":\"pasenger\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"45.678\",\"id\":\"Hijklmn\",\"routeId\":\"abcde\",\"trainType\":\"pasenger\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"45.678\",\"id\":\"Hijklmn\",\"routeId\":\"abcde\",\"trainType\":\"pasenger\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"10.95\",\"firstClassPriceRate\":\"23.99\",\"id\":\"hijklmn\",\"routeId\":\"printf\",\"trainType\":\"passanger\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"10.95\",\"firstClassPriceRate\":\"23.99\",\"id\":\"hijklmn\",\"routeId\":\"printf\",\"trainType\":\"passanger\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"234.78\",\"firstClassPriceRate\":\"23.99\",\"id\":\"Hijklmn\",\"routeId\":\"xyz789\",\"trainType\":\"Freight\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"234.78\",\"firstClassPriceRate\":\"23.99\",\"id\":\"Hijklmn\",\"routeId\":\"xyz789\",\"trainType\":\"Freight\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"45.678\",\"id\":\"12345\",\"routeId\":\"routes\",\"trainType\":\"freights\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"45.678\",\"id\":\"12345\",\"routeId\":\"routes\",\"trainType\":\"freights\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"10.95\",\"firstClassPriceRate\":\"89.001\",\"id\":\"opqrst\",\"routeId\":\"path\",\"trainType\":\"passengers\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"10.95\",\"firstClassPriceRate\":\"89.001\",\"id\":\"opqrst\",\"routeId\":\"path\",\"trainType\":\"passengers\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
        // 🔐 STEP 0: Authentication - Always show in Allure report
        // Create dynamic authentication title with status - will be updated based on result
        final String[] authStepTitle = {"⏳ Step 0: Authentication (Login)"};
        Allure.step(() -> authStepTitle[0], () -> {
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
                
                // ✅ SUCCESS: Update authentication title and add success symbols
                authStepTitle[0] = "✅ Step 0: Authentication (Login)";
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.parameter("📊 Final Result", "✅ SUCCESS");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Update authentication title and add failure symbols
                authStepTitle[0] = "❌ Step 0: Authentication (Login)";
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody1 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"23.99\",\"id\":\"hijklmn\",\"routeId\":\"Abcde\",\"trainType\":\"cargoes\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody1);
                        req = req.body(requestBody1);
                        Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
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
                        System.out.println("❌ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                        
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
                        errorDetails.append("Service: ts-admin-basic-info-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200)";
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-admin-basic-info-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices (expect 200): " + stepException.getMessage());
            stepResults.put(1, false);
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        try {
            // Create dynamic step title with status - will be updated based on result
            final String[] dynamicStepTitle = {"⏳ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)"};
            Allure.step(() -> dynamicStepTitle[0], () -> {
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
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        String requestBody2 = "{\"basicPriceRate\":\"0.00\",\"firstClassPriceRate\":\"23.99\",\"id\":\"hijklmn\",\"routeId\":\"Abcde\",\"trainType\":\"cargoes\"}";
                        Allure.addAttachment("📋 Request Body", "application/json", requestBody2);
                        req = req.body(requestBody2);
                        Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                               .then().log().ifValidationFails()
                               .statusCode(201)
                               .extract().response();
                        stepResults.put(2, true);
                        System.out.println("✅ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - SUCCESS");
                        // ✅ SUCCESS: Update step title and add success parameters
                        dynamicStepTitle[0] = "✅ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
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
                        System.out.println("❌ Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Update step title and add failure symbols
                        dynamicStepTitle[0] = "❌ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                        
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
                        errorDetails.append("Service: ts-price-service\n");
                        errorDetails.append("Method: POST\n");
                        errorDetails.append("Path: /api/v1/priceservice/prices\n");
                        errorDetails.append("Expected Status: 201\n\n");
                        errorDetails.append("💥 ERROR INFO:\n");
                        errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                        errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                        errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                        
                        Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                        
                        // DON'T throw - let other steps execute
                        // Instead, mark step as failed but continue
                    }
                } else {
                    // ⏭️ SKIP: Update step title and add skip symbols
                    dynamicStepTitle[0] = "⏭️ " + "Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201)";
                    System.out.println("⏭️ SKIPPING: Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201) - " + skipReason);
                    stepResults.put(2, false);
                    
                    // Add comprehensive SKIP information with yellow symbols
                    Allure.parameter("⏭️ Skip Category", skipCategory);
                    Allure.parameter("💬 Skip Details", skipReason);
                    Allure.parameter("📊 Final Result", "⏭️ SKIPPED");
                    
                    // Generate detailed dependency analysis report
                    StringBuilder dependencyReport = new StringBuilder();
                    dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                    dependencyReport.append("📋 STEP INFO:\n");
                    dependencyReport.append("Service: ts-price-service\n");
                    dependencyReport.append("Method: POST\n");
                    dependencyReport.append("Path: /api/v1/priceservice/prices\n");
                    dependencyReport.append("Expected Status: 201\n\n");
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
            System.out.println("⚠️ Step wrapper failed for Step 2: ts-price-service POST /api/v1/priceservice/prices (expect 201): " + stepException.getMessage());
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
