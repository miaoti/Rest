package books;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooksTest_1747087509130 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testScenario1() throws Exception {
        boolean scenarioFailed = false;
        StringBuilder failureLog = new StringBuilder();
        try {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given();
            String stepInfo1 = "SERVICE : user-service\n" + "VERB    : POST\n" + "PATH    : /users\n" + "EXPECT  : 201";
            Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
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
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given();
            String stepInfo2 = "SERVICE : order-service\n" + "VERB    : POST\n" + "PATH    : /orders\n" + "EXPECT  : 201";
            Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
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
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given();
            String stepInfo3 = "SERVICE : payment-service\n" + "VERB    : POST\n" + "PATH    : /payments\n" + "EXPECT  : 200";
            Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(200).extract().response();
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
        boolean scenarioFailed = false;
        StringBuilder failureLog = new StringBuilder();
        try {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given();
            String stepInfo1 = "SERVICE : user-service\n" + "VERB    : POST\n" + "PATH    : /users\n" + "EXPECT  : 201";
            Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
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
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given();
            String stepInfo2 = "SERVICE : order-service\n" + "VERB    : POST\n" + "PATH    : /orders\n" + "EXPECT  : 201";
            Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
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
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given();
            String stepInfo3 = "SERVICE : payment-service\n" + "VERB    : POST\n" + "PATH    : /payments\n" + "EXPECT  : 402";
            Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(402).extract().response();
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
