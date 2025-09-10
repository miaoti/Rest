package trainticket_twostage_test.TrainTicketTwoStageTest_1757286386566;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AssumptionViolatedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;
import static org.junit.Assert.*;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.model.Status;

public class GET_api_v1_adminorderservice_adminorder_1 {

    // Jaeger configuration
    private static final boolean JAEGER_ENABLED = Boolean.parseBoolean(System.getProperty("jaeger.enabled", "true"));
    private static final String JAEGER_BASE_URL = System.getProperty("jaeger.base.url", "http://129.62.148.112:30005/jaeger/ui/api");
    private static final String JAEGER_LOOKBACK = System.getProperty("jaeger.lookback", "10m");

    private static void attachJaegerTrace(String service, String method, String path, long requestStartMicros) {
        if (!JAEGER_ENABLED) return;
        try {
            String operation = method + " " + path;
            String opEncoded = URLEncoder.encode(operation, StandardCharsets.UTF_8);
            String svcEncoded = URLEncoder.encode(service, StandardCharsets.UTF_8);
            // Create time window around the request (2 minutes before, 5 minutes after)
            long start = requestStartMicros - (2L * 60L * 1000L * 1000L); // 2 minutes earlier (us)
            long end = requestStartMicros + (5L * 60L * 1000L * 1000L); // 5 minutes later (us)
            if (start < 0) start = 0;
            
            // Try multiple query strategies to find traces
            String[] queryUrls = {
                // Strategy 1: Exact time window with operation (most specific)
                JAEGER_BASE_URL + "/traces?limit=50&service=" + svcEncoded + "&operation=" + opEncoded + "&start=" + start + "&end=" + end,
                // Strategy 2: Time window with method only
                JAEGER_BASE_URL + "/traces?limit=50&service=" + svcEncoded + "&operation=" + URLEncoder.encode(method, StandardCharsets.UTF_8) + "&start=" + start + "&end=" + end,
                // Strategy 3: Service in time window
                JAEGER_BASE_URL + "/traces?limit=100&service=" + svcEncoded + "&start=" + start + "&end=" + end,
                // Strategy 4: Recent traces with lookback
                JAEGER_BASE_URL + "/traces?limit=100&lookback=" + JAEGER_LOOKBACK + "&service=" + svcEncoded,
                // Strategy 5: ts-gateway-service (where HTTP calls are usually traced)
                JAEGER_BASE_URL + "/traces?limit=100&service=ts-gateway-service&start=" + start + "&end=" + end,
                JAEGER_BASE_URL + "/traces?limit=50&service=ts-gateway-service&operation=" + URLEncoder.encode(method, StandardCharsets.UTF_8) + "&start=" + start + "&end=" + end,
                // Strategy 6: Other common gateway services
                JAEGER_BASE_URL + "/traces?limit=100&service=gateway-service&start=" + start + "&end=" + end,
                JAEGER_BASE_URL + "/traces?limit=100&service=api-gateway&start=" + start + "&end=" + end,
                // Strategy 7: Broad search for any traces with API calls
                JAEGER_BASE_URL + "/traces?limit=200&start=" + start + "&end=" + end,
                // Strategy 8: Very broad recent search
                JAEGER_BASE_URL + "/traces?limit=200&lookback=" + JAEGER_LOOKBACK
            };
            
            HttpClient client = HttpClient.newHttpClient();
            StringBuilder debugInfo = new StringBuilder();
            debugInfo.append("🔍 Jaeger Query Debug Info:\n");
            debugInfo.append("Target Service: ").append(service).append("\n");
            debugInfo.append("Method: ").append(method).append("\n");
            debugInfo.append("Path: ").append(path).append("\n");
            debugInfo.append("Operation: ").append(operation).append("\n");
            debugInfo.append("Request Start (μs): ").append(requestStartMicros).append("\n");
            debugInfo.append("Search Window: ").append(start).append(" to ").append(end).append(" (μs)\n");
            debugInfo.append("Time Window: ").append((end - start) / 1000000).append(" seconds\n");
            debugInfo.append("🚪 Gateway Strategy: Searching ts-gateway-service first (HTTP traces usually there)\n\n");
            
            for (int queryIdx = 0; queryIdx < queryUrls.length; queryIdx++) {
                String url = queryUrls[queryIdx];
                debugInfo.append("Query ").append(queryIdx + 1).append(": ").append(url).append("\n");
                
                HttpRequest req = HttpRequest.newBuilder(java.net.URI.create(url)).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                debugInfo.append("Response Status: ").append(res.statusCode()).append("\n");
                
                if (res.statusCode() >= 200 && res.statusCode() < 300) {
                    String body = res.body();
                    debugInfo.append("Response Body Length: ").append(body.length()).append("\n");
                    
                    try {
                        JSONObject json = new JSONObject(body);
                        if (json.has("data")) {
                            JSONArray data = json.getJSONArray("data");
                            debugInfo.append("Traces Found: ").append(data.length()).append("\n");
                            
                            if (data.length() > 0) {
                                // Smart trace selection - prioritize traces with actual API calls
                                JSONObject bestTrace = null;
                                long bestDiff = Long.MAX_VALUE;
                                int bestTraceIdx = -1;
                                int bestScore = -1;
                                
                                debugInfo.append("Analyzing ").append(data.length()).append(" traces for API calls...\n");
                                
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject trace = data.getJSONObject(i);
                                    JSONArray spans = trace.optJSONArray("spans");
                                    if (spans == null) continue;
                                    
                                    int traceScore = 0;
                                    int apiCallCount = 0;
                                    boolean hasTargetOperation = false;
                                    long closestTime = Long.MAX_VALUE;
                                    
                                    // Analyze all spans in this trace
                                    for (int s = 0; s < spans.length(); s++) {
                                        JSONObject span = spans.getJSONObject(s);
                                        String opName = span.optString("operationName", "");
                                        long spanStart = span.optLong("startTime", 0L);
                                        
                                        // Check if this span is an actual API call
                                        boolean isApiSpan = false;
                                        String spanMethod = "";
                                        String spanPath = "";
                                        
                                        JSONArray tags = span.optJSONArray("tags");
                                        if (tags != null) {
                                            for (int t = 0; t < tags.length(); t++) {
                                                JSONObject tag = tags.getJSONObject(t);
                                                String key = tag.optString("key");
                                                String value = tag.optString("value");
                                                if ("http.method".equals(key)) spanMethod = value;
                                                if ("http.target".equals(key) || "http.url".equals(key)) spanPath = value;
                                            }
                                        }
                                        
                                        // Check if this is an API call
                                        if ((!spanMethod.isEmpty() && !spanPath.isEmpty() && spanPath.contains("/api/")) ||
                                            opName.contains("/api/") ||
                                            opName.matches("(GET|POST|PUT|DELETE|PATCH)\\s+/api/.*")) {
                                            isApiSpan = true;
                                            apiCallCount++;
                                        }
                                        
                                        // Check if this matches our target operation
                                        boolean isTargetOp = opName.equals(operation) ||
                                            (spanMethod.equalsIgnoreCase(method) && spanPath.contains(path)) ||
                                            (opName.contains(method) && opName.contains(path));
                                        
                                        if (isTargetOp) {
                                            hasTargetOperation = true;
                                            if (spanStart > 0L) {
                                                long timeDiff = Math.abs(spanStart - requestStartMicros);
                                                closestTime = Math.min(closestTime, timeDiff);
                                            }
                                        }
                                    }
                                    
                                    // Calculate trace score
                                    if (hasTargetOperation) traceScore += 1000; // Highest priority
                                    traceScore += apiCallCount * 10; // More API calls = better
                                    if (closestTime < Long.MAX_VALUE && closestTime < 300000000L) traceScore += 100; // Within 5 minutes
                                    
                                    debugInfo.append("Trace ").append(i).append(": score=").append(traceScore);
                                    debugInfo.append(", APIs=").append(apiCallCount);
                                    debugInfo.append(", hasTarget=").append(hasTargetOperation);
                                    debugInfo.append(", timeDiff=").append(closestTime == Long.MAX_VALUE ? "N/A" : closestTime + "μs");
                                    debugInfo.append(", spans=").append(spans.length()).append("\n");
                                    
                                    // Show first few operations for debugging
                                    debugInfo.append("  Operations: ");
                                    for (int opIdx = 0; opIdx < Math.min(spans.length(), 5); opIdx++) {
                                        JSONObject span = spans.getJSONObject(opIdx);
                                        String opName = span.optString("operationName", "");
                                        if (opIdx > 0) debugInfo.append(", ");
                                        debugInfo.append(opName.length() > 30 ? opName.substring(0, 27) + "..." : opName);
                                    }
                                    if (spans.length() > 5) debugInfo.append(", +").append(spans.length() - 5).append(" more");
                                    debugInfo.append("\n");
                                    
                                    // Select best trace
                                    if (traceScore > bestScore || (traceScore == bestScore && closestTime < bestDiff)) {
                                        bestScore = traceScore;
                                        bestDiff = closestTime;
                                        bestTrace = trace;
                                        bestTraceIdx = i;
                                    }
                                }
                                
                                debugInfo.append("\nSelected trace ").append(bestTraceIdx).append(" with score ").append(bestScore);
                                if (bestScore > 0) {
                                    debugInfo.append(" (contains API calls)\n");
                                    
                                    if (bestTrace != null) {
                                        String traceTable = generateTraceTable(bestTrace);
                                        String traceSummary = generateTraceSummary(bestTrace);
                                        Allure.addAttachment("🔗 API Call Trace", "text/plain", traceTable);
                                        Allure.addAttachment("📊 Trace Summary", "text/plain", traceSummary);
                                        Allure.addAttachment("📈 Raw Trace Data", "application/json", bestTrace.toString());
                                        Allure.addAttachment("🔍 Query Debug Info", "text/plain", debugInfo.toString());
                                        return;
                                    }
                                } else {
                                    debugInfo.append(" (no API calls found)\n");
                                    debugInfo.append("\n⚠️ This query found traces but no API calls. Continuing to next strategy...\n");
                                    // Don't return yet - try next query strategy
                                }
                            } else {
                                debugInfo.append("No traces found in response\n");
                            }
                        } else {
                            debugInfo.append("No 'data' field in response\n");
                        }
                    } catch (Exception e) {
                        debugInfo.append("JSON Parse Error: ").append(e.getMessage()).append("\n");
                    }
                    
                    // If this query didn't work, try next one
                    debugInfo.append("Query ").append(queryIdx + 1).append(" failed, trying next...\n\n");
                } else {
                    debugInfo.append("HTTP Error: ").append(res.statusCode()).append("\n\n");
                }
            }
            
            // No traces found with any query
            debugInfo.append("❌ No traces found with any query strategy");
            Allure.addAttachment("🔍 Jaeger Query Debug (No Traces)", "text/plain", debugInfo.toString());
            
        } catch (Exception e) {
            Allure.addAttachment("📈 Jaeger Trace Error", "text/plain", "Error: " + e.toString() + "\nService: " + service + "\nMethod: " + method + "\nPath: " + path);
        }
    }

    private static String generateTraceTable(JSONObject trace) {
        try {
            JSONArray spans = trace.getJSONArray("spans");
            JSONObject processes = trace.optJSONObject("processes");
            String traceId = trace.optString("traceID", "");
            
            StringBuilder table = new StringBuilder();
            table.append("████████████████████████████████████████████████████████████████████████████████████████\n");
            table.append("                           🔗 MICROSERVICE API CALL TRACE                           \n");
            table.append("████████████████████████████████████████████████████████████████████████████████████████\n");
            if (!traceId.isEmpty()) {
                table.append("Trace ID: ").append(traceId).append("\n");
                String uiBase = JAEGER_BASE_URL.endsWith("/api") ? JAEGER_BASE_URL.substring(0, JAEGER_BASE_URL.length() - 4) : JAEGER_BASE_URL;
                table.append("Jaeger UI: ").append(uiBase).append("/trace/").append(traceId).append("\n");
                table.append("────────────────────────────────────────────────────────────────────────────────────────\n");
            }
            
            // Build hierarchical API call structure
            Map<String, JSONObject> apiSpanById = new HashMap<>();
            Map<String, List<String>> apiChildren = new HashMap<>();
            Map<String, String> apiParentOf = new HashMap<>();
            
            // First pass: collect all API spans and build relationships
            for (int i = 0; i < spans.length(); i++) {
                JSONObject span = spans.getJSONObject(i);
                if (isApiCall(span)) {
                    String spanId = span.getString("spanID");
                    apiSpanById.put(spanId, span);
                    apiChildren.put(spanId, new ArrayList<>());
                }
            }
            
            // Second pass: establish parent-child relationships for API calls
            for (int i = 0; i < spans.length(); i++) {
                JSONObject span = spans.getJSONObject(i);
                if (isApiCall(span)) {
                    String spanId = span.getString("spanID");
                    String parentId = span.optString("parentSpanId", null);
                    
                    // Look for parent in references if not directly available
                    if (parentId == null || parentId.isEmpty()) {
                        JSONArray refs = span.optJSONArray("references");
                        if (refs != null) {
                            for (int r = 0; r < refs.length(); r++) {
                                JSONObject ref = refs.getJSONObject(r);
                                if ("CHILD_OF".equalsIgnoreCase(ref.optString("refType"))) {
                                    parentId = ref.optString("spanID");
                                    break;
                                }
                            }
                        }
                    }
                    
                    // Only connect if parent is also an API call
                    if (parentId != null && apiSpanById.containsKey(parentId)) {
                        apiChildren.get(parentId).add(spanId);
                        apiParentOf.put(spanId, parentId);
                    }
                }
            }
            
            // Find root API calls (those without API parents)
            List<String> rootApiCalls = new ArrayList<>();
            for (String apiId : apiSpanById.keySet()) {
                if (!apiParentOf.containsKey(apiId)) {
                    rootApiCalls.add(apiId);
                }
            }
            
            // Sort children by start time
            Comparator<String> byStartTime = (a, b) -> Long.compare(
                apiSpanById.get(a).optLong("startTime", 0L),
                apiSpanById.get(b).optLong("startTime", 0L)
            );
            for (List<String> children : apiChildren.values()) {
                children.sort(byStartTime);
            }
            rootApiCalls.sort(byStartTime);
            
            if (rootApiCalls.isEmpty()) {
                table.append("⚠️  No API calls found in this trace\n");
                table.append("   This might be an internal operation or framework activity\n");
            } else {
                table.append("🌐 API CALL HIERARCHY (Parent → Child Relationships)\n");
                table.append("────────────────────────────────────────────────────────────────────────────────────────\n");
                
                // Render hierarchical tree
                AtomicInteger apiCounter = new AtomicInteger(1);
                for (String rootId : rootApiCalls) {
                    renderApiHierarchy(table, rootId, apiSpanById, apiChildren, processes, 0, apiCounter, "");
                }
            }
            
            table.append("████████████████████████████████████████████████████████████████████████████████████████\n");
            return table.toString();
        } catch (Exception e) {
            return "❌ Failed to generate trace table: " + e.getMessage();
        }
    }

    private static String generateTraceSummary(JSONObject trace) {
        try {
            JSONArray spans = trace.getJSONArray("spans");
            JSONObject processes = trace.optJSONObject("processes");
            String traceId = trace.optString("traceID", "");
            
            StringBuilder summary = new StringBuilder();
            summary.append("🎯 TRACE SUMMARY\n");
            summary.append("═══════════════\n\n");
            
            // Count API calls by status
            int totalApis = 0;
            int successApis = 0;
            int errorApis = 0;
            Set<String> services = new HashSet<>();
            long totalDuration = 0L;
            long minDuration = Long.MAX_VALUE;
            long maxDuration = 0L;
            
            for (int i = 0; i < spans.length(); i++) {
                JSONObject span = spans.getJSONObject(i);
                if (isApiCall(span)) {
                    totalApis++;
                    String[] httpInfo = extractHttpInfo(span);
                    String status = httpInfo[2];
                    
                    if (status.startsWith("2")) successApis++;
                    else if (status.startsWith("4") || status.startsWith("5")) errorApis++;
                    
                    services.add(getServiceName(span, processes));
                    
                    long duration = span.optLong("duration", 0L);
                    if (duration > 0) {
                        totalDuration += duration;
                        minDuration = Math.min(minDuration, duration);
                        maxDuration = Math.max(maxDuration, duration);
                    }
                }
            }
            
            summary.append("📊 API Call Statistics:\n");
            summary.append("   Total API Calls: ").append(totalApis).append("\n");
            summary.append("   ✅ Successful: ").append(successApis).append("\n");
            summary.append("   ❌ Failed: ").append(errorApis).append("\n");
            summary.append("   🏢 Services Involved: ").append(services.size()).append("\n\n");
            
            if (totalApis > 0) {
                summary.append("⏱️ Performance Metrics:\n");
                summary.append("   Total Duration: ").append(formatDuration(totalDuration)).append("\n");
                summary.append("   Average Duration: ").append(formatDuration(totalDuration / totalApis)).append("\n");
                if (minDuration != Long.MAX_VALUE) {
                    summary.append("   Fastest Call: ").append(formatDuration(minDuration)).append("\n");
                    summary.append("   Slowest Call: ").append(formatDuration(maxDuration)).append("\n");
                }
                summary.append("\n");
            }
            
            summary.append("🏢 Services in Trace:\n");
            for (String service : services) {
                summary.append("   • ").append(service).append("\n");
            }
            
            return summary.toString();
        } catch (Exception e) {
            return "❌ Failed to generate trace summary: " + e.getMessage();
        }
    }

    private static boolean isApiCall(JSONObject span) {
        String opName = span.optString("operationName", "");
        JSONArray tags = span.optJSONArray("tags");
        
        boolean hasHttpMethod = false;
        boolean hasHttpUrl = false;
        String httpUrl = "";
        String httpMethod = "";
        
        if (tags != null) {
            for (int t = 0; t < tags.length(); t++) {
                JSONObject tag = tags.getJSONObject(t);
                String key = tag.optString("key");
                String value = tag.optString("value");
                
                if ("http.method".equals(key)) { hasHttpMethod = true; httpMethod = value; }
                if ("http.url".equals(key) || "http.target".equals(key)) { hasHttpUrl = true; httpUrl = value; }
            }
        }
        
        // Must be an actual API call
        if (hasHttpMethod && hasHttpUrl && (httpUrl.contains("/api/") || httpMethod.matches("GET|POST|PUT|DELETE|PATCH"))) {
            return true;
        }
        
        if (opName.contains("/api/") && (opName.contains("GET") || opName.contains("POST") || opName.contains("PUT") || opName.contains("DELETE") || opName.contains("PATCH"))) {
            return true;
        }
        
        return opName.matches("(GET|POST|PUT|DELETE|PATCH)\\s+/api/.*");
    }

    private static String getServiceName(JSONObject span, JSONObject processes) {
        String processId = span.optString("processID", "");
        if (processes != null && processes.has(processId)) {
            return processes.getJSONObject(processId).optString("serviceName", "unknown-service");
        }
        return "unknown-service";
    }

    private static String[] extractHttpInfo(JSONObject span) {
        String method = "";
        String endpoint = "";
        String status = "";
        String opName = span.optString("operationName", "");
        
        JSONArray tags = span.optJSONArray("tags");
        if (tags != null) {
            for (int t = 0; t < tags.length(); t++) {
                JSONObject tag = tags.getJSONObject(t);
                String key = tag.optString("key");
                String value = tag.optString("value");
                
                if ("http.method".equals(key)) method = value;
                else if ("http.url".equals(key) || "http.target".equals(key)) endpoint = value;
                else if ("http.status_code".equals(key)) status = value;
            }
        }
        
        // Extract from operation name if tags don't have the info
        if (method.isEmpty() && opName.matches("(GET|POST|PUT|DELETE|PATCH)\\s+.*")) {
            String[] parts = opName.split("\\s+", 2);
            if (parts.length == 2) {
                method = parts[0];
                endpoint = parts[1];
            }
        }
        
        if (endpoint.isEmpty()) endpoint = opName;
        if (status.isEmpty()) status = "?";
        
        return new String[]{method, endpoint, status};
    }

    private static String formatDuration(long durationMicros) {
        if (durationMicros < 1000) {
            return durationMicros + "μs";
        } else if (durationMicros < 1000000) {
            return String.format("%.1f", durationMicros / 1000.0) + "ms";
        } else {
            return String.format("%.2f", durationMicros / 1000000.0) + "s";
        }
    }

    private static void renderApiHierarchy(StringBuilder table, String spanId, Map<String, JSONObject> apiSpanById, Map<String, List<String>> apiChildren, JSONObject processes, int depth, AtomicInteger counter, String prefix) {
        JSONObject span = apiSpanById.get(spanId);
        if (span == null) return;
        
        // Extract API information
        String serviceName = getServiceName(span, processes);
        String[] httpInfo = extractHttpInfo(span);
        String method = httpInfo[0];
        String endpoint = httpInfo[1];
        String status = httpInfo[2];
        long duration = span.optLong("duration", 0L);
        String durationStr = formatDuration(duration);
        
        // Format for compact display
        String statusIcon = status.startsWith("2") ? "✅" : status.startsWith("4") || status.startsWith("5") ? "❌" : "❓";
        
        // Smart service name handling - keep essential info
        String displayService = serviceName;
        if (serviceName.startsWith("ts-")) {
            // Keep meaningful part: ts-admin-order-service -> admin-order
            displayService = serviceName.substring(3);
            if (displayService.endsWith("-service")) {
                displayService = displayService.substring(0, displayService.length() - 8);
            }
        }
        
        // Smart endpoint handling - preserve important path info
        String displayEndpoint = endpoint;
        if (endpoint.startsWith("http")) {
            try {
                // Extract path from full URL: http://host:port/path -> /path
                int pathStart = endpoint.indexOf("/", 8);
                if (pathStart > 0) {
                    displayEndpoint = endpoint.substring(pathStart);
                }
            } catch (Exception ignored) {
                // If parsing fails, use original
            }
        }
        
        // Compress common patterns but keep essential info
        if (displayEndpoint.contains("/api/v1/")) {
            displayEndpoint = displayEndpoint.replace("/api/v1/", "/v1/");
        }
        
        // Create compact tree line
        StringBuilder line = new StringBuilder();
        
        // Minimal indentation for space efficiency
        for (int i = 0; i < depth; i++) {
            line.append("  ");  // 2 spaces per level
        }
        
        // Add compact connector
        if (depth > 0) {
            line.append("├─");
        }
        
        // Build comprehensive format with full information
        int apiNumber = counter.getAndIncrement();
        
        // Main line: [#] Service Method Status(time)
        line.append(String.format("[%d] %s %s %s %s(%s)", 
            apiNumber, displayService, method, statusIcon, status, durationStr));
        table.append(line.toString()).append("\n");
        
        // Detail line: Full endpoint path (indented)
        StringBuilder detailLine = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            detailLine.append("  ");
        }
        if (depth > 0) {
            detailLine.append("   ");  // Extra indent for detail line
        } else {
            detailLine.append("    ");  // Indent for root detail line
        }
        detailLine.append("↳ ").append(endpoint);
        table.append(detailLine.toString()).append("\n");
        
        // Render children recursively
        List<String> children = apiChildren.getOrDefault(spanId, Collections.emptyList());
        for (String childId : children) {
            renderApiHierarchy(table, childId, apiSpanById, apiChildren, processes, depth + 1, counter, "");
        }
    }

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_GET_1_1() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_1");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_1");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_2() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_2");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_2");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_3() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_3");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_3");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_4() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_4");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_4");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_5() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_5");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_5");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_6() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_6");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_6");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_7() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_7");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_7");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_8() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_8");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_8");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_9() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_9");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_9");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

    @Test
    public void test_GET_1_10() throws Exception {
        final String[] _jwt     = new String[1];
        final String[] _jwtType = new String[1];
        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);
        // 🔐 STEP 0: Authentication - Clean reporting
        Allure.step("🔐 Step 0: Authentication (Login)", () -> {
            try {
                Allure.parameter("🏢 Service", "Authentication Service");
                Allure.parameter("📡 HTTP Method", "POST");
                Allure.parameter("🔗 Endpoint", "/api/v1/users/login");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("👤 Username", "admin");
                Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - Authentication required");
                Allure.description("🔐 **Authentication Step**\n" +
                                 "This step authenticates the user to obtain JWT token for subsequent API calls.\n" +
                                 "All other steps depend on successful authentication.");
                
                Response loginRes = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"222222\"}")
                .when().post("/api/v1/users/login")
                    .then().log().ifValidationFails()
                    .statusCode(200)  // Login expects 200 - could be configurable in future
                    .extract().response();
                _jwt[0]     = loginRes.jsonPath().getString("data.token");
                _jwtType[0] = "Bearer";
                
                // ✅ SUCCESS: Clean login success reporting
                String tokenObtained = _jwt[0] != null ? "Yes" : "No";
                Allure.parameter("🎯 Result", "✅ SUCCESS (Token: " + tokenObtained + ")");
                Allure.addAttachment("🔐 Login Response", "application/json", loginRes.getBody().asString());
            } catch (Throwable loginError) {
                loginSucceeded.set(false);
                
                // ❌ FAILURE: Clean login failure reporting
                String errorType = loginError.getClass().getSimpleName();
                if (loginError instanceof java.net.ConnectException) {
                    errorType = "Connection Failed";
                } else if (loginError instanceof AssertionError) {
                    errorType = "Authentication Failed";
                }
                
                Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                Allure.addAttachment("💥 Login Error", "text/plain", "Error: " + errorType + "\nMessage: " + loginError.getMessage());
                
                // Throw to mark login step as failed in Allure
                throw new RuntimeException("Login failed: " + loginError.getMessage(), loginError);
            }
        });
        String jwt     = _jwt[0];
        String jwtType = _jwtType[0];

        // Step execution results tracking
        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();
        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();

        // Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)", () -> {
                Allure.parameter("🏢 Service", "ts-admin-order-service");
                Allure.parameter("📡 HTTP Method", "GET");
                Allure.parameter("🔗 Endpoint", "/api/v1/adminorderservice/adminorder");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-order-service\n" +
                                 "📡 **Method**: GET\n" +
                                 "🔗 **Path**: /api/v1/adminorderservice/adminorder\n" +
                                 "🎯 **Expected**: 200\n" +
                                 "🔗 **Dependencies**: INDEPENDENT (can execute regardless of other step results)");
                
                // Execution decision analysis - determine if step should execute
                boolean shouldSkip = false;
                String skipReason = "";
                String skipCategory = "";
                
                // Check authentication dependency
                if (!loginSucceeded.get()) {
                    shouldSkip = true;
                    skipReason = "Authentication failed - cannot proceed with authenticated API calls";
                    skipCategory = "🔐 AUTH_FAILED";
                }
                
                // Add execution decision as parameter
                if (shouldSkip) {
                    Allure.parameter("🚦 Execution Decision", "⏭️ SKIP - " + skipCategory);
                    Allure.parameter("⏭️ Skip Reason", skipReason);
                } else {
                    Allure.parameter("🚦 Execution Decision", "▶️ EXECUTE - All dependencies satisfied");
                }
                
                if (!shouldSkip) {
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) (dependency analysis passed)");
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        Response stepResponse1 = req.when().get("/api/v1/adminorderservice/adminorder")
                               .then().log().ifValidationFails()
                               .statusCode(200)
                               .extract().response();
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Single response attachment (avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - FAILED: " + t.getMessage());
                        
                        // ❌ FAILURE: Enhanced failure reporting with detailed analysis
                        String errorType = t.getClass().getSimpleName();
                        String failureReason = "";
                        if (t instanceof java.net.ConnectException) {
                            errorType = "Connection Failed";
                            failureReason = "Service unreachable - Connection refused";
                        } else if (t instanceof AssertionError) {
                            errorType = "Status Code Mismatch";
                            failureReason = "Expected vs actual status code mismatch";
                        } else if (t instanceof java.net.SocketTimeoutException) {
                            errorType = "Request Timeout";
                            failureReason = "Service response timeout";
                        } else if (t.getMessage() != null && t.getMessage().contains("404")) {
                            errorType = "Not Found (404)";
                            failureReason = "API endpoint not found";
                        } else if (t.getMessage() != null && t.getMessage().contains("500")) {
                            errorType = "Internal Server Error (500)";
                            failureReason = "Service internal error";
                        } else {
                            failureReason = "Unexpected error: " + errorType;
                        }
                        
                        // Enhanced failure parameters
                        Allure.parameter("🎯 Result", "❌ FAILED (" + errorType + ")");
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-order-service");
                        Allure.parameter("📡 Failed Method", "GET");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/adminorderservice/adminorder");
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200)\n");
                        errorDetails.append("🏢 Service: ts-admin-order-service\n");
                        errorDetails.append("📡 Method: GET\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/adminorderservice/adminorder\n");
                        errorDetails.append("💥 Error Type: ").append(errorType).append("\n");
                        errorDetails.append("🔍 Reason: ").append(failureReason).append("\n\n");
                        errorDetails.append("📜 Full Error Message:\n");
                        errorDetails.append(t.getMessage() != null ? t.getMessage() : "No message").append("\n\n");
                        if (t.getCause() != null) {
                            errorDetails.append("🔗 Root Cause:\n");
                            errorDetails.append(t.getCause().toString()).append("\n\n");
                        }
                        errorDetails.append("🔧 Troubleshooting Tips:\n");
                        if (errorType.contains("Connection Failed")) {
                            errorDetails.append("• Check if the service is running\n• Verify network connectivity\n• Check firewall settings\n");
                        } else if (errorType.contains("Timeout")) {
                            errorDetails.append("• Service may be overloaded\n• Increase timeout settings\n• Check service health\n");
                        } else if (errorType.contains("404")) {
                            errorDetails.append("• Verify API endpoint exists\n• Check service deployment\n• Review API documentation\n");
                        } else if (errorType.contains("500")) {
                            errorDetails.append("• Check service logs\n• Verify service configuration\n• Review dependencies\n");
                        } else {
                            errorDetails.append("• Review full error message\n• Check service status\n• Verify request parameters\n");
                        }
                        
                        Allure.addAttachment("💥 Detailed Failure Analysis", "text/plain", errorDetails.toString());
                        attachJaegerTrace("ts-admin-order-service", "GET", "/api/v1/adminorderservice/adminorder", requestStartMicros);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200) - " + skipReason);
                    stepResults.put(1, false);
                    
                    // Single skip status parameter
                    Allure.parameter("🎯 Result", "⏭️ SKIPPED (" + skipCategory.replaceAll("🔐 |📊 |🔄 ", "") + ")");
                    
                    // Single skip reason attachment
                    Allure.addAttachment("⏭️ Skip Details", "text/plain", "Reason: " + skipReason);
                    
                    // 🔥 CRITICAL: Throw Assumption exception to mark step as SKIPPED (yellow arrow) in Allure
                    throw new org.junit.AssumptionViolatedException("Step skipped: " + skipReason);
                }
            });
        } catch (Exception stepException) {
            // Step wrapper exception handling - maintain execution flow
            if (stepException instanceof RuntimeException && stepException.getMessage().startsWith("Step failed:")) {
                // This is a failed step - already handled, just continue
                System.out.println("Step 1 marked as FAILED in Allure");
            } else if (stepException instanceof org.junit.AssumptionViolatedException) {
                // This is a skipped step - already handled, just continue
                System.out.println("Step 1 marked as SKIPPED in Allure");
            } else {
                // Unexpected wrapper failure
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-order-service GET /api/v1/adminorderservice/adminorder (expect 200): " + stepException.getMessage());
                stepResults.put(1, false);
            }
        }

        // Evaluate scenario result with comprehensive reporting
        long successfulSteps = stepResults.values().stream().filter(result -> result).count();
        long failedSteps = stepResults.values().stream().filter(result -> !result).count();
        long totalSteps = stepResults.size();
        
        // Add clean test summary - no duplicate content
        String overallResult;
        String severity;
        if (!loginSucceeded.get()) {
            overallResult = "❌ AUTHENTICATION FAILED";
            severity = "critical";
        } else if (failedSteps == 0) {
            overallResult = "✅ ALL STEPS PASSED";
            severity = "normal";
        } else if (successfulSteps > 0) {
            overallResult = "⚠️ PARTIAL FAILURE";
            severity = "major";
        } else {
            overallResult = "❌ ALL STEPS FAILED";
            severity = "critical";
        }
        
        // Single summary parameter with all key info
        Allure.parameter("📊 Scenario Result", overallResult + " (" + successfulSteps + "/" + totalSteps + " steps)");
        
        // Add clean categorization
        Allure.label("severity", severity);
        Allure.label("feature", "Microservice Workflow");
        Allure.label("story", "test_GET_1_10");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_GET_1_10");
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Successful: " + successfulSteps);
        System.out.println("Failed: " + failedSteps);
        System.out.println("Login Status: " + (loginSucceeded.get() ? "SUCCESS" : "FAILED"));
        
        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)
        if (!loginSucceeded.get()) {
            fail("Scenario FAILED: Authentication failed - cannot proceed with API calls");
        } else if (failedSteps > 0) {
            fail("Scenario FAILED: " + failedSteps + " out of " + totalSteps + " steps failed. " +
                 "In microservice testing, all workflow steps must succeed for end-to-end validation.");
        } else if (successfulSteps == 0) {
            fail("Scenario FAILED: No steps executed successfully - check service availability");
        } else {
            System.out.println("🎉 Scenario PASSED: All " + totalSteps + " steps completed successfully");
        }
    }

}
