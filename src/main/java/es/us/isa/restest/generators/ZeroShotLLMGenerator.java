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
    
    /**
     * Generate comprehensive invalid inputs for all fault types
     * Returns an InvalidInputPool with properly typed invalid values
     */
    public es.us.isa.restest.inputs.InvalidInputPool generateInvalidInputPool(ParameterInfo param) {
        System.out.println("*** ZeroShotLLMGenerator.generateInvalidInputPool for: " + param.getName() + 
                          " (type: " + param.getType() + ")");
        
        es.us.isa.restest.inputs.InvalidInputPool pool = 
            new es.us.isa.restest.inputs.InvalidInputPool(param.getName(), safeStr(param.getType()));
        
        // Generate each type of invalid input
        generateTypeMismatchInputs(param, pool);
        generateRegexMismatchInputs(param, pool);
        generateSemanticMismatchInputs(param, pool);
        generateOverflowInputs(param, pool);
        generateEmptyInputs(param, pool);
        generateNullInputs(param, pool);
        generateSpecialCharacterInputs(param, pool);
        generateBoundaryViolationInputs(param, pool);
        
        System.out.println("*** Generated invalid input pool:\n" + pool.getPoolSummary());
        
        return pool;
    }
    
    /**
     * Generate type mismatch inputs - wrong data type for the parameter
     * CRITICAL: These are stored as raw objects (Integer, Boolean, etc.) NOT strings
     */
    private void generateTypeMismatchInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 3-5 TYPE MISMATCH invalid values for parameter '" + param.getName() + "'.\n" +
                       "Expected type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values of WRONG TYPE that would cause type validation errors.\n" +
                       "Examples:\n" +
                       "- If expecting string, provide: integer 123, boolean true, array [1,2,3]\n" +
                       "- If expecting integer, provide: string 'abc', boolean false, object {}\n" +
                       "- If expecting boolean, provide: string 'yes', integer 1, array []\n\n" +
                       "IMPORTANT: Provide actual type-mismatched values, not string representations.\n" +
                       "Format: TYPE:VALUE where TYPE is integer|string|boolean|null|array|object\n" +
                       "Examples: integer:999, string:notANumber, boolean:true, null:null\n" +
                       "Return only the values, one per line:";
        
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
     * Parse typed value from LLM response (format: "type:value")
     * Returns actual typed object (Integer, Boolean, etc.) not String
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
        // 🔥 FIX: Strip leading numbers/dots (e.g., "3integer" -> "integer", "1. integer" -> "integer")
        type = type.replaceAll("^[0-9.\\s]+", "");
        String value = parts[1].trim();
        
        // Parse based on specified type
        try {
            switch (type) {
                case "integer":
                case "int":
                case "number":
                    return Integer.parseInt(value);
                    
                case "long":
                    return Long.parseLong(value);
                    
                case "double":
                case "float":
                    return Double.parseDouble(value);
                    
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
            System.err.println("Failed to parse typed value: " + line + " - " + e.getMessage());
            return value; // Return as string if parsing fails
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
                       "Generate 3-5 values that DO NOT MATCH this regex pattern for parameter '" + param.getName() + "':\n" +
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
                       "Generate 3-5 SEMANTICALLY INVALID values for parameter '" + param.getName() + "'.\n" +
                       "Type: " + param.getType() + "\n" +
                       "Description: " + safeStr(param.getDescription()) + "\n\n" +
                       "Generate values that have correct type and format but are MEANINGLESS or IMPOSSIBLE.\n" +
                       "Examples:\n" +
                       "- Age parameter: negative numbers (-5, -100), impossibly high (999, 500)\n" +
                       "- Email parameter: invalid format (missing @, no domain)\n" +
                       "- Date parameter: impossible dates (Feb 30, Month 13)\n" +
                       "- Country code: non-existent codes (ZZZ, XXX)\n" +
                       "- Phone number: wrong format or length\n\n" +
                       "Return only the semantically invalid values, one per line:";
        
        String response = callLLM(prompt);
        List<String> values = parseLines(response);
        
        for (String value : values) {
            pool.addValue(es.us.isa.restest.inputs.InvalidInputType.SEMANTIC_MISMATCH, value);
        }
    }
    
    /**
     * Generate overflow inputs
     */
    private void generateOverflowInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 3-5 OVERFLOW values for parameter '" + param.getName() + "'.\n" +
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
     * Generate empty inputs
     * ONLY for REQUIRED parameters - optional parameters can legitimately be empty
     */
    private void generateEmptyInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        // Skip empty inputs for optional parameters - they are valid!
        if (param.getRequired() == null || !param.getRequired()) {
            System.out.println("⚠️  Skipping EMPTY_INPUT generation for optional parameter: " + param.getName());
            return;
        }
        
        System.out.println("✅ Generating EMPTY_INPUT for required parameter: " + param.getName());
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
     * Generate null inputs
     * ONLY for REQUIRED parameters - optional parameters can legitimately be null
     */
    private void generateNullInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        // Skip null inputs for optional parameters - they are valid!
        if (param.getRequired() == null || !param.getRequired()) {
            System.out.println("⚠️  Skipping NULL_INPUT generation for optional parameter: " + param.getName());
            return;
        }
        
        System.out.println("✅ Generating NULL_INPUT for required parameter: " + param.getName());
        
        // Actual null
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, null);
        
        // String representations of null (sometimes APIs parse these)
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "null");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "NULL");
        pool.addValue(es.us.isa.restest.inputs.InvalidInputType.NULL_INPUT, "Null");
    }
    
    /**
     * Generate special character inputs (potential injection attempts)
     */
    private void generateSpecialCharacterInputs(ParameterInfo param, es.us.isa.restest.inputs.InvalidInputPool pool) {
        String prompt = "Current Date/Time: " + getCurrentTimestamp() + "\n\n" +
                       "Generate 3-5 values with SPECIAL CHARACTERS or INJECTION attempts for parameter '" + param.getName() + "'.\n" +
                       "Type: " + param.getType() + "\n\n" +
                       "Generate values with malicious or special characters:\n" +
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
                       "Generate 3-5 BOUNDARY VIOLATION values for parameter '" + param.getName() + "'.\n" +
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
     * Build cache key that includes parameter name, type, and location
     * This ensures parameters with same name but different types are cached separately
     */
    private String buildCacheKey(ParameterInfo param) {
        String name = param.getName() != null ? param.getName() : "unknown";
        String type = param.getType() != null ? param.getType() : "unknown";
        String location = param.getInLocation() != null ? param.getInLocation() : "unknown";
        return name + ":" + type + ":" + location;
    }

    /**
     * Build a textual prompt describing the parameter and how many examples we want.
     */
    private String buildPrompt(ParameterInfo param, int howMany) {
        StringBuilder promptBuilder = new StringBuilder();
        
        // Clear introduction with context
        promptBuilder.append("You are an API testing assistant that generates realistic parameter values.\n");
        promptBuilder.append("Current Date/Time: ").append(getCurrentTimestamp()).append("\n\n");
        
        // API Context (if available)
        String apiName = safeStr(param.getApiName());
        if (!apiName.isEmpty()) {
            promptBuilder.append("API Context:\n");
            promptBuilder.append("- API Endpoint: ").append(apiName).append("\n");
            
            String serviceName = safeStr(param.getServiceName());
            if (!serviceName.isEmpty()) {
                promptBuilder.append("- Service: ").append(serviceName).append("\n");
            }
            
            if (param.getAllParameterNames() != null && !param.getAllParameterNames().isEmpty()) {
                promptBuilder.append("- All Parameters in this API: ").append(String.join(", ", param.getAllParameterNames())).append("\n");
            }
            promptBuilder.append("\n");
        }
        
        // Parameter details
        promptBuilder.append("Parameter Information:\n");
        promptBuilder.append("- Name: ").append(safeStr(param.getName())).append("\n");
        
        String description = safeStr(param.getDescription());
        if (!description.isEmpty()) {
            promptBuilder.append("- Description: ").append(description).append("\n");
        }
        
        promptBuilder.append("- Location: ").append(safeStr(param.getInLocation())).append("\n");
        promptBuilder.append("- Data Type: ").append(safeStr(param.getType())).append("\n");
        
        // Add required/optional information
        if (param.getRequired() != null) {
            promptBuilder.append("- Required: ").append(param.getRequired() ? "Yes (mandatory)" : "No (optional)").append("\n");
        }
        
        String format = safeStr(param.getFormat());
        if (!format.isEmpty()) {
            promptBuilder.append("- Format: ").append(format).append("\n");
        }
        
        String example = safeStr(param.getSchemaExample());
        if (!example.isEmpty()) {
            promptBuilder.append("- Example: ").append(example).append("\n");
        }
        
        String regex = safeStr(param.getRegex());
        if (!regex.isEmpty()) {
            promptBuilder.append("- Pattern/Regex: ").append(regex).append("\n");
            promptBuilder.append("  (Your generated values MUST match this pattern)\n");
        }
        
        // Clear task instructions with emphasis on formatting
        promptBuilder.append("\nTask: Generate ").append(howMany).append(" realistic test values for this parameter.\n\n");
        
        String paramType = safeStr(param.getType()).toLowerCase();
        
        if ("array".equals(paramType)) {
            // For array parameters, generate JSON array format
            promptBuilder.append("CRITICAL FORMATTING REQUIREMENT:\n");
            promptBuilder.append("You MUST return a valid JSON array containing exactly ").append(howMany).append(" values.\n");
            promptBuilder.append("Format: [\"value1\", \"value2\", \"value3\"]\n");
            promptBuilder.append("Do NOT add explanations, numbering, or extra formatting.\n\n");
            
            promptBuilder.append("Content Requirements:\n");
            promptBuilder.append("- Values should be appropriate for the parameter type and context\n");
            promptBuilder.append("- Generate diverse, realistic examples that an API might actually receive\n");
            promptBuilder.append("- Consider common use cases and edge cases\n\n");
            
            promptBuilder.append("Intelligent Value Generation Guidelines:\n");
            promptBuilder.append("• Analyze the parameter name, type, description, and format to understand its purpose\n");
            promptBuilder.append("• For temporal parameters (dates, times, timestamps): If the parameter suggests future events\n");
            promptBuilder.append("  (e.g., 'departure', 'arrival', 'booking', 'scheduled', 'planned'), generate values AFTER\n");
            promptBuilder.append("  the Current Date/Time (use realistic near-future: 1-30 days ahead)\n");
            promptBuilder.append("• For IDs/identifiers: Match expected format patterns (numeric, alphanumeric, UUID, etc.)\n");
            promptBuilder.append("• For location/place parameters: Use realistic, specific names appropriate to the domain\n");
            promptBuilder.append("• For numeric parameters: Use realistic ranges appropriate to the context\n");
            promptBuilder.append("• For string parameters: Consider typical business domain values, not generic placeholders\n");
            promptBuilder.append("• Ensure generated values would pass typical validation rules\n\n");
            
            promptBuilder.append("Example Format (for 3 values):\n");
            promptBuilder.append("[\"New York Penn Station\", \"Los Angeles Union Station\", \"Chicago Union Station\"]\n\n");
            
            promptBuilder.append("Now generate your JSON array with ").append(howMany).append(" values:");
        } else {
            // For non-array parameters, use line-separated format
            promptBuilder.append("CRITICAL FORMATTING REQUIREMENT:\n");
            promptBuilder.append("You MUST return exactly ").append(howMany).append(" separate lines.\n");
            promptBuilder.append("Each line contains exactly ONE value.\n");
            promptBuilder.append("Press ENTER after each value.\n");
            promptBuilder.append("Do NOT put multiple values on the same line.\n\n");
            
            promptBuilder.append("Content Requirements:\n");
            promptBuilder.append("- Values should be appropriate for the parameter type and context\n");
            promptBuilder.append("- Generate diverse, realistic examples that an API might actually receive\n");
            promptBuilder.append("- Consider common use cases and edge cases\n\n");
            
            promptBuilder.append("Intelligent Value Generation Guidelines:\n");
            promptBuilder.append("• Analyze the parameter name, type, description, and format to understand its purpose\n");
            promptBuilder.append("• For temporal parameters (dates, times, timestamps): If the parameter name suggests future events\n");
            promptBuilder.append("  (e.g., contains 'departure', 'arrival', 'booking', 'scheduled', 'planned', 'start', 'end'),\n");
            promptBuilder.append("  generate values AFTER the Current Date/Time shown above (use realistic near-future: 1-30 days ahead)\n");
            promptBuilder.append("• For IDs/identifiers: Match expected format patterns (numeric, alphanumeric, UUID, etc.)\n");
            promptBuilder.append("• For location/place parameters: Use realistic, specific names appropriate to the domain\n");
            promptBuilder.append("• For numeric parameters: Use realistic ranges appropriate to the context\n");
            promptBuilder.append("• For string parameters: Consider typical business domain values, not generic placeholders\n");
            promptBuilder.append("• Ensure generated values would pass typical validation rules\n\n");
            
            promptBuilder.append("Example Format (for 3 values):\n");
            promptBuilder.append("Value1\n");
            promptBuilder.append("Value2\n");
            promptBuilder.append("Value3\n\n");
            
            promptBuilder.append("Now generate your ").append(howMany).append(" values, one per line:");
        }
        
        return promptBuilder.toString();
    }
    
    /**
     * DEPRECATED: Replaced by intelligent prompt engineering in buildPrompt()
     * This hardcoded approach has been removed in favor of letting the LLM intelligently
     * interpret parameter context from the comprehensive prompt instructions.
     */
    @Deprecated
    private String getContextualGuidance(ParameterInfo param) {
        // No longer needed - intelligent guidelines are now built into the main prompt
        return "";
    }


    // Legacy method removed - now using unified LLM service via callLLM()


    private String callLLM(String prompt) {
        String systemContent =
                "You are an AI system that generates parameter values for API testing. " +
                        "CRITICAL: When asked to generate N values, you MUST return exactly N lines. " +
                        "Each line contains exactly one value. Use line breaks between values. " +
                        "Do NOT put multiple values on the same line separated by spaces or commas. " +
                        "Do NOT add explanations, numbering, or extra formatting.";

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
                lines.add(trimmed);
            }
        }
        return lines;
    }

    private String safeStr(String s) {
        return (s == null ? "" : s);
    }
}
