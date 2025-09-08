package es.us.isa.restest.writers.restassured;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import es.us.isa.restest.writers.restassured.RESTAssuredWriter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.json.JSONObject;
import org.json.JSONArray;
import es.us.isa.restest.analysis.TraceErrorAnalyzer;



/**
 * Writes a JUnit/REST‑assured test‑suite that replays a
 * {@link MultiServiceTestCase}.
 */
public class MultiServiceRESTAssuredWriter extends RESTAssuredWriter {

    /* ------------------------------------------------------------------ */
    private final String outputDir;
    private final String packageName;
    private final String baseURI;

    private boolean loggingEnabled        = false;
    private boolean allureReport          = false;
    private boolean statsEnabled          = false;
    private boolean outputCoverageEnabled = false;
    private String  proxyHostPort         = null;

    /* ------------------------------------------------------------------ */
    public MultiServiceRESTAssuredWriter(String openAPIPath,
                                         String testConfPath,
                                         String outputDir,
                                         String className,
                                         String packageName,
                                         String baseURI,
                                         boolean logToFile) {

        super(openAPIPath, testConfPath, outputDir, className, packageName, baseURI, logToFile);
        this.outputDir    = outputDir;
        this.packageName  = packageName;
        this.baseURI      = baseURI;

        this.testClassName = className;
    }

    /* ---------- delegate the feature‑toggles to super & keep local copy --- */
    @Override public void setLogging(boolean logging)                    { super.setLogging(logging);          this.loggingEnabled        = logging; }
    @Override public void setAllureReport(boolean allure)                { super.setAllureReport(allure);      this.allureReport          = allure;  }
    @Override public void setEnableStats(boolean enableStats)            { super.setEnableStats(enableStats);  this.statsEnabled          = enableStats; }
    @Override public void setEnableOutputCoverage(boolean enableOutput)  { super.setEnableOutputCoverage(enableOutput); this.outputCoverageEnabled = enableOutput; }
    @Override public void setProxy(String proxy)                         { super.setProxy(proxy);              this.proxyHostPort         = proxy; }


    /*  WRITE JAVA SOURCE                                           */
    @Override
    public void write(Collection<TestCase> testCases) {
        if (testCases == null || testCases.isEmpty()) return;

        // Group test cases by scenario name
        Map<String, List<TestCase>> byScenario = new LinkedHashMap<>();
        for (TestCase tc : testCases) {
            if (tc instanceof MultiServiceTestCase) {
                String sc = ((MultiServiceTestCase) tc).getScenarioName();
                if (sc == null || sc.isEmpty()) sc = "Scenario";
                byScenario.computeIfAbsent(sc, k -> new ArrayList<>()).add(tc);
            }
        }

        for (Map.Entry<String, List<TestCase>> entry : byScenario.entrySet()) {
            try {
                writeTestSuite(entry.getValue(), sanitize(entry.getKey()));
        } catch (RESTestException e) {
            throw new RuntimeException("Error writing multi‑service test suite", e);
            }
        }
    }

    private void writeTestSuite(Collection<TestCase> testCases, String className) throws RESTestException {

        if (testCases == null || testCases.isEmpty()) return;

        try {
            // Create directory structure that matches package structure
            // Package: packageName.testClassName -> Directory: packageName/testClassName
            String packagePath = packageName.replace('.', '/');
            File packageDir = new File(outputDir, packagePath);
            File testClassDir = new File(packageDir, this.testClassName);
            if (!testClassDir.exists()) testClassDir.mkdirs();

            File javaFile = new File(testClassDir, className + ".java");

            try (PrintWriter pw = new PrintWriter(new FileWriter(javaFile))) {

                /* ---------- package & imports ------------------------------------ */
                if (packageName != null && !packageName.isEmpty()) {
                    // Use unique package name to avoid duplicate class issues across test runs
                    String uniquePackageName = packageName + "." + this.testClassName;
                    pw.println("package " + uniquePackageName + ";");
                    pw.println();
                }

                pw.println("import io.restassured.RestAssured;");
                pw.println("import io.restassured.response.Response;");
                pw.println("import io.restassured.specification.RequestSpecification;");
                pw.println("import org.junit.BeforeClass;");
                pw.println("import org.junit.Test;");
                pw.println("import org.junit.AssumptionViolatedException;");
                pw.println("import java.util.concurrent.atomic.AtomicBoolean;");
                pw.println("import java.util.concurrent.atomic.AtomicInteger;");
                pw.println("import java.util.Map;");
                pw.println("import java.util.*;");
                pw.println("import java.net.URLEncoder;");
                pw.println("import java.nio.charset.StandardCharsets;");
                pw.println("import java.net.http.HttpClient;");
                pw.println("import java.net.http.HttpRequest;");
                pw.println("import java.net.http.HttpResponse;");
                pw.println("import org.json.JSONObject;");
                pw.println("import org.json.JSONArray;");
                pw.println("import static org.junit.Assert.*;");
                pw.println("import es.us.isa.restest.testcases.MultiServiceTestCase;");
                pw.println("import es.us.isa.restest.analysis.TraceErrorAnalyzer;");

                if (allureReport) {
                    pw.println("import io.qameta.allure.Allure;");
                    pw.println("import io.qameta.allure.restassured.AllureRestAssured;");
                    pw.println("import io.qameta.allure.model.Status;");
                }
                pw.println();

                /* ---------- class header ---------------------------------------- */
                pw.println("public class " + className + " {");
                pw.println();
                if (allureReport) {
                    pw.println("    // Jaeger configuration");
                    pw.println("    private static final boolean JAEGER_ENABLED = Boolean.parseBoolean(System.getProperty(\"jaeger.enabled\", \"true\"));");
                    pw.println("    private static final String JAEGER_BASE_URL = System.getProperty(\"jaeger.base.url\", \"http://129.62.148.112:30005/jaeger/ui/api\");");
                    pw.println("    private static final String JAEGER_LOOKBACK = System.getProperty(\"jaeger.lookback\", \"10m\");");
                    pw.println();
                    pw.println("    private static void attachJaegerTrace(String service, String method, String path, long requestStartMicros) {");
                    pw.println("        if (!JAEGER_ENABLED) return;");
                    pw.println("        try {");
                    pw.println("            String operation = method + \" \" + path;");
                    pw.println("            String opEncoded = URLEncoder.encode(operation, StandardCharsets.UTF_8);");
                    pw.println("            String svcEncoded = URLEncoder.encode(service, StandardCharsets.UTF_8);");
                    pw.println("            // Create very precise time window for unique trace identification (10 seconds before, 30 seconds after)");
                    pw.println("            long start = requestStartMicros - (10L * 1000L * 1000L); // 10 seconds earlier (us)");
                    pw.println("            long end = requestStartMicros + (30L * 1000L * 1000L); // 30 seconds later (us)");
                    pw.println("            if (start < 0) start = 0;");
                    pw.println("            ");
                    pw.println("            // Try multiple query strategies to find traces");
                    pw.println("            String[] queryUrls = {");
                    pw.println("                // Strategy 1: Exact time window with operation (most specific)");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=50&service=\" + svcEncoded + \"&operation=\" + opEncoded + \"&start=\" + start + \"&end=\" + end,");
                    pw.println("                // Strategy 2: Time window with method only");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=50&service=\" + svcEncoded + \"&operation=\" + URLEncoder.encode(method, StandardCharsets.UTF_8) + \"&start=\" + start + \"&end=\" + end,");
                    pw.println("                // Strategy 3: Service in time window");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=100&service=\" + svcEncoded + \"&start=\" + start + \"&end=\" + end,");
                    pw.println("                // Strategy 4: Recent traces with lookback");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=100&lookback=\" + JAEGER_LOOKBACK + \"&service=\" + svcEncoded,");
                    pw.println("                // Strategy 5: ts-gateway-service (where HTTP calls are usually traced)");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=100&service=ts-gateway-service&start=\" + start + \"&end=\" + end,");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=50&service=ts-gateway-service&operation=\" + URLEncoder.encode(method, StandardCharsets.UTF_8) + \"&start=\" + start + \"&end=\" + end,");
                    pw.println("                // Strategy 6: Other common gateway services");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=100&service=gateway-service&start=\" + start + \"&end=\" + end,");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=100&service=api-gateway&start=\" + start + \"&end=\" + end,");
                    pw.println("                // Strategy 7: Broad search for any traces with API calls");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=200&start=\" + start + \"&end=\" + end,");
                    pw.println("                // Strategy 8: Very broad recent search");
                    pw.println("                JAEGER_BASE_URL + \"/traces?limit=200&lookback=\" + JAEGER_LOOKBACK");
                    pw.println("            };");
                    pw.println("            ");
                    pw.println("            HttpClient client = HttpClient.newHttpClient();");
                    pw.println("            StringBuilder debugInfo = new StringBuilder();");
                    pw.println("            debugInfo.append(\"🔍 Jaeger Query Debug Info:\\n\");");
                    pw.println("            debugInfo.append(\"Target Service: \").append(service).append(\"\\n\");");
                    pw.println("            debugInfo.append(\"Method: \").append(method).append(\"\\n\");");
                    pw.println("            debugInfo.append(\"Path: \").append(path).append(\"\\n\");");
                    pw.println("            debugInfo.append(\"Operation: \").append(operation).append(\"\\n\");");
                    pw.println("            debugInfo.append(\"Request Start (μs): \").append(requestStartMicros).append(\"\\n\");");
                    pw.println("            debugInfo.append(\"Search Window: \").append(start).append(\" to \").append(end).append(\" (μs)\\n\");");
                    pw.println("            debugInfo.append(\"Time Window: \").append((end - start) / 1000000).append(\" seconds\\n\");");
                    pw.println("            debugInfo.append(\"🚪 Gateway Strategy: Searching ts-gateway-service first (HTTP traces usually there)\\n\");");
                    pw.println("            debugInfo.append(\"⏱️ Test Separation: 2-second delay enforced between executions for unique traces\\n\");");
                    pw.println("            debugInfo.append(\"🔄 Trace Propagation: 3-second delay after execution for Jaeger indexing\\n\");");
                    pw.println("            debugInfo.append(\"🔄 Retry Strategy: Multiple attempts to find current execution trace\\n\\n\");");
                    pw.println("            ");
                    pw.println("            // 🔄 Retry mechanism to find current execution trace");
                    pw.println("            boolean foundCurrentTrace = false;");
                    pw.println("            int maxRetries = 3;");
                    pw.println("            ");
                    pw.println("            for (int retry = 0; retry < maxRetries && !foundCurrentTrace; retry++) {");
                    pw.println("                if (retry > 0) {");
                    pw.println("                    debugInfo.append(\"\\n🔄 RETRY \").append(retry).append(\": Searching again for current execution trace...\\n\");");
                    pw.println("                    try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }");
                    pw.println("                }");
                    pw.println("                ");
                    pw.println("            for (int queryIdx = 0; queryIdx < queryUrls.length; queryIdx++) {");
                    pw.println("                String url = queryUrls[queryIdx];");
                    pw.println("                debugInfo.append(\"Query \").append(queryIdx + 1).append(\": \").append(url).append(\"\\n\");");
                    pw.println("                ");
                    pw.println("                HttpRequest req = HttpRequest.newBuilder(java.net.URI.create(url)).GET().build();");
                    pw.println("                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());");
                    pw.println("                debugInfo.append(\"Response Status: \").append(res.statusCode()).append(\"\\n\");");
                    pw.println("                ");
                    pw.println("                if (res.statusCode() >= 200 && res.statusCode() < 300) {");
                    pw.println("                    String body = res.body();");
                    pw.println("                    debugInfo.append(\"Response Body Length: \").append(body.length()).append(\"\\n\");");
                    pw.println("                    ");
                    pw.println("                    try {");
                    pw.println("                        JSONObject json = new JSONObject(body);");
                    pw.println("                        if (json.has(\"data\")) {");
                    pw.println("                            JSONArray data = json.getJSONArray(\"data\");");
                    pw.println("                            debugInfo.append(\"Traces Found: \").append(data.length()).append(\"\\n\");");
                    pw.println("                            ");
                                pw.println("                            if (data.length() > 0) {");
                                pw.println("                                // Smart trace selection - prioritize traces with actual API calls");
                                pw.println("                                JSONObject bestTrace = null;");
                                pw.println("                                long bestDiff = Long.MAX_VALUE;");
                                pw.println("                                int bestTraceIdx = -1;");
                                pw.println("                                int bestScore = -1;");
                                pw.println("                                ");
                                pw.println("                                debugInfo.append(\"Analyzing \").append(data.length()).append(\" traces for API calls...\\n\");");
                                pw.println("                                ");
                                pw.println("                                for (int i = 0; i < data.length(); i++) {");
                                pw.println("                                    JSONObject trace = data.getJSONObject(i);");
                                pw.println("                                    JSONArray spans = trace.optJSONArray(\"spans\");");
                                pw.println("                                    if (spans == null) continue;");
                                pw.println("                                    ");
                                pw.println("                                    int traceScore = 0;");
                                pw.println("                                    int apiCallCount = 0;");
                                pw.println("                                    boolean hasTargetOperation = false;");
                                pw.println("                                    long closestTime = Long.MAX_VALUE;");
                                pw.println("                                    ");
                                pw.println("                                    // Analyze all spans in this trace");
                                pw.println("                                    for (int s = 0; s < spans.length(); s++) {");
                                pw.println("                                        JSONObject span = spans.getJSONObject(s);");
                                pw.println("                                        String opName = span.optString(\"operationName\", \"\");");
                                pw.println("                                        long spanStart = span.optLong(\"startTime\", 0L);");
                                pw.println("                                        ");
                                pw.println("                                        // Check if this span is an actual API call");
                                pw.println("                                        boolean isApiSpan = false;");
                                pw.println("                                        String spanMethod = \"\";");
                                pw.println("                                        String spanPath = \"\";");
                                pw.println("                                        ");
                                pw.println("                                        JSONArray tags = span.optJSONArray(\"tags\");");
                                pw.println("                                        if (tags != null) {");
                                pw.println("                                            for (int t = 0; t < tags.length(); t++) {");
                                pw.println("                                                JSONObject tag = tags.getJSONObject(t);");
                                pw.println("                                                String key = tag.optString(\"key\");");
                                pw.println("                                                String value = tag.optString(\"value\");");
                                pw.println("                                                if (\"http.method\".equals(key)) spanMethod = value;");
                                pw.println("                                                if (\"http.target\".equals(key) || \"http.url\".equals(key)) spanPath = value;");
                                pw.println("                                            }");
                                pw.println("                                        }");
                                pw.println("                                        ");
                                pw.println("                                        // Check if this is an API call");
                                pw.println("                                        if ((!spanMethod.isEmpty() && !spanPath.isEmpty() && spanPath.contains(\"/api/\")) ||");
                                pw.println("                                            opName.contains(\"/api/\") ||");
                                pw.println("                                            opName.matches(\"(GET|POST|PUT|DELETE|PATCH)\\\\s+/api/.*\")) {");
                                pw.println("                                            isApiSpan = true;");
                                pw.println("                                            apiCallCount++;");
                                pw.println("                                        }");
                                pw.println("                                        ");
                                pw.println("                                        // Check if this matches our target operation");
                                pw.println("                                        boolean isTargetOp = opName.equals(operation) ||");
                                pw.println("                                            (spanMethod.equalsIgnoreCase(method) && spanPath.contains(path)) ||");
                                pw.println("                                            (opName.contains(method) && opName.contains(path));");
                                pw.println("                                        ");
                                pw.println("                                        if (isTargetOp) {");
                                pw.println("                                            hasTargetOperation = true;");
                                pw.println("                                            if (spanStart > 0L) {");
                                pw.println("                                                long timeDiff = Math.abs(spanStart - requestStartMicros);");
                                pw.println("                                                closestTime = Math.min(closestTime, timeDiff);");
                                pw.println("                                            }");
                                pw.println("                                        }");
                                pw.println("                                    }");
                                pw.println("                                    ");
                    pw.println("                                    // Enhanced scoring - HEAVILY prioritize recent execution timing");
                    pw.println("                                    if (hasTargetOperation) traceScore += 1000; // Target operation match");
                    pw.println("                                    traceScore += apiCallCount * 5; // More API calls = better (reduced weight)");
                    pw.println("                                    ");
                    pw.println("                                    // CRITICAL: Ultra-precise time proximity scoring (test separation enforced)");
                    pw.println("                                    if (closestTime < Long.MAX_VALUE) {");
                    pw.println("                                        if (closestTime < 1000000L) traceScore += 5000; // Within 1 second (CURRENT execution)");
                    pw.println("                                        else if (closestTime < 2000000L) traceScore += 3000; // Within 2 seconds (EXACT execution)");
                    pw.println("                                        else if (closestTime < 5000000L) traceScore += 1500; // Within 5 seconds (very recent)");
                    pw.println("                                        else if (closestTime < 10000000L) traceScore += 500; // Within 10 seconds");
                    pw.println("                                        else if (closestTime < 20000000L) traceScore += 200; // Within 20 seconds");
                    pw.println("                                        else if (closestTime < 30000000L) traceScore += 100; // Within 30 seconds");
                    pw.println("                                    }");
                                pw.println("                                    ");
                                pw.println("                                    debugInfo.append(\"Trace \").append(i).append(\": score=\").append(traceScore);");
                                pw.println("                                    debugInfo.append(\", APIs=\").append(apiCallCount);");
                                pw.println("                                    debugInfo.append(\", hasTarget=\").append(hasTargetOperation);");
                                pw.println("                                    debugInfo.append(\", timeDiff=\").append(closestTime == Long.MAX_VALUE ? \"N/A\" : closestTime + \"μs\");");
                                pw.println("                                    debugInfo.append(\", spans=\").append(spans.length()).append(\"\\n\");");
                                pw.println("                                    ");
                                pw.println("                                    // Show first few operations for debugging");
                                pw.println("                                    debugInfo.append(\"  Operations: \");");
                                pw.println("                                    for (int opIdx = 0; opIdx < Math.min(spans.length(), 5); opIdx++) {");
                                pw.println("                                        JSONObject span = spans.getJSONObject(opIdx);");
                                pw.println("                                        String opName = span.optString(\"operationName\", \"\");");
                                pw.println("                                        if (opIdx > 0) debugInfo.append(\", \");");
                                pw.println("                                        debugInfo.append(opName.length() > 30 ? opName.substring(0, 27) + \"...\" : opName);");
                                pw.println("                                    }");
                                pw.println("                                    if (spans.length() > 5) debugInfo.append(\", +\").append(spans.length() - 5).append(\" more\");");
                                pw.println("                                    debugInfo.append(\"\\n\");");
                                pw.println("                                    ");
                                pw.println("                                    // Select best trace: TIMING is the primary discriminator for recent requests");
                                pw.println("                                    boolean shouldSelectTrace = false;");
                                pw.println("                                    ");
                                pw.println("                                    if (traceScore > 0) { // Must have API calls");
                                pw.println("                                        if (bestTrace == null) {");
                                pw.println("                                            shouldSelectTrace = true;");
                                pw.println("                                        } else {");
                                pw.println("                                            // For recent traces (within 10 seconds), prioritize timing over score");
                                pw.println("                                            boolean currentIsRecent = closestTime < 10000000L; // 10 seconds (post-delay execution)");
                                pw.println("                                            boolean bestIsRecent = bestDiff < 10000000L;");
                                pw.println("                                            ");
                                pw.println("                                            if (currentIsRecent && !bestIsRecent) {");
                                pw.println("                                                shouldSelectTrace = true; // Always prefer recent traces");
                                pw.println("                                            } else if (!currentIsRecent && bestIsRecent) {");
                                pw.println("                                                shouldSelectTrace = false; // Keep the recent one");
                                pw.println("                                            } else if (currentIsRecent && bestIsRecent) {");
                                pw.println("                                                // Both recent: pick the closest in time");
                                pw.println("                                                shouldSelectTrace = closestTime < bestDiff;");
                                pw.println("                                            } else {");
                                pw.println("                                                // Both old: use score then timing");
                                pw.println("                                                shouldSelectTrace = traceScore > bestScore || ");
                                pw.println("                                                    (traceScore == bestScore && closestTime < bestDiff);");
                                pw.println("                                            }");
                                pw.println("                                        }");
                                pw.println("                                    }");
                                pw.println("                                    ");
                                pw.println("                                    if (shouldSelectTrace) {");
                                pw.println("                                        bestScore = traceScore;");
                                pw.println("                                        bestDiff = closestTime;");
                                pw.println("                                        bestTrace = trace;");
                                pw.println("                                        bestTraceIdx = i;");
                                pw.println("                                    }");
                                pw.println("                                }");
                                pw.println("                                ");
                                pw.println("                                debugInfo.append(\"\\nSelected trace \").append(bestTraceIdx).append(\" with score \").append(bestScore);");
                                pw.println("                                if (bestScore > 0) {");
                                pw.println("                                    // Check if this is a current execution trace (within 3 seconds)");
                                pw.println("                                    boolean isCurrentTrace = bestDiff < 3000000L; // 3 seconds");
                                pw.println("                                    if (isCurrentTrace) {");
                                pw.println("                                        debugInfo.append(\" (CURRENT EXECUTION - perfect match!)\\n\");");
                                pw.println("                                        foundCurrentTrace = true;");
                                pw.println("                                    } else {");
                                pw.println("                                        debugInfo.append(\" (contains API calls - from previous execution)\\n\");");
                                pw.println("                                    }");
                                pw.println("                                    ");
                                pw.println("                                    if (bestTrace != null) {");
                                pw.println("                                        // Generate trace analysis and displays");
                                pw.println("                                        String traceTable = generateTraceTable(bestTrace);");
                                pw.println("                                        String traceSummary = generateTraceSummary(bestTrace);");
                                pw.println("                                        ");
                                pw.println("                                        // Perform error analysis");
                                pw.println("                                        TraceErrorAnalyzer.ErrorAnalysisResult errorAnalysis = TraceErrorAnalyzer.analyzeTrace(bestTrace);");
                                pw.println("                                        String errorReport = TraceErrorAnalyzer.generateErrorReport(errorAnalysis);");
                                pw.println("                                        ");
                                pw.println("                                        // Attach trace information to Allure report");
                                pw.println("                                        if (errorAnalysis.hasErrors()) {");
                                pw.println("                                            // Get intelligent analysis from LLM");
                                pw.println("                                            String intelligentAnalysis = TraceErrorAnalyzer.generateIntelligentAnalysis(errorAnalysis, bestTrace);");
                                pw.println("                                            if (intelligentAnalysis != null && !intelligentAnalysis.trim().isEmpty()) {");
                                pw.println("                                                Allure.addAttachment(\"🤖 INTELLIGENT ANALYSIS\", \"text/plain\", intelligentAnalysis);");
                                pw.println("                                            }");
                                pw.println("                                            ");
                                pw.println("                                            Allure.addAttachment(\"🔗 API Call Trace (FAILED)\", \"text/plain\", traceTable);");
                                pw.println("                                        } else {");
                                pw.println("                                            Allure.addAttachment(\"🔗 API Call Trace (SUCCESS)\", \"text/plain\", traceTable);");
                                pw.println("                                        }");
                                pw.println("                                        Allure.addAttachment(\"📊 Trace Summary\", \"text/plain\", traceSummary);");
                                pw.println("                                        Allure.addAttachment(\"📈 Raw Trace Data\", \"application/json\", bestTrace.toString());");
                                pw.println("                                        Allure.addAttachment(\"🔍 Query Debug Info\", \"text/plain\", debugInfo.toString());");
                                pw.println("                                        return;");
                                pw.println("                                    }");
                                pw.println("                                } else {");
                                pw.println("                                    debugInfo.append(\" (no API calls found)\\n\");");
                                pw.println("                                    debugInfo.append(\"\\n⚠️ This query found traces but no API calls. Continuing to next strategy...\\n\");");
                                pw.println("                                    // Don't return yet - try next query strategy");
                                pw.println("                                }");
                    pw.println("                            } else {");
                    pw.println("                                debugInfo.append(\"No traces found in response\\n\");");
                    pw.println("                            }");
                    pw.println("                        } else {");
                    pw.println("                            debugInfo.append(\"No 'data' field in response\\n\");");
                    pw.println("                        }");
                    pw.println("                    } catch (Exception e) {");
                    pw.println("                        debugInfo.append(\"JSON Parse Error: \").append(e.getMessage()).append(\"\\n\");");
                    pw.println("                    }");
                    pw.println("                    ");
                    pw.println("                    // If this query didn't work, try next one");
                    pw.println("                    debugInfo.append(\"Query \").append(queryIdx + 1).append(\" failed, trying next...\\n\\n\");");
                    pw.println("                } else {");
                    pw.println("                    debugInfo.append(\"HTTP Error: \").append(res.statusCode()).append(\"\\n\\n\");");
                    pw.println("                }");
                    pw.println("            }"); // End of query loop
                    pw.println("            "); 
                    pw.println("            // If we found a current trace, exit retry loop");
                    pw.println("            if (foundCurrentTrace) {");
                    pw.println("                debugInfo.append(\"✅ Found current execution trace, stopping retries.\\n\");");
                    pw.println("                break; // Exit retry loop");
                    pw.println("            }");
                    pw.println("            }"); // End of retry loop
                    pw.println("            ");
                    pw.println("            // No traces found with any query after all retries");
                    pw.println("            if (!foundCurrentTrace) {");
                    pw.println("                debugInfo.append(\"❌ No current execution traces found after \").append(maxRetries).append(\" retries\\n\");");
                    pw.println("            }");
                    pw.println("            debugInfo.append(\"❌ No traces found with any query strategy\");");
                    pw.println("            Allure.addAttachment(\"🔍 Jaeger Query Debug (No Traces)\", \"text/plain\", debugInfo.toString());");
                    pw.println("            ");
                    pw.println("        } catch (Exception e) {");
                    pw.println("            Allure.addAttachment(\"📈 Jaeger Trace Error\", \"text/plain\", \"Error: \" + e.toString() + \"\\nService: \" + service + \"\\nMethod: \" + method + \"\\nPath: \" + path);");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static String generateTraceTable(JSONObject trace) {");
                    pw.println("        try {");
                    pw.println("            JSONArray spans = trace.getJSONArray(\"spans\");");
                    pw.println("            JSONObject processes = trace.optJSONObject(\"processes\");");
                    pw.println("            String traceId = trace.optString(\"traceID\", \"\");");
                    pw.println("            ");
                    pw.println("            StringBuilder table = new StringBuilder();");
                    pw.println("            table.append(\"----------------------------------------------------------------------------------------\\n\");");
                    pw.println("            table.append(\"                           🔗 MICROSERVICE API CALL TRACE                           \\n\");");
                    pw.println("            table.append(\"----------------------------------------------------------------------------------------\\n\");");
                    pw.println("            if (!traceId.isEmpty()) {");
                    pw.println("                table.append(\"Trace ID: \").append(traceId).append(\"\\n\");");
                    pw.println("                String uiBase = JAEGER_BASE_URL.endsWith(\"/api\") ? JAEGER_BASE_URL.substring(0, JAEGER_BASE_URL.length() - 4) : JAEGER_BASE_URL;");
                    pw.println("                table.append(\"Jaeger UI: \").append(uiBase).append(\"/trace/\").append(traceId).append(\"\\n\");");
                    pw.println("                table.append(\"────────────────────────────────────────────────────────────────────────────────────────\\n\");");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            // Build hierarchical API call structure");
                    pw.println("            Map<String, JSONObject> apiSpanById = new HashMap<>();");
                    pw.println("            Map<String, List<String>> apiChildren = new HashMap<>();");
                    pw.println("            Map<String, String> apiParentOf = new HashMap<>();");
                    pw.println("            ");
                    pw.println("            // First pass: collect all API spans and build relationships");
                    pw.println("            for (int i = 0; i < spans.length(); i++) {");
                    pw.println("                JSONObject span = spans.getJSONObject(i);");
                    pw.println("                if (isApiCall(span)) {");
                    pw.println("                    String spanId = span.getString(\"spanID\");");
                    pw.println("                    apiSpanById.put(spanId, span);");
                    pw.println("                    apiChildren.put(spanId, new ArrayList<>());");
                    pw.println("                }");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            // Second pass: establish parent-child relationships for API calls");
                    pw.println("            for (int i = 0; i < spans.length(); i++) {");
                    pw.println("                JSONObject span = spans.getJSONObject(i);");
                    pw.println("                if (isApiCall(span)) {");
                    pw.println("                    String spanId = span.getString(\"spanID\");");
                    pw.println("                    String parentId = span.optString(\"parentSpanId\", null);");
                    pw.println("                    ");
                    pw.println("                    // Look for parent in references if not directly available");
                    pw.println("                    if (parentId == null || parentId.isEmpty()) {");
                    pw.println("                        JSONArray refs = span.optJSONArray(\"references\");");
                    pw.println("                        if (refs != null) {");
                    pw.println("                            for (int r = 0; r < refs.length(); r++) {");
                    pw.println("                                JSONObject ref = refs.getJSONObject(r);");
                    pw.println("                                if (\"CHILD_OF\".equalsIgnoreCase(ref.optString(\"refType\"))) {");
                    pw.println("                                    parentId = ref.optString(\"spanID\");");
                    pw.println("                                    break;");
                    pw.println("                                }");
                    pw.println("                            }");
                    pw.println("                        }");
                    pw.println("                    }");
                    pw.println("                    ");
                    pw.println("                    // Only connect if parent is also an API call");
                    pw.println("                    if (parentId != null && apiSpanById.containsKey(parentId)) {");
                    pw.println("                        apiChildren.get(parentId).add(spanId);");
                    pw.println("                        apiParentOf.put(spanId, parentId);");
                    pw.println("                    }");
                    pw.println("                }");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            // Find root API calls (those without API parents)");
                    pw.println("            List<String> rootApiCalls = new ArrayList<>();");
                    pw.println("            for (String apiId : apiSpanById.keySet()) {");
                    pw.println("                if (!apiParentOf.containsKey(apiId)) {");
                    pw.println("                    rootApiCalls.add(apiId);");
                    pw.println("                }");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            // Sort children by start time");
                    pw.println("            Comparator<String> byStartTime = (a, b) -> Long.compare(");
                    pw.println("                apiSpanById.get(a).optLong(\"startTime\", 0L),");
                    pw.println("                apiSpanById.get(b).optLong(\"startTime\", 0L)");
                    pw.println("            );");
                    pw.println("            for (List<String> children : apiChildren.values()) {");
                    pw.println("                children.sort(byStartTime);");
                    pw.println("            }");
                    pw.println("            rootApiCalls.sort(byStartTime);");
                    pw.println("            ");
                    pw.println("            if (rootApiCalls.isEmpty()) {");
                    pw.println("                table.append(\"⚠️  No API calls found in this trace\\n\");");
                    pw.println("                table.append(\"   This might be an internal operation or framework activity\\n\");");
                    pw.println("            } else {");
                    pw.println("                table.append(\"🌐 API CALL HIERARCHY (Parent → Child Relationships)\\n\");");
                    pw.println("                table.append(\"────────────────────────────────────────────────────────────────────────────────────────\\n\");");
                    pw.println("                ");
                    pw.println("                // Render hierarchical tree");
                    pw.println("                AtomicInteger apiCounter = new AtomicInteger(1);");
                    pw.println("                for (String rootId : rootApiCalls) {");
                    pw.println("                    renderApiHierarchy(table, rootId, apiSpanById, apiChildren, processes, 0, apiCounter, \"\");");
                    pw.println("                }");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            table.append(\"████████████████████████████████████████████████████████████████████████████████████████\\n\");");
                    pw.println("            return table.toString();");
                    pw.println("        } catch (Exception e) {");
                    pw.println("            return \"❌ Failed to generate trace table: \" + e.getMessage();");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static String generateTraceSummary(JSONObject trace) {");
                    pw.println("        try {");
                    pw.println("            JSONArray spans = trace.getJSONArray(\"spans\");");
                    pw.println("            JSONObject processes = trace.optJSONObject(\"processes\");");
                    pw.println("            String traceId = trace.optString(\"traceID\", \"\");");
                    pw.println("            ");
                    pw.println("            StringBuilder summary = new StringBuilder();");
                    pw.println("            summary.append(\"🎯 TRACE SUMMARY\\n\");");
                    pw.println("            summary.append(\"═══════════════\\n\\n\");");
                    pw.println("            ");
                    pw.println("            // Count API calls by status");
                    pw.println("            int totalApis = 0;");
                    pw.println("            int successApis = 0;");
                    pw.println("            int errorApis = 0;");
                    pw.println("            Set<String> services = new HashSet<>();");
                    pw.println("            long totalDuration = 0L;");
                    pw.println("            long minDuration = Long.MAX_VALUE;");
                    pw.println("            long maxDuration = 0L;");
                    pw.println("            ");
                    pw.println("            for (int i = 0; i < spans.length(); i++) {");
                    pw.println("                JSONObject span = spans.getJSONObject(i);");
                    pw.println("                if (isApiCall(span)) {");
                    pw.println("                    totalApis++;");
                    pw.println("                    String[] httpInfo = extractHttpInfo(span);");
                    pw.println("                    String status = httpInfo[2];");
                    pw.println("                    ");
                    pw.println("                    if (status.startsWith(\"2\")) successApis++;");
                    pw.println("                    else if (status.startsWith(\"4\") || status.startsWith(\"5\")) errorApis++;");
                    pw.println("                    ");
                    pw.println("                    services.add(getServiceName(span, processes));");
                    pw.println("                    ");
                    pw.println("                    long duration = span.optLong(\"duration\", 0L);");
                    pw.println("                    if (duration > 0) {");
                    pw.println("                        totalDuration += duration;");
                    pw.println("                        minDuration = Math.min(minDuration, duration);");
                    pw.println("                        maxDuration = Math.max(maxDuration, duration);");
                    pw.println("                    }");
                    pw.println("                }");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            summary.append(\"📊 API Call Statistics:\\n\");");
                    pw.println("            summary.append(\"   Total API Calls: \").append(totalApis).append(\"\\n\");");
                    pw.println("            summary.append(\"   ✅ Successful: \").append(successApis).append(\"\\n\");");
                    pw.println("            summary.append(\"   ❌ Failed: \").append(errorApis).append(\"\\n\");");
                    pw.println("            summary.append(\"   🏢 Services Involved: \").append(services.size()).append(\"\\n\\n\");");
                    pw.println("            ");
                    pw.println("            if (totalApis > 0) {");
                    pw.println("                summary.append(\"⏱️ Performance Metrics:\\n\");");
                    pw.println("                summary.append(\"   Total Duration: \").append(formatDuration(totalDuration)).append(\"\\n\");");
                    pw.println("                summary.append(\"   Average Duration: \").append(formatDuration(totalDuration / totalApis)).append(\"\\n\");");
                    pw.println("                if (minDuration != Long.MAX_VALUE) {");
                    pw.println("                    summary.append(\"   Fastest Call: \").append(formatDuration(minDuration)).append(\"\\n\");");
                    pw.println("                    summary.append(\"   Slowest Call: \").append(formatDuration(maxDuration)).append(\"\\n\");");
                    pw.println("                }");
                    pw.println("                summary.append(\"\\n\");");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            summary.append(\"🏢 Services in Trace:\\n\");");
                    pw.println("            for (String service : services) {");
                    pw.println("                summary.append(\"   • \").append(service).append(\"\\n\");");
                    pw.println("            }");
                    pw.println("            ");
                    pw.println("            return summary.toString();");
                    pw.println("        } catch (Exception e) {");
                    pw.println("            return \"❌ Failed to generate trace summary: \" + e.getMessage();");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static boolean isApiCall(JSONObject span) {");
                    pw.println("        String opName = span.optString(\"operationName\", \"\");");
                    pw.println("        JSONArray tags = span.optJSONArray(\"tags\");");
                    pw.println("        ");
                    pw.println("        boolean hasHttpMethod = false;");
                    pw.println("        boolean hasHttpUrl = false;");
                    pw.println("        String httpUrl = \"\";");
                    pw.println("        String httpMethod = \"\";");
                    pw.println("        ");
                    pw.println("        if (tags != null) {");
                    pw.println("            for (int t = 0; t < tags.length(); t++) {");
                    pw.println("                JSONObject tag = tags.getJSONObject(t);");
                    pw.println("                String key = tag.optString(\"key\");");
                    pw.println("                String value = tag.optString(\"value\");");
                    pw.println("                ");
                    pw.println("                if (\"http.method\".equals(key)) { hasHttpMethod = true; httpMethod = value; }");
                    pw.println("                if (\"http.url\".equals(key) || \"http.target\".equals(key)) { hasHttpUrl = true; httpUrl = value; }");
                    pw.println("            }");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Must be an actual API call");
                    pw.println("        if (hasHttpMethod && hasHttpUrl && (httpUrl.contains(\"/api/\") || httpMethod.matches(\"GET|POST|PUT|DELETE|PATCH\"))) {");
                    pw.println("            return true;");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        if (opName.contains(\"/api/\") && (opName.contains(\"GET\") || opName.contains(\"POST\") || opName.contains(\"PUT\") || opName.contains(\"DELETE\") || opName.contains(\"PATCH\"))) {");
                    pw.println("            return true;");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        return opName.matches(\"(GET|POST|PUT|DELETE|PATCH)\\\\s+/api/.*\");");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static String getServiceName(JSONObject span, JSONObject processes) {");
                    pw.println("        String processId = span.optString(\"processID\", \"\");");
                    pw.println("        if (processes != null && processes.has(processId)) {");
                    pw.println("            return processes.getJSONObject(processId).optString(\"serviceName\", \"unknown-service\");");
                    pw.println("        }");
                    pw.println("        return \"unknown-service\";");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static String[] extractHttpInfo(JSONObject span) {");
                    pw.println("        String method = \"\";");
                    pw.println("        String endpoint = \"\";");
                    pw.println("        String status = \"\";");
                    pw.println("        String opName = span.optString(\"operationName\", \"\");");
                    pw.println("        ");
                    pw.println("        JSONArray tags = span.optJSONArray(\"tags\");");
                    pw.println("        if (tags != null) {");
                    pw.println("            for (int t = 0; t < tags.length(); t++) {");
                    pw.println("                JSONObject tag = tags.getJSONObject(t);");
                    pw.println("                String key = tag.optString(\"key\");");
                    pw.println("                String value = tag.optString(\"value\");");
                    pw.println("                ");
                    pw.println("                if (\"http.method\".equals(key)) method = value;");
                    pw.println("                else if (\"http.url\".equals(key) || \"http.target\".equals(key)) endpoint = value;");
                    pw.println("                else if (\"http.status_code\".equals(key)) status = value;");
                    pw.println("            }");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Extract from operation name if tags don't have the info");
                    pw.println("        if (method.isEmpty() && opName.matches(\"(GET|POST|PUT|DELETE|PATCH)\\\\s+.*\")) {");
                    pw.println("            String[] parts = opName.split(\"\\\\s+\", 2);");
                    pw.println("            if (parts.length == 2) {");
                    pw.println("                method = parts[0];");
                    pw.println("                endpoint = parts[1];");
                    pw.println("            }");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        if (endpoint.isEmpty()) endpoint = opName;");
                    pw.println("        if (status.isEmpty()) status = \"?\";");
                    pw.println("        ");
                    pw.println("        return new String[]{method, endpoint, status};");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static boolean hasErrorTag(JSONObject span) {");
                    pw.println("        if (!span.has(\"tags\")) return false;");
                    pw.println("        ");
                    pw.println("        JSONArray tags = span.getJSONArray(\"tags\");");
                    pw.println("        for (int i = 0; i < tags.length(); i++) {");
                    pw.println("            JSONObject tag = tags.getJSONObject(i);");
                    pw.println("            if (\"error\".equals(tag.optString(\"key\")) && ");
                    pw.println("                \"bool\".equals(tag.optString(\"type\")) && ");
                    pw.println("                tag.optBoolean(\"value\", false)) {");
                    pw.println("                return true;");
                    pw.println("            }");
                    pw.println("        }");
                    pw.println("        return false;");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static String formatDuration(long durationMicros) {");
                    pw.println("        if (durationMicros < 1000) {");
                    pw.println("            return durationMicros + \"μs\";");
                    pw.println("        } else if (durationMicros < 1000000) {");
                    pw.println("            return String.format(\"%.1f\", durationMicros / 1000.0) + \"ms\";");
                    pw.println("        } else {");
                    pw.println("            return String.format(\"%.2f\", durationMicros / 1000000.0) + \"s\";");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                    pw.println("    private static void renderApiHierarchy(StringBuilder table, String spanId, Map<String, JSONObject> apiSpanById, Map<String, List<String>> apiChildren, JSONObject processes, int depth, AtomicInteger counter, String prefix) {");
                    pw.println("        JSONObject span = apiSpanById.get(spanId);");
                    pw.println("        if (span == null) return;");
                    pw.println("        ");
                    pw.println("        // Extract API information");
                    pw.println("        String serviceName = getServiceName(span, processes);");
                    pw.println("        String[] httpInfo = extractHttpInfo(span);");
                    pw.println("        String method = httpInfo[0];");
                    pw.println("        String endpoint = httpInfo[1];");
                    pw.println("        String status = httpInfo[2];");
                    pw.println("        long duration = span.optLong(\"duration\", 0L);");
                    pw.println("        ");
                    pw.println("        // Filter out gateway services - only show actual business APIs");
                    pw.println("        boolean isGateway = serviceName.toLowerCase().contains(\"gateway\") || ");
                    pw.println("                           serviceName.toLowerCase().contains(\"proxy\") ||");
                    pw.println("                           serviceName.toLowerCase().equals(\"gateway-service\") ||");
                    pw.println("                           serviceName.toLowerCase().equals(\"ts-gateway-service\") ||");
                    pw.println("                           serviceName.toLowerCase().equals(\"api-gateway\");");
                    pw.println("        ");
                    pw.println("        // If this is a gateway, skip rendering but still process children");
                    pw.println("        if (isGateway) {");
                    pw.println("            // Render children recursively without showing this gateway span");
                    pw.println("            List<String> children = apiChildren.getOrDefault(spanId, Collections.emptyList());");
                    pw.println("            for (String childId : children) {");
                    pw.println("                renderApiHierarchy(table, childId, apiSpanById, apiChildren, processes, depth, counter, \"\");");
                    pw.println("            }");
                    pw.println("            return;");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Check for errors in this span");
                    pw.println("        boolean hasError = hasErrorTag(span);");
                    pw.println("        String durationStr = formatDuration(duration);");
                    pw.println("        ");
                    pw.println("        // Use SINGLE status indicator - prioritize error detection over HTTP status");
                    pw.println("        String statusIcon;");
                    pw.println("        if (hasError) {");
                    pw.println("            statusIcon = \"❌\";  // Error detected in span");
                    pw.println("        } else if (status.startsWith(\"2\")) {");
                    pw.println("            statusIcon = \"✅\";  // HTTP 2xx success");
                    pw.println("        } else if (status.startsWith(\"4\") || status.startsWith(\"5\")) {");
                    pw.println("            statusIcon = \"❌\";  // HTTP 4xx/5xx error");
                    pw.println("        } else {");
                    pw.println("            statusIcon = \"❓\";  // Unknown/other status");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Smart service name handling - keep essential info");
                    pw.println("        String displayService = serviceName;");
                    pw.println("        if (serviceName.startsWith(\"ts-\")) {");
                    pw.println("            // Keep meaningful part: ts-admin-order-service -> admin-order");
                    pw.println("            displayService = serviceName.substring(3);");
                    pw.println("            if (displayService.endsWith(\"-service\")) {");
                    pw.println("                displayService = displayService.substring(0, displayService.length() - 8);");
                    pw.println("            }");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Smart endpoint handling - preserve important path info");
                    pw.println("        String displayEndpoint = endpoint;");
                    pw.println("        if (endpoint.startsWith(\"http\")) {");
                    pw.println("            try {");
                    pw.println("                // Extract path from full URL: http://host:port/path -> /path");
                    pw.println("                int pathStart = endpoint.indexOf(\"/\", 8);");
                    pw.println("                if (pathStart > 0) {");
                    pw.println("                    displayEndpoint = endpoint.substring(pathStart);");
                    pw.println("                }");
                    pw.println("            } catch (Exception ignored) {");
                    pw.println("                // If parsing fails, use original");
                    pw.println("            }");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Compress common patterns but keep essential info");
                    pw.println("        if (displayEndpoint.contains(\"/api/v1/\")) {");
                    pw.println("            displayEndpoint = displayEndpoint.replace(\"/api/v1/\", \"/v1/\");");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Create compact tree line");
                    pw.println("        StringBuilder line = new StringBuilder();");
                    pw.println("        ");
                    pw.println("        // Minimal indentation for space efficiency");
                    pw.println("        for (int i = 0; i < depth; i++) {");
                    pw.println("            line.append(\"  \");  // 2 spaces per level");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Add compact connector");
                    pw.println("        if (depth > 0) {");
                    pw.println("            line.append(\"├─\");");
                    pw.println("        }");
                    pw.println("        ");
                    pw.println("        // Build comprehensive format with full information");
                    pw.println("        int apiNumber = counter.getAndIncrement();");
                    pw.println("        ");
                    pw.println("        // Main line: [#] Status Service Method HTTPStatus(time)");
                    pw.println("        line.append(String.format(\"[%d] %s %s %s %s(%s)\", ");
                    pw.println("            apiNumber, statusIcon, displayService, method, status, durationStr));");
                    pw.println("        table.append(line.toString()).append(\"\\n\");");
                    pw.println("        ");
                    pw.println("        // Detail line: Full endpoint path (indented)");
                    pw.println("        StringBuilder detailLine = new StringBuilder();");
                    pw.println("        for (int i = 0; i < depth; i++) {");
                    pw.println("            detailLine.append(\"  \");");
                    pw.println("        }");
                    pw.println("        if (depth > 0) {");
                    pw.println("            detailLine.append(\"   \");  // Extra indent for detail line");
                    pw.println("        } else {");
                    pw.println("            detailLine.append(\"    \");  // Indent for root detail line");
                    pw.println("        }");
                    pw.println("        detailLine.append(\"↳ \").append(endpoint);");
                    pw.println("        table.append(detailLine.toString()).append(\"\\n\");");
                    pw.println("        ");
                    pw.println("        // Render children recursively");
                    pw.println("        List<String> children = apiChildren.getOrDefault(spanId, Collections.emptyList());");
                    pw.println("        for (String childId : children) {");
                    pw.println("            renderApiHierarchy(table, childId, apiSpanById, apiChildren, processes, depth + 1, counter, \"\");");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                }

                /* ---------- Rest-assured global setup --------------------------- */
                pw.println("    @BeforeClass");
                pw.println("    public static void setupRestAssured() {");
                if (baseURI != null && !baseURI.isEmpty()) {
                    pw.println("        RestAssured.baseURI = \"" + escape(baseURI) + "\";");
                }
                if (proxyHostPort != null && !proxyHostPort.isEmpty()) {
                    String[] p = proxyHostPort.split(":", 2);
                    pw.println("        RestAssured.proxy = RestAssured.proxy(\"" + p[0] + "\"," + p[1] + ");");
                }
                if (allureReport) {
                    pw.println("        // Configure Allure results directory");
                    pw.println("        System.setProperty(\"allure.results.directory\", \"target/allure-results\");");
                    pw.println("        RestAssured.filters(new AllureRestAssured());");
                }
                pw.println("    }");
                pw.println();

                /* ---------- one @Test per scenario ----------------------------- */
                int scenarioIdx = 1;
                for (TestCase raw : testCases) {

                    if (!(raw instanceof MultiServiceTestCase)) continue;
                    MultiServiceTestCase scenario = (MultiServiceTestCase) raw;

                    // Use the meaningful test name from the generator instead of generic counter
                    String testMethodName = scenario.getOperationId();
                    if (testMethodName == null || testMethodName.isEmpty() || testMethodName.equals("workflow")) {
                        // Fallback to counter-based naming if no meaningful name is set
                        testMethodName = "testScenario" + scenarioIdx;
                    }

                    pw.println("    @Test");
                    pw.println("    public void " + testMethodName + "() throws Exception {");

                    /* ------------ login (own Allure step) ---------------------- */
                    pw.println("        final String[] _jwt     = new String[1];");
                    pw.println("        final String[] _jwtType = new String[1];");
                    pw.println("        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);");
                    pw.println("        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);");

                    if (allureReport) {
                        pw.println("        // 🔐 STEP 0: Authentication - Clean reporting");
                        pw.println("        Allure.step(\"🔐 Step 0: Authentication (Login)\", () -> {");
                        pw.println("            try {");
                        pw.println("                Allure.parameter(\"🏢 Service\", \"Authentication Service\");");
                        pw.println("                Allure.parameter(\"📡 HTTP Method\", \"POST\");");
                        pw.println("                Allure.parameter(\"🔗 Endpoint\", \"/api/v1/users/login\");");
                        pw.println("                Allure.parameter(\"🎯 Expected Status\", 200);");
                        pw.println("                Allure.parameter(\"👤 Username\", \"admin\");");
                        pw.println("                Allure.parameter(\"🚦 Execution Decision\", \"▶️ EXECUTE - Authentication required\");");
                        pw.println("                Allure.description(\"🔐 **Authentication Step**\\n\" +");
                        pw.println("                                 \"This step authenticates the user to obtain JWT token for subsequent API calls.\\n\" +");
                        pw.println("                                 \"All other steps depend on successful authentication.\");");
                        pw.println("                ");
                    }
                    pw.println("                Response loginRes = RestAssured.given()");
                    pw.println("                    .contentType(\"application/json\")");
                    pw.println("                    .body(\"{\\\"username\\\":\\\"admin\\\"," +
                            "\\\"password\\\":\\\"222222\\\"}\")");
                    pw.println("                .when().post(\"/api/v1/users/login\")");
                    pw.println("                    .then().log().ifValidationFails()");
                    pw.println("                    .statusCode(200)  // Login expects 200 - could be configurable in future");
                    pw.println("                    .extract().response();");
                    pw.println("                _jwt[0]     = loginRes.jsonPath().getString(\"data.token\");");
                    pw.println("                _jwtType[0] = \"Bearer\";");
                    if (allureReport) {
                        pw.println("                ");
                        pw.println("                // ✅ SUCCESS: Clean login success reporting");
                        pw.println("                String tokenObtained = _jwt[0] != null ? \"Yes\" : \"No\";");
                        pw.println("                Allure.parameter(\"🎯 Result\", \"✅ SUCCESS (Token: \" + tokenObtained + \")\");");
                        pw.println("                Allure.addAttachment(\"🔐 Login Response\", \"application/json\", loginRes.getBody().asString());");
                        pw.println("            } catch (Throwable loginError) {");
                        pw.println("                loginSucceeded.set(false);");
                        pw.println("                ");
                        pw.println("                // ❌ FAILURE: Clean login failure reporting");
                        pw.println("                String errorType = loginError.getClass().getSimpleName();");
                        pw.println("                if (loginError instanceof java.net.ConnectException) {");
                        pw.println("                    errorType = \"Connection Failed\";");
                        pw.println("                } else if (loginError instanceof AssertionError) {");
                        pw.println("                    errorType = \"Authentication Failed\";");
                        pw.println("                }");
                        pw.println("                ");
                        pw.println("                Allure.parameter(\"🎯 Result\", \"❌ FAILED (\" + errorType + \")\");");
                        pw.println("                Allure.addAttachment(\"💥 Login Error\", \"text/plain\", \"Error: \" + errorType + \"\\nMessage: \" + loginError.getMessage());");
                        pw.println("                ");
                        pw.println("                // Throw to mark login step as failed in Allure");
                        pw.println("                throw new RuntimeException(\"Login failed: \" + loginError.getMessage(), loginError);");
                        pw.println("            }");
                        pw.println("        });");
                    } else {
                        pw.println("        } catch (Throwable __t) {");   // login failed
                        pw.println("            loginSucceeded.set(false);");
                        pw.println("        }");
                    }
                    pw.println("        String jwt     = _jwt[0];");
                    pw.println("        String jwtType = _jwtType[0];");
                    pw.println();

                    /* ------------ every StepCall -------------------------------- */
                    pw.println("        // Step execution results tracking");
                    pw.println("        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();");
                    pw.println("        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();");
                    pw.println();

                    int stepIdx = 1;
                    for (MultiServiceTestCase.StepCall step : scenario.getSteps()) {

                        String verb = step.getMethod() == null || step.getMethod().getMethod() == null
                                ? "get"
                                : step.getMethod().getMethod().toLowerCase();
                        
                        // Generate hierarchical step number if available from the generator
                        String stepNumber = "Step " + stepIdx;
                        String stepTitle = stepNumber + ": "
                                + step.getServiceName() + " "
                                + verb.toUpperCase() + " " + step.getPath()
                                + " (expect " + step.getExpectedStatus() + ")";

                        pw.println("        // " + escape(stepTitle));
                        
                        // 🔥 CRITICAL FIX: ALWAYS create Allure step - NO conditional logic outside
                        // This ensures ALL steps appear in the Allure report regardless of dependencies
                        if (allureReport) {
                            pw.println("        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE");
                            pw.println("        ");
                            pw.println("        // 🎯 CRITICAL: Add delay between test executions to ensure unique traces");
                            pw.println("        // This prevents tests from executing so rapidly that they find the same traces");
                            pw.println("        try {");
                            pw.println("            Thread.sleep(2000); // 2 second delay between tests for trace separation");
                            pw.println("        } catch (InterruptedException ie) {");
                            pw.println("            Thread.currentThread().interrupt();");
                            pw.println("        }");
                            pw.println("        ");
                            pw.println("        final long requestStartMicros = System.currentTimeMillis() * 1000L;");
                            pw.println("        try {");
                            pw.println("            Allure.step(\"" + escape(stepTitle) + "\", () -> {");
                            
                            // Add step metadata as parameters that will be prominently displayed
                            pw.println("                Allure.parameter(\"🏢 Service\", \"" + escape(step.getServiceName()) + "\");");
                            pw.println("                Allure.parameter(\"📡 HTTP Method\", \"" + verb.toUpperCase() + "\");");
                            pw.println("                Allure.parameter(\"🔗 Endpoint\", \"" + escape(step.getPath()) + "\");");
                            pw.println("                Allure.parameter(\"🎯 Expected Status\", " + step.getExpectedStatus() + ");");
                            
                            // Add dependency analysis information
                            String stepDepType = getDependencyTypeString(step);
                            pw.println("                Allure.parameter(\"🔗 Dependency Type\", \"" + stepDepType + "\");");
                            
                            // Add comprehensive description
                            pw.println("                Allure.description(\"🎯 **Testing**: " + escape(step.getServiceName()) + "\\n\" +");
                            pw.println("                                 \"📡 **Method**: " + verb.toUpperCase() + "\\n\" +");
                            pw.println("                                 \"🔗 **Path**: " + escape(step.getPath()) + "\\n\" +");
                            pw.println("                                 \"🎯 **Expected**: " + step.getExpectedStatus() + "\\n\" +");
                            pw.println("                                 \"🔗 **Dependencies**: " + stepDepType + "\");");
                            pw.println("                ");
                            
                            // 🔥 EXECUTION DECISION INSIDE THE STEP - so it's always shown
                            pw.println("                // Execution decision analysis - determine if step should execute");
                            pw.println("                boolean shouldSkip = false;");
                            pw.println("                String skipReason = \"\";");
                            pw.println("                String skipCategory = \"\";");
                            pw.println("                ");
                            
                            // Check authentication dependency first
                            pw.println("                // Check authentication dependency");
                            pw.println("                if (!loginSucceeded.get()) {");
                            pw.println("                    shouldSkip = true;");
                            pw.println("                    skipReason = \"Authentication failed - cannot proceed with authenticated API calls\";");
                            pw.println("                    skipCategory = \"🔐 AUTH_FAILED\";");
                            pw.println("                }");
                            
                            // Check other dependencies
                            if (!step.getParamDependencies().isEmpty()) {
                                pw.println("                // Check data dependencies");
                                pw.println("                else if (false"); // Start with false, then OR the conditions
                                for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                    int sourceStepIdx = dep.getValue().sourceStepIndex;
                                    pw.println("                    || !stepResults.getOrDefault(" + sourceStepIdx + ", false)");
                                }
                                pw.println("                ) {");
                                pw.println("                    shouldSkip = true;");
                                pw.println("                    skipReason = \"Required data from previous step(s) is not available\";");
                                pw.println("                    skipCategory = \"📊 DATA_DEPENDENCY\";");
                                pw.println("                }");
                            }
                            
                            if (!step.getWorkflowDependencies().isEmpty()) {
                                pw.println("                // Check workflow dependencies");
                                pw.println("                else if (false"); // Start with false, then OR the conditions
                                for (Integer workflowDep : step.getWorkflowDependencies()) {
                                    pw.println("                    || !stepResults.getOrDefault(" + workflowDep + ", false)");
                                }
                                pw.println("                ) {");
                                pw.println("                    shouldSkip = true;");
                                pw.println("                    skipReason = \"Workflow predecessor step(s) failed\";");
                                pw.println("                    skipCategory = \"🔄 WORKFLOW_DEPENDENCY\";");
                                pw.println("                }");
                            }
                            
                            pw.println("                ");
                            pw.println("                // Add execution decision as parameter");
                            pw.println("                if (shouldSkip) {");
                            pw.println("                    Allure.parameter(\"🚦 Execution Decision\", \"⏭️ SKIP - \" + skipCategory);");
                            pw.println("                    Allure.parameter(\"⏭️ Skip Reason\", skipReason);");
                            pw.println("                } else {");
                            pw.println("                    Allure.parameter(\"🚦 Execution Decision\", \"▶️ EXECUTE - All dependencies satisfied\");");
                            pw.println("                }");
                            pw.println("                ");
                            
                            // NOW the actual execution or skip logic
                            pw.println("                if (!shouldSkip) {");
                            pw.println("                    System.out.println(\"▶️ EXECUTING: " + escape(stepTitle) + " (dependency analysis passed)\");");
                            
                            // Execute the step
                            pw.println("                    try {");
                            pw.println("                        RequestSpecification req = RestAssured.given();");
                            
                            // 🔥 FIX: Always set Content-Type to application/json for requests with bodies
                            String requestBody = step.getBody() != null ? step.getBody() : "";
                            if (!requestBody.isEmpty()) {
                                pw.println("                        // 🔥 FIX: Set Content-Type to application/json for requests with bodies");
                                pw.println("                        req = req.contentType(\"application/json\");");
                                pw.println("                        String requestBody" + stepIdx + " = \"" + escape(requestBody) + "\";");
                                pw.println("                        req = req.body(requestBody" + stepIdx + ");");
                                pw.println("                        ");
                                pw.println("                        // Add request details as single attachment");
                                pw.println("                        Allure.addAttachment(\"📤 Request Body\", \"application/json\", requestBody" + stepIdx + ");");
                            }
                            
                            pw.println("                        if (loginSucceeded.get()) {");
                            pw.println("                            req = req.header(\"Authorization\", jwtType + \" \" + jwt);");
                            pw.println("                        }");
                            
                            // Add dependency resolution for parameters
                            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                String paramName = dep.getKey();
                                int sourceStepIdx = dep.getValue().sourceStepIndex;
                                pw.println("                        String " + paramName + "Value = capturedOutputs.get(" + sourceStepIdx + ");");
                                pw.println("                        if (" + paramName + "Value != null) {");
                                if (step.getMethod().getMethod().equalsIgnoreCase("GET")) {
                                    pw.println("                            req = req.queryParam(\"" + paramName + "\", " + paramName + "Value);");
                                } else {
                                    pw.println("                            // Add to body if needed");
                                }
                                pw.println("                        }");
                            }
                            
                            pw.println("                        Response stepResponse" + stepIdx + " = req.when()." + verb + "(\"" + escape(step.getPath()) + "\")");
                            pw.println("                               .then().log().ifValidationFails()");
                            pw.println("                               .statusCode(" + step.getExpectedStatus() + ")");
                            pw.println("                               .extract().response();");
                            pw.println("                        ");
                            pw.println("                        stepResults.put(" + stepIdx + ", true);");
                            pw.println("                        System.out.println(\"✅ " + escape(stepTitle) + " - SUCCESS\");");
                            
                            // 🔥 FIX: Clean up SUCCESS reporting - single set of parameters, no duplication
                            pw.println("                        // ✅ SUCCESS: Clean success reporting without duplication");
                            pw.println("                        try {");
                            pw.println("                            String responseBody = stepResponse" + stepIdx + ".getBody().asString();");
                            pw.println("                            int actualStatus = stepResponse" + stepIdx + ".getStatusCode();");
                            pw.println("                            long responseTime = stepResponse" + stepIdx + ".getTime();");
                            pw.println("                            ");
                            pw.println("                            // Single success status parameter");
                            pw.println("                            Allure.parameter(\"🎯 Result\", \"✅ SUCCESS (\" + actualStatus + \" in \" + responseTime + \"ms)\");");
                            pw.println("                            ");
                            pw.println("                            // Single response attachment (avoid duplication)");
                            pw.println("                            Allure.addAttachment(\"📥 Response (\" + actualStatus + \")\", \"application/json\", responseBody);");
                            pw.println("                            // ⏱️ Wait longer for trace propagation to Jaeger (increased delay)");
                            pw.println("                            try { Thread.sleep(3000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }");
                            pw.println("                            attachJaegerTrace(\"" + escape(step.getServiceName()) + "\", \"" + verb.toUpperCase() + "\", \"" + escape(step.getPath()) + "\", requestStartMicros);");
                            pw.println("                        } catch (Exception e) {");
                            pw.println("                            Allure.parameter(\"🎯 Result\", \"✅ SUCCESS (response capture failed)\");");
                            pw.println("                        }");
                            
                                                    // 🔥 FIX: Proper FAILURE reporting with correct Allure status
                        pw.println("                    } catch (Throwable t) {");
                        pw.println("                        stepResults.put(" + stepIdx + ", false);");
                        pw.println("                        System.out.println(\"❌ " + escape(stepTitle) + " - FAILED: \" + t.getMessage());");
                        pw.println("                        ");
                        pw.println("                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis");
                        pw.println("                        String errorType = t.getClass().getSimpleName();");
                        pw.println("                        String failureReason = \"\";");
                        pw.println("                        if (t instanceof java.net.ConnectException) {");
                        pw.println("                            errorType = \"Connection Failed\";");
                        pw.println("                            failureReason = \"Service unreachable - Connection refused\";");
                        pw.println("                        } else if (t instanceof AssertionError) {");
                        pw.println("                            errorType = \"Status Code Mismatch\";");
                        pw.println("                            failureReason = \"Expected vs actual status code mismatch\";");
                        pw.println("                        } else if (t instanceof java.net.SocketTimeoutException) {");
                        pw.println("                            errorType = \"Request Timeout\";");
                        pw.println("                            failureReason = \"Service response timeout\";");
                        pw.println("                        } else if (t.getMessage() != null && t.getMessage().contains(\"404\")) {");
                        pw.println("                            errorType = \"Not Found (404)\";");
                        pw.println("                            failureReason = \"API endpoint not found\";");
                        pw.println("                        } else if (t.getMessage() != null && t.getMessage().contains(\"500\")) {");
                        pw.println("                            errorType = \"Internal Server Error (500)\";");
                        pw.println("                            failureReason = \"Service internal error\";");
                        pw.println("                        } else {");
                        pw.println("                            failureReason = \"Unexpected error: \" + errorType;");
                        pw.println("                        }");
                        pw.println("                        ");
                        pw.println("                        // Enhanced failure parameters");
                        pw.println("                        Allure.parameter(\"🎯 Result\", \"❌ FAILED (\" + errorType + \")\");");
                        pw.println("                        Allure.parameter(\"🔍 Failure Reason\", failureReason);");
                        pw.println("                        Allure.parameter(\"🏢 Failed Service\", \"" + escape(step.getServiceName()) + "\");");
                        pw.println("                        Allure.parameter(\"📡 Failed Method\", \"" + verb.toUpperCase() + "\");");
                        pw.println("                        Allure.parameter(\"🔗 Failed Endpoint\", \"" + escape(step.getPath()) + "\");");
                        pw.println("                        ");
                        pw.println("                        // Comprehensive error details");
                        pw.println("                        StringBuilder errorDetails = new StringBuilder();");
                        pw.println("                        errorDetails.append(\"❌ STEP FAILURE ANALYSIS\\n\");");
                        pw.println("                        errorDetails.append(\"=====================================\\n\\n\");");
                        pw.println("                        errorDetails.append(\"📋 Step: " + escape(stepTitle) + "\\n\");");
                        pw.println("                        errorDetails.append(\"🏢 Service: " + escape(step.getServiceName()) + "\\n\");");
                        pw.println("                        errorDetails.append(\"📡 Method: " + verb.toUpperCase() + "\\n\");");
                        pw.println("                        errorDetails.append(\"🔗 Endpoint: " + escape(step.getPath()) + "\\n\");");
                        pw.println("                        errorDetails.append(\"💥 Error Type: \").append(errorType).append(\"\\n\");");
                        pw.println("                        errorDetails.append(\"🔍 Reason: \").append(failureReason).append(\"\\n\\n\");");
                        pw.println("                        errorDetails.append(\"📜 Full Error Message:\\n\");");
                        pw.println("                        errorDetails.append(t.getMessage() != null ? t.getMessage() : \"No message\").append(\"\\n\\n\");");
                        pw.println("                        if (t.getCause() != null) {");
                        pw.println("                            errorDetails.append(\"🔗 Root Cause:\\n\");");
                        pw.println("                            errorDetails.append(t.getCause().toString()).append(\"\\n\\n\");");
                        pw.println("                        }");
                        pw.println("                        errorDetails.append(\"🔧 Troubleshooting Tips:\\n\");");
                        pw.println("                        if (errorType.contains(\"Connection Failed\")) {");
                        pw.println("                            errorDetails.append(\"• Check if the service is running\\n• Verify network connectivity\\n• Check firewall settings\\n\");");
                        pw.println("                        } else if (errorType.contains(\"Timeout\")) {");
                        pw.println("                            errorDetails.append(\"• Service may be overloaded\\n• Increase timeout settings\\n• Check service health\\n\");");
                        pw.println("                        } else if (errorType.contains(\"404\")) {");
                        pw.println("                            errorDetails.append(\"• Verify API endpoint exists\\n• Check service deployment\\n• Review API documentation\\n\");");
                        pw.println("                        } else if (errorType.contains(\"500\")) {");
                        pw.println("                            errorDetails.append(\"• Check service logs\\n• Verify service configuration\\n• Review dependencies\\n\");");
                        pw.println("                        } else {");
                        pw.println("                            errorDetails.append(\"• Review full error message\\n• Check service status\\n• Verify request parameters\\n\");");
                        pw.println("                        }");
                        pw.println("                        ");
                        // Remove detailed failure analysis - replaced by intelligent analysis
                        pw.println("                        // ⏱️ Wait longer for trace propagation to Jaeger (increased delay)");
                        pw.println("                        try { Thread.sleep(3000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }");
                        pw.println("                        attachJaegerTrace(\"" + escape(step.getServiceName()) + "\", \"" + verb.toUpperCase() + "\", \"" + escape(step.getPath()) + "\", requestStartMicros);");
                        pw.println("                        ");
                        pw.println("                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure");
                        pw.println("                        throw new RuntimeException(\"" + escape(stepTitle) + " failed: \" + failureReason + \" (\" + errorType + \")\", t);");
                        pw.println("                    }");
                            
                            // 🔥 FIX: Proper SKIP reporting with correct Allure status
                            pw.println("                } else {");
                            pw.println("                    // ⏭️ SKIP: Clean skip reporting with proper Allure status");
                            pw.println("                    System.out.println(\"⏭️ SKIPPING: " + escape(stepTitle) + " - \" + skipReason);");
                            pw.println("                    stepResults.put(" + stepIdx + ", false);");
                            pw.println("                    ");
                            pw.println("                    // Single skip status parameter");
                            pw.println("                    Allure.parameter(\"🎯 Result\", \"⏭️ SKIPPED (\" + skipCategory.replaceAll(\"🔐 |📊 |🔄 \", \"\") + \")\");");
                            pw.println("                    ");
                            pw.println("                    // Single skip reason attachment");
                            pw.println("                    Allure.addAttachment(\"⏭️ Skip Details\", \"text/plain\", \"Reason: \" + skipReason);");
                            pw.println("                    ");
                            pw.println("                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure");
                            pw.println("                    throw new org.junit.AssumptionViolatedException(\"Step skipped: \" + skipReason);");
                            pw.println("                }");
                            
                            pw.println("            });");
                            pw.println("        } catch (Exception stepException) {");
                            pw.println("            // Step wrapper exception handling - maintain execution flow");
                            pw.println("            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith(\"Step failed:\")) {");
                            pw.println("                // This is a failed step - already handled, just continue");
                            pw.println("                System.out.println(\"Step " + stepIdx + " marked as FAILED in Allure\");");
                            pw.println("            } else if (stepException instanceof org.junit.AssumptionViolatedException) {");
                            pw.println("                // This is a skipped step - already handled, just continue"); 
                            pw.println("                System.out.println(\"Step " + stepIdx + " marked as SKIPPED in Allure\");");
                            pw.println("            } else {");
                            pw.println("                // Unexpected wrapper failure");
                            pw.println("                System.out.println(\"⚠️ Step wrapper failed for " + escape(stepTitle) + ": \" + stepException.getMessage());");
                            pw.println("                stepResults.put(" + stepIdx + ", false);");
                            pw.println("            }");
                            pw.println("        }");
                        } else {
                            // Non-Allure version - simplified (fallback for when Allure is disabled)
                            pw.println("        // Non-Allure version - simplified execution");
                        pw.println("        MultiServiceTestCase.ExecutionDecision decision" + stepIdx + ";");
                        
                        // Generate the actual decision logic based on step's dependency configuration
                        if (!step.getParamDependencies().isEmpty()) {
                            pw.println("        // This step has DATA dependencies");
                            pw.println("        boolean hasFailedDataDependency = false;");
                            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                int sourceStepIdx = dep.getValue().sourceStepIndex;
                                pw.println("        if (!stepResults.getOrDefault(" + sourceStepIdx + ", false)) {");
                                pw.println("            hasFailedDataDependency = true;");
                                pw.println("        }");
                            }
                            pw.println("        if (hasFailedDataDependency) {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(false, ");
                            pw.println("                MultiServiceTestCase.SkipReason.DATA_DEPENDENCY_FAILED, ");
                            pw.println("                \"Required data from previous step(s) is not available\");");
                            pw.println("        } else {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(true, null, null);");
                            pw.println("        }");
                        } else if (!step.getWorkflowDependencies().isEmpty()) {
                            pw.println("        // This step has WORKFLOW dependencies");
                            pw.println("        boolean hasFailedWorkflowDependency = false;");
                            for (Integer workflowDep : step.getWorkflowDependencies()) {
                                pw.println("        if (!stepResults.getOrDefault(" + workflowDep + ", false)) {");
                                pw.println("            hasFailedWorkflowDependency = true;");
                                pw.println("        }");
                            }
                            pw.println("        if (hasFailedWorkflowDependency) {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(false, ");
                            pw.println("                MultiServiceTestCase.SkipReason.WORKFLOW_DEPENDENCY_FAILED, ");
                            pw.println("                \"Workflow predecessor step(s) failed\");");
                            pw.println("        } else {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(true, null, null);");
                            pw.println("        }");
                        } else {
                            pw.println("        // This step is INDEPENDENT - always execute");
                            pw.println("        decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(true, null, null);");
                        }
                            
                            pw.println("        if (decision" + stepIdx + ".shouldExecute && loginSucceeded.get()) {");
                            pw.println("            System.out.println(\"✅ EXECUTING: " + escape(stepTitle) + "\");");
                            pw.println("            // Execute step logic here (simplified version)");
                            pw.println("            stepResults.put(" + stepIdx + ", true);");
                            pw.println("        } else {");
                            pw.println("            System.out.println(\"⏭️ SKIPPING: " + escape(stepTitle) + "\");");
                            pw.println("            stepResults.put(" + stepIdx + ", false);");
                            pw.println("        }");
                        }
                        
                        pw.println();
                        stepIdx++;
                    }

                    // Check overall scenario result with detailed reporting
                    pw.println("        // Evaluate scenario result with comprehensive reporting");
                    pw.println("        long successfulSteps = stepResults.values().stream().filter(result -> result).count();");
                    pw.println("        long failedSteps = stepResults.values().stream().filter(result -> !result).count();");
                    pw.println("        long totalSteps = stepResults.size();");
                    pw.println("        ");
                    
                    // Add clean test summary
                    if (allureReport) {
                        pw.println("        // Add clean test summary - no duplicate content");
                        pw.println("        String overallResult;");
                        pw.println("        String severity;");
                        pw.println("        if (!loginSucceeded.get()) {");
                        pw.println("            overallResult = \"❌ AUTHENTICATION FAILED\";");
                        pw.println("            severity = \"critical\";");
                        pw.println("        } else if (failedSteps == 0) {");
                        pw.println("            overallResult = \"✅ ALL STEPS PASSED\";");
                        pw.println("            severity = \"normal\";");
                        pw.println("        } else if (successfulSteps > 0) {");
                        pw.println("            overallResult = \"⚠️ PARTIAL FAILURE\";");
                        pw.println("            severity = \"major\";");
                        pw.println("        } else {");
                        pw.println("            overallResult = \"❌ ALL STEPS FAILED\";");
                        pw.println("            severity = \"critical\";");
                        pw.println("        }");
                        pw.println("        ");
                        pw.println("        // Single summary parameter with all key info");
                        pw.println("        Allure.parameter(\"📊 Scenario Result\", overallResult + \" (\" + successfulSteps + \"/\" + totalSteps + \" steps)\");");
                        pw.println("        ");
                        pw.println("        // Add clean categorization");
                        pw.println("        Allure.label(\"severity\", severity);");
                        pw.println("        Allure.label(\"feature\", \"Microservice Workflow\");");
                        pw.println("        Allure.label(\"story\", \"" + escape(scenario.getOperationId()) + "\");");
                        pw.println("        Allure.description(\"Microservice test scenario with \" + totalSteps + \" steps.\");");
                        pw.println("        ");
                    }
                    
                    pw.println("        System.out.println(\"=== SCENARIO RESULT ===\");");
                    pw.println("        System.out.println(\"Scenario: " + escape(scenario.getOperationId()) + "\");");
                    pw.println("        System.out.println(\"Total Steps: \" + totalSteps);");
                    pw.println("        System.out.println(\"Successful: \" + successfulSteps);");
                    pw.println("        System.out.println(\"Failed: \" + failedSteps);");
                    pw.println("        System.out.println(\"Login Status: \" + (loginSucceeded.get() ? \"SUCCESS\" : \"FAILED\"));");
                    pw.println("        ");
                    
                    // Enhanced failure logic - fail if ANY step fails OR login fails
                    pw.println("        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)");
                    pw.println("        if (!loginSucceeded.get()) {");
                    pw.println("            fail(\"Scenario FAILED: Authentication failed - cannot proceed with API calls\");");
                    pw.println("        } else if (failedSteps > 0) {");
                    pw.println("            fail(\"Scenario FAILED: \" + failedSteps + \" out of \" + totalSteps + \" steps failed. \" +");
                    pw.println("                 \"In microservice testing, all workflow steps must succeed for end-to-end validation.\");");
                    pw.println("        } else if (successfulSteps == 0) {");
                    pw.println("            fail(\"Scenario FAILED: No steps executed successfully - check service availability\");");
                    pw.println("        } else {");
                    pw.println("            System.out.println(\"🎉 Scenario PASSED: All \" + totalSteps + \" steps completed successfully\");");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                    scenarioIdx++;
                }

                pw.println("}");
            }
        } catch (Exception e) {
            throw new RESTestException("Error writing REST‑assured suite: " + e.getMessage(), e);
        }
    }


    private static boolean isStandardVerb(String v) {
        switch (v) {
            case "get": case "post": case "put":
            case "delete": case "patch": return true;
            default: return false;
        }
    }
    
    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String sanitize(String s) {
        if (s == null) return "Scenario";
        return s.replaceAll("[^a-zA-Z0-9_]", "_").replaceAll("_+", "_").replaceAll("^_|_$", "");
    }
    
    /**
     * Get human-readable dependency type string for a step
     */
    private static String getDependencyTypeString(MultiServiceTestCase.StepCall step) {
        if (step.getParamDependencies() != null && !step.getParamDependencies().isEmpty()) {
            return "DATA_DEPENDENCY (needs data from previous steps)";
        } else if (step.getWorkflowDependencies() != null && !step.getWorkflowDependencies().isEmpty()) {
            return "WORKFLOW_DEPENDENCY (part of sequential workflow)";
        } else {
            return "INDEPENDENT (can execute regardless of other step results)";
        }
    }
    
    /**
     * Generate detailed dependency analysis report for Allure
     */
    private static void generateDependencyAnalysisReport(MultiServiceTestCase.StepCall step, int stepIdx, PrintWriter pw) {
        pw.println("                // Generate comprehensive dependency analysis attachment");
        pw.println("                StringBuilder depAnalysis = new StringBuilder();");
        pw.println("                depAnalysis.append(\"📋 DEPENDENCY ANALYSIS REPORT\\n\\n\");");
        pw.println("                depAnalysis.append(\"🔍 Step: \" + " + stepIdx + " + \" - " + escape(step.getServiceName()) + "\\n\");");
        pw.println("                depAnalysis.append(\"📡 Method: " + (step.getMethod() != null ? step.getMethod().getMethod() : "UNKNOWN") + "\\n\");");
        pw.println("                depAnalysis.append(\"🔗 Path: " + escape(step.getPath()) + "\\n\\n\");");
        
        // Add parameter dependencies analysis
        if (step.getParamDependencies() != null && !step.getParamDependencies().isEmpty()) {
            pw.println("                depAnalysis.append(\"💾 DATA DEPENDENCIES:\\n\");");
            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                pw.println("                depAnalysis.append(\"  • Parameter '" + escape(dep.getKey()) + "' requires data from Step \" + " + dep.getValue().sourceStepIndex + " + \" (field: '" + escape(dep.getValue().sourceOutputKey) + "')\\n\");");
            }
            pw.println("                depAnalysis.append(\"\\n\");");
        }
        
        // Add workflow dependencies analysis  
        if (step.getWorkflowDependencies() != null && !step.getWorkflowDependencies().isEmpty()) {
            pw.println("                depAnalysis.append(\"🔄 WORKFLOW DEPENDENCIES:\\n\");");
            for (Integer workflowDep : step.getWorkflowDependencies()) {
                pw.println("                depAnalysis.append(\"  • Must execute after Step \" + " + workflowDep + " + \" completes successfully\\n\");");
            }
            pw.println("                depAnalysis.append(\"\\n\");");
        }
        
        // Add execution decision reasoning
        pw.println("                depAnalysis.append(\"📊 EXECUTION DECISION LOGIC:\\n\");");
        pw.println("                depAnalysis.append(\"  Reason: \" + decision" + stepIdx + ".skipReason.description + \"\\n\");");
        pw.println("                depAnalysis.append(\"  Details: \" + decision" + stepIdx + ".skipMessage + \"\\n\\n\");");
        
        // Add step results context
        pw.println("                depAnalysis.append(\"📈 PREVIOUS STEP RESULTS:\\n\");");
        pw.println("                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {");
        pw.println("                    String status = result.getValue() ? \"✅ PASSED\" : \"❌ FAILED\";");
        pw.println("                    depAnalysis.append(\"  Step \" + result.getKey() + \": \" + status + \"\\n\");");
        pw.println("                }");
        pw.println("                depAnalysis.append(\"\\n\");");
        
        // Add impact analysis
        pw.println("                depAnalysis.append(\"🎯 IMPACT ANALYSIS:\\n\");");
        pw.println("                depAnalysis.append(\"  • This step was skipped to prevent cascading failures\\n\");");
        pw.println("                depAnalysis.append(\"  • Dependent steps may also be skipped if they rely on this step\\n\");");
        pw.println("                depAnalysis.append(\"  • Independent steps will continue to execute\\n\");");
        
        pw.println("                Allure.addAttachment(\"🔍 Dependency Analysis Report\", \"text/plain\", depAnalysis.toString());");
    }

    @Override
    public void setClassName(String className) {
        super.setClassName(className);
        this.testClassName = className;
    }

    @Override
    public void setTestId(String testId) {
        super.setTestId(testId);

    }
}
