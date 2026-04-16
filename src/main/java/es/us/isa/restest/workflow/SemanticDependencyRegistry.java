package es.us.isa.restest.workflow;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.specification.OpenAPISpecification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Builds a queryable dictionary of semantic parameter dependencies between APIs.
 *
 * <h3>Two-Pass Build Architecture</h3>
 * <ol>
 *   <li><b>Pass 1 — Heuristic Discovery:</b> registers producers and consumers using
 *       naming conventions. JsonPath defaults to {@code "data.id"}.</li>
 *   <li><b>Pass 2 — Schema Refinement:</b> for every producer registered in Pass 1,
 *       traverses the OpenAPI 200/201 response schema to locate the exact JSON path
 *       of the ID field, replacing the hardcoded fallback.</li>
 * </ol>
 *
 * <p><b>Consumers</b> are parameters whose name matches an ID pattern (e.g. {@code orderId},
 * {@code tripId}, {@code account_uuid}).
 *
 * <p><b>Producers</b> are operations that are likely to <em>create or return</em> the entity
 * that owns the ID.  Detection heuristics (in priority order):
 * <ol>
 *   <li>POST/PUT on a path whose resource noun matches the ID stem
 *       (e.g. POST {@code /order} produces {@code orderId}).</li>
 *   <li>Any operation whose own response parameters or path contains the same ID field
 *       (weaker signal, kept for completeness).</li>
 * </ol>
 *
 * The registry is immutable once built; it is safe to share across threads.
 */
public class SemanticDependencyRegistry {

    private static final Logger log = LogManager.getLogger(SemanticDependencyRegistry.class);

    private static final Pattern ID_SUFFIX = Pattern.compile("(?i)^.+(id|Id|ID|uuid|Uuid|UUID)$");

    private static final String DEFAULT_JSON_PATH = "data.id";
    private static final int MAX_SCHEMA_DEPTH = 8;

    /**
     * Key: normalised consumer API key ({@code "post /api/v1/orderservice/order"}).
     * Value: map of paramName → {@link ProducerBinding}.
     */
    private final Map<String, Map<String, ProducerBinding>> consumerIndex = new HashMap<>();

    /** All known producer API keys, keyed by the normalised ID stem they produce. */
    private final Map<String, List<ProducerBinding>> producersByIdStem = new HashMap<>();

    public static class ProducerBinding {
        public final String serviceName;
        public final String apiKey;
        /** Mutable so that Pass 2 (schema refinement) can upgrade the default path. */
        public String jsonPath;
        /** True if Pass 2 resolved the jsonPath from the actual OpenAPI response schema. */
        public boolean schemaResolved;

        public ProducerBinding(String serviceName, String apiKey, String jsonPath) {
            this.serviceName = serviceName;
            this.apiKey = apiKey;
            this.jsonPath = jsonPath;
            this.schemaResolved = false;
        }

        @Override
        public String toString() {
            return apiKey + " → " + jsonPath + (schemaResolved ? " [schema]" : " [heuristic]");
        }
    }

    private SemanticDependencyRegistry() { }

    /**
     * Builds the registry from the full set of per-service test configurations.
     * Backwards-compatible overload that skips Pass 2 (trace refinement).
     *
     * @param serviceConfigs map of serviceName → parsed {@link TestConfigurationObject}
     * @return a populated registry using heuristic "data.id" fallback paths
     */
    public static SemanticDependencyRegistry build(Map<String, TestConfigurationObject> serviceConfigs) {
        return build(serviceConfigs, null, null);
    }

    /**
     * Builds the registry using the Two-Pass Architecture.
     * Backwards-compatible overload that accepts OpenAPI specs but also needs traces
     * for the new trace-driven Pass 2.
     *
     * @param serviceConfigs map of serviceName → parsed {@link TestConfigurationObject}
     * @param serviceSpecs   map of serviceName → parsed {@link OpenAPISpecification} (nullable, kept for API compat)
     * @return a populated registry
     */
    public static SemanticDependencyRegistry build(Map<String, TestConfigurationObject> serviceConfigs,
                                                   Map<String, OpenAPISpecification> serviceSpecs) {
        return build(serviceConfigs, serviceSpecs, null);
    }

    /**
     * Builds the registry using the Two-Pass Architecture.
     *
     * <ul>
     *   <li><b>Pass 1:</b> Heuristic Discovery — populates {@code producersByIdStem}
     *       and {@code consumerIndex} with {@code jsonPath = "data.id"} fallback.</li>
     *   <li><b>Pass 2:</b> Trace-Driven Refinement — uses actual recorded trace
     *       response bodies to locate the exact JSON path of ID fields, completely
     *       bypassing the broken OpenAPI schema resolution.</li>
     * </ul>
     *
     * @param serviceConfigs map of serviceName → parsed {@link TestConfigurationObject}
     * @param serviceSpecs   map of serviceName → parsed {@link OpenAPISpecification} (nullable, kept for API compat)
     * @param scenarios      list of recorded {@link WorkflowScenario}s with trace data (nullable)
     * @return a populated registry
     */
    public static SemanticDependencyRegistry build(Map<String, TestConfigurationObject> serviceConfigs,
                                                   Map<String, OpenAPISpecification> serviceSpecs,
                                                   List<WorkflowScenario> scenarios) {
        SemanticDependencyRegistry reg = new SemanticDependencyRegistry();

        // ═══════════════════════════════════════════════════════════════
        // Pass 1: Heuristic Discovery
        // ═══════════════════════════════════════════════════════════════

        // Pass 1a: discover all producers (POST/PUT operations on entity resources)
        for (Map.Entry<String, TestConfigurationObject> svcEntry : serviceConfigs.entrySet()) {
            String svcName = svcEntry.getKey();
            TestConfigurationObject tco = svcEntry.getValue();
            if (tco.getTestConfiguration() == null
                    || tco.getTestConfiguration().getOperations() == null) continue;

            for (Operation op : tco.getTestConfiguration().getOperations()) {
                String method = op.getMethod() != null ? op.getMethod().toLowerCase(Locale.ROOT) : "";
                String path = op.getTestPath() != null ? op.getTestPath() : "";
                String apiKey = method + " " + path;

                registerProducers(reg, svcName, apiKey, method, path, op);
            }
        }

        // Pass 1b: for every consumer parameter, find the best producer
        for (Map.Entry<String, TestConfigurationObject> svcEntry : serviceConfigs.entrySet()) {
            String svcName = svcEntry.getKey();
            TestConfigurationObject tco = svcEntry.getValue();
            if (tco.getTestConfiguration() == null
                    || tco.getTestConfiguration().getOperations() == null) continue;

            for (Operation op : tco.getTestConfiguration().getOperations()) {
                String method = op.getMethod() != null ? op.getMethod().toLowerCase(Locale.ROOT) : "";
                String path = op.getTestPath() != null ? op.getTestPath() : "";
                String consumerKey = method + " " + path;

                if (op.getTestParameters() == null) continue;

                for (TestParameter tp : op.getTestParameters()) {
                    String pName = tp.getName();
                    if (pName == null || !ID_SUFFIX.matcher(pName).matches()) continue;

                    String stem = normaliseIdStem(pName);
                    List<ProducerBinding> candidates = reg.producersByIdStem.get(stem);
                    if (candidates == null || candidates.isEmpty()) continue;

                    // Pick the best producer (prefer a different service, then POST over GET)
                    ProducerBinding best = candidates.get(0);
                    for (ProducerBinding pb : candidates) {
                        if (!pb.serviceName.equals(svcName)) {
                            best = pb;
                            break;
                        }
                    }

                    reg.consumerIndex
                            .computeIfAbsent(consumerKey, k -> new LinkedHashMap<>())
                            .put(pName, best);
                }
            }
        }

        // ═══════════════════════════════════════════════════════════════
        // Pass 2: Trace-Driven JSON Path Refinement
        // ═══════════════════════════════════════════════════════════════
        if (scenarios != null && !scenarios.isEmpty()) {
            List<WorkflowStep> allSteps = flattenAllSteps(scenarios);
            log.info("Pass 2: running trace-driven refinement with {} trace steps", allSteps.size());
            refineJsonPathsFromTraces(reg, allSteps);
        } else {
            log.info("Pass 2 skipped: no trace scenarios provided; using heuristic '{}' for all producers",
                    DEFAULT_JSON_PATH);
        }

        log.info("SemanticDependencyRegistry built: {} consumer APIs, {} ID stems with producers",
                reg.consumerIndex.size(), reg.producersByIdStem.size());
        for (Map.Entry<String, Map<String, ProducerBinding>> e : reg.consumerIndex.entrySet()) {
            for (Map.Entry<String, ProducerBinding> dep : e.getValue().entrySet()) {
                log.debug("  {} param '{}' ← {}", e.getKey(), dep.getKey(), dep.getValue());
            }
        }

        return reg;
    }

    /**
     * Looks up a producer for a given consumer API and parameter name.
     *
     * @param consumerApiKey normalised API key (e.g. {@code "get /api/v1/orderservice/order/{orderId}"})
     * @param paramName      the parameter name (e.g. {@code "orderId"})
     * @return the {@link ProducerBinding}, or {@code null} if none is registered
     */
    public ProducerBinding findProducer(String consumerApiKey, String paramName) {
        Map<String, ProducerBinding> params = consumerIndex.get(consumerApiKey);
        return params != null ? params.get(paramName) : null;
    }

    /**
     * Returns all candidate producers that could provide a value for the given
     * parameter, based purely on the parameter's entity stem.
     *
     * <p>Unlike {@link #findProducer(String, String)} which returns a single
     * pre-selected binding for a specific consumer API, this method returns
     * <b>every</b> registered producer for the stem, allowing the caller to
     * match against the actual preceding test-case history at runtime.
     *
     * @param paramName the consumer parameter name (e.g. {@code "orderId"})
     * @return all producer bindings whose stem matches, or an empty list
     */
    public List<ProducerBinding> getCandidateProducers(String paramName) {
        String stem = normaliseIdStem(paramName);
        if (stem == null) return Collections.emptyList();
        List<ProducerBinding> candidates = producersByIdStem.get(stem);
        return candidates != null ? Collections.unmodifiableList(candidates) : Collections.emptyList();
    }

    /**
     * Returns all registered consumer entries (for diagnostics / logging).
     */
    public Map<String, Map<String, ProducerBinding>> getAllConsumerBindings() {
        return Collections.unmodifiableMap(consumerIndex);
    }

    /**
     * Serializes the entire consumer index and producer stems to a human-readable
     * JSON file for manual auditing of the heuristic stemming rules.
     *
     * @param filePath the output file path (e.g. {@code "target/semantic-registry-dump.json"})
     */
    public void dumpRegistryToFile(String filePath) {
        try {
            JSONObject root = new JSONObject();

            // Consumer index
            JSONObject consumers = new JSONObject();
            for (Map.Entry<String, Map<String, ProducerBinding>> api : consumerIndex.entrySet()) {
                JSONObject params = new JSONObject();
                for (Map.Entry<String, ProducerBinding> param : api.getValue().entrySet()) {
                    JSONObject binding = new JSONObject();
                    binding.put("producerService", param.getValue().serviceName);
                    binding.put("producerApiKey", param.getValue().apiKey);
                    binding.put("jsonPath", param.getValue().jsonPath);
                    binding.put("schemaResolved", param.getValue().schemaResolved);
                    params.put(param.getKey(), binding);
                }
                consumers.put(api.getKey(), params);
            }
            root.put("consumerIndex", consumers);

            // Producer stems
            JSONObject producers = new JSONObject();
            int schemaResolvedCount = 0;
            int heuristicCount = 0;
            for (Map.Entry<String, List<ProducerBinding>> stem : producersByIdStem.entrySet()) {
                org.json.JSONArray arr = new org.json.JSONArray();
                for (ProducerBinding pb : stem.getValue()) {
                    JSONObject entry = new JSONObject();
                    entry.put("serviceName", pb.serviceName);
                    entry.put("apiKey", pb.apiKey);
                    entry.put("jsonPath", pb.jsonPath);
                    entry.put("schemaResolved", pb.schemaResolved);
                    arr.put(entry);
                    if (pb.schemaResolved) schemaResolvedCount++; else heuristicCount++;
                }
                producers.put(stem.getKey(), arr);
            }
            root.put("producersByIdStem", producers);

            // Summary stats
            JSONObject stats = new JSONObject();
            stats.put("totalConsumerApis", consumerIndex.size());
            stats.put("totalProducerStems", producersByIdStem.size());
            int totalBindings = 0;
            for (Map<String, ProducerBinding> m : consumerIndex.values()) totalBindings += m.size();
            stats.put("totalParamBindings", totalBindings);
            stats.put("pass2_schemaResolved", schemaResolvedCount);
            stats.put("pass2_heuristicFallback", heuristicCount);
            root.put("_stats", stats);

            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, root.toString(2));
            log.info("SemanticDependencyRegistry dumped to {}", filePath);
        } catch (IOException e) {
            log.warn("Failed to dump registry to {}: {}", filePath, e.getMessage());
        }
    }

    /**
     * Checks whether {@code consumerStep} has ANY parameter that can be satisfied by
     * {@code producerStep} according to the registry's semantic rules.
     *
     * <p>This is used by the Scenario Shattering / Partitioning optimizer to build
     * a directed dependency graph over the root steps of a merged scenario. An edge
     * from producer → consumer is drawn when this method returns {@code true}.
     *
     * @param consumerStep the downstream WorkflowStep whose parameters may depend on the producer
     * @param producerStep the upstream WorkflowStep that may produce values for the consumer
     * @return true if at least one consumer parameter has a registered producer that matches
     *         the producer step's API key
     */
    public boolean hasDirectedDependency(WorkflowStep consumerStep, WorkflowStep producerStep) {
        String consumerApiKey = buildApiKey(consumerStep);
        String producerApiKey = buildApiKey(producerStep);
        if (consumerApiKey == null || producerApiKey == null) return false;

        Map<String, ProducerBinding> paramBindings = consumerIndex.get(consumerApiKey);
        if (paramBindings == null || paramBindings.isEmpty()) return false;

        for (ProducerBinding binding : paramBindings.values()) {
            if (producerApiKey.equals(binding.apiKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a normalised API key ({@code "post /api/v1/orderservice/order"}) from
     * a WorkflowStep's span attributes — mirrors the key format used by the registry.
     */
    private static String buildApiKey(WorkflowStep step) {
        Map<String, String> out = step.getOutputFields();
        Map<String, String> in = step.getInputFields();

        String method = out.get("http.method");
        if (method == null) method = in.get("http.method");
        if (method == null) {
            String opName = step.getOperationName();
            if (opName != null && opName.contains(" ")) {
                method = opName.substring(0, opName.indexOf(' '));
            }
        }

        String path = out.get("http.target");
        if (path == null) path = in.get("http.target");
        if (path == null) {
            String url = out.get("http.url");
            if (url == null) url = in.get("http.url");
            if (url != null) {
                try {
                    path = new java.net.URL(url).getPath();
                } catch (Exception ignored) {
                    int s = url.indexOf("://");
                    if (s >= 0) {
                        int p = url.indexOf("/", s + 3);
                        if (p >= 0) path = url.substring(p);
                    }
                }
            }
        }
        if (path == null) {
            String opName = step.getOperationName();
            if (opName != null && opName.contains(" ")) {
                path = opName.substring(opName.indexOf(' ') + 1);
            }
        }

        if (method == null || path == null) return null;

        // Strip query string from path
        int q = path.indexOf('?');
        if (q >= 0) path = path.substring(0, q);

        return method.toLowerCase(Locale.ROOT) + " " + path;
    }

    // ── Pass 2: Trace-Driven JSON Path Resolution ──────────────────────────

    /**
     * Iterates through every {@link ProducerBinding} in {@code producersByIdStem}
     * and uses the actual recorded trace response bodies to resolve the exact
     * JSON path of the ID field.  This replaces the previous schema-based approach
     * which failed due to broken {@code $ref} names and generic type erasure.
     *
     * <p>For each producer, we find a matching trace span, extract its
     * {@code http.response.body}, and run a DFS to locate the ID field path.
     */
    private static void refineJsonPathsFromTraces(SemanticDependencyRegistry reg,
                                                  List<WorkflowStep> allTraceSteps) {
        int refined = 0;
        int skipped = 0;

        // Index trace steps by normalised API key for fast lookup
        Map<String, List<WorkflowStep>> stepsByApiKey = new HashMap<>();
        for (WorkflowStep step : allTraceSteps) {
            String apiKey = buildApiKey(step);
            if (apiKey != null) {
                stepsByApiKey.computeIfAbsent(apiKey, k -> new ArrayList<>()).add(step);
            }
        }

        for (Map.Entry<String, List<ProducerBinding>> stemEntry : reg.producersByIdStem.entrySet()) {
            String stem = stemEntry.getKey();
            for (ProducerBinding pb : stemEntry.getValue()) {
                // Find a trace step that matches this producer's API key
                List<WorkflowStep> matchingSteps = stepsByApiKey.get(pb.apiKey);
                if (matchingSteps == null || matchingSteps.isEmpty()) {
                    // Try fuzzy match: normalise path params to {}
                    String normKey = pb.apiKey.replaceAll("\\{[^}]+}", "{}");
                    for (Map.Entry<String, List<WorkflowStep>> e : stepsByApiKey.entrySet()) {
                        if (e.getKey().replaceAll("\\{[^}]+}", "{}").equals(normKey)) {
                            matchingSteps = e.getValue();
                            break;
                        }
                    }
                }
                if (matchingSteps == null || matchingSteps.isEmpty()) {
                    skipped++;
                    log.trace("Pass 2 (trace): no trace data for {} — keeping '{}'", pb.apiKey, pb.jsonPath);
                    continue;
                }

                // Try each matching step until we find a usable response body
                String discoveredPath = null;
                for (WorkflowStep traceStep : matchingSteps) {
                    String responseBody = traceStep.getOutputFields().get("http.response.body");
                    if (responseBody == null || responseBody.isBlank()) continue;

                    discoveredPath = findIdJsonPathInPayload(responseBody, stem);
                    if (discoveredPath != null) break;
                }

                if (discoveredPath != null) {
                    String oldPath = pb.jsonPath;
                    pb.jsonPath = discoveredPath;
                    pb.schemaResolved = true;
                    refined++;
                    log.debug("Pass 2 (trace) refined: {} stem '{}': '{}' → '{}'",
                            pb.apiKey, stem, oldPath, discoveredPath);
                } else {
                    skipped++;
                    log.trace("Pass 2 (trace): ID not found in response body for {} stem '{}' — keeping '{}'",
                            pb.apiKey, stem, pb.jsonPath);
                }
            }
        }

        log.info("Pass 2 trace-driven refinement complete: {} refined, {} kept heuristic default", refined, skipped);
    }

    /**
     * Parses a real JSON response body and searches for the first field whose
     * name matches an ID pattern for the given entity stem.  Returns the
     * dot-notation JSON path (e.g. {@code "data.id"}).
     *
     * @param jsonBody   the raw JSON response string
     * @param targetStem the normalised entity stem (e.g. "order", "trip")
     * @return the JSON path, or {@code null} if not found
     */
    private static String findIdJsonPathInPayload(String jsonBody, String targetStem) {
        try {
            String trimmed = jsonBody.trim();
            if (trimmed.startsWith("{")) {
                JSONObject root = new JSONObject(trimmed);
                return dfsForIdField(root, targetStem, "", 0);
            } else if (trimmed.startsWith("[")) {
                JSONArray arr = new JSONArray(trimmed);
                if (arr.length() > 0 && arr.get(0) instanceof JSONObject) {
                    return dfsForIdField(arr.getJSONObject(0), targetStem, "[0]", 0);
                }
            }
        } catch (Exception e) {
            log.trace("Failed to parse response body for stem '{}': {}", targetStem, e.getMessage());
        }
        return null;
    }

    /**
     * DFS traversal of a {@link JSONObject} to find the first ID-like field
     * that matches the target entity stem.
     *
     * <p><b>Matching priority per level:</b>
     * <ol>
     *   <li>Exact ID match ({@code id}, {@code uuid}, {@code {stem}Id}, etc.)</li>
     *   <li>Recurse into nested objects</li>
     *   <li>Recurse into array elements (first element only)</li>
     * </ol>
     */
    private static String dfsForIdField(JSONObject obj, String targetStem,
                                        String currentPath, int depth) {
        if (obj == null || depth > MAX_SCHEMA_DEPTH) return null;

        // First pass: check direct properties for an ID match
        for (String key : obj.keySet()) {
            if (isIdMatch(key, targetStem)) {
                String path = currentPath.isEmpty() ? key : currentPath + "." + key;
                return path;
            }
        }

        // Second pass: recurse into nested objects and arrays
        for (String key : obj.keySet()) {
            Object value = obj.opt(key);
            if (value == null) continue;

            String childPath = currentPath.isEmpty() ? key : currentPath + "." + key;

            if (value instanceof JSONObject) {
                String found = dfsForIdField((JSONObject) value, targetStem, childPath, depth + 1);
                if (found != null) return found;
            } else if (value instanceof JSONArray) {
                JSONArray arr = (JSONArray) value;
                if (arr.length() > 0 && arr.get(0) instanceof JSONObject) {
                    String found = dfsForIdField(arr.getJSONObject(0), targetStem,
                            childPath + "[0]", depth + 1);
                    if (found != null) return found;
                }
            }
        }

        return null;
    }

    // ── Dynamic Payload Traverser (public API) ──────────────────────────────

    /**
     * Traverses a real JSON response body using DFS to find the exact JSON path
     * of a leaf node whose string representation matches {@code targetValue}.
     *
     * <p>This is the core of the "Trace-Driven Double Traversal Algorithm":
     * given a concrete value that was observed flowing from Step A to Step B,
     * this method locates where that value lives in Step A's response body,
     * yielding a precise JSON path for runtime extraction.
     *
     * @param jsonBody    the raw JSON response body string from the producer step
     * @param targetValue the concrete value to search for (e.g. "abc-123")
     * @return the dot-notation JSON path (e.g. "data.id"), or {@code null} if not found
     */
    public static String findJsonPathFromRealPayload(String jsonBody, String targetValue) {
        if (jsonBody == null || jsonBody.isBlank()
                || targetValue == null || targetValue.isBlank()) {
            return null;
        }
        try {
            String trimmed = jsonBody.trim();
            if (trimmed.startsWith("{")) {
                JSONObject root = new JSONObject(trimmed);
                return dfsForValue(root, targetValue, "", 0);
            } else if (trimmed.startsWith("[")) {
                JSONArray arr = new JSONArray(trimmed);
                if (arr.length() > 0 && arr.get(0) instanceof JSONObject) {
                    return dfsForValue(arr.getJSONObject(0), targetValue, "[0]", 0);
                }
            }
        } catch (Exception e) {
            log.trace("[Dynamic Path Finder] Failed to parse payload: {}", e.getMessage());
        }
        return null;
    }

    /**
     * DFS traversal that visits every leaf node searching for a value match.
     * Halts and returns the path on first match.
     */
    private static String dfsForValue(JSONObject obj, String targetValue,
                                      String currentPath, int depth) {
        if (obj == null || depth > MAX_SCHEMA_DEPTH) return null;

        for (String key : obj.keySet()) {
            Object value = obj.opt(key);
            if (value == null) continue;

            String childPath = currentPath.isEmpty() ? key : currentPath + "." + key;

            if (value instanceof JSONObject) {
                String found = dfsForValue((JSONObject) value, targetValue, childPath, depth + 1);
                if (found != null) return found;
            } else if (value instanceof JSONArray) {
                JSONArray arr = (JSONArray) value;
                for (int i = 0; i < arr.length(); i++) {
                    Object elem = arr.get(i);
                    if (elem instanceof JSONObject) {
                        String found = dfsForValue((JSONObject) elem, targetValue,
                                childPath + "[" + i + "]", depth + 1);
                        if (found != null) return found;
                    } else if (matchesValue(elem, targetValue)) {
                        return childPath + "[" + i + "]";
                    }
                }
            } else {
                if (matchesValue(value, targetValue)) {
                    return childPath;
                }
            }
        }
        return null;
    }

    /**
     * Compares a JSON leaf value against the target, tolerant of type differences
     * (e.g. numeric 123 vs string "123").
     */
    private static boolean matchesValue(Object jsonValue, String targetValue) {
        if (jsonValue == null || targetValue == null) return false;
        String asString = jsonValue.toString();
        if (asString.equalsIgnoreCase(targetValue)) return true;
        // Numeric tolerance: "123.0" should match "123"
        try {
            double jsonNum = Double.parseDouble(asString);
            double targetNum = Double.parseDouble(targetValue);
            return Double.compare(jsonNum, targetNum) == 0;
        } catch (NumberFormatException ignored) {
            // not numeric, string comparison was definitive
        }
        return false;
    }

    /**
     * Checks whether a property name represents an ID field for the given entity stem.
     *
     * <p>Matches (case-insensitive):
     * <ul>
     *   <li>{@code id}, {@code uuid}</li>
     *   <li>{@code {stem}Id}, {@code {stem}_id}, {@code {stem}UUID}</li>
     * </ul>
     */
    private static boolean isIdMatch(String propName, String targetStem) {
        if (propName == null) return false;
        String lower = propName.toLowerCase(Locale.ROOT);

        if ("id".equals(lower) || "uuid".equals(lower)) return true;

        String stemLower = targetStem.toLowerCase(Locale.ROOT);
        if (lower.equals(stemLower + "id")
                || lower.equals(stemLower + "_id")
                || lower.equals(stemLower + "uuid")
                || lower.equals(stemLower + "_uuid")) {
            return true;
        }

        return false;
    }

    /**
     * Collects all {@link WorkflowStep} nodes from a list of scenarios into a
     * flat list, recursively including children.
     */
    public static List<WorkflowStep> flattenAllSteps(List<WorkflowScenario> scenarios) {
        List<WorkflowStep> all = new ArrayList<>();
        for (WorkflowScenario scenario : scenarios) {
            for (WorkflowStep root : scenario.getRootSteps()) {
                collectStepsRecursively(root, all);
            }
        }
        return all;
    }

    private static void collectStepsRecursively(WorkflowStep step, List<WorkflowStep> acc) {
        acc.add(step);
        if (step.getChildren() != null) {
            for (WorkflowStep child : step.getChildren()) {
                collectStepsRecursively(child, acc);
            }
        }
    }

    // ── internals ──────────────────────────────────────────────────────────

    private static void registerProducers(SemanticDependencyRegistry reg,
                                          String svcName, String apiKey,
                                          String method, String path,
                                          Operation op) {
        // Heuristic 1: POST or PUT on an entity resource produces that entity's ID.
        // e.g. POST /api/v1/orderservice/order  → produces "orderId" at "data.id"
        //      POST /api/v1/consignservice/consigns → produces "consignId"
        if ("post".equals(method) || "put".equals(method)) {
            String resourceNoun = extractTrailingNoun(path);
            if (resourceNoun != null) {
                String stem = normaliseNounToStem(resourceNoun);
                if (stem != null) {
                    ProducerBinding pb = new ProducerBinding(svcName, apiKey, "data.id");
                    reg.producersByIdStem
                            .computeIfAbsent(stem, k -> new ArrayList<>())
                            .add(pb);
                    log.debug("Producer registered: {} ({}) → stem '{}'", apiKey, svcName, stem);
                }
            }
        }

        // Heuristic 2: any operation that has an ID-like parameter in its OWN params
        // is also a potential producer of that ID (it received it, so it can output it).
        // Only register if no stronger producer already exists for that stem.
        if (op.getTestParameters() != null) {
            for (TestParameter tp : op.getTestParameters()) {
                String pName = tp.getName();
                if (pName == null || !ID_SUFFIX.matcher(pName).matches()) continue;
                if (!"path".equalsIgnoreCase(tp.getIn())) continue;

                String stem = normaliseIdStem(pName);
                if (!reg.producersByIdStem.containsKey(stem)) {
                    String jsonPath = "data." + pName;
                    ProducerBinding pb = new ProducerBinding(svcName, apiKey, jsonPath);
                    reg.producersByIdStem
                            .computeIfAbsent(stem, k -> new ArrayList<>())
                            .add(pb);
                }
            }
        }
    }

    /**
     * Extracts the trailing resource noun from a REST path, ignoring path parameters.
     * <pre>
     *   /api/v1/orderservice/order         → "order"
     *   /api/v1/consignservice/consigns    → "consigns"
     *   /api/v1/orderservice/order/{id}    → "order"  (skips the {id} segment)
     * </pre>
     */
    static String extractTrailingNoun(String path) {
        if (path == null || path.isEmpty()) return null;
        String[] segments = path.split("/");
        for (int i = segments.length - 1; i >= 0; i--) {
            String seg = segments[i].trim();
            if (seg.isEmpty() || seg.startsWith("{")) continue;
            if (seg.matches("v\\d+|api|actuator")) continue;
            return seg;
        }
        return null;
    }

    /**
     * Normalises a resource noun to a canonical stem used as the dictionary key.
     * <pre>
     *   "orders" → "order", "consigns" → "consign", "trips" → "trip"
     * </pre>
     */
    static String normaliseNounToStem(String noun) {
        if (noun == null || noun.isEmpty()) return null;
        String lower = noun.toLowerCase(Locale.ROOT);
        if (lower.endsWith("s") && lower.length() > 2) {
            lower = lower.substring(0, lower.length() - 1);
        }
        return lower;
    }

    /**
     * Normalises a parameter name like "orderId" or "trip_uuid" to its entity stem.
     * <pre>
     *   "orderId"    → "order"
     *   "tripId"     → "trip"
     *   "accountId"  → "account"
     *   "contactsId" → "contact"
     * </pre>
     */
    public static String normaliseIdStem(String paramName) {
        if (paramName == null) return null;
        String stem = paramName
                .replaceAll("(?i)(id|uuid)$", "")
                .replaceAll("_$", "");
        if (stem.isEmpty()) return null;
        stem = stem.toLowerCase(Locale.ROOT);
        if (stem.endsWith("s") && stem.length() > 2) {
            stem = stem.substring(0, stem.length() - 1);
        }
        return stem;
    }
}
