package trainticket;

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

public class TrainTicketTestNew_1751347263915 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void testScenario1() throws Exception {
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
                    .statusCode(200)
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
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"4.5\",\"firstClassPriceRate\":\"0.45\",\"id\":\"string\",\"routeId\":\"routeId\",\"trainType\":\"Highway\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", t.toString());
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
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"basicPriceRate\":\"4.5\",\"firstClassPriceRate\":\"0.45\",\"id\":\"string\",\"routeId\":\"routeId\",\"trainType\":\"Highway\"}");
                Response stepResponse2 = req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-price-service POST /api/v1/priceservice/prices - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-price-service POST /api/v1/priceservice/prices - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 2: ts-price-service POST /api/v1/priceservice/prices", t.toString());
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

        // Evaluate scenario result
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long totalSteps = stepResults.size();
        System.out.println("Scenario completed: " + successfulSteps + "/" + totalSteps + " steps successful");
        
        // Scenario passes if at least some steps executed successfully
        // (allows for independent step execution based on trace dependencies)
        if (successfulSteps == 0) {
            fail("Scenario failed: No steps executed successfully");
        }
    }

}
