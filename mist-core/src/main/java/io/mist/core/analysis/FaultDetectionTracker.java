package io.mist.core.analysis;

import io.mist.core.oracle.attribution.AttributionVerdict;
import io.mist.core.oracle.attribution.TraceAttribution;
import io.mist.core.oracle.shape.TraceModel;
import io.mist.core.oracle.shape.TraceShapeVerdict;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe singleton tracker for monitoring injected fault detection during test execution.
 * Tracks which faults from injected-faults.json are detected by test cases and generates
 * comprehensive coverage reports.
 */
public class FaultDetectionTracker {
    
    private static final Logger logger = LogManager.getLogger(FaultDetectionTracker.class);
    private static final FaultDetectionTracker INSTANCE = new FaultDetectionTracker();
    
    // Thread-safe data structures
    private final Map<String, InjectedFault> injectedFaults = new ConcurrentHashMap<>();
    private final Map<String, List<FaultDetection>> detectedFaults = new ConcurrentHashMap<>();
    private final Set<String> allTestCases = ConcurrentHashMap.newKeySet();

    // Phase 0: TraceShapeOracle violations that fired but didn't match an
    // injected fault name. Keyed by (oracle, endpointSig, violationFingerprint)
    // so same-shape violations across many tests collapse into one entry with
    // its hitCount incremented. See recordOracleAnomaly / recordVerdict.
    private final Map<String, OracleAnomaly> oracleAnomalies = new ConcurrentHashMap<>();
    
    // Metadata
    private String experimentName;
    private long trackingStartTime;
    private boolean initialized = false;
    
    private FaultDetectionTracker() {
        this.trackingStartTime = System.currentTimeMillis();
    }
    
    public static FaultDetectionTracker getInstance() {
        return INSTANCE;
    }
    
    /**
     * Load injected faults from the JSON registry file
     */
    public synchronized void loadInjectedFaults(String jsonPath) {
        try {
            logger.info("Loading injected faults from: {}", jsonPath);
            
            String content = new String(Files.readAllBytes(Paths.get(jsonPath)));
            JSONObject root = new JSONObject(content);
            
            if (!root.has("injected_faults")) {
                logger.error("JSON file does not contain 'injected_faults' array");
                return;
            }
            
            JSONArray faults = root.getJSONArray("injected_faults");
            injectedFaults.clear();
            
            for (int i = 0; i < faults.length(); i++) {
                JSONObject faultJson = faults.getJSONObject(i);
                InjectedFault fault = new InjectedFault(
                    faultJson.getString("faultName"),
                    faultJson.getString("service"),
                    faultJson.getString("api")
                );
                injectedFaults.put(fault.faultName, fault);
            }
            
            logger.info("Loaded {} injected faults from registry", injectedFaults.size());
            initialized = true;
            
        } catch (IOException e) {
            logger.error("Failed to load injected faults file: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to parse injected faults JSON: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Record a detected fault from a test execution
     */
    public synchronized void recordDetectedFault(String faultName, String testClassName, 
                                                 String testMethodName, long timestamp, 
                                                 String responseBody) {
        if (!initialized) {
            logger.warn("FaultDetectionTracker not initialized. Call loadInjectedFaults() first.");
            return;
        }
        
        // Track test case
        String fullTestName = testClassName + "." + testMethodName;
        allTestCases.add(fullTestName);
        
        // Check if this is a known injected fault
        if (!injectedFaults.containsKey(faultName)) {
            logger.debug("Detected fault '{}' is not in the injected faults registry", faultName);
            return;
        }
        
        // Record the detection
        FaultDetection detection = new FaultDetection(
            faultName, 
            testClassName, 
            testMethodName, 
            timestamp, 
            responseBody
        );
        
        detectedFaults.computeIfAbsent(faultName, k -> new ArrayList<>()).add(detection);
        
        logger.info("✅ Detected injected fault: {} in test: {}", faultName, fullTestName);
    }
    
    /**
     * Record a test case execution (for tracking total test count)
     */
    public synchronized void recordTestCase(String testClassName, String testMethodName) {
        String fullTestName = testClassName + "." + testMethodName;
        allTestCases.add(fullTestName);
    }

    /**
     * Record one oracle violation against the parallel anomaly map. Same
     * (oracle, endpointSig, violationSig) tuples collapse into one entry whose
     * hitCount is incremented; the first-seen sample wins (never overwritten)
     * so each anomaly carries a stable, reproducible example.
     *
     * <p>Severity is preserved (ERROR / WARN / INFO) so the report can show
     * what kind of finding it is.
     *
     * <p>Independent of {@link #recordDetectedFault}: a single test may both
     * detect an injected fault by name AND fire an oracle violation; the two
     * appear in their respective sections.
     */
    public synchronized void recordOracleAnomaly(String oracle,
                                                 String endpointSig,
                                                 String violationSig,
                                                 String violationDetail,
                                                 String severity,
                                                 String testClassName,
                                                 String testMethodName,
                                                 String traceId) {
        if (oracle == null || oracle.isEmpty()) return;
        if (endpointSig == null) endpointSig = "";
        if (violationSig == null || violationSig.isEmpty()) return;

        // 0x01 SOH is a control char that can't appear in oracle / endpoint /
        // fingerprint strings, so it's a safer delimiter than "::" (paths or
        // method names could theoretically contain "::").
        String key = oracle + "\u0001" + endpointSig + "\u0001" + violationSig;
        long now = System.currentTimeMillis();
        OracleAnomaly existing = oracleAnomalies.get(key);
        if (existing == null) {
            OracleAnomaly anomaly = new OracleAnomaly(
                    oracle,
                    endpointSig,
                    violationSig,
                    violationDetail == null ? "" : violationDetail,
                    severity == null ? "" : severity,
                    testClassName == null ? "" : testClassName,
                    testMethodName == null ? "" : testMethodName,
                    traceId == null ? "" : traceId,
                    now);
            oracleAnomalies.put(key, anomaly);
        } else {
            existing.hitCount++;
            existing.lastSeenTs = now;
        }
    }

    /**
     * Convenience: record every failing outcome in a TraceShapeOracle verdict
     * against the anomaly map. Used by generated test code, called once per
     * step-trace evaluation. Passing outcomes are skipped.
     *
     * <p>This is the production wiring path. The four invariant classes
     * themselves don't call {@link #recordOracleAnomaly} directly because they
     * emit verdicts (not log lines) and have no test-identity context.
     * Recording at the verdict-consumption site (generated test code, inside
     * {@code attachJaegerTrace}) is where both verdict and identity are in
     * scope. Violations from oracle runs outside the generated-test path
     * (e.g. ad-hoc replay against a stored trace) must call
     * {@link #recordOracleAnomaly} directly.
     */
    public synchronized void recordVerdict(TraceShapeVerdict verdict,
                                           String rootApiKey,
                                           String testClassName,
                                           String testMethodName,
                                           String traceId) {
        recordVerdictInternal(verdict, null, rootApiKey, testClassName, testMethodName,
                traceId, null, null);
    }

    /**
     * Phase 2 part 3 entry point that explicitly carries
     * {@code targetService} + {@code targetParam}. Kept for callers (mainly
     * unit tests) that build a {@link TraceShapeVerdict} without an embedded
     * TARGET_ATTRIBUTION outcome; the implementation falls back to computing
     * attribution inline in that case.
     *
     * <p>Post-FIXES.md F1+F3 the production writer goes through
     * {@code TraceShapeOracle.evaluate(trace, rootApiKey, targetService,
     * targetParam)} which embeds the attribution as a verdict outcome, and
     * then calls the 5-arg overload above — this 8-arg path is an
     * alternative entry kept for backward compatibility with existing tests.
     */
    public synchronized void recordVerdict(TraceShapeVerdict verdict,
                                           TraceModel trace,
                                           String rootApiKey,
                                           String testClassName,
                                           String testMethodName,
                                           String traceId,
                                           String targetService,
                                           String targetParam) {
        recordVerdictInternal(verdict, trace, rootApiKey, testClassName, testMethodName,
                traceId, targetService, targetParam);
    }

    private void recordVerdictInternal(TraceShapeVerdict verdict,
                                       TraceModel trace,
                                       String rootApiKey,
                                       String testClassName,
                                       String testMethodName,
                                       String traceId,
                                       String targetService,
                                       String targetParam) {
        if (verdict == null) return;
        List<TraceShapeVerdict.InvariantOutcome> outcomes = verdict.getOutcomes();
        if (outcomes == null || outcomes.isEmpty()) return;

        // FIXES.md F1+F3: prefer attribution carried by the verdict (set by
        // TargetAttributionInvariant inside TraceShapeOracle). Fall back to
        // computing inline only when the verdict carries no attribution
        // outcome AND the caller provided target context — keeps the
        // 8-arg overload working for unit tests that construct verdicts
        // without the embedded outcome.
        AttributionVerdict attribution = extractAttribution(outcomes);
        if (attribution == null
                && trace != null
                && targetService != null && !targetService.isEmpty()) {
            try {
                attribution = TraceAttribution.attribute(trace, targetService, targetParam);
            } catch (Throwable t) {
                logger.debug("Attribution failed for trace {}: {}", traceId, t.toString());
            }
        }

        for (TraceShapeVerdict.InvariantOutcome o : outcomes) {
            if (o == null || o.passed) continue;
            // TARGET_ATTRIBUTION outcomes are diagnostic classifications,
            // not anomalies. They're consumed above as the attribution
            // bucket key; do not record them as their own anomalies.
            if ("TARGET_ATTRIBUTION".equals(o.kind)) continue;
            String violationSig = fingerprintViolation(o.kind, o.detail);
            recordOracleAnomaly(
                    o.kind,
                    rootApiKey,
                    violationSig,
                    o.detail,
                    o.severity == null ? "" : o.severity.name(),
                    testClassName,
                    testMethodName,
                    traceId);
            if (attribution != null) {
                String key = o.kind + "\u0001" + (rootApiKey == null ? "" : rootApiKey) + "\u0001" + violationSig;
                OracleAnomaly anomaly = oracleAnomalies.get(key);
                if (anomaly != null) {
                    anomaly.attributionCounts.merge(attribution, 1, Integer::sum);
                }
            }
        }
    }

    /**
     * Find the AttributionVerdict embedded in a TARGET_ATTRIBUTION outcome,
     * regardless of its passed flag (TARGET_REJECTION and NO_ATTRIBUTION
     * are informative too — they just classify as not-a-deviation). Returns
     * null when no such outcome is present or its detail isn't a recognized
     * verdict name.
     */
    private static AttributionVerdict extractAttribution(List<TraceShapeVerdict.InvariantOutcome> outcomes) {
        for (TraceShapeVerdict.InvariantOutcome o : outcomes) {
            if (o == null) continue;
            if (!"TARGET_ATTRIBUTION".equals(o.kind)) continue;
            String d = o.detail;
            if (d == null || d.isEmpty()) return null;
            try {
                return AttributionVerdict.valueOf(d);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * Stable fingerprint of an invariant violation. Normalizes whitespace and
     * runs of digits with units (durations, p99 thresholds) so two timing
     * outliers on the same span shape collapse, while structural details
     * (depth, status code, edge endpoints) stay distinguishable.
     *
     * <p>SHA-256 hex; first 16 chars used as a compact, sort-friendly key.
     */
    static String fingerprintViolation(String kind, String detail) {
        String normalized = (detail == null ? "" : detail)
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .trim()
                // Duration literals with unit suffixes — collapse the numeric
                // value so two timing outliers on the same shape share an fp.
                // Order matters: longer units (ns/ms) before "µs"/"us"/"s".
                .replaceAll("\\d+\\s*ns\\b", "<DUR>")
                .replaceAll("\\d+\\s*ms\\b", "<DUR>")
                .replaceAll("\\d+\\s*[µu]s\\b", "<DUR>")
                // Standalone hex blobs of length >= 8 (real Jaeger span IDs
                // are 16 hex chars, trace IDs 32). 8 is the smallest threshold
                // that won't collapse common all-hex English words like "facade"
                // or "decade" (6 chars) into <HEX>.
                .replaceAll("\\b[0-9a-f]{8,}\\b", "<HEX>")
                // Explicit keyed forms (kept for robustness).
                .replaceAll("traceid=[0-9a-f]+", "traceid=<HEX>")
                .replaceAll("spanid=[0-9a-f]+", "spanid=<HEX>");
        String input = (kind == null ? "" : kind) + "::" + normalized;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(16);
            for (int i = 0; i < 8; i++) hex.append(String.format("%02x", hash[i]));
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(input.hashCode());
        }
    }
    
    /**
     * Generate comprehensive fault detection report
     */
    public synchronized void generateReport(String reportDir, String experimentName) {
        if (!initialized) {
            logger.warn("FaultDetectionTracker not initialized. Cannot generate report.");
            return;
        }
        
        try {
            // Create report directory if needed
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
                logger.info("Created fault detection report directory: {}", reportDir);
            }
            
            // Generate report filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String timestamp = dateFormat.format(new Date());
            String reportFileName = String.format("fault-detection-summary-%s-%s.txt", 
                experimentName, timestamp);
            String reportPath = reportDir + "/" + reportFileName;
            
            // Write report
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportPath))) {
                writeReport(writer, experimentName, timestamp);
            }
            
            logger.info("✅ Fault detection report generated: {}", reportPath);
            
        } catch (IOException e) {
            logger.error("Failed to generate fault detection report: {}", e.getMessage(), e);
        }
    }
    
    private void writeReport(PrintWriter writer, String experimentName, String timestamp) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Calculate statistics
        int totalFaults = injectedFaults.size();
        int detectedCount = detectedFaults.size();
        int undetectedCount = totalFaults - detectedCount;
        double detectionRate = totalFaults > 0 ? (detectedCount * 100.0 / totalFaults) : 0.0;
        
        // Header
        writer.println("=".repeat(80));
        writer.println("                    FAULT DETECTION SUMMARY REPORT");
        writer.println("=".repeat(80));
        writer.println();
        writer.println("Experiment:         " + experimentName);
        writer.println("Generated:          " + displayFormat.format(new Date()));
        writer.println("Tracking Started:   " + displayFormat.format(new Date(trackingStartTime)));
        writer.println("Total Test Cases:   " + allTestCases.size());
        writer.println();
        
        // Fault Coverage Summary
        writer.println("=" + "=".repeat(79));
        writer.println("FAULT COVERAGE SUMMARY");
        writer.println("=" + "=".repeat(79));
        writer.println();
        writer.printf("Total Injected Faults:    %d%n", totalFaults);
        writer.printf("Detected Faults:          %d (%.1f%%)%n", detectedCount, detectionRate);
        writer.printf("Undetected Faults:        %d (%.1f%%)%n", undetectedCount, 100.0 - detectionRate);
        writer.println();
        
        // Progress bar visualization
        writer.println("Detection Progress:");
        int barLength = 50;
        int filledLength = (int) (barLength * detectionRate / 100.0);
        String bar = "█".repeat(filledLength) + "░".repeat(barLength - filledLength);
        writer.printf("[%s] %.1f%%%n", bar, detectionRate);
        writer.println();
        
        // Detected Faults Section
        if (!detectedFaults.isEmpty()) {
            writer.println("=" + "=".repeat(79));
            writer.println("DETECTED FAULTS (" + detectedCount + ")");
            writer.println("=" + "=".repeat(79));
            writer.println();
            
            int faultNum = 1;
            List<String> sortedFaultNames = new ArrayList<>(detectedFaults.keySet());
            Collections.sort(sortedFaultNames);
            
            for (String faultName : sortedFaultNames) {
                InjectedFault injectedFault = injectedFaults.get(faultName);
                List<FaultDetection> detections = detectedFaults.get(faultName);
                
                writer.printf("%d. %s%n", faultNum++, faultName);
                writer.printf("   Service:       %s%n", injectedFault.service);
                writer.printf("   API:           %s%n", injectedFault.api);
                writer.printf("   Detections:    %d time(s)%n", detections.size());
                writer.println();
                
                // List all detections with details
                for (int i = 0; i < detections.size(); i++) {
                    FaultDetection detection = detections.get(i);
                    writer.printf("   Detection #%d:%n", i + 1);
                    writer.printf("     Test Class:  %s%n", detection.testClassName);
                    writer.printf("     Test Method: %s%n", detection.testMethodName);
                    writer.printf("     Timestamp:   %s%n", displayFormat.format(new Date(detection.timestamp)));
                    writer.println();
                }
                
                writer.println("-".repeat(80));
                writer.println();
            }
        }
        
        // Undetected Faults Section
        Set<String> undetectedFaultNames = new HashSet<>(injectedFaults.keySet());
        undetectedFaultNames.removeAll(detectedFaults.keySet());
        
        if (!undetectedFaultNames.isEmpty()) {
            writer.println("=" + "=".repeat(79));
            writer.println("UNDETECTED FAULTS (" + undetectedCount + ")");
            writer.println("=" + "=".repeat(79));
            writer.println();
            
            List<String> sortedUndetected = new ArrayList<>(undetectedFaultNames);
            Collections.sort(sortedUndetected);
            
            int faultNum = 1;
            for (String faultName : sortedUndetected) {
                InjectedFault fault = injectedFaults.get(faultName);
                writer.printf("%d. %s%n", faultNum++, faultName);
                writer.printf("   Service:       %s%n", fault.service);
                writer.printf("   API:           %s%n", fault.api);
                writer.println();
            }
        }
        
        // Phase 0: Oracle Anomalies Section — surfaces trace-shape oracle
        // violations that fired but didn't match an injected fault by name.
        // Gated by mist.report.oracle.anomalies.enabled (default true). Set
        // false to reproduce the legacy report shape byte-for-byte.
        boolean anomaliesEnabled = !"false".equalsIgnoreCase(
                System.getProperty("mist.report.oracle.anomalies.enabled", "true"));
        if (anomaliesEnabled && !oracleAnomalies.isEmpty()) {
            List<OracleAnomaly> sorted = new ArrayList<>(oracleAnomalies.values());
            sorted.sort((a, b) -> Integer.compare(b.hitCount, a.hitCount));
            long totalHits = 0L;
            for (OracleAnomaly a : sorted) totalHits += a.hitCount;

            writer.println("=" + "=".repeat(79));
            writer.println("ORACLE ANOMALIES (" + sorted.size() + " distinct, " + totalHits + " hits)");
            writer.println("=" + "=".repeat(79));
            writer.println();
            writer.println("These are TraceShapeOracle invariant violations the tool detected.");
            writer.println("They are tool-detected trace-shape deviations, NOT confirmed bugs —");
            writer.println("an upper-bound bug-finding signal. Entries also matching an injected");
            writer.println("fault name appear in the DETECTED FAULTS section above.");
            writer.println();

            // Phase 2: attribution roll-up. Sums all per-anomaly attribution
            // histograms into a single line so a reviewer sees the
            // tool's confirmed-bug-detection count at a glance, separate
            // from the upper-bound anomaly count above.
            Map<AttributionVerdict, Long> rollup = new EnumMap<>(AttributionVerdict.class);
            for (OracleAnomaly a : sorted) {
                for (Map.Entry<AttributionVerdict, Integer> e : a.attributionCounts.entrySet()) {
                    rollup.merge(e.getKey(), e.getValue().longValue(), Long::sum);
                }
            }
            if (!rollup.isEmpty()) {
                writer.println("Attribution roll-up (target = SUT-confirmed bug-detection event):");
                for (AttributionVerdict v : AttributionVerdict.values()) {
                    Long c = rollup.get(v);
                    if (c != null && c > 0) {
                        writer.printf("  %-22s %d%n", v.name() + ":", c);
                    }
                }
                writer.println();
            }

            int n = 1;
            for (OracleAnomaly a : sorted) {
                writer.printf("%d. %s  |  %s%n", n++, a.oracle,
                        a.endpointSig == null || a.endpointSig.isEmpty() ? "(no endpoint)" : a.endpointSig);
                if (a.severity != null && !a.severity.isEmpty()) {
                    writer.printf("   Severity:      %s%n", a.severity);
                }
                writer.printf("   Violation:     %s%n",
                        a.violationDetail == null || a.violationDetail.isEmpty() ? "(no detail)" : a.violationDetail);
                writer.printf("   Hits:          %d time(s)%n", a.hitCount);
                writer.printf("   First seen:    %s%n", displayFormat.format(new Date(a.firstSeenTs)));
                writer.printf("   Last seen:     %s%n", displayFormat.format(new Date(a.lastSeenTs)));
                // Phase 2: attribution histogram in increasing-confidence order.
                if (!a.attributionCounts.isEmpty()) {
                    StringBuilder attr = new StringBuilder();
                    for (AttributionVerdict v : AttributionVerdict.values()) {
                        Integer c = a.attributionCounts.get(v);
                        if (c != null && c > 0) {
                            if (attr.length() > 0) attr.append(", ");
                            attr.append(v.name()).append(": ").append(c);
                        }
                    }
                    writer.printf("   Attribution:   %s%n", attr);
                }
                if (a.sampleTestClass != null && !a.sampleTestClass.isEmpty()) {
                    writer.printf("   Example test:  %s.%s%n", a.sampleTestClass, a.sampleTestMethod);
                }
                if (a.sampleTraceId != null && !a.sampleTraceId.isEmpty()) {
                    writer.printf("   Example trace: %s%n", a.sampleTraceId);
                }
                writer.println();
            }
        }

        // Test Cases Summary
        if (!allTestCases.isEmpty()) {
            writer.println("=" + "=".repeat(79));
            writer.println("TEST CASES EXECUTED (" + allTestCases.size() + ")");
            writer.println("=" + "=".repeat(79));
            writer.println();
            
            List<String> sortedTestCases = new ArrayList<>(allTestCases);
            Collections.sort(sortedTestCases);
            
            for (int i = 0; i < sortedTestCases.size(); i++) {
                writer.printf("%d. %s%n", i + 1, sortedTestCases.get(i));
            }
            writer.println();
        }
        
        // Footer
        writer.println("=" + "=".repeat(79));
        writer.println("                          END OF REPORT");
        writer.println("=" + "=".repeat(79));
    }
    
    /**
     * Reset tracker state (useful for multiple test runs)
     */
    public synchronized void reset() {
        detectedFaults.clear();
        allTestCases.clear();
        oracleAnomalies.clear();
        trackingStartTime = System.currentTimeMillis();
        logger.info("FaultDetectionTracker reset for new test run");
    }
    
    /**
     * Get detection statistics
     */
    public synchronized Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInjectedFaults", injectedFaults.size());
        stats.put("detectedFaults", detectedFaults.size());
        stats.put("undetectedFaults", injectedFaults.size() - detectedFaults.size());
        stats.put("detectionRate", injectedFaults.size() > 0 
            ? (detectedFaults.size() * 100.0 / injectedFaults.size()) : 0.0);
        stats.put("totalTestCases", allTestCases.size());
        int totalAnomalyHits = 0;
        for (OracleAnomaly a : oracleAnomalies.values()) totalAnomalyHits += a.hitCount;
        stats.put("oracleAnomaliesDistinct", oracleAnomalies.size());
        stats.put("oracleAnomaliesTotalHits", totalAnomalyHits);
        return stats;
    }

    /**
     * Test-only: read-only view of oracle anomalies for assertions.
     */
    Map<String, OracleAnomaly> getOracleAnomaliesForTest() {
        return Collections.unmodifiableMap(oracleAnomalies);
    }
    
    // Inner classes for data structures
    
    private static class InjectedFault {
        final String faultName;
        final String service;
        final String api;
        
        InjectedFault(String faultName, String service, String api) {
            this.faultName = faultName;
            this.service = service;
            this.api = api;
        }
    }
    
    private static class FaultDetection {
        final String faultName;
        final String testClassName;
        final String testMethodName;
        final long timestamp;
        final String responseBody;

        FaultDetection(String faultName, String testClassName, String testMethodName,
                      long timestamp, String responseBody) {
            this.faultName = faultName;
            this.testClassName = testClassName;
            this.testMethodName = testMethodName;
            this.timestamp = timestamp;
            this.responseBody = responseBody;
        }
    }

    /**
     * A trace-shape oracle violation collapsed across all tests that fired
     * the same kind of mismatch against the same endpoint. First-seen sample
     * is preserved; {@link #hitCount} grows with each recurrence.
     */
    static final class OracleAnomaly {
        final String oracle;
        final String endpointSig;
        final String violationSig;
        final String violationDetail;
        final String severity;
        final String sampleTestClass;
        final String sampleTestMethod;
        final String sampleTraceId;
        final long firstSeenTs;
        long lastSeenTs;
        int hitCount;
        // Phase 2: per-anomaly attribution histogram. Each verdict iteration
        // that visits this anomaly increments one bucket. Empty when no
        // negative-target context was passed (positive tests, ad-hoc runs).
        final Map<AttributionVerdict, Integer> attributionCounts = new EnumMap<>(AttributionVerdict.class);

        OracleAnomaly(String oracle, String endpointSig, String violationSig,
                      String violationDetail, String severity,
                      String sampleTestClass, String sampleTestMethod,
                      String sampleTraceId, long ts) {
            this.oracle = oracle;
            this.endpointSig = endpointSig;
            this.violationSig = violationSig;
            this.violationDetail = violationDetail;
            this.severity = severity;
            this.sampleTestClass = sampleTestClass;
            this.sampleTestMethod = sampleTestMethod;
            this.sampleTraceId = sampleTraceId;
            this.firstSeenTs = ts;
            this.lastSeenTs = ts;
            this.hitCount = 1;
        }
    }
}

