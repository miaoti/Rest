package books;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import static org.junit.Assert.*;

public class BooksTest_1747605171744 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void testScenario1() throws Exception {
            // 1) perform login and capture JWT
            Response loginRes = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"YOUR_USERNAME\",\"password\":\"YOUR_PASSWORD\"}")
            .when().post("/login").then().log().ifValidationFails()
                .statusCode(200)
                .extract().response();
            String jwt     = loginRes.jsonPath().getString("id");
            String jwtType = loginRes.jsonPath().getString("type");

        boolean scenarioFailed = false;
        StringBuilder failureLog = new StringBuilder();

        // Step 1: user-service POST /users
        if (!scenarioFailed) {
            String __stepId1 = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr1 = new io.qameta.allure.model.StepResult().setName("Step 1: user-service POST /users");
            Allure.getLifecycle().startStep(__stepId1, __sr1);
            try {
                RequestSpecification req1 = RestAssured.given()
                    .header("Authorization", jwtType + " " + jwt);
                req1 = req1.log().all();
                req1 = req1.filter(new AllureRestAssured());
                Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
            } catch (Throwable t) {
                scenarioFailed = true;
                Allure.addAttachment("Error • Step 1: user-service POST /users", t.toString());
                Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(Status.BROKEN));
            }
            Allure.getLifecycle().stopStep(__stepId1);
        } else {
            String __skipId1 = java.util.UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(__skipId1, new io.qameta.allure.model.StepResult().setName("Step 1: user-service POST /users").setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep(__skipId1);
        }
        // Step 2: order-service POST /orders
        if (!scenarioFailed) {
            String __stepId2 = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr2 = new io.qameta.allure.model.StepResult().setName("Step 2: order-service POST /orders");
            Allure.getLifecycle().startStep(__stepId2, __sr2);
            try {
                RequestSpecification req2 = RestAssured.given()
                    .header("Authorization", jwtType + " " + jwt);
                req2 = req2.log().all();
                req2 = req2.filter(new AllureRestAssured());
                Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
            } catch (Throwable t) {
                scenarioFailed = true;
                Allure.addAttachment("Error • Step 2: order-service POST /orders", t.toString());
                Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(Status.BROKEN));
            }
            Allure.getLifecycle().stopStep(__stepId2);
        } else {
            String __skipId2 = java.util.UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(__skipId2, new io.qameta.allure.model.StepResult().setName("Step 2: order-service POST /orders").setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep(__skipId2);
        }
        // Step 3: payment-service POST /payments
        if (!scenarioFailed) {
            String __stepId3 = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr3 = new io.qameta.allure.model.StepResult().setName("Step 3: payment-service POST /payments");
            Allure.getLifecycle().startStep(__stepId3, __sr3);
            try {
                RequestSpecification req3 = RestAssured.given()
                    .header("Authorization", jwtType + " " + jwt);
                req3 = req3.log().all();
                req3 = req3.filter(new AllureRestAssured());
                Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(200).extract().response();
            } catch (Throwable t) {
                scenarioFailed = true;
                Allure.addAttachment("Error • Step 3: payment-service POST /payments", t.toString());
                Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(Status.BROKEN));
            }
            Allure.getLifecycle().stopStep(__stepId3);
        } else {
            String __skipId3 = java.util.UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(__skipId3, new io.qameta.allure.model.StepResult().setName("Step 3: payment-service POST /payments").setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep(__skipId3);
        }
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

    @Test
    public void testScenario2() throws Exception {
            // 1) perform login and capture JWT
            Response loginRes = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"YOUR_USERNAME\",\"password\":\"YOUR_PASSWORD\"}")
            .when().post("/login").then().log().ifValidationFails()
                .statusCode(200)
                .extract().response();
            String jwt     = loginRes.jsonPath().getString("id");
            String jwtType = loginRes.jsonPath().getString("type");

        boolean scenarioFailed = false;
        StringBuilder failureLog = new StringBuilder();

        // Step 1: user-service POST /users
        if (!scenarioFailed) {
            String __stepId1 = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr1 = new io.qameta.allure.model.StepResult().setName("Step 1: user-service POST /users");
            Allure.getLifecycle().startStep(__stepId1, __sr1);
            try {
                RequestSpecification req1 = RestAssured.given()
                    .header("Authorization", jwtType + " " + jwt);
                req1 = req1.log().all();
                req1 = req1.filter(new AllureRestAssured());
                Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
            } catch (Throwable t) {
                scenarioFailed = true;
                Allure.addAttachment("Error • Step 1: user-service POST /users", t.toString());
                Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(Status.BROKEN));
            }
            Allure.getLifecycle().stopStep(__stepId1);
        } else {
            String __skipId1 = java.util.UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(__skipId1, new io.qameta.allure.model.StepResult().setName("Step 1: user-service POST /users").setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep(__skipId1);
        }
        // Step 2: order-service POST /orders
        if (!scenarioFailed) {
            String __stepId2 = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr2 = new io.qameta.allure.model.StepResult().setName("Step 2: order-service POST /orders");
            Allure.getLifecycle().startStep(__stepId2, __sr2);
            try {
                RequestSpecification req2 = RestAssured.given()
                    .header("Authorization", jwtType + " " + jwt);
                req2 = req2.log().all();
                req2 = req2.filter(new AllureRestAssured());
                Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
            } catch (Throwable t) {
                scenarioFailed = true;
                Allure.addAttachment("Error • Step 2: order-service POST /orders", t.toString());
                Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(Status.BROKEN));
            }
            Allure.getLifecycle().stopStep(__stepId2);
        } else {
            String __skipId2 = java.util.UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(__skipId2, new io.qameta.allure.model.StepResult().setName("Step 2: order-service POST /orders").setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep(__skipId2);
        }
        // Step 3: payment-service POST /payments
        if (!scenarioFailed) {
            String __stepId3 = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr3 = new io.qameta.allure.model.StepResult().setName("Step 3: payment-service POST /payments");
            Allure.getLifecycle().startStep(__stepId3, __sr3);
            try {
                RequestSpecification req3 = RestAssured.given()
                    .header("Authorization", jwtType + " " + jwt);
                req3 = req3.log().all();
                req3 = req3.filter(new AllureRestAssured());
                Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(402).extract().response();
            } catch (Throwable t) {
                scenarioFailed = true;
                Allure.addAttachment("Error • Step 3: payment-service POST /payments", t.toString());
                Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(Status.BROKEN));
            }
            Allure.getLifecycle().stopStep(__stepId3);
        } else {
            String __skipId3 = java.util.UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(__skipId3, new io.qameta.allure.model.StepResult().setName("Step 3: payment-service POST /payments").setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep(__skipId3);
        }
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

}
