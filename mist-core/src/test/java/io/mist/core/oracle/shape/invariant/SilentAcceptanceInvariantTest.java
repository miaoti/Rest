package io.mist.core.oracle.shape.invariant;

import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeVerdict;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Pins SilentAcceptanceInvariant: fires (WARN) when a negative test's trace
 * shows full success (no rejection, root 2xx); no-ops on positive tests
 * (null target, D2 guard); stays silent when the SUT rejected anywhere.
 */
public class SilentAcceptanceInvariantTest {

    private static TraceModel.Span span(String id, String parent, String svc, String op, int http, String otel) {
        return new TraceModel.Span(id, parent, svc, op, http, otel, 0L, new HashMap<>());
    }

    private static TraceModel allSuccess() {
        return new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-admin-route", "AdminRouteController.addRoute", 200, null)));
    }

    @Test
    public void fires_whenNegativeTestSilentlyAccepted() {
        SilentAcceptanceInvariant inv = new SilentAcceptanceInvariant("POST /x", "ts-admin-route", "stationList");
        TraceShapeVerdict.InvariantOutcome o = inv.evaluate(allSuccess());
        assertFalse("silent acceptance must fire", o.passed);
        assertEquals("SILENT_ACCEPTANCE", o.kind);
        assertEquals(TraceShapeVerdict.Severity.WARN, o.severity);
    }

    @Test
    public void noop_onPositiveTest_nullTarget() {
        // D2: positive test (no target) must never fire, even on an all-success trace.
        SilentAcceptanceInvariant inv = new SilentAcceptanceInvariant("POST /x", null, null);
        assertTrue(inv.evaluate(allSuccess()).passed);
    }

    @Test
    public void passes_whenSutRejected() {
        SilentAcceptanceInvariant inv = new SilentAcceptanceInvariant("POST /x", "ts-admin-route", "stationList");
        TraceModel t = new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 400, "ERROR"),
                span("child", "root", "ts-admin-route", "AdminRouteController.addRoute", 400, null)));
        assertTrue("SUT rejected → not silent acceptance", inv.evaluate(t).passed);
    }

    @Test
    public void passes_whenNoSuccessRoot() {
        SilentAcceptanceInvariant inv = new SilentAcceptanceInvariant("POST /x", "ts-admin-route", "stationList");
        TraceModel t = new TraceModel("t", Collections.singletonList(
                span("root", null, "ts-gateway", "POST /x", 0, null)));
        assertTrue(inv.evaluate(t).passed);
    }

    @Test
    public void passes_whenEmptyOrNull() {
        SilentAcceptanceInvariant inv = new SilentAcceptanceInvariant("POST /x", "ts-admin-route", "stationList");
        assertTrue(inv.evaluate(null).passed);
        assertTrue(inv.evaluate(new TraceModel("t", Collections.emptyList())).passed);
    }
}
