package trainticket_twostage_test.TrainTicketTwoStageTest_1752569887665;

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

public class POST_api_v1_adminrouteservice_adminroute_11 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_POST_11_1() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"300\",\"distances\":\"25.6\",\"endStation\":\"Grand Central Terminal\",\"id\":\"test_data\",\"loginId\":\"Guest789\",\"startStation\":\"New York Penn Station\",\"stationList\":\"London victoria station\",\"stations\":\"Baker street\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"300\",\"distances\":\"25.6\",\"endStation\":\"Grand Central Terminal\",\"id\":\"test_data\",\"loginId\":\"Guest789\",\"startStation\":\"New York Penn Station\",\"stationList\":\"London victoria station\",\"stations\":\"Baker street\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"300\",\"endStation\":\"Grand Central Terminal\",\"id\":\"test_data\",\"startStation\":\"New York Penn Station\",\"stationList\":\"London victoria station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"300\",\"endStation\":\"Grand Central Terminal\",\"id\":\"test_data\",\"startStation\":\"New York Penn Station\",\"stationList\":\"London victoria station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_1");
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
    public void test_POST_11_2() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"450.75\",\"distances\":\"25.6\",\"endStation\":\"Borough Hall\",\"id\":\"buf\",\"loginId\":\"admin456\",\"startStation\":\"los angeles union station\",\"stationList\":\"san francisco union square\",\"stations\":\"Central station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"450.75\",\"distances\":\"25.6\",\"endStation\":\"Borough Hall\",\"id\":\"buf\",\"loginId\":\"admin456\",\"startStation\":\"los angeles union station\",\"stationList\":\"san francisco union square\",\"stations\":\"Central station\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"450.75\",\"endStation\":\"Borough Hall\",\"id\":\"buf\",\"startStation\":\"los angeles union station\",\"stationList\":\"san francisco union square\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"450.75\",\"endStation\":\"Borough Hall\",\"id\":\"buf\",\"startStation\":\"los angeles union station\",\"stationList\":\"san francisco union square\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_2");
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
    public void test_POST_11_3() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"distances\":\"45.3\",\"endStation\":\"springfield train station\",\"id\":\"distinctive\",\"loginId\":\"User123\",\"startStation\":\"San francisco international airport\",\"stationList\":\"tokyo shinjuku station\",\"stations\":\"downtown crossing\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"distances\":\"45.3\",\"endStation\":\"springfield train station\",\"id\":\"distinctive\",\"loginId\":\"User123\",\"startStation\":\"San francisco international airport\",\"stationList\":\"tokyo shinjuku station\",\"stations\":\"downtown crossing\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"endStation\":\"springfield train station\",\"id\":\"distinctive\",\"startStation\":\"San francisco international airport\",\"stationList\":\"tokyo shinjuku station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"endStation\":\"springfield train station\",\"id\":\"distinctive\",\"startStation\":\"San francisco international airport\",\"stationList\":\"tokyo shinjuku station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_3");
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
    public void test_POST_11_4() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"300\",\"distances\":\"9.9\",\"endStation\":\"borough hall\",\"id\":\"uniquely\",\"loginId\":\"McIntyre_fondly_remembered\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"New york penn station\",\"stations\":\"Times Square\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"300\",\"distances\":\"9.9\",\"endStation\":\"borough hall\",\"id\":\"uniquely\",\"loginId\":\"McIntyre_fondly_remembered\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"New york penn station\",\"stations\":\"Times Square\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"300\",\"endStation\":\"borough hall\",\"id\":\"uniquely\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"New york penn station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"300\",\"endStation\":\"borough hall\",\"id\":\"uniquely\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"New york penn station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_4");
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
    public void test_POST_11_5() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"10\",\"distances\":\"25.6\",\"endStation\":\"Grand central terminal\",\"id\":\"Abcdef123\",\"loginId\":\"user123\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"London Victoria Station\",\"stations\":\"Central Station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"10\",\"distances\":\"25.6\",\"endStation\":\"Grand central terminal\",\"id\":\"Abcdef123\",\"loginId\":\"user123\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"London Victoria Station\",\"stations\":\"Central Station\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"10\",\"endStation\":\"Grand central terminal\",\"id\":\"Abcdef123\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"London Victoria Station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"10\",\"endStation\":\"Grand central terminal\",\"id\":\"Abcdef123\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"London Victoria Station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_5");
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
    public void test_POST_11_6() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"25.6\",\"distances\":\"45.3\",\"endStation\":\"grand central terminal\",\"id\":\"unique_id789\",\"loginId\":\"testuser001\",\"startStation\":\"los angeles union station\",\"stationList\":\"San francisco union square\",\"stations\":\"Downtown crossing\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"25.6\",\"distances\":\"45.3\",\"endStation\":\"grand central terminal\",\"id\":\"unique_id789\",\"loginId\":\"testuser001\",\"startStation\":\"los angeles union station\",\"stationList\":\"San francisco union square\",\"stations\":\"Downtown crossing\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"25.6\",\"endStation\":\"grand central terminal\",\"id\":\"unique_id789\",\"startStation\":\"los angeles union station\",\"stationList\":\"San francisco union square\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"25.6\",\"endStation\":\"grand central terminal\",\"id\":\"unique_id789\",\"startStation\":\"los angeles union station\",\"stationList\":\"San francisco union square\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_6");
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
    public void test_POST_11_7() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"distances\":\"10\",\"endStation\":\"Borough Hall\",\"id\":\"Sydney_measured_####kbps\",\"loginId\":\"admins\",\"startStation\":\"San Francisco International Airport\",\"stationList\":\"London Victoria Station\",\"stations\":\"Downtown Crossing\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"distances\":\"10\",\"endStation\":\"Borough Hall\",\"id\":\"Sydney_measured_####kbps\",\"loginId\":\"admins\",\"startStation\":\"San Francisco International Airport\",\"stationList\":\"London Victoria Station\",\"stations\":\"Downtown Crossing\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"endStation\":\"Borough Hall\",\"id\":\"Sydney_measured_####kbps\",\"startStation\":\"San Francisco International Airport\",\"stationList\":\"London Victoria Station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"endStation\":\"Borough Hall\",\"id\":\"Sydney_measured_####kbps\",\"startStation\":\"San Francisco International Airport\",\"stationList\":\"London Victoria Station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_7");
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
    public void test_POST_11_8() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"distances\":\"9.9\",\"endStation\":\"springfield train station\",\"id\":\"uniqueness\",\"loginId\":\"john_doe2023\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"sydney central railway station\",\"stations\":\"Times Square\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"distances\":\"9.9\",\"endStation\":\"springfield train station\",\"id\":\"uniqueness\",\"loginId\":\"john_doe2023\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"sydney central railway station\",\"stations\":\"Times Square\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"endStation\":\"springfield train station\",\"id\":\"uniqueness\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"sydney central railway station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"endStation\":\"springfield train station\",\"id\":\"uniqueness\",\"startStation\":\"Los Angeles Union Station\",\"stationList\":\"sydney central railway station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_8");
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
    public void test_POST_11_9() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"distances\":\"25.6\",\"endStation\":\"Borough hall\",\"id\":\"users\",\"loginId\":\"guest789\",\"startStation\":\"los angeles union station\",\"stationList\":\"Sydney central railway station\",\"stations\":\"Baker Street\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"distances\":\"25.6\",\"endStation\":\"Borough hall\",\"id\":\"users\",\"loginId\":\"guest789\",\"startStation\":\"los angeles union station\",\"stationList\":\"Sydney central railway station\",\"stations\":\"Baker Street\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"800\",\"endStation\":\"Borough hall\",\"id\":\"users\",\"startStation\":\"los angeles union station\",\"stationList\":\"Sydney central railway station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"800\",\"endStation\":\"Borough hall\",\"id\":\"users\",\"startStation\":\"los angeles union station\",\"stationList\":\"Sydney central railway station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_9");
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
    public void test_POST_11_10() throws Exception {
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
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
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
                
                // ✅ Login successful - capture token details
                Allure.parameter("✅ Login Status", "SUCCESS");
                Allure.parameter("🔑 Token Obtained", _jwt[0] != null ? "Yes" : "No");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ Login failed - capture error details
                Allure.parameter("❌ Login Status", "FAILED");
                Allure.parameter("💥 Error Type", loginError.getClass().getSimpleName());
                Allure.parameter("💬 Error Message", loginError.getMessage());
                
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-admin-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"25.6\",\"distances\":\"45.3\",\"endStation\":\"Union square\",\"id\":\"distinctive\",\"loginId\":\"Sydney_measured_####kbps\",\"startStation\":\"chicago o'hare international airport\",\"stationList\":\"Sydney central railway station\",\"stations\":\"Downtown Crossing\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"25.6\",\"distances\":\"45.3\",\"endStation\":\"Union square\",\"id\":\"distinctive\",\"loginId\":\"Sydney_measured_####kbps\",\"startStation\":\"chicago o'hare international airport\",\"stationList\":\"Sydney central railway station\",\"stations\":\"Downtown Crossing\"}");
                    Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(1, true);
                    System.out.println("✅ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(1, false);
                    System.out.println("❌ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-admin-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/adminrouteservice/adminroute\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + skipReason);
                stepResults.put(1, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-admin-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/adminrouteservice/adminroute\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-station-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-station-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
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
                    System.out.println("✅ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(2, false);
                    System.out.println("❌ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-station-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/stationservice/stations/idlist\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + skipReason);
                stepResults.put(2, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-station-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/stationservice/stations/idlist\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
            Allure.parameter("🏢 Service", "ts-route-service");
            Allure.parameter("📡 HTTP Method", "POST");
            Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
            Allure.parameter("✅ Expected Status", 200);
            Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
            Allure.description("🎯 **Testing**: ts-route-service\n" +
                             "📡 **Method**: POST\n" +
                             "🔗 **Path**: /api/v1/routeservice/routes\n" +
                             "✅ **Expected**: 200\n" +
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
                Allure.parameter("📊 Execution Decision", "SKIP - " + skipCategory);
                Allure.parameter("⏭️ Skip Reason", skipReason);
            } else {
                Allure.parameter("📊 Execution Decision", "EXECUTE - All dependencies satisfied");
            }
            
            if (!shouldSkip) {
                System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"25.6\",\"endStation\":\"Union square\",\"id\":\"distinctive\",\"startStation\":\"chicago o'hare international airport\",\"stationList\":\"Sydney central railway station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"25.6\",\"endStation\":\"Union square\",\"id\":\"distinctive\",\"startStation\":\"chicago o'hare international airport\",\"stationList\":\"Sydney central railway station\"}");
                    Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                           .then().log().ifValidationFails()
                           .statusCode(200)
                           .extract().response();
                    stepResults.put(3, true);
                    System.out.println("✅ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - SUCCESS");
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
                        Allure.parameter("📊 Result", "SUCCESS");
                    } catch (Exception e) {
                        Allure.addAttachment("⚠️ Response Capture Error", "text/plain", e.getMessage());
                    }
                } catch (Throwable t) {
                    stepResults.put(3, false);
                    System.out.println("❌ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - FAILED: " + t.getMessage());
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
                    Allure.parameter("📊 Result", "FAILED");
                    
                    // Create detailed error report
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("🚨 STEP FAILURE REPORT\n\n");
                    errorDetails.append("📋 STEP INFO:\n");
                    errorDetails.append("Service: ts-route-service\n");
                    errorDetails.append("Method: POST\n");
                    errorDetails.append("Path: /api/v1/routeservice/routes\n");
                    errorDetails.append("Expected Status: 200\n\n");
                    errorDetails.append("💥 ERROR INFO:\n");
                    errorDetails.append("Type: ").append(t.getClass().getSimpleName()).append("\n");
                    errorDetails.append("Message: ").append(t.getMessage()).append("\n\n");
                    errorDetails.append("📚 FULL STACK TRACE:\n").append(t.toString());
                    
                    Allure.addAttachment("🚨 Step Failure Details", "text/plain", errorDetails.toString());
                    
                    // Throw to mark step as failed in Allure
                    throw new RuntimeException("Step failed: " + t.getMessage(), t);
                }
            } else {
                // Step is being skipped - show comprehensive skip information
                System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + skipReason);
                stepResults.put(3, false);
                
                // Add comprehensive skip information as parameters
                Allure.parameter("⏭️ Skip Category", skipCategory);
                Allure.parameter("💬 Skip Details", skipReason);
                Allure.parameter("📊 Result", "SKIPPED");
                
                // Generate detailed dependency analysis report
                StringBuilder dependencyReport = new StringBuilder();
                dependencyReport.append("⏭️ STEP SKIP ANALYSIS\n\n");
                dependencyReport.append("📋 STEP INFO:\n");
                dependencyReport.append("Service: ts-route-service\n");
                dependencyReport.append("Method: POST\n");
                dependencyReport.append("Path: /api/v1/routeservice/routes\n");
                dependencyReport.append("Expected Status: 200\n\n");
                dependencyReport.append("⏭️ SKIP REASON:\n");
                dependencyReport.append("Category: ").append(skipCategory).append("\n");
                dependencyReport.append("Details: ").append(skipReason).append("\n\n");
                
                // Add dependency analysis
                if (!loginSucceeded.get()) {
                    dependencyReport.append("🔐 AUTHENTICATION STATUS: FAILED\n");
                    dependencyReport.append("Authentication is required for all API calls.\n\n");
                }
                dependencyReport.append("💡 IMPACT:\n");
                dependencyReport.append("This step was skipped to prevent cascading failures.\n");
                dependencyReport.append("Fix the dependency issues above to enable this step.\n");
                
                Allure.addAttachment("⏭️ Skip Analysis Report", "text/plain", dependencyReport.toString());
                
                // Mark step as skipped in Allure using AssumptionViolatedException
                throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
            }
        }); // End of Allure.step()

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
        Allure.label("story", "test_POST_11_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps. " +
                           "Generated using two-stage LLM + semantic expansion approach.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_POST_11_10");
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
