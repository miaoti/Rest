package io.mist.core.oracle.shape;

import io.mist.core.config.MstConfig;
import io.mist.core.oracle.shape.invariant.ResponseEnvelopeInvariant;
import io.mist.core.oracle.shape.invariant.SpanTreeShapeInvariant;
import io.mist.core.oracle.shape.invariant.StatusPropagationInvariant;
import io.mist.core.oracle.shape.invariant.TimingEnvelopeInvariant;

/**
 * Top-level entry point for runtime evaluation. Loads the four invariant
 * kinds for a given root API from the {@link ShapeInvariantStore} and runs
 * them against a {@link TraceModel}. The aggregate verdict's {@code passed}
 * flag is true iff every ERROR-severity invariant outcome is true.
 *
 * <p>Each invariant is gated by a flag carried on the
 * {@link MstConfig.Oracle} record passed to the constructor. When the
 * whole-oracle gate is off, {@link #evaluate} returns
 * {@link TraceShapeVerdict#empty()} without loading any invariant; when
 * one per-invariant gate is off, that invariant's load + evaluate calls
 * are skipped while the rest still run.
 */
public final class TraceShapeOracle {

    private final ShapeInvariantStore store;
    private final boolean shapeOracleEnabled;
    private final boolean spanTreeEnabled;
    private final boolean statusPropagationEnabled;
    private final boolean responseEnvelopeEnabled;
    private final boolean timingEnvelopeEnabled;

    public TraceShapeOracle(ShapeInvariantStore store, MstConfig.Oracle oracleConfig) {
        this.store = store == null ? new ShapeInvariantStore() : store;
        MstConfig.Oracle cfg = oracleConfig == null ? new MstConfig.Oracle() : oracleConfig;
        this.shapeOracleEnabled = cfg.shapeOracleEnabled();
        this.spanTreeEnabled = cfg.spanTreeInvariantEnabled();
        this.statusPropagationEnabled = cfg.statusPropagationInvariantEnabled();
        this.responseEnvelopeEnabled = cfg.responseEnvelopeInvariantEnabled();
        this.timingEnvelopeEnabled = cfg.timingEnvelopeInvariantEnabled();
    }

    public TraceShapeOracle(ShapeInvariantStore store) {
        this(store, MstConfig.instance().oracle());
    }

    public TraceShapeVerdict evaluate(TraceModel trace, String rootApiKey) {
        if (!shapeOracleEnabled) return TraceShapeVerdict.empty();
        TraceShapeVerdict.Builder builder = TraceShapeVerdict.builder();
        if (spanTreeEnabled) {
            builder.add(SpanTreeShapeInvariant.load(rootApiKey, store).evaluate(trace));
        }
        if (statusPropagationEnabled) {
            builder.add(StatusPropagationInvariant.load(rootApiKey, store).evaluate(trace));
        }
        if (timingEnvelopeEnabled) {
            builder.add(TimingEnvelopeInvariant.load(rootApiKey, store).evaluate(trace));
        }
        if (responseEnvelopeEnabled) {
            builder.add(ResponseEnvelopeInvariant.load(rootApiKey, store).evaluate(trace));
        }
        return builder.build();
    }
}
