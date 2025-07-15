package trainticket_twostage_test.TrainTicketTwoStageTest_1752554072810;

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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"QWERT\",\"trainType\":\"Standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"QWERT\",\"trainType\":\"Standard\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"QWERT\",\"trainType\":\"Standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"QWERT\",\"trainType\":\"Standard\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"5000.00\",\"firstClassPriceRate\":\"10.5\",\"id\":\"buf\",\"routeId\":\"67890\",\"trainType\":\"speeds\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"5000.00\",\"firstClassPriceRate\":\"10.5\",\"id\":\"buf\",\"routeId\":\"67890\",\"trainType\":\"speeds\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"5000.00\",\"firstClassPriceRate\":\"10.5\",\"id\":\"buf\",\"routeId\":\"67890\",\"trainType\":\"speeds\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"5000.00\",\"firstClassPriceRate\":\"10.5\",\"id\":\"buf\",\"routeId\":\"67890\",\"trainType\":\"speeds\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"10.5\",\"id\":\"Xyz789\",\"routeId\":\"Xyzzy\",\"trainType\":\"high-speed\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"10.5\",\"id\":\"Xyz789\",\"routeId\":\"Xyzzy\",\"trainType\":\"high-speed\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"10.5\",\"id\":\"Xyz789\",\"routeId\":\"Xyzzy\",\"trainType\":\"high-speed\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"10.5\",\"id\":\"Xyz789\",\"routeId\":\"Xyzzy\",\"trainType\":\"high-speed\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"12.50\",\"firstClassPriceRate\":\"10.5\",\"id\":\"printf\",\"routeId\":\"12345\",\"trainType\":\"standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"12.50\",\"firstClassPriceRate\":\"10.5\",\"id\":\"printf\",\"routeId\":\"12345\",\"trainType\":\"standard\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"12.50\",\"firstClassPriceRate\":\"10.5\",\"id\":\"printf\",\"routeId\":\"12345\",\"trainType\":\"standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"12.50\",\"firstClassPriceRate\":\"10.5\",\"id\":\"printf\",\"routeId\":\"12345\",\"trainType\":\"standard\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"specification\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"specification\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"specification\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"specification\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"10.5\",\"id\":\"tested\",\"routeId\":\"67890\",\"trainType\":\"higher\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"10.5\",\"id\":\"tested\",\"routeId\":\"67890\",\"trainType\":\"higher\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"10.5\",\"id\":\"tested\",\"routeId\":\"67890\",\"trainType\":\"higher\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"10.5\",\"id\":\"tested\",\"routeId\":\"67890\",\"trainType\":\"higher\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"50.75\",\"id\":\"tested\",\"routeId\":\"Qwert\",\"trainType\":\"high-speed\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"50.75\",\"id\":\"tested\",\"routeId\":\"Qwert\",\"trainType\":\"high-speed\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"50.75\",\"id\":\"tested\",\"routeId\":\"Qwert\",\"trainType\":\"high-speed\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.01\",\"firstClassPriceRate\":\"50.75\",\"id\":\"tested\",\"routeId\":\"Qwert\",\"trainType\":\"high-speed\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"qwert\",\"trainType\":\"higher\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"qwert\",\"trainType\":\"higher\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"qwert\",\"trainType\":\"higher\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"349.79\",\"firstClassPriceRate\":\"1.99\",\"id\":\"abcde\",\"routeId\":\"qwert\",\"trainType\":\"higher\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"1.99\",\"id\":\"Xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"expresses\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"1.99\",\"id\":\"Xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"expresses\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"1.99\",\"id\":\"Xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"expresses\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"1.99\",\"id\":\"Xyz789\",\"routeId\":\"QWERT\",\"trainType\":\"expresses\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                    .setDescription("Service: ts-admin-basic-info-service | Method: POST | Path: /api/v1/adminbasicservice/adminbasic/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-basic-info-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminbasicservice/adminbasic/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"23.99\",\"id\":\"Xyz789\",\"routeId\":\"Qwert\",\"trainType\":\"Freight\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"23.99\",\"id\":\"Xyz789\",\"routeId\":\"Qwert\",\"trainType\":\"Freight\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse1.getBody().asString();
                    int actualStatus = stepResponse1.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse1.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-basic-info-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminbasicservice/adminbasic/prices\n");
                errorDetails.append("Expected Status: 200\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 201");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 201);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"23.99\",\"id\":\"Xyz789\",\"routeId\":\"Qwert\",\"trainType\":\"Freight\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"-8.25\",\"firstClassPriceRate\":\"23.99\",\"id\":\"Xyz789\",\"routeId\":\"Qwert\",\"trainType\":\"Freight\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(201)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse2.getBody().asString();
                    int actualStatus = stepResponse2.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse2.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-price-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/priceservice/prices\n");
                errorDetails.append("Expected Status: 201\n");
                
                // Try to capture response information even on failure
                try {
                    if (t instanceof AssertionError && t.getMessage().contains("Expected status code")) {
                        // This is likely a status code mismatch - try to get actual response
                        errorDetails.append("\nThis appears to be a status code mismatch.\n");
                        errorDetails.append("Check if the service is running and the endpoint is correct.\n");
                    }
                } catch (Exception responseError) {
                    errorDetails.append("\nCould not capture response details: ").append(responseError.getMessage());
                }
                
                errorDetails.append("\n\nFull Stack Trace:\n").append(t.toString());
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-price-service POST /api/v1/priceservice/prices", "text/plain", errorDetails.toString());
                
                // Add failure categorization
                if (t instanceof java.net.ConnectException) {
                    Allure.parameter("Error Category", "Connection Failed - Service Unreachable");
                } else if (t instanceof AssertionError) {
                    Allure.parameter("Error Category", "Assertion Failed - Unexpected Response");
                } else if (t instanceof java.net.SocketTimeoutException) {
                    Allure.parameter("Error Category", "Timeout - Service Too Slow");
                } else {
                    Allure.parameter("Error Category", "Unknown - " + t.getClass().getSimpleName());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-price-service POST /api/v1/priceservice/prices - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
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
            System.out.println("✓ Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

}
