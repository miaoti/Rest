package es.us.isa.restest.generators;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.llm.LLMService;
import es.us.isa.restest.llm.LLMConfig;
import es.us.isa.restest.util.PropertyManager;
import org.json.JSONObject;
import org.json.JSONArray;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A "zero-shot" style generator that queries a Large Language Model (LLM)
 * to produce realistic sample values for *any* parameter concept,
 * without enumerating categories like IP, city, country, etc.
 */
public class ZeroShotLLMGenerator {

    // optional: param name -> cached list of values
    private final Map<String, List<String>> cache = new ConcurrentHashMap<>();

    // LLM service for unified model access
    private final LLMService llmService;

    public ZeroShotLLMGenerator() {
        // Initialize LLM service with properties
        Map<String, String> properties = loadLLMProperties();
        this.llmService = LLMService.getInstance(properties);
    }

    /**
     * Load LLM properties from system properties
     */
    private Map<String, String> loadLLMProperties() {
        Map<String, String> properties = new HashMap<>();

        // List of LLM-related properties to load
        String[] llmProperties = {
            "llm.enabled", "llm.model.type",
            "llm.local.enabled", "llm.local.url", "llm.local.model",
            "llm.gemini.enabled", "llm.gemini.api.key", "llm.gemini.model", "llm.gemini.api.url",
            "llm.ollama.enabled", "llm.ollama.url", "llm.ollama.model",
            "llm.rate.limit.retry.enabled", "llm.rate.limit.max.retries",
            // LLM Communication Logging Properties
            "llm.communication.logging.enabled", "llm.communication.logging.dir",
            "llm.communication.logging.file.prefix", "llm.communication.logging.include.response.time",
            "llm.communication.logging.include.content", "llm.communication.logging.include.metadata",
            "llm.communication.logging.level", "llm.communication.logging.max.content.length"
        };

        for (String prop : llmProperties) {
            String value = System.getProperty(prop);
            if (value != null) {
                properties.put(prop, value);
            }
        }

        return properties;
    }

    /**
     * Generate multiple candidate values for a parameter.
     * If the param is "ip_address", the LLM might produce valid IPs.
     * If "city", it might produce city names, etc.
     */
    public List<String> generateParameterValues(ParameterInfo param, int howMany) {
        System.out.println("*** ZeroShotLLMGenerator.generateParameterValues called for: " + param.getName() + " (howMany=" + howMany + ")");

        // 1) check cache using proper cache key (name + type + location)
        String cacheKey = buildCacheKey(param);
        if (cache.containsKey(cacheKey)) {
            System.out.println("*** Found cached value for: " + param.getName() + " (type: " + param.getType() + ")");
            return cache.get(cacheKey);
        }

        // 2) build prompt
        String prompt = buildPrompt(param, howMany);

        // 3) call the LLM
        String rawOutput = callLLM(prompt);
        System.out.println("*** LLM Raw output: " + rawOutput);

        // 4) parse the response based on parameter type
        List<String> values;
        String paramType = safeStr(param.getType()).toLowerCase();
        if ("array".equals(paramType)) {
            values = parseJsonArray(rawOutput);
        } else {
            values = parseLines(rawOutput);
        }

        // 5) filter by regex if present
        if (param.getRegex() != null && !param.getRegex().isEmpty()) {
            values.removeIf(val -> !val.matches(param.getRegex()));
        }

        // fallback if empty
        if (values.isEmpty()) {
            values = Collections.singletonList("");
        }

        // store in cache using proper cache key
        cache.put(cacheKey, values);
        return values;
    }

    /**
     * Generate faulty parameter values for negative testing - DEPRECATED
     * Use generateInvalidInputPool instead for comprehensive fault coverage
     */
    @Deprecated
    public List<String> generateFaultyParameterValues(ParameterInfo param, int howMany) {
        System.out.println("*** DEPRECATED: Use generateInvalidInputPool instead");
        return new ArrayList<>();
    }
    
    // Shared hardcoded generator used by SMART mode and HARDCODE mode to populate
    // the structural/universal fault categories (NULL, EMPTY, OVERFLOW, SPECIAL, TYPE_MISMATCH, BOUNDARY).
    // Created lazily to avoid any load-order cost when LLM mode is selected.
    private volatile HardcodedInvalidInputGenerator hardcodedGen;

    private HardcodedInvalidInputGenerator hardcodedGen() {
        HardcodedInvalidInputGenerator local = hardcodedGen;
        if (local == null) {
            synchronized (this) {
                local = hardcodedGen;
                if (local == null) {
                    local = new HardcodedInvalidInputGenerator();
                    hardcodedGen = local;
                }
            }
        }
        return local;
    }

    /**
     * Generate comprehensive invalid inputs for all fault types.
     *
     * <p>Dispatches on the {@code negative.input.generation.mode} system property:
     * <ul>
     *   <li><b>smart</b> (default) — LLM is called only for REGEX_MISMATCH and SEMANTIC_MISMATCH,
     *       where domain understanding genuinely helps.  The other 6 categories are filled from
     *       the universal/schema-derived payload set via {@link HardcodedInvalidInputGenerator}.
     *       Reduces LLM calls per parameter from ~6 to ~2.</li>
     *   <li><b>llm</b> — every contextual category goes through the LLM (legacy behavior, kept
     *       for research / ablation experiments).</li>
     *   <li><b>hardcode</b> — every category is populated from {@link HardcodedInvalidInputGenerator};
     *       zero LLM calls; fastest and fully deterministic.</li>
     * </ul>
     */
    public es.us.isa.restest.inputs.InvalidInputPool generateInvalidInputPool(ParameterInfo param) {
        String mode = System.getProperty("negative.input.generation.mode", "smart")
                .toLowerCase(java.util.Locale.ROOT)
                .trim();

        System.out.println("*** ZeroShotLLMGenerator.generateInvalidInputPool for: " + param.getName() +
                          " (type: " + param.getType() + ", mode: " + mode + ")");

        switch (mode) {
            case "hardcode":
                // Fully deterministic, zero LLM calls.
                return hardcodedGen().generateInvalidInputPool(param);

            case "llm":
                // Legacy all-LLM path — kept for research / ablation.
                return generateInvalidInputPoolAllLLM(param);

            case "smart":
            default:
                // Recommended default — static/schema for universal categories, LLM only where
                // domain context actually adds value (regex inversion, semantic mismatch).
                return generateInvalidInputPoolSmart(param);
        }
    }

    /**
     * SMART MODE: mix of static payloads and targeted LLM calls.
     *
     * <p>NULL/EMPTY/OVERFLOW/SPECIAL_CHARACTERS/TYPE_MISMATCH/BOUNDARY_VIOLATION are universal
     * or schema-derived — populated from {@link HardcodedInvalidInputGenerator}.
     *
     * <p>REGEX_MISMATCH and SEMANTIC_MISMATCH keep the LLM call because they benefit from
     * domain context the schema cannot express.
     */
    private es.us.isa.restest.inputs.InvalidInputPool generateInvalidInputPoolSmart(ParameterInfo param) {
        es.us.isa.restest.inputs.InvalidInputPool pool =
            new es.us.isa.restest.inputs.InvalidInputPool(param.getName(), safeStr(param.getType()));

        HardcodedInvalidInputGenerator hc = hardcodedGen();

        // Static / schema-derived categories — no LLM call.
        hc.generateTypeMismatchInputs(param, pool);
        hc.generateOverflowInputs(param, pool);
        hc.generateEmptyInputs(param, pool);
        hc.generateNullInputs(param, pool);
        hc.generateSpecialCharacterInputs(param, pool);
        hc.generateBoundaryViolationInputs(param, pool);

        // Context-aware categories — LLM earns its keep here.
        generateRegexMismatchInputs(param, pool);
        generateSemanticMismatchInputs(param, pool);

        System.out.println("*** [SMART] Generated invalid input pool:\n" + pool.getPoolSummary());
        return pool;
    }

    /**
     * LEGACY LLM MODE: the original behavior before the smart/hardcode split — LLM is called
     * for every contextual category.  Retained for ablation studies and head-to-head comparisons.
     */
    private es.us.isa.restest.inputs.InvalidInputPool generateInvalidInputPoolAllLLM(ParameterInfo param) {
        es.us.isa.restest.inputs.InvalidInputPool pool =
            new es.us.isa.restest.inputs.InvalidInputPool(param.getName(), safeStr(param.getType()));

        generateTypeMismatchInputs(param, pool);
        generateRegexMismatchInputs(param, pool);
        generateSemanticMismatchInputs(param, pool);
        generateOverflowInputs(param, pool);
        generateEmptyInputs(param, pool);
        generateNullInputs(param, pool);
        generateSpecialCharacterInputs(param, pool);
        generateBoundaryViolationInputs(param, pool);

        System.out.println("*** [LLM] Generated invalid input pool:\n" + pool.getPoolSummary());
        return pool;
    }
    
    /**
     * Generate type mismatch inputs - wrong data type for the parameter
     * CRITICAL: These are stored as raw objects (Integer, Boolean, etc.) NOT strings
     */
    private void generateTypeMismatchInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 5-8 TYPE MISMATCH invalid values for parameter '" + param.getName() + "'.\n" +
                       "Expected type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values of WRONG TYPE that would cause type validation errors.\n\n" +
                       "FORMAT RULES (strictly follow):\n" +
                       "  TYPE:VALUE  — TYPE is one of: integer|string|boolean|null|array|object\n" +
                       "  VALUE must be a raw literal — NO quotes, NO NaN, NO undefined, NO Infinity.\n\n" +
                       "GOOD examples (one per line):\n" +
                       "  string:hello\n" +
                       "  boolean:true\n" +
                       "  integer:42\n" +
                       "  null:null\n" +
                       "  array:[1,2,3]\n" +
                       "  object:{}\n\n" +
                       "BAD examples (NEVER do this):\n" +
                       "  integer:'123'    <- NO quotes around the number\n" +
                       "  integer:\"abc\"   <- wrong, that is a string value, use string:abc\n" +
                       "  integer:NaN      <- NaN is not a valid literal here\n" +
                       "  integer:2026-03-29  <- a date is a string, use string:2026-03-29\n\n" +
                       "What to generate for each expected type:\n" +
                       "  integer  -> provide string and boolean values  (e.g., string:hello, boolean:false)\n" +
                       "  string   -> provide integer and boolean values (e.g., integer:123, boolean:true)\n" +
                       "  boolean  -> provide string and integer values  (e.g., string:yes, integer:1)\n" +
                       "  array    -> provide string and integer values  (e.g., string:notAList, integer:0)\n\n" +
                       "Return ONLY the values, one per line, in TYPE:VALUE format:";
        
        String response = callLLM(prompt);
        List<String> lines = parseLines(response);
        
        // Parse and add typed values
        for (String line : lines) {
            Object typedValue = parseTypedValue(line, paramType);
            if (typedValue != null) {
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, typedValue);
            }
        }
        
        // Add common type mismatches if LLM didn't provide enough
        if (pool.getCountForType(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH) < 3) {
            addDefaultTypeMismatches(paramType, pool);
        }
    }
    
    /**
     * Parse typed value from LLM response (format: "type:value").
     * Returns an actual typed object (Integer, Boolean, etc.), not a String.
     *
     * Handles common LLM formatting mistakes:
     *  - Single/double quotes around numeric values  ('123', "123")
     *  - NaN / Infinity / undefined
     *  - Date/timestamp strings given for integer fields
     */
    private Object parseTypedValue(String line, String expectedParamType) {
        if (line == null || !line.contains(":")) {
            return null;
        }
        
        String[] parts = line.split(":", 2);
        if (parts.length != 2) {
            return null;
        }
        
        String type = parts[0].trim().toLowerCase();
        // Strip leading numbers/dots (e.g., "3integer" -> "integer", "1. integer" -> "integer")
        type = type.replaceAll("^[0-9.\\s]+", "");
        String value = parts[1].trim();

        // Sanitize: strip surrounding single or double quotes the LLM may add
        if ((value.startsWith("'") && value.endsWith("'")) ||
            (value.startsWith("\"") && value.endsWith("\""))) {
            value = value.substring(1, value.length() - 1).trim();
        }

        // Parse based on specified type
        try {
            switch (type) {
                case "integer":
                case "int":
                case "number": {
                    // Guard against non-numeric tokens the LLM sometimes emits for integers
                    if (value.equalsIgnoreCase("NaN") || value.equalsIgnoreCase("null")
                            || value.equalsIgnoreCase("undefined")
                            || value.equalsIgnoreCase("Infinity")
                            || value.equalsIgnoreCase("-Infinity")) {
                        // Fall through: return as string (it IS a type-mismatch value)
                        return value;
                    }
                    // If it still looks non-numeric (contains letters/dashes in non-digit positions),
                    // return it as a string — it serves perfectly as a type-mismatch invalid value.
                    if (!value.matches("-?\\d+")) {
                        return value;
                    }
                    return Integer.parseInt(value);
                }
                    
                case "long": {
                    if (!value.matches("-?\\d+")) {
                        return value;
                    }
                    return Long.parseLong(value);
                }
                    
                case "double":
                case "float": {
                    if (value.equalsIgnoreCase("NaN") || value.equalsIgnoreCase("Infinity")
                            || value.equalsIgnoreCase("-Infinity")) {
                        return value;
                    }
                    return Double.parseDouble(value);
                }
                    
                case "boolean":
                case "bool":
                    return Boolean.parseBoolean(value);
                    
                case "null":
                    return null;
                    
                case "string":
                    return value;
                    
                case "array":
                    return value; // Represented as string in JSON format
                    
                case "object":
                    return value; // Represented as string in JSON format
                    
                default:
                    return value;
            }
        } catch (Exception e) {
            // Parsing failed; return raw value as a string — still useful as a type-mismatch input
            return value;
        }
    }
    
    /**
     * Add default type mismatches based on parameter type
     */
    private void addDefaultTypeMismatches(String paramType, es.us.isa.restest.inputs.InvalidInputPool pool) {
        switch (paramType) {
            case "string":
                // String expects text, provide numbers/booleans/null
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, 12345);
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, null);
                break;
                
            case "integer":
            case "int":
            case "number":
                // Number expects integer, provide strings/booleans
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, "not_a_number");
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, "12.34abc");
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, false);
                break;
                
            case "boolean":
            case "bool":
                // Boolean expects true/false, provide strings/numbers
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, "yes");
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, 1);
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, "true");
                break;
                
            case "array":
                // Array expects list, provide primitives
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, "not_an_array");
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, 123);
                break;
                
            case "object":
                // Object expects key-value, provide primitives
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, "not_an_object");
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, 456);
                break;
                
            default:
                // Generic type mismatches
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, null);
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.TYPE_MISMATCH, 999);
                break;
        }
    }
    
    /**
     * Generate regex pattern mismatch inputs
     */
    private void generateRegexMismatchInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        if (param.getRegex() == null || param.getRegex().isEmpty()) {
            // No regex constraint, skip this type
            return;
        }
        
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 5-8 values that DO NOT MATCH this regex pattern for parameter '" + param.getName() + "':\n" +
                       "Pattern: " + param.getRegex() + "\n" +
                       "Expected type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values that have correct type but VIOLATE the regex pattern.\n" +
                       "Return only the invalid values, one per line:";
        
        String response = callLLM(prompt);
        List<String> values = parseLines(response);
        
        for (String value : values) {
            if (!value.matches(param.getRegex())) {
                pool.addValue(es.us.isa.restest.inputs.InvalidInputType.REGEX_MISMATCH, value);
            }
        }
    }
    
    /**
     * Generate semantically invalid inputs
     */
    private void generateSemanticMismatchInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 5-8 SEMANTICALLY INVALID values for parameter '" + param.getName() + "'.\n" +
                       "Type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values that have correct type and format but are MEANINGLESS or IMPOSSIBLE.\n" +
                       "IMPORTANT: Include BOTH long meaningless values AND very SHORT invalid values!\n\n" +
                       "Categories to cover:\n" +
                       "1. MEANINGLESS/IMPOSSIBLE values (non-existent entities)\n" +
                       "2. VERY SHORT values (1-3 chars) that are too short to be valid\n" +
                       "3. Single characters or digits that can't be valid\n" +
                       "If this is a comma-separated list, generate lists with non-existent or invalid items or make some values as null or empty.\n\n" +
                       "Examples:\n" +
                       "- Station name: X, AB, 1, NonExistentStation, FakeCity123\n" +
                       "- Route ID: a, 0, -, FAKE-ROUTE-999, invalid_id\n" +
                       "- Age parameter: -5, 999, -100, 0\n" +
                       "- Email parameter: a, @, invalid@, nodomain, test@@test\n" +
                       "- Date parameter: 1, x, 2025-02-30, 2025-13-01\n" +
                       "- Country code: Z, XX, ZZZ, 9, 999\n" +
                       "- Train type: x, 1, !, InvalidType, FakeTrain\n" +
                       "- Price rate: a, x, -, NaN, infinity\n" +
                       "- Station list: X, NonExistent1,NonExistent2, ,InvalidStation\n\n" +
                       "OUTPUT FORMAT: Return ONLY the raw values, one per line. Do NOT include parameter names or quotes.\n" +
                       "WRONG: stationList=\"value\"\n" +
                       "RIGHT: value\n\n" +
                       "Return only the semantically invalid values, one per line:";
        
        String response = callLLM(prompt);
        List<String> values = parseLines(response);
        
        for (String value : values) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, value);
        }
        
        // Also add some hardcoded very short semantic mismatches that LLM might miss
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, "x");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, "1");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, "a");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, "-");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, "?");
    }
    
    /**
     * Generate overflow inputs
     */
    private void generateOverflowInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 5-8 OVERFLOW values for parameter '" + param.getName() + "'.\n" +
                       "Type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values that EXCEED expected limits:\n";
        
        if ("string".equals(paramType)) {
            prompt += "- Very long strings (1000+ characters)\n" +
                     "- Strings with repeated characters (AAAA...)\n" +
                     "- Maximum length violations\n";
        } else if (paramType.contains("int") || paramType.contains("number")) {
            prompt += "- Very large numbers (9999999999)\n" +
                     "- Numbers beyond typical ranges\n" +
                     "- Scientific notation extremes\n";
        } else {
            prompt += "- Values exceeding typical constraints\n" +
                     "- Maximum size violations\n";
        }
        
        prompt += "\nReturn only the overflow values, one per line:";
        
        String response = callLLM(prompt);
        List<String> values = parseLines(response);
        
        for (String value : values) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.OVERFLOW, value);
        }
        
        // Add guaranteed overflow values
        if ("string".equals(paramType)) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.OVERFLOW, "A".repeat(10000)); // Very long string
        } else if (paramType.contains("int")) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.OVERFLOW, Integer.MAX_VALUE);
        }
    }
    
    /**
     * Generate empty inputs for ALL parameters (both required and optional)
     * Even optional parameters should be tested with empty values to catch edge cases
     */
    private void generateEmptyInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        boolean isRequired = param.getRequired() != null && param.getRequired();
        String requiredStatus = isRequired ? "REQUIRED" : "OPTIONAL";
        
        System.out.println("✅ Generating EMPTY_INPUT for " + requiredStatus + " parameter: " + param.getName());
        String paramType = safeStr(param.getType()).toLowerCase();
        
        // Empty string
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, "");
        
        // Whitespace only
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, " ");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, "   ");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, "\t");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, "\n");
        
        // Type-specific empty values
        if ("array".equals(paramType)) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, "[]");
        } else if ("object".equals(paramType)) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.EMPTY_INPUT, "{}");
        }
    }
    
    /**
     * Generate null inputs for ALL parameters (both required and optional)
     * Even optional parameters should be tested with null values to catch edge cases
     */
    private void generateNullInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        boolean isRequired = param.getRequired() != null && param.getRequired();
        String requiredStatus = isRequired ? "REQUIRED" : "OPTIONAL";
        
        System.out.println("✅ Generating NULL_INPUT for " + requiredStatus + " parameter: " + param.getName());
        
        // Actual null
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, null);
        
        // String representations of null (sometimes APIs parse these)
        // NOTE: Only use lowercase "null" to avoid class name conflicts on case-insensitive filesystems
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "null");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "Null");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "undefined");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "nil");
    }
    
    /**
     * Generate special character inputs (potential injection attempts)
     */
    private void generateSpecialCharacterInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 5-8 values with SPECIAL CHARACTERS or INJECTION attempts for parameter '" + param.getName() + "'.\n" +
                       "Type: " + param.getType() + "\n\n" +
                       "Generate values with malicious or special characters\n" +
                       "Down here are some examples of special characters and injection attempts. Generate values that can fit into the parameter type and context as provided.\n" +
                       "- SQL injection attempts: ' OR '1'='1, '; DROP TABLE--\n" +
                       "- XSS attempts: <script>alert('XSS')</script>\n" +
                       "- Path traversal: ../../etc/passwd\n" +
                       "- Command injection: ; ls -la\n" +
                       "- Special characters: !@#$%^&*(){}[]|\\:;\"'<>?,./\n\n" +
                       "Return only the special character values, one per line:";
        
        String response = callLLM(prompt);
        List<String> values = parseLines(response);
        
        for (String value : values) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SPECIAL_CHARACTERS, value);
        }
        
        // Add guaranteed special character values
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SPECIAL_CHARACTERS, "' OR '1'='1");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SPECIAL_CHARACTERS, "<script>alert('test')</script>");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SPECIAL_CHARACTERS, "../../../etc/passwd");
    }
    
    /**
     * Generate boundary violation inputs
     */
    private void generateBoundaryViolationInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 5-8 BOUNDARY VIOLATION values for parameter '" + param.getName() + "'.\n" +
                       "Type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values that are JUST OUTSIDE valid boundaries:\n" +
                       "- If minLength is 5, provide length 4\n" +
                       "- If maxValue is 100, provide 101\n" +
                       "- If range is 1-10, provide 0 or 11\n" +
                       "- Off-by-one errors\n\n" +
                       "Return only the boundary violation values, one per line:";
        
        String response = callLLM(prompt);
        List<String> values = parseLines(response);
        
        for (String value : values) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.BOUNDARY_VIOLATION, value);
        }
        
        // Add common boundary violations
        String paramType = safeStr(param.getType()).toLowerCase();
        if (paramType.contains("int") || paramType.contains("number")) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.BOUNDARY_VIOLATION, -1);
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.BOUNDARY_VIOLATION, 0);
        }
    }

    /**
     * Build cache key that captures the full constraint signature of a parameter.
     * Parameters sharing a name but differing in type, location, enum set, or
     * numeric bounds are intentionally separated so each gets its own LLM batch.
     */
    private String buildCacheKey(ParameterInfo param) {
        String name     = param.getName()        != null ? param.getName()        : "unknown";
        String type     = param.getType()        != null ? param.getType()        : "unknown";
        String location = param.getInLocation()  != null ? param.getInLocation()  : "unknown";
        String format   = param.getFormat()      != null ? param.getFormat()      : "";
        String enums    = param.hasEnum()
                ? String.join(",", param.getEnumValues())
                : "";
        String bounds   = (param.getMinimum() != null ? param.getMinimum() : "")
                + ".." + (param.getMaximum() != null ? param.getMaximum() : "");
        return name + ":" + type + ":" + location + ":" + format + ":" + enums + ":" + bounds;
    }

    /**
     * Build a structured, richly contextualised prompt for positive-value generation.
     *
     * <p>The prompt is split into four clearly delimited sections:
     * <ol>
     *   <li><b>[API Context]</b> — endpoint, service, and sibling parameter list</li>
     *   <li><b>[Parameter Details]</b> — name, location, type, format, description, example</li>
     *   <li><b>[Constraints]</b> — enum list, numeric bounds, length limits, regex pattern</li>
     *   <li><b>[Instructions]</b> — strict output format rules and domain guidance</li>
     * </ol>
     */
    private String buildPrompt(ParameterInfo param, int howMany) {
        StringBuilder p = new StringBuilder();
        String paramType = safeStr(param.getType()).toLowerCase();
        boolean isArray  = "array".equals(paramType);

        // ── Persona ──────────────────────────────────────────────────────────────
        p.append("You are an expert API tester. Generate ").append(howMany)
         .append(" distinct, highly realistic, and strictly valid values for the following API parameter.\n");
        p.append("Current Date/Time: ").append(getCurrentTimestamp()).append("\n\n");

        // ── [API Context] ────────────────────────────────────────────────────────
        String apiName     = safeStr(param.getApiName());
        String serviceName = safeStr(param.getServiceName());
        boolean hasApiCtx  = !apiName.isEmpty() || !serviceName.isEmpty();
        if (hasApiCtx) {
            p.append("[API Context]\n");
            if (!apiName.isEmpty())     p.append("Endpoint: ").append(apiName).append("\n");
            if (!serviceName.isEmpty()) p.append("Service:  ").append(serviceName).append("\n");
            if (param.getAllParameterNames() != null && !param.getAllParameterNames().isEmpty()) {
                p.append("Sibling Parameters: ")
                 .append(String.join(", ", param.getAllParameterNames())).append("\n");
            }
            p.append("\n");
        }

        // ── [Parameter Details] ──────────────────────────────────────────────────
        p.append("[Parameter Details]\n");
        p.append("Parameter Name: ").append(safeStr(param.getName())).append("\n");
        p.append("Location:       ").append(safeStr(param.getInLocation())).append("\n");

        // Type + format on one line: "Type: integer (int64)"
        String typeStr = safeStr(param.getType());
        String fmtStr  = safeStr(param.getFormat());
        p.append("Type:           ").append(typeStr);
        if (!fmtStr.isEmpty()) p.append(" (").append(fmtStr).append(")");
        p.append("\n");

        String desc = safeStr(param.getDescription());
        if (!desc.isEmpty()) p.append("Description:    ").append(desc).append("\n");

        String example = safeStr(param.getSchemaExample());
        if (!example.isEmpty()) p.append("Example:        ").append(example).append("\n");

        if (param.getRequired() != null) {
            p.append("Required:       ").append(param.getRequired() ? "Yes" : "No").append("\n");
        }
        p.append("\n");

        // ── [Constraints] ────────────────────────────────────────────────────────
        boolean hasConstraints = param.hasEnum() || param.hasBounds()
                || param.hasLengthConstraints() || !safeStr(param.getRegex()).isEmpty();
        if (hasConstraints) {
            p.append("[Constraints]\n");

            if (param.hasEnum()) {
                p.append("Allowed Values (Enum): [")
                 .append(String.join(", ", param.getEnumValues())).append("]\n");
                p.append("  → You MUST only use values from the Allowed Values list above.\n");
            }

            if (param.getMinimum() != null || param.getMaximum() != null) {
                p.append("Numeric Range: ");
                if (param.getMinimum() != null) p.append("min=").append(param.getMinimum()).append(" ");
                if (param.getMaximum() != null) p.append("max=").append(param.getMaximum());
                p.append("\n  → Every generated number MUST fall within this range.\n");
            }

            if (param.getMinLength() != null || param.getMaxLength() != null) {
                p.append("String Length: ");
                if (param.getMinLength() != null) p.append("minLength=").append(param.getMinLength()).append(" ");
                if (param.getMaxLength() != null) p.append("maxLength=").append(param.getMaxLength());
                p.append("\n  → Every generated string MUST satisfy this length constraint.\n");
            }

            String regex = safeStr(param.getRegex());
            if (!regex.isEmpty()) {
                p.append("Pattern (regex): ").append(regex).append("\n");
                p.append("  → Every generated value MUST match this pattern.\n");
            }
            p.append("\n");
        }

        // ── [Instructions] ───────────────────────────────────────────────────────
        p.append("[Instructions]\n");
        p.append("1. You MUST strictly adhere to the Type, Format, and all Constraints listed above.\n");
        if (param.hasEnum()) {
            p.append("2. Because an enum is defined, select ONLY values from the Allowed Values list.\n");
        } else {
            p.append("2. The values must be semantically realistic for the Endpoint context and domain.\n");
        }
        p.append("3. Return EXACTLY ").append(howMany).append(" values.\n");

        // Type-specific generation guidance
        p.append("4. Domain guidance:\n");
        switch (typeStr.toLowerCase()) {
            case "integer":
            case "number":
                p.append("   • Generate realistic numeric values appropriate to the business context.\n");
                if (param.hasBounds()) {
                    p.append("   • Stay strictly within the Numeric Range defined in Constraints.\n");
                }
                break;
            case "boolean":
                p.append("   • Output only 'true' or 'false' (lowercase, no quotes).\n");
                break;
            default:
                if (!fmtStr.isEmpty()) {
                    switch (fmtStr.toLowerCase()) {
                        case "uuid":
                            p.append("   • Each value must be a valid UUID v4 (e.g., 550e8400-e29b-41d4-a716-446655440000).\n");
                            break;
                        case "date":
                            p.append("   • Use ISO-8601 date format: YYYY-MM-DD.\n");
                            break;
                        case "date-time":
                            p.append("   • Use ISO-8601 date-time format: YYYY-MM-DDTHH:MM:SSZ.\n");
                            break;
                        case "email":
                            p.append("   • Each value must be a valid email address.\n");
                            break;
                        default:
                            p.append("   • Respect the '").append(fmtStr).append("' format specification.\n");
                    }
                }
                // Temporal heuristic (only when no explicit format is given)
                if (fmtStr.isEmpty()) {
                    String nameLc = safeStr(param.getName()).toLowerCase();
                    boolean isFutureTemporal = nameLc.contains("departure") || nameLc.contains("arrival")
                            || nameLc.contains("booking") || nameLc.contains("scheduled")
                            || nameLc.contains("planned") || nameLc.contains("end")
                            || nameLc.contains("traveldate") || nameLc.contains("boughtdate");
                    if (isFutureTemporal) {
                        p.append("   • This is a future-oriented temporal parameter — generate dates 1-30 days AFTER the Current Date/Time.\n");
                    }
                }
                break;
        }

        // ── Output format block ───────────────────────────────────────────────────
        p.append("5. Output format:\n");
        if (isArray) {
            p.append("   • Return a single valid JSON array containing exactly ").append(howMany).append(" elements.\n");
            p.append("   • Format: [\"value1\", \"value2\", ...]\n");
            p.append("   • Do NOT use markdown code fences, bullet points, numbering, or explanations.\n\n");
            p.append("Generate the JSON array now:");
        } else {
            p.append("   • Output ONLY the values, one per line.\n");
            p.append("   • Do NOT use markdown code blocks, bullet points, numbering, or any explanations.\n");
            p.append("   • Do NOT prefix values with hyphens, dashes, or numbers.\n\n");
            p.append("Generate your ").append(howMany).append(" values now, one per line:");
        }

        return p.toString();
    }


    // Legacy method removed - now using unified LLM service via callLLM()


    private String callLLM(String prompt) {
        String systemContent =
                "You are an expert API tester specialising in test data generation. " +
                "Your sole task is to produce realistic, constraint-compliant values for API parameters. " +
                "STRICT RULES: " +
                "(1) When asked for N values, return EXACTLY N items — no more, no fewer. " +
                "(2) For line-separated output: one value per line, nothing else on that line. " +
                "(3) For JSON array output: a single valid JSON array, nothing else. " +
                "(4) Never add markdown fences (```), bullet points, numbering, explanations, " +
                "    or commentary of any kind. " +
                "(5) Always respect the Type, Format, Enum, and numeric/length Constraints stated in the prompt. " +
                "(6) If an enum list is provided, output ONLY values from that list.";

        System.out.println("[LLM] Calling LLM service with model type: " + llmService.getConfig().getModelType());
        System.out.println("[LLM] User prompt: " + prompt);

        try {
            String result = llmService.generateText(systemContent, prompt, 200, 0.7);

            if (result != null && !result.trim().isEmpty()) {
                System.out.println("[LLM] Successfully generated content: " + result);
                return result;
            } else {
                System.out.println("[LLM] LLM service returned null or empty result, using fallback");
                return generateFallbackValue();
            }

        } catch (Exception e) {
            System.out.println("[LLM] Error calling LLM service: " + e.getMessage());
            e.printStackTrace();
            return generateFallbackValue();
        }
    }

    private String generateFallbackValue() {
        // Generate simple fallback values when LLM is unavailable
        String fallback = "test" + System.currentTimeMillis() % 1000;
        System.out.println("*** FALLBACK VALUE GENERATED: " + fallback);
        return fallback;
    }

    private String extractFromGeminiResponse(String responseJson) {
        try {
            JSONObject json = new JSONObject(responseJson);
            JSONArray candidates = json.getJSONArray("candidates");
            if (!candidates.isEmpty()) {
                JSONObject first = candidates.getJSONObject(0);
                JSONObject content = first.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (!parts.isEmpty()) {
                    //System.out.println("Here is the output:" + parts.getJSONObject(0).optString("text", ""));
                    return parts.getJSONObject(0).optString("text", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    // Legacy method removed - response parsing now handled by unified LLM service

    /**
     * Get current date/time in human-readable format for LLM prompts.
     */
    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm:ss a z");
        return ZonedDateTime.now().format(formatter);
    }

    /**
     * Escape quotes or backslashes so we can embed user text in JSON.
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }


    private List<String> parseJsonArray(String raw) {
        if (raw == null || raw.isEmpty()) {
            return Collections.emptyList();
        }
        
        try {
            // Try to parse as JSON array directly
            String trimmed = raw.trim();
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                JSONArray jsonArray = new JSONArray(trimmed);
                List<String> values = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    values.add(jsonArray.getString(i));
                }
                return values;
            }
        } catch (Exception e) {
            System.out.println("Failed to parse as JSON array, falling back to line parsing: " + e.getMessage());
        }
        
        // Fallback to line parsing if JSON parsing fails
        return parseLines(raw);
    }
    
    private List<String> parseLines(String raw) {
        if (raw == null || raw.isEmpty()) {
            return Collections.emptyList();
        }
        String[] arr = raw.split("\\r?\\n");
        List<String> lines = new ArrayList<>();
        for (String line : arr) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                // 🔥 FIX: Filter out explanatory/header text that LLM sometimes adds
                // Skip lines that look like explanations or headers
                if (isExplanatoryText(trimmed)) {
                    System.out.println("⚠️ Skipping explanatory line: " + trimmed.substring(0, Math.min(50, trimmed.length())) + "...");
                    continue;
                }
                
                // Strip surrounding quotes if present
                String cleaned = stripQuotes(trimmed);
                
                // Skip empty after cleaning
                if (!cleaned.isEmpty()) {
                    lines.add(cleaned);
                }
            }
        }
        return lines;
    }
    
    /**
     * Detect if a line is explanatory text rather than an actual value.
     * LLM often adds introductory sentences before the values.
     */
    private boolean isExplanatoryText(String line) {
        String lower = line.toLowerCase();
        
        // Lines ending with colon are usually headers
        if (line.endsWith(":")) {
            return true;
        }
        
        // Common introductory phrases
        String[] explanatoryPatterns = {
            "here are", "here is", "following are", "following is",
            "invalid", "example", "these are", "below are",
            "the values", "values that", "values for",
            "syntactically", "semantically", "meaningless",
            "i'll generate", "i will generate", "let me",
            "note:", "note that", "please note",
            "generate", "providing", "output:"
        };
        
        for (String pattern : explanatoryPatterns) {
            if (lower.startsWith(pattern) || lower.contains(pattern + " ")) {
                return true;
            }
        }
        
        // Lines that are too long to be a simple value (likely explanations)
        // Unless they look like actual overflow test values (repeated chars)
        if (line.length() > 200 && !isLikelyOverflowValue(line)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if a long string is likely an intentional overflow test value
     * (repeated characters, long random strings, etc.)
     */
    private boolean isLikelyOverflowValue(String line) {
        // Check for repeated character patterns (like "AAAAA..." or "XXXXX...")
        if (line.length() > 100) {
            char first = line.charAt(0);
            int sameCharCount = 0;
            for (int i = 0; i < Math.min(50, line.length()); i++) {
                if (line.charAt(i) == first) {
                    sameCharCount++;
                }
            }
            // If more than 80% of first 50 chars are the same, it's likely overflow
            if (sameCharCount > 40) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Strip surrounding quotes and parameter name prefixes from a value.
     * Handles formats like:
     * - "value" → value
     * - 'value' → value
     * - paramName="value" → value
     * - paramName='value' → value
     * 
     * IMPORTANT: Does NOT strip quotes from SQL injection values like ' OR '1'='1
     */
    private String stripQuotes(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        String cleaned = value.trim();
        
        // Handle paramName="value" or paramName='value' format ONLY if:
        // 1. The part before '=' looks like a valid parameter name (letters, digits, underscores)
        // 2. The value after '=' is quoted
        // This prevents breaking SQL injection values like: ' OR '1'='1
        if (cleaned.contains("=")) {
            int eqIndex = cleaned.indexOf("=");
            if (eqIndex > 0 && eqIndex < cleaned.length() - 1) {
                String beforeEq = cleaned.substring(0, eqIndex).trim();
                String afterEq = cleaned.substring(eqIndex + 1).trim();
                
                // Only strip if beforeEq is a valid identifier (alphanumeric + underscore, starts with letter)
                // AND afterEq is quoted
                boolean isValidParamName = beforeEq.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
                boolean afterIsQuoted = (afterEq.startsWith("\"") && afterEq.endsWith("\"")) ||
                                        (afterEq.startsWith("'") && afterEq.endsWith("'"));
                
                if (isValidParamName && afterIsQuoted) {
                    // Strip the param name and the outer quotes
                    cleaned = afterEq.substring(1, afterEq.length() - 1);
                    return cleaned;
                }
            }
        }
        
        // Strip surrounding double quotes (only if BOTH start and end with quotes)
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            // Check this isn't a value that legitimately contains quotes (like JSON)
            String inner = cleaned.substring(1, cleaned.length() - 1);
            // Only strip if inner doesn't contain unescaped quotes that would indicate it's not simple quoting
            if (!inner.contains("\"") || inner.contains("\\\"")) {
                return inner;
            }
        }
        
        // Strip surrounding single quotes (only if BOTH start and end with quotes)
        // BUT NOT for SQL injection values that have internal structure
        if (cleaned.length() >= 2 && cleaned.startsWith("'") && cleaned.endsWith("'")) {
            String inner = cleaned.substring(1, cleaned.length() - 1);
            // Don't strip if this looks like SQL injection (contains ' OR, ' AND, '=, etc.)
            if (!inner.contains("'") && !inner.toUpperCase().contains(" OR ") && 
                !inner.toUpperCase().contains(" AND ") && !inner.contains("=")) {
                return inner;
            }
        }
        
        return cleaned;
    }

    private String safeStr(String s) {
        return (s == null ? "" : s);
    }

    /**
     * Truncate response body for LLM validation prompts.
     * Large responses (e.g., GET /adminorder with thousands of records) can overwhelm the LLM.
     * This method:
     * 1. Limits total length to MAX_RESPONSE_BODY_LENGTH chars
     * 2. For JSON arrays, shows first few + last few elements with [...] indicator
     * 
     * @param responseBody The original response body
     * @return Truncated version suitable for LLM prompt
     */
    private static final int MAX_RESPONSE_BODY_LENGTH = 10000; // Max chars for response body in LLM prompts
    
    private String truncateResponseForLLM(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return "(empty response)";
        }
        
        // If already within limit, return as-is
        if (responseBody.length() <= MAX_RESPONSE_BODY_LENGTH) {
            return responseBody;
        }
        
        // Try to intelligently truncate JSON arrays
        String trimmed = responseBody.trim();
        if (trimmed.contains("\"data\":[") && trimmed.contains("]")) {
            // This looks like a JSON response with a data array
            try {
                // Find the data array boundaries
                int dataStart = trimmed.indexOf("\"data\":[");
                if (dataStart >= 0) {
                    int arrayStart = dataStart + 7; // Position after "data":[
                    
                    // Count array elements to provide context
                    int bracketCount = 0;
                    int elementCount = 0;
                    for (int i = arrayStart; i < trimmed.length(); i++) {
                        char c = trimmed.charAt(i);
                        if (c == '{') {
                            if (bracketCount == 0) elementCount++;
                            bracketCount++;
                        } else if (c == '}') bracketCount--;
                        else if (c == ']' && bracketCount == 0) break;
                    }
                    
                    // Build truncated version showing metadata + count
                    StringBuilder truncated = new StringBuilder();
                    truncated.append("{\n  \"_truncation_note\": \"Response truncated for LLM analysis. Original had ~");
                    truncated.append(elementCount).append(" elements in data array.\",\n");
                    
                    // Include first 3000 chars to show structure
                    int previewLength = Math.min(3000, trimmed.length());
                    truncated.append("  \"_preview_start\": ");
                    truncated.append(trimmed.substring(0, previewLength));
                    
                    // Add ellipsis and ending
                    truncated.append("\n  ... [TRUNCATED ").append(responseBody.length() - previewLength).append(" chars] ...\n");
                    
                    // Include last 500 chars to show closing structure
                    if (trimmed.length() > 500) {
                        truncated.append("  \"_preview_end\": ...");
                        truncated.append(trimmed.substring(trimmed.length() - 500));
                    }
                    truncated.append("\n}");
                    
                    return truncated.toString();
                }
            } catch (Exception e) {
                // Fall through to simple truncation
            }
        }
        
        // Simple truncation: first 8000 + last 1500 chars
        StringBuilder truncated = new StringBuilder();
        truncated.append(responseBody.substring(0, 8000));
        truncated.append("\n\n... [TRUNCATED ").append(responseBody.length() - 9500).append(" chars] ...\n\n");
        truncated.append(responseBody.substring(responseBody.length() - 1500));
        
        return truncated.toString();
    }

    /**
     * Validate a 2XX response to detect "soft errors" - cases where the API returns 200 OK
     * but includes error information in the response body.
     * 
     * @param statusCode HTTP status code
     * @param responseBody Response body as string
     * @param serviceName Name of the service
     * @param method HTTP method (GET, POST, etc.)
     * @param path API path
     * @return ValidationResult containing isFailed flag and RCA explanation
     */
    public ValidationResult validateResponse(int statusCode, String responseBody, String serviceName, String method, String path) {
        // Build system prompt (instructions and criteria)
        StringBuilder systemPrompt = new StringBuilder();
        systemPrompt.append("You are an API testing expert analyzing response data.\n\n");
        systemPrompt.append("ANALYSIS CRITERIA:\n");
        systemPrompt.append("A response is considered FAILED if it contains ANY of:\n");
        systemPrompt.append("1. Explicit failure indicators:\n");
        systemPrompt.append("   - status: 0 or status: false or status: \"error\" or status: \"failed\"\n");
        systemPrompt.append("   - success: false\n");
        systemPrompt.append("   - error: true or hasError: true\n");
        systemPrompt.append("   - Any field explicitly indicating failure\n\n");
        systemPrompt.append("2. Error messages:\n");
        systemPrompt.append("   - Fields named: error, errorMessage, msg, message, errorMsg containing non-empty error text\n");
        systemPrompt.append("   - Exception information or stack traces\n\n");
        systemPrompt.append("3. Data validation:\n");
        systemPrompt.append("   - data field is null or empty when data is expected\n");
        systemPrompt.append("   - Empty result arrays when results are expected\n\n");
        systemPrompt.append("4. Business logic errors:\n");
        systemPrompt.append("   - Validation error messages (e.g., \"invalid parameters\", \"not found\", \"unauthorized\")\n");
        systemPrompt.append("   - Constraint violation messages\n\n");
        systemPrompt.append("IMPORTANT:\n");
        systemPrompt.append("- If the response looks successful with valid data, return FAILED=false\n");
        systemPrompt.append("- Only return FAILED=true if there are clear error indicators\n");
        systemPrompt.append("- Be specific about WHY it failed in your root cause analysis\n\n");
        systemPrompt.append("OUTPUT FORMAT (exactly 2 lines):\n");
        systemPrompt.append("FAILED: true|false\n");
        systemPrompt.append("RCA: <detailed root cause analysis explaining why this is a failure or success>\n");
        
        // Build user prompt (actual task with API details and response)
        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append("TASK: Determine if this API call actually FAILED despite returning a success status code.\n\n");
        userPrompt.append("API Details:\n");
        userPrompt.append("- Service: ").append(serviceName).append("\n");
        userPrompt.append("- Endpoint: ").append(method).append(" ").append(path).append("\n");
        userPrompt.append("- HTTP Status Code: ").append(statusCode).append("\n\n");
        userPrompt.append("Response Body:\n");
        userPrompt.append("```json\n");
        
        // 🔥 SMART FIX: JSON-aware truncation that preserves ALL root-level fields
        // Simple truncation could miss error indicators at the end (e.g., {"status":1, "data":[...], "failed":true})
        // This approach: parse JSON, keep all root fields, only truncate/summarize data arrays
        final int MAX_RESPONSE_BODY_SIZE = 16 * 1024; // 16KB threshold
        if (responseBody != null && responseBody.length() > MAX_RESPONSE_BODY_SIZE) {
            String processedBody = smartTruncateJsonResponse(responseBody, MAX_RESPONSE_BODY_SIZE);
            userPrompt.append(processedBody);
        } else {
            userPrompt.append(responseBody).append("\n");
        }
        
        userPrompt.append("```\n\n");
        userPrompt.append("Examples:\n");
        userPrompt.append("Example 1 (Soft Error):\n");
        userPrompt.append("Response: {\"status\":0,\"msg\":\"start station not in list\",\"data\":null}\n");
        userPrompt.append("FAILED: true\n");
        userPrompt.append("RCA: API returned status=0 indicating failure. Error message states 'start station not in list', and data field is null. This is a business logic validation failure.\n\n");
        userPrompt.append("Example 2 (Success):\n");
        userPrompt.append("Response: {\"status\":1,\"msg\":\"Success\",\"data\":{\"id\":123,\"name\":\"Route A\"}}\n");
        userPrompt.append("FAILED: false\n");
        userPrompt.append("RCA: API returned status=1 indicating success. Response contains valid data with id and name fields. No error indicators present.\n\n");
        userPrompt.append("Now analyze the response above and provide your answer:\n");

        try {
            // Call LLM service with higher token limit for detailed analysis
            String llmResponse = llmService.generateText(systemPrompt.toString(), userPrompt.toString(), 500, 0.3);
            
            // Parse response
            boolean isFailed = false;
            String rca = "";
            
            String[] lines = llmResponse.split("\\r?\\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.startsWith("FAILED:")) {
                    String failedValue = trimmed.substring("FAILED:".length()).trim().toLowerCase();
                    isFailed = failedValue.equals("true");
                } else if (trimmed.startsWith("RCA:")) {
                    rca = trimmed.substring("RCA:".length()).trim();
                }
            }
            
            return new ValidationResult(isFailed, rca, llmResponse);
            
        } catch (Exception e) {
            System.err.println("⚠️ Failed to validate response with LLM: " + e.getMessage());
            // Return non-failed by default to avoid false positives
            return new ValidationResult(false, "LLM validation failed: " + e.getMessage(), "");
        }
    }

    /**
     * Validate a response for NEGATIVE tests - check if the error is related to the designed invalid inputs.
     * For a negative test to PASS, the error message must be about the specific invalid parameter we intentionally set.
     * 
     * @param statusCode HTTP status code
     * @param responseBody Response body as string
     * @param serviceName Name of the service
     * @param method HTTP method (GET, POST, etc.)
     * @param path API path
     * @param invalidParameters Map of parameter name to invalid value that was intentionally set
     * @return ValidationResult containing isFailed flag (true = error related to invalid input) and RCA explanation
     */
    public ValidationResult validateNegativeTestResponse(int statusCode, String responseBody, 
            String serviceName, String method, String path, java.util.Map<String, String> invalidParameters) {
        
        // Build system prompt for negative test validation
        StringBuilder systemPrompt = new StringBuilder();
        systemPrompt.append("You are an API testing expert validating NEGATIVE TEST results.\n\n");
        systemPrompt.append("CONTEXT: This is a NEGATIVE test where we INTENTIONALLY sent INVALID inputs to test error handling.\n");
        systemPrompt.append("The test PASSES (returns FAILED=true) if the API correctly rejected our invalid input.\n");
        systemPrompt.append("The test FAILS (returns FAILED=false) if the API accepted the invalid input or the error is unrelated.\n\n");
        
        systemPrompt.append("DESIGNED INVALID INPUTS:\n");
        if (invalidParameters == null || invalidParameters.isEmpty()) {
            systemPrompt.append("  (No specific invalid parameters provided)\n");
        } else {
            for (java.util.Map.Entry<String, String> entry : invalidParameters.entrySet()) {
                systemPrompt.append("  - Parameter '").append(entry.getKey()).append("' was set to INVALID value: ");
                String value = entry.getValue();
                if (value != null && value.length() > 100) {
                    value = value.substring(0, 100) + "...";
                }
                systemPrompt.append(value).append("\n");
            }
        }
        systemPrompt.append("\n");
        
        systemPrompt.append("VALIDATION CRITERIA:\n");
        systemPrompt.append("Return FAILED=true (test PASSES) if:\n");
        systemPrompt.append("1. The response contains an error message that SPECIFICALLY mentions or relates to one of the designed invalid parameters\n");
        systemPrompt.append("2. The error message indicates validation failure for the invalid input we sent\n");
        systemPrompt.append("3. The response shows the API correctly rejected our invalid data\n\n");
        
        systemPrompt.append("Return FAILED=false (test FAILS) if:\n");
        systemPrompt.append("1. The response shows SUCCESS (API accepted our invalid input - this is BAD!)\n");
        systemPrompt.append("2. The error is about something UNRELATED to our invalid parameters (e.g., authentication, server error)\n");
        systemPrompt.append("3. The error message doesn't mention or relate to the parameters we made invalid\n\n");
        
        systemPrompt.append("OUTPUT FORMAT (exactly 3 lines):\n");
        systemPrompt.append("FAILED: true|false\n");
        systemPrompt.append("RELATED_TO_INVALID_INPUT: true|false\n");
        systemPrompt.append("RCA: <explanation of whether the error is about our designed invalid input>\n");
        
        // Build user prompt
        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append("TASK: Determine if this API response correctly rejected our DESIGNED INVALID INPUT.\n\n");
        userPrompt.append("API Details:\n");
        userPrompt.append("- Service: ").append(serviceName).append("\n");
        userPrompt.append("- Endpoint: ").append(method).append(" ").append(path).append("\n");
        userPrompt.append("- HTTP Status Code: ").append(statusCode).append("\n\n");
        
        userPrompt.append("Designed Invalid Parameters:\n");
        if (invalidParameters != null && !invalidParameters.isEmpty()) {
            for (java.util.Map.Entry<String, String> entry : invalidParameters.entrySet()) {
                userPrompt.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            userPrompt.append("  (General negative test)\n");
        }
        userPrompt.append("\n");
        
        userPrompt.append("Response Body:\n");
        userPrompt.append("```json\n");
        
        // 🔥 Use same smart JSON truncation logic to preserve all root-level error fields
        final int MAX_RESPONSE_BODY_SIZE = 16 * 1024; // 16KB max
        if (responseBody != null && responseBody.length() > MAX_RESPONSE_BODY_SIZE) {
            String processedBody = smartTruncateJsonResponse(responseBody, MAX_RESPONSE_BODY_SIZE);
            userPrompt.append(processedBody);
        } else {
            userPrompt.append(responseBody).append("\n");
        }
        
        userPrompt.append("```\n\n");
        
        userPrompt.append("Examples:\n");
        userPrompt.append("Example 1 (Error is about our invalid input - TEST PASSES):\n");
        userPrompt.append("Invalid Parameter: basicPriceRate = -100\n");
        userPrompt.append("Response: {\"status\":0,\"msg\":\"Invalid price rate: must be positive\"}\n");
        userPrompt.append("FAILED: true\n");
        userPrompt.append("RELATED_TO_INVALID_INPUT: true\n");
        userPrompt.append("RCA: The error message 'Invalid price rate' directly relates to our invalid basicPriceRate parameter. The API correctly rejected our negative price value.\n\n");
        
        userPrompt.append("Example 2 (Error is UNRELATED to our invalid input - TEST FAILS):\n");
        userPrompt.append("Invalid Parameter: basicPriceRate = -100\n");
        userPrompt.append("Response: {\"status\":0,\"msg\":\"Authentication token expired\"}\n");
        userPrompt.append("FAILED: false\n");
        userPrompt.append("RELATED_TO_INVALID_INPUT: false\n");
        userPrompt.append("RCA: The error is about authentication, NOT about our invalid basicPriceRate. This is an unrelated error so the negative test FAILS.\n\n");
        
        userPrompt.append("Example 3 (API accepted invalid input - TEST FAILS):\n");
        userPrompt.append("Invalid Parameter: stationName = \"\" (empty string)\n");
        userPrompt.append("Response: {\"status\":1,\"msg\":\"Success\",\"data\":{\"id\":123}}\n");
        userPrompt.append("FAILED: false\n");
        userPrompt.append("RELATED_TO_INVALID_INPUT: false\n");
        userPrompt.append("RCA: The API accepted our empty station name without error. The invalid input was NOT rejected, so the negative test FAILS.\n\n");
        
        userPrompt.append("Now analyze the response above and provide your answer:\n");

        try {
            // Call LLM service
            String llmResponse = llmService.generateText(systemPrompt.toString(), userPrompt.toString(), 500, 0.3);
            
            // Handle null response from LLM
            if (llmResponse == null || llmResponse.trim().isEmpty()) {
                System.err.println("⚠️ LLM returned null/empty response for negative test validation");
                return new ValidationResult(false, "LLM returned empty response - validation skipped", "");
            }
            
            // Parse response
            boolean isFailed = false;
            boolean relatedToInvalidInput = false;
            String rca = "";
            
            String[] lines = llmResponse.split("\\r?\\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.startsWith("FAILED:")) {
                    String failedValue = trimmed.substring("FAILED:".length()).trim().toLowerCase();
                    isFailed = failedValue.equals("true");
                } else if (trimmed.startsWith("RELATED_TO_INVALID_INPUT:")) {
                    String relatedValue = trimmed.substring("RELATED_TO_INVALID_INPUT:".length()).trim().toLowerCase();
                    relatedToInvalidInput = relatedValue.equals("true");
                } else if (trimmed.startsWith("RCA:")) {
                    rca = trimmed.substring("RCA:".length()).trim();
                }
            }
            
            // For negative tests: both conditions must be true for the test to pass
            // The error must exist AND be related to our invalid input
            boolean negativeTestPassed = isFailed && relatedToInvalidInput;
            
            // Enhance RCA with context
            String enhancedRca = rca;
            if (!relatedToInvalidInput && isFailed) {
                enhancedRca = "[ERROR NOT RELATED TO INVALID INPUT] " + rca;
            } else if (!isFailed) {
                enhancedRca = "[NO ERROR DETECTED - INVALID INPUT ACCEPTED] " + rca;
            } else {
                enhancedRca = "[INVALID INPUT CORRECTLY REJECTED] " + rca;
            }
            
            return new ValidationResult(negativeTestPassed, enhancedRca, llmResponse);
            
        } catch (Exception e) {
            // Better error handling with exception type
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            System.err.println("⚠️ Failed to validate negative test response with LLM: " + errorMsg);
            e.printStackTrace(); // Print stack trace for debugging
            // Return non-failed by default (negative test fails if we can't validate)
            return new ValidationResult(false, "LLM validation failed: " + errorMsg, "");
        }
    }

    /**
     * 🔥 Smart JSON truncation that preserves ALL root-level fields for error detection.
     * 
     * Problem: Simple truncation could miss error indicators at the end of the response.
     * Example: {"status":1, "data":[...100KB...], "failed":true, "error":"timeout"}
     *          Simple truncation would cut off "failed" and "error"!
     * 
     * Solution: Parse JSON and keep all root-level fields, only truncate/summarize data arrays.
     */
    private String smartTruncateJsonResponse(String responseBody, int maxSize) {
        try {
            // Try to parse as JSON object using Gson (already in project dependencies)
            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
            com.google.gson.JsonElement element = parser.parse(responseBody);
            
            if (element.isJsonObject()) {
                com.google.gson.JsonObject jsonObject = element.getAsJsonObject();
                
                // Build a summary that includes ALL root-level fields
                StringBuilder summary = new StringBuilder();
                summary.append("{\n");
                
                // Collect all fields - preserve primitives, truncate arrays
                java.util.List<String> preservedFields = new java.util.ArrayList<>();
                java.util.List<String> truncatedArrays = new java.util.ArrayList<>();
                
                for (java.util.Map.Entry<String, com.google.gson.JsonElement> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    com.google.gson.JsonElement value = entry.getValue();
                    
                    if (value.isJsonArray()) {
                        com.google.gson.JsonArray arr = value.getAsJsonArray();
                        int arraySize = arr.size();
                        
                        // Show first 3 items + summary
                        StringBuilder arraySummary = new StringBuilder();
                        arraySummary.append("  \"").append(key).append("\": [");
                        
                        if (arraySize > 0) {
                            int itemsToShow = Math.min(3, arraySize);
                            for (int i = 0; i < itemsToShow; i++) {
                                if (i > 0) arraySummary.append(",");
                                String itemStr = arr.get(i).toString();
                                if (itemStr.length() > 200) {
                                    itemStr = itemStr.substring(0, 200) + "...}";
                                }
                                arraySummary.append("\n    ").append(itemStr);
                            }
                            if (arraySize > itemsToShow) {
                                arraySummary.append(",\n    /* ... ").append(arraySize - itemsToShow)
                                           .append(" more items (").append(arraySize).append(" total) ... */");
                            }
                            arraySummary.append("\n  ]");
                        } else {
                            arraySummary.append("]");
                        }
                        truncatedArrays.add(arraySummary.toString());
                        
                    } else if (value.isJsonObject() && value.toString().length() > 500) {
                        // Truncate large nested objects but show structure
                        String objStr = value.toString();
                        preservedFields.add("  \"" + key + "\": " + objStr.substring(0, 500) + "... /* truncated */}");
                        
                    } else {
                        // Preserve all primitive values - CRITICAL for error detection!
                        preservedFields.add("  \"" + key + "\": " + value.toString());
                    }
                }
                
                // Output: first all preserved fields, then truncated arrays
                boolean first = true;
                for (String field : preservedFields) {
                    if (!first) summary.append(",\n");
                    summary.append(field);
                    first = false;
                }
                for (String arr : truncatedArrays) {
                    if (!first) summary.append(",\n");
                    summary.append(arr);
                    first = false;
                }
                
                summary.append("\n}");
                summary.append("\n/* NOTE: Response was ").append(responseBody.length())
                       .append(" bytes. All root-level fields preserved, only data arrays summarized. */\n");
                
                return summary.toString();
            }
            
        } catch (Exception e) {
            // JSON parsing failed, fall back to simple truncation
            System.err.println("⚠️ JSON-aware truncation failed, using simple truncation: " + e.getMessage());
        }
        
        // Fallback: simple truncation at JSON boundary
        String truncated = responseBody.substring(0, Math.min(maxSize, responseBody.length()));
        int lastCloseBrace = truncated.lastIndexOf("},");
        if (lastCloseBrace > maxSize / 2) {
            truncated = truncated.substring(0, lastCloseBrace + 1);
        }
        return truncated + "\n... [TRUNCATED - Original size: " + responseBody.length() + " bytes]\n";
    }

    // ======================================================================
    // Cached soft-error validation (reduces LLM calls from N to ~1 per API)
    // ======================================================================

    /**
     * Validate a 2XX response using the soft-error rule cache.
     * On cache hit, returns instantly without calling the LLM.
     * On cache miss (first call per API, or unknown pattern), calls the LLM
     * with an enhanced prompt that both evaluates the response AND generates
     * a reusable rule for future calls.
     */
    public ValidationResult validateResponseWithCache(
            int statusCode, String responseBody,
            String serviceName, String method, String path,
            es.us.isa.restest.validation.SoftErrorRuleCache cache) {

        String normalizedPath = normalizeToTemplate(path);
        if (!normalizedPath.equals(path)) {
            System.out.println("[SoftErrorCache] Normalized cache key: '" + path + "' → '" + normalizedPath + "'");
        }
        String apiKey = method.toUpperCase() + " " + normalizedPath;

        // 1. Try cache first
        java.util.Optional<es.us.isa.restest.validation.SoftErrorRuleCache.CachedValidationResult> cached =
                cache.evaluate(apiKey, responseBody);
        if (cached.isPresent()) {
            es.us.isa.restest.validation.SoftErrorRuleCache.CachedValidationResult cr = cached.get();
            return new ValidationResult(cr.isFailed(), cr.getRca(), "[from cache: " + cr.getMatchSource() + "]");
        }

        // 2. Cache miss -- call LLM with rule-generation prompt
        return validateResponseAndGenerateRule(statusCode, responseBody, serviceName, method, path, cache, apiKey);
    }

    /**
     * Validate a negative-test 2XX response using the soft-error rule cache.
     * For negative tests the cache only handles the "is this a soft error?" part;
     * the "is the error related to invalid input?" check still requires the LLM
     * on the first occurrence, but the base soft-error detection is cached.
     */
    public ValidationResult validateNegativeResponseWithCache(
            int statusCode, String responseBody,
            String serviceName, String method, String path,
            java.util.Map<String, String> invalidParameters,
            es.us.isa.restest.validation.SoftErrorRuleCache cache) {

        String normalizedPath = normalizeToTemplate(path);
        if (!normalizedPath.equals(path)) {
            System.out.println("[SoftErrorCache] Normalized cache key: '" + path + "' → '" + normalizedPath + "'");
        }
        String apiKey = method.toUpperCase() + " " + normalizedPath;

        // 1. Try cache for the base soft-error detection
        java.util.Optional<es.us.isa.restest.validation.SoftErrorRuleCache.CachedValidationResult> cached =
                cache.evaluate(apiKey, responseBody);

        if (cached.isPresent()) {
            es.us.isa.restest.validation.SoftErrorRuleCache.CachedValidationResult cr = cached.get();
            if (!cr.isFailed()) {
                // Response looks successful -> negative test FAILS (invalid input was accepted)
                return new ValidationResult(false,
                        "[Cached Rule] [NO ERROR DETECTED - INVALID INPUT ACCEPTED] " + cr.getRca(),
                        "[from cache: " + cr.getMatchSource() + "]");
            }
            // Soft error detected by cache -> negative test PASSES
            return new ValidationResult(true,
                    "[Cached Rule] [INVALID INPUT CORRECTLY REJECTED] " + cr.getRca(),
                    "[from cache: " + cr.getMatchSource() + "]");
        }

        // 2. Cache miss -- call full LLM validation for negative tests
        //    This also seeds the cache for the base soft-error rule
        ValidationResult baseResult = validateResponseAndGenerateRule(
                statusCode, responseBody, serviceName, method, path, cache, apiKey);

        // Now delegate to the full negative-test validator for input-relatedness analysis
        // but only if the base check detected a soft error, otherwise short-circuit
        if (!baseResult.isFailed()) {
            return new ValidationResult(false,
                    "[NO ERROR DETECTED - INVALID INPUT ACCEPTED] " + baseResult.getRca(),
                    baseResult.getRawLlmResponse());
        }

        // Base detected failure -- call the full negative validator for relatedness check
        return validateNegativeTestResponse(statusCode, responseBody, serviceName, method, path, invalidParameters);
    }

    /**
     * Call the LLM with an enhanced prompt that evaluates the response AND produces
     * a reusable soft-error rule.  Stores the rule in the cache for future calls.
     */
    private ValidationResult validateResponseAndGenerateRule(
            int statusCode, String responseBody,
            String serviceName, String method, String path,
            es.us.isa.restest.validation.SoftErrorRuleCache cache, String apiKey) {

        StringBuilder systemPrompt = new StringBuilder();
        systemPrompt.append("You are an API testing expert. You have TWO tasks:\n\n");
        systemPrompt.append("TASK 1: Determine if this API response is a SOFT ERROR (returned 2XX but actually failed).\n");
        systemPrompt.append("TASK 2: Identify the PRIMARY indicator field and tell us what the OBSERVED value means.\n\n");

        systemPrompt.append("IMPORTANT: Only report what you can confirm from THIS response.\n");
        systemPrompt.append("Do NOT guess or speculate about values you have not seen.\n");
        systemPrompt.append("For example, if the response has status=1 and that means success, report that.\n");
        systemPrompt.append("Do NOT assume what other values (like 0 or -1) would mean -- we will ask you when we see them.\n\n");

        systemPrompt.append("COMMON PATTERNS (for reference, not assumption):\n");
        systemPrompt.append("- Fields like 'status', 'code', 'success', 'error' often indicate outcome\n");
        systemPrompt.append("- Fields like 'msg', 'message', 'error' often carry descriptive text\n");
        systemPrompt.append("- A null 'data' field sometimes indicates failure\n\n");

        systemPrompt.append("OUTPUT FORMAT (one value per line, every line required):\n");
        systemPrompt.append("FAILED: true|false\n");
        systemPrompt.append("RCA: <root cause analysis of THIS specific response>\n");
        systemPrompt.append("RULE_FIELD: <the primary JSON field that indicates success/failure, e.g. status>\n");
        systemPrompt.append("RULE_OBSERVED_VALUE: <the actual value of that field in THIS response, e.g. 1>\n");
        systemPrompt.append("RULE_OBSERVED_MEANING: success|failure\n");
        systemPrompt.append("RULE_MESSAGE_FIELDS: <comma-separated field names that carry descriptive messages, e.g. msg,message>\n");

        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append("API: ").append(method.toUpperCase()).append(" ").append(path).append("\n");
        userPrompt.append("Service: ").append(serviceName).append("\n");
        userPrompt.append("HTTP Status: ").append(statusCode).append("\n\n");
        userPrompt.append("Response Body:\n```json\n");

        final int MAX_SIZE = 16 * 1024;
        if (responseBody != null && responseBody.length() > MAX_SIZE) {
            userPrompt.append(smartTruncateJsonResponse(responseBody, MAX_SIZE));
        } else {
            userPrompt.append(responseBody).append("\n");
        }
        userPrompt.append("```\n\n");
        userPrompt.append("Analyze THIS response and report ONLY what you observe.\n");

        try {
            String llmResponse = llmService.generateText(systemPrompt.toString(), userPrompt.toString(), 600, 0.3);

            boolean isFailed = false;
            String rca = "";
            String ruleField = "";
            String observedValue = "";
            String observedMeaning = "";
            String ruleMessageFields = "";

            if (llmResponse != null) {
                for (String line : llmResponse.split("\\r?\\n")) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("FAILED:")) {
                        isFailed = trimmed.substring("FAILED:".length()).trim().toLowerCase().equals("true");
                    } else if (trimmed.startsWith("RCA:")) {
                        rca = trimmed.substring("RCA:".length()).trim();
                    } else if (trimmed.startsWith("RULE_FIELD:")) {
                        ruleField = trimmed.substring("RULE_FIELD:".length()).trim();
                    } else if (trimmed.startsWith("RULE_OBSERVED_VALUE:")) {
                        observedValue = trimmed.substring("RULE_OBSERVED_VALUE:".length()).trim();
                    } else if (trimmed.startsWith("RULE_OBSERVED_MEANING:")) {
                        observedMeaning = trimmed.substring("RULE_OBSERVED_MEANING:".length()).trim().toLowerCase();
                    } else if (trimmed.startsWith("RULE_MESSAGE_FIELDS:")) {
                        ruleMessageFields = trimmed.substring("RULE_MESSAGE_FIELDS:".length()).trim();
                    }
                }
            }

            // Build or extend the rule with ONLY the confirmed observation
            if (!cache.hasRule(apiKey)) {
                es.us.isa.restest.validation.SoftErrorRuleCache.SoftErrorRule rule =
                        new es.us.isa.restest.validation.SoftErrorRuleCache.SoftErrorRule();

                if (!ruleField.isEmpty()) {
                    es.us.isa.restest.validation.SoftErrorRuleCache.FieldCheck fc =
                            new es.us.isa.restest.validation.SoftErrorRuleCache.FieldCheck();
                    fc.setField(ruleField);
                    // Only put the observed value in the confirmed list
                    if ("failure".equals(observedMeaning)) {
                        fc.setFailureValues(new java.util.ArrayList<>(java.util.Collections.singletonList(observedValue)));
                        fc.setSuccessValues(new java.util.ArrayList<>());
                    } else {
                        fc.setSuccessValues(new java.util.ArrayList<>(java.util.Collections.singletonList(observedValue)));
                        fc.setFailureValues(new java.util.ArrayList<>());
                    }
                    rule.setFieldChecks(new java.util.ArrayList<>(java.util.Collections.singletonList(fc)));
                }
                rule.setFailureMessageFields(splitCsv(ruleMessageFields));
                cache.registerRule(apiKey, rule);
            } else {
                // Rule exists but we got here because of an unknown value -- extend it
                if (!ruleField.isEmpty() && !observedValue.isEmpty()) {
                    cache.addObservedValue(apiKey, ruleField, observedValue,
                            "failure".equals(observedMeaning));
                }
            }

            // Record the response pattern for instant future matching
            String signature = cache.buildSignature(apiKey, responseBody);
            if (!signature.isEmpty()) {
                es.us.isa.restest.validation.SoftErrorRuleCache.PatternEntry pattern =
                        new es.us.isa.restest.validation.SoftErrorRuleCache.PatternEntry(signature, isFailed, rca);
                cache.addPattern(apiKey, pattern);
            }

            return new ValidationResult(isFailed, rca, llmResponse != null ? llmResponse : "");

        } catch (Exception e) {
            System.err.println("Failed to validate response with LLM (rule generation): " + e.getMessage());
            return new ValidationResult(false, "LLM validation failed: " + e.getMessage(), "");
        }
    }

    /**
     * Normalizes a concrete URL path to a generic template by replacing dynamic
     * path segments (UUIDs, order IDs, numeric IDs, long tokens) with "{id}".
     * This prevents the soft-error rule cache from exploding with one entry per
     * unique path-parameter value when the same API endpoint is tested with many
     * different inputs.
     *
     * <p>If the path already contains {@code {param}} style placeholders it is
     * returned unchanged, so OAS template paths are always safe to pass through.
     *
     * <p>Examples:
     * <pre>
     *   /api/v1/orders/ORD100260405A          → /api/v1/orders/{id}
     *   /api/v1/execute/collected/42           → /api/v1/execute/collected/{id}
     *   /api/v1/items/123e4567-e89b-12d3-a456-426614174000 → /api/v1/items/{id}
     *   /api/v1/adminrouteservice/adminroute   → /api/v1/adminrouteservice/adminroute  (unchanged)
     * </pre>
     */
    private static String normalizeToTemplate(String path) {
        if (path == null || path.contains("{")) {
            return path; // already a URI template — nothing to do
        }
        String result = path;

        // 1. Strict RFC-4122 UUID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (all hex)
        result = result.replaceAll(
                "(?<=/)[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(?=/|$)",
                "{id}");

        // 2. UUID-like with non-hex characters in the segments (test-generated IDs)
        //    e.g. qrstuvwx-yz01-2345-6789-abcdef012345
        result = result.replaceAll(
                "(?<=/)[a-zA-Z0-9]{4,8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{6,12}(?=/|$)",
                "{id}");

        // 3. Order-ID style: 2+ UPPERCASE letters followed by 5+ digits, optional suffix
        //    e.g. ORD100260405A, ORD20230408B, TKT12345
        result = result.replaceAll(
                "(?<=/)[A-Z]{2,}[0-9]{5,}[A-Za-z0-9]*(?=/|$)",
                "{id}");

        // 4. Pure numeric ID segments  e.g. /api/v1/prices/1301, /api/v1/trains/501
        result = result.replaceAll(
                "(?<=/)[0-9]+(?=/|$)",
                "{id}");

        // 5. Very long alphanumeric tokens (20+ chars): hashes, base64 fragments,
        //    overflow strings, etc.
        result = result.replaceAll(
                "(?<=/)[a-zA-Z0-9_-]{20,}(?=/|$)",
                "{id}");

        // 6. Segments that look like account/user IDs: letters followed by 4+ trailing digits
        //    e.g. user98765, account12345 — but NOT service-name words like "travel2service"
        //    (those contain digits in the middle, not exclusively at the tail).
        result = result.replaceAll(
                "(?<=/)[a-zA-Z][a-zA-Z0-9-]*[0-9]{4,}(?=/|$)",
                "{id}");

        return result;
    }

    private static java.util.List<String> splitCsv(String csv) {
        java.util.List<String> result = new java.util.ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String part : csv.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) result.add(trimmed);
        }
        return result;
    }

    /**
     * Result of LLM response validation
     */
    public static class ValidationResult {
        private final boolean failed;
        private final String rca;
        private final String rawLlmResponse;

        public ValidationResult(boolean failed, String rca, String rawLlmResponse) {
            this.failed = failed;
            this.rca = rca;
            this.rawLlmResponse = rawLlmResponse;
        }

        public boolean isFailed() {
            return failed;
        }

        public String getRca() {
            return rca;
        }

        public String getRawLlmResponse() {
            return rawLlmResponse;
        }
    }
}
