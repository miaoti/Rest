package books;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.Allure;
import static org.junit.Assert.*;

public class BooksTest_1747604105229 {

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

        try {
            io.qameta.allure.Allure.step("Step 1: user-service POST /users", () -> {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given()
                .header("Authorization", jwtType + " " + jwt);
            String stepInfo1 = "SERVICE : user-service\n" + "VERB    : POST\n" + "PATH    : /users\n" + "EXPECT  : 201";
            io.qameta.allure.Allure.addAttachment("Step 1: user-service POST /users", stepInfo1);
            req1 = req1.log().all();
            req1 = req1.filter(new AllureRestAssured());
                Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
            });
        } catch (Throwable t) {
            scenarioFailed = true;
            /* your failureLog code… */
            if (true) {
                io.qameta.allure.Allure.addAttachment("Error • Step 1: user-service POST /users", t.toString());
                io.qameta.allure.Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
        }

        try {
            io.qameta.allure.Allure.step("Step 2: order-service POST /orders", () -> {
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given()
                .header("Authorization", jwtType + " " + jwt);
            String stepInfo2 = "SERVICE : order-service\n" + "VERB    : POST\n" + "PATH    : /orders\n" + "EXPECT  : 201";
            io.qameta.allure.Allure.addAttachment("Step 2: order-service POST /orders", stepInfo2);
            req2 = req2.log().all();
            req2 = req2.filter(new AllureRestAssured());
                Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
            });
        } catch (Throwable t) {
            scenarioFailed = true;
            /* your failureLog code… */
            if (true) {
                io.qameta.allure.Allure.addAttachment("Error • Step 2: order-service POST /orders", t.toString());
                io.qameta.allure.Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
        }

        try {
            io.qameta.allure.Allure.step("Step 3: payment-service POST /payments", () -> {
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given()
                .header("Authorization", jwtType + " " + jwt);
            String stepInfo3 = "SERVICE : payment-service\n" + "VERB    : POST\n" + "PATH    : /payments\n" + "EXPECT  : 200";
            io.qameta.allure.Allure.addAttachment("Step 3: payment-service POST /payments", stepInfo3);
            req3 = req3.log().all();
            req3 = req3.filter(new AllureRestAssured());
                Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(200).extract().response();
            });
        } catch (Throwable t) {
            scenarioFailed = true;
            /* your failureLog code… */
            if (true) {
                io.qameta.allure.Allure.addAttachment("Error • Step 3: payment-service POST /payments", t.toString());
                io.qameta.allure.Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
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

        try {
            io.qameta.allure.Allure.step("Step 1: user-service POST /users", () -> {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given()
                .header("Authorization", jwtType + " " + jwt);
            String stepInfo1 = "SERVICE : user-service\n" + "VERB    : POST\n" + "PATH    : /users\n" + "EXPECT  : 201";
            io.qameta.allure.Allure.addAttachment("Step 1: user-service POST /users", stepInfo1);
            req1 = req1.log().all();
            req1 = req1.filter(new AllureRestAssured());
                Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
            });
        } catch (Throwable t) {
            scenarioFailed = true;
            /* your failureLog code… */
            if (true) {
                io.qameta.allure.Allure.addAttachment("Error • Step 1: user-service POST /users", t.toString());
                io.qameta.allure.Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
        }

        try {
            io.qameta.allure.Allure.step("Step 2: order-service POST /orders", () -> {
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given()
                .header("Authorization", jwtType + " " + jwt);
            String stepInfo2 = "SERVICE : order-service\n" + "VERB    : POST\n" + "PATH    : /orders\n" + "EXPECT  : 201";
            io.qameta.allure.Allure.addAttachment("Step 2: order-service POST /orders", stepInfo2);
            req2 = req2.log().all();
            req2 = req2.filter(new AllureRestAssured());
                Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
            });
        } catch (Throwable t) {
            scenarioFailed = true;
            /* your failureLog code… */
            if (true) {
                io.qameta.allure.Allure.addAttachment("Error • Step 2: order-service POST /orders", t.toString());
                io.qameta.allure.Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
        }

        try {
            io.qameta.allure.Allure.step("Step 3: payment-service POST /payments", () -> {
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given()
                .header("Authorization", jwtType + " " + jwt);
            String stepInfo3 = "SERVICE : payment-service\n" + "VERB    : POST\n" + "PATH    : /payments\n" + "EXPECT  : 402";
            io.qameta.allure.Allure.addAttachment("Step 3: payment-service POST /payments", stepInfo3);
            req3 = req3.log().all();
            req3 = req3.filter(new AllureRestAssured());
                Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(402).extract().response();
            });
        } catch (Throwable t) {
            scenarioFailed = true;
            /* your failureLog code… */
            if (true) {
                io.qameta.allure.Allure.addAttachment("Error • Step 3: payment-service POST /payments", t.toString());
                io.qameta.allure.Allure.getLifecycle()
                      .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
        }

        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

}
