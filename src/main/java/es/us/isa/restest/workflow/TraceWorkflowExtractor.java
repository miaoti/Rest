package es.us.isa.restest.workflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class to extract workflow scenarios from OpenTelemetry trace data.
 *
 * The extractor reads a JSON or JSONL (one JSON object per line) file containing span data,
 * then reconstructs hierarchical {@link WorkflowScenario} instances. It handles missing fields,
 * orders spans by parent-child relationships and start times, and infers cross-trace dependencies
 * by matching output fields from one trace to input fields of another.
 *
 * This class is designed for robust operation: it tolerates missing optional data and can merge
 * multiple traces into a single scenario if data dependencies (e.g., an ID in a response used in another request) are detected.
 * Future enhancements could include using OpenTelemetry span links for asynchronous propagation
 * or inserting special {@link WorkflowStep} types for events like message queues or fault injection.
 */
public class TraceWorkflowExtractor {
    private static final Logger log = LogManager.getLogger(TraceWorkflowExtractor.class);

    /**
     * Reads OpenTelemetry spans from a JSON/JSONL file or directory and extracts logical workflow scenarios.
     * Each scenario is a sequence of steps (potentially across multiple services) representing an end-to-end workflow.
     *
     * @param fileOrDirPath the path to the JSON/JSONL file or directory containing trace files
     * @return a list of {@link WorkflowScenario} objects reconstructed from the trace data
     * @throws IOException if an I/O error occurs reading the file(s)
     * @throws IllegalArgumentException if the file content is not valid JSON/span format
     */
    public static List<WorkflowScenario> extractScenarios(String fileOrDirPath) throws IOException {
        File path = new File(fileOrDirPath);
        List<WorkflowScenario> allScenarios = new ArrayList<>();
        
        if (path.isDirectory()) {
            // Process all JSON files in the directory
            File[] jsonFiles = path.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".json") || name.toLowerCase().endsWith(".jsonl"));
            
            if (jsonFiles == null || jsonFiles.length == 0) {
                log.warn("No JSON/JSONL files found in directory: " + fileOrDirPath);
                return allScenarios;
            }
            
            log.info("Processing {} trace files from directory: {}", jsonFiles.length, fileOrDirPath);
            for (File jsonFile : jsonFiles) {
                log.info("Processing trace file: {}", jsonFile.getName());
                try {
                    List<WorkflowScenario> fileScenarios = extractScenariosFromFile(jsonFile.getPath());
                    allScenarios.addAll(fileScenarios);
                } catch (Exception e) {
                    log.error("Error processing trace file {}: {}", jsonFile.getName(), e.getMessage());
                    // Continue processing other files
                }
            }
        } else {
            // Single file processing
            allScenarios = extractScenariosFromFile(fileOrDirPath);
        }
        
        return allScenarios;
    }
    
    /**
     * Internal method to extract scenarios from a single file.
     */
    private static List<WorkflowScenario> extractScenariosFromFile(String filePath) throws IOException {
        List<JSONObject> spanObjects = new ArrayList<>();


        // Read the file and parse JSON content
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Peek at the first non-empty line to determine format (JSON array vs JSON Lines)
            String firstLine = null;
            while ((firstLine = reader.readLine()) != null && firstLine.trim().isEmpty()) {
                // skip empty lines at start
            }
            if (firstLine == null) {
                throw new IllegalArgumentException("Input file is empty or contains no JSON content.");
            }
            String trimmed = firstLine.trim();
            if (trimmed.startsWith("[")) {
                StringBuilder sb = new StringBuilder(trimmed);
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }


                JSONArray spanArray = new JSONArray(sb.toString());
                for (int i = 0; i < spanArray.length(); i++) {
                    if (spanArray.get(i) instanceof JSONObject) {
                        spanObjects.add(spanArray.getJSONObject(i));
                    }
                }

            } else if (trimmed.startsWith("{")) {
                // NEW: potentially a wrapper object
                // Read the whole file into a single string
                StringBuilder sb = new StringBuilder(trimmed);
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject root = new JSONObject(sb.toString());
                if (root.has("data")) {
                    // Flatten each data[i].spans[] into spanObjects
                    JSONArray dataArr = root.getJSONArray("data");
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject bucket = dataArr.getJSONObject(i);
                        // grab the map of processID -> { serviceName, tags, … }
                        JSONObject processes = bucket.optJSONObject("processes");

                        JSONArray spans = bucket.optJSONArray("spans");
                        if (spans != null) {
                            for (int j = 0; j < spans.length(); j++) {
                                JSONObject spanObj = spans.getJSONObject(j);

                                // === NEW: pluck serviceName out of the processes map ===
                                if (processes != null) {
                                    String procId = spanObj.optString("processID", null);
                                    if (procId != null && processes.has(procId)) {
                                        JSONObject procInfo = processes.getJSONObject(procId);
                                        String svc = procInfo.optString("serviceName", null);
                                        if (svc != null && !svc.isEmpty()) {
                                            spanObj.put("serviceName", svc);
                                        }
                                    }
                                }

                                spanObjects.add(spanObj);
                            }
                        }
                    }
                } else if (root.has("spans")) {
                    // Top-level "spans": rare but possible
                    JSONArray spans = root.getJSONArray("spans");
                    for (int j = 0; j < spans.length(); j++) {
                        spanObjects.add(spans.getJSONObject(j));
                    }
                } else {
                    throw new IllegalArgumentException("Unrecognized JSON object format");
                }

            } else {
                // The file is newline-delimited JSON (JSONL): each line is a separate JSON object
                try {
                    // Parse the first line as a JSON object
                    spanObjects.add(new JSONObject(firstLine));
                } catch (JSONException e) {
                    throw new IllegalArgumentException("Failed to parse JSON object on line 1: " + e.getMessage(), e);
                }
                String line;
                int lineNum = 1;
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    String jsonLine = line.trim();
                    if (jsonLine.isEmpty()) continue; // skip blank lines between entries
                    try {
                        spanObjects.add(new JSONObject(jsonLine));
                    } catch (JSONException e) {
                        // Log the error and skip this line, but continue processing other lines
                        log.error("Invalid JSON span on line " + lineNum + " - skipping. Error: " + e.getMessage());
                    }
                }
            }
        }

        // Group spans by traceId
        Map<String, List<JSONObject>> spansByTrace = new HashMap<>();
        for (JSONObject spanObj : spanObjects) {
            // Extract required fields from the JSON object
            String traceId = spanObj.optString("traceId",
                    spanObj.optString("traceID", null));
            if (traceId == null || traceId.isEmpty()) {
                log.warn("Encountered span with missing traceId, skipping: " + spanObj);
                continue;
            }
            // Group by trace
            spansByTrace.computeIfAbsent(traceId, k -> new ArrayList<>()).add(spanObj);
        }

        List<WorkflowScenario> scenarios = new ArrayList<>();
        // Process each trace group into a WorkflowScenario
        for (Map.Entry<String, List<JSONObject>> entry : spansByTrace.entrySet()) {
            String traceId = entry.getKey();
            List<JSONObject> spans = entry.getValue();
            if (spans.isEmpty()) continue;

            // Create a new scenario for this trace
            WorkflowScenario scenario = new WorkflowScenario();
            // Temporary map to hold spanId -> WorkflowStep for linking parent/child
            Map<String, WorkflowStep> stepIndex = new HashMap<>();

            // First pass: create WorkflowStep for each span (without parent/child links yet)
            for (JSONObject spanObj : spans) {
                // Basic span info
                String spanId  = spanObj.optString("spanId",
                        spanObj.optString("spanID", null));
                if (spanId == null || spanId.isEmpty()) {
                    // If spanId missing, skip this span
                    log.warn("Skipping span in trace " + traceId + " due to missing spanId.");
                    continue;
                }
                String parentSpanId = spanObj.has("parentSpanId")
                        ? spanObj.optString("parentSpanId", null)
                        : null;

                if (parentSpanId == null && spanObj.has("references")) {
                    JSONArray refs = spanObj.optJSONArray("references");
                    if (refs != null) {
                        for (int k = 0; k < refs.length(); k++) {
                            JSONObject ref = refs.getJSONObject(k);
                            if ("CHILD_OF".equals(ref.optString("refType"))) {
                                parentSpanId = ref.optString("spanID", null);
                                break;
                            }
                        }
                    }
                }

                if (parentSpanId != null && parentSpanId.isEmpty()) {
                    parentSpanId = null; // normalize empty parent ID to null
                }
                String serviceName = spanObj.optString("serviceName", null);
                if (serviceName == null || serviceName.isEmpty()) {
                    serviceName = "unknown";
                }
                // Operation name or fallback to HTTP method + URL
                String operation = spanObj.optString("operationName", null);
                if (operation == null || operation.isEmpty()) {
                    String method = null, url = null;
                    JSONObject attrs = spanObj.optJSONObject("attributes");
                    if (attrs != null) {
                        method = attrs.optString("http.method", null);
                        url = attrs.optString("http.url", null);
                    }
                    if (method != null || url != null) {
                        // Construct a placeholder operation name from method and URL/path
                        String partA = (method != null ? method : "");
                        String partB = (url != null ? url : "");
                        operation = (partA + " " + partB).trim();
                    }
                    if (operation == null || operation.isEmpty()) {
                        operation = "unknown";
                    }
                }
                // Start and end times (as long). These might be epoch milliseconds or nanoseconds.
                long startTime = -1L;
                long endTime = -1L;
                try {
                    if (spanObj.has("startTime")) {
                        // Try parsing as long (JSON may store numbers as Long or even String)
                        startTime = spanObj.getLong("startTime");
                    }
                } catch (Exception e) {
                    // If not a straightforward long, try as string then parse
                    String startStr = spanObj.optString("startTime", null);
                    if (startStr != null) {
                        try {
                            startTime = Long.parseLong(startStr);
                        } catch (NumberFormatException nfe) {
                            // Could not parse, leave as -1
                        }
                    }
                }
                try {
                    if (spanObj.has("endTime")) {
                        endTime = spanObj.getLong("endTime");
                    }
                } catch (Exception e) {
                    String endStr = spanObj.optString("endTime", null);
                    if (endStr != null) {
                        try {
                            endTime = Long.parseLong(endStr);
                        } catch (NumberFormatException nfe) {
                            // leave as -1
                        }
                    }
                }

                // Parse attributes for input/output data
                JSONObject attributes = spanObj.optJSONObject("attributes");
                if (attributes == null) attributes = spanObj.optJSONObject("tags");
                String httpMethod = null;
                String httpUrl = null;
                String httpTarget = null;
                String requestBody = null;
                String responseBody = null;
                String statusCode = null;
                if (attributes != null) {
                    httpMethod = attributes.optString("http.method", null);
                    httpUrl = attributes.optString("http.url", null);
                    httpTarget = attributes.optString("http.target", null);
                    requestBody = attributes.optString("http.request.body", null);
                    responseBody = attributes.optString("http.response.body", null);
                    // status code might be numeric or string in JSON; handle both
                    Object statusObj = attributes.opt("http.status_code");
                    if (statusObj != null) {
                        statusCode = statusObj.toString();
                    }
                }

                // Prepare maps for input and output fields
                Map<String, String> inputFields = new HashMap<>();
                Map<String, String> outputFields = new HashMap<>();
                
                // Include HTTP method and URL information in input fields for generator access
                if (httpMethod != null && !httpMethod.isEmpty()) {
                    inputFields.put("http.method", httpMethod);
                }
                if (httpUrl != null && !httpUrl.isEmpty()) {
                    inputFields.put("http.url", httpUrl);
                }
                if (httpTarget != null && !httpTarget.isEmpty()) {
                    inputFields.put("http.target", httpTarget);
                }
                
                // Parse request inputs (body and query params)
                if (requestBody != null && !requestBody.isEmpty()) {
                    inputFields.put("http.request.body", requestBody);
                    extractFieldsFromContent(requestBody, inputFields);
                }
                if (httpUrl != null && !httpUrl.isEmpty()) {
                    // If URL contains query parameters, extract them
                    int qMark = httpUrl.indexOf('?');
                    if (qMark >= 0 && qMark < httpUrl.length() - 1) {
                        String queryString = httpUrl.substring(qMark + 1);
                        extractFieldsFromContent(queryString, inputFields);
                    }
                }

                // Parse response outputs (body)
                if (responseBody != null && !responseBody.isEmpty()) {
                    outputFields.put("http.response.body", responseBody);
                    extractFieldsFromContent(responseBody, outputFields);
                }
                if (statusCode != null && !statusCode.isEmpty()) {
                    // Include status code in output fields (could be useful in scenario verification, though not for linking)
                    outputFields.put("http.status_code", statusCode);
                }
                
                // Include HTTP method and URL in output fields as well for generator access
                if (httpMethod != null && !httpMethod.isEmpty()) {
                    outputFields.put("http.method", httpMethod);
                }
                if (httpUrl != null && !httpUrl.isEmpty()) {
                    outputFields.put("http.url", httpUrl);
                }
                if (httpTarget != null && !httpTarget.isEmpty()) {
                    outputFields.put("http.target", httpTarget);
                }

                // Create the WorkflowStep object for this span
                WorkflowStep step = new WorkflowStep(traceId, spanId, serviceName, operation, startTime, endTime,
                        inputFields, outputFields);
                // Temporarily store parent ID for linking
                stepIndex.put(spanId, step);
                // We can use the parentSpanId later to link; for now we might store it via an intermediate structure
                // (Alternatively, we could store it in a field of WorkflowStep, but we chose not to to keep the model clean.)
                step.setParentSpanIdTemp(parentSpanId);  // Pseudo-call: see below for handling parentSpanId linking
            }

            // Second pass: link children to parents within this trace
            for (WorkflowStep step : new ArrayList<>(stepIndex.values())) {
                String parentSpanId = step.getParentSpanIdTemp();  // Retrieve stored parentSpanId
                if (parentSpanId != null) {
                    WorkflowStep parentStep = stepIndex.get(parentSpanId);
                    if (parentStep != null) {
                        // Link this step as a child of its parent
                        parentStep.addChild(step);
                    } else {
                        // Parent span not found in this trace (possibly span data is incomplete)
                        log.warn("Trace " + traceId + ": Parent span " + parentSpanId + " not found for span " + step.getSpanId()
                                + ". Treating as root in scenario.");
                    }
                }
            }

            // Determine roots (spans with no parent in this scenario)
            for (WorkflowStep step : stepIndex.values()) {
                if (step.getParent() == null) {
                    scenario.addRootStep(step);
                }
            }
            // Sort children of each step by startTime to maintain chronological order among siblings
            for (WorkflowStep step : stepIndex.values()) {
                step.sortChildrenByStartTime();
            }

            // Record this scenario
            scenario.addTraceId(traceId);  // add the traceId (though addRootStep already did, this ensures even if no root was added above due to missing parent handling)
            scenarios.add(scenario);
        }

        // Merge scenarios that have cross-trace data dependencies
        mergeScenariosByDataDependency(scenarios);

        return scenarios;
    }

    /**
     * Helper method to parse a content string (JSON or URL-encoded or plain) into key-value fields.
     * This method tries to interpret the content as JSON (object or array) or as URL-encoded form/query parameters.
     * It populates the provided map with any extracted fields.
     *
     * @param content the content string (e.g., HTTP request/response body or query string)
     * @param fieldMap the map to populate with extracted key-value pairs
     */
    private static void extractFieldsFromContent(String content, Map<String, String> fieldMap) {
        String trimmed = content.trim();
        if (trimmed.isEmpty()) return;
        try {
            // Try parsing as JSON object
            if (trimmed.startsWith("{")) {
                JSONObject obj = new JSONObject(trimmed);
                // Recursively extract all key-value pairs from the JSON object
                extractJsonObjectFields(obj, fieldMap);
                return;
            } else if (trimmed.startsWith("[")) {
                // If it's a JSON array, we won't treat each element as separate fields (not typical for id propagation),
                // but we can store the whole array as a single field if needed or try first element if it's an object.
                JSONArray arr = new JSONArray(trimmed);
                if (arr.length() > 0) {
                    Object firstElem = arr.get(0);
                    if (firstElem instanceof JSONObject) {
                        // If array of objects, extract fields from first object (assuming uniform structure or id likely in first element)
                        extractJsonObjectFields((JSONObject) firstElem, fieldMap);
                    } else {
                        // Non-object array elements: we store the entire array string under a generic key
                        fieldMap.put("array", trimmed);
                    }
                }
                return;
            }
        } catch (JSONException e) {
            // Not a JSON object/array, will attempt other formats
        }
        // If content is not pure JSON, check for key=value pairs (e.g., form data or query params)
        if (trimmed.contains("=")) {
            String[] pairs = trimmed.split("&");
            for (String pair : pairs) {
                if (pair.isEmpty()) continue;
                int eqIndex = pair.indexOf('=');
                String key, value;
                if (eqIndex >= 0) {
                    key = pair.substring(0, eqIndex);
                    value = pair.substring(eqIndex + 1);
                } else {
                    // No '=' present, treat the whole pair as key with empty value (or skip)
                    key = pair;
                    value = "";
                }
                try {
                    // URL decode in case of percent-encoding
                    key = URLDecoder.decode(key, StandardCharsets.UTF_8.name());
                    value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
                } catch (Exception ignore) { /* Should not happen for StandardCharsets */ }
                if (!key.isEmpty()) {
                    fieldMap.put(key, value);
                }
            }
        } else {
            // If it's a plain string with no obvious key, we can store it under a generic key (e.g., "value" or "body")
            // but such values will likely not be used for linking since no key name to match on another span.
            fieldMap.put("value", trimmed);
        }
    }

    /**
     * Recursively extracts all leaf key-value pairs from a JSONObject into the field map.
     * Nested objects are flattened by combining parent and child keys with a dot, to avoid key collisions.
     *
     * @param jsonObj the JSONObject to extract fields from
     *
     * @param fieldMap the map to populate with extracted fields
     */
    private static void extractJsonObjectFields(JSONObject jsonObj, Map<String, String> fieldMap) {
        // Iterate over the keys in the JSON object
        for (String key : jsonObj.keySet()) {
            Object valueObj = jsonObj.get(key);
            if (valueObj == null) {
                continue; // skip null values
            }
            if (valueObj instanceof JSONObject) {
                // Nested object: recurse with prefix
                extractJsonObjectFields((JSONObject) valueObj, fieldMap);
            } else if (valueObj instanceof JSONArray) {
                // Nested array: we won't enumerate all elements, store the array as a string or first element's fields
                JSONArray array = (JSONArray) valueObj;
                if (array.length() > 0 && array.get(0) instanceof JSONObject) {
                    // If array of JSON objects, we might extract from the first element as representative
                    extractJsonObjectFields(array.getJSONObject(0), fieldMap);
                } else {
                    // Otherwise, store the raw array string
                    fieldMap.put(key, array.toString());
                }
            } else {
                // Primitive value (string/number/boolean)
                fieldMap.put(key, valueObj.toString());
            }
        }
    }

    /**
     * Merges any scenarios in the list that have data dependencies between them.
     * If a field in one scenario's output matches a field in another scenario's input (by key and value),
     * the two scenarios will be merged into one, with the producer step becoming the parent of the consumer step.
     * The list of scenarios is updated in-place to reflect merged scenarios.
     *
     * @param scenarios the list of scenarios to analyze and merge as necessary
     */
    private static void mergeScenariosByDataDependency(List<WorkflowScenario> scenarios) {
        // We use a set of keys to ignore for dependency matching to avoid trivial or common fields.
        Set<String> ignoreKeys = new HashSet<>();
        ignoreKeys.add("http.status_code");
        ignoreKeys.add("status_code");
        ignoreKeys.add("value"); // generic or uninformative keys

        boolean mergedSomething = true;
        // Keep attempting to merge until no more merges occur in a full pass
        while (mergedSomething) {
            mergedSomething = false;
            outerLoop:
            for (int i = 0; i < scenarios.size(); i++) {
                WorkflowScenario scenarioA = scenarios.get(i);
                // Collect all steps from scenarioA
                List<WorkflowStep> stepsA = collectAllSteps(scenarioA);
                for (int j = i + 1; j < scenarios.size(); j++) {
                    WorkflowScenario scenarioB = scenarios.get(j);
                    List<WorkflowStep> stepsB = collectAllSteps(scenarioB);
                    // Try to find any matching field dependency between scenarioA and scenarioB
                    for (WorkflowStep stepA : stepsA) {
                        // Check each output field of A
                        for (Map.Entry<String, String> outEntry : stepA.getOutputFields().entrySet()) {
                            String key = outEntry.getKey();
                            if (ignoreKeys.contains(key)) continue;
                            String value = outEntry.getValue();
                            if (value == null || value.isEmpty()) continue;
                            // Look for the same key/value in any input of scenarioB
                            for (WorkflowStep stepB : stepsB) {
                                String inVal = stepB.getInputFields().get(key);
                                if (inVal != null && !inVal.isEmpty() && inVal.equals(value)) {
                                    // Found a dependency: output from stepA matches input of stepB
                                    log.info("Data dependency found: " + key + "=" + value
                                            + " from " + stepA.getServiceName() + "->" + stepB.getServiceName()
                                            + " (merging scenarios of trace " + stepA.getTraceId()
                                            + " and trace " + stepB.getTraceId() + ")");
                                    // Merge scenarioB into scenarioA by attaching stepB (or its root) under stepA
                                    // Identify the root of scenarioB's trace (stepB might not be root if it's a deeper span,
                                    // but logically the entire scenarioB will attach under scenarioA at stepA).
                                    WorkflowStep rootB = stepB;
                                    while (rootB.getParent() != null) {
                                        rootB = rootB.getParent();
                                    }
                                    // Attach scenarioB's root to stepA
                                    stepA.addChild(rootB);
                                    // Merge the scenarios' metadata
                                    scenarioA.addTraceId(stepB.getTraceId());
                                    for (String tid : scenarioB.getTraceIds()) {
                                        scenarioA.addTraceId(tid);
                                    }
                                    // Transfer any other root steps from B (if any) to scenarioA
                                    for (WorkflowStep otherRoot : scenarioB.getRootSteps()) {
                                        if (otherRoot != rootB) {
                                            scenarioA.addRootStep(otherRoot);
                                        }
                                    }
                                    // Remove scenarioB from the list as it is now merged into A
                                    scenarios.remove(j);
                                    mergedSomething = true;
                                    // Restart merging process since list changed
                                    break outerLoop;
                                }
                            }
                        }
                    }
                    // Also check the opposite direction (scenarioB outputs into scenarioA inputs)
                    // in case the dependency is reversed.
                    for (WorkflowStep stepB : stepsB) {
                        for (Map.Entry<String, String> outEntry : stepB.getOutputFields().entrySet()) {
                            String key = outEntry.getKey();
                            if (ignoreKeys.contains(key)) continue;
                            String value = outEntry.getValue();
                            if (value == null || value.isEmpty()) continue;
                            for (WorkflowStep stepA : stepsA) {
                                String inVal = stepA.getInputFields().get(key);
                                if (inVal != null && !inVal.isEmpty() && inVal.equals(value)) {
                                    // Found reverse dependency: output from stepB matches input of stepA
                                    log.info("Data dependency found: " + key + "=" + value
                                            + " from " + stepB.getServiceName() + "->" + stepA.getServiceName()
                                            + " (merging scenarios of trace " + stepB.getTraceId()
                                            + " and trace " + stepA.getTraceId() + ")");
                                    // Merge scenarioA into scenarioB by attaching scenarioA's root under stepB
                                    WorkflowStep rootA = stepA;
                                    while (rootA.getParent() != null) {
                                        rootA = rootA.getParent();
                                    }
                                    stepB.addChild(rootA);
                                    scenarioB.addTraceId(stepA.getTraceId());
                                    for (String tid : scenarioA.getTraceIds()) {
                                        scenarioB.addTraceId(tid);
                                    }
                                    for (WorkflowStep otherRoot : scenarioA.getRootSteps()) {
                                        if (otherRoot != rootA) {
                                            scenarioB.addRootStep(otherRoot);
                                        }
                                    }
                                    scenarios.remove(i);
                                    mergedSomething = true;
                                    break outerLoop;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Utility method to collect all WorkflowStep objects in a scenario into a list.
     * This performs a depth-first traversal of the scenario's step tree.
     */
    private static List<WorkflowStep> collectAllSteps(WorkflowScenario scenario) {
        List<WorkflowStep> allSteps = new ArrayList<>();
        for (WorkflowStep root : scenario.getRootSteps()) {
            collectStepsRecursive(root, allSteps);
        }
        return allSteps;
    }

    /** Recursive helper for collectAllSteps. */
    private static void collectStepsRecursive(WorkflowStep step, List<WorkflowStep> list) {
        list.add(step);
        for (WorkflowStep child : step.getChildren()) {
            collectStepsRecursive(child, list);
        }
    }


}
