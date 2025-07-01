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

public class TrainTicketTest_1751343396337 {

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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"distances\":\"[1.0, 2.5, 8.3]\",\"endStation\":\"Main Street\",\"id\":\"id\",\"loginId\":\"loginId\",\"startStation\":\"New York\",\"stationList\":\"stationList\",\"stations\":\"station1\"}");
                req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"endStation\":\"Main Street\",\"id\":\"id\",\"startStation\":\"New York\",\"stationList\":\"stationList\"}");
                req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 3: ts-route-service POST /api/v1/routeservice/routes", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed.get()) {
            fail("Scenario finished with one or more failed steps");
        }
    }

    @Test
    public void testScenario2() throws Exception {
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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"distances\":\"[1.0, 2.5, 8.3]\",\"endStation\":\"Main Street\",\"id\":\"id\",\"loginId\":\"loginId\",\"startStation\":\"New York\",\"stationList\":\"stationList\",\"stations\":\"station1\"}");
                req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"endStation\":\"Main Street\",\"id\":\"id\",\"startStation\":\"New York\",\"stationList\":\"stationList\"}");
                req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 3: ts-route-service POST /api/v1/routeservice/routes", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed.get()) {
            fail("Scenario finished with one or more failed steps");
        }
    }

    @Test
    public void testScenario3() throws Exception {
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

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"tW9yG45Z\",\"boughtDate\":\"2023-07-14\",\"coachNumber\":\"13\",\"contactsDocumentNumber\":\"contactsDocumentNumber\",\"contactsName\":\"Alice\",\"differenceMoney\":\"1,200\",\"documentType\":\"1\",\"from\":\"from\",\"id\":\"id\",\"price\":\"$123.45\",\"seatClass\":\"1\",\"seatNumber\":\"Seat1\",\"status\":\"0\",\"to\":\"to\",\"trainNumber\":\"1234\",\"travelDate\":\"2023-07-15\",\"travelTime\":\"three hours\"}");
                req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"tW9yG45Z\",\"boughtDate\":\"2023-07-14\",\"coachNumber\":\"13\",\"contactsDocumentNumber\":\"contactsDocumentNumber\",\"contactsName\":\"Alice\",\"documentType\":\"1\",\"from\":\"from\",\"id\":\"id\",\"price\":\"$123.45\",\"seatClass\":\"1\",\"seatNumber\":\"Seat1\",\"status\":\"0\",\"to\":\"to\",\"trainNumber\":\"1234\",\"travelDate\":\"2023-07-15\",\"travelTime\":\"three hours\"}");
                req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed.get()) {
            fail("Scenario finished with one or more failed steps");
        }
    }

    @Test
    public void testScenario4() throws Exception {
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
                req = req.body("{\"basicPriceRate\":\"0.5\",\"firstClassPriceRate\":\"1.23\",\"id\":\"id\",\"routeId\":\"routeId\",\"trainType\":\"Locomotive\"}");
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
                req = req.body("{\"basicPriceRate\":\"0.5\",\"firstClassPriceRate\":\"1.23\",\"id\":\"id\",\"routeId\":\"routeId\",\"trainType\":\"Locomotive\"}");
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

    @Test
    public void testScenario5() throws Exception {
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

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req.when().get("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-service GET /api/v1/orderservice/order");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req.when().get("/api/v1/orderservice/order")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 2: ts-order-service GET /api/v1/orderservice/order", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-service GET /api/v1/orderservice/order")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther
        if (!scenarioFailed.get()) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req.when().get("/api/v1/orderOtherService/orderOther")
                   .then().log().ifValidationFails()
                   .statusCode(200);
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                scenarioFailed.set(true);
                Allure.addAttachment("Error • Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // scenario already broken – mark as SKIPPED
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        if (scenarioFailed.get()) {
            fail("Scenario finished with one or more failed steps");
        }
    }

}
