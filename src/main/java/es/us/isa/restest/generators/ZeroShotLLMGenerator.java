package es.us.isa.restest.generators;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
        promptBuilder.append("You are an API testing assistant that generates realistic parameter values.\n\n");
        
        // Parameter details
        promptBuilder.append("Parameter Information:\n");
        promptBuilder.append("- Name: ").append(safeStr(param.getName())).append("\n");
        
        String description = safeStr(param.getDescription());
        if (!description.isEmpty()) {
            promptBuilder.append("- Description: ").append(description).append("\n");
        }
        
        promptBuilder.append("- Location: ").append(safeStr(param.getInLocation())).append("\n");
        promptBuilder.append("- Data Type: ").append(safeStr(param.getType())).append("\n");
        
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
            promptBuilder.append("- Pattern: ").append(regex).append("\n");
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
            
            // Context-specific guidance based on parameter name/type
            String guidance = getContextualGuidance(param);
            if (!guidance.isEmpty()) {
                promptBuilder.append("Domain Guidance: ").append(guidance).append("\n\n");
            }
            
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
            
            // Context-specific guidance based on parameter name/type
            String guidance = getContextualGuidance(param);
            if (!guidance.isEmpty()) {
                promptBuilder.append("Domain Guidance: ").append(guidance).append("\n\n");
            }
            
            promptBuilder.append("Example Format (for 3 values):\n");
            promptBuilder.append("Value1\n");
            promptBuilder.append("Value2\n");
            promptBuilder.append("Value3\n\n");
            
            promptBuilder.append("Now generate your ").append(howMany).append(" values, one per line:");
        }
        
        return promptBuilder.toString();
    }
    
    /**
     * Provide context-specific guidance based on parameter characteristics
     */
    private String getContextualGuidance(ParameterInfo param) {
        String name = safeStr(param.getName()).toLowerCase();
        String type = safeStr(param.getType()).toLowerCase();
        
        // Station/location related
        if (name.contains("station") || name.contains("location") || name.contains("place")) {
            return "Generate realistic station/location names like train stations, bus stops, or landmarks.";
        }
        
        // ID related
        if (name.contains("id") && (name.contains("login") || name.contains("user"))) {
            return "Generate realistic user login IDs/usernames with mix of letters and numbers.";
        }
        
        // Date/time related
        if (name.contains("date") || name.contains("time") || type.contains("date")) {
            return "Generate valid date/time values in appropriate format.";
        }
        
        // List related
        if (name.contains("list") || type.contains("array")) {
            return "Generate single values that would be elements in a list, not the entire list.";
        }
        
        // Password related
        if (name.contains("password") || name.contains("pass")) {
            return "Generate realistic password patterns with appropriate complexity.";
        }
        
        // Number related
        if (type.contains("int") || type.contains("number")) {
            return "Generate realistic numeric values appropriate for the context.";
        }
        
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
