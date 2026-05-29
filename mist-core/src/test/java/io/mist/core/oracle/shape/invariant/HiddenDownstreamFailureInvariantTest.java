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
 * Pins HiddenDownstreamFailureInvariant: fires (ERROR) when the root returned
 * 2xx but a descendant span server-errored (http>=500 or otel=ERROR), and
 * stays silent on healthy traces, loud failures, and benign downstream 4xx.
 */
public class HiddenDownstreamFailureInvariantTest {

    private final HiddenDownstreamFailureInvariant inv =
            new HiddenDownstreamFailureInvariant("POST /api/v1/x");

    private static TraceModel.Span span(String id, String parent, String svc, String op, int http, String otel) {
        return new TraceModel.Span(id, parent, svc, op, http, otel, 0L, new HashMap<>());
    }

    @Test
    public void fires_whenRoot2xxButDescendant500() {
        TraceModel t = new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-order", "OrderController.create", 500, "ERROR")));
        TraceShapeVerdict.InvariantOutcome o = inv.evaluate(t);
        assertFalse("hidden failure must fire", o.passed);
        assertEquals("HIDDEN_DOWNSTREAM_FAILURE", o.kind);
        assertEquals(TraceShapeVerdict.Severity.ERROR, o.severity);
    }

    @Test
    public void fires_whenRoot2xxButDescendantOtelErrorNoHttp() {
        // The real train-ticket pattern: leaf error span has otel=ERROR, http=0.
        TraceModel t = new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-route", "RouteController.createAndModifyRoute", 0, "ERROR")));
        assertFalse(inv.evaluate(t).passed);
    }

    @Test
    public void passes_whenAllHealthy2xx() {
        TraceModel t = new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-order", "OrderController.create", 200, null)));
        assertTrue(inv.evaluate(t).passed);
    }

    @Test
    public void passes_whenRootItselfErrored_loudNotHidden() {
        TraceModel t = new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 500, "ERROR"),
                span("child", "root", "ts-order", "OrderController.create", 500, "ERROR")));
        assertTrue("root surfaced the error → loud, not hidden", inv.evaluate(t).passed);
    }

    @Test
    public void passes_whenDescendant4xx_belowServerErrorThreshold() {
        // reviewer B3: downstream 4xx is benign control-flow, must NOT fire.
        TraceModel t = new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-order", "OrderController.lookup", 404, null)));
        assertTrue(inv.evaluate(t).passed);
    }

    @Test
    public void passes_whenEmptyOrNull() {
        assertTrue(inv.evaluate(null).passed);
        assertTrue(inv.evaluate(new TraceModel("t", Collections.emptyList())).passed);
    }
}
