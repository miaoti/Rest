package trainticket_twostage_test;

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

public class POST {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_POST_1() throws Exception {
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
                req = req.body("{\"distanceList\":\"10\",\"distances\":\"230\",\"endStation\":\"Los angeles union station\",\"id\":\"users\",\"loginId\":\"user123\",\"startStation\":\"chicago union south station\",\"stationList\":\"Houston Hobby Airport\",\"stations\":\"brixton bus stop\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"distances\":\"230\",\"endStation\":\"Los angeles union station\",\"id\":\"users\",\"loginId\":\"user123\",\"startStation\":\"chicago union south station\",\"stationList\":\"Houston Hobby Airport\",\"stations\":\"brixton bus stop\"}");
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
                req = req.body("{\"distanceList\":\"10\",\"endStation\":\"los angeles union station\",\"id\":\"cbs\",\"startStation\":\"Chicago Union South Station\",\"stationList\":\"San francisco international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"endStation\":\"los angeles union station\",\"id\":\"cbs\",\"startStation\":\"Chicago Union South Station\",\"stationList\":\"San francisco international airport\"}");
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
        Allure.label("story", "test_POST_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1");
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
    public void test_POST_2() throws Exception {
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
                req = req.body("{\"distanceList\":\"10\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"San francisco int'l airport\",\"id\":\"4567890\",\"loginId\":\"User123\",\"startStation\":\"New York Penn Station\",\"stationList\":\"los angeles union station\",\"stations\":\"oakland pier\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"San francisco int'l airport\",\"id\":\"4567890\",\"loginId\":\"User123\",\"startStation\":\"New York Penn Station\",\"stationList\":\"los angeles union station\",\"stations\":\"oakland pier\"}");
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
                req = req.body("{\"distanceList\":\"234\",\"endStation\":\"Los angeles union station\",\"id\":\"user_123\",\"startStation\":\"boston logan international airport\",\"stationList\":\"san francisco international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"234\",\"endStation\":\"Los angeles union station\",\"id\":\"user_123\",\"startStation\":\"boston logan international airport\",\"stationList\":\"san francisco international airport\"}");
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
        Allure.label("story", "test_POST_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_2");
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
    public void test_POST_3() throws Exception {
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
                req = req.body("{\"distanceList\":\"5.5\",\"distances\":\"45\",\"endStation\":\"New York Penn Station\",\"id\":\"abc-def-ghi\",\"loginId\":\"admins\",\"startStation\":\"Los angeles union station\",\"stationList\":\"New york penn station\",\"stations\":\"Harlem Landmark\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"5.5\",\"distances\":\"45\",\"endStation\":\"New York Penn Station\",\"id\":\"abc-def-ghi\",\"loginId\":\"admins\",\"startStation\":\"Los angeles union station\",\"stationList\":\"New york penn station\",\"stations\":\"Harlem Landmark\"}");
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
                req = req.body("{\"distanceList\":\"1\",\"endStation\":\"New york penn station\",\"id\":\"brennan\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"san francisco international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"1\",\"endStation\":\"New york penn station\",\"id\":\"brennan\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"san francisco international airport\"}");
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
        Allure.label("story", "test_POST_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_3");
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
    public void test_POST_4() throws Exception {
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
                req = req.body("{\"distanceList\":\"89.76\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"4567890\",\"loginId\":\"dave\",\"startStation\":\"Chicago union south station\",\"stationList\":\"New York Penn Station\",\"stations\":\"Central station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"89.76\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"4567890\",\"loginId\":\"dave\",\"startStation\":\"Chicago union south station\",\"stationList\":\"New York Penn Station\",\"stations\":\"Central station\"}");
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
                req = req.body("{\"distanceList\":\"3\",\"endStation\":\"New york penn station\",\"id\":\"users\",\"startStation\":\"boston logan international airport\",\"stationList\":\"New york penn station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"3\",\"endStation\":\"New york penn station\",\"id\":\"users\",\"startStation\":\"boston logan international airport\",\"stationList\":\"New york penn station\"}");
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
        Allure.label("story", "test_POST_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_4");
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
    public void test_POST_5() throws Exception {
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
                req = req.body("{\"distanceList\":\"2\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"los angeles union station\",\"id\":\"User_123\",\"loginId\":\"User123\",\"startStation\":\"San Francisco 49ers Stadium\",\"stationList\":\"Los Angeles Union Station\",\"stations\":\"brixton bus stop\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"2\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"los angeles union station\",\"id\":\"User_123\",\"loginId\":\"User123\",\"startStation\":\"San Francisco 49ers Stadium\",\"stationList\":\"Los Angeles Union Station\",\"stations\":\"brixton bus stop\"}");
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
                req = req.body("{\"distanceList\":\"3\",\"endStation\":\"San francisco int'l airport\",\"id\":\"User_123\",\"startStation\":\"New York Penn Station\",\"stationList\":\"new york penn station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"3\",\"endStation\":\"San francisco int'l airport\",\"id\":\"User_123\",\"startStation\":\"New York Penn Station\",\"stationList\":\"new york penn station\"}");
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
        Allure.label("story", "test_POST_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_5");
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
    public void test_POST_6() throws Exception {
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
                req = req.body("{\"distanceList\":\"1\",\"distances\":\"0\",\"endStation\":\"houston hobby airport\",\"id\":\"4567890\",\"loginId\":\"user123\",\"startStation\":\"San francisco 49ers stadium\",\"stationList\":\"Los angeles union station\",\"stations\":\"Union square\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"1\",\"distances\":\"0\",\"endStation\":\"houston hobby airport\",\"id\":\"4567890\",\"loginId\":\"user123\",\"startStation\":\"San francisco 49ers stadium\",\"stationList\":\"Los angeles union station\",\"stations\":\"Union square\"}");
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
                req = req.body("{\"distanceList\":\"89.76\",\"endStation\":\"New york penn station\",\"id\":\"sessions\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"san francisco international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"89.76\",\"endStation\":\"New york penn station\",\"id\":\"sessions\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"san francisco international airport\"}");
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
        Allure.label("story", "test_POST_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_6");
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
    public void test_POST_7() throws Exception {
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
                req = req.body("{\"distanceList\":\"5.5\",\"distances\":\"45\",\"endStation\":\"los angeles union station\",\"id\":\"session_token\",\"loginId\":\"john_doe789\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"Houston Hobby Airport\",\"stations\":\"harlem landmark\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"5.5\",\"distances\":\"45\",\"endStation\":\"los angeles union station\",\"id\":\"session_token\",\"loginId\":\"john_doe789\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"Houston Hobby Airport\",\"stations\":\"harlem landmark\"}");
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
                req = req.body("{\"distanceList\":\"2\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"sesssion\",\"startStation\":\"New york penn station\",\"stationList\":\"chicago o'hare international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"2\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"sesssion\",\"startStation\":\"New york penn station\",\"stationList\":\"chicago o'hare international airport\"}");
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
        Allure.label("story", "test_POST_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_7");
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
    public void test_POST_8() throws Exception {
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
                req = req.body("{\"distanceList\":\"5.5\",\"distances\":\"230\",\"endStation\":\"new york penn station\",\"id\":\"Session_token\",\"loginId\":\"Admin456\",\"startStation\":\"san francisco 49ers stadium\",\"stationList\":\"San Francisco International Airport\",\"stations\":\"Oakland pier\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"5.5\",\"distances\":\"230\",\"endStation\":\"new york penn station\",\"id\":\"Session_token\",\"loginId\":\"Admin456\",\"startStation\":\"san francisco 49ers stadium\",\"stationList\":\"San Francisco International Airport\",\"stations\":\"Oakland pier\"}");
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
                req = req.body("{\"distanceList\":\"1\",\"endStation\":\"san francisco int'l airport\",\"id\":\"session_token\",\"startStation\":\"los angeles union station\",\"stationList\":\"Los angeles union station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"1\",\"endStation\":\"san francisco int'l airport\",\"id\":\"session_token\",\"startStation\":\"los angeles union station\",\"stationList\":\"Los angeles union station\"}");
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
        Allure.label("story", "test_POST_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_8");
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
    public void test_POST_9() throws Exception {
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
                req = req.body("{\"distanceList\":\"234\",\"distances\":\"bd_div_story\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"User_123\",\"loginId\":\"user123\",\"startStation\":\"San Francisco 49ers Stadium\",\"stationList\":\"san francisco international airport\",\"stations\":\"Union square\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"234\",\"distances\":\"bd_div_story\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"User_123\",\"loginId\":\"user123\",\"startStation\":\"San Francisco 49ers Stadium\",\"stationList\":\"san francisco international airport\",\"stations\":\"Union square\"}");
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
                req = req.body("{\"distanceList\":\"4\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"sesssion\",\"startStation\":\"chicago union south station\",\"stationList\":\"new york penn station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"4\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"sesssion\",\"startStation\":\"chicago union south station\",\"stationList\":\"new york penn station\"}");
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
        Allure.label("story", "test_POST_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_9");
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
    public void test_POST_10() throws Exception {
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
                req = req.body("{\"distanceList\":\"89.76\",\"distances\":\"10.5\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"4567890\",\"loginId\":\"admin456\",\"startStation\":\"Chicago Union South Station\",\"stationList\":\"Los angeles union station\",\"stations\":\"Union Square\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"89.76\",\"distances\":\"10.5\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"4567890\",\"loginId\":\"admin456\",\"startStation\":\"Chicago Union South Station\",\"stationList\":\"Los angeles union station\",\"stations\":\"Union Square\"}");
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
                req = req.body("{\"distanceList\":\"4\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"user_123\",\"startStation\":\"los angeles union station\",\"stationList\":\"Los Angeles Union Station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"4\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"user_123\",\"startStation\":\"los angeles union station\",\"stationList\":\"Los Angeles Union Station\"}");
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
        Allure.label("story", "test_POST_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_10");
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
    public void test_POST_1() throws Exception {
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
                req = req.body("{\"distanceList\":\"3\",\"distances\":\"789.1\",\"endStation\":\"New york penn station\",\"id\":\"123\",\"loginId\":\"admin456\",\"startStation\":\"san francisco 49ers stadium\",\"stationList\":\"chicago o'hare international airport\",\"stations\":\"oakland pier\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"3\",\"distances\":\"789.1\",\"endStation\":\"New york penn station\",\"id\":\"123\",\"loginId\":\"admin456\",\"startStation\":\"san francisco 49ers stadium\",\"stationList\":\"chicago o'hare international airport\",\"stations\":\"oakland pier\"}");
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
                req = req.body("{\"distanceList\":\"5.5\",\"endStation\":\"San Francisco Int'l Airport\",\"id\":\"sesssion\",\"startStation\":\"chicago union south station\",\"stationList\":\"Los Angeles Union Station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"5.5\",\"endStation\":\"San Francisco Int'l Airport\",\"id\":\"sesssion\",\"startStation\":\"chicago union south station\",\"stationList\":\"Los Angeles Union Station\"}");
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
        Allure.label("story", "test_POST_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1");
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
    public void test_POST_2() throws Exception {
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
                req = req.body("{\"distanceList\":\"10\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"new york penn station\",\"id\":\"sessions\",\"loginId\":\"users\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"Los Angeles Union Station\",\"stations\":\"Brixton Bus Stop\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"new york penn station\",\"id\":\"sessions\",\"loginId\":\"users\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"Los Angeles Union Station\",\"stations\":\"Brixton Bus Stop\"}");
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
                req = req.body("{\"distanceList\":\"1\",\"endStation\":\"New york penn station\",\"id\":\"cbs\",\"startStation\":\"San Francisco 49ers Stadium\",\"stationList\":\"San Francisco International Airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"1\",\"endStation\":\"New york penn station\",\"id\":\"cbs\",\"startStation\":\"San Francisco 49ers Stadium\",\"stationList\":\"San Francisco International Airport\"}");
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
        Allure.label("story", "test_POST_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_2");
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
    public void test_POST_3() throws Exception {
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
                req = req.body("{\"distanceList\":\"4\",\"distances\":\"230\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"123\",\"loginId\":\"user123\",\"startStation\":\"Chicago Union South Station\",\"stationList\":\"Los Angeles Union Station\",\"stations\":\"Oakland Pier\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"4\",\"distances\":\"230\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"123\",\"loginId\":\"user123\",\"startStation\":\"Chicago Union South Station\",\"stationList\":\"Los Angeles Union Station\",\"stations\":\"Oakland Pier\"}");
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
                req = req.body("{\"distanceList\":\"1\",\"endStation\":\"san francisco int'l airport\",\"id\":\"session_token\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"San Francisco International Airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"1\",\"endStation\":\"san francisco int'l airport\",\"id\":\"session_token\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"San Francisco International Airport\"}");
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
        Allure.label("story", "test_POST_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_3");
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
    public void test_POST_4() throws Exception {
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
                req = req.body("{\"distanceList\":\"234\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"User_123\",\"loginId\":\"johnston\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"Houston Hobby Airport\",\"stations\":\"Union square\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"234\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"User_123\",\"loginId\":\"johnston\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"Houston Hobby Airport\",\"stations\":\"Union square\"}");
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
                req = req.body("{\"distanceList\":\"4\",\"endStation\":\"houston hobby airport\",\"id\":\"cbs\",\"startStation\":\"boston logan international airport\",\"stationList\":\"Chicago o'hare international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"4\",\"endStation\":\"houston hobby airport\",\"id\":\"cbs\",\"startStation\":\"boston logan international airport\",\"stationList\":\"Chicago o'hare international airport\"}");
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
        Allure.label("story", "test_POST_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_4");
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
    public void test_POST_5() throws Exception {
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
                req = req.body("{\"distanceList\":\"10\",\"distances\":\"0\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"brennan\",\"loginId\":\"dave\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"San Francisco International Airport\",\"stations\":\"Union square\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"distances\":\"0\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"brennan\",\"loginId\":\"dave\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"San Francisco International Airport\",\"stations\":\"Union square\"}");
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
                req = req.body("{\"distanceList\":\"234\",\"endStation\":\"chicago o'hare international airport\",\"id\":\"abc-def-ghi\",\"startStation\":\"New york penn station\",\"stationList\":\"Houston Hobby Airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"234\",\"endStation\":\"chicago o'hare international airport\",\"id\":\"abc-def-ghi\",\"startStation\":\"New york penn station\",\"stationList\":\"Houston Hobby Airport\"}");
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
        Allure.label("story", "test_POST_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_5");
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
    public void test_POST_6() throws Exception {
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
                req = req.body("{\"distanceList\":\"1\",\"distances\":\"bd_div_story\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"sesion\",\"loginId\":\"admin456\",\"startStation\":\"los angeles union station\",\"stationList\":\"Chicago O'Hare International Airport\",\"stations\":\"Central Station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"1\",\"distances\":\"bd_div_story\",\"endStation\":\"Houston Hobby Airport\",\"id\":\"sesion\",\"loginId\":\"admin456\",\"startStation\":\"los angeles union station\",\"stationList\":\"Chicago O'Hare International Airport\",\"stations\":\"Central Station\"}");
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
                req = req.body("{\"distanceList\":\"3\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"user_123\",\"startStation\":\"Boston Logan International Airport\",\"stationList\":\"Los Angeles Union Station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"3\",\"endStation\":\"Los Angeles Union Station\",\"id\":\"user_123\",\"startStation\":\"Boston Logan International Airport\",\"stationList\":\"Los Angeles Union Station\"}");
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
        Allure.label("story", "test_POST_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_6");
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
    public void test_POST_7() throws Exception {
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
                req = req.body("{\"distanceList\":\"234\",\"distances\":\"789.1\",\"endStation\":\"new york penn station\",\"id\":\"sesssion\",\"loginId\":\"User123\",\"startStation\":\"chicago union south station\",\"stationList\":\"New York Penn Station\",\"stations\":\"harlem landmark\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"234\",\"distances\":\"789.1\",\"endStation\":\"new york penn station\",\"id\":\"sesssion\",\"loginId\":\"User123\",\"startStation\":\"chicago union south station\",\"stationList\":\"New York Penn Station\",\"stations\":\"harlem landmark\"}");
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
                req = req.body("{\"distanceList\":\"2\",\"endStation\":\"Los angeles union station\",\"id\":\"session_token\",\"startStation\":\"Chicago union south station\",\"stationList\":\"Chicago o'hare international airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"2\",\"endStation\":\"Los angeles union station\",\"id\":\"session_token\",\"startStation\":\"Chicago union south station\",\"stationList\":\"Chicago o'hare international airport\"}");
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
        Allure.label("story", "test_POST_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_7");
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
    public void test_POST_8() throws Exception {
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
                req = req.body("{\"distanceList\":\"234\",\"distances\":\"bd_div_story\",\"endStation\":\"New york penn station\",\"id\":\"sesssion\",\"loginId\":\"user123\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"San Francisco International Airport\",\"stations\":\"Oakland pier\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"234\",\"distances\":\"bd_div_story\",\"endStation\":\"New york penn station\",\"id\":\"sesssion\",\"loginId\":\"user123\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"San Francisco International Airport\",\"stations\":\"Oakland pier\"}");
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
                req = req.body("{\"distanceList\":\"3\",\"endStation\":\"New york penn station\",\"id\":\"session_token\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"New york penn station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"3\",\"endStation\":\"New york penn station\",\"id\":\"session_token\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"New york penn station\"}");
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
        Allure.label("story", "test_POST_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_8");
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
    public void test_POST_9() throws Exception {
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
                req = req.body("{\"distanceList\":\"2\",\"distances\":\"230\",\"endStation\":\"Chicago O'Hare International Airport\",\"id\":\"abc-def-ghi\",\"loginId\":\"admins\",\"startStation\":\"new york penn station\",\"stationList\":\"New york penn station\",\"stations\":\"union square\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"2\",\"distances\":\"230\",\"endStation\":\"Chicago O'Hare International Airport\",\"id\":\"abc-def-ghi\",\"loginId\":\"admins\",\"startStation\":\"new york penn station\",\"stationList\":\"New york penn station\",\"stations\":\"union square\"}");
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
                req = req.body("{\"distanceList\":\"89.76\",\"endStation\":\"San francisco int'l airport\",\"id\":\"nbc\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"Houston Hobby Airport\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"89.76\",\"endStation\":\"San francisco int'l airport\",\"id\":\"nbc\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"Houston Hobby Airport\"}");
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
        Allure.label("story", "test_POST_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_9");
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
    public void test_POST_10() throws Exception {
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
                req = req.body("{\"distanceList\":\"10\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"Los angeles union station\",\"id\":\"Session_token\",\"loginId\":\"John_doe789\",\"startStation\":\"New york penn station\",\"stationList\":\"chicago o'hare international airport\",\"stations\":\"Oakland pier\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"distances\":\"var_bd_=_getElementsByClass\",\"endStation\":\"Los angeles union station\",\"id\":\"Session_token\",\"loginId\":\"John_doe789\",\"startStation\":\"New york penn station\",\"stationList\":\"chicago o'hare international airport\",\"stations\":\"Oakland pier\"}");
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
                req = req.body("{\"distanceList\":\"10\",\"endStation\":\"Houston hobby airport\",\"id\":\"cbs\",\"startStation\":\"san francisco 49ers stadium\",\"stationList\":\"New York Penn Station\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"distanceList\":\"10\",\"endStation\":\"Houston hobby airport\",\"id\":\"cbs\",\"startStation\":\"san francisco 49ers stadium\",\"stationList\":\"New York Penn Station\"}");
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
        Allure.label("story", "test_POST_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_10");
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
    public void test_POST_1() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"Abcde\",\"boughtDate\":\"2030-04-15t12:00:00z\",\"coachNumber\":\"4\",\"contactsDocumentNumber\":\"Xyz123\",\"contactsName\":\"alice johnson\",\"differenceMoney\":\"-20.99\",\"documentType\":\"1\",\"from\":\"Houston\",\"id\":\"sesion\",\"price\":\"$45.00\",\"seatClass\":\"Premium Economy\",\"seatNumber\":\"C789\",\"status\":\"101\",\"to\":\"tests\",\"trainNumber\":\"Xyz\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-01t16:45:00-04:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"Abcde\",\"boughtDate\":\"2030-04-15t12:00:00z\",\"coachNumber\":\"4\",\"contactsDocumentNumber\":\"Xyz123\",\"contactsName\":\"alice johnson\",\"differenceMoney\":\"-20.99\",\"documentType\":\"1\",\"from\":\"Houston\",\"id\":\"sesion\",\"price\":\"$45.00\",\"seatClass\":\"Premium Economy\",\"seatNumber\":\"C789\",\"status\":\"101\",\"to\":\"tests\",\"trainNumber\":\"Xyz\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2022-07-01t16:45:00-04:00\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"XYZ123\",\"contactsName\":\"Alice Johnson\",\"documentType\":\"2\",\"from\":\"Phoenix\",\"id\":\"sesssion\",\"price\":\"$78.95\",\"seatClass\":\"busines\",\"seatNumber\":\"D012\",\"status\":\"301\",\"to\":\"Hello_world\",\"trainNumber\":\"98765\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2022-07-01T16:45:00-04:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"XYZ123\",\"contactsName\":\"Alice Johnson\",\"documentType\":\"2\",\"from\":\"Phoenix\",\"id\":\"sesssion\",\"price\":\"$78.95\",\"seatClass\":\"busines\",\"seatNumber\":\"D012\",\"status\":\"301\",\"to\":\"Hello_world\",\"trainNumber\":\"98765\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2022-07-01T16:45:00-04:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1");
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
    public void test_POST_2() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2019-12-31T23:59:59Z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"0123456789\",\"contactsName\":\"Charlie davis\",\"differenceMoney\":\"-98765\",\"documentType\":\"6\",\"from\":\"denver\",\"id\":\"session_token\",\"price\":\"$10.99\",\"seatClass\":\"third\",\"seatNumber\":\"E345\",\"status\":\"201\",\"to\":\"tests\",\"trainNumber\":\"67890\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2019-12-31T23:59:59Z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"0123456789\",\"contactsName\":\"Charlie davis\",\"differenceMoney\":\"-98765\",\"documentType\":\"6\",\"from\":\"denver\",\"id\":\"session_token\",\"price\":\"$10.99\",\"seatClass\":\"third\",\"seatNumber\":\"E345\",\"status\":\"201\",\"to\":\"tests\",\"trainNumber\":\"67890\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"Xyzzy\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"abcd-efgh\",\"contactsName\":\"John Doe\",\"documentType\":\"1\",\"from\":\"los angeles\",\"id\":\"123\",\"price\":\"$45.00\",\"seatClass\":\"business\",\"seatNumber\":\"A123\",\"status\":\"201\",\"to\":\"tests\",\"trainNumber\":\"67890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"Xyzzy\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"abcd-efgh\",\"contactsName\":\"John Doe\",\"documentType\":\"1\",\"from\":\"los angeles\",\"id\":\"123\",\"price\":\"$45.00\",\"seatClass\":\"business\",\"seatNumber\":\"A123\",\"status\":\"201\",\"to\":\"tests\",\"trainNumber\":\"67890\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_2");
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
    public void test_POST_3() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"TestAccount123\",\"boughtDate\":\"2022-06-18t09:15:00z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"987.6543\",\"contactsName\":\"charlie davis\",\"differenceMoney\":\"-20.99\",\"documentType\":\"7\",\"from\":\"New york\",\"id\":\"nbc\",\"price\":\"$45.00\",\"seatClass\":\"busi_ness\",\"seatNumber\":\"b456\",\"status\":\"500\",\"to\":\"goodbye\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"TestAccount123\",\"boughtDate\":\"2022-06-18t09:15:00z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"987.6543\",\"contactsName\":\"charlie davis\",\"differenceMoney\":\"-20.99\",\"documentType\":\"7\",\"from\":\"New york\",\"id\":\"nbc\",\"price\":\"$45.00\",\"seatClass\":\"busi_ness\",\"seatNumber\":\"b456\",\"status\":\"500\",\"to\":\"goodbye\",\"trainNumber\":\"12345\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"12345\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"456789\",\"contactsDocumentNumber\":\"abcd-efgh\",\"contactsName\":\"charlie davis\",\"documentType\":\"765\",\"from\":\"chicago\",\"id\":\"cbs\",\"price\":\"$23456.78\",\"seatClass\":\"economy\",\"seatNumber\":\"B456\",\"status\":\"404\",\"to\":\"howdy\",\"trainNumber\":\"cbs\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2023-10-05t08:00:00z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"12345\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"456789\",\"contactsDocumentNumber\":\"abcd-efgh\",\"contactsName\":\"charlie davis\",\"documentType\":\"765\",\"from\":\"chicago\",\"id\":\"cbs\",\"price\":\"$23456.78\",\"seatClass\":\"economy\",\"seatNumber\":\"B456\",\"status\":\"404\",\"to\":\"howdy\",\"trainNumber\":\"cbs\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2023-10-05t08:00:00z\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_3");
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
    public void test_POST_4() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"4\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Bob brown\",\"differenceMoney\":\"0.01\",\"documentType\":\"3\",\"from\":\"New york\",\"id\":\"sessions\",\"price\":\"$0.99\",\"seatClass\":\"business\",\"seatNumber\":\"d012\",\"status\":\"500\",\"to\":\"admin@site.org\",\"trainNumber\":\"Xyz\",\"travelDate\":\"2025-07-08\",\"travelTime\":\"2025-11-30t00:00:00z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"4\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Bob brown\",\"differenceMoney\":\"0.01\",\"documentType\":\"3\",\"from\":\"New york\",\"id\":\"sessions\",\"price\":\"$0.99\",\"seatClass\":\"business\",\"seatNumber\":\"d012\",\"status\":\"500\",\"to\":\"admin@site.org\",\"trainNumber\":\"Xyz\",\"travelDate\":\"2025-07-08\",\"travelTime\":\"2025-11-30t00:00:00z\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2019-12-31T23:59:59Z\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Jane smith\",\"documentType\":\"6\",\"from\":\"New york\",\"id\":\"sessions\",\"price\":\"$23456.78\",\"seatClass\":\"economies\",\"seatNumber\":\"a123\",\"status\":\"500\",\"to\":\"john.doe+tag@gmail.com\",\"trainNumber\":\"nbc\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2020-03-10t18:15:00+02:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"Testaccount123\",\"boughtDate\":\"2019-12-31T23:59:59Z\",\"coachNumber\":\"var_bd_=_getElementsByClass\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Jane smith\",\"documentType\":\"6\",\"from\":\"New york\",\"id\":\"sessions\",\"price\":\"$23456.78\",\"seatClass\":\"economies\",\"seatNumber\":\"a123\",\"status\":\"500\",\"to\":\"john.doe+tag@gmail.com\",\"trainNumber\":\"nbc\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2020-03-10t18:15:00+02:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_4");
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
    public void test_POST_5() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"98765\",\"boughtDate\":\"2023-10-05t14:30:00z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"bob brown\",\"differenceMoney\":\"-20.99\",\"documentType\":\"9\",\"from\":\"Los angeles\",\"id\":\"users\",\"price\":\"$78.95\",\"seatClass\":\"Economy\",\"seatNumber\":\"B456\",\"status\":\"500\",\"to\":\"hi\",\"trainNumber\":\"xyz\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2022-07-01t16:45:00-04:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"98765\",\"boughtDate\":\"2023-10-05t14:30:00z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"bob brown\",\"differenceMoney\":\"-20.99\",\"documentType\":\"9\",\"from\":\"Los angeles\",\"id\":\"users\",\"price\":\"$78.95\",\"seatClass\":\"Economy\",\"seatNumber\":\"B456\",\"status\":\"500\",\"to\":\"hi\",\"trainNumber\":\"xyz\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2022-07-01t16:45:00-04:00\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"xyzzy\",\"boughtDate\":\"2023-10-05t14:30:00z\",\"coachNumber\":\"1\",\"contactsDocumentNumber\":\"Abcd-efgh\",\"contactsName\":\"John Doe\",\"documentType\":\"2\",\"from\":\"New York\",\"id\":\"user_123\",\"price\":\"$23456.78\",\"seatClass\":\"first\",\"seatNumber\":\"b456\",\"status\":\"404\",\"to\":\"Hello_world\",\"trainNumber\":\"Xyz\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2022-07-01T16:45:00-04:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"xyzzy\",\"boughtDate\":\"2023-10-05t14:30:00z\",\"coachNumber\":\"1\",\"contactsDocumentNumber\":\"Abcd-efgh\",\"contactsName\":\"John Doe\",\"documentType\":\"2\",\"from\":\"New York\",\"id\":\"user_123\",\"price\":\"$23456.78\",\"seatClass\":\"first\",\"seatNumber\":\"b456\",\"status\":\"404\",\"to\":\"Hello_world\",\"trainNumber\":\"Xyz\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2022-07-01T16:45:00-04:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_5");
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
    public void test_POST_6() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"98765\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"3\",\"contactsDocumentNumber\":\"buf\",\"contactsName\":\"John doe\",\"differenceMoney\":\"-20.99\",\"documentType\":\"6\",\"from\":\"New york\",\"id\":\"Abc-def-ghi\",\"price\":\"$23456.78\",\"seatClass\":\"Business\",\"seatNumber\":\"c789\",\"status\":\"201\",\"to\":\"user@example.com\",\"trainNumber\":\"12345\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2022-07-01T16:45:00-04:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"98765\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"3\",\"contactsDocumentNumber\":\"buf\",\"contactsName\":\"John doe\",\"differenceMoney\":\"-20.99\",\"documentType\":\"6\",\"from\":\"New york\",\"id\":\"Abc-def-ghi\",\"price\":\"$23456.78\",\"seatClass\":\"Business\",\"seatNumber\":\"c789\",\"status\":\"201\",\"to\":\"user@example.com\",\"trainNumber\":\"12345\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2022-07-01T16:45:00-04:00\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"98765\",\"boughtDate\":\"2025-07-29T00:00:00Z\",\"coachNumber\":\"3\",\"contactsDocumentNumber\":\"ABCD-EFGH\",\"contactsName\":\"john doe\",\"documentType\":\"1\",\"from\":\"baltimore\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"$45.00\",\"seatClass\":\"busi_ness\",\"seatNumber\":\"d012\",\"status\":\"500\",\"to\":\"goodbye\",\"trainNumber\":\"buf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2025-11-30t00:00:00z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"98765\",\"boughtDate\":\"2025-07-29T00:00:00Z\",\"coachNumber\":\"3\",\"contactsDocumentNumber\":\"ABCD-EFGH\",\"contactsName\":\"john doe\",\"documentType\":\"1\",\"from\":\"baltimore\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"$45.00\",\"seatClass\":\"busi_ness\",\"seatNumber\":\"d012\",\"status\":\"500\",\"to\":\"goodbye\",\"trainNumber\":\"buf\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2025-11-30t00:00:00z\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_6");
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
    public void test_POST_7() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"12345\",\"boughtDate\":\"2025-07-29t00:00:00z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"987.6543\",\"contactsName\":\"alice johnson\",\"differenceMoney\":\"45678\",\"documentType\":\"6\",\"from\":\"denver\",\"id\":\"user_123\",\"price\":\"$0.99\",\"seatClass\":\"third\",\"seatNumber\":\"B456\",\"status\":\"101\",\"to\":\"Hello_world\",\"trainNumber\":\"buf\",\"travelDate\":\"2025-07-08\",\"travelTime\":\"2025-11-30t00:00:00z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"12345\",\"boughtDate\":\"2025-07-29t00:00:00z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"987.6543\",\"contactsName\":\"alice johnson\",\"differenceMoney\":\"45678\",\"documentType\":\"6\",\"from\":\"denver\",\"id\":\"user_123\",\"price\":\"$0.99\",\"seatClass\":\"third\",\"seatNumber\":\"B456\",\"status\":\"101\",\"to\":\"Hello_world\",\"trainNumber\":\"buf\",\"travelDate\":\"2025-07-08\",\"travelTime\":\"2025-11-30t00:00:00z\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"12345\",\"boughtDate\":\"2023-10-05t14:30:00z\",\"coachNumber\":\"bd_div_story\",\"contactsDocumentNumber\":\"ABCD-EFGH\",\"contactsName\":\"Jane Smith\",\"documentType\":\"7\",\"from\":\"baltimore\",\"id\":\"Abc-def-ghi\",\"price\":\"$78.95\",\"seatClass\":\"Economy\",\"seatNumber\":\"a123\",\"status\":\"404\",\"to\":\"goodbye\",\"trainNumber\":\"ABC12\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2024-01-15t12:30:00+01:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"12345\",\"boughtDate\":\"2023-10-05t14:30:00z\",\"coachNumber\":\"bd_div_story\",\"contactsDocumentNumber\":\"ABCD-EFGH\",\"contactsName\":\"Jane Smith\",\"documentType\":\"7\",\"from\":\"baltimore\",\"id\":\"Abc-def-ghi\",\"price\":\"$78.95\",\"seatClass\":\"Economy\",\"seatNumber\":\"a123\",\"status\":\"404\",\"to\":\"goodbye\",\"trainNumber\":\"ABC12\",\"travelDate\":\"2026-02-29\",\"travelTime\":\"2024-01-15t12:30:00+01:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_7");
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
    public void test_POST_8() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"98765\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"BORDER_=\",\"contactsDocumentNumber\":\"XYZ123\",\"contactsName\":\"john doe\",\"differenceMoney\":\"-98765\",\"documentType\":\"9\",\"from\":\"baltimore\",\"id\":\"abc-def-ghi\",\"price\":\"$0.99\",\"seatClass\":\"second\",\"seatNumber\":\"C789\",\"status\":\"404\",\"to\":\"howdy\",\"trainNumber\":\"ABC12\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2025-11-30T00:00:00Z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"98765\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"BORDER_=\",\"contactsDocumentNumber\":\"XYZ123\",\"contactsName\":\"john doe\",\"differenceMoney\":\"-98765\",\"documentType\":\"9\",\"from\":\"baltimore\",\"id\":\"abc-def-ghi\",\"price\":\"$0.99\",\"seatClass\":\"second\",\"seatNumber\":\"C789\",\"status\":\"404\",\"to\":\"howdy\",\"trainNumber\":\"ABC12\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2025-11-30T00:00:00Z\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"abcde\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"4\",\"contactsDocumentNumber\":\"Abcd-efgh\",\"contactsName\":\"jane smith\",\"documentType\":\"765\",\"from\":\"atlanta\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"$23456.78\",\"seatClass\":\"economic\",\"seatNumber\":\"e345\",\"status\":\"404\",\"to\":\"howdy\",\"trainNumber\":\"Abc12\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2025-11-30T00:00:00Z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"abcde\",\"boughtDate\":\"2022-06-18T09:15:00Z\",\"coachNumber\":\"4\",\"contactsDocumentNumber\":\"Abcd-efgh\",\"contactsName\":\"jane smith\",\"documentType\":\"765\",\"from\":\"atlanta\",\"id\":\"Sydney_measured_####kbps\",\"price\":\"$23456.78\",\"seatClass\":\"economic\",\"seatNumber\":\"e345\",\"status\":\"404\",\"to\":\"howdy\",\"trainNumber\":\"Abc12\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2025-11-30T00:00:00Z\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_8");
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
    public void test_POST_9() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"xyzzy\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"1\",\"contactsDocumentNumber\":\"xyz123\",\"contactsName\":\"Jane Smith\",\"differenceMoney\":\"-20.99\",\"documentType\":\"765\",\"from\":\"baltimore\",\"id\":\"sesssion\",\"price\":\"$78.95\",\"seatClass\":\"second\",\"seatNumber\":\"A123\",\"status\":\"101\",\"to\":\"User@example.com\",\"trainNumber\":\"nbc\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2023-10-05t08:00:00z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"xyzzy\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"1\",\"contactsDocumentNumber\":\"xyz123\",\"contactsName\":\"Jane Smith\",\"differenceMoney\":\"-20.99\",\"documentType\":\"765\",\"from\":\"baltimore\",\"id\":\"sesssion\",\"price\":\"$78.95\",\"seatClass\":\"second\",\"seatNumber\":\"A123\",\"status\":\"101\",\"to\":\"User@example.com\",\"trainNumber\":\"nbc\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2023-10-05t08:00:00z\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"98765\",\"boughtDate\":\"2023-10-05T14:30:00Z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"abcd-efgh\",\"contactsName\":\"Charlie Davis\",\"documentType\":\"1\",\"from\":\"houston\",\"id\":\"Abc-def-ghi\",\"price\":\"$45.00\",\"seatClass\":\"first\",\"seatNumber\":\"e345\",\"status\":\"404\",\"to\":\"User@example.com\",\"trainNumber\":\"yyyy\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-01-15t12:30:00+01:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"98765\",\"boughtDate\":\"2023-10-05T14:30:00Z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"abcd-efgh\",\"contactsName\":\"Charlie Davis\",\"documentType\":\"1\",\"from\":\"houston\",\"id\":\"Abc-def-ghi\",\"price\":\"$45.00\",\"seatClass\":\"first\",\"seatNumber\":\"e345\",\"status\":\"404\",\"to\":\"User@example.com\",\"trainNumber\":\"yyyy\",\"travelDate\":\"2022-12-31\",\"travelTime\":\"2024-01-15t12:30:00+01:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_9");
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
    public void test_POST_10() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                    .setDescription("Service: ts-admin-order-service | Method: POST | Path: /api/v1/adminorderservice/adminorder | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-admin-order-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"xyzzy\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Charlie Davis\",\"differenceMoney\":\"-20.99\",\"documentType\":\"6\",\"from\":\"los angeles\",\"id\":\"users\",\"price\":\"$45.00\",\"seatClass\":\"fourth\",\"seatNumber\":\"c789\",\"status\":\"201\",\"to\":\"goodbye\",\"trainNumber\":\"98765\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2023-10-05T08:00:00Z\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"xyzzy\",\"boughtDate\":\"2019-12-31t23:59:59z\",\"coachNumber\":\"0\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"Charlie Davis\",\"differenceMoney\":\"-20.99\",\"documentType\":\"6\",\"from\":\"los angeles\",\"id\":\"users\",\"price\":\"$45.00\",\"seatClass\":\"fourth\",\"seatNumber\":\"c789\",\"status\":\"201\",\"to\":\"goodbye\",\"trainNumber\":\"98765\",\"travelDate\":\"2023-10-05\",\"travelTime\":\"2023-10-05T08:00:00Z\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
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
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-admin-order-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/adminorderservice/adminorder\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                    .setDescription("Service: ts-order-other-service | Method: POST | Path: /api/v1/orderOtherService/orderOther/admin | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-order-other-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/orderOtherService/orderOther/admin");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"testaccount123\",\"boughtDate\":\"2022-06-18t09:15:00z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"John Doe\",\"documentType\":\"3\",\"from\":\"baltimore\",\"id\":\"Abc-def-ghi\",\"price\":\"$78.95\",\"seatClass\":\"economies\",\"seatNumber\":\"C789\",\"status\":\"404\",\"to\":\"Test1234567890\",\"trainNumber\":\"cbs\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"accountId\":\"testaccount123\",\"boughtDate\":\"2022-06-18t09:15:00z\",\"coachNumber\":\"123\",\"contactsDocumentNumber\":\"yyyy\",\"contactsName\":\"John Doe\",\"documentType\":\"3\",\"from\":\"baltimore\",\"id\":\"Abc-def-ghi\",\"price\":\"$78.95\",\"seatClass\":\"economies\",\"seatNumber\":\"C789\",\"status\":\"404\",\"to\":\"Test1234567890\",\"trainNumber\":\"cbs\",\"travelDate\":\"2024-01-15\",\"travelTime\":\"2024-01-15T12:30:00+01:00\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
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
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                // Enhanced error reporting with detailed debugging information
                StringBuilder errorDetails = new StringBuilder();
                errorDetails.append("ERROR DETAILS:\n");
                errorDetails.append("Exception Type: ").append(t.getClass().getSimpleName()).append("\n");
                errorDetails.append("Error Message: ").append(t.getMessage()).append("\n\n");
                errorDetails.append("STEP DETAILS:\n");
                errorDetails.append("Service: ts-order-other-service\n");
                errorDetails.append("Method: POST\n");
                errorDetails.append("Path: /api/v1/orderOtherService/orderOther/admin\n");
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
                
                Allure.addAttachment("Detailed Error Report • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", "text/plain", errorDetails.toString());
                
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
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
        Allure.label("story", "test_POST_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_10");
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
    public void test_POST_1() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"234.99\",\"id\":\"abc-def-ghi\",\"routeId\":\"abcdefg\",\"trainType\":\"express\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"234.99\",\"id\":\"abc-def-ghi\",\"routeId\":\"abcdefg\",\"trainType\":\"express\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"78.6\",\"id\":\"nbc\",\"routeId\":\"path\",\"trainType\":\"Express\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"78.6\",\"id\":\"nbc\",\"routeId\":\"path\",\"trainType\":\"Express\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_1");
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
    public void test_POST_2() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"10.5\",\"firstClassPriceRate\":\"78.6\",\"id\":\"session_token\",\"routeId\":\"routes\",\"trainType\":\"cargo\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"10.5\",\"firstClassPriceRate\":\"78.6\",\"id\":\"session_token\",\"routeId\":\"routes\",\"trainType\":\"cargo\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"234.99\",\"id\":\"Abc-def-ghi\",\"routeId\":\"xyz7890\",\"trainType\":\"specification\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"234.99\",\"id\":\"Abc-def-ghi\",\"routeId\":\"xyz7890\",\"trainType\":\"specification\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_2");
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
    public void test_POST_3() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"10.5\",\"id\":\"sesion\",\"routeId\":\"routes\",\"trainType\":\"standards\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"10.5\",\"id\":\"sesion\",\"routeId\":\"routes\",\"trainType\":\"standards\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"0.001\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"Route-12\",\"trainType\":\"expressed\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"0.001\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"Route-12\",\"trainType\":\"expressed\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_3");
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
    public void test_POST_4() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"234.99\",\"id\":\"session_token\",\"routeId\":\"yyyy\",\"trainType\":\"stan_dards\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"234.99\",\"id\":\"session_token\",\"routeId\":\"yyyy\",\"trainType\":\"stan_dards\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"10.5\",\"id\":\"users\",\"routeId\":\"routes\",\"trainType\":\"Electric\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"10.5\",\"id\":\"users\",\"routeId\":\"routes\",\"trainType\":\"Electric\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_4");
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
    public void test_POST_5() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"10.5\",\"firstClassPriceRate\":\"0.001\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"12345\",\"trainType\":\"specification\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"10.5\",\"firstClassPriceRate\":\"0.001\",\"id\":\"Sydney_measured_####kbps\",\"routeId\":\"12345\",\"trainType\":\"specification\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.75\",\"firstClassPriceRate\":\"78.6\",\"id\":\"users\",\"routeId\":\"buf\",\"trainType\":\"standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.75\",\"firstClassPriceRate\":\"78.6\",\"id\":\"users\",\"routeId\":\"buf\",\"trainType\":\"standard\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_5");
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
    public void test_POST_6() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"-50.5\",\"id\":\"123\",\"routeId\":\"shortest_route\",\"trainType\":\"Cargo\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"-50.5\",\"id\":\"123\",\"routeId\":\"shortest_route\",\"trainType\":\"Cargo\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"-50.5\",\"id\":\"cbs\",\"routeId\":\"route-12\",\"trainType\":\"express\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"-50.5\",\"id\":\"cbs\",\"routeId\":\"route-12\",\"trainType\":\"express\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_6");
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
    public void test_POST_7() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"-50.5\",\"id\":\"Session_token\",\"routeId\":\"route-12\",\"trainType\":\"standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"-50.5\",\"id\":\"Session_token\",\"routeId\":\"route-12\",\"trainType\":\"standard\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"78.6\",\"id\":\"123\",\"routeId\":\"printf\",\"trainType\":\"stan_dards\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"78.6\",\"id\":\"123\",\"routeId\":\"printf\",\"trainType\":\"stan_dards\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_7");
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
    public void test_POST_8() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"78.6\",\"id\":\"abc-def-ghi\",\"routeId\":\"shortest_route\",\"trainType\":\"Standard\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"45.32\",\"firstClassPriceRate\":\"78.6\",\"id\":\"abc-def-ghi\",\"routeId\":\"shortest_route\",\"trainType\":\"Standard\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"0.001\",\"id\":\"abc-def-ghi\",\"routeId\":\"route-12\",\"trainType\":\"Electric\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"0.001\",\"id\":\"abc-def-ghi\",\"routeId\":\"route-12\",\"trainType\":\"Electric\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_8");
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
    public void test_POST_9() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"0.001\",\"id\":\"Session_token\",\"routeId\":\"Route-12\",\"trainType\":\"cargoes\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"800\",\"firstClassPriceRate\":\"0.001\",\"id\":\"Session_token\",\"routeId\":\"Route-12\",\"trainType\":\"cargoes\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"10.5\",\"id\":\"abc-def-ghi\",\"routeId\":\"yyyy\",\"trainType\":\"standards\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"10.5\",\"id\":\"abc-def-ghi\",\"routeId\":\"yyyy\",\"trainType\":\"standards\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_9");
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
    public void test_POST_10() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"78.6\",\"id\":\"cbs\",\"routeId\":\"path\",\"trainType\":\"Cargo\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"299.99\",\"firstClassPriceRate\":\"78.6\",\"id\":\"cbs\",\"routeId\":\"path\",\"trainType\":\"Cargo\"}");
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
                    .setDescription("Service: ts-price-service | Method: POST | Path: /api/v1/priceservice/prices | Expected Status: 200");
            Allure.getLifecycle().startStep(__uuid, __sr);
            Allure.parameter("Service", "ts-price-service");
            Allure.parameter("HTTP Method", "POST");
            Allure.parameter("Endpoint", "/api/v1/priceservice/prices");
            Allure.parameter("Expected Status", 200);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"0.75\",\"firstClassPriceRate\":\"0.001\",\"id\":\"sessions\",\"routeId\":\"buf\",\"trainType\":\"expressed\"}");
                Allure.addAttachment("Request Body", "application/json", "{\"basicPriceRate\":\"0.75\",\"firstClassPriceRate\":\"0.001\",\"id\":\"sessions\",\"routeId\":\"buf\",\"trainType\":\"expressed\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
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
        Allure.label("story", "test_POST_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_10");
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
