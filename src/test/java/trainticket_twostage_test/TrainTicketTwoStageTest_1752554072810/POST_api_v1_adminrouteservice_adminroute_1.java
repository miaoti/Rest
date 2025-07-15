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

public class POST_api_v1_adminrouteservice_adminroute_1 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_POST_1_1() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"0.01\",\"distances\":\"789\",\"endStation\":\"west end bus stop\",\"id\":\"xyz789\",\"loginId\":\"John_doe_987654321\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Boston logan international airport\",\"stations\":\"Chicago Loop\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"0.01\",\"distances\":\"789\",\"endStation\":\"west end bus stop\",\"id\":\"xyz789\",\"loginId\":\"John_doe_987654321\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Boston logan international airport\",\"stations\":\"Chicago Loop\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"0.01\",\"endStation\":\"west end bus stop\",\"id\":\"xyz789\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Boston logan international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"0.01\",\"endStation\":\"west end bus stop\",\"id\":\"xyz789\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Boston logan international airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_1");
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
    public void test_POST_1_2() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"25.34\",\"distances\":\"0\",\"endStation\":\"Central Station\",\"id\":\"Test123\",\"loginId\":\"john_doe_987654321\",\"startStation\":\"Los angeles terminal\",\"stationList\":\"Chicago O'Hare International Airport\",\"stations\":\"San francisco powell street\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"25.34\",\"distances\":\"0\",\"endStation\":\"Central Station\",\"id\":\"Test123\",\"loginId\":\"john_doe_987654321\",\"startStation\":\"Los angeles terminal\",\"stationList\":\"Chicago O'Hare International Airport\",\"stations\":\"San francisco powell street\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"25.34\",\"endStation\":\"Central Station\",\"id\":\"Test123\",\"startStation\":\"Los angeles terminal\",\"stationList\":\"Chicago O'Hare International Airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"25.34\",\"endStation\":\"Central Station\",\"id\":\"Test123\",\"startStation\":\"Los angeles terminal\",\"stationList\":\"Chicago O'Hare International Airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_2");
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
    public void test_POST_1_3() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"789.65\",\"distances\":\"10\",\"endStation\":\"union square\",\"id\":\"xyz789\",\"loginId\":\"user1\",\"startStation\":\"Chicago Loop\",\"stationList\":\"San Francisco Fisherman's Wharf\",\"stations\":\"New York Station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"789.65\",\"distances\":\"10\",\"endStation\":\"union square\",\"id\":\"xyz789\",\"loginId\":\"user1\",\"startStation\":\"Chicago Loop\",\"stationList\":\"San Francisco Fisherman's Wharf\",\"stations\":\"New York Station\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"789.65\",\"endStation\":\"union square\",\"id\":\"xyz789\",\"startStation\":\"Chicago Loop\",\"stationList\":\"San Francisco Fisherman's Wharf\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"789.65\",\"endStation\":\"union square\",\"id\":\"xyz789\",\"startStation\":\"Chicago Loop\",\"stationList\":\"San Francisco Fisherman's Wharf\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_3");
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
    public void test_POST_1_4() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"-10\",\"distances\":\"10\",\"endStation\":\"Hollywood Sign\",\"id\":\"Xyz789\",\"loginId\":\"Admins\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\",\"stations\":\"los angeles metro center\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"-10\",\"distances\":\"10\",\"endStation\":\"Hollywood Sign\",\"id\":\"Xyz789\",\"loginId\":\"Admins\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\",\"stations\":\"los angeles metro center\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"-10\",\"endStation\":\"Hollywood Sign\",\"id\":\"Xyz789\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"-10\",\"endStation\":\"Hollywood Sign\",\"id\":\"Xyz789\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_4");
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
    public void test_POST_1_5() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"0.01\",\"distances\":\"10\",\"endStation\":\"Hollywood Sign\",\"id\":\"printf\",\"loginId\":\"bennett\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\",\"stations\":\"San francisco powell street\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"0.01\",\"distances\":\"10\",\"endStation\":\"Hollywood Sign\",\"id\":\"printf\",\"loginId\":\"bennett\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\",\"stations\":\"San francisco powell street\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"0.01\",\"endStation\":\"Hollywood Sign\",\"id\":\"printf\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"0.01\",\"endStation\":\"Hollywood Sign\",\"id\":\"printf\",\"startStation\":\"Houston intermodal center\",\"stationList\":\"Boston logan international airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_5");
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
    public void test_POST_1_6() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"25.34\",\"distances\":\"0\",\"endStation\":\"union square\",\"id\":\"Test123\",\"loginId\":\"guest2022\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Chicago O'Hare International Airport\",\"stations\":\"New york station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"25.34\",\"distances\":\"0\",\"endStation\":\"union square\",\"id\":\"Test123\",\"loginId\":\"guest2022\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Chicago O'Hare International Airport\",\"stations\":\"New york station\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"25.34\",\"endStation\":\"union square\",\"id\":\"Test123\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Chicago O'Hare International Airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"25.34\",\"endStation\":\"union square\",\"id\":\"Test123\",\"startStation\":\"Los Angeles Terminal\",\"stationList\":\"Chicago O'Hare International Airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_6");
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
    public void test_POST_1_7() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"25.34\",\"distances\":\"25.3\",\"endStation\":\"hollywood sign\",\"id\":\"printf\",\"loginId\":\"John_doe_987654321\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"Los Angeles Union Square Bus Terminal\",\"stations\":\"New york station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"25.34\",\"distances\":\"25.3\",\"endStation\":\"hollywood sign\",\"id\":\"printf\",\"loginId\":\"John_doe_987654321\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"Los Angeles Union Square Bus Terminal\",\"stations\":\"New york station\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"25.34\",\"endStation\":\"hollywood sign\",\"id\":\"printf\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"Los Angeles Union Square Bus Terminal\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"25.34\",\"endStation\":\"hollywood sign\",\"id\":\"printf\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"Los Angeles Union Square Bus Terminal\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_7");
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
    public void test_POST_1_8() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"-10\",\"distances\":\"BORDER_=\",\"endStation\":\"west end bus stop\",\"id\":\"0987654321\",\"loginId\":\"test_user_123\",\"startStation\":\"new york city station\",\"stationList\":\"Boston logan international airport\",\"stations\":\"Los Angeles Metro Center\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"-10\",\"distances\":\"BORDER_=\",\"endStation\":\"west end bus stop\",\"id\":\"0987654321\",\"loginId\":\"test_user_123\",\"startStation\":\"new york city station\",\"stationList\":\"Boston logan international airport\",\"stations\":\"Los Angeles Metro Center\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"-10\",\"endStation\":\"west end bus stop\",\"id\":\"0987654321\",\"startStation\":\"new york city station\",\"stationList\":\"Boston logan international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"-10\",\"endStation\":\"west end bus stop\",\"id\":\"0987654321\",\"startStation\":\"new york city station\",\"stationList\":\"Boston logan international airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_8");
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
    public void test_POST_1_9() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"100\",\"distances\":\"bd_div_story\",\"endStation\":\"Hollywood Sign\",\"id\":\"buf\",\"loginId\":\"guest2022\",\"startStation\":\"Chicago loop\",\"stationList\":\"chicago o'hare international airport\",\"stations\":\"Chicago Loop\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"100\",\"distances\":\"bd_div_story\",\"endStation\":\"Hollywood Sign\",\"id\":\"buf\",\"loginId\":\"guest2022\",\"startStation\":\"Chicago loop\",\"stationList\":\"chicago o'hare international airport\",\"stations\":\"Chicago Loop\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"100\",\"endStation\":\"Hollywood Sign\",\"id\":\"buf\",\"startStation\":\"Chicago loop\",\"stationList\":\"chicago o'hare international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"100\",\"endStation\":\"Hollywood Sign\",\"id\":\"buf\",\"startStation\":\"Chicago loop\",\"stationList\":\"chicago o'hare international airport\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_9");
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
    public void test_POST_1_10() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                    .setDescription("Service: ts-admin-route-service | Method: POST | Path: /api/v1/adminrouteservice/adminroute | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"-10\",\"distances\":\"BORDER_=\",\"endStation\":\"Union square\",\"id\":\"12345\",\"loginId\":\"Admin2023\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"los angeles union square bus terminal\",\"stations\":\"chicago loop\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"-10\",\"distances\":\"BORDER_=\",\"endStation\":\"Union square\",\"id\":\"12345\",\"loginId\":\"Admin2023\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"los angeles union square bus terminal\",\"stations\":\"chicago loop\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                    .setDescription("Service: ts-station-service | Method: POST | Path: /api/v1/stationservice/stations/idlist | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-station-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
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
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-station-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                    .setDescription("Service: ts-route-service | Method: POST | Path: /api/v1/routeservice/routes | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-route-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"-10\",\"endStation\":\"Union square\",\"id\":\"12345\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"los angeles union square bus terminal\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"-10\",\"endStation\":\"Union square\",\"id\":\"12345\",\"startStation\":\"Philadelphia Convention Center\",\"stationList\":\"los angeles union square bus terminal\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                // Capture response details for successful steps
                try {
                    String responseBody = stepResponse3.getBody().asString();
                    int actualStatus = stepResponse3.getStatusCode();
                    Allure.addAttachment("Response Body (Status: " + actualStatus + ")", "application/json", responseBody);
                    Allure.parameter("Actual Status Code", actualStatus);
                    
                    // Add timing information if available
                    long responseTime = stepResponse3.getTime();
                    Allure.parameter("Response Time (ms)", responseTime);
                } catch (Exception e) {
                    Allure.addAttachment("Response Capture Error", e.getMessage());
                }
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-route-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/routeservice/routes\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 3: ts-route-service POST /api/v1/routeservice/routes", "text/plain", errorDetails.toString());
                
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
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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
        Allure.label("story", "test_POST_1_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1_10");
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
