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

public class TrainTicketTest_1750977259981 {

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

        // Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"id\":\"1\",\"trainType\":\"InterCity Express\",\"routeId\":\"Route-12-A-B-C-D-E-F-G-H-I-J-K-L-M-N-O-P-Q-R-S-T-U-V-W-X-Y-Z\",\"basicPriceRate\":5.99,\"firstClassPriceRate\":9.99}");
                req.when().post("/api/v1/adminbasicservice/adminbasic/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-basic-info-service POST /api/v1/adminbasicservice/adminbasic/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-price-service POST /api/v1/priceservice/prices
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-price-service POST /api/v1/priceservice/prices");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"id\":\"1\",\"trainType\":\"InterCity Express\",\"routeId\":\"Route-12-A-B-C-D-E-F-G-H-I-J-K-L-M-N-O-P-Q-R-S-T-U-V-W-X-Y-Z\",\"basicPriceRate\":5.99,\"firstClassPriceRate\":9.99}");
                req.when().post("/api/v1/priceservice/prices")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 2: ts-price-service POST /api/v1/priceservice/prices", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-price-service POST /api/v1/priceservice/prices")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed.get()) {
            fail("Scenario finished with one or more failed steps");
        }
    }

}
