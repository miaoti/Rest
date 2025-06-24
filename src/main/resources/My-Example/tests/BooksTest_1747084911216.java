package books;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooksTest_1747084911216 {

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
            Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            scenarioFailed = true;
            failureLog.append("\u2022 Step 1: user-service POST /users → ")                     // NEW
                      .append(t.getMessage()).append("\n");
            System.err.println("FAILED Step 1: user-service POST /users → " + t);
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 1: user-service POST /users", t.toString());
                io.qameta.allure.Allure.getLifecycle()                                      // NEW
                        .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
            // keep executing the remaining steps
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

        try {
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given();
            Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            scenarioFailed = true;
            failureLog.append("\u2022 Step 2: order-service POST /orders → ")                     // NEW
                      .append(t.getMessage()).append("\n");
            System.err.println("FAILED Step 2: order-service POST /orders → " + t);
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 2: order-service POST /orders", t.toString());
                io.qameta.allure.Allure.getLifecycle()                                      // NEW
                        .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
            // keep executing the remaining steps
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

        try {
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given();
            Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(200).extract().response();
        } catch (Throwable t) {
            scenarioFailed = true;
            failureLog.append("\u2022 Step 3: payment-service POST /payments → ")                     // NEW
                      .append(t.getMessage()).append("\n");
            System.err.println("FAILED Step 3: payment-service POST /payments → " + t);
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 3: payment-service POST /payments", t.toString());
                io.qameta.allure.Allure.getLifecycle()                                      // NEW
                        .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
            // keep executing the remaining steps
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

    }

    @Test
    public void testScenario2() throws Exception {
        boolean scenarioFailed = false;
        StringBuilder failureLog = new StringBuilder();
        try {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given();
            Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            scenarioFailed = true;
            failureLog.append("\u2022 Step 1: user-service POST /users → ")                     // NEW
                      .append(t.getMessage()).append("\n");
            System.err.println("FAILED Step 1: user-service POST /users → " + t);
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 1: user-service POST /users", t.toString());
                io.qameta.allure.Allure.getLifecycle()                                      // NEW
                        .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
            // keep executing the remaining steps
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

        try {
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given();
            Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            scenarioFailed = true;
            failureLog.append("\u2022 Step 2: order-service POST /orders → ")                     // NEW
                      .append(t.getMessage()).append("\n");
            System.err.println("FAILED Step 2: order-service POST /orders → " + t);
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 2: order-service POST /orders", t.toString());
                io.qameta.allure.Allure.getLifecycle()                                      // NEW
                        .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
            // keep executing the remaining steps
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

        try {
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given();
            Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(402).extract().response();
        } catch (Throwable t) {
            scenarioFailed = true;
            failureLog.append("\u2022 Step 3: payment-service POST /payments → ")                     // NEW
                      .append(t.getMessage()).append("\n");
            System.err.println("FAILED Step 3: payment-service POST /payments → " + t);
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 3: payment-service POST /payments", t.toString());
                io.qameta.allure.Allure.getLifecycle()                                      // NEW
                        .updateStep(s -> s.setStatus(io.qameta.allure.model.Status.BROKEN));
            }
            // keep executing the remaining steps
        if (scenarioFailed) {
            fail("Scenario finished with failed steps:\n" + failureLog.toString());
        }
    }

    }

}
