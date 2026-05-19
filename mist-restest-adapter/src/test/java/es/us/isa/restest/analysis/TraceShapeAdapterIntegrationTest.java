package es.us.isa.restest.analysis;

import io.mist.core.oracle.shape.ShapeInvariantStore;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeOracle;
import io.mist.core.oracle.shape.TraceShapeVerdict;
import io.mist.core.oracle.shape.invariant.SpanTreeShapeInvariant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * End-to-end verification of the {@code TraceShapeAdapter -> TraceShapeOracle}
 * pipeline. Pre-seeds an invariant store with a {@link SpanTreeShapeInvariant}
 * learned from a benign two-service trace, then asks the oracle to evaluate
 * a hand-crafted Jaeger JSON whose tree contains an unrelated downstream
 * service. Asserts that the verdict carries a {@code SPAN_TREE_SHAPE}
 * violation.
 */
public class TraceShapeAdapterIntegrationTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    /** Root API key the seed traces and the candidate trace both share. */
    private static final String ROOT_API_KEY = "POST /api/v1/orders";

    @Test
    public void unrelatedServiceProducesSpanTreeShapeViolation() throws Exception {
        Path storePath = tmp.newFile("trace-shape-invariants.json").toPath();
        Files.deleteIfExists(storePath); // start clean — fresh learner output goes here.

        // Step 1: learn a span-tree shape from a corpus of known-good traces where
        // the only allowed edge is gateway -> order-service. Persist to the store.
        List<TraceModel> goodCorpus = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            goodCorpus.add(buildGoodTrace("trace-good-" + i));
        }
        ShapeInvariantStore store = new ShapeInvariantStore(storePath);
        SpanTreeShapeInvariant.Data data = SpanTreeShapeInvariant.learn(goodCorpus);
        new SpanTreeShapeInvariant(ROOT_API_KEY, data).persist(store);
        store.flush();
        assertTrue("invariant file should now exist", Files.isRegularFile(storePath));

        // Step 2: build a Jaeger-shaped trace that introduces an unrelated downstream
        // service ("unrelated-rogue-service") under the gateway root.
        JSONObject jaegerTrace = buildCandidateJaegerTrace();
        assertNotNull(jaegerTrace);

        // Step 3: run the writer-side adapter and the oracle. The store has a
        // SpanTreeShape invariant trained for the same root API; the candidate
        // trace contains an edge the corpus never observed.
        TraceModel model = TraceShapeAdapter.toModel(jaegerTrace, ROOT_API_KEY);
        assertTrue("adapter should yield at least the three spans we built",
                model.getSpans().size() >= 3);

        TraceShapeOracle oracle = new TraceShapeOracle(new ShapeInvariantStore(storePath));
        TraceShapeVerdict verdict = oracle.evaluate(model, ROOT_API_KEY);
        assertFalse("verdict should not pass — unrelated service span breaks span-tree shape",
                verdict.isPassed());

        boolean spanTreeViolation = false;
        for (TraceShapeVerdict.InvariantOutcome o : verdict.getOutcomes()) {
            if ("SPAN_TREE_SHAPE".equals(o.kind) && !o.passed) {
                spanTreeViolation = true;
                assertNotNull("violation detail should describe the unexpected edge", o.detail);
                assertTrue("violation should mention unrelated service",
                        o.detail.contains("unrelated-rogue-service")
                                || o.detail.contains("unexpected edges"));
                break;
            }
        }
        assertTrue("at least one SPAN_TREE_SHAPE violation outcome should be present",
                spanTreeViolation);
    }

    /**
     * Build a known-good trace with just two services: gateway is the root
     * and calls order-service. No other edges; nothing exotic.
     */
    private static TraceModel buildGoodTrace(String traceId) {
        List<TraceModel.Span> spans = new ArrayList<>();
        spans.add(new TraceModel.Span(
                "root-" + traceId,
                /* parent */ null,
                "ts-gateway-service",
                "POST /api/v1/orders",
                200,
                null,
                /* duration micros */ 1000L,
                Collections.singletonMap("http.method", "POST")));
        spans.add(new TraceModel.Span(
                "order-" + traceId,
                "root-" + traceId,
                "ts-order-service",
                "POST /api/v1/orders",
                200,
                null,
                500L,
                Collections.emptyMap()));
        return new TraceModel(traceId, spans);
    }

    /**
     * Build a Jaeger-format trace whose root call (gateway) fans out to BOTH
     * the trained order-service AND a service the learner has never seen.
     * The unrelated edge is what the oracle should flag.
     */
    private static JSONObject buildCandidateJaegerTrace() {
        JSONObject trace = new JSONObject();
        trace.put("traceID", "candidate-trace-1");

        // Jaeger's "processes" map: processID -> { serviceName: ... }
        JSONObject processes = new JSONObject();
        processes.put("p1", new JSONObject().put("serviceName", "ts-gateway-service"));
        processes.put("p2", new JSONObject().put("serviceName", "ts-order-service"));
        processes.put("p3", new JSONObject().put("serviceName", "unrelated-rogue-service"));
        trace.put("processes", processes);

        JSONArray spans = new JSONArray();
        spans.put(jaegerSpan("root-1", null, "p1", "POST /api/v1/orders", 200, 1000L));
        spans.put(jaegerSpan("order-1", "root-1", "p2", "POST /api/v1/orders", 200, 500L));
        // The rogue span: gateway -> unrelated-rogue-service edge was never in
        // the corpus, so SpanTreeShapeInvariant must flag it.
        spans.put(jaegerSpan("rogue-1", "root-1", "p3", "GET /api/v1/rogue", 200, 250L));
        trace.put("spans", spans);

        return trace;
    }

    private static JSONObject jaegerSpan(String spanId, String parentSpanId, String processId,
                                         String operationName, int httpStatusCode, long durationMicros) {
        JSONObject span = new JSONObject();
        span.put("spanID", spanId);
        if (parentSpanId != null) {
            JSONArray refs = new JSONArray();
            JSONObject ref = new JSONObject();
            ref.put("refType", "CHILD_OF");
            ref.put("spanID", parentSpanId);
            refs.put(ref);
            span.put("references", refs);
        }
        span.put("processID", processId);
        span.put("operationName", operationName);
        span.put("duration", durationMicros);
        span.put("startTime", 0L);

        JSONArray tags = new JSONArray();
        JSONObject httpStatus = new JSONObject();
        httpStatus.put("key", "http.status_code");
        httpStatus.put("type", "int64");
        httpStatus.put("value", httpStatusCode);
        tags.put(httpStatus);
        span.put("tags", tags);

        return span;
    }

    /**
     * Sanity check that the adapter accepts a totally empty payload without
     * blowing up. Important because the writer may invoke it before any
     * spans have arrived on the wire.
     */
    @Test
    public void emptyTraceProducesEmptyModelWithoutException() {
        JSONObject empty = new JSONObject();
        empty.put("traceID", "empty");
        TraceModel model = TraceShapeAdapter.toModel(empty, ROOT_API_KEY);
        assertNotNull(model);
        assertTrue(model.getSpans().isEmpty());
        // Also: a freshly-constructed oracle on an empty store should pass.
        TraceShapeOracle oracle = new TraceShapeOracle(new ShapeInvariantStore(
                tmp.getRoot().toPath().resolve("never-existed.json")));
        TraceShapeVerdict v = oracle.evaluate(model, ROOT_API_KEY);
        assertTrue("empty store -> all invariants vacuously pass", v.isPassed());
    }
}
