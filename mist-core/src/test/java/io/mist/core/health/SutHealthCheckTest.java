package io.mist.core.health;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.mist.core.health.SutHealthCheck.Endpoint;
import io.mist.core.health.SutHealthCheck.Probe;
import io.mist.core.health.SutHealthCheck.Report;
import io.mist.core.health.SutHealthCheck.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SutHealthCheck}. Uses stub probes so the suite has
 * zero network dependency — preserves the byte-identical idempotent test
 * contract that lets these run inside CI without a live SUT.
 */
public class SutHealthCheckTest {

    private static final Endpoint GET_HEALTH    = new Endpoint("GET",    "http://sut/api/health");
    private static final Endpoint GET_BROKEN    = new Endpoint("GET",    "http://sut/api/admintravel");
    private static final Endpoint POST_BACKUP   = new Endpoint("POST",   "http://sut/api/backup");
    private static final Endpoint DELETE_GONE   = new Endpoint("DELETE", "http://sut/api/admintravel/{tripId}");

    /** Stub probe driven by a verb+url → statusCode lookup table. */
    private static Probe stubProbe(Map<String, Integer> table) {
        return endpoint -> {
            Integer status = table.get(endpoint.verb + " " + endpoint.url);
            if (status == null) status = 0;
            return new Result(endpoint, status, /*latencyMs*/ 1L, /*errorMessage*/ null);
        };
    }

    // ── Result.healthy() boundary cases ────────────────────────────────────

    @Test
    public void result_status200_isHealthy() {
        assertTrue(new Result(GET_HEALTH, 200, 1, null).healthy());
    }

    @Test
    public void result_status404_isHealthy_serviceUpJustNotFound() {
        // 404 means the service is reachable; only 5xx implies the SUT is broken
        // in a way that will silently drop scenarios downstream.
        assertTrue("404 must be healthy — service is up", new Result(GET_HEALTH, 404, 1, null).healthy());
    }

    @Test
    public void result_status403_isHealthy_authIsSeparateFromReachability() {
        // Auth-rejected is a separate concern from health. The preflight only
        // certifies "the network path works"; auth is handled by MstAuthHandler.
        assertTrue(new Result(GET_HEALTH, 403, 1, null).healthy());
    }

    @Test
    public void result_status500_isUnhealthy_thisIsTheRun13RegressionShape() {
        // This is exactly the Run 13 shape: GET /admintravelservice/admintravel → 500.
        // Preflight MUST flag this as unhealthy so the operator sees the regression
        // at startup, not as a missing-fault detection 5 hours later.
        assertFalse(new Result(GET_BROKEN, 500, 1, null).healthy());
    }

    @Test
    public void result_status503_isUnhealthy() {
        assertFalse(new Result(GET_BROKEN, 503, 1, null).healthy());
    }

    @Test
    public void result_transportError_isUnhealthy() {
        // statusCode == -1 = no HTTP response at all (timeout / connect-refused).
        assertFalse(new Result(GET_BROKEN, -1, 1, "ConnectException: connection refused").healthy());
    }

    // ── check() iteration + Report aggregation ─────────────────────────────

    @Test
    public void check_emptyList_returnsEmptyReport() {
        Report r = SutHealthCheck.check(Collections.emptyList(), stubProbe(new HashMap<>()));
        assertEquals(0, r.totalCount());
        assertTrue("empty list trivially all-healthy", r.allHealthy());
    }

    @Test
    public void check_invokesProbeForEachEndpoint() {
        Map<String, Integer> table = new HashMap<>();
        table.put("GET http://sut/api/health",    200);
        table.put("GET http://sut/api/admintravel",  500);
        table.put("POST http://sut/api/backup",     201);

        Report r = SutHealthCheck.check(
                Arrays.asList(GET_HEALTH, GET_BROKEN, POST_BACKUP),
                stubProbe(table));

        assertEquals(3, r.totalCount());
        assertEquals(2, r.healthyCount());
        assertEquals(1, r.unhealthyCount());
        assertFalse(r.allHealthy());
    }

    @Test
    public void check_preservesEndpointOrder() {
        List<Endpoint> endpoints = Arrays.asList(POST_BACKUP, GET_HEALTH, DELETE_GONE);
        Map<String, Integer> table = new HashMap<>();
        table.put("POST http://sut/api/backup", 201);
        table.put("GET http://sut/api/health",  200);
        table.put("DELETE http://sut/api/admintravel/{tripId}", 404);

        Report r = SutHealthCheck.check(endpoints, stubProbe(table));
        assertEquals("POST",   r.results.get(0).endpoint.verb);
        assertEquals("GET",    r.results.get(1).endpoint.verb);
        assertEquals("DELETE", r.results.get(2).endpoint.verb);
    }

    @Test
    public void report_unhealthy_returnsOnlyFailingResults() {
        Map<String, Integer> table = new HashMap<>();
        table.put("GET http://sut/api/health",        200);
        table.put("GET http://sut/api/admintravel",   500);
        table.put("DELETE http://sut/api/admintravel/{tripId}", 503);

        Report r = SutHealthCheck.check(
                Arrays.asList(GET_HEALTH, GET_BROKEN, DELETE_GONE),
                stubProbe(table));

        List<Result> bad = r.unhealthy();
        assertEquals(2, bad.size());
        // Order preserved — same as input.
        assertEquals(500, bad.get(0).statusCode);
        assertEquals(503, bad.get(1).statusCode);
    }

    @Test
    public void report_summary_humanReadable() {
        Map<String, Integer> table = new HashMap<>();
        table.put("GET http://sut/api/health",      200);
        table.put("GET http://sut/api/admintravel", 500);

        Report r = SutHealthCheck.check(Arrays.asList(GET_HEALTH, GET_BROKEN), stubProbe(table));
        String s = r.summary();
        assertTrue("summary mentions reachable count: " + s, s.contains("1/2 endpoints reachable"));
        assertTrue("summary mentions unhealthy count: " + s, s.contains("1 unhealthy"));
    }

    // ── Run 13 scenario: real failure pattern reproduced via stub ──────────

    @Test
    public void run13RegressionShape_flagsAdmintravel500() {
        // Reproduces the exact endpoint mix from the Run 13 failure: most root
        // APIs healthy, but admintravel returns 500. The preflight Report must
        // call this out at startup so the operator catches the regression
        // without log archaeology.
        Map<String, Integer> table = new HashMap<>();
        table.put("GET http://sut/api/v1/adminbasicservice/adminbasic/stations", 200);
        table.put("GET http://sut/api/v1/userservice/users",                     200);
        table.put("GET http://sut/api/v1/admintravelservice/admintravel",        500);
        table.put("POST http://sut/api/v1/adminbasicservice/adminbasic/prices",  200);

        Report r = SutHealthCheck.check(Arrays.asList(
                new Endpoint("GET",  "http://sut/api/v1/adminbasicservice/adminbasic/stations"),
                new Endpoint("GET",  "http://sut/api/v1/userservice/users"),
                new Endpoint("GET",  "http://sut/api/v1/admintravelservice/admintravel"),
                new Endpoint("POST", "http://sut/api/v1/adminbasicservice/adminbasic/prices")),
                stubProbe(table));

        assertFalse(r.allHealthy());
        assertEquals(1, r.unhealthyCount());
        Result bad = r.unhealthy().get(0);
        assertEquals("the regression endpoint is admintravel, exactly as Run 13",
                "http://sut/api/v1/admintravelservice/admintravel", bad.endpoint.url);
        assertEquals(500, bad.statusCode);
    }
}
