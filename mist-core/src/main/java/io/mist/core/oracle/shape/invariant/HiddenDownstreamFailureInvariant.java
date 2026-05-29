package io.mist.core.oracle.shape.invariant;

import io.mist.core.oracle.shape.ShapeInvariant;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeVerdict;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Intent-agnostic structural invariant: flags a HIDDEN downstream failure —
 * the client-facing root span returned success (2xx) while a deeper span
 * server-errored, so the failure was swallowed and never surfaced to the
 * caller. This is a real correctness bug a response-level oracle cannot see
 * (the response looked fine); detecting it requires the distributed trace.
 *
 * <p>"Server error" here is {@code http >= 500} OR {@code otel.status_code =
 * ERROR} — deliberately NOT the {@code >= 400} definition used by
 * {@link io.mist.core.oracle.attribution.LeafErrorSpanFinder#isErrorTagged},
 * because a downstream 4xx is typically benign control-flow (lookup miss,
 * internal validation) and would be a false-positive source.
 *
 * <p>No learned data ({@code T = Void}); pure runtime structural check.
 */
public final class HiddenDownstreamFailureInvariant implements ShapeInvariant<Void> {

    public static final String KIND = "HIDDEN_DOWNSTREAM_FAILURE";

    private final String rootApiKey;

    public HiddenDownstreamFailureInvariant(String rootApiKey) {
        this.rootApiKey = rootApiKey == null ? "" : rootApiKey;
    }

    @Override public String kind() { return KIND; }

    @Override public String rootApiKey() { return rootApiKey; }

    @Override
    public TraceShapeVerdict.InvariantOutcome evaluate(TraceModel trace) {
        if (trace == null || trace.getSpans() == null || trace.getSpans().isEmpty()) {
            return pass();
        }
        List<TraceModel.Span> roots = trace.roots();
        boolean someRoot2xx = false;
        for (TraceModel.Span r : roots) {
            // If any root surfaced the error, the failure is LOUD, not hidden.
            if (isServerError(r)) return pass();
            if (is2xx(r)) someRoot2xx = true;
        }
        if (!someRoot2xx) return pass();

        Set<String> rootIds = new HashSet<>();
        for (TraceModel.Span r : roots) rootIds.add(r.spanId);

        List<String> badDescendants = new ArrayList<>();
        StringBuilder detail = new StringBuilder();
        for (TraceModel.Span s : trace.getSpans()) {
            if (rootIds.contains(s.spanId) || !isServerError(s)) continue;
            badDescendants.add(s.spanId);
            if (detail.length() > 0) detail.append("; ");
            detail.append(s.service).append('/').append(s.operation)
                  .append(" http=").append(s.httpStatus).append(" otel=").append(s.otelStatus);
        }
        if (badDescendants.isEmpty()) return pass();

        return TraceShapeVerdict.InvariantOutcome.fail(
                KIND, rootApiKey, TraceShapeVerdict.Severity.ERROR,
                "root returned 2xx but " + badDescendants.size()
                        + " downstream span(s) server-errored: " + detail,
                badDescendants);
    }

    private TraceShapeVerdict.InvariantOutcome pass() {
        return TraceShapeVerdict.InvariantOutcome.pass(KIND, rootApiKey, TraceShapeVerdict.Severity.ERROR);
    }

    private static boolean isServerError(TraceModel.Span s) {
        if (s == null) return false;
        return s.httpStatus >= 500 || "ERROR".equalsIgnoreCase(s.otelStatus);
    }

    private static boolean is2xx(TraceModel.Span s) {
        return s != null && s.httpStatus >= 200 && s.httpStatus < 300;
    }
}
