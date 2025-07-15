package trainticket_twostage_test.TrainTicketTwoStageTest_1752559667288;

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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"12345\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Emily Davis\",\"differenceMoney\":\"10.50\",\"documentType\":\"789\",\"from\":\"Charlie789\",\"id\":\"1a2b3c\",\"price\":\"29.87\",\"seatClass\":\"premium economy\",\"seatNumber\":\"a12\",\"status\":\"6\",\"to\":\"suport\",\"trainNumber\":\"cbs\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29t11:00z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"12345\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Emily Davis\",\"differenceMoney\":\"10.50\",\"documentType\":\"789\",\"from\":\"Charlie789\",\"id\":\"1a2b3c\",\"price\":\"29.87\",\"seatClass\":\"premium economy\",\"seatNumber\":\"a12\",\"status\":\"6\",\"to\":\"suport\",\"trainNumber\":\"cbs\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29t11:00z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"12345\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Emily Davis\",\"documentType\":\"789\",\"from\":\"Charlie789\",\"id\":\"1a2b3c\",\"price\":\"29.87\",\"seatClass\":\"premium economy\",\"seatNumber\":\"a12\",\"status\":\"6\",\"to\":\"suport\",\"trainNumber\":\"cbs\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29t11:00z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"12345\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Emily Davis\",\"documentType\":\"789\",\"from\":\"Charlie789\",\"id\":\"1a2b3c\",\"price\":\"29.87\",\"seatClass\":\"premium economy\",\"seatNumber\":\"a12\",\"status\":\"6\",\"to\":\"suport\",\"trainNumber\":\"cbs\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29t11:00z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"-890\",\"contactsDocumentNumber\":\"Abc123456\",\"contactsName\":\"John Doe\",\"differenceMoney\":\"50.0\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"brennan\",\"price\":\"59.49\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"10\",\"to\":\"supporting\",\"trainNumber\":\"XYZ789\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15t14:45z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"-890\",\"contactsDocumentNumber\":\"Abc123456\",\"contactsName\":\"John Doe\",\"differenceMoney\":\"50.0\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"brennan\",\"price\":\"59.49\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"10\",\"to\":\"supporting\",\"trainNumber\":\"XYZ789\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15t14:45z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"-890\",\"contactsDocumentNumber\":\"Abc123456\",\"contactsName\":\"John Doe\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"brennan\",\"price\":\"59.49\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"10\",\"to\":\"supporting\",\"trainNumber\":\"XYZ789\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15t14:45z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"-890\",\"contactsDocumentNumber\":\"Abc123456\",\"contactsName\":\"John Doe\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"brennan\",\"price\":\"59.49\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"10\",\"to\":\"supporting\",\"trainNumber\":\"XYZ789\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15t14:45z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"users\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"XYZ789012\",\"contactsName\":\"John doe\",\"differenceMoney\":\"10.50\",\"documentType\":\"BORDER_=\",\"from\":\"backcombed\",\"id\":\"New york city\",\"price\":\"10.99\",\"seatClass\":\"Premium Economy\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"cbs\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05t08:30z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"users\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"XYZ789012\",\"contactsName\":\"John doe\",\"differenceMoney\":\"10.50\",\"documentType\":\"BORDER_=\",\"from\":\"backcombed\",\"id\":\"New york city\",\"price\":\"10.99\",\"seatClass\":\"Premium Economy\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"cbs\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05t08:30z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"users\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"XYZ789012\",\"contactsName\":\"John doe\",\"documentType\":\"BORDER_=\",\"from\":\"backcombed\",\"id\":\"New york city\",\"price\":\"10.99\",\"seatClass\":\"Premium Economy\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"cbs\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05t08:30z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"users\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"XYZ789012\",\"contactsName\":\"John doe\",\"documentType\":\"BORDER_=\",\"from\":\"backcombed\",\"id\":\"New york city\",\"price\":\"10.99\",\"seatClass\":\"Premium Economy\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"cbs\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05t08:30z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"testing\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"nbc\",\"contactsName\":\"Mike brown\",\"differenceMoney\":\"10.50\",\"documentType\":\"0\",\"from\":\"nikki\",\"id\":\"User123\",\"price\":\"59.49\",\"seatClass\":\"Buisness Class\",\"seatNumber\":\"b6\",\"status\":\"6\",\"to\":\"user@example.com\",\"trainNumber\":\"xyz789\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"testing\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"nbc\",\"contactsName\":\"Mike brown\",\"differenceMoney\":\"10.50\",\"documentType\":\"0\",\"from\":\"nikki\",\"id\":\"User123\",\"price\":\"59.49\",\"seatClass\":\"Buisness Class\",\"seatNumber\":\"b6\",\"status\":\"6\",\"to\":\"user@example.com\",\"trainNumber\":\"xyz789\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"testing\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"nbc\",\"contactsName\":\"Mike brown\",\"documentType\":\"0\",\"from\":\"nikki\",\"id\":\"User123\",\"price\":\"59.49\",\"seatClass\":\"Buisness Class\",\"seatNumber\":\"b6\",\"status\":\"6\",\"to\":\"user@example.com\",\"trainNumber\":\"xyz789\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"testing\",\"boughtDate\":\"2022-07-18T09:15:00Z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"nbc\",\"contactsName\":\"Mike brown\",\"documentType\":\"0\",\"from\":\"nikki\",\"id\":\"User123\",\"price\":\"59.49\",\"seatClass\":\"Buisness Class\",\"seatNumber\":\"b6\",\"status\":\"6\",\"to\":\"user@example.com\",\"trainNumber\":\"xyz789\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"test456\",\"boughtDate\":\"2024-03-21t08:20:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"Emily Davis\",\"differenceMoney\":\"10.50\",\"documentType\":\"var_bd_=_getElementsByClass\",\"from\":\"Bob123\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"premium economy\",\"seatNumber\":\"e9\",\"status\":\"4096\",\"to\":\"suport\",\"trainNumber\":\"7890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2025-12-31T23:59Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"test456\",\"boughtDate\":\"2024-03-21t08:20:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"Emily Davis\",\"differenceMoney\":\"10.50\",\"documentType\":\"var_bd_=_getElementsByClass\",\"from\":\"Bob123\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"premium economy\",\"seatNumber\":\"e9\",\"status\":\"4096\",\"to\":\"suport\",\"trainNumber\":\"7890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2025-12-31T23:59Z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"test456\",\"boughtDate\":\"2024-03-21t08:20:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"Emily Davis\",\"documentType\":\"var_bd_=_getElementsByClass\",\"from\":\"Bob123\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"premium economy\",\"seatNumber\":\"e9\",\"status\":\"4096\",\"to\":\"suport\",\"trainNumber\":\"7890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2025-12-31T23:59Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"test456\",\"boughtDate\":\"2024-03-21t08:20:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"Emily Davis\",\"documentType\":\"var_bd_=_getElementsByClass\",\"from\":\"Bob123\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"premium economy\",\"seatNumber\":\"e9\",\"status\":\"4096\",\"to\":\"suport\",\"trainNumber\":\"7890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2025-12-31T23:59Z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"67890\",\"boughtDate\":\"2023-10-05T14:30:00Z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"buf\",\"contactsName\":\"emily davis\",\"differenceMoney\":\"-20.75\",\"documentType\":\"0\",\"from\":\"Alice456\",\"id\":\"4567890\",\"price\":\"0.99\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"-30\",\"to\":\"supporting\",\"trainNumber\":\"nbc\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01t00:00z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"67890\",\"boughtDate\":\"2023-10-05T14:30:00Z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"buf\",\"contactsName\":\"emily davis\",\"differenceMoney\":\"-20.75\",\"documentType\":\"0\",\"from\":\"Alice456\",\"id\":\"4567890\",\"price\":\"0.99\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"-30\",\"to\":\"supporting\",\"trainNumber\":\"nbc\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01t00:00z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"67890\",\"boughtDate\":\"2023-10-05T14:30:00Z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"buf\",\"contactsName\":\"emily davis\",\"documentType\":\"0\",\"from\":\"Alice456\",\"id\":\"4567890\",\"price\":\"0.99\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"-30\",\"to\":\"supporting\",\"trainNumber\":\"nbc\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01t00:00z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"67890\",\"boughtDate\":\"2023-10-05T14:30:00Z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"buf\",\"contactsName\":\"emily davis\",\"documentType\":\"0\",\"from\":\"Alice456\",\"id\":\"4567890\",\"price\":\"0.99\",\"seatClass\":\"First Class\",\"seatNumber\":\"e1\",\"status\":\"-30\",\"to\":\"supporting\",\"trainNumber\":\"nbc\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01t00:00z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"test456\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"JKL012345\",\"contactsName\":\"Alice Johnson\",\"differenceMoney\":\"-20.75\",\"documentType\":\"789\",\"from\":\"bobs\",\"id\":\"user123\",\"price\":\"109.99\",\"seatClass\":\"First class\",\"seatNumber\":\"d4\",\"status\":\"256\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"test456\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"JKL012345\",\"contactsName\":\"Alice Johnson\",\"differenceMoney\":\"-20.75\",\"documentType\":\"789\",\"from\":\"bobs\",\"id\":\"user123\",\"price\":\"109.99\",\"seatClass\":\"First class\",\"seatNumber\":\"d4\",\"status\":\"256\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"test456\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"JKL012345\",\"contactsName\":\"Alice Johnson\",\"documentType\":\"789\",\"from\":\"bobs\",\"id\":\"user123\",\"price\":\"109.99\",\"seatClass\":\"First class\",\"seatNumber\":\"d4\",\"status\":\"256\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"test456\",\"boughtDate\":\"2021-06-02t10:45:00z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"JKL012345\",\"contactsName\":\"Alice Johnson\",\"documentType\":\"789\",\"from\":\"bobs\",\"id\":\"user123\",\"price\":\"109.99\",\"seatClass\":\"First class\",\"seatNumber\":\"d4\",\"status\":\"256\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-03-15T14:45Z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"brennan\",\"contactsName\":\"Mike Brown\",\"differenceMoney\":\"-0.99\",\"documentType\":\"10\",\"from\":\"joshua\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"Business\",\"seatNumber\":\"h6\",\"status\":\"7\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"Xyz789\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05T08:30Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"brennan\",\"contactsName\":\"Mike Brown\",\"differenceMoney\":\"-0.99\",\"documentType\":\"10\",\"from\":\"joshua\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"Business\",\"seatNumber\":\"h6\",\"status\":\"7\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"Xyz789\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05T08:30Z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"brennan\",\"contactsName\":\"Mike Brown\",\"documentType\":\"10\",\"from\":\"joshua\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"Business\",\"seatNumber\":\"h6\",\"status\":\"7\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"Xyz789\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05T08:30Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17T13:50:00Z\",\"coachNumber\":\"3456789\",\"contactsDocumentNumber\":\"brennan\",\"contactsName\":\"Mike Brown\",\"documentType\":\"10\",\"from\":\"joshua\",\"id\":\"cbs\",\"price\":\"109.99\",\"seatClass\":\"Business\",\"seatNumber\":\"h6\",\"status\":\"7\",\"to\":\"Info.contactwebsite.net\",\"trainNumber\":\"Xyz789\",\"travelDate\":\"2021-07-28\",\"travelTime\":\"2023-10-05T08:30Z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17t13:50:00z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"John Doe\",\"differenceMoney\":\"50.0\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"cbs\",\"price\":\"10.99\",\"seatClass\":\"First class\",\"seatNumber\":\"h6\",\"status\":\"9\",\"to\":\"suppport\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29T11:00Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17t13:50:00z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"John Doe\",\"differenceMoney\":\"50.0\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"cbs\",\"price\":\"10.99\",\"seatClass\":\"First class\",\"seatNumber\":\"h6\",\"status\":\"9\",\"to\":\"suppport\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29T11:00Z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17t13:50:00z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"John Doe\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"cbs\",\"price\":\"10.99\",\"seatClass\":\"First class\",\"seatNumber\":\"h6\",\"status\":\"9\",\"to\":\"suppport\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29T11:00Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"tested\",\"boughtDate\":\"2020-11-17t13:50:00z\",\"coachNumber\":\"-4\",\"contactsDocumentNumber\":\"printf\",\"contactsName\":\"John Doe\",\"documentType\":\"bd_div_story\",\"from\":\"bobs\",\"id\":\"cbs\",\"price\":\"10.99\",\"seatClass\":\"First class\",\"seatNumber\":\"h6\",\"status\":\"9\",\"to\":\"suppport\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-29T11:00Z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"Sydney_measured_####kbps\",\"boughtDate\":\"2021-06-02T10:45:00Z\",\"coachNumber\":\"4567\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"jane smith\",\"differenceMoney\":\"50.0\",\"documentType\":\"789\",\"from\":\"christina\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"0.99\",\"seatClass\":\"Business\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"admin@domain.org\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01T00:00Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"Sydney_measured_####kbps\",\"boughtDate\":\"2021-06-02T10:45:00Z\",\"coachNumber\":\"4567\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"jane smith\",\"differenceMoney\":\"50.0\",\"documentType\":\"789\",\"from\":\"christina\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"0.99\",\"seatClass\":\"Business\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"admin@domain.org\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01T00:00Z\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
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
            System.out.println("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("⏭️ Skip Reason", "Dependency failed - previous step(s) failed");
                throw new org.junit.AssumptionViolatedException("Step skipped due to dependency failure");
            });
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
                Allure.parameter("✅ Expected Status", 200);
                Allure.description("🎯 **Testing**: ts-order-other-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/orderOtherService/orderOther/admin\n" +
                                 "✅ **Expected**: 200");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"accountId\":\"Sydney_measured_####kbps\",\"boughtDate\":\"2021-06-02T10:45:00Z\",\"coachNumber\":\"4567\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"jane smith\",\"documentType\":\"789\",\"from\":\"christina\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"0.99\",\"seatClass\":\"Business\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"admin@domain.org\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01T00:00Z\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"accountId\":\"Sydney_measured_####kbps\",\"boughtDate\":\"2021-06-02T10:45:00Z\",\"coachNumber\":\"4567\",\"contactsDocumentNumber\":\"cbs\",\"contactsName\":\"jane smith\",\"documentType\":\"789\",\"from\":\"christina\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"0.99\",\"seatClass\":\"Business\",\"seatNumber\":\"a12\",\"status\":\"10\",\"to\":\"admin@domain.org\",\"trainNumber\":\"printf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2026-01-01T00:00Z\"}");
                    Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                       .then().log().ifValidationFails()
                       .statusCode(200)
                       .extract().response();
                    stepResults.put(2, true);
                    System.out.println("✅ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SUCCESS");
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
                    System.out.println("❌ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - FAILED: " + t.getMessage());
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
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
            System.out.println("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) - SKIPPED (dependency failed)");
            // Create skipped step with visible reason
            Allure.step("⏭️ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-order-other-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/orderOtherService/orderOther/admin");
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
