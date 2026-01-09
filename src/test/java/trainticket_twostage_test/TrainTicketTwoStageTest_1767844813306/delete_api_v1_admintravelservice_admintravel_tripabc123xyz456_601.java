package trainticket_twostage_test.TrainTicketTwoStageTest_1767844813306;

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
import org.hamcrest.Matchers;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;
import es.us.isa.restest.analysis.TraceErrorAnalyzer;
import es.us.isa.restest.inputs.smart.ParameterErrorAnalyzer;
import es.us.isa.restest.inputs.smart.InputFetchRegistry;
import es.us.isa.restest.inputs.smart.ParameterError;
import static org.junit.Assert.*;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

public class delete_api_v1_admintravelservice_admintravel_tripabc123xyz456_601 {

    // Jaeger configuration
    private static final boolean JAEGER_ENABLED = Boolean.parseBoolean(System.getProperty("jaeger.enabled", "true"));
    // ⚠️ CRITICAL: Use /traces/{id} endpoint to fetch COMPLETE trace with all spans
    // /traces endpoint returns list with summaries (limited spans)
    // /traces/{id} endpoint returns COMPLETE trace tree with all spans
    private static final String JAEGER_BASE_URL = System.getProperty("jaeger.base.url", "http://129.62.148.112:30005/jaeger/ui/api");
    private static final String JAEGER_LOOKBACK = System.getProperty("jaeger.lookback", "10m");

    private static void attachJaegerTrace(String service, String method, String path, long requestStartMicros, Map<String, String> stepParameters, boolean isStepFailed) {
        if (!JAEGER_ENABLED) return;
        try {
            String operation = method + " " + path;
            String opEncoded = URLEncoder.encode(operation, StandardCharsets.UTF_8);
            String svcEncoded = URLEncoder.encode(service, StandardCharsets.UTF_8);
            // Create very precise time window for unique trace identification (10 seconds before, 30 seconds after)
            long start = requestStartMicros - (10L * 1000L * 1000L); // 10 seconds earlier (us)
            long end = requestStartMicros + (30L * 1000L * 1000L); // 30 seconds later (us)
            if (start < 0) start = 0;
            
            // Try multiple query strategies to find traces
            // 🔥 FIX: Query gateway services FIRST to get complete distributed traces
            // Gateway traces include all downstream service spans, but backend traces may not include gateway spans
            String[] queryUrls = {
                // Strategy 1: ts-gateway-service with time window (PRIORITY - contains full distributed trace)
                JAEGER_BASE_URL + "/traces?limit=100&service=ts-gateway-service&start=" + start + "&end=" + end,
                // Strategy 2: ts-gateway-service with method (more specific)
                JAEGER_BASE_URL + "/traces?limit=50&service=ts-gateway-service&operation=" + URLEncoder.encode(method, StandardCharsets.UTF_8) + "&start=" + start + "&end=" + end,
                // Strategy 3: Other common gateway services
                JAEGER_BASE_URL + "/traces?limit=100&service=gateway-service&start=" + start + "&end=" + end,
                JAEGER_BASE_URL + "/traces?limit=100&service=api-gateway&start=" + start + "&end=" + end,
                // Strategy 4: Broad search for any traces with API calls
                JAEGER_BASE_URL + "/traces?limit=200&start=" + start + "&end=" + end,
                // Strategy 5: Target service with exact operation (fallback if gateway not found)
                JAEGER_BASE_URL + "/traces?limit=50&service=" + svcEncoded + "&operation=" + opEncoded + "&start=" + start + "&end=" + end,
                // Strategy 6: Target service with method only
                JAEGER_BASE_URL + "/traces?limit=50&service=" + svcEncoded + "&operation=" + URLEncoder.encode(method, StandardCharsets.UTF_8) + "&start=" + start + "&end=" + end,
                // Strategy 7: Target service in time window
                JAEGER_BASE_URL + "/traces?limit=100&service=" + svcEncoded + "&start=" + start + "&end=" + end,
                // Strategy 8: Recent traces with lookback (last resort)
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
            debugInfo.append("🚪 Gateway Strategy: Searching ts-gateway-service first (HTTP traces usually there)\n");
            debugInfo.append("⏱️ Test Separation: 2-second delay enforced between executions for unique traces\n");
            debugInfo.append("🔄 Trace Propagation: 3-second delay after execution for Jaeger indexing\n");
            debugInfo.append("🔄 Retry Strategy: Multiple attempts to find current execution trace\n\n");
            
            // 🔄 Retry mechanism to find current execution trace
            boolean foundCurrentTrace = false;
            int maxRetries = 3;
            JSONObject globalBestTrace = null;  // Track best trace across all queries
            long globalBestDiff = Long.MAX_VALUE;
            int globalBestScore = -1;
            
            for (int retry = 0; retry < maxRetries && !foundCurrentTrace; retry++) {
                if (retry > 0) {
                    debugInfo.append("\n🔄 RETRY ").append(retry).append(": Searching again for current execution trace...\n");
                    try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }
                
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
                                    
                                    // Enhanced scoring - HEAVILY prioritize recent execution timing
                                    if (hasTargetOperation) traceScore += 1000; // Target operation match
                                    traceScore += apiCallCount * 5; // More API calls = better (reduced weight)
                                    
                                    // CRITICAL: Ultra-precise time proximity scoring (test separation enforced)
                                    if (closestTime < Long.MAX_VALUE) {
                                        if (closestTime < 1000000L) traceScore += 5000; // Within 1 second (CURRENT execution)
                                        else if (closestTime < 2000000L) traceScore += 3000; // Within 2 seconds (EXACT execution)
                                        else if (closestTime < 5000000L) traceScore += 1500; // Within 5 seconds (very recent)
                                        else if (closestTime < 10000000L) traceScore += 500; // Within 10 seconds
                                        else if (closestTime < 20000000L) traceScore += 200; // Within 20 seconds
                                        else if (closestTime < 30000000L) traceScore += 100; // Within 30 seconds
                                    }
                                    
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
                                    
                                    // Select best trace: TIMING is the primary discriminator for recent requests
                                    boolean shouldSelectTrace = false;
                                    
                                    if (traceScore > 0) { // Must have API calls
                                        if (bestTrace == null) {
                                            shouldSelectTrace = true;
                                        } else {
                                            // For recent traces (within 10 seconds), prioritize timing over score
                                            boolean currentIsRecent = closestTime < 10000000L; // 10 seconds (post-delay execution)
                                            boolean bestIsRecent = bestDiff < 10000000L;
                                            
                                            if (currentIsRecent && !bestIsRecent) {
                                                shouldSelectTrace = true; // Always prefer recent traces
                                            } else if (!currentIsRecent && bestIsRecent) {
                                                shouldSelectTrace = false; // Keep the recent one
                                            } else if (currentIsRecent && bestIsRecent) {
                                                // Both recent: pick the closest in time
                                                shouldSelectTrace = closestTime < bestDiff;
                                            } else {
                                                // Both old: use score then timing
                                                shouldSelectTrace = traceScore > bestScore || 
                                                    (traceScore == bestScore && closestTime < bestDiff);
                                            }
                                        }
                                    }
                                    
                                    if (shouldSelectTrace) {
                                        bestScore = traceScore;
                                        bestDiff = closestTime;
                                        bestTrace = trace;
                                        bestTraceIdx = i;
                                    }
                                }
                                
                                debugInfo.append("\nSelected trace ").append(bestTraceIdx).append(" with score ").append(bestScore);
                                if (bestScore > 0) {
                                    // Check if this is a current execution trace (within 3 seconds)
                                    boolean isCurrentTrace = bestDiff < 3000000L; // 3 seconds
                                    if (isCurrentTrace) {
                                        debugInfo.append(" (CURRENT EXECUTION - perfect match!)\n");
                                        foundCurrentTrace = true;
                                    } else {
                                        debugInfo.append(" (matched but not current - possibly previous execution)\n");
                                    }
                                    
                                    // Update global best trace if this is better
                                    boolean shouldUpdateGlobal = false;
                                    if (globalBestTrace == null) {
                                        shouldUpdateGlobal = true;
                                    } else if (isCurrentTrace && globalBestDiff >= 3000000L) {
                                        shouldUpdateGlobal = true; // Always prefer current over non-current
                                    } else if (isCurrentTrace && globalBestDiff < 3000000L) {
                                        shouldUpdateGlobal = bestDiff < globalBestDiff; // Both current: pick closer
                                    } else if (!isCurrentTrace && globalBestDiff >= 3000000L) {
                                        // Both non-current: pick better score or closer time
                                        shouldUpdateGlobal = bestScore > globalBestScore || 
                                            (bestScore == globalBestScore && bestDiff < globalBestDiff);
                                    }
                                    
                                    if (shouldUpdateGlobal) {
                                        // Fetch COMPLETE trace to replace summary
                                        if (bestTrace != null) {
                                            String traceId = bestTrace.optString("traceID", "");
                                            if (!traceId.isEmpty()) {
                                                debugInfo.append("\n🔄 Fetching complete trace for ID: ").append(traceId).append("\n");
                                                String traceDetailUrl = JAEGER_BASE_URL + "/traces/" + traceId;
                                                HttpRequest traceReq = HttpRequest.newBuilder(java.net.URI.create(traceDetailUrl)).GET().build();
                                                HttpResponse<String> traceRes = client.send(traceReq, HttpResponse.BodyHandlers.ofString());
                                                
                                                if (traceRes.statusCode() >= 200 && traceRes.statusCode() < 300) {
                                                    JSONObject completeTraceResponse = new JSONObject(traceRes.body());
                                                    if (completeTraceResponse.has("data") && completeTraceResponse.getJSONArray("data").length() > 0) {
                                                        bestTrace = completeTraceResponse.getJSONArray("data").getJSONObject(0);
                                                        debugInfo.append("✅ Complete trace fetched with ").append(bestTrace.optJSONArray("spans").length()).append(" spans\n");
                                                    } else {
                                                        debugInfo.append("⚠️  Complete trace response has no data, using summary trace\n");
                                                    }
                                                } else {
                                                    debugInfo.append("⚠️  Failed to fetch complete trace (status: ").append(traceRes.statusCode()).append("), using summary trace\n");
                                                }
                                            }
                                        }
                                        
                                        // Update global best
                                        globalBestTrace = bestTrace;
                                        globalBestDiff = bestDiff;
                                        globalBestScore = bestScore;
                                        debugInfo.append("✅ Updated global best trace (score: ").append(bestScore).append(", timeDiff: ").append(bestDiff / 1000000.0).append("s)\n");
                                    }
                                    
                                    // If this is a current trace, we can stop retrying
                                    if (isCurrentTrace) {
                                        debugInfo.append("✅ Found current execution trace - stopping search\n");
                                        break; // Exit query loop
                                    } else {
                                        debugInfo.append("⏭️ Continuing to search for more recent trace...\n");
                                    }
                                } else {
                                    debugInfo.append(" (no API calls found)\n");
                                    debugInfo.append("\n⚠️ This query found traces but no API calls. Continuing to next strategy...\n");
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
            
            // If we found a current trace, exit retry loop
            if (foundCurrentTrace) {
                debugInfo.append("✅ Found current execution trace, stopping retries.\n");
                break; // Exit retry loop
            }
            }
            
            // After all searches, analyze and attach the BEST trace found (if any)
            if (globalBestTrace != null) {
                debugInfo.append("\n✅ Using best trace found (score: ").append(globalBestScore)
                           .append(", timeDiff: ").append(globalBestDiff / 1000000.0).append("s)\n");
                
                String traceTable = generateTraceTable(globalBestTrace);
                String traceSummary = generateTraceSummary(globalBestTrace);
                TraceErrorAnalyzer.ErrorAnalysisResult errorAnalysis = TraceErrorAnalyzer.analyzeTrace(globalBestTrace);
                String errorReport = TraceErrorAnalyzer.generateErrorReport(errorAnalysis);
                
                if (errorAnalysis.hasErrors()) {
                    // Trace contains technical errors (error=true tags)
                    analyzeAndRecordParameterErrors(globalBestTrace, stepParameters);
                    String intelligentAnalysis = TraceErrorAnalyzer.generateIntelligentAnalysis(errorAnalysis, globalBestTrace);
                    if (intelligentAnalysis != null && !intelligentAnalysis.trim().isEmpty()) {
                        Allure.addAttachment("🤖 INTELLIGENT ANALYSIS (Based on Trace)", "text/plain", intelligentAnalysis);
                    }
                    Allure.addAttachment("🔗 API Call Trace", "text/plain", traceTable);
                } else if (isStepFailed) {
                    // Test failed but trace has no technical errors - likely business logic validation failure
                    StringBuilder analysisMsg = new StringBuilder();
                    analysisMsg.append("═══════════════════════════════════════════════════════════════════════\n");
                    analysisMsg.append("🔍 ROOT CAUSE ANALYSIS\n");
                    analysisMsg.append("═══════════════════════════════════════════════════════════════════════\n\n");
                    analysisMsg.append("**ANALYSIS STATUS:**\n");
                    analysisMsg.append("The distributed trace for this failed test execution was successfully retrieved,\n");
                    analysisMsg.append("however, it does not contain any technical error indicators (error tags).\n\n");
                    analysisMsg.append("**LIKELY CAUSE:**\n");
                    analysisMsg.append("This failure is most likely due to business logic validation rather than\n");
                    analysisMsg.append("a technical error. The service processed the request successfully but\n");
                    analysisMsg.append("rejected it based on application-level validation rules (e.g., invalid\n");
                    analysisMsg.append("business state, constraint violations, or semantic validation failures).\n\n");
                    analysisMsg.append("**RECOMMENDATIONS:**\n");
                    analysisMsg.append("• Review the response body in the '📥 Response' section for validation error messages\n");
                    analysisMsg.append("• Check the HTTP status code (e.g., 400 Bad Request, 422 Unprocessable Entity)\n");
                    analysisMsg.append("• Examine the '🔗 API Call Trace' section to identify which service rejected the request\n");
                    analysisMsg.append("• Verify input parameters against business rules and constraints\n");
                    analysisMsg.append("• Consult the API documentation for validation requirements\n\n");
                    analysisMsg.append("For detailed trace information, see the '📈 Raw Trace Data' section below.\n");
                    Allure.addAttachment("🤖 INTELLIGENT ANALYSIS (Based on Trace)", "text/plain", analysisMsg.toString());
                    Allure.addAttachment("🔗 API Call Trace", "text/plain", traceTable);
                } else {
                    // Test succeeded and trace has no errors
                    Allure.addAttachment("🔗 API Call Trace", "text/plain", traceTable);
                }
                Allure.addAttachment("📊 Trace Summary", "text/plain", traceSummary);
                Allure.addAttachment("📈 Raw Trace Data", "application/json", globalBestTrace.toString());
                Allure.addAttachment("🔍 Query Debug Info", "text/plain", debugInfo.toString());
            } else {
                // No traces found with any query after all retries
                if (!foundCurrentTrace) {
                    debugInfo.append("❌ No current execution traces found after ").append(maxRetries).append(" retries\n");
                }
                debugInfo.append("❌ No traces found with any query strategy");
                Allure.addAttachment("🔍 Jaeger Query Debug (No Traces)", "text/plain", debugInfo.toString());
            }
            
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
            table.append("----------------------------------------------------------------------------------------\n");
            table.append("                           🔗 MICROSERVICE API CALL TRACE                           \n");
            table.append("----------------------------------------------------------------------------------------\n");
            if (!traceId.isEmpty()) {
                table.append("Trace ID: ").append(traceId).append("\n");
                // Convert API endpoint to UI endpoint for browser link
                // Remove /api from end if present, keep everything else (including /jaeger/ui if present)
                String uiBase = JAEGER_BASE_URL;
                if (uiBase.endsWith("/api")) {
                    uiBase = uiBase.substring(0, uiBase.length() - 4);  // Remove "/api"
                }
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

    private static boolean hasErrorTag(JSONObject span) {
        if (!span.has("tags")) return false;
        
        JSONArray tags = span.getJSONArray("tags");
        for (int i = 0; i < tags.length(); i++) {
            JSONObject tag = tags.getJSONObject(i);
            if ("error".equals(tag.optString("key")) && 
                "bool".equals(tag.optString("type")) && 
                tag.optBoolean("value", false)) {
                return true;
            }
        }
        return false;
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
        
        // Filter out gateway services - only show actual business APIs
        boolean isGateway = serviceName.toLowerCase().contains("gateway") || 
                           serviceName.toLowerCase().contains("proxy") ||
                           serviceName.toLowerCase().equals("gateway-service") ||
                           serviceName.toLowerCase().equals("ts-gateway-service") ||
                           serviceName.toLowerCase().equals("api-gateway");
        
        // If this is a gateway, skip rendering but still process children
        if (isGateway) {
            // Render children recursively without showing this gateway span
            List<String> children = apiChildren.getOrDefault(spanId, Collections.emptyList());
            for (String childId : children) {
                renderApiHierarchy(table, childId, apiSpanById, apiChildren, processes, depth, counter, "");
            }
            return;
        }
        
        // Check for errors in this span
        boolean hasError = hasErrorTag(span);
        String durationStr = formatDuration(duration);
        
        // Use SINGLE status indicator - prioritize error detection over HTTP status
        String statusIcon;
        if (hasError) {
            statusIcon = "❌";  // Error detected in span
        } else if (status.startsWith("2")) {
            statusIcon = "✅";  // HTTP 2xx success
        } else if (status.startsWith("4") || status.startsWith("5")) {
            statusIcon = "❌";  // HTTP 4xx/5xx error
        } else {
            statusIcon = "❓";  // Unknown/other status
        }
        
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
        
        // Main line: [#] Status Service Method HTTPStatus(time)
        line.append(String.format("[%d] %s %s %s %s(%s)", 
            apiNumber, statusIcon, displayService, method, status, durationStr));
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

    private static void analyzeAndRecordParameterErrors(JSONObject trace, Map<String, String> stepParameters) {
        try {
            // Get LLM properties from system properties
            Map<String, String> llmProperties = new HashMap<>();
            llmProperties.put("llm.enabled", System.getProperty("llm.enabled", "true"));
            llmProperties.put("llm.model.type", System.getProperty("llm.model.type", "ollama"));
            llmProperties.put("llm.ollama.enabled", System.getProperty("llm.ollama.enabled", "true"));
            llmProperties.put("llm.ollama.url", System.getProperty("llm.ollama.url", "http://localhost:11434"));
            llmProperties.put("llm.ollama.model", System.getProperty("llm.ollama.model", "gemma3:4b"));
            llmProperties.put("llm.gemini.enabled", System.getProperty("llm.gemini.enabled", "false"));
            llmProperties.put("llm.gemini.api.key", System.getProperty("llm.gemini.api.key", ""));
            llmProperties.put("llm.gemini.model", System.getProperty("llm.gemini.model", "gemini-2.0-flash-exp"));
            llmProperties.put("llm.gemini.api.url", System.getProperty("llm.gemini.api.url", "https://generativelanguage.googleapis.com/v1beta/models"));
            
            // Analyze parameter errors
            es.us.isa.restest.inputs.smart.ParameterErrorAnalyzer.ParameterErrorAnalysisResult result = 
                es.us.isa.restest.inputs.smart.ParameterErrorAnalyzer.analyzeParameterErrors(trace, stepParameters, llmProperties);
            
            if (result.hasParameterErrors()) {
                System.out.println("🔍 Parameter Error Analysis: Found " + result.getIdentifiedErrors().size() + " parameter-related errors");
                
                // Load and update the input fetch registry
                String registryPath = System.getProperty("smart.input.fetch.registry.path");
                if (registryPath != null && !registryPath.isEmpty()) {
                    try {
                        java.io.File registryFile = new java.io.File(registryPath);
                        es.us.isa.restest.inputs.smart.InputFetchRegistry registry;
                        
                        if (registryFile.exists()) {
                            registry = es.us.isa.restest.inputs.smart.InputFetchRegistry.loadFromFile(registryFile);
                        } else {
                            registry = new es.us.isa.restest.inputs.smart.InputFetchRegistry();
                        }
                        
                        // Record each parameter error
                        for (es.us.isa.restest.inputs.smart.ParameterError error : result.getIdentifiedErrors()) {
                            registry.addParameterError(error.getApiEndpoint(), error.getParameterName(), error);
                            System.out.println("📝 Recorded error: " + error.getParameterName() + " -> " + error.getErrorType() + ": " + error.getErrorReason());
                        }
                        
                        // Save updated registry
                        registry.saveToFile(registryFile);
                        System.out.println("💾 Updated parameter error registry at: " + registryPath);
                        
                    } catch (Exception e) {
                        System.err.println("⚠️ Failed to update parameter error registry: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("⚠️ Parameter error registry path not configured. Set 'smart.input.fetch.registry.path' property.");
                }
            } else {
                System.out.println("✅ No parameter-related errors detected in trace");
            }
            
        } catch (Exception e) {
            System.err.println("⚠️ Error during parameter error analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://129.62.148.112:32677";
        // Configure Allure results directory
        System.setProperty("allure.results.directory", "target/allure-results");
        // ⚠️ AllureRestAssured filter disabled - causes duplicate request logging
        // We use manual, controlled attachments instead for cleaner reports
        // RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void test_negative_DELETE_601_75() throws Exception {
        // Record test execution for fault detection tracking
        es.us.isa.restest.analysis.FaultDetectionTracker.getInstance()
            .recordTestCase(this.getClass().getName(), "test_negative_DELETE_601_75");

        // 🔧 Test Case Enhancer: Capture test metadata
        es.us.isa.restest.enhancer.TestResultCapture.setTestMetadata(
            "/api/v1/admintravelservice/admintravel/tripabc123xyz456", "DELETE", 
            "ts-admin-travel-service", true);

        // 🎯 Allure Suite Grouping: Normalize by API template (not by resolved path values)
        Allure.label("parentSuite", "ts-admin-travel-service");
        Allure.label("suite", "DELETE /api/v1/admintravelservice/admintravel/{tripId}");
        Allure.label("subSuite", "test_negative_DELETE_601_75");

        // 🚨 NEGATIVE TEST METADATA
        Allure.parameter("🚨 Test Type", "NEGATIVE (Invalid Input Testing)");
        Allure.parameter("🔴 Invalid Parameters", "tripId=tripabc123xyz456");
        Allure.description("**⚠️ This is a NEGATIVE test case with intentionally invalid inputs.**\n\n" +
                         "The following parameters were intentionally set to invalid values:\n" +
                         "tripId=tripabc123xyz456\n\n" +
                         "This test expects a 4XX or 5XX error response. If it returns 2XX, the test will FAIL.");
        es.us.isa.restest.enhancer.TestResultCapture.addInvalidParameter("tripId=tripabc123xyz456");

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
        // Parameter tracking for error analysis
        final java.util.Map<String, String> allStepParameters = new java.util.HashMap<>();

        // Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200]
        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE
        
        // 🎯 CRITICAL: Add delay between test executions to ensure unique traces
        // This prevents tests from executing so rapidly that they find the same traces
        try {
            Thread.sleep(2000); // 2 second delay between tests for trace separation
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        
        final long requestStartMicros = System.currentTimeMillis() * 1000L;
        try {
            Allure.step("Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200]", () -> {
                Allure.parameter("🏢 Service", "ts-admin-travel-service");
                Allure.parameter("📡 HTTP Method", "DELETE");
                Allure.parameter("🔗 Endpoint", "/api/v1/admintravelservice/admintravel/tripabc123xyz456");
                Allure.parameter("🎯 Expected Status", 200);
                Allure.parameter("🔗 Dependency Type", "INDEPENDENT (can execute regardless of other step results)");
                Allure.description("🎯 **Testing**: ts-admin-travel-service\n" +
                                 "📡 **Method**: DELETE\n" +
                                 "🔗 **Path**: /api/v1/admintravelservice/admintravel/tripabc123xyz456\n" +
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
                    System.out.println("▶️ EXECUTING: Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200] (dependency analysis passed)");
                    Response stepResponse1 = null;
                    
                    try {
                        RequestSpecification req = RestAssured.given();
                        if (loginSucceeded.get()) {
                            req = req.header("Authorization", jwtType + " " + jwt);
                        }
                        // 🔥 FIX: Extract response FIRST (before status code assertion) to capture response body in all cases
                        stepResponse1 = req.when().delete("/api/v1/admintravelservice/admintravel/tripabc123xyz456")
                               .then().log().ifValidationFails()
                               .extract().response();
                        
                        // Now validate status code based on expected status from configuration
                        int actualStatusCode1 = stepResponse1.getStatusCode();
                        int expectedStatusCode1 = 200;
                        
                        // 🤖 LLM RESPONSE VALIDATION: Check response body for errors
                        boolean llmValidationEnabled = Boolean.parseBoolean(System.getProperty("llm.response.validation.enabled", "false"));
                        boolean only2xx = Boolean.parseBoolean(System.getProperty("llm.response.validation.only.2xx", "true"));
                        boolean includeRca = Boolean.parseBoolean(System.getProperty("llm.response.validation.include.rca", "true"));
                        
                        // 🔴 NEGATIVE TEST VALIDATION
                        // For negative tests with invalid inputs, we expect EITHER:
                        // 1. A different status code (error response), OR
                        // 2. Status 200 but with error/failure message in response body (soft error)
                        // CRITICAL: The error message MUST be related to our designed invalid input!
                        boolean statusCodeIndicatesError = (actualStatusCode1 != expectedStatusCode1);
                        boolean llmDetectedRelatedError = false;
                        String llmRca = "LLM validation not performed";
                        
                        // Build map of designed invalid parameters for this negative test
                        java.util.Map<String, String> invalidParams = new java.util.HashMap<>();
                        invalidParams.put("tripId", "tripabc123xyz456");
                        
                        // Always run LLM validation for negative tests (to detect soft errors related to our invalid input)
                        if (llmValidationEnabled) {
                            try {
                                String validationBody = stepResponse1.getBody().asString();
                                
                                // Create LLM validator instance
                                es.us.isa.restest.generators.ZeroShotLLMGenerator llmValidator = new es.us.isa.restest.generators.ZeroShotLLMGenerator();
                                
                                // Validate response - checks if error is RELATED to our invalid input
                                es.us.isa.restest.generators.ZeroShotLLMGenerator.ValidationResult validationResult = 
                                    llmValidator.validateNegativeTestResponse(
                                        actualStatusCode1,
                                        validationBody,
                                        "ts-admin-travel-service",
                                        "DELETE",
                                        "/api/v1/admintravelservice/admintravel/tripabc123xyz456",
                                        invalidParams
                                    );
                                
                                // isFailed() returns true only if error was detected AND related to our invalid input
                                llmDetectedRelatedError = validationResult.isFailed();
                                llmRca = validationResult.getRca();
                                System.out.println("🤖 LLM Validation (Negative Test): Error Related to Invalid Input=" + llmDetectedRelatedError + ", RCA: " + llmRca);
                                
                                // Attach LLM analysis to Allure report
                                if (includeRca) {
                                    StringBuilder llmReport = new StringBuilder();
                                    llmReport.append("════════════════════════════════════════════════════════════════════════\n");
                                    llmReport.append("🤖 INTELLIGENT ANALYSIS (Negative Test)\n");
                                    llmReport.append("════════════════════════════════════════════════════════════════════════\n\n");
                                    llmReport.append("Test Type: 🔴 NEGATIVE (Invalid Input Testing)\n\n");
                                    
                                    // Show designed invalid inputs
                                    llmReport.append("📋 DESIGNED INVALID INPUTS:\n");
                                    for (java.util.Map.Entry<String, String> entry : invalidParams.entrySet()) {
                                        llmReport.append("   • ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
                                    }
                                    llmReport.append("\n");
                                    
                                    llmReport.append("Status Code: ").append(actualStatusCode1).append(" (expected: ").append(expectedStatusCode1).append(")\n");
                                    llmReport.append("Status Code Indicates Error: ").append(statusCodeIndicatesError ? "✅ YES" : "❌ NO").append("\n");
                                    llmReport.append("Error Related to Invalid Input: ").append(llmDetectedRelatedError ? "✅ YES" : "❌ NO").append("\n\n");
                                    llmReport.append("Root Cause Analysis:\n");
                                    llmReport.append(llmRca).append("\n");
                                    Allure.addAttachment("🤖 INTELLIGENT ANALYSIS (Negative Test)", "text/plain", llmReport.toString());
                                }
                                
                            } catch (Exception llmEx) {
                                System.err.println("⚠️ LLM validation failed: " + llmEx.getMessage());
                                // Continue with status code check only
                            }
                        }
                        
                        // Negative test PASSES if either:
                        // 1. Status code is different from expected (clear error), OR
                        // 2. LLM detected a soft error that is RELATED to our designed invalid input
                        // NOTE: If error is NOT related to our invalid input, test still FAILS!
                        boolean negativeTestPassed = statusCodeIndicatesError || llmDetectedRelatedError;
                        
                        if (!negativeTestPassed) {
                            throw new AssertionError("Negative test failed: Either no error detected, or error was not related to our invalid input. RCA: " + llmRca);
                        }
                        
                        if (statusCodeIndicatesError) {
                            System.out.println("✅ Negative test PASSED: Status code " + actualStatusCode1 + " indicates error (expected: " + expectedStatusCode1 + ")");
                        } else {
                            System.out.println("✅ Negative test PASSED: LLM confirmed error is related to our designed invalid input");
                        }
                        
                        
                        // 🔍 FAULT DETECTION: Check if root API response contains injected fault
                        try {
                            String faultCheckBody = stepResponse1.getBody().asString();
                            org.json.JSONObject faultJson = new org.json.JSONObject(faultCheckBody);
                            if (faultJson.has("data")) {
                                Object dataValue = faultJson.get("data");
                                if (dataValue instanceof org.json.JSONObject) {
                                    org.json.JSONObject dataObj = (org.json.JSONObject) dataValue;
                                    if (dataObj.optBoolean("injected", false)) {
                                        String detectedFaultName = dataObj.optString("faultName", "");
                                        if (!detectedFaultName.isEmpty()) {
                                            es.us.isa.restest.analysis.FaultDetectionTracker.getInstance().recordDetectedFault(
                                                detectedFaultName,
                                                this.getClass().getName(),
                                                "test_negative_DELETE_601_75",
                                                System.currentTimeMillis(),
                                                faultCheckBody
                                            );
                                            System.out.println("🔍 FAULT DETECTED: " + detectedFaultName + " in test: test_negative_DELETE_601_75");
                                        }
                                    }
                                }
                            }
                        } catch (Exception faultEx) {
                            // Silent fail - don't break test execution
                        }
                        
                        stepResults.put(1, true);
                        System.out.println("✅ Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200] - SUCCESS");
                        // ✅ SUCCESS: Clean success reporting without duplication
                        try {
                            String responseBody = stepResponse1.getBody().asString();
                            int actualStatus = stepResponse1.getStatusCode();
                            long responseTime = stepResponse1.getTime();
                            
                            // 🔧 Test Case Enhancer: Capture response for enhancement
                            es.us.isa.restest.enhancer.TestResultCapture.captureResponse(actualStatus, responseBody);
                            
                            // Single success status parameter
                            Allure.parameter("🎯 Result", "✅ SUCCESS (" + actualStatus + " in " + responseTime + "ms)");
                            
                            // Add response as attachment (AllureRestAssured filter disabled to avoid duplication)
                            Allure.addAttachment("📥 Response (" + actualStatus + ")", "application/json", responseBody);
                            // ⏱️ Wait longer for trace propagation to Jaeger (increased delay)
                            try { Thread.sleep(3000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                            attachJaegerTrace("ts-admin-travel-service", "DELETE", "/api/v1/admintravelservice/admintravel/tripabc123xyz456", requestStartMicros, allStepParameters, false);
                        } catch (Exception e) {
                            Allure.parameter("🎯 Result", "✅ SUCCESS (response capture failed)");
                        }
                    } catch (Throwable t) {
                        stepResults.put(1, false);
                        System.out.println("❌ Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200] - FAILED: " + t.getMessage());
                        
                        // 🔥 CRITICAL: Capture response body for failed requests (response was extracted before assertion)
                        String failedResponseBody = null;
                        int failedStatusCode = -1;
                        long failedResponseTime = -1;
                        try {
                            if (stepResponse1 != null) {
                                failedResponseBody = stepResponse1.getBody().asString();
                                failedStatusCode = stepResponse1.getStatusCode();
                                failedResponseTime = stepResponse1.getTime();
                                // 🔧 Test Case Enhancer: Capture response for enhancement
                                es.us.isa.restest.enhancer.TestResultCapture.captureResponse(failedStatusCode, failedResponseBody);
                            }
                        } catch (Exception respEx) {
                            failedResponseBody = "Unable to capture response: " + respEx.getMessage();
                        }
                        
                        // 🔍 FAULT DETECTION (FAILURE PATH): Check if failed response contains injected fault
                        try {
                            if (failedResponseBody != null && !failedResponseBody.startsWith("Unable to capture")) {
                                org.json.JSONObject faultJson = new org.json.JSONObject(failedResponseBody);
                                if (faultJson.has("data")) {
                                    Object dataValue = faultJson.get("data");
                                    if (dataValue instanceof org.json.JSONObject) {
                                        org.json.JSONObject dataObj = (org.json.JSONObject) dataValue;
                                        if (dataObj.optBoolean("injected", false)) {
                                            String detectedFaultName = dataObj.optString("faultName", "");
                                            if (!detectedFaultName.isEmpty()) {
                                                es.us.isa.restest.analysis.FaultDetectionTracker.getInstance().recordDetectedFault(
                                                    detectedFaultName,
                                                    this.getClass().getName(),
                                                    "test_negative_DELETE_601_75",
                                                    System.currentTimeMillis(),
                                                    failedResponseBody
                                                );
                                                System.out.println("🔍 FAULT DETECTED (in failed test): " + detectedFaultName + " in test: test_negative_DELETE_601_75");
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception faultEx) {
                            // Silent fail - don't break error handling
                        }
                        
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
                        String resultMsg = "❌ FAILED (" + errorType;
                        if (failedStatusCode >= 0) {
                            resultMsg += ", status: " + failedStatusCode;
                        }
                        if (failedResponseTime >= 0) {
                            resultMsg += ", " + failedResponseTime + "ms";
                        }
                        resultMsg += ")";
                        Allure.parameter("🎯 Result", resultMsg);
                        Allure.parameter("🔍 Failure Reason", failureReason);
                        Allure.parameter("🏢 Failed Service", "ts-admin-travel-service");
                        Allure.parameter("📡 Failed Method", "DELETE");
                        Allure.parameter("🔗 Failed Endpoint", "/api/v1/admintravelservice/admintravel/tripabc123xyz456");
                        
                        // 🔥 CRITICAL: Attach response body for failed requests
                        if (failedResponseBody != null) {
                            String responseTitle = "📥 Response (" + failedStatusCode + ")";
                            Allure.addAttachment(responseTitle, "application/json", failedResponseBody);
                        }
                        
                        // Comprehensive error details
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("❌ STEP FAILURE ANALYSIS\n");
                        errorDetails.append("=====================================\n\n");
                        errorDetails.append("📋 Step: Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200]\n");
                        errorDetails.append("🏢 Service: ts-admin-travel-service\n");
                        errorDetails.append("📡 Method: DELETE\n");
                        errorDetails.append("🔗 Endpoint: /api/v1/admintravelservice/admintravel/tripabc123xyz456\n");
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
                        
                        // ⏱️ Wait longer for trace propagation to Jaeger (increased delay)
                        try { Thread.sleep(3000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                        attachJaegerTrace("ts-admin-travel-service", "DELETE", "/api/v1/admintravelservice/admintravel/tripabc123xyz456", requestStartMicros, allStepParameters, true);
                        
                        // 🔥 CRITICAL: Throw exception to mark step as FAILED (red arrow) in Allure
                        throw new RuntimeException("Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200] failed: " + failureReason + " (" + errorType + ")", t);
                    }
                } else {
                    // ⏭️ SKIP: Clean skip reporting with proper Allure status
                    System.out.println("⏭️ SKIPPING: Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200] - " + skipReason);
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
                System.out.println("⚠️ Step wrapper failed for Step 1: ts-admin-travel-service DELETE /api/v1/admintravelservice/admintravel/tripabc123xyz456 [expect not 200]: " + stepException.getMessage());
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
        Allure.label("story", "test_negative_DELETE_601_75");
        Allure.description("Microservice test scenario with " + totalSteps + " steps.");
        
        System.out.println("=== SCENARIO RESULT ===");
        System.out.println("Scenario: test_negative_DELETE_601_75");
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
