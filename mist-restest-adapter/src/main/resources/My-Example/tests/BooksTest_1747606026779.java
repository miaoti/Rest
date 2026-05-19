package books;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.model.Status;

public class BooksTest_1747606026779 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void testScenario1() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        boolean scenarioFailed = false;
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"YOUR_USERNAME\",\"password\":\"YOUR_PASSWORD\"}")
                .when().post("/login").then().log().ifValidationFails()
                    .statusCode(200)
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("id");
                _jwtType[0] = loginRes.jsonPath().getString("type");
            });
        } catch (Throwable __t) {
            scenarioFailed = true;
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step 1: user-service POST /users
        if (!scenarioFailed) {
            Allure.step("Step 1: user-service POST /users", () -> {
                try {
                    RequestSpecification req = RestAssured.given()
                        .header("Authorization", jwtType + " " + jwt)
                        .log().all();
                    req = req.filter(new AllureRestAssured());
                    req.when().post("/users")
                       .then().log().ifValidationFails()
                       .statusCode(201);
                } catch (Throwable t) {
                    scenarioFailed = true;
                    Allure.addAttachment("Error • Step 1: user-service POST /users", t.toString());
                    throw t;
                }
            });
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: user-service POST /users")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: order-service POST /orders
        if (!scenarioFailed) {
            Allure.step("Step 2: order-service POST /orders", () -> {
                try {
                    RequestSpecification req = RestAssured.given()
                        .header("Authorization", jwtType + " " + jwt)
                        .log().all();
                    req = req.filter(new AllureRestAssured());
                    req.when().post("/orders")
                       .then().log().ifValidationFails()
                       .statusCode(201);
                } catch (Throwable t) {
                    scenarioFailed = true;
                    Allure.addAttachment("Error • Step 2: order-service POST /orders", t.toString());
                    throw t;
                }
            });
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: order-service POST /orders")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: payment-service POST /payments
        if (!scenarioFailed) {
            Allure.step("Step 3: payment-service POST /payments", () -> {
                try {
                    RequestSpecification req = RestAssured.given()
                        .header("Authorization", jwtType + " " + jwt)
                        .log().all();
                    req = req.filter(new AllureRestAssured());
                    req.when().post("/payments")
                       .then().log().ifValidationFails()
                       .statusCode(200);
                } catch (Throwable t) {
                    scenarioFailed = true;
                    Allure.addAttachment("Error • Step 3: payment-service POST /payments", t.toString());
                    throw t;
                }
            });
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: payment-service POST /payments")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed) {
            fail("Scenario finished with one or more failed steps");
        }
    }

    @Test
    public void testScenario2() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        boolean scenarioFailed = false;
        try {
            Allure.step("Login", () -> {
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"YOUR_USERNAME\",\"password\":\"YOUR_PASSWORD\"}")
                .when().post("/login").then().log().ifValidationFails()
                    .statusCode(200)
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("id");
                _jwtType[0] = loginRes.jsonPath().getString("type");
            });
        } catch (Throwable __t) {
            scenarioFailed = true;
        }
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step 1: user-service POST /users
        if (!scenarioFailed) {
            Allure.step("Step 1: user-service POST /users", () -> {
                try {
                    RequestSpecification req = RestAssured.given()
                        .header("Authorization", jwtType + " " + jwt)
                        .log().all();
                    req = req.filter(new AllureRestAssured());
                    req.when().post("/users")
                       .then().log().ifValidationFails()
                       .statusCode(201);
                } catch (Throwable t) {
                    scenarioFailed = true;
                    Allure.addAttachment("Error • Step 1: user-service POST /users", t.toString());
                    throw t;
                }
            });
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: user-service POST /users")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: order-service POST /orders
        if (!scenarioFailed) {
            Allure.step("Step 2: order-service POST /orders", () -> {
                try {
                    RequestSpecification req = RestAssured.given()
                        .header("Authorization", jwtType + " " + jwt)
                        .log().all();
                    req = req.filter(new AllureRestAssured());
                    req.when().post("/orders")
                       .then().log().ifValidationFails()
                       .statusCode(201);
                } catch (Throwable t) {
                    scenarioFailed = true;
                    Allure.addAttachment("Error • Step 2: order-service POST /orders", t.toString());
                    throw t;
                }
            });
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: order-service POST /orders")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: payment-service POST /payments
        if (!scenarioFailed) {
            Allure.step("Step 3: payment-service POST /payments", () -> {
                try {
                    RequestSpecification req = RestAssured.given()
                        .header("Authorization", jwtType + " " + jwt)
                        .log().all();
                    req = req.filter(new AllureRestAssured());
                    req.when().post("/payments")
                       .then().log().ifValidationFails()
                       .statusCode(402);
                } catch (Throwable t) {
                    scenarioFailed = true;
                    Allure.addAttachment("Error • Step 3: payment-service POST /payments", t.toString());
                    throw t;
                }
            });
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: payment-service POST /payments")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed) {
            fail("Scenario finished with one or more failed steps");
        }
    }

}
