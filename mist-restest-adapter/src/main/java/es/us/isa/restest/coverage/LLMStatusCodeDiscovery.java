package es.us.isa.restest.coverage;

import es.us.isa.restest.llm.LLMConfig;
import es.us.isa.restest.llm.LLMService;
import es.us.isa.restest.inputs.llm.ParameterInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses LLM to discover ALL possible HTTP status codes for an API operation.
 * This class runs AFTER first execution to use actual results for informed discovery.
 * 
 * The discovery is fully dynamic - the LLM analyzes the API semantics and identifies
 * all possible status codes (not just common ones), along with strategies to trigger each.
 */
public class LLMStatusCodeDiscovery {
    
    private static final Logger log = LogManager.getLogger(LLMStatusCodeDiscovery.class);
    
    private final LLMService llmService;
    
    // Cache to avoid redundant LLM calls for the same API
    private final Map<String, List<StatusCodeTarget>> discoveryCache = new HashMap<>();
    
    public LLMStatusCodeDiscovery(LLMService llmService) {
        this.llmService = llmService;
    }
    
    /**
     * Create instance from properties.
     */
    public static LLMStatusCodeDiscovery fromProperties(Map<String, String> properties) {
        LLMConfig config = LLMConfig.fromProperties(properties);
        LLMService service = LLMService.getInstance(config);
        return new LLMStatusCodeDiscovery(service);
    }
    
    /**
     * Discover all possible status codes for an API operation.
     * Uses execution results to inform the discovery.
     * 
     * @param serviceName The microservice name
     * @param httpMethod The HTTP method (GET, POST, etc.)
     * @param path The API path
     * @param parameters List of parameters for this operation
     * @param observedStatusCodes Status codes already observed in execution
     * @param sampleResponses Sample responses from execution (for context)
     * @return List of StatusCodeTarget objects representing all possible status codes
     */
    public List<StatusCodeTarget> discoverStatusCodes(
            String serviceName,
            String httpMethod,
            String path,
            List<ParameterInfo> parameters,
            Set<Integer> observedStatusCodes,
            List<String> sampleResponses) {
        
        String apiKey = getApiKey(httpMethod, path);
        
        // Check cache first
        if (discoveryCache.containsKey(apiKey)) {
            log.debug("Using cached status code discovery for {}", apiKey);
            return new ArrayList<>(discoveryCache.get(apiKey));
        }
        
        log.info("Discovering status codes for {} {} (service: {})", httpMethod, path, serviceName);
        
        String prompt = buildDiscoveryPrompt(serviceName, httpMethod, path, parameters, 
                                             observedStatusCodes, sampleResponses);
        
        String systemPrompt = buildSystemPrompt();
        
        try {
            String llmResponse = llmService.generateText(systemPrompt, prompt, 2000, 0.3);
            
            if (llmResponse == null || llmResponse.trim().isEmpty()) {
                log.warn("LLM returned empty response for status code discovery");
                return createDefaultTargets(observedStatusCodes);
            }
            
            List<StatusCodeTarget> targets = parseDiscoveryResponse(llmResponse);
            
            if (targets.isEmpty()) {
                log.warn("Failed to parse LLM response, using defaults");
                return createDefaultTargets(observedStatusCodes);
            }
            
            // Cache the result
            discoveryCache.put(apiKey, new ArrayList<>(targets));
            
            log.info("Discovered {} possible status codes for {}: {}", 
                targets.size(), apiKey, 
                targets.stream().map(t -> String.valueOf(t.getStatusCode())).reduce((a, b) -> a + ", " + b).orElse(""));
            
            return targets;
            
        } catch (Exception e) {
            log.error("Error during status code discovery for {}: {}", apiKey, e.getMessage(), e);
            return createDefaultTargets(observedStatusCodes);
        }
    }
    
    /**
     * Simplified discovery without execution context (for initial setup).
     */
    public List<StatusCodeTarget> discoverStatusCodes(
            String serviceName,
            String httpMethod,
            String path,
            List<ParameterInfo> parameters) {
        return discoverStatusCodes(serviceName, httpMethod, path, parameters, 
                                   Collections.emptySet(), Collections.emptyList());
    }
    
    private String buildSystemPrompt() {
        return "You are an API testing expert specializing in HTTP status codes and REST API behavior.\n" +
               "Your task is to analyze API operations and identify ALL possible HTTP status codes they could return.\n\n" +
               "Be comprehensive - consider all standard HTTP status codes (1xx, 2xx, 3xx, 4xx, 5xx).\n" +
               "For each status code, provide a clear strategy to trigger it and suggested parameter values.\n\n" +
               "Always respond with valid JSON only. No markdown, no explanations outside the JSON.";
    }
    
    private String buildDiscoveryPrompt(
            String serviceName,
            String httpMethod,
            String path,
            List<ParameterInfo> parameters,
            Set<Integer> observedStatusCodes,
            List<String> sampleResponses) {
        
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Analyze this REST API operation and identify ALL HTTP status codes it could possibly return.\n\n");
        
        prompt.append("=== API OPERATION ===\n");
        prompt.append("Service: ").append(serviceName).append("\n");
        prompt.append("Method: ").append(httpMethod).append("\n");
        prompt.append("Path: ").append(path).append("\n\n");
        
        if (parameters != null && !parameters.isEmpty()) {
            prompt.append("=== PARAMETERS ===\n");
            for (ParameterInfo param : parameters) {
                prompt.append("- ").append(param.getName())
                      .append(" (").append(param.getType()).append(")")
                      .append(Boolean.TRUE.equals(param.getRequired()) ? " [REQUIRED]" : " [optional]");
                if (param.getDescription() != null && !param.getDescription().isEmpty()) {
                    prompt.append(": ").append(param.getDescription());
                }
                prompt.append("\n");
            }
            prompt.append("\n");
        }
        
        if (!observedStatusCodes.isEmpty()) {
            prompt.append("=== ALREADY OBSERVED STATUS CODES ===\n");
            prompt.append("From first execution: ").append(observedStatusCodes).append("\n\n");
        }
        
        if (sampleResponses != null && !sampleResponses.isEmpty()) {
            prompt.append("=== SAMPLE RESPONSES ===\n");
            for (int i = 0; i < Math.min(3, sampleResponses.size()); i++) {
                String response = sampleResponses.get(i);
                if (response.length() > 500) {
                    response = response.substring(0, 500) + "...";
                }
                prompt.append("Response ").append(i + 1).append(": ").append(response).append("\n");
            }
            prompt.append("\n");
        }
        
        prompt.append("=== TASK ===\n");
        prompt.append("Identify ALL possible HTTP status codes this API could return.\n");
        prompt.append("Consider these categories:\n");
        prompt.append("- 2xx Success: 200, 201, 202, 204, 206...\n");
        prompt.append("- 4xx Client Errors: 400, 401, 403, 404, 405, 409, 422, 429...\n");
        prompt.append("- 5xx Server Errors: 500, 502, 503, 504...\n");
        prompt.append("- 3xx Redirects if applicable: 301, 302, 304...\n\n");
        
        prompt.append("For EACH status code, provide:\n");
        prompt.append("1. statusCode: The HTTP status code number\n");
        prompt.append("2. category: Category name (Success, Client Error, Server Error, Redirect)\n");
        prompt.append("3. description: When/why this status code would be returned\n");
        prompt.append("4. triggerStrategy: How to trigger this status code\n");
        prompt.append("5. requiresAuthManipulation: true/false - does triggering require auth changes?\n");
        prompt.append("6. suggestedInputs: Parameter values to trigger this code (JSON object)\n\n");
        
        prompt.append("=== RESPONSE FORMAT ===\n");
        prompt.append("Respond with a JSON array ONLY (no markdown, no explanation):\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"statusCode\": 200,\n");
        prompt.append("    \"category\": \"Success\",\n");
        prompt.append("    \"description\": \"Successful operation\",\n");
        prompt.append("    \"triggerStrategy\": \"Provide valid inputs for all parameters\",\n");
        prompt.append("    \"requiresAuthManipulation\": false,\n");
        prompt.append("    \"suggestedInputs\": {\"param1\": \"validValue\"}\n");
        prompt.append("  },\n");
        prompt.append("  ...\n");
        prompt.append("]\n");
        
        return prompt.toString();
    }
    
    /**
     * Parse the LLM response into StatusCodeTarget objects.
     */
    List<StatusCodeTarget> parseDiscoveryResponse(String llmResponse) {
        List<StatusCodeTarget> targets = new ArrayList<>();
        
        try {
            // Try to extract JSON array from response
            String jsonStr = extractJsonArray(llmResponse);
            
            if (jsonStr == null || jsonStr.isEmpty()) {
                log.warn("Could not extract JSON array from LLM response");
                return targets;
            }
            
            JSONArray jsonArray = new JSONArray(jsonStr);
            
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    StatusCodeTarget target = StatusCodeTarget.fromLLMResponse(entry);
                    targets.add(target);
                    log.debug("Parsed status code target: {}", target);
                } catch (Exception e) {
                    log.warn("Failed to parse status code entry {}: {}", i, e.getMessage());
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to parse LLM discovery response: {}", e.getMessage());
        }
        
        return targets;
    }
    
    /**
     * Extract JSON array from LLM response (handles markdown code blocks, etc.)
     */
    private String extractJsonArray(String response) {
        if (response == null) return null;
        
        String cleaned = response.trim();
        
        // Remove markdown code blocks if present
        if (cleaned.contains("```json")) {
            int start = cleaned.indexOf("```json") + 7;
            int end = cleaned.indexOf("```", start);
            if (end > start) {
                cleaned = cleaned.substring(start, end).trim();
            }
        } else if (cleaned.contains("```")) {
            int start = cleaned.indexOf("```") + 3;
            int end = cleaned.indexOf("```", start);
            if (end > start) {
                cleaned = cleaned.substring(start, end).trim();
            }
        }
        
        // Find the JSON array
        int arrayStart = cleaned.indexOf('[');
        int arrayEnd = cleaned.lastIndexOf(']');
        
        if (arrayStart >= 0 && arrayEnd > arrayStart) {
            return cleaned.substring(arrayStart, arrayEnd + 1);
        }
        
        return null;
    }
    
    /**
     * Create default status code targets when LLM fails.
     * Based on common REST API patterns.
     */
    private List<StatusCodeTarget> createDefaultTargets(Set<Integer> observedCodes) {
        List<StatusCodeTarget> defaults = new ArrayList<>();
        
        // Always include common status codes
        if (!observedCodes.contains(200)) {
            defaults.add(new StatusCodeTarget.Builder(200)
                .category("Success")
                .description("Successful operation")
                .triggerStrategy("Provide valid inputs")
                .build());
        }
        
        if (!observedCodes.contains(400)) {
            defaults.add(new StatusCodeTarget.Builder(400)
                .category("Client Error")
                .description("Bad request - invalid input")
                .triggerStrategy("Provide malformed or invalid parameters")
                .build());
        }
        
        if (!observedCodes.contains(401)) {
            defaults.add(new StatusCodeTarget.Builder(401)
                .category("Client Error")
                .description("Unauthorized - missing or invalid auth")
                .triggerStrategy("Remove or invalidate authentication")
                .requiresAuthManipulation(true)
                .build());
        }
        
        if (!observedCodes.contains(403)) {
            defaults.add(new StatusCodeTarget.Builder(403)
                .category("Client Error")
                .description("Forbidden - insufficient permissions")
                .triggerStrategy("Use credentials with insufficient permissions")
                .requiresAuthManipulation(true)
                .build());
        }
        
        if (!observedCodes.contains(404)) {
            defaults.add(new StatusCodeTarget.Builder(404)
                .category("Client Error")
                .description("Not found - resource does not exist")
                .triggerStrategy("Use non-existent resource ID")
                .addSuggestedInput("id", "NONEXISTENT_ID_99999")
                .build());
        }
        
        if (!observedCodes.contains(500)) {
            defaults.add(new StatusCodeTarget.Builder(500)
                .category("Server Error")
                .description("Internal server error")
                .triggerStrategy("Trigger edge case or malformed data")
                .build());
        }
        
        return defaults;
    }
    
    /**
     * Get a unique key for an API operation.
     */
    public static String getApiKey(String httpMethod, String path) {
        return httpMethod.toUpperCase() + " " + normalizePath(path);
    }
    
    /**
     * Normalize path by replacing path parameters with placeholders.
     */
    private static String normalizePath(String path) {
        // Replace UUID-like patterns
        String normalized = path.replaceAll("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", "{id}");
        // Replace numeric IDs
        normalized = normalized.replaceAll("/\\d+(?=/|$)", "/{id}");
        return normalized;
    }
    
    /**
     * Clear the discovery cache.
     */
    public void clearCache() {
        discoveryCache.clear();
    }
    
    /**
     * Check if an API has been discovered.
     */
    public boolean hasDiscovery(String httpMethod, String path) {
        return discoveryCache.containsKey(getApiKey(httpMethod, path));
    }
    
    /**
     * Get cached discovery for an API.
     */
    public List<StatusCodeTarget> getCachedDiscovery(String httpMethod, String path) {
        String apiKey = getApiKey(httpMethod, path);
        return discoveryCache.containsKey(apiKey) ? 
            new ArrayList<>(discoveryCache.get(apiKey)) : Collections.emptyList();
    }
}
