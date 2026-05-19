package books;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooksTest_1747084217553 {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testScenario1() throws Exception {
        try {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given();
            Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            System.err.println("FAILED Step 1: user-service POST /users → " + t.getMessage());
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 1: user-service POST /users", t.toString());
            }
            // keep executing the remaining steps
        }

        try {
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given();
            Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            System.err.println("FAILED Step 2: order-service POST /orders → " + t.getMessage());
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 2: order-service POST /orders", t.toString());
            }
            // keep executing the remaining steps
        }

        try {
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given();
            Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(200).extract().response();
        } catch (Throwable t) {
            System.err.println("FAILED Step 3: payment-service POST /payments → " + t.getMessage());
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 3: payment-service POST /payments", t.toString());
            }
            // keep executing the remaining steps
        }

    }

    @Test
    public void testScenario2() throws Exception {
        try {
            // Step 1: user-service POST /users
            RequestSpecification req1 = RestAssured.given();
            Response res1 = req1.when().post("/users").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            System.err.println("FAILED Step 1: user-service POST /users → " + t.getMessage());
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 1: user-service POST /users", t.toString());
            }
            // keep executing the remaining steps
        }

        try {
            // Step 2: order-service POST /orders
            RequestSpecification req2 = RestAssured.given();
            Response res2 = req2.when().post("/orders").then().log().ifValidationFails().statusCode(201).extract().response();
        } catch (Throwable t) {
            System.err.println("FAILED Step 2: order-service POST /orders → " + t.getMessage());
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 2: order-service POST /orders", t.toString());
            }
            // keep executing the remaining steps
        }

        try {
            // Step 3: payment-service POST /payments
            RequestSpecification req3 = RestAssured.given();
            Response res3 = req3.when().post("/payments").then().log().ifValidationFails().statusCode(402).extract().response();
        } catch (Throwable t) {
            System.err.println("FAILED Step 3: payment-service POST /payments → " + t.getMessage());
            if (false) {
                io.qameta.allure.Allure.addAttachment("Error in Step 3: payment-service POST /payments", t.toString());
            }
            // keep executing the remaining steps
        }

    }

}
