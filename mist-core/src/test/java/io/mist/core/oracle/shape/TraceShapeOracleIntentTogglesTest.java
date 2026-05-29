package io.mist.core.oracle.shape;

import io.mist.core.config.MstConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Pins the Phase-3 wiring of the two intent-aware detectors into
 * TraceShapeOracle's 4-arg evaluate: each participates only when its opt-in
 * flag is on (both default OFF), so legacy verdicts are unchanged.
 */
public class TraceShapeOracleIntentTogglesTest {

    private static final String HDF = "mst.oracle.shape.invariants.hidden_downstream_failure.enabled";
    private static final String SA = "mst.oracle.shape.invariants.silent_acceptance.enabled";
    private String prevHdf, prevSa;

    @Before
    public void setUp() {
        prevHdf = System.getProperty(HDF);
        prevSa = System.getProperty(SA);
        System.clearProperty(HDF);
        System.clearProperty(SA);
        MstConfig.resetForTesting();
    }

    @After
    public void tearDown() {
        restore(HDF, prevHdf);
        restore(SA, prevSa);
        MstConfig.resetForTesting();
    }

    private static void restore(String k, String v) {
        if (v == null) System.clearProperty(k); else System.setProperty(k, v);
    }

    private static TraceModel.Span span(String id, String parent, String svc, String op, int http, String otel) {
        return new TraceModel.Span(id, parent, svc, op, http, otel, 0L, new HashMap<>());
    }

    private static TraceModel hiddenFailureTrace() {
        return new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-order", "OrderController.create", 500, "ERROR")));
    }

    private static TraceModel silentAcceptTrace() {
        return new TraceModel("t", Arrays.asList(
                span("root", null, "ts-gateway", "POST /x", 200, null),
                span("child", "root", "ts-admin-route", "AdminRouteController.addRoute", 200, null)));
    }

    private static TraceShapeVerdict.InvariantOutcome outcome(TraceShapeVerdict v, String kind) {
        for (TraceShapeVerdict.InvariantOutcome o : v.getOutcomes()) {
            if (kind.equals(o.kind)) return o;
        }
        return null;
    }

    @Test
    public void hiddenDownstream_firesWhenFlagOn() {
        System.setProperty(HDF, "true");
        MstConfig.resetForTesting();
        TraceShapeVerdict v = new TraceShapeOracle(new ShapeInvariantStore())
                .evaluate(hiddenFailureTrace(), "POST /x", null, null);
        TraceShapeVerdict.InvariantOutcome o = outcome(v, "HIDDEN_DOWNSTREAM_FAILURE");
        assertNotNull("invariant should be wired in when flag on", o);
        assertFalse(o.passed);
    }

    @Test
    public void hiddenDownstream_absentWhenFlagOff() {
        TraceShapeVerdict v = new TraceShapeOracle(new ShapeInvariantStore())
                .evaluate(hiddenFailureTrace(), "POST /x", null, null);
        assertNull("must not run when flag off (default)", outcome(v, "HIDDEN_DOWNSTREAM_FAILURE"));
    }

    @Test
    public void silentAcceptance_firesWhenFlagOnAndTargetPresent() {
        System.setProperty(SA, "true");
        MstConfig.resetForTesting();
        TraceShapeVerdict v = new TraceShapeOracle(new ShapeInvariantStore())
                .evaluate(silentAcceptTrace(), "POST /x", "ts-admin-route", "stationList");
        TraceShapeVerdict.InvariantOutcome o = outcome(v, "SILENT_ACCEPTANCE");
        assertNotNull("invariant should be wired in when flag on + target present", o);
        assertFalse(o.passed);
    }

    @Test
    public void silentAcceptance_absentWhenFlagOff() {
        TraceShapeVerdict v = new TraceShapeOracle(new ShapeInvariantStore())
                .evaluate(silentAcceptTrace(), "POST /x", "ts-admin-route", "stationList");
        assertNull("must not run when flag off (default)", outcome(v, "SILENT_ACCEPTANCE"));
    }
}
