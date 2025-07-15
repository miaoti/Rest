package trainticket_twostage_test.TrainTicketTwoStageTest_1752567765454;

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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"123.45\",\"distances\":\"45\",\"endStation\":\"Central Station\",\"id\":\"TRO_temporary_restraining\",\"loginId\":\"admin456\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Landmark C\",\"stations\":\"Houston Ellsworth Air Force Base\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"123.45\",\"distances\":\"45\",\"endStation\":\"Central Station\",\"id\":\"TRO_temporary_restraining\",\"loginId\":\"admin456\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Landmark C\",\"stations\":\"Houston Ellsworth Air Force Base\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"123.45\",\"endStation\":\"Central Station\",\"id\":\"TRO_temporary_restraining\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Landmark C\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"123.45\",\"endStation\":\"Central Station\",\"id\":\"TRO_temporary_restraining\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Landmark C\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"mam\",\"distances\":\"10\",\"endStation\":\"union square\",\"id\":\"Sydney_measured_####kbps\",\"loginId\":\"users\",\"startStation\":\"San Francisco International Terminal\",\"stationList\":\"Landmark C\",\"stations\":\"Chicago O'Hare International Airport\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"mam\",\"distances\":\"10\",\"endStation\":\"union square\",\"id\":\"Sydney_measured_####kbps\",\"loginId\":\"users\",\"startStation\":\"San Francisco International Terminal\",\"stationList\":\"Landmark C\",\"stations\":\"Chicago O'Hare International Airport\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"mam\",\"endStation\":\"union square\",\"id\":\"Sydney_measured_####kbps\",\"startStation\":\"San Francisco International Terminal\",\"stationList\":\"Landmark C\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"mam\",\"endStation\":\"union square\",\"id\":\"Sydney_measured_####kbps\",\"startStation\":\"San Francisco International Terminal\",\"stationList\":\"Landmark C\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"50\",\"distances\":\"25.6\",\"endStation\":\"chinatown\",\"id\":\"TRO_temporary_restraining\",\"loginId\":\"JohnDoe789\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\",\"stations\":\"Phoenix Sky Harbor International Airport\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"50\",\"distances\":\"25.6\",\"endStation\":\"chinatown\",\"id\":\"TRO_temporary_restraining\",\"loginId\":\"JohnDoe789\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\",\"stations\":\"Phoenix Sky Harbor International Airport\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"50\",\"endStation\":\"chinatown\",\"id\":\"TRO_temporary_restraining\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"50\",\"endStation\":\"chinatown\",\"id\":\"TRO_temporary_restraining\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"mam\",\"distances\":\"300\",\"endStation\":\"central station\",\"id\":\"order_2023\",\"loginId\":\"JohnDoe789\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"monument e\",\"stations\":\"Houston Ellsworth Air Force Base\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"mam\",\"distances\":\"300\",\"endStation\":\"central station\",\"id\":\"order_2023\",\"loginId\":\"JohnDoe789\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"monument e\",\"stations\":\"Houston Ellsworth Air Force Base\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"mam\",\"endStation\":\"central station\",\"id\":\"order_2023\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"monument e\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"mam\",\"endStation\":\"central station\",\"id\":\"order_2023\",\"startStation\":\"Boston logan international airport\",\"stationList\":\"monument e\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"gran\",\"distances\":\"25.6\",\"endStation\":\"chinatown\",\"id\":\"orders\",\"loginId\":\"admins\",\"startStation\":\"new york penn station\",\"stationList\":\"Landmark C\",\"stations\":\"los angeles union station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"gran\",\"distances\":\"25.6\",\"endStation\":\"chinatown\",\"id\":\"orders\",\"loginId\":\"admins\",\"startStation\":\"new york penn station\",\"stationList\":\"Landmark C\",\"stations\":\"los angeles union station\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"gran\",\"endStation\":\"chinatown\",\"id\":\"orders\",\"startStation\":\"new york penn station\",\"stationList\":\"Landmark C\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"gran\",\"endStation\":\"chinatown\",\"id\":\"orders\",\"startStation\":\"new york penn station\",\"stationList\":\"Landmark C\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"gran\",\"distances\":\"300\",\"endStation\":\"Union square\",\"id\":\"user_01\",\"loginId\":\"users\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Subway D\",\"stations\":\"new york penn station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"gran\",\"distances\":\"300\",\"endStation\":\"Union square\",\"id\":\"user_01\",\"loginId\":\"users\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Subway D\",\"stations\":\"new york penn station\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"gran\",\"endStation\":\"Union square\",\"id\":\"user_01\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Subway D\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"gran\",\"endStation\":\"Union square\",\"id\":\"user_01\",\"startStation\":\"San francisco international terminal\",\"stationList\":\"Subway D\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"-10\",\"distances\":\"25.6\",\"endStation\":\"ralph\",\"id\":\"buf\",\"loginId\":\"user123\",\"startStation\":\"san francisco international terminal\",\"stationList\":\"Landmark C\",\"stations\":\"Los Angeles Union Station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"-10\",\"distances\":\"25.6\",\"endStation\":\"ralph\",\"id\":\"buf\",\"loginId\":\"user123\",\"startStation\":\"san francisco international terminal\",\"stationList\":\"Landmark C\",\"stations\":\"Los Angeles Union Station\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"-10\",\"endStation\":\"ralph\",\"id\":\"buf\",\"startStation\":\"san francisco international terminal\",\"stationList\":\"Landmark C\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"-10\",\"endStation\":\"ralph\",\"id\":\"buf\",\"startStation\":\"san francisco international terminal\",\"stationList\":\"Landmark C\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"7890\",\"distances\":\"45\",\"endStation\":\"Times square\",\"id\":\"Abcde\",\"loginId\":\"Admins\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\",\"stations\":\"Chicago O'Hare International Airport\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"7890\",\"distances\":\"45\",\"endStation\":\"Times square\",\"id\":\"Abcde\",\"loginId\":\"Admins\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\",\"stations\":\"Chicago O'Hare International Airport\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"7890\",\"endStation\":\"Times square\",\"id\":\"Abcde\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"7890\",\"endStation\":\"Times square\",\"id\":\"Abcde\",\"startStation\":\"New york penn station\",\"stationList\":\"Bus Stop B\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"NaN\",\"distances\":\"45\",\"endStation\":\"safeway\",\"id\":\"yyyy\",\"loginId\":\"admin456\",\"startStation\":\"Boston Logan International Airport\",\"stationList\":\"Monument E\",\"stations\":\"Los Angeles Union Station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"NaN\",\"distances\":\"45\",\"endStation\":\"safeway\",\"id\":\"yyyy\",\"loginId\":\"admin456\",\"startStation\":\"Boston Logan International Airport\",\"stationList\":\"Monument E\",\"stations\":\"Los Angeles Union Station\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"NaN\",\"endStation\":\"safeway\",\"id\":\"yyyy\",\"startStation\":\"Boston Logan International Airport\",\"stationList\":\"Monument E\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"NaN\",\"endStation\":\"safeway\",\"id\":\"yyyy\",\"startStation\":\"Boston Logan International Airport\",\"stationList\":\"Monument E\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision1;
        // This step is INDEPENDENT - always execute
        decision1 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision1.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"7890\",\"distances\":\"45\",\"endStation\":\"safeway\",\"id\":\"Order_2023\",\"loginId\":\"Sydney_measured_####kbps\",\"startStation\":\"New York Penn Station\",\"stationList\":\"Subway d\",\"stations\":\"los angeles union station\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"7890\",\"distances\":\"45\",\"endStation\":\"safeway\",\"id\":\"Order_2023\",\"loginId\":\"Sydney_measured_####kbps\",\"startStation\":\"New York Penn Station\",\"stationList\":\"Subway d\",\"stations\":\"los angeles union station\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(1, false);
            System.out.println("⏭️ SKIPPING: Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) - " + decision1.skipReason.description + " (" + decision1.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminrouteservice/adminroute");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision1.skipReason.description);
                Allure.parameter("💬 Skip Details", decision1.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-admin-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/adminrouteservice/adminroute\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision1.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision1.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 1 + " - ts-admin-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/adminrouteservice/adminroute\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision1.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision1.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision1.skipMessage);
            });
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision2;
        // This step is INDEPENDENT - always execute
        decision2 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision2.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(2, false);
            System.out.println("⏭️ SKIPPING: Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) - " + decision2.skipReason.description + " (" + decision2.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-station-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/stationservice/stations/idlist");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision2.skipReason.description);
                Allure.parameter("💬 Skip Details", decision2.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-station-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/stationservice/stations/idlist\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision2.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision2.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 2 + " - ts-station-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/stationservice/stations/idlist\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision2.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision2.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision2.skipMessage);
            });
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)
        // Intelligent dependency analysis - determine if step should execute
        MultiServiceTestCase.ExecutionDecision decision3;
        // This step is INDEPENDENT - always execute
        decision3 = new MultiServiceTestCase.ExecutionDecision(true, null, null);

        if (decision3.shouldExecute) {
            System.out.println("✅ EXECUTING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (dependency analysis passed)");
            // Use Allure.step() for proper step hierarchy and visualization
            Allure.step("Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "EXECUTE - Dependencies satisfied");
                Allure.description("🎯 **Testing**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "✅ **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                try {
                    RequestSpecification req = RestAssured.given();
                    if (loginSucceeded.get()) {
                        req = req.header("Authorization", jwtType + " " + jwt);
                    }
                    req = req.contentType("application/json");
                    req = req.body("{\"distanceList\":\"7890\",\"endStation\":\"safeway\",\"id\":\"Order_2023\",\"startStation\":\"New York Penn Station\",\"stationList\":\"Subway d\"}");
                    Allure.addAttachment("📋 Request Body", "application/json", "{\"distanceList\":\"7890\",\"endStation\":\"safeway\",\"id\":\"Order_2023\",\"startStation\":\"New York Penn Station\",\"stationList\":\"Subway d\"}");
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
                        Allure.parameter("📊 Response Size", responseBody.length() + " chars");
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
            }); // End of Allure.step()
        } else {   // step skipped due to intelligent dependency analysis
            stepResults.put(3, false);
            System.out.println("⏭️ SKIPPING: Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) - " + decision3.skipReason.description + " (" + decision3.skipMessage + ")");
            // Create detailed skip step with comprehensive reasoning
            Allure.step("⏭️ Step 3: ts-route-service POST /api/v1/routeservice/routes (expect 200) (SKIPPED)", () -> {
                Allure.parameter("🏢 Service", "ts-route-service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/routeservice/routes");
                Allure.parameter("✅ Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.parameter("📊 Execution Decision", "SKIP");
                Allure.parameter("⏭️ Skip Reason", decision3.skipReason.description);
                Allure.parameter("💬 Skip Details", decision3.skipMessage);
                Allure.description("⏭️ **Step Skipped**: ts-route-service\n" +
                                 "📡 **Method**: POST\n" +
                                 "🔗 **Path**: /api/v1/routeservice/routes\n" +
                                 "🔗 **Dependency Type**: INDEPENDENT (can execute regardless of other step results)\n" +
                                 "⏭️ **Skip Reason**: " + decision3.skipReason.description + "\n" +
                                 "💬 **Details**: " + decision3.skipMessage);
                // Generate comprehensive dependency analysis attachment
                StringBuilder depAnalysis = new StringBuilder();
                depAnalysis.append("📋 DEPENDENCY ANALYSIS REPORT\n\n");
                depAnalysis.append("🔍 Step: " + 3 + " - ts-route-service\n");
                depAnalysis.append("📡 Method: post\n");
                depAnalysis.append("🔗 Path: /api/v1/routeservice/routes\n\n");
                depAnalysis.append("📊 EXECUTION DECISION LOGIC:\n");
                depAnalysis.append("  Reason: " + decision3.skipReason.description + "\n");
                depAnalysis.append("  Details: " + decision3.skipMessage + "\n\n");
                depAnalysis.append("📈 PREVIOUS STEP RESULTS:\n");
                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {
                    String status = result.getValue() ? "✅ PASSED" : "❌ FAILED";
                    depAnalysis.append("  Step " + result.getKey() + ": " + status + "\n");
                }
                depAnalysis.append("\n");
                depAnalysis.append("🎯 IMPACT ANALYSIS:\n");
                depAnalysis.append("  • This step was skipped to prevent cascading failures\n");
                depAnalysis.append("  • Dependent steps may also be skipped if they rely on this step\n");
                depAnalysis.append("  • Independent steps will continue to execute\n");
                Allure.addAttachment("🔍 Dependency Analysis Report", "text/plain", depAnalysis.toString());
                throw new org.junit.AssumptionViolatedException("Step skipped: " + decision3.skipMessage);
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
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

}
