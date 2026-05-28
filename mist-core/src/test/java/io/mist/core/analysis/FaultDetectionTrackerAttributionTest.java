package io.mist.core.analysis;

import io.mist.core.oracle.attribution.AttributionVerdict;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeVerdict;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Phase 2 part 3 contract: attribution counts on OracleAnomaly,
 * attribution roll-up in report, behavior when target context is null.
 */
public class FaultDetectionTrackerAttributionTest {

    private static final String FAKE_FAULTS_JSON =
            "{\"injected_faults\":[{\"faultName\":\"F1\",\"service\":\"svc\",\"api\":\"GET /x\"}]}";

    private Path tempFaultsFile;
    private String prevReportFlag;

    @Before
    public void setUp() throws IOException {
        tempFaultsFile = Files.createTempFile("injected-faults-attr-", ".json");
        Files.write(tempFaultsFile, FAKE_FAULTS_JSON.getBytes());
        FaultDetectionTracker.getInstance().reset();
        FaultDetectionTracker.getInstance().loadInjectedFaults(tempFaultsFile.toString());
        prevReportFlag = System.getProperty("mist.report.oracle.anomalies.enabled");
        System.clearProperty("mist.report.oracle.anomalies.enabled");
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFaultsFile);
        FaultDetectionTracker.getInstance().reset();
        if (prevReportFlag == null) {
            System.clearProperty("mist.report.oracle.anomalies.enabled");
        } else {
            System.setProperty("mist.report.oracle.anomalies.enabled", prevReportFlag);
        }
    }

    @Test
    public void recordVerdict_nullTargets_skipsAttribution() {
        TraceShapeVerdict v = oneFailingVerdict();
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        t.recordVerdict(v, "POST /x", "C", "m", "trace-1");
        assertEquals(1, t.getOracleAnomaliesForTest().size());
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertTrue(a.attributionCounts.isEmpty());
    }

    @Test
    public void recordVerdict_targetRejection_incrementsBucket() {
        TraceShapeVerdict v = oneFailingVerdict();
        TraceModel trace = traceWithLeafError("ts-order-service", "validateSeatNumber");
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        t.recordVerdict(v, trace, "POST /x", "C", "m", "trace-1",
                "ts-order-service", "seatNumber");
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertEquals(Integer.valueOf(1), a.attributionCounts.get(AttributionVerdict.TARGET_REJECTION));
    }

    @Test
    public void recordVerdict_upstreamRejection_incrementsBucket() {
        TraceShapeVerdict v = oneFailingVerdict();
        TraceModel trace = traceWithLeafError("ts-payment-service", "charge");
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        t.recordVerdict(v, trace, "POST /x", "C", "m", "trace-1",
                "ts-order-service", "seatNumber");
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertEquals(Integer.valueOf(1), a.attributionCounts.get(AttributionVerdict.UPSTREAM_REJECTION));
    }

    @Test
    public void recordVerdict_wrongParamRejection_incrementsBucket() {
        TraceShapeVerdict v = oneFailingVerdict();
        TraceModel trace = traceWithLeafError("ts-order-service", "validateContactsName");
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        t.recordVerdict(v, trace, "POST /x", "C", "m", "trace-1",
                "ts-order-service", "seatNumber");
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertEquals(Integer.valueOf(1), a.attributionCounts.get(AttributionVerdict.WRONG_PARAM_REJECTION));
    }

    @Test
    public void multipleVerdicts_accumulateInBuckets() {
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        TraceModel target = traceWithLeafError("ts-order-service", "validateSeatNumber");
        TraceModel upstream = traceWithLeafError("ts-payment-service", "charge");
        for (int i = 0; i < 3; i++) {
            t.recordVerdict(oneFailingVerdict(), target, "POST /x", "C", "m" + i, "tr" + i,
                    "ts-order-service", "seatNumber");
        }
        for (int i = 0; i < 5; i++) {
            t.recordVerdict(oneFailingVerdict(), upstream, "POST /x", "C", "u" + i, "tu" + i,
                    "ts-order-service", "seatNumber");
        }
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertEquals(8, a.hitCount);
        assertEquals(Integer.valueOf(3), a.attributionCounts.get(AttributionVerdict.TARGET_REJECTION));
        assertEquals(Integer.valueOf(5), a.attributionCounts.get(AttributionVerdict.UPSTREAM_REJECTION));
    }

    @Test
    public void report_emitsAttributionLine_perAnomaly() throws IOException {
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        TraceModel target = traceWithLeafError("ts-order-service", "validateSeatNumber");
        t.recordVerdict(oneFailingVerdict(), target, "POST /x", "C", "m", "tr",
                "ts-order-service", "seatNumber");

        Path reportDir = Files.createTempDirectory("fdt-attr-");
        try {
            t.generateReport(reportDir.toString(), "phase2-attr");
            File[] files = reportDir.toFile().listFiles();
            assertNotNull(files);
            assertTrue(files.length > 0);
            String text = new String(Files.readAllBytes(files[0].toPath()));
            assertTrue("anomaly section present", text.contains("ORACLE ANOMALIES"));
            assertTrue("attribution line on entry", text.contains("Attribution:"));
            assertTrue("rollup label present", text.contains("Attribution roll-up"));
            assertTrue("target verdict surfaced", text.contains("TARGET_REJECTION"));
        } finally {
            deleteRecursive(reportDir.toFile());
        }
    }

    @Test
    public void recordVerdict_nullTrace_recordsAnomalyButSkipsAttribution() {
        TraceShapeVerdict v = oneFailingVerdict();
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        t.recordVerdict(v, null /* trace */, "POST /x", "C", "m", "tr",
                "ts-order-service", "seatNumber");
        assertEquals(1, t.getOracleAnomaliesForTest().size());
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertTrue("null trace cannot produce attribution", a.attributionCounts.isEmpty());
    }

    @Test
    public void recordVerdict_nullTargetService_skipsAttribution() {
        TraceShapeVerdict v = oneFailingVerdict();
        TraceModel trace = traceWithLeafError("ts-order-service", "validateSeat");
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        t.recordVerdict(v, trace, "POST /x", "C", "m", "tr",
                null /* targetService */, "seatNumber");
        FaultDetectionTracker.OracleAnomaly a = t.getOracleAnomaliesForTest().values().iterator().next();
        assertTrue("no target service → no attribution", a.attributionCounts.isEmpty());
    }

    @Test
    public void resetClearsAttribution() {
        FaultDetectionTracker t = FaultDetectionTracker.getInstance();
        TraceModel target = traceWithLeafError("ts-order-service", "validateSeatNumber");
        t.recordVerdict(oneFailingVerdict(), target, "POST /x", "C", "m", "tr",
                "ts-order-service", "seatNumber");
        assertEquals(1, t.getOracleAnomaliesForTest().size());
        t.reset();
        assertEquals(0, t.getOracleAnomaliesForTest().size());
    }

    // ---------------------------------------------------------------------

    private static TraceShapeVerdict oneFailingVerdict() {
        return TraceShapeVerdict.builder()
                .add(TraceShapeVerdict.InvariantOutcome.fail(
                        "STATUS_PROPAGATION", "POST /x", TraceShapeVerdict.Severity.ERROR,
                        "depth=0 http=500 expected=[200, 201]", Collections.singletonList("s1")))
                .build();
    }

    private static TraceModel traceWithLeafError(String leafService, String leafOp) {
        Map<String, String> tags = new HashMap<>();
        tags.put("http.status_code", "500");
        tags.put("otel.status_code", "ERROR");
        return new TraceModel("test-trace", Arrays.asList(
                new TraceModel.Span("root", null, "ts-gateway", "POST /x", 500, "ERROR", 0L, tags),
                new TraceModel.Span("leaf", "root", leafService, leafOp, 500, "ERROR", 0L, tags)));
    }

    private static void deleteRecursive(File f) {
        if (f.isDirectory()) {
            File[] kids = f.listFiles();
            if (kids != null) for (File k : kids) deleteRecursive(k);
        }
        f.delete();
    }
}
