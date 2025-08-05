package es.us.isa.restest.inputs.smart;

import es.us.isa.restest.generators.AiDrivenLLMGenerator;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.specification.OpenAPISpecification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Smart Input Fetching Service
 * Intelligently fetches realistic test data from existing APIs instead of generating random values
 */
public class SmartInputFetcher {
    
    private static final Logger log = LogManager.getLogger(SmartInputFetcher.class);
    
    private final SmartInputFetchConfig config;
    private final AiDrivenLLMGenerator llmGenerator;
    private final ObjectMapper objectMapper;
    private final Random random;
    private final OpenAPIEndpointDiscovery openAPIDiscovery;
    
    // Runtime data
    private InputFetchRegistry registry;
    private Map<String, CachedValue> cache;
    private String baseUrl;
    
    // Cache for fetched values
    private static class CachedValue {
        final String value;
        final LocalDateTime timestamp;
        
        CachedValue(String value) {
            this.value = value;
            this.timestamp = LocalDateTime.now();
        }
        
        boolean isExpired(int ttlSeconds) {
            return LocalDateTime.now().isAfter(timestamp.plusSeconds(ttlSeconds));
        }
    }
    
    public SmartInputFetcher(SmartInputFetchConfig config, String baseUrl) {
        this.config = config;
        this.baseUrl = baseUrl;
        this.llmGenerator = new AiDrivenLLMGenerator();
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
        this.cache = new ConcurrentHashMap<>();
        this.openAPIDiscovery = new OpenAPIEndpointDiscovery();
        
        loadRegistry();
        loadOpenAPISpec();
        
        log.info("SmartInputFetcher initialized with config: {}", config);
    }
    
    /**
     * Main method to fetch a smart input value for a parameter
     */
    public String fetchSmartInput(ParameterInfo parameterInfo) {
        if (!config.isEnabled()) {
            log.debug("Smart fetching disabled, using LLM for parameter '{}'", parameterInfo.getName());
            return fallbackToLLM(parameterInfo);
        }
        
        // FIXED: Decide whether to use smart fetching or LLM based on configured percentage
        double randomValue = random.nextDouble();
        if (randomValue < config.getSmartFetchPercentage()) {
            // Use smart fetching (e.g., 90% of the time if percentage = 0.9)
            log.info("🎯 Smart Fetch Decision → {} (random: {:.3f} < {:.1f}%)",
                     parameterInfo.getName(), randomValue, config.getSmartFetchPercentage() * 100);
            try {
                String result = fetchFromSmartSource(parameterInfo);
                if (result != null) {
                    log.info("Smart Fetch → {} = {} ✅", parameterInfo.getName(), result);
                    return result;
                } else {
                    log.info("Smart Fetch → {} = FAILED (no good matches found), falling back to LLM", parameterInfo.getName());
                    return fallbackToLLM(parameterInfo);
                }
            } catch (Exception e) {
                log.info("Smart Fetch → {} = ERROR ({}), falling back to LLM", parameterInfo.getName(), e.getMessage());
                return fallbackToLLM(parameterInfo);
            }
        } else {
            // Use traditional LLM generation (e.g., 10% of the time if percentage = 0.9)
            log.info("🤖 LLM Decision → {} (random: {:.3f} >= {:.1f}%)",
                     parameterInfo.getName(), randomValue, config.getSmartFetchPercentage() * 100);
            return fallbackToLLM(parameterInfo);
        }
    }
    
    /**
     * Fetch input from smart sources (existing APIs)
     */
    private String fetchFromSmartSource(ParameterInfo parameterInfo) throws Exception {
        String paramName = parameterInfo.getName();
        
        // Check cache first
        if (config.isCacheEnabled()) {
            String cacheKey = buildCacheKey(parameterInfo);
            CachedValue cached = cache.get(cacheKey);
            if (cached != null && !cached.isExpired(config.getCacheTtlSeconds())) {
                log.debug("Using cached value for parameter '{}'", paramName);
                return cached.value;
            }
        }
        
        // Look for existing mappings
        List<ApiMapping> mappings = registry.getMappingsForParameter(paramName);
        log.info("🔍 Parameter '{}' has {} existing mappings", paramName, mappings.size());

        // If no mappings found, try to discover new ones
        if (mappings.isEmpty() && config.isLlmDiscoveryEnabled()) {
            log.info("🚀 No existing mappings for '{}', attempting discovery...", paramName);
            mappings = discoverApiMappings(parameterInfo);
            log.info("🎯 Discovery for '{}' found {} new mappings", paramName, mappings.size());
        } else if (mappings.isEmpty()) {
            log.warn("❌ No mappings for '{}' and discovery is disabled", paramName);
        }
        
        // Try each mapping in order of score
        for (ApiMapping mapping : mappings.stream()
                .sorted((a, b) -> Double.compare(b.calculateScore(), a.calculateScore()))
                .limit(config.getMaxCandidates())
                .collect(Collectors.toList())) {
            
            try {
                String value = fetchFromApiMapping(mapping, parameterInfo);
                if (value != null && !value.trim().isEmpty()) {
                    // Update success rate and cache
                    mapping.updateSuccessRate(true);
                    cacheValue(parameterInfo, value);
                    
                    log.debug("Successfully fetched value '{}' for parameter '{}' from {}", 
                             value, paramName, mapping.getEndpoint());
                    return value;
                }
            } catch (Exception e) {
                log.debug("Failed to fetch from mapping {}: {}", mapping, e.getMessage());
                mapping.updateSuccessRate(false);
            }
        }
        
        // If all smart sources failed, fall back to LLM
        throw new Exception("No smart sources available for parameter: " + paramName);
    }
    
    /**
     * Discover new API mappings using LLM and pattern matching
     */
    private List<ApiMapping> discoverApiMappings(ParameterInfo parameterInfo) {
        List<ApiMapping> discoveries = new ArrayList<>();
        String paramName = parameterInfo.getName();

        try {
            log.info("🔍 Starting discovery for parameter '{}'", paramName);

            // 1. Pattern-based discovery
            List<ApiMapping> patternMappings = discoverByPatterns(parameterInfo);
            discoveries.addAll(patternMappings);
            log.info("📋 Pattern discovery for '{}' found {} mappings", paramName, patternMappings.size());

            // 2. LLM-based discovery
            if (config.isLlmDiscoveryEnabled()) {
                List<ApiMapping> llmMappings = discoverByLLM(parameterInfo);
                discoveries.addAll(llmMappings);
                log.info("🤖 LLM discovery for '{}' found {} mappings", paramName, llmMappings.size());
            } else {
                log.info("🚫 LLM discovery disabled for '{}'", paramName);
            }

            // 3. Save discovered mappings to registry
            for (ApiMapping mapping : discoveries) {
                registry.addMapping(parameterInfo.getName(), mapping);
                log.info("💾 Saved mapping for '{}': {} -> {}", paramName, mapping.getService(), mapping.getEndpoint());
            }

            if (!discoveries.isEmpty()) {
                saveRegistry(); // Persist learned mappings
                log.info("✅ Registry updated with {} new mappings for '{}'", discoveries.size(), paramName);
            } else {
                log.warn("❌ No mappings discovered for parameter '{}'", paramName);
            }

        } catch (Exception e) {
            log.warn("💥 Discovery failed for parameter '{}': {}", parameterInfo.getName(), e.getMessage(), e);
        }

        return discoveries;
    }
    
    /**
     * Discover APIs using pattern matching
     */
    private List<ApiMapping> discoverByPatterns(ParameterInfo parameterInfo) {
        List<ApiMapping> mappings = new ArrayList<>();
        String paramName = parameterInfo.getName();

        log.info("🔍 Pattern discovery for '{}' checking {} patterns", paramName, registry.getServicePatterns().size());

        for (ServicePattern pattern : registry.getServicePatterns()) {
            log.debug("🔍 Checking pattern '{}' against parameter '{}'", pattern.getPattern(), paramName);

            if (Pattern.matches(pattern.getPattern(), paramName)) {
                log.info("✅ Pattern '{}' matched parameter '{}'", pattern.getPattern(), paramName);

                for (String endpoint : pattern.getEndpoints()) {
                    for (String service : pattern.getServices()) {
                        // Create a basic mapping - could be improved with schema analysis
                        String extractPath = guessExtractPath(parameterInfo, endpoint);
                        ApiMapping mapping = new ApiMapping(endpoint, service, extractPath);
                        mapping.setPriority(config.getPatternDiscoveryPriority());
                        mappings.add(mapping);

                        log.info("📋 Created pattern mapping: '{}' -> {} {} (extractPath: {})",
                                 paramName, service, endpoint, extractPath);
                    }
                }
            } else {
                log.debug("❌ Pattern '{}' did not match parameter '{}'", pattern.getPattern(), paramName);
            }
        }

        log.info("📋 Pattern discovery for '{}' created {} mappings", paramName, mappings.size());
        return mappings;
    }
    
    /**
     * Get all available services from both registry and OpenAPI specification
     */
    private List<String> getAllAvailableServices() {
        Set<String> allServices = new HashSet<>();
        
        // Add services from registry
        allServices.addAll(registry.getAllServices());
        
        // Add services from OpenAPI specification
        if (openAPIDiscovery != null && openAPIDiscovery.isLoaded()) {
            allServices.addAll(openAPIDiscovery.getAllServices());
        }
        
        return new ArrayList<>(allServices);
    }
    
    /**
     * Discover APIs using LLM analysis
     */
    private List<ApiMapping> discoverByLLM(ParameterInfo parameterInfo) {
        List<ApiMapping> mappings = new ArrayList<>();
        
        try {
            // Get available services from both registry and OpenAPI spec
            List<String> availableServices = getAllAvailableServices();
            
            // Ask LLM which services might provide data for this parameter
            String prompt = buildLLMDiscoveryPrompt(parameterInfo, availableServices);
            List<String> suggestedServices = askLLMForServices(prompt);
            
            // Create mappings for suggested services with priority based on order
            if (suggestedServices.isEmpty()) {
                log.info("LLM indicated no good service matches for parameter '{}', skipping LLM discovery",
                        parameterInfo.getName());
            } else {
                for (int i = 0; i < suggestedServices.size(); i++) {
                    String service = suggestedServices.get(i);
                    String endpoint = inferEndpointForService(service, parameterInfo);
                    if (endpoint != null) {
                        // No need for JSONPath discovery anymore - we do direct extraction
                        ApiMapping mapping = new ApiMapping(endpoint, service, "DIRECT_EXTRACTION");

                        // Set priority based on LLM ranking (higher priority = lower number)
                        // First service gets highest priority (base priority), second gets base+1, etc.
                        int priority = config.getLlmDiscoveryPriority() + i;
                        mapping.setPriority(priority);
                        mappings.add(mapping);

                        log.info("LLM-discovered mapping #{}: {} -> {} {} (priority: {}) [DIRECT]",
                                i+1, parameterInfo.getName(), service, endpoint, priority);
                    }
                }
            }
            
        } catch (Exception e) {
            log.warn("LLM discovery failed: {}", e.getMessage());
        }
        
        return mappings;
    }
    
    /**
     * Fetch data from a specific API mapping
     */
    private String fetchFromApiMapping(ApiMapping mapping, ParameterInfo parameterInfo) throws Exception {
        String url = baseUrl + mapping.getEndpoint();
        
        log.info("🌐 API Call: {} {} for parameter '{}'", mapping.getMethod(), url, parameterInfo.getName());

        // Make HTTP request
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(mapping.getMethod());
        conn.setRequestProperty("Content-Type", config.getDefaultContentType());
        conn.setConnectTimeout((int) config.getDiscoveryTimeoutMs());
        conn.setReadTimeout((int) config.getDiscoveryTimeoutMs());

        try {
            int responseCode = conn.getResponseCode();
            if (responseCode != config.getSuccessResponseCode()) {
                log.warn("❌ API call failed with HTTP {}: {}", responseCode, url);
                throw new Exception("HTTP " + responseCode + " from " + url);
            }

            // Read response
            String responseBody = readResponse(conn);
            log.debug("API response preview: {}", responseBody.substring(0, Math.min(200, responseBody.length())));

            // Validate response quality
            if (!isValidApiResponse(responseBody, parameterInfo)) {
                log.warn("❌ Invalid API response from {}", url);
                return null;
            }

            // Use direct value extraction instead of JSONPath
            String result = extractValueDirectlyFromResponse(responseBody, parameterInfo);
            if (result != null && !result.trim().isEmpty()) {
                log.info("✅ Smart Fetch Success: {} = '{}' (from {})",
                        parameterInfo.getName(), result, mapping.getService());
                return result;
            } else {
                log.warn("❌ No valid value extracted for parameter '{}'", parameterInfo.getName());
                return null;
            }
            
        } finally {
            conn.disconnect();
        }
    }

    /**
     * Validate that API response contains useful data
     */
    private boolean isValidApiResponse(String responseBody, ParameterInfo parameterInfo) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return false;
        }

        // Check for common error responses
        String lower = responseBody.toLowerCase();
        if (lower.contains("error") || lower.contains("exception") ||
            lower.contains("not found") || lower.contains("unauthorized")) {
            return false;
        }

        // Check for empty data structures
        if (responseBody.trim().equals("{}") || responseBody.trim().equals("[]") ||
            responseBody.contains("\"data\":[]") || responseBody.contains("\"data\":{}")) {
            log.debug("API response contains empty data structure");
            return false;
        }

        // Check minimum content length (avoid trivial responses)
        if (responseBody.length() < 20) {
            log.debug("API response too short: {} chars", responseBody.length());
            return false;
        }

        return true;
    }

    /**
     * Extract value directly from API response using LLM
     */
    private String extractValueDirectlyFromResponse(String responseBody, ParameterInfo parameterInfo) {
        try {
            // Build direct extraction prompt
            String prompt = buildDirectExtractionPrompt(responseBody, parameterInfo);

            if (prompt.length() > 2044) {
                log.warn("Direct extraction prompt too long ({} chars), using fallback", prompt.length());
                return extractValueWithSimpleFallback(responseBody, parameterInfo);
            }

            // Ask LLM to extract value directly
            String llmResponse = askLLMForDirectValueExtraction(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                String cleanResponse = llmResponse.trim();

                // Check for NO_GOOD_MATCH response
                if (cleanResponse.equals("NO_GOOD_MATCH")) {
                    log.info("LLM indicated no good value match for parameter '{}'", parameterInfo.getName());
                    return extractValueWithSimpleFallback(responseBody, parameterInfo);
                }

                log.info("🧠 LLM extracted value '{}' for parameter '{}'", cleanResponse, parameterInfo.getName());
                return cleanResponse;
            }

            // Fallback if LLM fails
            return extractValueWithSimpleFallback(responseBody, parameterInfo);

        } catch (Exception e) {
            log.debug("Direct value extraction failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
            return extractValueWithSimpleFallback(responseBody, parameterInfo);
        }
    }

    /**
     * Build prompt for direct value extraction
     */
    private String buildDirectExtractionPrompt(String responseBody, ParameterInfo parameterInfo) {
        String template = registry.getLlmPrompts().get("directValueExtraction");

        // Handle message length limit for response body
        String truncatedResponse = truncateResponseSchemaForLLM(responseBody, template, parameterInfo);

        String paramName = parameterInfo.getName() != null ? parameterInfo.getName() : "";
        String paramType = parameterInfo.getType() != null ? parameterInfo.getType() : "";
        String paramDesc = parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "";

        return template
                .replace("{responseSchema}", truncatedResponse != null ? truncatedResponse : "")
                .replace("{parameterName}", paramName)
                .replace("{parameterType}", paramType)
                .replace("{parameterDescription}", paramDesc)
                // Handle multiple parameter name references
                .replaceAll("\\{parameterName\\}", paramName);
    }

    /**
     * Ask LLM for direct value extraction
     */
    private String askLLMForDirectValueExtraction(String prompt) {
        try {
            // Call LLM for direct value extraction
            String rawResponse = callLLMForExtractionPathDiscovery(prompt);

            if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                String cleaned = cleanJsonFromMarkdown(rawResponse);

                // Check for NO_GOOD_MATCH response first
                if (cleaned.trim().equals("NO_GOOD_MATCH")) {
                    return "NO_GOOD_MATCH";
                }

                // Remove quotes if the LLM wrapped the value in quotes
                if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
                    cleaned = cleaned.substring(1, cleaned.length() - 1);
                }

                return cleaned.trim();
            }

            return null;
        } catch (Exception e) {
            log.debug("LLM direct value extraction failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Simple fallback value extraction when LLM fails
     */
    private String extractValueWithSimpleFallback(String responseBody, ParameterInfo parameterInfo) {
        try {
            // Parse JSON and try to find any reasonable value
            Object parsed = objectMapper.readValue(responseBody, Object.class);

            if (parsed instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) parsed;

                // Look for data array
                if (map.containsKey("data") && map.get("data") instanceof List) {
                    List<?> dataList = (List<?>) map.get("data");
                    if (!dataList.isEmpty() && dataList.get(0) instanceof Map) {
                        Map<?, ?> firstItem = (Map<?, ?>) dataList.get(0);

                        // Try to find a reasonable field based on parameter name
                        String paramName = parameterInfo.getName() != null ? parameterInfo.getName().toLowerCase() : "";

                        // Direct field name match
                        for (Map.Entry<?, ?> entry : firstItem.entrySet()) {
                            String fieldName = entry.getKey().toString().toLowerCase();
                            if (fieldName.contains(paramName) || paramName.contains(fieldName)) {
                                Object value = entry.getValue();
                                if (value != null) {
                                    log.info("📋 Fallback extracted '{}' from field '{}' for parameter '{}'",
                                            value, entry.getKey(), parameterInfo.getName());
                                    return value.toString();
                                }
                            }
                        }

                        // Semantic matching fallback
                        String fallbackValue = findSemanticMatch(firstItem, paramName);
                        if (fallbackValue != null) {
                            log.info("📋 Fallback found semantic match '{}' for parameter '{}'",
                                    fallbackValue, parameterInfo.getName());
                            return fallbackValue;
                        }

                        // Last resort: return first non-null string value
                        for (Map.Entry<?, ?> entry : firstItem.entrySet()) {
                            Object value = entry.getValue();
                            if (value != null && value instanceof String && !value.toString().trim().isEmpty()) {
                                log.info("📋 Fallback using first string value '{}' for parameter '{}'",
                                        value, parameterInfo.getName());
                                return value.toString();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.debug("Fallback extraction failed: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Find semantic matches for parameter names
     */
    private String findSemanticMatch(Map<?, ?> data, String paramName) {
        // Distance-related
        if (paramName.contains("distance")) {
            for (String field : new String[]{"price", "from", "to", "trainNumber"}) {
                Object value = data.get(field);
                if (value != null) return value.toString();
            }
        }

        // Station-related
        if (paramName.contains("station")) {
            for (String field : new String[]{"from", "to", "contactsName"}) {
                Object value = data.get(field);
                if (value != null) return value.toString();
            }
        }

        // ID-related
        if (paramName.contains("id")) {
            for (String field : new String[]{"id", "accountId", "trainNumber"}) {
                Object value = data.get(field);
                if (value != null) return value.toString();
            }
        }

        // Name-related
        if (paramName.contains("name")) {
            for (String field : new String[]{"contactsName", "trainNumber"}) {
                Object value = data.get(field);
                if (value != null) return value.toString();
            }
        }

        return null;
    }

    /**
     * Extract value from API response using JSONPath (legacy method, kept for compatibility)
     */
    private String extractValueFromResponse(String responseBody, String extractPath, ParameterInfo parameterInfo) {
        try {
            Object result = JsonPath.read(responseBody, extractPath);
            
            if (result != null) {
                // Use LLM to intelligently select the most appropriate value
                String selectedValue = selectValueWithLLM(result, parameterInfo);
                if (selectedValue != null) {
                    return selectedValue;
                }
                
                // Fallback: use intelligent selection logic if LLM fails
                return selectValueWithFallbackLogic(result, parameterInfo);
            }
        } catch (PathNotFoundException e) {
            log.debug("JSONPath '{}' not found in response for parameter '{}'",
                     extractPath, parameterInfo.getName());
        } catch (Exception e) {
            log.debug("Failed to extract value using JSONPath '{}' for parameter '{}': {}",
                     extractPath, parameterInfo.getName(), e.getMessage());
        }
        return null;
    }

    /**
     * Smart fallback value selection when LLM fails
     */
    private String selectValueWithFallbackLogic(Object result, ParameterInfo parameterInfo) {
        if (result == null) return null;

        String paramName = parameterInfo.getName() != null ? parameterInfo.getName().toLowerCase() : "";
        String paramType = parameterInfo.getType() != null ? parameterInfo.getType().toLowerCase() : "";

        if (result instanceof List) {
            List<?> list = (List<?>) result;
            if (list.isEmpty()) return null;

            // Smart selection based on parameter characteristics
            if (paramName.contains("list") || paramName.contains("array")) {
                // For list parameters, return a representative value (not first, which might be 0)
                if (list.size() > 1) {
                    Object value = list.get(1); // Second element often more representative
                    return value != null ? value.toString() : null;
                }
            }

            if (paramName.contains("id") || paramName.contains("identifier")) {
                // For ID parameters, prefer non-empty, non-zero values
                for (Object item : list) {
                    if (item != null) {
                        String str = item.toString();
                        if (!str.isEmpty() && !str.equals("0") && !str.equals("null")) {
                            return str;
                        }
                    }
                }
            }

            if (paramName.contains("name") || paramName.contains("title")) {
                // For name parameters, prefer non-empty strings
                for (Object item : list) {
                    if (item != null) {
                        String str = item.toString();
                        if (!str.isEmpty() && !str.matches("\\d+")) { // Not just numbers
                            return str;
                        }
                    }
                }
            }

            // Default: take first non-null element
            for (Object item : list) {
                if (item != null) {
                    return item.toString();
                }
            }
        } else {
            return result.toString();
        }

        return null;
    }
    
    private String selectValueWithLLM(Object extractedData, ParameterInfo parameterInfo) {
        try {
            // Convert extracted data to a readable format for LLM
            String dataString = objectMapper.writeValueAsString(extractedData);
            
            // Build prompt for value selection
            String prompt = buildValueSelectionPrompt(dataString, parameterInfo);
            String llmResponse = askLLMForValueSelection(prompt);
            
            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                String cleanResponse = llmResponse.trim();

                // Check for NO_GOOD_MATCH response
                if (cleanResponse.equals("NO_GOOD_MATCH")) {
                    log.info("LLM indicated no good value match for parameter '{}'", parameterInfo.getName());
                    return null; // Will trigger fallback to traditional LLM generation
                }

                log.debug("LLM selected value '{}' for parameter '{}'", cleanResponse, parameterInfo.getName());
                return cleanResponse;
            }
        } catch (Exception e) {
            log.debug("LLM value selection failed for parameter '{}': {}", 
                     parameterInfo.getName(), e.getMessage());
        }
        return null;
    }
    
    private String buildValueSelectionPrompt(String extractedData, ParameterInfo parameterInfo) {
        String template = registry.getLlmPrompts().get("valueSelection");

        // Handle message length limit (2044 chars for GPT4All)
        String truncatedData = truncateDataForLLM(extractedData, template, parameterInfo);

        return template
                .replace("{extractedData}", truncatedData != null ? truncatedData : "")
                .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                .replace("{parameterDescription}", parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "");
    }

    /**
     * Truncate extracted data to fit within LLM message length limits
     */
    private String truncateDataForLLM(String extractedData, String template, ParameterInfo parameterInfo) {
        if (extractedData == null) return "";

        // Calculate space available for data (leave 200 chars buffer for template + parameters)
        String tempPrompt = template
                .replace("{extractedData}", "")
                .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                .replace("{parameterDescription}", parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "");

        int maxDataLength = 1844 - tempPrompt.length(); // 2044 - 200 buffer

        if (extractedData.length() <= maxDataLength) {
            return extractedData;
        }

        // Try to truncate intelligently
        try {
            // If it's JSON, try to keep complete objects/arrays
            if (extractedData.trim().startsWith("{") || extractedData.trim().startsWith("[")) {
                return truncateJsonIntelligently(extractedData, maxDataLength);
            } else {
                // Simple truncation for non-JSON data
                return extractedData.substring(0, maxDataLength) + "...";
            }
        } catch (Exception e) {
            // Fallback to simple truncation
            return extractedData.substring(0, Math.min(maxDataLength, extractedData.length())) + "...";
        }
    }

    /**
     * Intelligently truncate JSON to keep complete objects/arrays when possible
     */
    private String truncateJsonIntelligently(String jsonData, int maxLength) {
        if (jsonData.length() <= maxLength) return jsonData;

        try {
            // Parse JSON to understand structure
            Object parsed = objectMapper.readValue(jsonData, Object.class);

            if (parsed instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) parsed;
                // For response objects, try to keep the data array with fewer items
                if (map.containsKey("data") && map.get("data") instanceof List) {
                    List<?> dataList = (List<?>) map.get("data");

                    // Keep only first 1-2 items from data array to show structure
                    List<Object> truncatedData = new ArrayList<>();
                    for (int i = 0; i < Math.min(2, dataList.size()); i++) {
                        truncatedData.add(dataList.get(i));
                    }

                    // Rebuild response with truncated data
                    Map<String, Object> truncatedResponse = new HashMap<>();
                    truncatedResponse.put("status", map.get("status"));
                    truncatedResponse.put("msg", "truncated");
                    truncatedResponse.put("data", truncatedData);

                    String result = objectMapper.writeValueAsString(truncatedResponse);
                    if (result.length() <= maxLength) {
                        return result;
                    }

                    // If still too long, keep only 1 item
                    truncatedData = truncatedData.subList(0, Math.min(1, truncatedData.size()));
                    truncatedResponse.put("data", truncatedData);
                    return objectMapper.writeValueAsString(truncatedResponse);
                }
            }

            // Fallback to simple truncation
            return jsonData.substring(0, Math.min(maxLength - 3, jsonData.length())) + "...";

        } catch (Exception e) {
            // Fallback to simple truncation
            return jsonData.substring(0, Math.min(maxLength - 3, jsonData.length())) + "...";
        }
    }
    
    private String askLLMForValueSelection(String prompt) {
        try {
            // Call LLM directly for value selection, not parameter generation
            String rawResponse = callLLMForValueSelection(prompt);
            
            if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                // Clean any markdown formatting
                String cleaned = cleanJsonFromMarkdown(rawResponse);

                // Check for NO_GOOD_MATCH response first
                if (cleaned.trim().equals("NO_GOOD_MATCH")) {
                    return "NO_GOOD_MATCH";
                }

                // Remove quotes if the LLM wrapped the value in quotes
                if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
                    cleaned = cleaned.substring(1, cleaned.length() - 1);
                }
                return cleaned.trim();
            }
            
            return null;
            
        } catch (Exception e) {
            log.warn("Failed to ask LLM for value selection: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Call LLM specifically for value selection (not parameter generation)
     */
    private String callLLMForValueSelection(String prompt) {
        try {
            final String LOCAL_LLM_API_URL = "http://localhost:4891/v1/chat/completions";
            
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("max_tokens", 100); // Short response for value selection
            requestBody.put("temperature", 0.1); // Low temperature for consistent selection
            
            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are an AI assistant that selects the most appropriate value from given data. Respond with only the selected value, no explanations.");
            messages.put(systemMessage);
            
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);
            
            requestBody.put("messages", messages);
            
            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                    .url(LOCAL_LLM_API_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    
                    if (jsonResponse.has("choices")) {
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        if (choices.length() > 0) {
                            JSONObject firstChoice = choices.getJSONObject(0);
                            if (firstChoice.has("message")) {
                                JSONObject message = firstChoice.getJSONObject("message");
                                if (message.has("content")) {
                                    return message.getString("content").trim();
                                }
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            log.warn("Failed to call LLM for value selection: {}", e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Fallback to traditional LLM generation
     */
    private String fallbackToLLM(ParameterInfo parameterInfo) {
        try {
            List<String> values = llmGenerator.generateParameterValues(parameterInfo);
            String result = values.isEmpty() ? "DEFAULT_" + parameterInfo.getName() : values.get(0);

            // Clean any malformed LLM output (e.g., ```json)
            if (result != null) {
                result = cleanJsonFromMarkdown(result);
                // If the result is still malformed or empty after cleaning, use a default
                if (result.trim().isEmpty() || result.equals("json") || result.startsWith("```")) {
                    result = "DEFAULT_" + parameterInfo.getName();
                    log.warn("LLM returned malformed output, using default for parameter '{}'", parameterInfo.getName());
                }
            }

            log.info("LLM (Fallback) → {} = {}", parameterInfo.getName(), result);
            return result;
        } catch (Exception e) {
            log.warn("LLM fallback failed for '{}': {}", parameterInfo.getName(), e.getMessage());
            String fallback = "FALLBACK_" + parameterInfo.getName();
            log.info("Default Fallback → {} = {}", parameterInfo.getName(), fallback);
            return fallback;
        }
    }
    
    /**
     * Helper methods
     */
    
    private void loadRegistry() {
        try {
            File registryFile = new File(config.getRegistryPath());
            if (registryFile.exists()) {
                registry = InputFetchRegistry.loadFromFile(registryFile);
                log.info("Loaded input fetch registry from: {}", config.getRegistryPath());
            } else {
                registry = new InputFetchRegistry();
                log.info("Created new input fetch registry");
            }
        } catch (Exception e) {
            log.warn("Failed to load registry, creating new one: {}", e.getMessage());
            registry = new InputFetchRegistry();
        }
    }
    
    /**
     * Load OpenAPI specification for endpoint discovery
     */
    private void loadOpenAPISpec() {
        try {
            String openApiPath = config.getOpenApiSpecPath();
            if (openApiPath != null && !openApiPath.trim().isEmpty()) {
                File openApiFile = new File(openApiPath);
                
                if (openApiFile.exists()) {
                    openAPIDiscovery.loadFromFile(openApiPath);
                    log.info("Loaded OpenAPI specification from: {}", openApiPath);
                    log.info("Available services: {}", openAPIDiscovery.getAllServices().size());
                } else {
                    log.warn("OpenAPI specification file not found: {}", openApiPath);
                    log.info("Will fall back to LLM endpoint guessing");
                }
            } else {
                log.info("No OpenAPI specification path configured, will use LLM endpoint guessing");
            }
        } catch (Exception e) {
            log.error("Failed to load OpenAPI specification: {}", e.getMessage());
            log.info("Will fall back to LLM endpoint guessing");
        }
    }
    
    private void saveRegistry() {
        try {
            File registryFile = new File(config.getRegistryPath());
            registryFile.getParentFile().mkdirs();
            registry.saveToFile(registryFile);
            log.debug("Saved input fetch registry");
        } catch (Exception e) {
            log.warn("Failed to save registry: {}", e.getMessage());
        }
    }
    
    private void cacheValue(ParameterInfo parameterInfo, String value) {
        if (config.isCacheEnabled()) {
            String cacheKey = buildCacheKey(parameterInfo);
            cache.put(cacheKey, new CachedValue(value));
        }
    }
    
    private String buildCacheKey(ParameterInfo parameterInfo) {
        return parameterInfo.getName() + ":" + parameterInfo.getType() + ":" + parameterInfo.getInLocation();
    }
    
    private String readResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
    
    private String guessExtractPath(ParameterInfo parameterInfo, String endpoint) {
        try {
            // First, try to get the actual API response schema by making a sample request
            String responseSchema = getApiResponseSchema(endpoint);

            if (responseSchema != null) {
                // Build prompt and check length before sending to LLM
                String prompt = buildDataExtractionPrompt(parameterInfo, responseSchema);

                if (prompt.length() <= 2044) {
                    // Use LLM to determine the best JSONPath for extraction
                    String llmResponse = askLLMForExtractionPath(prompt);

                    if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                        String cleanPath = llmResponse.trim();
                        // Remove any markdown formatting
                        cleanPath = cleanJsonFromMarkdown(cleanPath);

                        // Check for NO_GOOD_MATCH response
                        if (cleanPath.equals("NO_GOOD_MATCH")) {
                            log.info("LLM indicated no good JSONPath match for parameter '{}'", parameterInfo.getName());
                            return null; // Will trigger fallback
                        }

                        // Validate JSONPath before using it
                        if (isValidJsonPath(cleanPath, responseSchema)) {
                            log.info("✅ LLM suggested valid JSONPath '{}' for parameter '{}'", cleanPath, parameterInfo.getName());
                            return cleanPath;
                        } else {
                            log.warn("❌ LLM suggested invalid JSONPath '{}' for parameter '{}', using fallback",
                                    cleanPath, parameterInfo.getName());
                            return null; // Will trigger pattern-based fallback
                        }
                    }
                } else {
                    log.warn("Prompt too long ({} chars) for LLM, using pattern-based fallback for parameter '{}'",
                            prompt.length(), parameterInfo.getName());
                }
            }
        } catch (Exception e) {
            log.debug("LLM extraction path discovery failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
        }

        // Fallback: use pattern-based guessing
        String fallbackPath = guessPathByParameterName(parameterInfo.getName());
        log.info("Using pattern-based JSONPath '{}' for parameter '{}'", fallbackPath, parameterInfo.getName());
        return fallbackPath;
    }

    /**
     * Guess JSONPath based on parameter name patterns
     */
    private String guessPathByParameterName(String paramName) {
        if (paramName == null) return "$.data[*]";

        String lowerName = paramName.toLowerCase();

        // Distance-related parameters
        if (lowerName.contains("distance")) {
            return "$.data[*].route.distances[*]";
        }

        // Station-related parameters
        if (lowerName.contains("station")) {
            if (lowerName.contains("start") || lowerName.contains("from")) {
                return "$.data[*].route.startStation";
            } else if (lowerName.contains("end") || lowerName.contains("to") || lowerName.contains("terminal")) {
                return "$.data[*].route.endStation";
            } else {
                return "$.data[*].route.stations[*]";
            }
        }

        // ID-related parameters
        if (lowerName.contains("id")) {
            if (lowerName.contains("trip")) {
                return "$.data[*].trip.id";
            } else if (lowerName.contains("route")) {
                return "$.data[*].route.id";
            } else {
                return "$.data[*].id";
            }
        }

        // Name-related parameters
        if (lowerName.contains("name")) {
            if (lowerName.contains("train")) {
                return "$.data[*].trip.trainTypeName";
            } else {
                return "$.data[*].name";
            }
        }

        // Time-related parameters
        if (lowerName.contains("time")) {
            if (lowerName.contains("start")) {
                return "$.data[*].trip.startTime";
            } else if (lowerName.contains("end")) {
                return "$.data[*].trip.endTime";
            } else {
                return "$.data[*].time";
            }
        }

        // Price-related parameters
        if (lowerName.contains("price") || lowerName.contains("cost")) {
            return "$.data[*].price";
        }

        // Default fallback
        return "$.data[*]." + paramName;
    }

    /**
     * Validate that a JSONPath expression works with the given response
     */
    private boolean isValidJsonPath(String jsonPath, String responseBody) {
        if (jsonPath == null || jsonPath.trim().isEmpty()) {
            return false;
        }

        try {
            // Test the JSONPath against the response
            Object result = JsonPath.read(responseBody, jsonPath);

            // Check if result is meaningful
            if (result == null) {
                return false;
            }

            if (result instanceof List) {
                List<?> list = (List<?>) result;
                return !list.isEmpty(); // List should have at least one element
            }

            if (result instanceof String) {
                return !((String) result).trim().isEmpty(); // String should not be empty
            }

            return true; // Other types (numbers, objects) are generally valid

        } catch (Exception e) {
            log.debug("JSONPath validation failed for '{}': {}", jsonPath, e.getMessage());
            return false;
        }
    }

    private String getApiResponseSchema(String endpoint) {
        try {
            // Make a sample request to get the response structure
            String url = baseUrl + endpoint;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", config.getDefaultContentType());
            conn.setConnectTimeout((int) config.getSchemaDiscoveryTimeoutMs());
            conn.setReadTimeout((int) config.getSchemaDiscoveryTimeoutMs());
            
            int responseCode = conn.getResponseCode();
            if (responseCode == config.getSuccessResponseCode()) {
                String responseBody = readResponse(conn);
                log.debug("Got sample response from {}: {}", url, responseBody.substring(0, Math.min(200, responseBody.length())));
                return responseBody;
            } else {
                log.debug("Schema discovery request failed with HTTP {}: {}", responseCode, url);
            }
        } catch (Exception e) {
            log.debug("Failed to get API response schema from {}: {}", endpoint, e.getMessage());
        }
        return null;
    }
    
    private String buildDataExtractionPrompt(ParameterInfo parameterInfo, String responseSchema) {
        String template = registry.getLlmPrompts().get("dataExtraction");

        // Handle message length limit for response schema
        String truncatedSchema = truncateResponseSchemaForLLM(responseSchema, template, parameterInfo);

        String paramName = parameterInfo.getName() != null ? parameterInfo.getName() : "";
        String paramType = parameterInfo.getType() != null ? parameterInfo.getType() : "";
        String paramDesc = parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "";

        return template
                .replace("{responseSchema}", truncatedSchema != null ? truncatedSchema : "")
                .replace("{parameterName}", paramName)
                .replace("{parameterType}", paramType)
                .replace("{parameterDescription}", paramDesc)
                // Handle multiple parameter name references in the new template
                .replaceAll("\\{parameterName\\}", paramName);
    }

    /**
     * Truncate response schema to fit within LLM message length limits
     */
    private String truncateResponseSchemaForLLM(String responseSchema, String template, ParameterInfo parameterInfo) {
        if (responseSchema == null) return "";

        // Calculate space available for schema (leave 100 chars buffer)
        String tempPrompt = template
                .replace("{responseSchema}", "")
                .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                .replace("{parameterDescription}", parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "");

        int maxSchemaLength = 1500 - tempPrompt.length(); // Much more aggressive limit

        if (responseSchema.length() <= maxSchemaLength) {
            return responseSchema;
        }

        log.info("Response schema too long ({} chars), truncating to {} chars for parameter '{}'",
                responseSchema.length(), maxSchemaLength, parameterInfo.getName());

        // Try to truncate intelligently
        try {
            // If it's JSON, try to keep complete objects/arrays
            if (responseSchema.trim().startsWith("{") || responseSchema.trim().startsWith("[")) {
                return truncateJsonIntelligently(responseSchema, maxSchemaLength);
            } else {
                // Simple truncation for non-JSON data
                return responseSchema.substring(0, maxSchemaLength) + "...";
            }
        } catch (Exception e) {
            // Fallback to simple truncation
            return responseSchema.substring(0, Math.min(maxSchemaLength, responseSchema.length())) + "...";
        }
    }
    
    private String askLLMForExtractionPath(String prompt) {
        try {
            // Call LLM directly for extraction path discovery with appropriate system prompt
            String rawResponse = callLLMForExtractionPathDiscovery(prompt);

            if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                // Clean any markdown formatting and return the JSONPath
                String cleaned = cleanJsonFromMarkdown(rawResponse);

                // Check for NO_GOOD_MATCH response first
                if (cleaned.trim().equals("NO_GOOD_MATCH")) {
                    return "NO_GOOD_MATCH";
                }

                // Remove quotes if the LLM wrapped the path in quotes
                if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
                    cleaned = cleaned.substring(1, cleaned.length() - 1);
                }
                return cleaned;
            }

            return null;
            
        } catch (Exception e) {
            log.warn("Failed to ask LLM for extraction path: {}", e.getMessage());
            return null;
        }
    }
    
    private String buildLLMDiscoveryPrompt(ParameterInfo parameterInfo, List<String> availableServices) {
        String template = registry.getLlmPrompts().get("apiDiscovery");
        
        // Limit services list to prevent message length issues (max 2044 chars)
        String servicesString = String.join(", ", availableServices);
        
        // First replace all parameters except availableServices
        String basePrompt = template
                .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                .replace("{parameterDescription}", parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "")
                .replace("{parameterLocation}", parameterInfo.getInLocation() != null ? parameterInfo.getInLocation() : "");
        
        // Calculate remaining space for services (leave 100 chars buffer)
        // Use a temporary replacement to calculate space needed
        String tempPrompt = basePrompt.replace("{availableServices}", "");
        int maxServicesLength = 1944 - tempPrompt.length();
        
        if (servicesString.length() > maxServicesLength) {
            // Truncate services list to fit within limit
            List<String> truncatedServices = new ArrayList<>();
            int currentLength = 0;
            
            for (String service : availableServices) {
                int serviceLength = service.length() + 2; // +2 for ", "
                if (currentLength + serviceLength > maxServicesLength) {
                    break;
                }
                truncatedServices.add(service);
                currentLength += serviceLength;
            }
            
            servicesString = String.join(", ", truncatedServices);
            if (truncatedServices.size() < availableServices.size()) {
                servicesString += " (and " + (availableServices.size() - truncatedServices.size()) + " more)";
            }
        }
        
        // Finally replace the availableServices placeholder
        return basePrompt.replace("{availableServices}", servicesString);
    }
    
    private List<String> askLLMForServices(String prompt) {
        try {
            // Call LLM directly for service discovery with appropriate system prompt
            String rawResponse = callLLMForServiceDiscovery(prompt);
            
            if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                try {
                    // FIXED: Clean markdown code blocks from LLM response
                    String cleanedResponse = cleanJsonFromMarkdown(rawResponse);
                    
                    JsonNode jsonResponse = objectMapper.readTree(cleanedResponse);
                    // Check for NO_GOOD_MATCH response
                    if (cleanedResponse.trim().equals("NO_GOOD_MATCH")) {
                        log.info("LLM indicated no good service matches for this parameter");
                        return new ArrayList<>();
                    }

                    if (jsonResponse.isArray()) {
                        List<String> services = new ArrayList<>();
                        jsonResponse.forEach(node -> {
                            String serviceName = node.asText().trim();
                            if (!serviceName.isEmpty()) {
                                services.add(serviceName);
                            }
                        });

                        // Validate that we got reasonable results
                        if (services.size() > 0 && services.size() <= 5) {
                            log.info("LLM suggested {} services: {}", services.size(), services);
                            return services;
                        } else {
                            log.warn("LLM returned {} services (expected 1-5): {}", services.size(), services);
                            // Return first 3 if too many, or all if too few
                            return services.subList(0, Math.min(3, services.size()));
                        }
                    } else {
                        log.debug("LLM response is not a JSON array: {}", cleanedResponse);
                    }
                } catch (Exception e) {
                    log.debug("Failed to parse LLM response as JSON: {} (raw: '{}')", e.getMessage(), rawResponse);
                }
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            log.warn("Failed to ask LLM for services: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Call LLM directly for service discovery with appropriate system prompt
     */
    private String callLLMForServiceDiscovery(String prompt) {
        final String LOCAL_LLM_API_URL = "http://localhost:4891/v1/chat/completions";
        
        String systemContent = 
                "You are an API testing assistant that helps identify which microservices " +
                "would most likely provide realistic data for given parameters. " +
                "Respond with a JSON array of service names in priority order. " +
                "Do NOT generate test values. Only return service names as a JSON array.";
        
        try {
            // Build the request using the same HTTP client setup as ZeroShotLLMGenerator
            org.json.JSONArray messages = new org.json.JSONArray()
                    .put(new org.json.JSONObject().put("role", "system").put("content", systemContent))
                    .put(new org.json.JSONObject().put("role", "user").put("content", prompt));
            
            org.json.JSONObject requestBody = new org.json.JSONObject()
                    .put("model", "llama-3-8b-instruct")
                    .put("messages", messages)
                    .put("max_tokens", 200)
                    .put("temperature", 0.7);
            
            log.debug("[Service Discovery LLM] Sending request to: {}", LOCAL_LLM_API_URL);
            log.debug("[Service Discovery LLM] Request body: {}", requestBody.toString());
            
            // Use the same HTTP client configuration as ZeroShotLLMGenerator
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                    .build();
            
            okhttp3.RequestBody body = okhttp3.RequestBody.create(
                    requestBody.toString(),
                    okhttp3.MediaType.parse("application/json")
            );
            
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(LOCAL_LLM_API_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    log.debug("[Service Discovery LLM] Response: {}", responseBody);
                    
                    // Parse the response to extract the content
                    org.json.JSONObject jsonResponse = new org.json.JSONObject(responseBody);
                    if (jsonResponse.has("choices")) {
                        org.json.JSONArray choices = jsonResponse.getJSONArray("choices");
                        if (choices.length() > 0) {
                            org.json.JSONObject firstChoice = choices.getJSONObject(0);
                            if (firstChoice.has("message")) {
                                org.json.JSONObject message = firstChoice.getJSONObject("message");
                                if (message.has("content")) {
                                    return message.getString("content").trim();
                                }
                            }
                        }
                    }
                } else {
                    log.warn("[Service Discovery LLM] HTTP error: {} - {}", response.code(), response.message());
                }
            }
        } catch (Exception e) {
            log.warn("[Service Discovery LLM] Failed to call LLM: {}", e.getMessage());
        }
        
        return null;
    }
    
    /**
      * Call LLM directly for endpoint discovery with appropriate system prompt
      */
     private String callLLMForEndpointDiscovery(String prompt) {
         final String LOCAL_LLM_API_URL = "http://localhost:4891/v1/chat/completions";
         
         String systemContent = 
                 "You are an API testing assistant that helps identify REST API endpoints " +
                 "within microservices that would provide data for given parameters. " +
                 "Respond with the most likely endpoint path (e.g., /api/v1/service/resource). " +
                 "Do NOT generate test values. Only return the endpoint path.";
         
         try {
             // Build the request using the same HTTP client setup as ZeroShotLLMGenerator
             org.json.JSONArray messages = new org.json.JSONArray()
                     .put(new org.json.JSONObject().put("role", "system").put("content", systemContent))
                     .put(new org.json.JSONObject().put("role", "user").put("content", prompt));
             
             org.json.JSONObject requestBody = new org.json.JSONObject()
                     .put("model", "llama-3-8b-instruct")
                     .put("messages", messages)
                     .put("max_tokens", 100)
                     .put("temperature", 0.3);
             
             log.debug("[Endpoint Discovery LLM] Sending request to: {}", LOCAL_LLM_API_URL);
             log.debug("[Endpoint Discovery LLM] Request body: {}", requestBody.toString());
             
             // Use the same HTTP client configuration as ZeroShotLLMGenerator
             okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                     .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                     .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                     .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                     .build();
             
             okhttp3.RequestBody body = okhttp3.RequestBody.create(
                     requestBody.toString(),
                     okhttp3.MediaType.parse("application/json")
             );
             
             okhttp3.Request request = new okhttp3.Request.Builder()
                     .url(LOCAL_LLM_API_URL)
                     .post(body)
                     .addHeader("Content-Type", "application/json")
                     .build();
             
             try (okhttp3.Response response = client.newCall(request).execute()) {
                 if (response.isSuccessful() && response.body() != null) {
                     String responseBody = response.body().string();
                     log.debug("[Endpoint Discovery LLM] Response: {}", responseBody);
                     
                     // Parse the response to extract the content
                     org.json.JSONObject jsonResponse = new org.json.JSONObject(responseBody);
                     if (jsonResponse.has("choices")) {
                         org.json.JSONArray choices = jsonResponse.getJSONArray("choices");
                         if (choices.length() > 0) {
                             org.json.JSONObject firstChoice = choices.getJSONObject(0);
                             if (firstChoice.has("message")) {
                                 org.json.JSONObject message = firstChoice.getJSONObject("message");
                                 if (message.has("content")) {
                                     return message.getString("content").trim();
                                 }
                             }
                         }
                     }
                 } else {
                     log.warn("[Endpoint Discovery LLM] HTTP error: {} - {}", response.code(), response.message());
                 }
             }
         } catch (Exception e) {
             log.warn("[Endpoint Discovery LLM] Failed to call LLM: {}", e.getMessage());
         }
         
         return null;
     }
     
     /**
       * Call LLM directly for extraction path discovery with appropriate system prompt
       */
      private String callLLMForExtractionPathDiscovery(String prompt) {
          final String LOCAL_LLM_API_URL = "http://localhost:4891/v1/chat/completions";
          
          String systemContent = 
                  "You are an API testing assistant that helps create JSONPath expressions " +
                  "to extract specific data from JSON API responses. " +
                  "Respond with a valid JSONPath expression (e.g., $.data[*].name or $.items[0].id). " +
                  "Do NOT generate test values. Only return the JSONPath expression.";
          
          try {
              // Build the request using the same HTTP client setup as ZeroShotLLMGenerator
              org.json.JSONArray messages = new org.json.JSONArray()
                      .put(new org.json.JSONObject().put("role", "system").put("content", systemContent))
                      .put(new org.json.JSONObject().put("role", "user").put("content", prompt));
              
              org.json.JSONObject requestBody = new org.json.JSONObject()
                      .put("model", "llama-3-8b-instruct")
                      .put("messages", messages)
                      .put("max_tokens", 100)
                      .put("temperature", 0.3);
              
              log.debug("[Extraction Path Discovery LLM] Sending request to: {}", LOCAL_LLM_API_URL);
              log.debug("[Extraction Path Discovery LLM] Request body: {}", requestBody.toString());
              
              // Use the same HTTP client configuration as ZeroShotLLMGenerator
              okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                      .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                      .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                      .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                      .build();
              
              okhttp3.RequestBody body = okhttp3.RequestBody.create(
                      requestBody.toString(),
                      okhttp3.MediaType.parse("application/json")
              );
              
              okhttp3.Request request = new okhttp3.Request.Builder()
                      .url(LOCAL_LLM_API_URL)
                      .post(body)
                      .addHeader("Content-Type", "application/json")
                      .build();
              
              try (okhttp3.Response response = client.newCall(request).execute()) {
                  if (response.isSuccessful() && response.body() != null) {
                      String responseBody = response.body().string();
                      log.debug("[Extraction Path Discovery LLM] Response: {}", responseBody);
                      
                      // Parse the response to extract the content
                      org.json.JSONObject jsonResponse = new org.json.JSONObject(responseBody);
                      if (jsonResponse.has("choices")) {
                          org.json.JSONArray choices = jsonResponse.getJSONArray("choices");
                          if (choices.length() > 0) {
                              org.json.JSONObject firstChoice = choices.getJSONObject(0);
                              if (firstChoice.has("message")) {
                                  org.json.JSONObject message = firstChoice.getJSONObject("message");
                                  if (message.has("content")) {
                                      return message.getString("content").trim();
                                  }
                              }
                          }
                      }
                  } else {
                      log.warn("[Extraction Path Discovery LLM] HTTP error: {} - {}", response.code(), response.message());
                  }
              }
          } catch (Exception e) {
              log.warn("[Extraction Path Discovery LLM] Failed to call LLM: {}", e.getMessage());
          }
          
          return null;
      }
      
      /**
       * Clean JSON response from markdown code blocks
       * Handles responses like: ```json\n["service1", "service2"]\n```
     */
    private String cleanJsonFromMarkdown(String response) {
        if (response == null) {
            return "";
        }
        
        // Remove markdown code blocks
        String cleaned = response.trim();
        
        // Remove ```json at the beginning
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7).trim();
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3).trim();
        }
        
        // Remove ``` at the end
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
        }
        
        return cleaned;
    }
    
    private String inferEndpointForService(String service, ParameterInfo parameterInfo) {
        // First try LLM-based endpoint selection if enabled
        if (config.isLlmEndpointSelectionEnabled()) {
            if (openAPIDiscovery != null && openAPIDiscovery.isLoaded()) {
                List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints = openAPIDiscovery.getEndpointsForService(service);
                if (!endpoints.isEmpty()) {
                    // Use LLM to select the best endpoint
                    String selectedEndpoint = selectEndpointWithLLM(endpoints, parameterInfo, service);
                    if (selectedEndpoint != null) {
                        log.info("🧠 LLM selected endpoint '{}' for parameter '{}' in service '{}'",
                                selectedEndpoint, parameterInfo.getName(), service);
                        return selectedEndpoint;
                    }
                }
            }
        }

        // Fallback to scoring-based selection
        if (openAPIDiscovery != null && openAPIDiscovery.isLoaded()) {
            List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints = openAPIDiscovery.getEndpointsForService(service);
            if (!endpoints.isEmpty()) {
                // Apply intelligent selection logic based on parameter
                String selectedEndpoint = selectBestEndpointForParameter(endpoints, parameterInfo);
                log.info("📊 Scoring selected endpoint '{}' for parameter '{}' in service '{}'",
                        selectedEndpoint, parameterInfo.getName(), service);
                return selectedEndpoint;
            }
        }
        
        // Fallback to LLM discovery if OpenAPI doesn't have the service or selection failed
        try {
            // Get available endpoints for context if possible
            List<String> availableEndpoints = new ArrayList<>();
            if (openAPIDiscovery != null && openAPIDiscovery.isLoaded()) {
                List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints = openAPIDiscovery.getEndpointsForService(service);
                for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
                    availableEndpoints.add(endpoint.getMethod().toUpperCase() + " " + endpoint.getPath());
                }
            }

            // Use LLM to discover the most appropriate endpoint for this service and parameter
            String prompt = buildEndpointDiscoveryPrompt(service, parameterInfo, availableEndpoints);
            String llmResponse = askLLMForEndpoint(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                return parseEndpointFromLLMResponse(llmResponse);
            }
        } catch (Exception e) {
            log.warn("LLM endpoint discovery failed for service '{}' and parameter '{}': {}",
                    service, parameterInfo.getName(), e.getMessage());
        }
        
        // Final fallback to a default pattern
        String fallbackEndpoint = "/api/v1/" + service.toLowerCase().replace("-", "") + "/data";
        log.debug("Using fallback endpoint '{}' for service '{}'", fallbackEndpoint, service);
        return fallbackEndpoint;
     }

     /**
      * Use LLM to select the best endpoint for a parameter
      */
     private String selectEndpointWithLLM(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo, String serviceName) {
         try {
             // Build endpoint selection prompt
             String prompt = buildEndpointSelectionPrompt(endpoints, parameterInfo, serviceName);

             if (prompt.length() > 2044) {
                 log.warn("Endpoint selection prompt too long ({} chars), falling back to scoring", prompt.length());
                 return null;
             }

             // Ask LLM to select best endpoint
             String llmResponse = askLLMForEndpointSelection(prompt);

             if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                 String cleanResponse = llmResponse.trim();

                 // Check for NO_GOOD_MATCH
                 if (cleanResponse.equals("NO_GOOD_MATCH")) {
                     log.info("LLM indicated no good endpoint match for parameter '{}'", parameterInfo.getName());
                     return null;
                 }

                 // Validate that the selected endpoint exists
                 for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
                     if (endpoint.getPath().equals(cleanResponse)) {
                         return cleanResponse;
                     }
                 }

                 log.warn("LLM selected non-existent endpoint '{}', falling back to scoring", cleanResponse);
             }

         } catch (Exception e) {
             log.debug("LLM endpoint selection failed: {}", e.getMessage());
         }

         return null; // Fallback to scoring
     }

     /**
      * Build prompt for LLM endpoint selection
      */
     private String buildEndpointSelectionPrompt(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo, String serviceName) {
         StringBuilder prompt = new StringBuilder();
         prompt.append("Service: ").append(serviceName).append("\n");
         prompt.append("Parameter: ").append(parameterInfo.getName()).append(" (type: ").append(parameterInfo.getType()).append(")\n");
         prompt.append("Description: ").append(parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "").append("\n\n");

         prompt.append("Available endpoints:\n");
         for (int i = 0; i < Math.min(10, endpoints.size()); i++) {
             OpenAPIEndpointDiscovery.EndpointInfo endpoint = endpoints.get(i);
             prompt.append("- ").append(endpoint.getMethod().toUpperCase()).append(" ").append(endpoint.getPath()).append("\n");
         }

         prompt.append("\nTask: Select the BEST endpoint to fetch data for this parameter.\n");
         prompt.append("Consider parameter semantics and endpoint purposes.\n\n");
         prompt.append("Guidelines:\n");
         prompt.append("- For 'list' parameters: prefer GET endpoints returning collections\n");
         prompt.append("- For 'id' parameters: prefer GET endpoints with path parameters\n");
         prompt.append("- For 'name' parameters: prefer GET endpoints returning entity details\n");
         prompt.append("- Avoid utility endpoints (welcome, health, status)\n\n");
         prompt.append("Respond with ONLY the endpoint path (e.g., /api/v1/service/resource)\n");
         prompt.append("If NO endpoint is suitable, respond with: NO_GOOD_MATCH");

         return prompt.toString();
     }

     /**
      * Intelligently select the best endpoint for a parameter from available OpenAPI endpoints
      */
     private String selectBestEndpointForParameter(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo) {
         String paramName = parameterInfo.getName() != null ? parameterInfo.getName().toLowerCase() : "";
         String paramDesc = parameterInfo.getDescription() != null ? parameterInfo.getDescription().toLowerCase() : "";

         // Score each endpoint based on how well it matches the parameter
         String bestEndpoint = null;
         int bestScore = -1;

         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             int score = scoreEndpointForParameter(endpoint, paramName, paramDesc);
             log.debug("Endpoint '{}' scored {} for parameter '{}'", endpoint.getPath(), score, paramName);

             if (score > bestScore) {
                 bestScore = score;
                 bestEndpoint = endpoint.getPath();
             }
         }

         // If no endpoint scored well, use the first non-welcome endpoint
         if (bestScore <= 0) {
             for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
                 if (!endpoint.getPath().contains("welcome") && !endpoint.getPath().contains("health")) {
                     log.debug("No good scoring endpoint, using first non-utility endpoint: {}", endpoint.getPath());
                     return endpoint.getPath();
                 }
             }
             // Last resort: use first endpoint
             return endpoints.get(0).getPath();
         }

         log.info("Selected endpoint '{}' with score {} for parameter '{}'", bestEndpoint, bestScore, paramName);
         return bestEndpoint;
     }

     /**
      * Score how well an endpoint matches a parameter (higher score = better match)
      */
     private int scoreEndpointForParameter(OpenAPIEndpointDiscovery.EndpointInfo endpoint, String paramName, String paramDesc) {
         String path = endpoint.getPath().toLowerCase();
         String method = endpoint.getMethod() != null ? endpoint.getMethod().toLowerCase() : "get";
         int score = 0;

         // Prefer GET endpoints for data fetching (unless parameter suggests otherwise)
         if ("get".equals(method)) {
             score += 10;
         }

         // Score based on parameter name patterns
         if (paramName.contains("list") || paramName.contains("all")) {
             // For list parameters, prefer endpoints that return collections
             if (path.contains("/routes") && !path.contains("/{") && "get".equals(method)) {
                 score += 50; // /api/v1/routeservice/routes (GET all)
             }
             if (path.contains("/stations") && !path.contains("/{") && "get".equals(method)) {
                 score += 50; // /api/v1/stationservice/stations (GET all)
             }
             if (path.contains("/travels") && !path.contains("/{") && "get".equals(method)) {
                 score += 50; // /api/v1/travelservice/travels (GET all)
             }
         }

         // Score based on semantic matching
         if (paramName.contains("distance")) {
             if (path.contains("route")) score += 30;
             if (path.contains("travel")) score += 20;
         }

         if (paramName.contains("station")) {
             if (path.contains("station")) score += 30;
             if (path.contains("route")) score += 20;
         }

         if (paramName.contains("train")) {
             if (path.contains("train")) score += 30;
             if (path.contains("travel")) score += 20;
         }

         if (paramName.contains("price") || paramName.contains("cost")) {
             if (path.contains("price")) score += 30;
             if (path.contains("order")) score += 20;
         }

         if (paramName.contains("user") || paramName.contains("login")) {
             if (path.contains("user")) score += 30;
             if (path.contains("auth")) score += 20;
         }

         // Penalize utility endpoints
         if (path.contains("welcome") || path.contains("health") || path.contains("status")) {
             score -= 100;
         }

         // Penalize endpoints with many path parameters (they're usually for specific lookups)
         long pathParamCount = path.chars().filter(ch -> ch == '{').count();
         if (pathParamCount > 1) {
             score -= 20;
         } else if (pathParamCount == 1) {
             score -= 10;
         }

         return score;
     }

     private String buildEndpointDiscoveryPrompt(String serviceName, ParameterInfo parameterInfo) {
         return buildEndpointDiscoveryPrompt(serviceName, parameterInfo, new ArrayList<>());
     }

     private String buildEndpointDiscoveryPrompt(String serviceName, ParameterInfo parameterInfo, List<String> availableEndpoints) {
         String template = registry.getLlmPrompts().get("endpointDiscovery");

         // Truncate description if too long to prevent message length issues
         String description = parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "";
         if (description.length() > 300) {
             description = description.substring(0, 297) + "...";
         }

         // Build available endpoints context
         String endpointsContext = "";
         if (!availableEndpoints.isEmpty()) {
             endpointsContext = "\n\nAvailable endpoints in " + serviceName + ":\n" +
                               String.join("\n", availableEndpoints.subList(0, Math.min(5, availableEndpoints.size())));
             if (availableEndpoints.size() > 5) {
                 endpointsContext += "\n... and " + (availableEndpoints.size() - 5) + " more";
             }
         }

         String prompt = template
                 .replace("{serviceName}", serviceName != null ? serviceName : "")
                 .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                 .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                 .replace("{parameterDescription}", description) + endpointsContext;

         // Ensure total prompt length doesn't exceed limit
         if (prompt.length() > 2000) {
             log.warn("Endpoint discovery prompt too long ({}), truncating", prompt.length());
             prompt = prompt.substring(0, 1997) + "...";
         }
         
         return prompt;
     }
     
     private String askLLMForEndpoint(String prompt) {
         try {
             // Call LLM directly for endpoint discovery with appropriate system prompt
             String rawResponse = callLLMForEndpointDiscovery(prompt);

             if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                 String cleaned = cleanJsonFromMarkdown(rawResponse);

                 // Check for NO_GOOD_MATCH response
                 if (cleaned.trim().equals("NO_GOOD_MATCH")) {
                     return "NO_GOOD_MATCH";
                 }

                 return cleaned;
             }

             return null;
             
         } catch (Exception e) {
             log.warn("Failed to ask LLM for endpoint: {}", e.getMessage());
             return null;
         }
     }
     
     private String parseEndpointFromLLMResponse(String llmResponse) {
         // Check for NO_GOOD_MATCH response first
         if (llmResponse != null && llmResponse.trim().equals("NO_GOOD_MATCH")) {
             log.info("LLM indicated no good endpoint match");
             return null;
         }

         try {
             JsonNode jsonResponse = objectMapper.readTree(llmResponse);
             if (jsonResponse.has("endpoint")) {
                 String endpoint = jsonResponse.get("endpoint").asText();
                 log.debug("LLM discovered endpoint: {}", endpoint);
                 return endpoint;
             } else {
                 log.debug("LLM response missing 'endpoint' field: {}", llmResponse);
             }
         } catch (Exception e) {
             log.debug("Failed to parse endpoint from LLM response: {} (raw: '{}')", e.getMessage(), llmResponse);
         }
         return null;
     }

     /**
      * Ask LLM to select the best endpoint from available options
      */
     private String askLLMForEndpointSelection(String prompt) {
         try {
             // Call LLM with endpoint selection prompt
             String rawResponse = callLLMForEndpointDiscovery(prompt);

             if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                 String cleaned = cleanJsonFromMarkdown(rawResponse);

                 // Check for NO_GOOD_MATCH response first
                 if (cleaned.trim().equals("NO_GOOD_MATCH")) {
                     return "NO_GOOD_MATCH";
                 }

                 // Extract endpoint path from response
                 String endpointPath = cleaned.trim();

                 // Remove quotes if present
                 if (endpointPath.startsWith("\"") && endpointPath.endsWith("\"")) {
                     endpointPath = endpointPath.substring(1, endpointPath.length() - 1);
                 }

                 return endpointPath;
             }

             return null;
         } catch (Exception e) {
             log.warn("Failed to call LLM for endpoint selection: {}", e.getMessage());
             return null;
         }
     }
 }