package es.us.isa.restest.validation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-API cache of soft-error detection rules.
 * <p>
 * On the first 2XX response for a given API (identified by {@code METHOD /path}),
 * the LLM is asked to both evaluate the response AND produce a reusable rule set.
 * Subsequent responses for the same API are evaluated programmatically against
 * the cached rule, eliminating redundant LLM calls.
 * <p>
 * If a response contains a pattern not covered by the cached rule, a follow-up
 * LLM call is made and the rule is extended with the new pattern.
 * <p>
 * Thread-safe via {@link ConcurrentHashMap}. Persisted to a JSON file so all
 * test classes in the same JVM run share one cache instance.
 */
public class SoftErrorRuleCache {

    private static final Logger logger = LogManager.getLogger(SoftErrorRuleCache.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<String, SoftErrorRuleCache> instances = new ConcurrentHashMap<>();

    private final String filePath;
    private final ConcurrentHashMap<String, SoftErrorRule> rules = new ConcurrentHashMap<>();

    private SoftErrorRuleCache(String filePath) {
        this.filePath = filePath;
        load();
    }

    /**
     * Get (or create) a singleton cache instance backed by the given file path.
     */
    public static synchronized SoftErrorRuleCache getInstance(String filePath) {
        return instances.computeIfAbsent(filePath, SoftErrorRuleCache::new);
    }

    // ------------------------------------------------------------------
    // Rule model
    // ------------------------------------------------------------------

    /**
     * A reusable rule describing how to detect soft errors for one API.
     */
    public static class SoftErrorRule {
        private List<FieldCheck> fieldChecks = new ArrayList<>();
        private List<String> failureMessageFields = new ArrayList<>();
        private boolean nullDataIsFailure = true;
        private List<PatternEntry> knownPatterns = new ArrayList<>();

        public List<FieldCheck> getFieldChecks() { return fieldChecks; }
        public void setFieldChecks(List<FieldCheck> fieldChecks) { this.fieldChecks = fieldChecks; }
        public List<String> getFailureMessageFields() { return failureMessageFields; }
        public void setFailureMessageFields(List<String> f) { this.failureMessageFields = f; }
        public boolean isNullDataIsFailure() { return nullDataIsFailure; }
        public void setNullDataIsFailure(boolean v) { this.nullDataIsFailure = v; }
        public List<PatternEntry> getKnownPatterns() { return knownPatterns; }
        public void setKnownPatterns(List<PatternEntry> p) { this.knownPatterns = p; }
    }

    /**
     * A single field-level check: which values of a top-level JSON field
     * indicate failure vs success.
     */
    public static class FieldCheck {
        private String field;
        private List<String> failureValues = new ArrayList<>();
        private List<String> successValues = new ArrayList<>();

        public FieldCheck() {}
        public FieldCheck(String field, List<String> failureValues, List<String> successValues) {
            this.field = field;
            this.failureValues = failureValues;
            this.successValues = successValues;
        }

        public String getField() { return field; }
        public void setField(String f) { this.field = f; }
        public List<String> getFailureValues() { return failureValues; }
        public void setFailureValues(List<String> v) { this.failureValues = v; }
        public List<String> getSuccessValues() { return successValues; }
        public void setSuccessValues(List<String> v) { this.successValues = v; }
    }

    /**
     * A known response pattern (a signature string) mapped to a cached outcome.
     */
    public static class PatternEntry {
        private String signature;
        private boolean failed;
        private String rcaTemplate;

        public PatternEntry() {}
        public PatternEntry(String signature, boolean failed, String rcaTemplate) {
            this.signature = signature;
            this.failed = failed;
            this.rcaTemplate = rcaTemplate;
        }

        public String getSignature() { return signature; }
        public void setSignature(String s) { this.signature = s; }
        public boolean isFailed() { return failed; }
        public void setFailed(boolean f) { this.failed = f; }
        public String getRcaTemplate() { return rcaTemplate; }
        public void setRcaTemplate(String r) { this.rcaTemplate = r; }
    }

    /**
     * Result returned by {@link #evaluate}.
     */
    public static class CachedValidationResult {
        private final boolean failed;
        private final String rca;
        private final String matchSource;

        public CachedValidationResult(boolean failed, String rca, String matchSource) {
            this.failed = failed;
            this.rca = rca;
            this.matchSource = matchSource;
        }

        public boolean isFailed() { return failed; }
        public String getRca() { return rca; }
        public String getMatchSource() { return matchSource; }
    }

    // ------------------------------------------------------------------
    // Core API
    // ------------------------------------------------------------------

    /**
     * Check whether a cached rule can classify this response without calling the LLM.
     *
     * @param apiKey       e.g. {@code "POST /api/v1/travelservice/trips/left"}
     * @param responseBody the raw JSON response body
     * @return present if the cache can determine pass/fail; empty if LLM is needed
     */
    public Optional<CachedValidationResult> evaluate(String apiKey, String responseBody) {
        SoftErrorRule rule = rules.get(apiKey);
        if (rule == null) {
            return Optional.empty();
        }

        try {
            JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();
            String signature = buildSignature(json, rule);

            // 1. Check known patterns first (exact signature match)
            for (PatternEntry pattern : rule.getKnownPatterns()) {
                if (pattern.getSignature().equals(signature)) {
                    String rca = expandRcaTemplate(pattern.getRcaTemplate(), json);
                    logger.debug("Cache HIT (pattern) for {}: signature={}, failed={}", apiKey, signature, pattern.isFailed());
                    return Optional.of(new CachedValidationResult(pattern.isFailed(), rca, "cached-pattern"));
                }
            }

            // 2. Apply field-check rules to determine outcome
            Optional<CachedValidationResult> fieldResult = evaluateFieldChecks(json, rule, apiKey);
            if (fieldResult.isPresent()) {
                // Auto-learn this signature so future identical responses are instant
                PatternEntry learned = new PatternEntry(signature, fieldResult.get().isFailed(), fieldResult.get().getRca());
                rule.getKnownPatterns().add(learned);
                save();
                logger.debug("Cache HIT (field-rule) for {}: signature={}, failed={}", apiKey, signature, fieldResult.get().isFailed());
                return fieldResult;
            }

            // 3. Could not determine -- need LLM
            logger.info("Cache MISS for {}: unknown signature={}", apiKey, signature);
            return Optional.empty();

        } catch (JsonSyntaxException | IllegalStateException e) {
            logger.warn("Could not parse response as JSON for cache evaluation: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Whether a rule already exists for this API key.
     */
    public boolean hasRule(String apiKey) {
        return rules.containsKey(apiKey);
    }

    /**
     * Store a new rule for an API.
     */
    public void registerRule(String apiKey, SoftErrorRule rule) {
        rules.put(apiKey, rule);
        save();
        logger.info("Registered soft-error rule for API: {} ({} field checks, {} known patterns)",
                apiKey, rule.getFieldChecks().size(), rule.getKnownPatterns().size());
    }

    /**
     * Extend an existing rule by adding a newly observed field value to the
     * confirmed success or failure list.  Called after the LLM classifies a
     * value that was not previously in the cache.
     */
    public void addObservedValue(String apiKey, String fieldName, String value, boolean isFailure) {
        SoftErrorRule rule = rules.get(apiKey);
        if (rule == null) return;
        for (FieldCheck check : rule.getFieldChecks()) {
            if (check.getField().equals(fieldName)) {
                String normalized = value.toLowerCase().trim();
                List<String> targetList = isFailure ? check.getFailureValues() : check.getSuccessValues();
                if (!targetList.stream().anyMatch(v -> v.toLowerCase().trim().equals(normalized))) {
                    targetList.add(value);
                    save();
                    logger.info("Extended rule for {}: field '{}' now has {} value '{}'",
                            apiKey, fieldName, isFailure ? "failure" : "success", value);
                }
                return;
            }
        }
    }

    /**
     * Add a newly discovered pattern to an existing rule.
     */
    public void addPattern(String apiKey, PatternEntry pattern) {
        SoftErrorRule rule = rules.get(apiKey);
        if (rule != null) {
            rule.getKnownPatterns().add(pattern);
            save();
            logger.info("Added pattern to rule for {}: signature={}, failed={}", apiKey, pattern.getSignature(), pattern.isFailed());
        }
    }

    /**
     * Build a signature string from the primary indicator field's actual value.
     * The signature uses exact observed values -- no heuristic classification.
     * Example: {@code "status=1"} or {@code "status=0"}
     */
    public String buildSignature(JsonObject json, SoftErrorRule rule) {
        List<String> parts = new ArrayList<>();

        for (FieldCheck check : rule.getFieldChecks()) {
            String field = check.getField();
            if (json.has(field)) {
                JsonElement val = json.get(field);
                if (val.isJsonNull()) {
                    parts.add(field + "=null");
                } else if (val.isJsonPrimitive()) {
                    parts.add(field + "=" + val.getAsString());
                } else if (val.isJsonArray()) {
                    parts.add(field + "=" + (val.getAsJsonArray().size() == 0 ? "empty-array" : "non-empty-array"));
                } else if (val.isJsonObject()) {
                    parts.add(field + "=object");
                }
            } else {
                parts.add(field + "=absent");
            }
        }

        return String.join(",", parts);
    }

    /**
     * Build a signature from a JSON string (convenience overload).
     */
    public String buildSignature(String apiKey, String responseBody) {
        SoftErrorRule rule = rules.get(apiKey);
        if (rule == null) return "";
        try {
            JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();
            return buildSignature(json, rule);
        } catch (Exception e) {
            return "";
        }
    }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    private Optional<CachedValidationResult> evaluateFieldChecks(JsonObject json, SoftErrorRule rule, String apiKey) {
        StringBuilder rcaBuilder = new StringBuilder();

        for (FieldCheck check : rule.getFieldChecks()) {
            if (!json.has(check.getField())) continue;
            JsonElement val = json.get(check.getField());
            if (val.isJsonNull()) continue;

            String strVal = val.isJsonPrimitive() ? val.getAsString().toLowerCase().trim() : val.toString().toLowerCase().trim();

            // Check confirmed failure values
            for (String fv : check.getFailureValues()) {
                if (strVal.equals(fv.toLowerCase().trim())) {
                    rcaBuilder.append("[Cached Rule] Field '").append(check.getField())
                            .append("' has confirmed failure value: ").append(strVal).append(". ");
                    appendMessageContext(json, rule, rcaBuilder);
                    return Optional.of(new CachedValidationResult(true, rcaBuilder.toString().trim(), "cached-field-rule"));
                }
            }

            // Check confirmed success values
            for (String sv : check.getSuccessValues()) {
                if (strVal.equals(sv.toLowerCase().trim())) {
                    rcaBuilder.append("[Cached Rule] Field '").append(check.getField())
                            .append("' has confirmed success value: ").append(strVal).append(". ");
                    return Optional.of(new CachedValidationResult(false, rcaBuilder.toString().trim(), "cached-field-rule"));
                }
            }

            // Value is in neither list -- this is an UNKNOWN value.
            // Return empty to trigger an LLM call so we learn what it means.
            logger.info("Cache MISS for {}: field '{}' has unknown value '{}' (known failure={}, known success={})",
                    apiKey, check.getField(), strVal, check.getFailureValues(), check.getSuccessValues());
            return Optional.empty();
        }

        // No field checks matched at all (field absent, etc.) -- cache miss
        return Optional.empty();
    }

    /**
     * Append message field context to an RCA string for richer diagnostics.
     * This is supplementary info only -- it never drives the pass/fail decision.
     */
    private void appendMessageContext(JsonObject json, SoftErrorRule rule, StringBuilder rcaBuilder) {
        for (String msgField : rule.getFailureMessageFields()) {
            if (json.has(msgField) && json.get(msgField).isJsonPrimitive()) {
                String msg = json.get(msgField).getAsString();
                if (!msg.isEmpty()) {
                    rcaBuilder.append("Message in '").append(msgField).append("': ").append(truncate(msg, 120)).append(". ");
                }
            }
        }
    }

    private static String truncate(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen) + "...";
    }

    private String expandRcaTemplate(String template, JsonObject json) {
        String result = template;
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getValue().isJsonPrimitive()) {
                result = result.replace("{" + entry.getKey() + "}", truncate(entry.getValue().getAsString(), 200));
            }
        }
        return result;
    }

    // ------------------------------------------------------------------
    // Persistence
    // ------------------------------------------------------------------

    private void load() {
        Path path = Paths.get(filePath);
        // One-shot migration: if the configured path lives under a .mist/
        // directory and a sibling target/<filename> cache exists, move that
        // legacy file under the new .mist/ location so accumulated learning
        // survives the cache-location switch. Gated on the parent directory
        // being named ".mist" so user-overridden cache paths (e.g.
        // /custom/some.json) do not accidentally swallow a same-named file
        // under target/. Once the legacy file is gone, this branch is a
        // cheap Files.exists() no-op on every subsequent run.
        Path legacy = legacyTargetSiblingPath(path);
        if (legacy != null && !Files.exists(path) && Files.exists(legacy)) {
            try {
                Path parent = path.getParent();
                if (parent != null) Files.createDirectories(parent);
                Files.move(legacy, path, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Migrated cache: {} -> {}", legacy, path);
            } catch (IOException e) {
                logger.warn("Failed to migrate legacy cache {} -> {}: {}", legacy, path, e.getMessage());
            }
        }
        if (!Files.exists(path)) {
            logger.info("No existing soft-error rule cache at {}; starting fresh", filePath);
            return;
        }
        try (Reader reader = new FileReader(filePath)) {
            Type mapType = new TypeToken<Map<String, SoftErrorRule>>() {}.getType();
            Map<String, SoftErrorRule> loaded = GSON.fromJson(reader, mapType);
            if (loaded != null) {
                rules.putAll(loaded);
                logger.info("Loaded {} soft-error rules from {}", rules.size(), filePath);
            }
        } catch (Exception e) {
            logger.warn("Could not load soft-error rule cache from {}: {}", filePath, e.getMessage());
        }
    }

    private synchronized void save() {
        try {
            Path parent = Paths.get(filePath).getParent();
            if (parent != null) Files.createDirectories(parent);
            try (Writer writer = new FileWriter(filePath)) {
                GSON.toJson(rules, writer);
            }
        } catch (IOException e) {
            logger.warn("Could not save soft-error rule cache to {}: {}", filePath, e.getMessage());
        }
    }

    /**
     * Number of APIs that have cached rules.
     */
    public int size() {
        return rules.size();
    }

    /**
     * Compute the legacy {@code target/<filename>} location that a cache file
     * configured at {@code path} would have lived in before the migration to
     * {@code .mist/}. Returns
     * {@code null} if {@code path} does not have the canonical
     * {@code <prefix>/.mist/<filename>} shape — i.e., the user has overridden
     * the cache location to something custom and we should not auto-move
     * anything for them.
     */
    static Path legacyTargetSiblingPath(Path path) {
        Path parent = path.getParent();
        if (parent == null) return null;
        Path parentName = parent.getFileName();
        if (parentName == null || !".mist".equals(parentName.toString())) {
            return null;
        }
        Path grandparent = parent.getParent();
        if (grandparent == null) {
            // Relative path like ".mist/foo.json": the legacy sibling is the
            // relative "target/foo.json" resolved against the working dir.
            return Paths.get("target", path.getFileName().toString());
        }
        return grandparent.resolve("target").resolve(path.getFileName());
    }
}
