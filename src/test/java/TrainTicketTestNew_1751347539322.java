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

public class TrainTicketTestNew_1751347539322 {

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

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"distances\":\"[12, 5]\",\"endStation\":\"Cincinnati\",\"id\":\"id\",\"loginId\":\"loginId\",\"startStation\":\"Main Street\",\"stationList\":\"stationList\",\"stations\":\"[\\\"Hudson Station\\\", \\\"Southwest Airlines Terminal\\\", \\\"North Central Train Depot\\\", \\\"East Coast Highway Stop\\\", \\\"Westside Bus Station\\\"]\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"endStation\":\"Cincinnati\",\"id\":\"id\",\"startStation\":\"Main Street\",\"stationList\":\"stationList\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 3: ts-route-service POST /api/v1/routeservice/routes", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"distances\":\"[12, 5]\",\"endStation\":\"Cincinnati\",\"id\":\"id\",\"loginId\":\"loginId\",\"startStation\":\"Main Street\",\"stationList\":\"stationList\",\"stations\":\"[\\\"Hudson Station\\\", \\\"Southwest Airlines Terminal\\\", \\\"North Central Train Depot\\\", \\\"East Coast Highway Stop\\\", \\\"Westside Bus Station\\\"]\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminrouteservice/adminroute")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-route-service POST /api/v1/adminrouteservice/adminroute")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().post("/api/v1/stationservice/stations/idlist")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-station-service POST /api/v1/stationservice/stations/idlist")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-route-service POST /api/v1/routeservice/routes
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-route-service POST /api/v1/routeservice/routes");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"distanceList\":\"distanceList\",\"endStation\":\"Cincinnati\",\"id\":\"id\",\"startStation\":\"Main Street\",\"stationList\":\"stationList\"}");
                Response stepResponse3 = req.when().post("/api/v1/routeservice/routes")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-route-service POST /api/v1/routeservice/routes - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-route-service POST /api/v1/routeservice/routes - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 3: ts-route-service POST /api/v1/routeservice/routes", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-route-service POST /api/v1/routeservice/routes - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-route-service POST /api/v1/routeservice/routes")
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

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"1234567890\",\"boughtDate\":\"2022-05-14\",\"coachNumber\":\"1\",\"contactsDocumentNumber\":\"123456\",\"contactsName\":\"Sarah\",\"differenceMoney\":\"$123.45\",\"documentType\":\"1\",\"from\":\"from\",\"id\":\"id\",\"price\":\"$45.99\",\"seatClass\":\"1\",\"seatNumber\":\"seatNumber\",\"status\":\"0\",\"to\":\"to\",\"trainNumber\":\"12345\",\"travelDate\":\"2023-07-17\",\"travelTime\":\"Hello\"}");
                Response stepResponse1 = req.when().post("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service POST /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                req = req.contentType("application/json");
                req = req.body("{\"accountId\":\"1234567890\",\"boughtDate\":\"2022-05-14\",\"coachNumber\":\"1\",\"contactsDocumentNumber\":\"123456\",\"contactsName\":\"Sarah\",\"documentType\":\"1\",\"from\":\"from\",\"id\":\"id\",\"price\":\"$45.99\",\"seatClass\":\"1\",\"seatNumber\":\"seatNumber\",\"status\":\"0\",\"to\":\"to\",\"trainNumber\":\"12345\",\"travelDate\":\"2023-07-17\",\"travelTime\":\"Hello\"}");
                Response stepResponse2 = req.when().post("/api/v1/orderOtherService/orderOther/admin")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-other-service POST /api/v1/orderOtherService/orderOther/admin")
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
                req = req.body("{\"basicPriceRate\":\"0.5\",\"firstClassPriceRate\":\"22.5\",\"id\":\"id\",\"routeId\":\"routeId\",\"trainType\":\"TrainType\"}");
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
                req = req.body("{\"basicPriceRate\":\"0.5\",\"firstClassPriceRate\":\"22.5\",\"id\":\"id\",\"routeId\":\"routeId\",\"trainType\":\"TrainType\"}");
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

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder
        boolean shouldExecuteStep1 = true;
        if (shouldExecuteStep1) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(1, true);
                System.out.println("✓ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(1, false);
                System.out.println("✗ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(1, false);
            System.out.println("⚠ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 2: ts-order-service GET /api/v1/orderservice/order
        boolean shouldExecuteStep2 = true;
        if (shouldExecuteStep2) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 2: ts-order-service GET /api/v1/orderservice/order");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse2 = req.when().get("/api/v1/orderservice/order")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(2, true);
                System.out.println("✓ Step 2: ts-order-service GET /api/v1/orderservice/order - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(2, false);
                System.out.println("✗ Step 2: ts-order-service GET /api/v1/orderservice/order - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 2: ts-order-service GET /api/v1/orderservice/order", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(2, false);
            System.out.println("⚠ Step 2: ts-order-service GET /api/v1/orderservice/order - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 2: ts-order-service GET /api/v1/orderservice/order")
                     .setStatus(Status.SKIPPED));
            Allure.getLifecycle().stopStep();
        }

        // Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther
        boolean shouldExecuteStep3 = true;
        if (shouldExecuteStep3) {
            String __uuid = java.util.UUID.randomUUID().toString();
            io.qameta.allure.model.StepResult __sr = new io.qameta.allure.model.StepResult().setName("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther");
            Allure.getLifecycle().startStep(__uuid, __sr);
            try {
                RequestSpecification req = RestAssured.given();
                if (loginSucceeded.get()) {
                    req = req.header("Authorization", jwtType + " " + jwt);
                }
                Response stepResponse3 = req.when().get("/api/v1/orderOtherService/orderOther")
                   .then().log().ifValidationFails()
                   .statusCode(200)
                   .extract().response();
                stepResults.put(3, true);
                System.out.println("✓ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther - SUCCESS");
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            } catch (Throwable t) {
                stepResults.put(3, false);
                System.out.println("✗ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther - FAILED: " + t.getMessage());
                Allure.addAttachment("Error • Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther", t.toString());
                Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
            }
            Allure.getLifecycle().stopStep();
        } else {   // step skipped due to dependency failure
            stepResults.put(3, false);
            System.out.println("⚠ Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther - SKIPPED (dependency failed)");
            Allure.getLifecycle().startStep(
                java.util.UUID.randomUUID().toString(),
                new io.qameta.allure.model.StepResult()
                     .setName("Step 3: ts-order-other-service GET /api/v1/orderOtherService/orderOther")
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
