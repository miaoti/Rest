import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImprovedAllureStepsDemo {
    
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        System.setProperty("allure.results.directory", "target/allure-results");
    }
    
    @Test
    public void demonstrateProperStepReporting() {
        String jwt = performLogin();
        
        boolean step1Success = executeStep1WithDetailedReporting(jwt);
        boolean step2Success = executeStep2WithDetailedReporting(jwt);
        boolean step3Success = executeStep3WithDetailedReporting(jwt);
        
        // Overall test evaluation with detailed reporting
        evaluateScenarioResults(step1Success, step2Success, step3Success);
    }
    
    @Step("🔐 Step 0: User Authentication")
    private String performLogin() {
        Allure.parameter("Service", "ts-user-service");
        Allure.parameter("Endpoint", "/api/v1/users/login");
        Allure.parameter("Method", "POST");
        
        try {
            Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                .then().statusCode(200)
                .extract().response();
                
            String token = response.jsonPath().getString("data.token");
            
            Allure.addAttachment("Login Response", "application/json", response.getBody().asString());
            Allure.parameter("Status", "✅ SUCCESS");
            
            return token;
        } catch (Exception e) {
            Allure.addAttachment("Login Error", "text/plain", e.getMessage());
            Allure.parameter("Status", "❌ FAILED");
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
    
    @Step("📡 Step 1: Get Admin Orders - ts-admin-order-service GET /api/v1/adminorderservice/adminorder")
    private boolean executeStep1WithDetailedReporting(String jwt) {
        Allure.parameter("Service", "ts-admin-order-service");
        Allure.parameter("Method", "GET");
        Allure.parameter("Endpoint", "/api/v1/adminorderservice/adminorder");
        Allure.parameter("Expected Status", 200);
        
        try {
            Response response = RestAssured.given()
                .header("Authorization", "Bearer " + jwt)
                .when().get("/api/v1/adminorderservice/adminorder")
                .then().statusCode(200)
                .extract().response();
                
            // Capture detailed response information
            String responseBody = response.getBody().asString();
            int actualStatus = response.getStatusCode();
            long responseTime = response.getTime();
            
            Allure.addAttachment("Response Body", "application/json", responseBody);
            Allure.parameter("Actual Status", actualStatus);
            Allure.parameter("Response Time (ms)", responseTime);
            Allure.parameter("Result", "✅ SUCCESS");
            
            return true;
        } catch (Exception e) {
            Allure.addAttachment("Step 1 Error Details", "text/plain", 
                "Error Type: " + e.getClass().getSimpleName() + "\n" +
                "Error Message: " + e.getMessage() + "\n" +
                "Service: ts-admin-order-service\n" +
                "Endpoint: /api/v1/adminorderservice/adminorder\n" +
                "Expected Status: 200");
            Allure.parameter("Result", "❌ FAILED");
            
            return false;
        }
    }
    
    @Step("🚂 Step 2: Search Routes - ts-route-service GET /api/v1/routeservice/routes")
    private boolean executeStep2WithDetailedReporting(String jwt) {
        Allure.parameter("Service", "ts-route-service");
        Allure.parameter("Method", "GET");
        Allure.parameter("Endpoint", "/api/v1/routeservice/routes");
        Allure.parameter("Expected Status", 200);
        
        try {
            Response response = RestAssured.given()
                .header("Authorization", "Bearer " + jwt)
                .when().get("/api/v1/routeservice/routes")
                .then().statusCode(200)
                .extract().response();
                
            String responseBody = response.getBody().asString();
            int actualStatus = response.getStatusCode();
            long responseTime = response.getTime();
            
            Allure.addAttachment("Response Body", "application/json", responseBody);
            Allure.parameter("Actual Status", actualStatus);
            Allure.parameter("Response Time (ms)", responseTime);
            Allure.parameter("Result", "✅ SUCCESS");
            
            return true;
        } catch (Exception e) {
            Allure.addAttachment("Step 2 Error Details", "text/plain", 
                "Error Type: " + e.getClass().getSimpleName() + "\n" +
                "Error Message: " + e.getMessage() + "\n" +
                "Service: ts-route-service\n" +
                "Endpoint: /api/v1/routeservice/routes\n" +
                "Expected Status: 200");
            Allure.parameter("Result", "❌ FAILED");
            
            return false;
        }
    }
    
    @Step("🎫 Step 3: Get Ticket Information - ts-basic-service GET /api/v1/basicservice/basic/travel")
    private boolean executeStep3WithDetailedReporting(String jwt) {
        Allure.parameter("Service", "ts-basic-service");
        Allure.parameter("Method", "GET");
        Allure.parameter("Endpoint", "/api/v1/basicservice/basic/travel");
        Allure.parameter("Expected Status", 200);
        
        try {
            Response response = RestAssured.given()
                .header("Authorization", "Bearer " + jwt)
                .when().get("/api/v1/basicservice/basic/travel")
                .then().statusCode(200)
                .extract().response();
                
            String responseBody = response.getBody().asString();
            int actualStatus = response.getStatusCode();
            long responseTime = response.getTime();
            
            Allure.addAttachment("Response Body", "application/json", responseBody);
            Allure.parameter("Actual Status", actualStatus);
            Allure.parameter("Response Time (ms)", responseTime);
            Allure.parameter("Result", "✅ SUCCESS");
            
            return true;
        } catch (Exception e) {
            Allure.addAttachment("Step 3 Error Details", "text/plain", 
                "Error Type: " + e.getClass().getSimpleName() + "\n" +
                "Error Message: " + e.getMessage() + "\n" +
                "Service: ts-basic-service\n" +
                "Endpoint: /api/v1/basicservice/basic/travel\n" +
                "Expected Status: 200");
            Allure.parameter("Result", "❌ FAILED");
            
            return false;
        }
    }
    
    @Step("📊 Scenario Evaluation: Microservice Workflow Validation")
    private void evaluateScenarioResults(boolean step1, boolean step2, boolean step3) {
        int totalSteps = 3;
        int successfulSteps = (step1 ? 1 : 0) + (step2 ? 1 : 0) + (step3 ? 1 : 0);
        int failedSteps = totalSteps - successfulSteps;
        
        // Create detailed summary
        StringBuilder summary = new StringBuilder();
        summary.append("🎯 MICROSERVICE TEST SCENARIO SUMMARY\n");
        summary.append("=====================================\n\n");
        summary.append("📈 Overall Results:\n");
        summary.append("• Total Steps: ").append(totalSteps).append("\n");
        summary.append("• Successful: ").append(successfulSteps).append("\n");
        summary.append("• Failed: ").append(failedSteps).append("\n");
        summary.append("• Success Rate: ").append(String.format("%.1f%%", (successfulSteps * 100.0 / totalSteps))).append("\n\n");
        
        summary.append("🔍 Step-by-Step Breakdown:\n");
        summary.append("• Step 1 (Admin Orders): ").append(step1 ? "✅ PASS" : "❌ FAIL").append("\n");
        summary.append("• Step 2 (Route Search): ").append(step2 ? "✅ PASS" : "❌ FAIL").append("\n");
        summary.append("• Step 3 (Ticket Info): ").append(step3 ? "✅ PASS" : "❌ FAIL").append("\n\n");
        
        if (failedSteps == 0) {
            summary.append("🎉 RESULT: ALL STEPS PASSED - Microservice workflow completed successfully!");
        } else {
            summary.append("⚠️ RESULT: ").append(failedSteps).append(" step(s) failed - Workflow incomplete");
        }
        
        Allure.addAttachment("Test Execution Summary", "text/plain", summary.toString());
        Allure.parameter("Total Steps", totalSteps);
        Allure.parameter("Successful Steps", successfulSteps);
        Allure.parameter("Failed Steps", failedSteps);
        Allure.parameter("Success Rate", String.format("%.1f%%", (successfulSteps * 100.0 / totalSteps)));
        
        // Add test categorization for Allure
        if (failedSteps == 0) {
            Allure.label("severity", "normal");
            Allure.label("testType", "Complete Success");
        } else if (successfulSteps > 0) {
            Allure.label("severity", "major");
            Allure.label("testType", "Partial Failure");
        } else {
            Allure.label("severity", "critical");
            Allure.label("testType", "Complete Failure");
        }
        
        Allure.label("feature", "Microservice Integration");
        Allure.label("story", "Multi-Service Workflow Testing");
        
        // Fail the test if any step failed (strict microservice validation)
        if (failedSteps > 0) {
            fail("Microservice workflow failed: " + failedSteps + " out of " + totalSteps + 
                 " steps failed. All microservice endpoints must be accessible for end-to-end validation.");
        }
    }
} 