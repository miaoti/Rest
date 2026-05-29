package io.mist.core.oracle.shape.invariant;

import io.mist.core.oracle.shape.ShapeInvariant;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeVerdict;

import java.util.ArrayList;
import java.util.List;

/**
 * Intent-aware invariant: flags SILENT ACCEPTANCE — a negative test injected
 * an invalid value into {@code targetParam}, yet the SUT returned success
 * (root 2xx) and the trace shows no rejection anywhere. The bad input was
 * accepted instead of rejected.
 *
 * <p>Intent-conditioned: only meaningful for a negative test, so it no-ops
 * (passes) when {@code targetService} is null/empty (a positive test). This
 * guard lives INSIDE the invariant so it can never fail a healthy positive.
 *
 * <p>Severity WARN for now: a 2xx for "invalid" input is not always a bug
 * (idempotent/optional params, syntactic faults the SUT tolerates). Promote
 * to ERROR once generation emits semantic baits that should unambiguously be
 * rejected. "Rejection anywhere" = any span with {@code http >= 400} or
 * {@code otel.status_code = ERROR}.
 *
 * <p>No learned data ({@code T = Void}); pure runtime check.
 */
public final class SilentAcceptanceInvariant implements ShapeInvariant<Void> {

    public static final String KIND = "SILENT_ACCEPTANCE";

    private final String rootApiKey;
    private final String targetService;
    private final String targetParam;

    public SilentAcceptanceInvariant(String rootApiKey, String targetService, String targetParam) {
        this.rootApiKey = rootApiKey == null ? "" : rootApiKey;
        this.targetService = targetService;
        this.targetParam = targetParam;
    }

    @Override public String kind() { return KIND; }

    @Override public String rootApiKey() { return rootApiKey; }

    @Override
    public TraceShapeVerdict.InvariantOutcome evaluate(TraceModel trace) {
        // Intent guard (D2): no target → positive test → never fire.
        if (targetService == null || targetService.isEmpty()) return pass();
        if (trace == null || trace.getSpans() == null || trace.getSpans().isEmpty()) return pass();

        // Any rejection anywhere (4xx or error span) → the SUT pushed back → not silent.
        for (TraceModel.Span s : trace.getSpans()) {
            if (isError(s)) return pass();
        }
        // No rejection anywhere; did the client get a success response?
        List<String> root2xxIds = new ArrayList<>();
        for (TraceModel.Span r : trace.roots()) {
            if (is2xx(r)) root2xxIds.add(r.spanId);
        }
        if (root2xxIds.isEmpty()) return pass();

        return TraceShapeVerdict.InvariantOutcome.fail(
                KIND, rootApiKey, TraceShapeVerdict.Severity.WARN,
                "negative test targeting " + targetService + "/" + targetParam
                        + " was silently accepted: no rejection in trace and root returned 2xx",
                root2xxIds);
    }

    private TraceShapeVerdict.InvariantOutcome pass() {
        return TraceShapeVerdict.InvariantOutcome.pass(KIND, rootApiKey, TraceShapeVerdict.Severity.WARN);
    }

    private static boolean isError(TraceModel.Span s) {
        if (s == null) return false;
        return s.httpStatus >= 400 || "ERROR".equalsIgnoreCase(s.otelStatus);
    }

    private static boolean is2xx(TraceModel.Span s) {
        return s != null && s.httpStatus >= 200 && s.httpStatus < 300;
    }
}
