package io.mist.core.oracle.shape.invariant;

import io.mist.core.oracle.shape.ShapeInvariant;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeVerdict;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Structural, label-free invariant that flags a HIDDEN downstream failure: the
 * client-facing entry returned success (2xx) while a deeper span server-errored,
 * so the failure was swallowed and never surfaced to the caller.
 *
 * <p><b>Why this needs the trace.</b> The bytes the client receives are a valid
 * 2xx response; the only evidence of failure lives in a downstream span that
 * never touches the response path. No response-level oracle can see it — not a
 * status/schema check, not MIST's own LLM soft-error check (the body is a clean
 * success). It is observable only in the distributed trace. The detector uses no
 * learned data ({@code T = Void}), no human-authored span assertions, and no
 * trained normal-pattern baseline — it is a pure runtime structural check. It is
 * intentionally intent-<i>agnostic</i>; the system's intent-conditioning lives in
 * the negative-test generation and {@code TargetAttributionInvariant}, not here.
 *
 * <p><b>Entry identification.</b> The client-facing entry is the span matching
 * {@link #rootApiKey} (the endpoint under test). Earlier versions treated <i>any</i>
 * parentless span as a root and passed as soon as one was a server error; under
 * Jaeger's partial/service-scoped trace views a downstream 5xx can appear as a
 * co-root, which silently suppressed real findings. We now key off the matched
 * entry and treat every <i>other</i> server-error span as swallowed. When no span
 * matches the key we fall back to all parentless spans (precision degrades on
 * partial views — documented, not silent).
 *
 * <p><b>Server error</b> is {@code http >= 500} OR {@code otel.status_code = ERROR}
 * — deliberately NOT the {@code >= 400} definition, because a downstream 4xx is
 * typically benign control-flow (lookup miss, internal validation).
 *
 * <p><b>Confidence (ERROR vs WARN).</b> A swallowed span with {@code http >= 500}
 * is a synchronous call the caller waited on and masked → {@link
 * TraceShapeVerdict.Severity#ERROR} (a real swallowed failure that fails the
 * verdict). A span with only {@code otel=ERROR} (no HTTP 5xx) lacks that
 * synchronous-RPC evidence — it may be an internal/asynchronous/tolerated error
 * — so it is reported as {@link TraceShapeVerdict.Severity#WARN}: surfaced, but
 * non-blocking, so a deliberately-tolerated downstream error is not over-claimed
 * as a bug.
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

        // Identify the client-facing entry: prefer spans matching rootApiKey;
        // fall back to all parentless spans when nothing matches.
        Set<String> entryIds = new HashSet<>();
        List<TraceModel.Span> entries = new ArrayList<>();
        for (TraceModel.Span r : roots) {
            if (isEntry(r)) { entries.add(r); entryIds.add(r.spanId); }
        }
        if (entries.isEmpty()) {
            entries = roots;
            for (TraceModel.Span r : roots) entryIds.add(r.spanId);
        }

        // If the entry itself surfaced a server error, the failure was LOUD
        // (already visible to the caller) — not hidden.
        boolean entry2xx = false;
        for (TraceModel.Span e : entries) {
            if (isServerError(e)) return pass();
            if (is2xx(e)) entry2xx = true;
        }
        if (!entry2xx) return pass();

        // Any server-error span that is NOT the entry was swallowed: the caller
        // got a clean 2xx while this span failed. A co-root downstream error in a
        // partial view is counted here rather than suppressing the finding.
        List<String> swallowed = new ArrayList<>();
        boolean anyHttp5xx = false;
        StringBuilder detail = new StringBuilder();
        for (TraceModel.Span s : trace.getSpans()) {
            if (entryIds.contains(s.spanId) || !isServerError(s)) continue;
            swallowed.add(s.spanId);
            if (s.httpStatus >= 500) anyHttp5xx = true;
            if (detail.length() > 0) detail.append("; ");
            detail.append(s.service).append('/').append(s.operation)
                  .append(" http=").append(s.httpStatus).append(" otel=").append(s.otelStatus);
        }
        if (swallowed.isEmpty()) return pass();

        TraceShapeVerdict.Severity sev = anyHttp5xx
                ? TraceShapeVerdict.Severity.ERROR
                : TraceShapeVerdict.Severity.WARN;

        return TraceShapeVerdict.InvariantOutcome.fail(
                KIND, rootApiKey, sev,
                "caller received 2xx but " + swallowed.size()
                        + " downstream span(s) server-errored (swallowed): " + detail,
                swallowed);
    }

    /** True if this span is the endpoint under test (the client-facing entry). */
    private boolean isEntry(TraceModel.Span s) {
        if (rootApiKey.isEmpty() || s == null) return false;
        String op = s.operation == null ? "" : s.operation;
        if (op.equalsIgnoreCase(rootApiKey)) return true;
        int sp = rootApiKey.indexOf(' ');
        String path = sp >= 0 ? rootApiKey.substring(sp + 1).trim() : rootApiKey;
        if (path.isEmpty()) return false;
        if (op.equalsIgnoreCase(path) || op.endsWith(path)) return true;
        String url = s.tags.get("http.url");
        if (url == null) url = s.tags.get("http.target");
        return url != null && url.contains(path);
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
