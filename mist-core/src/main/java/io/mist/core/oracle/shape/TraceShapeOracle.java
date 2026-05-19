package io.mist.core.oracle.shape;

import io.mist.core.oracle.shape.invariant.ResponseEnvelopeInvariant;
import io.mist.core.oracle.shape.invariant.SpanTreeShapeInvariant;
import io.mist.core.oracle.shape.invariant.StatusPropagationInvariant;
import io.mist.core.oracle.shape.invariant.TimingEnvelopeInvariant;

/**
 * Top-level entry point for runtime evaluation. Loads the four invariant
 * kinds for a given root API from the {@link ShapeInvariantStore} and runs
 * them against a {@link TraceModel}. The aggregate verdict's {@code passed}
 * flag is true iff every ERROR-severity invariant outcome is true.
 */
public final class TraceShapeOracle {

    private final ShapeInvariantStore store;

    public TraceShapeOracle(ShapeInvariantStore store) {
        this.store = store == null ? new ShapeInvariantStore() : store;
    }

    public TraceShapeVerdict evaluate(TraceModel trace, String rootApiKey) {
        TraceShapeVerdict.Builder builder = TraceShapeVerdict.builder();
        builder.add(SpanTreeShapeInvariant.load(rootApiKey, store).evaluate(trace));
        builder.add(StatusPropagationInvariant.load(rootApiKey, store).evaluate(trace));
        builder.add(TimingEnvelopeInvariant.load(rootApiKey, store).evaluate(trace));
        builder.add(ResponseEnvelopeInvariant.load(rootApiKey, store).evaluate(trace));
        return builder.build();
    }
}
