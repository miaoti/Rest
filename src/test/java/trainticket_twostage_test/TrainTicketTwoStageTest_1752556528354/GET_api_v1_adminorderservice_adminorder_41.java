package trainticket_twostage_test.TrainTicketTwoStageTest_1752556528354;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.Assert.*;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.model.Status;

public class GET_api_v1_adminorderservice_adminorder_41 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_GET_41_1() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_1");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_2() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_2");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_3() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_3");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_4() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_4");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_5() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_5");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_6() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_6");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_7() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_7");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_8() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_8");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_9() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_9");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_41_10() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-admin-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderservice/order\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderservice/order\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-service GET /api/v1/orderservice/order (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderservice/order");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SUCCESS");
                    // ✅ Step completed successfully - capture response details
                    try {
                        String responseBody = stepResponse3.getBody().asString();
                        int actualStatus = stepResponse3.getStatusCode();
                        long responseTime = stepResponse3.getTime();
                        
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
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Service: ts-order-other-service\n");
                    errorDetails.append("Method: GET\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther\n");
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
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
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
        Allure.label("story", "test_GET_41_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_41_10");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
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
