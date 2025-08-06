package es.us.isa.restest.inputs.smart;

import es.us.isa.restest.generators.AiDrivenLLMGenerator;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.llm.LLMService;
import es.us.isa.restest.llm.LLMConfig;

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
    private final LLMService llmService;
    private final ObjectMapper objectMapper;
    private final Random random;
    private final OpenAPIEndpointDiscovery openAPIDiscovery;
    private final SmartFetchAuthManager authManager;

    // Runtime data
    private InputFetchRegistry registry;
    private Map<String, CachedValue> cache;
    private Map<String, List<String>> diverseValueCache; // Cache multiple values per parameter
    private Map<String, Integer> valueRotationIndex; // Track which value to use next
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

        // Initialize LLM service with properties from system
        Map<String, String> llmProperties = loadLLMProperties();
        this.llmService = LLMService.getInstance(llmProperties);

        this.objectMapper = new ObjectMapper();
        this.random = new Random();
        this.cache = new ConcurrentHashMap<>();
        this.diverseValueCache = new ConcurrentHashMap<>();
        this.valueRotationIndex = new ConcurrentHashMap<>();
        this.openAPIDiscovery = new OpenAPIEndpointDiscovery();

        // Initialize authentication manager
        this.authManager = new SmartFetchAuthManager(
            baseUrl,
            config.getAuthAdminUsername(),
            config.getAuthAdminPassword()
        );

        loadRegistry();
        loadOpenAPISpec();

        log.info("SmartInputFetcher initialized with config: {}", config);
        log.info("Authentication manager configured: {}", authManager.isConfigured());
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
            "auth.admin.username", "auth.admin.password", "auth.user.username", "auth.user.password"
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

            // First, try to get a diverse cached value
            String diverseValue = getNextDiverseValue(parameterInfo);
            if (diverseValue != null) {
                log.info("🔄 Using diverse cached value → {} = {} ✅", parameterInfo.getName(), diverseValue);
                return diverseValue;
            }

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
     * DEPRECATED: Pattern discovery creates JSONPath mappings - we want direct extraction only
     */
    private List<ApiMapping> discoverByPatterns(ParameterInfo parameterInfo) {
        List<ApiMapping> mappings = new ArrayList<>();
        String paramName = parameterInfo.getName();

        log.info("🔍 Pattern discovery for '{}' checking {} patterns", paramName, registry.getServicePatterns().size());
        log.warn("❌ DEPRECATED: Pattern discovery creates JSONPath mappings - we want direct extraction only");
        log.warn("❌ Skipping pattern discovery to avoid JSONPath expressions like '$.data[*].route.endStation'");

        // DISABLED: Pattern discovery creates JSONPath mappings, but we want direct extraction only
        // The old pattern discovery would create mappings like:
        // endStation -> /api/v1/stationservice/stations (extractPath: $.data[*].route.endStation)
        //
        // Instead, we want LLM-based discovery that creates:
        // endStation -> /api/v1/stationservice/stations (extractPath: DIRECT_EXTRACTION)

        log.info("📋 Pattern discovery for '{}' created {} mappings (disabled for direct extraction)", paramName, mappings.size());
        return mappings; // Return empty list to force LLM-based discovery
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

        // Always use GET for data fetching
        String httpMethod = "GET";
        log.info("🌐 API Call: {} {} for parameter '{}'", httpMethod, url, parameterInfo.getName());

        // Make HTTP request (GET only for data fetching)
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(httpMethod);
        conn.setRequestProperty("Content-Type", config.getDefaultContentType());

        // Add authentication headers
        if (authManager.isConfigured()) {
            authManager.addAuthHeaders(conn);
            log.debug("🔐 Added authentication headers to API request");
        } else {
            log.warn("⚠️ Authentication not configured, API call may fail with 403");
        }
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
            log.info("🔄 Starting DIRECT VALUE EXTRACTION for parameter '{}'", parameterInfo.getName());

            // Build direct extraction prompt
            String prompt = buildDirectExtractionPrompt(responseBody, parameterInfo);

            if (prompt.length() > 2044) {
                log.warn("Direct extraction prompt too long ({} chars), using fallback", prompt.length());
                return extractValueWithSimpleFallback(responseBody, parameterInfo);
            }

            log.info("🧠 Calling LLM for DIRECT VALUE EXTRACTION (not JSONPath) for parameter '{}'", parameterInfo.getName());

            // Ask LLM to extract value directly
            String llmResponse = askLLMForDirectValueExtraction(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                String cleanResponse = llmResponse.trim();

                // Check for NO_GOOD_MATCH response
                if (cleanResponse.equals("NO_GOOD_MATCH")) {
                    log.info("LLM indicated no good value match for parameter '{}'", parameterInfo.getName());
                    return extractValueWithSimpleFallback(responseBody, parameterInfo);
                }

                // Check if LLM incorrectly returned a JSONPath expression
                if (cleanResponse.startsWith("$.") || cleanResponse.contains("$[") || cleanResponse.contains("data[")) {
                    log.error("❌ CRITICAL BUG: LLM returned JSONPath expression '{}' instead of actual value for parameter '{}'",
                             cleanResponse, parameterInfo.getName());
                    log.error("❌ This should NEVER happen with direct value extraction!");
                    log.error("❌ Using fallback extraction instead");
                    return extractValueWithSimpleFallback(responseBody, parameterInfo);
                }

                // Validate LLM response quality before using it
                if (!isValidLLMResponse(cleanResponse, parameterInfo)) {
                    log.warn("❌ LLM response '{}' is invalid for parameter '{}', using fallback",
                            cleanResponse, parameterInfo.getName());
                    return extractValueWithSimpleFallback(responseBody, parameterInfo);
                }

                log.info("✅ LLM extracted ACTUAL VALUE '{}' for parameter '{}' (not JSONPath)", cleanResponse, parameterInfo.getName());

                // Format the value according to OpenAPI schema
                String formattedValue = formatValueForSchema(cleanResponse, parameterInfo);
                log.info("🔧 Formatted value '{}' → '{}' for parameter '{}'", cleanResponse, formattedValue, parameterInfo.getName());

                // Try to extract additional diverse values from the same response
                extractAdditionalDiverseValues(responseBody, parameterInfo, cleanResponse);

                return formattedValue;
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
            // Call LLM for direct value extraction (NOT JSONPath generation)
            String rawResponse = callLLMForDirectValueExtractionFromResponse(prompt);

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

                        // Last resort: Ask LLM to generate a meaningful value instead of using random strings
                        log.info("📋 No suitable values found in API response, asking LLM to generate meaningful value for parameter '{}'", parameterInfo.getName());
                        String llmGeneratedValue = generateValueWithLLM(parameterInfo);
                        if (llmGeneratedValue != null && !llmGeneratedValue.trim().isEmpty()) {
                            log.info("✅ LLM generated meaningful value '{}' for parameter '{}'", llmGeneratedValue, parameterInfo.getName());
                            return llmGeneratedValue;
                        }

                        // Final fallback: return first non-null string value only if LLM fails
                        for (Map.Entry<?, ?> entry : firstItem.entrySet()) {
                            Object value = entry.getValue();
                            if (value != null && value instanceof String && !value.toString().trim().isEmpty()) {
                                log.warn("📋 LLM generation failed, using first string value '{}' for parameter '{}' as last resort",
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
        String systemContent = "You are an AI assistant that selects the most appropriate value from given data. Respond with only the selected value, no explanations.";

        log.debug("[Value Selection LLM] Using LLM service with model type: {}", llmService.getConfig().getModelType());
        log.debug("[Value Selection LLM] User prompt: {}", prompt);

        try {
            String result = llmService.generateText(systemContent, prompt, 100, 0.1);

            if (result != null && !result.trim().isEmpty()) {
                log.debug("[Value Selection LLM] Successfully generated content: {}", result);
                return result;
            } else {
                log.warn("[Value Selection LLM] LLM service returned null or empty result");
                return null;
            }

        } catch (Exception e) {
            log.warn("[Value Selection LLM] Failed to call LLM service: {}", e.getMessage());
            return null;
        }
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
            // Format value according to OpenAPI schema before caching
            String formattedValue = formatValueForSchema(value, parameterInfo);

            String cacheKey = buildCacheKey(parameterInfo);
            cache.put(cacheKey, new CachedValue(formattedValue));

            // Also add to diverse value cache
            cacheDiverseValue(parameterInfo, formattedValue);
        }
    }

    /**
     * Cache multiple diverse values for a parameter
     */
    private void cacheDiverseValue(ParameterInfo parameterInfo, String value) {
        // Format value according to OpenAPI schema before caching
        String formattedValue = formatValueForSchema(value, parameterInfo);

        String cacheKey = buildCacheKey(parameterInfo);
        diverseValueCache.computeIfAbsent(cacheKey, k -> new ArrayList<>());

        List<String> values = diverseValueCache.get(cacheKey);
        if (!values.contains(formattedValue)) {
            values.add(formattedValue);
            log.debug("📋 Cached diverse value '{}' for parameter '{}' (total: {})",
                     formattedValue, parameterInfo.getName(), values.size());
        }
    }

    /**
     * Get next diverse value using rotation
     */
    private String getNextDiverseValue(ParameterInfo parameterInfo) {
        String cacheKey = buildCacheKey(parameterInfo);
        List<String> values = diverseValueCache.get(cacheKey);

        if (values == null || values.isEmpty()) {
            return null;
        }

        // Get current rotation index
        int currentIndex = valueRotationIndex.getOrDefault(cacheKey, 0);

        // Get value at current index
        String value = values.get(currentIndex % values.size());

        // Update rotation index for next call
        valueRotationIndex.put(cacheKey, (currentIndex + 1) % values.size());

        log.debug("🔄 Rotating to diverse value '{}' for parameter '{}' (index: {}/{})",
                 value, parameterInfo.getName(), currentIndex, values.size());

        return value;
    }

    /**
     * Extract additional diverse values from API response using LLM-based analysis
     */
    private void extractAdditionalDiverseValues(String responseBody, ParameterInfo parameterInfo, String firstValue) {
        try {
            log.debug("🔍 Extracting additional diverse values for parameter '{}'", parameterInfo.getName());

            // Use LLM to extract all possible values from the response
            Set<String> extractedValues = extractAllValuesWithLLM(responseBody, parameterInfo);
            extractedValues.add(firstValue); // Include the first value

            // Cache all extracted diverse values
            for (String value : extractedValues) {
                if (!value.equals(firstValue)) { // Don't re-cache the first value
                    cacheDiverseValue(parameterInfo, value);
                }
            }

            if (extractedValues.size() > 1) {
                log.info("📋 Extracted {} diverse values for parameter '{}': {}",
                        extractedValues.size(), parameterInfo.getName(),
                        extractedValues.stream().limit(5).collect(Collectors.toList()));
            }

            // If we don't have enough values, generate more using semantic similarity
            int requiredValues = getRequiredValueCount(parameterInfo);
            if (extractedValues.size() < requiredValues) {
                log.info("🔍 Need {} values but only have {}, generating additional values using semantic similarity",
                        requiredValues, extractedValues.size());
                generateAdditionalValuesWithSemanticSimilarity(parameterInfo, extractedValues, requiredValues);
            }

        } catch (Exception e) {
            log.debug("Failed to extract additional diverse values for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
        }
    }

    /**
     * Extract all possible values from API response using LLM
     */
    private Set<String> extractAllValuesWithLLM(String responseBody, ParameterInfo parameterInfo) {
        Set<String> extractedValues = new HashSet<>();

        try {
            String prompt = buildMultipleValueExtractionPrompt(responseBody, parameterInfo);

            if (prompt.length() > 2044) {
                log.warn("Multiple value extraction prompt too long ({} chars), using fallback", prompt.length());
                return extractValuesWithFallback(responseBody, parameterInfo);
            }

            String llmResponse = askLLMForMultipleValueExtraction(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                // Parse LLM response to extract multiple values
                String[] values = llmResponse.split("[,\\n\\r]+");
                for (String value : values) {
                    String cleanValue = value.trim().replaceAll("^[\"']|[\"']$", ""); // Remove quotes
                    if (!cleanValue.isEmpty() && !cleanValue.equals("NO_VALUES_FOUND") &&
                        isValidValueForParameter(cleanValue, parameterInfo)) {
                        extractedValues.add(cleanValue);
                    }
                }

                log.debug("LLM extracted {} values from API response for parameter '{}'",
                         extractedValues.size(), parameterInfo.getName());
            }

        } catch (Exception e) {
            log.debug("LLM multiple value extraction failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
        }

        return extractedValues;
    }

    /**
     * Validate LLM response quality to reject explanations and invalid values
     */
    private boolean isValidLLMResponse(String response, ParameterInfo parameterInfo) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }

        String cleanResponse = response.trim();
        String paramName = parameterInfo.getName().toLowerCase();

        // Reject responses that look like explanations or descriptions
        if (cleanResponse.contains("The format appears to be") ||
            cleanResponse.contains("delivery route)") ||
            cleanResponse.contains("This is a") ||
            cleanResponse.contains("appears to be") ||
            cleanResponse.contains("should be") ||
            cleanResponse.contains("typically") ||
            cleanResponse.contains("usually") ||
            cleanResponse.contains("format:") ||
            cleanResponse.contains("example:") ||
            cleanResponse.length() > 100) { // Very long responses are likely explanations
            log.warn("❌ LLM response looks like explanation, not value: '{}'", cleanResponse);
            return false;
        }

        // Reject generic placeholder values
        if (cleanResponse.equals("objects") ||
            cleanResponse.equals("values") ||
            cleanResponse.equals("data") ||
            cleanResponse.equals("items") ||
            cleanResponse.equals("elements") ||
            cleanResponse.equals("content")) {
            log.warn("❌ LLM response is generic placeholder: '{}'", cleanResponse);
            return false;
        }

        // Validate parameter-specific formats
        if (paramName.contains("id") || paramName.contains("route")) {
            // IDs should be reasonable length and format
            if (cleanResponse.length() < 3 || cleanResponse.length() > 50) {
                log.warn("❌ ID parameter '{}' has invalid length: '{}'", paramName, cleanResponse);
                return false;
            }

            // Reject responses that contain explanatory text
            if (cleanResponse.contains(" ") && cleanResponse.split(" ").length > 3) {
                log.warn("❌ ID parameter '{}' contains too many words: '{}'", paramName, cleanResponse);
                return false;
            }
        }

        // Validate numeric parameters
        if (paramName.contains("price") || paramName.contains("rate") || paramName.contains("number")) {
            // Should be numeric or at least contain numbers
            if (!cleanResponse.matches(".*\\d.*")) {
                log.warn("❌ Numeric parameter '{}' contains no digits: '{}'", paramName, cleanResponse);
                return false;
            }
        }

        return true; // Response looks valid
    }

    /**
     * Extract value using simple fallback when LLM fails
     */
    private String extractValueWithSimpleFallback(String responseBody, ParameterInfo parameterInfo) {
        try {
            String paramName = parameterInfo.getName().toLowerCase();

            // Generate reasonable fallback values based on parameter type
            if (paramName.contains("id") || paramName.contains("route")) {
                return generateReasonableId(paramName);
            } else if (paramName.contains("station")) {
                return generateReasonableStation();
            } else if (paramName.contains("train")) {
                return generateReasonableTrain();
            } else if (paramName.contains("price") || paramName.contains("rate")) {
                return generateReasonablePrice();
            } else if (paramName.contains("class") || paramName.contains("type")) {
                return generateReasonableClass(paramName);
            } else {
                return generateGenericValue(paramName);
            }

        } catch (Exception e) {
            log.debug("Fallback value generation failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
            return "fallback_value";
        }
    }

    /**
     * Generate reasonable ID values
     */
    private String generateReasonableId(String paramName) {
        if (paramName.contains("route")) {
            return "route_" + (System.currentTimeMillis() % 10000);
        } else if (paramName.contains("account")) {
            return "acc_" + (System.currentTimeMillis() % 10000);
        } else {
            return "id_" + (System.currentTimeMillis() % 10000);
        }
    }

    /**
     * Generate reasonable station names
     */
    private String generateReasonableStation() {
        String[] stations = {"Shanghai", "Beijing", "Nanjing", "Suzhou", "Hangzhou"};
        return stations[(int)(System.currentTimeMillis() % stations.length)];
    }

    /**
     * Generate reasonable train numbers
     */
    private String generateReasonableTrain() {
        String[] prefixes = {"G", "D", "K", "T"};
        String prefix = prefixes[(int)(System.currentTimeMillis() % prefixes.length)];
        return prefix + (1000 + (System.currentTimeMillis() % 9000));
    }

    /**
     * Generate reasonable prices
     */
    private String generateReasonablePrice() {
        return String.valueOf(50 + (System.currentTimeMillis() % 500));
    }

    /**
     * Generate reasonable class/type values
     */
    private String generateReasonableClass(String paramName) {
        if (paramName.contains("seat")) {
            String[] classes = {"1", "2", "Economy", "Business"};
            return classes[(int)(System.currentTimeMillis() % classes.length)];
        } else if (paramName.contains("train")) {
            String[] types = {"highspeed", "normal", "express"};
            return types[(int)(System.currentTimeMillis() % types.length)];
        } else {
            return "standard";
        }
    }

    /**
     * Generate generic reasonable values
     */
    private String generateGenericValue(String paramName) {
        return paramName + "_" + (System.currentTimeMillis() % 1000);
    }

    /**
     * Build prompt for LLM multiple value extraction
     */
    private String buildMultipleValueExtractionPrompt(String responseBody, ParameterInfo parameterInfo) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Extract ALL possible values from this JSON response that could be used for parameter '");
        prompt.append(parameterInfo.getName()).append("' (type: ").append(parameterInfo.getType()).append(").\n\n");

        if (parameterInfo.getDescription() != null && !parameterInfo.getDescription().trim().isEmpty()
            && !parameterInfo.getDescription().equals("null")) {
            prompt.append("Parameter description: ").append(parameterInfo.getDescription()).append("\n\n");
        }

        // Truncate response if too long
        String truncatedResponse = truncateResponseSchemaForLLM(responseBody, "", parameterInfo);
        prompt.append("JSON Response:\n").append(truncatedResponse).append("\n\n");

        prompt.append("Instructions:\n");
        prompt.append("1. Find ALL values in the JSON that are semantically relevant for this parameter\n");
        prompt.append("2. Look for values in arrays, nested objects, and all fields\n");
        prompt.append("3. Consider field names, data types, and semantic meaning\n");
        prompt.append("4. Extract actual values, not field names or paths\n");
        prompt.append("5. For array-type parameters (stations, distances, lists), extract individual elements\n");
        prompt.append("6. For numeric parameters, extract only numeric values\n");
        prompt.append("7. Return each value on a separate line\n");
        prompt.append("8. If no relevant values found, respond with: NO_VALUES_FOUND\n\n");

        prompt.append("Example for parameter 'stationName':\n");
        prompt.append("Shanghai\n");
        prompt.append("Beijing\n");
        prompt.append("Nanjing\n");

        return prompt.toString();
    }

    /**
     * Ask LLM to extract multiple values from API response
     */
    private String askLLMForMultipleValueExtraction(String prompt) {
        try {
            String systemContent = "You are a data extraction expert. Extract ALL relevant values from JSON responses for test parameter generation. " +
                                  "Focus on finding diverse, meaningful values that match the parameter semantically. " +
                                  "Return actual values, not JSONPath expressions or field names.";

            String result = llmService.generateText(systemContent, prompt, 200, 0.3);

            if (result != null && !result.trim().isEmpty()) {
                log.debug("[Multiple Value Extraction LLM] Successfully extracted values: {}", result);
                return result;
            } else {
                log.warn("[Multiple Value Extraction LLM] LLM service returned null or empty result");
                return null;
            }

        } catch (Exception e) {
            log.warn("[Multiple Value Extraction LLM] Failed to call LLM service: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Fallback value extraction when LLM fails
     */
    private Set<String> extractValuesWithFallback(String responseBody, ParameterInfo parameterInfo) {
        Set<String> extractedValues = new HashSet<>();

        try {
            // Simple JSON parsing fallback
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);

            extractValuesFromJsonNode(rootNode, parameterInfo, extractedValues);

        } catch (Exception e) {
            log.debug("Fallback value extraction failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
        }

        return extractedValues;
    }

    /**
     * Get required number of diverse values based on test case count
     */
    private int getRequiredValueCount(ParameterInfo parameterInfo) {
        // Estimate based on typical test generation needs
        // This could be made configurable or dynamic based on test suite size
        return Math.max(5, 10); // At least 5, preferably 10 diverse values
    }

    /**
     * Generate additional values using semantic similarity when API values are insufficient
     */
    private void generateAdditionalValuesWithSemanticSimilarity(ParameterInfo parameterInfo, Set<String> existingValues, int requiredCount) {
        try {
            int additionalNeeded = requiredCount - existingValues.size();
            if (additionalNeeded <= 0) {
                return;
            }

            log.info("🧠 Generating {} additional values for parameter '{}' using semantic similarity",
                    additionalNeeded, parameterInfo.getName());

            // Use LLM to generate semantically similar values
            Set<String> generatedValues = generateSemanticallySimilarValues(parameterInfo, existingValues, additionalNeeded);

            // Cache the generated values
            for (String value : generatedValues) {
                cacheDiverseValue(parameterInfo, value);
            }

            if (!generatedValues.isEmpty()) {
                log.info("✅ Generated {} additional semantic values for parameter '{}': {}",
                        generatedValues.size(), parameterInfo.getName(),
                        generatedValues.stream().limit(3).collect(Collectors.toList()));
            }

        } catch (Exception e) {
            log.warn("Failed to generate additional semantic values for parameter '{}': {}",
                    parameterInfo.getName(), e.getMessage());
        }
    }

    /**
     * Generate semantically similar values using LLM-based semantic understanding
     */
    private Set<String> generateSemanticallySimilarValues(ParameterInfo parameterInfo, Set<String> existingValues, int count) {
        Set<String> generatedValues = new HashSet<>();

        try {
            String prompt = buildSemanticSimilarityPrompt(parameterInfo, existingValues, count);

            if (prompt.length() > 2044) {
                log.warn("Semantic similarity prompt too long ({} chars), using fallback", prompt.length());
                return generateFallbackSemanticValues(parameterInfo, existingValues, count);
            }

            String llmResponse = askLLMForSemanticSimilarValues(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                // Parse LLM response to extract generated values
                String[] values = llmResponse.split("[,\\n\\r]+");
                for (String value : values) {
                    String cleanValue = value.trim().replaceAll("^[\"']|[\"']$", ""); // Remove quotes
                    if (!cleanValue.isEmpty() && !cleanValue.equals("NO_VALUES_GENERATED") &&
                        !existingValues.contains(cleanValue) &&
                        isValidValueForParameter(cleanValue, parameterInfo)) {
                        generatedValues.add(cleanValue);

                        if (generatedValues.size() >= count) {
                            break; // Got enough values
                        }
                    }
                }

                log.debug("LLM generated {} semantic values for parameter '{}'",
                         generatedValues.size(), parameterInfo.getName());
            }

        } catch (Exception e) {
            log.debug("LLM semantic value generation failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
        }

        return generatedValues;
    }

    /**
     * Build prompt for semantic similarity-based value generation
     */
    private String buildSemanticSimilarityPrompt(ParameterInfo parameterInfo, Set<String> existingValues, int count) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Generate ").append(count).append(" additional values that are semantically similar to the existing values ");
        prompt.append("for parameter '").append(parameterInfo.getName()).append("' (type: ").append(parameterInfo.getType()).append(").\n\n");

        if (parameterInfo.getDescription() != null && !parameterInfo.getDescription().trim().isEmpty()
            && !parameterInfo.getDescription().equals("null")) {
            prompt.append("Parameter description: ").append(parameterInfo.getDescription()).append("\n\n");
        }

        prompt.append("Existing values:\n");
        for (String value : existingValues.stream().limit(5).collect(Collectors.toList())) {
            prompt.append("- ").append(value).append("\n");
        }

        prompt.append("\nInstructions:\n");
        prompt.append("1. Generate values that are semantically similar to the existing ones\n");
        prompt.append("2. Consider the same domain, category, or type as existing values\n");
        prompt.append("3. Use similar naming patterns, formats, or structures\n");
        prompt.append("4. Generate realistic, meaningful values (not random strings)\n");
        prompt.append("5. Each value should be different from existing ones\n");
        prompt.append("6. Return each value on a separate line\n");
        prompt.append("7. If unable to generate similar values, respond with: NO_VALUES_GENERATED\n\n");

        prompt.append("Examples:\n");
        prompt.append("If existing values are [Shanghai, Beijing] → generate: Nanjing, Hangzhou, Suzhou\n");
        prompt.append("If existing values are [G1237, D2468] → generate: K5678, T9012, Z3456\n");
        prompt.append("If existing values are [admin, user123] → generate: manager, guest456, operator\n");

        return prompt.toString();
    }

    /**
     * Ask LLM to generate semantically similar values
     */
    private String askLLMForSemanticSimilarValues(String prompt) {
        try {
            String systemContent = "You are an expert in semantic similarity and test data generation. " +
                                  "Generate diverse but semantically similar values based on existing examples. " +
                                  "Focus on maintaining the same domain, format, and meaning while ensuring diversity. " +
                                  "Use your knowledge of real-world entities, naming patterns, and data structures.";

            String result = llmService.generateText(systemContent, prompt, 150, 0.7); // Higher temperature for creativity

            if (result != null && !result.trim().isEmpty()) {
                log.debug("[Semantic Similarity LLM] Successfully generated similar values: {}", result);
                return result;
            } else {
                log.warn("[Semantic Similarity LLM] LLM service returned null or empty result");
                return null;
            }

        } catch (Exception e) {
            log.warn("[Semantic Similarity LLM] Failed to call LLM service: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Fallback semantic value generation when LLM fails
     */
    private Set<String> generateFallbackSemanticValues(ParameterInfo parameterInfo, Set<String> existingValues, int count) {
        Set<String> generatedValues = new HashSet<>();

        try {
            // Simple pattern-based generation as fallback
            String paramName = parameterInfo.getName().toLowerCase();

            if (paramName.contains("station") && !existingValues.isEmpty()) {
                // Generate station names
                String[] stationSuffixes = {"Station", "Railway", "Terminal", "Junction", "Central"};
                String[] cityPrefixes = {"North", "South", "East", "West", "Central", "New", "Old"};

                for (int i = 0; i < count && generatedValues.size() < count; i++) {
                    String prefix = cityPrefixes[i % cityPrefixes.length];
                    String suffix = stationSuffixes[i % stationSuffixes.length];
                    generatedValues.add(prefix + " " + suffix);
                }
            } else if (paramName.contains("train") && !existingValues.isEmpty()) {
                // Generate train numbers
                String[] trainPrefixes = {"G", "D", "K", "T", "Z", "C"};
                for (int i = 0; i < count && generatedValues.size() < count; i++) {
                    String prefix = trainPrefixes[i % trainPrefixes.length];
                    int number = 1000 + (i * 123) % 9000; // Generate varied numbers
                    generatedValues.add(prefix + number);
                }
            } else if (paramName.contains("user") || paramName.contains("id")) {
                // Generate user IDs
                String[] userPrefixes = {"user", "admin", "guest", "manager", "operator", "tester"};
                for (int i = 0; i < count && generatedValues.size() < count; i++) {
                    String prefix = userPrefixes[i % userPrefixes.length];
                    int number = 100 + (i * 47) % 900;
                    generatedValues.add(prefix + number);
                }
            } else {
                // Generic fallback
                for (int i = 0; i < count; i++) {
                    generatedValues.add("generated_value_" + (i + 1));
                }
            }

        } catch (Exception e) {
            log.debug("Fallback semantic value generation failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
        }

        return generatedValues;
    }

    /**
     * Recursively extract values from JSON node based on parameter characteristics
     */
    private void extractValuesFromJsonNode(JsonNode node, ParameterInfo parameterInfo, Set<String> extractedValues) {
        if (node == null || extractedValues.size() >= 10) { // Limit to 10 diverse values
            return;
        }

        String paramName = parameterInfo.getName().toLowerCase();
        String paramType = parameterInfo.getType();

        if (node.isArray()) {
            // Extract from array elements
            for (JsonNode arrayElement : node) {
                extractValuesFromJsonNode(arrayElement, parameterInfo, extractedValues);
            }
        } else if (node.isObject()) {
            // Extract from object fields
            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey().toLowerCase();
                JsonNode fieldValue = entry.getValue();

                // Check if field name matches parameter name or type
                if (isRelevantField(fieldName, paramName, paramType)) {
                    if (fieldValue.isTextual()) {
                        String value = fieldValue.asText().trim();
                        if (!value.isEmpty() && isValidValueForParameter(value, parameterInfo)) {
                            extractedValues.add(value);
                        }
                    } else if (fieldValue.isNumber()) {
                        extractedValues.add(fieldValue.asText());
                    }
                }

                // Recursively search in nested objects/arrays
                extractValuesFromJsonNode(fieldValue, parameterInfo, extractedValues);
            });
        }
    }

    /**
     * Check if a field is relevant for the parameter using LLM-based analysis
     */
    private boolean isRelevantField(String fieldName, String paramName, String paramType) {
        // Use LLM to determine field relevance instead of hardcoded rules
        return askLLMForFieldRelevance(fieldName, paramName, paramType);
    }

    /**
     * Ask LLM to determine if a field is relevant for a parameter
     */
    private boolean askLLMForFieldRelevance(String fieldName, String paramName, String paramType) {
        try {
            String prompt = buildFieldRelevancePrompt(fieldName, paramName, paramType);

            if (prompt.length() > 500) { // Keep prompt short for efficiency
                // Fallback to simple name matching for very long prompts
                return fieldName.toLowerCase().contains(paramName.toLowerCase()) ||
                       paramName.toLowerCase().contains(fieldName.toLowerCase());
            }

            String llmResponse = askLLMForFieldRelevanceDecision(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                String cleanResponse = llmResponse.trim().toLowerCase();
                return cleanResponse.equals("yes") || cleanResponse.equals("true") || cleanResponse.equals("relevant");
            }

            // Fallback to simple matching if LLM fails
            return fieldName.toLowerCase().contains(paramName.toLowerCase());

        } catch (Exception e) {
            log.debug("LLM field relevance check failed for field '{}' and parameter '{}': {}",
                     fieldName, paramName, e.getMessage());
            // Fallback to simple matching
            return fieldName.toLowerCase().contains(paramName.toLowerCase());
        }
    }

    /**
     * Build prompt for LLM field relevance analysis
     */
    private String buildFieldRelevancePrompt(String fieldName, String paramName, String paramType) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Determine if the JSON field '").append(fieldName).append("' is relevant ");
        prompt.append("for extracting values for parameter '").append(paramName).append("' ");
        prompt.append("(type: ").append(paramType).append(").\n\n");

        prompt.append("Consider semantic similarity, naming patterns, and data types.\n");
        prompt.append("Examples of relevant matches:\n");
        prompt.append("- Field 'stationName' is relevant for parameter 'endStation'\n");
        prompt.append("- Field 'trainId' is relevant for parameter 'trainNumber'\n");
        prompt.append("- Field 'seatType' is relevant for parameter 'seatClass'\n");
        prompt.append("- Field 'userId' is relevant for parameter 'loginId'\n\n");

        prompt.append("Respond with 'YES' if relevant, 'NO' if not relevant.");

        return prompt.toString();
    }

    /**
     * Ask LLM for field relevance decision
     */
    private String askLLMForFieldRelevanceDecision(String prompt) {
        try {
            String systemContent = "You are a data extraction expert. Determine if JSON fields are semantically relevant for parameter extraction. Be precise and consider naming patterns, semantic similarity, and data types.";

            String result = llmService.generateText(systemContent, prompt, 10, 0.1); // Low temperature for consistency

            if (result != null && !result.trim().isEmpty()) {
                return result.trim();
            }

            return null;

        } catch (Exception e) {
            log.debug("LLM field relevance decision failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validate if a value is suitable for the parameter and format it according to OpenAPI schema
     */
     private boolean isValidValueForParameter(String value, ParameterInfo parameterInfo) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        // Reject UUIDs and very long strings
        if (value.length() > 50 || value.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            return false;
        }

        // Accept reasonable values
        return true;
    }

    /**
     * Format value according to OpenAPI schema type
     */
    private String formatValueForSchema(String value, ParameterInfo parameterInfo) {
        if (value == null || parameterInfo == null) {
            return value;
        }

        try {
            // Check if parameter should be an array based on OpenAPI schema
            if (shouldBeArrayType(parameterInfo)) {
                return formatAsArrayValue(value, parameterInfo);
            }

            // Check if parameter should be a number
            if (shouldBeNumberType(parameterInfo)) {
                return formatAsNumberValue(value, parameterInfo);
            }

            // Default: return as string
            return value;

        } catch (Exception e) {
            log.debug("Failed to format value '{}' for parameter '{}': {}",
                     value, parameterInfo.getName(), e.getMessage());
            return value; // Return original value if formatting fails
        }
    }

    /**
     * Check if parameter should be array type based on OpenAPI schema
     */
    private boolean shouldBeArrayType(ParameterInfo parameterInfo) {
        String paramName = parameterInfo.getName().toLowerCase();
        String paramType = parameterInfo.getType();

        // Check explicit array indicators in type
        if (paramType != null && paramType.toLowerCase().contains("array")) {
            return true;
        }

        // FIXED: Be more conservative about array detection
        // Only treat as array if explicitly indicated by naming or schema

        // Explicit array indicators (very specific)
        if (paramName.equals("distances") || paramName.equals("stations")) {
            return true; // These are explicitly arrays in TrainTicket OpenAPI
        }

        // Do NOT treat these as arrays (common mistakes):
        // - seatClass (should be string)
        // - stationList (should be string, despite "List" in name)
        // - distanceList (should be string, despite "List" in name)
        // - routeId (should be string)
        // - trainType (should be string)

        // Conservative approach: only use LLM for ambiguous cases
        if (paramName.contains("list") && !paramName.equals("stationlist") && !paramName.equals("distancelist")) {
            return askLLMForArrayTypeDecision(parameterInfo);
        }

        return false; // Default to string type
    }

    /**
     * Ask LLM to determine if parameter should be array type
     */
    private boolean askLLMForArrayTypeDecision(ParameterInfo parameterInfo) {
        try {
            String prompt = buildArrayTypePrompt(parameterInfo);

            if (prompt.length() > 500) {
                // Fallback to name-based heuristics
                String paramName = parameterInfo.getName().toLowerCase();
                return paramName.contains("list") || paramName.contains("stations") ||
                       paramName.contains("distances") || paramName.endsWith("s");
            }

            String llmResponse = askLLMForArrayTypeDecisionCall(prompt);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                String cleanResponse = llmResponse.trim().toLowerCase();
                return cleanResponse.equals("yes") || cleanResponse.equals("true") || cleanResponse.equals("array");
            }

            return false;

        } catch (Exception e) {
            log.debug("LLM array type decision failed for parameter '{}': {}",
                     parameterInfo.getName(), e.getMessage());
            return false;
        }
    }

    /**
     * Build prompt for array type decision
     */
    private String buildArrayTypePrompt(ParameterInfo parameterInfo) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Determine if parameter '").append(parameterInfo.getName()).append("' ");
        prompt.append("(type: ").append(parameterInfo.getType()).append(") should be an array type ");
        prompt.append("based on OpenAPI schema conventions.\n\n");

        if (parameterInfo.getDescription() != null && !parameterInfo.getDescription().trim().isEmpty()) {
            prompt.append("Description: ").append(parameterInfo.getDescription()).append("\n\n");
        }

        prompt.append("Array type indicators:\n");
        prompt.append("- Parameter names ending with 's' (stations, distances)\n");
        prompt.append("- Parameter names containing 'list' (stationList, distanceList)\n");
        prompt.append("- Parameters that represent collections or multiple values\n\n");

        prompt.append("Respond with 'YES' if should be array, 'NO' if should be string/primitive.");

        return prompt.toString();
    }

    /**
     * Ask LLM for array type decision
     */
    private String askLLMForArrayTypeDecisionCall(String prompt) {
        try {
            String systemContent = "You are an OpenAPI schema expert. Determine if parameters should be array types based on naming conventions and semantic meaning.";

            String result = llmService.generateText(systemContent, prompt, 10, 0.1);

            if (result != null && !result.trim().isEmpty()) {
                return result.trim();
            }

            return null;

        } catch (Exception e) {
            log.debug("LLM array type decision call failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Format value as array - FIXED to avoid string-wrapped JSON
     */
    private String formatAsArrayValue(String value, ParameterInfo parameterInfo) {
        if (value == null || value.trim().isEmpty()) {
            return "[]";
        }

        try {
            String trimmedValue = value.trim();

            // CRITICAL FIX: If value is already a JSON array string, parse and return properly
            if (trimmedValue.startsWith("[") && trimmedValue.endsWith("]")) {
                // Try to parse as JSON to validate it's a proper array
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode arrayNode = mapper.readTree(trimmedValue);
                    if (arrayNode.isArray()) {
                        return trimmedValue; // It's already a valid JSON array
                    }
                } catch (Exception e) {
                    log.debug("Value looks like array but isn't valid JSON: '{}'", trimmedValue);
                }
            }

            // CRITICAL FIX: Don't wrap strings that are already JSON in quotes
            // This was causing "seatClass": "[\"1\"]" instead of "seatClass": ["1"]
            if (trimmedValue.startsWith("\"[") && trimmedValue.endsWith("]\"")) {
                // Remove outer quotes from JSON array string
                return trimmedValue.substring(1, trimmedValue.length() - 1);
            }

            // Split comma-separated values into array
            String[] parts;
            if (trimmedValue.contains(",")) {
                parts = trimmedValue.split(",");
            } else {
                parts = new String[]{trimmedValue};
            }

            // Build JSON array
            StringBuilder arrayBuilder = new StringBuilder("[");
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) arrayBuilder.append(", ");
                String part = parts[i].trim();
                // Remove quotes if already present
                if (part.startsWith("\"") && part.endsWith("\"")) {
                    arrayBuilder.append(part);
                } else {
                    arrayBuilder.append("\"").append(part).append("\"");
                }
            }
            arrayBuilder.append("]");

            return arrayBuilder.toString();

        } catch (Exception e) {
            log.debug("Failed to format '{}' as array for parameter '{}': {}",
                     value, parameterInfo.getName(), e.getMessage());
            return "[\"" + value + "\"]"; // Fallback: single-element array
        }
    }

    /**
     * Check if parameter should be number type
     */
    private boolean shouldBeNumberType(ParameterInfo parameterInfo) {
        String paramType = parameterInfo.getType();
        if (paramType != null) {
            String lowerType = paramType.toLowerCase();
            return lowerType.contains("number") || lowerType.contains("integer") ||
                   lowerType.contains("int") || lowerType.contains("double") ||
                   lowerType.contains("float");
        }
        return false;
    }

    /**
     * Format value as number
     */
    private String formatAsNumberValue(String value, ParameterInfo parameterInfo) {
        if (value == null || value.trim().isEmpty()) {
            return "0";
        }

        try {
            // Remove non-numeric characters except decimal point and minus
            String cleanValue = value.replaceAll("[^0-9.-]", "");

            if (cleanValue.isEmpty()) {
                return "0";
            }

            // Try to parse as number to validate
            Double.parseDouble(cleanValue);
            return cleanValue;

        } catch (NumberFormatException e) {
            log.debug("Failed to format '{}' as number for parameter '{}': {}",
                     value, parameterInfo.getName(), e.getMessage());
            return "0"; // Fallback
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

            // Add authentication headers for schema discovery
            if (authManager.isConfigured()) {
                authManager.addAuthHeaders(conn);
                log.debug("🔐 Added authentication headers to schema discovery request");
            }

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
        log.warn("❌ DEPRECATED: askLLMForExtractionPath called - this should not happen!");
        log.warn("❌ The system should use direct value extraction instead of JSONPath discovery");
        log.warn("❌ Returning null to force fallback to direct extraction");
        return null;
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
        String systemContent =
                "You are an API testing assistant that helps identify which microservices " +
                "would most likely provide realistic data for given parameters. " +
                "Respond with a JSON array of service names in priority order. " +
                "Do NOT generate test values. Only return service names as a JSON array.";

        log.debug("[Service Discovery LLM] Using LLM service with model type: {}", llmService.getConfig().getModelType());
        log.debug("[Service Discovery LLM] User prompt: {}", prompt);

        try {
            String result = llmService.generateText(systemContent, prompt, 200, 0.7);

            if (result != null && !result.trim().isEmpty()) {
                log.debug("[Service Discovery LLM] Successfully generated content: {}", result);
                return result;
            } else {
                log.warn("[Service Discovery LLM] LLM service returned null or empty result");
                return null;
            }

        } catch (Exception e) {
            log.warn("[Service Discovery LLM] Failed to call LLM service: {}", e.getMessage());
            return null;
        }
    }

    /**
      * Call LLM directly for endpoint discovery with appropriate system prompt
      */
     private String callLLMForEndpointDiscovery(String prompt) {
         String systemContent =
                 "You are an API testing assistant that helps identify REST API endpoints " +
                 "within microservices that would provide data for given parameters. " +
                 "Respond with the most likely endpoint path (e.g., /api/v1/service/resource). " +
                 "Do NOT generate test values. Only return the endpoint path.";

         log.debug("[Endpoint Discovery LLM] Using LLM service with model type: {}", llmService.getConfig().getModelType());
         log.debug("[Endpoint Discovery LLM] User prompt: {}", prompt);

         try {
             String result = llmService.generateText(systemContent, prompt, 100, 0.3);

             if (result != null && !result.trim().isEmpty()) {
                 log.debug("[Endpoint Discovery LLM] Successfully generated content: {}", result);
                 return result;
             } else {
                 log.warn("[Endpoint Discovery LLM] LLM service returned null or empty result");
                 return null;
             }

         } catch (Exception e) {
             log.warn("[Endpoint Discovery LLM] Failed to call LLM service: {}", e.getMessage());
             return null;
         }
     }

     /**
       * DEPRECATED: Old JSONPath discovery method - replaced by direct value extraction
       * This method is disabled to prevent JSONPath expressions from being returned
       */
      private String callLLMForExtractionPathDiscovery(String prompt) {
          log.warn("❌ DEPRECATED: callLLMForExtractionPathDiscovery called - this should not happen!");
          log.warn("❌ The system should use direct value extraction instead of JSONPath discovery");
          log.warn("❌ Returning null to force fallback to direct extraction");
          return null;
      }

      /**
       * Call LLM for direct value extraction from API response
       */
      private String callLLMForDirectValueExtractionFromResponse(String prompt) {
          String systemContent =
                  "You are an API testing assistant that extracts specific parameter values from JSON API responses. " +
                  "Given a JSON response and a parameter description, extract the most appropriate actual value from the response. " +
                  "CRITICAL RULES:\n" +
                  "1. Return ONLY the extracted value, not explanations or descriptions\n" +
                  "2. Do NOT return JSONPath expressions like $.data[*].name\n" +
                  "3. Do NOT return explanatory text like 'The format appears to be...'\n" +
                  "4. Do NOT return generic words like 'objects', 'data', 'items'\n" +
                  "5. For IDs: return actual ID values like 'route123' or 'abc-def-123'\n" +
                  "6. For names: return actual names like 'Shanghai' or 'Beijing'\n" +
                  "7. For numbers: return actual numbers like '100' or '25.5'\n" +
                  "8. If no suitable value exists, return 'NO_GOOD_MATCH'\n" +
                  "Examples: 'Shanghai', 'route123', '25.5', 'G1234' - NOT 'delivery route)', 'objects', 'The format appears to be UUID'";

          log.debug("[Direct Value Extraction LLM] Using LLM service with model type: {}", llmService.getConfig().getModelType());
          log.debug("[Direct Value Extraction LLM] User prompt: {}", prompt);

          try {
              String result = llmService.generateText(systemContent, prompt, 100, 0.3);

              if (result != null && !result.trim().isEmpty()) {
                  log.debug("[Direct Value Extraction LLM] Successfully generated content: {}", result);
                  return result;
              } else {
                  log.warn("[Direct Value Extraction LLM] LLM service returned null or empty result");
                  return null;
              }

          } catch (Exception e) {
              log.warn("[Direct Value Extraction LLM] Failed to call LLM service: {}", e.getMessage());
              return null;
          }
      }

      /**
       * Generate a meaningful value using LLM when API extraction fails
       */
      private String generateValueWithLLM(ParameterInfo parameterInfo) {
          try {
              log.info("🧠 Asking LLM to generate meaningful value for parameter '{}'", parameterInfo.getName());

              // Build prompt for value generation
              String prompt = buildValueGenerationPrompt(parameterInfo);

              if (prompt.length() > 2044) {
                  log.warn("Value generation prompt too long ({} chars), using simple generation", prompt.length());
                  return generateSimpleValue(parameterInfo);
              }

              // Ask LLM to generate a meaningful value
              String llmResponse = askLLMForValueGeneration(prompt);

              if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                  String cleanResponse = llmResponse.trim();

                  // Clean up the response (remove quotes, extra whitespace)
                  if (cleanResponse.startsWith("\"") && cleanResponse.endsWith("\"")) {
                      cleanResponse = cleanResponse.substring(1, cleanResponse.length() - 1);
                  }

                  // Validate that it's not a JSONPath expression
                  if (cleanResponse.startsWith("$.") || cleanResponse.contains("$[") || cleanResponse.contains("data[")) {
                      log.warn("❌ LLM returned JSONPath expression '{}' instead of actual value for parameter '{}'",
                               cleanResponse, parameterInfo.getName());
                      return generateSimpleValue(parameterInfo);
                  }

                  log.info("✅ LLM generated meaningful value '{}' for parameter '{}'", cleanResponse, parameterInfo.getName());
                  return cleanResponse;
              }

              log.warn("LLM value generation returned null/empty for parameter '{}'", parameterInfo.getName());
              return generateSimpleValue(parameterInfo);

          } catch (Exception e) {
              log.warn("LLM value generation failed for parameter '{}': {}", parameterInfo.getName(), e.getMessage());
              return generateSimpleValue(parameterInfo);
          }
      }

      /**
       * Build prompt for LLM value generation
       */
      private String buildValueGenerationPrompt(ParameterInfo parameterInfo) {
          StringBuilder prompt = new StringBuilder();

          prompt.append("Generate a realistic test value for the following parameter:\n\n");
          prompt.append("Parameter Name: ").append(parameterInfo.getName()).append("\n");
          prompt.append("Parameter Type: ").append(parameterInfo.getType()).append("\n");

          if (parameterInfo.getDescription() != null && !parameterInfo.getDescription().trim().isEmpty()
              && !parameterInfo.getDescription().equals("null")) {
              prompt.append("Description: ").append(parameterInfo.getDescription()).append("\n");
          }

          prompt.append("\nBased on the parameter name and type, generate a realistic test value.\n");
          prompt.append("Examples:\n");
          prompt.append("- For 'endStation' (string): 'Shanghai' or 'Beijing' or 'New York'\n");
          prompt.append("- For 'startStation' (string): 'Tokyo' or 'London' or 'Paris'\n");
          prompt.append("- For 'userId' (string): 'user123' or 'john.doe'\n");
          prompt.append("- For 'trainNumber' (string): 'G1237' or 'D2468'\n");
          prompt.append("- For 'price' (number): '150.50' or '89.99'\n");
          prompt.append("- For 'distance' (number): '350' or '1200'\n");
          prompt.append("- For 'date' (string): '2024-12-25' or '2024-01-15'\n");
          prompt.append("- For 'time' (string): '14:30' or '09:15'\n");

          prompt.append("\nRespond with ONLY the generated value (e.g., 'Shanghai' or '150.50' or 'G1237').\n");
          prompt.append("Do NOT include quotes, explanations, or JSONPath expressions.\n");
          prompt.append("If you cannot generate a suitable value: NO_GOOD_MATCH");

          return prompt.toString();
      }

      /**
       * Ask LLM to generate a meaningful value
       */
      private String askLLMForValueGeneration(String prompt) {
          try {
              // Use a simple system prompt for value generation
              String systemContent = "You are a test data generator. Generate realistic test values based on parameter information. " +
                                    "Return only the actual value, never JSONPath expressions or explanations.";

              String result = llmService.generateText(systemContent, prompt, 50, 0.7);

              if (result != null && !result.trim().isEmpty()) {
                  log.debug("[Value Generation LLM] Successfully generated value: {}", result);
                  return result;
              } else {
                  log.warn("[Value Generation LLM] LLM service returned null or empty result");
                  return null;
              }

          } catch (Exception e) {
              log.warn("[Value Generation LLM] Failed to call LLM service: {}", e.getMessage());
              return null;
          }
      }

      /**
       * Generate simple value based on parameter type and name when LLM fails
       */
      private String generateSimpleValue(ParameterInfo parameterInfo) {
          String paramName = parameterInfo.getName().toLowerCase();
          String paramType = parameterInfo.getType();

          // Generate based on parameter name patterns
          if (paramName.contains("station")) {
              return "Shanghai";
          } else if (paramName.contains("user") || paramName.contains("login")) {
              return "testuser123";
          } else if (paramName.contains("train") || paramName.contains("number")) {
              return "G1237";
          } else if (paramName.contains("date")) {
              return "2024-12-25";
          } else if (paramName.contains("time")) {
              return "14:30";
          } else if (paramName.contains("price") || paramName.contains("cost")) {
              return "150.50";
          } else if (paramName.contains("distance")) {
              return "350";
          } else if (paramName.contains("id")) {
              return "test123";
          } else if ("number".equals(paramType) || "integer".equals(paramType)) {
              return "100";
          } else {
              return "testvalue";
          }
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
        // Pure LLM-based endpoint selection (GET endpoints only)
        if (openAPIDiscovery != null && openAPIDiscovery.isLoaded()) {
            List<OpenAPIEndpointDiscovery.EndpointInfo> allEndpoints = openAPIDiscovery.getEndpointsForService(service);

            // Filter to only GET endpoints for data fetching
            List<OpenAPIEndpointDiscovery.EndpointInfo> getEndpoints = allEndpoints.stream()
                    .filter(endpoint -> "GET".equalsIgnoreCase(endpoint.getMethod()))
                    .collect(Collectors.toList());

            if (!getEndpoints.isEmpty()) {
                log.info("🔍 Found {} GET endpoints for service '{}' (filtered from {} total)",
                        getEndpoints.size(), service, allEndpoints.size());

                // Use LLM to select the best GET endpoint (with retries for reliability)
                String selectedEndpoint = selectEndpointWithLLMRetry(getEndpoints, parameterInfo, service);
                if (selectedEndpoint != null) {
                    log.info("🧠 LLM selected GET endpoint '{}' for parameter '{}' in service '{}'",
                            selectedEndpoint, parameterInfo.getName(), service);
                    return selectedEndpoint;
                }

                // If LLM completely fails after retries, log error and use first reasonable endpoint
                log.error("❌ LLM endpoint selection failed completely for parameter '{}' in service '{}', using first reasonable endpoint",
                         parameterInfo.getName(), service);
                String fallbackEndpoint = pickFirstReasonableEndpoint(getEndpoints);
                log.info("🔧 Emergency fallback: using GET endpoint '{}' for parameter '{}'",
                        fallbackEndpoint, parameterInfo.getName());
                return fallbackEndpoint;
            } else {
                log.warn("⚠️ No GET endpoints found for service '{}' (found {} non-GET endpoints)",
                        service, allEndpoints.size());
            }
        }

        // Final fallback: create a reasonable endpoint based on service name
        String fallbackEndpoint = "/api/v1/" + service.toLowerCase().replace("ts-", "").replace("-service", "") + "/query";
        log.info("🔧 Using generated endpoint '{}' for service '{}' and parameter '{}'",
                fallbackEndpoint, service, parameterInfo.getName());
        return fallbackEndpoint;
     }

     /**
      * Pick the first reasonable endpoint (avoid welcome, health, etc.)
      */
     private String pickFirstReasonableEndpoint(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints) {
         // First try: avoid utility endpoints
         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             String path = endpoint.getPath().toLowerCase();
             if (!path.contains("welcome") && !path.contains("health") &&
                 !path.contains("status") && !path.contains("info")) {
                 return endpoint.getPath();
             }
         }

         // Last resort: use first endpoint
         return endpoints.get(0).getPath();
     }

     /**
      * Use LLM to select the best endpoint for a parameter (with retries for reliability)
      */
     private String selectEndpointWithLLMRetry(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo, String serviceName) {
         int maxRetries = 3;

         for (int attempt = 1; attempt <= maxRetries; attempt++) {
             log.debug("🔄 LLM endpoint selection attempt {} of {} for parameter '{}'",
                      attempt, maxRetries, parameterInfo.getName());

             String result = selectEndpointWithLLM(endpoints, parameterInfo, serviceName);

             if (result != null && !result.equals("NO_GOOD_MATCH")) {
                 log.info("✅ LLM endpoint selection succeeded on attempt {} for parameter '{}'",
                         attempt, parameterInfo.getName());
                 return result;
             }

             if (result != null && result.equals("NO_GOOD_MATCH")) {
                 log.info("🤔 LLM said NO_GOOD_MATCH on attempt {} for parameter '{}' - forcing selection",
                         attempt, parameterInfo.getName());
                 // Force LLM to pick something by modifying the prompt
                 String forcedResult = forceEndpointSelectionWithLLM(endpoints, parameterInfo, serviceName);
                 if (forcedResult != null) {
                     return forcedResult;
                 }
             }

             log.warn("⚠️ LLM endpoint selection attempt {} failed for parameter '{}', retrying...",
                     attempt, parameterInfo.getName());
         }

         log.error("❌ All {} LLM endpoint selection attempts failed for parameter '{}'",
                  maxRetries, parameterInfo.getName());
         return null;
     }

     /**
      * Use LLM to select the best endpoint for a parameter (single attempt)
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
                 log.info("🧠 LLM raw response for parameter '{}': '{}'", parameterInfo.getName(), cleanResponse);

                 // Check for NO_GOOD_MATCH
                 if (cleanResponse.equals("NO_GOOD_MATCH")) {
                     log.info("LLM indicated no good endpoint match for parameter '{}'", parameterInfo.getName());
                     return "NO_GOOD_MATCH";
                 }

                 // Clean up the response (remove quotes, extra whitespace)
                 if (cleanResponse.startsWith("\"") && cleanResponse.endsWith("\"")) {
                     cleanResponse = cleanResponse.substring(1, cleanResponse.length() - 1);
                 }

                 // Validate that the selected endpoint exists (flexible matching)
                 String selectedEndpoint = validateAndNormalizeEndpoint(cleanResponse, endpoints, parameterInfo.getName());
                 if (selectedEndpoint != null) {
                     log.info("✅ LLM endpoint validation successful: '{}' for parameter '{}'", selectedEndpoint, parameterInfo.getName());
                     return selectedEndpoint;
                 }

                 log.warn("❌ LLM selected invalid endpoint '{}' for parameter '{}', available endpoints: {}",
                         cleanResponse, parameterInfo.getName(),
                         endpoints.stream().map(e -> e.getPath()).collect(Collectors.toList()));
             }

         } catch (Exception e) {
             log.debug("LLM endpoint selection failed: {}", e.getMessage());
         }

         return null; // Fallback to scoring
     }

     /**
      * Validate and normalize the LLM's endpoint selection with flexible matching
      */
     private String validateAndNormalizeEndpoint(String llmResponse, List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, String parameterName) {
         if (llmResponse == null || llmResponse.trim().isEmpty()) {
             return null;
         }

         String cleanResponse = llmResponse.trim();
         log.debug("🔍 Validating LLM endpoint selection '{}' against {} available endpoints", cleanResponse, endpoints.size());

         // 1. Exact match (most common case) - but only for GET endpoints
         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             if (endpoint.getPath().equals(cleanResponse)) {
                 if ("GET".equalsIgnoreCase(endpoint.getMethod())) {
                     log.debug("✅ Exact GET match found: '{}'", cleanResponse);
                     return cleanResponse;
                 } else {
                     log.warn("❌ LLM suggested '{}' but it's {} method, not GET. Rejecting.", cleanResponse, endpoint.getMethod());
                     return null; // Reject non-GET endpoints immediately
                 }
             }
         }

         // 2. Case-insensitive match
         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             if (endpoint.getPath().equalsIgnoreCase(cleanResponse)) {
                 log.debug("✅ Case-insensitive match found: '{}' -> '{}'", cleanResponse, endpoint.getPath());
                 return endpoint.getPath();
             }
         }

         // 3. Partial match - LLM might have returned just the endpoint name without full path
         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             String fullPath = endpoint.getPath();
             // Check if the LLM response is contained in the full path
             if (fullPath.contains(cleanResponse)) {
                 log.debug("✅ Partial match found: '{}' contained in '{}'", cleanResponse, fullPath);
                 return fullPath;
             }
             // Check if the full path ends with the LLM response
             if (fullPath.endsWith(cleanResponse)) {
                 log.debug("✅ Suffix match found: '{}' is suffix of '{}'", cleanResponse, fullPath);
                 return fullPath;
             }
         }

         // 4. Reverse partial match - full path might be contained in LLM response
         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             String fullPath = endpoint.getPath();
             if (cleanResponse.contains(fullPath)) {
                 log.debug("✅ Reverse partial match found: '{}' contains '{}'", cleanResponse, fullPath);
                 return fullPath;
             }
         }

         // 5. Fuzzy matching - remove common prefixes/suffixes and try again
         String normalizedResponse = normalizeEndpointPath(cleanResponse);
         for (OpenAPIEndpointDiscovery.EndpointInfo endpoint : endpoints) {
             String normalizedEndpoint = normalizeEndpointPath(endpoint.getPath());
             if (normalizedEndpoint.equals(normalizedResponse)) {
                 log.debug("✅ Fuzzy match found: '{}' -> '{}' (normalized: '{}' = '{}')",
                          cleanResponse, endpoint.getPath(), normalizedResponse, normalizedEndpoint);
                 return endpoint.getPath();
             }
         }

         log.debug("❌ No match found for '{}' in available endpoints: {}",
                  cleanResponse, endpoints.stream().map(e -> e.getPath()).collect(Collectors.toList()));
         return null;
     }

     /**
      * Normalize endpoint path for fuzzy matching
      */
     private String normalizeEndpointPath(String path) {
         if (path == null) return "";

         String normalized = path.toLowerCase().trim();

         // Remove common prefixes
         if (normalized.startsWith("/api/v1/")) {
             normalized = normalized.substring(8);
         } else if (normalized.startsWith("/api/")) {
             normalized = normalized.substring(5);
         } else if (normalized.startsWith("/")) {
             normalized = normalized.substring(1);
         }

         // Remove common suffixes
         if (normalized.endsWith("/")) {
             normalized = normalized.substring(0, normalized.length() - 1);
         }

         return normalized;
     }

     /**
      * Force LLM to select an endpoint when it initially says NO_GOOD_MATCH
      */
     private String forceEndpointSelectionWithLLM(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo, String serviceName) {
         try {
             // Build more aggressive prompt that forces selection
             String prompt = buildForcedEndpointSelectionPrompt(endpoints, parameterInfo, serviceName);

             if (prompt.length() > 2044) {
                 log.warn("Forced endpoint selection prompt too long ({} chars), skipping", prompt.length());
                 return null;
             }

             // Ask LLM with forced selection prompt
             String llmResponse = askLLMForEndpointSelection(prompt);

             if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                 String cleanResponse = llmResponse.trim();

                 // Don't accept NO_GOOD_MATCH this time
                 if (cleanResponse.equals("NO_GOOD_MATCH")) {
                     log.warn("LLM still said NO_GOOD_MATCH even with forced prompt for parameter '{}'", parameterInfo.getName());
                     return null;
                 }

                 // Clean up the response (remove quotes, extra whitespace)
                 if (cleanResponse.startsWith("\"") && cleanResponse.endsWith("\"")) {
                     cleanResponse = cleanResponse.substring(1, cleanResponse.length() - 1);
                 }

                 // Validate that the selected endpoint exists (flexible matching)
                 String selectedEndpoint = validateAndNormalizeEndpoint(cleanResponse, endpoints, parameterInfo.getName());
                 if (selectedEndpoint != null) {
                     log.info("🎯 LLM forced selection successful: '{}' for parameter '{}'", selectedEndpoint, parameterInfo.getName());
                     return selectedEndpoint;
                 }

                 log.warn("❌ LLM forced selection '{}' is invalid for parameter '{}', available endpoints: {}",
                         cleanResponse, parameterInfo.getName(),
                         endpoints.stream().map(e -> e.getPath()).collect(Collectors.toList()));
             }

         } catch (Exception e) {
             log.debug("Forced LLM endpoint selection failed: {}", e.getMessage());
         }

         return null;
     }

     /**
      * Build prompt for LLM endpoint selection
      */
     private String buildEndpointSelectionPrompt(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo, String serviceName) {
         StringBuilder prompt = new StringBuilder();
         prompt.append("Service: ").append(serviceName).append("\n");
         prompt.append("Parameter: ").append(parameterInfo.getName()).append(" (type: ").append(parameterInfo.getType()).append(")\n");
         prompt.append("Description: ").append(parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "").append("\n\n");

         prompt.append("Available GET endpoints (ONLY GET methods for data fetching - DO NOT suggest POST/PUT/DELETE):\n");
         int shownCount = 0;
         for (int i = 0; i < endpoints.size() && shownCount < 10; i++) {
             OpenAPIEndpointDiscovery.EndpointInfo endpoint = endpoints.get(i);
             // Only show GET endpoints (should already be filtered, but double-check)
             if ("GET".equalsIgnoreCase(endpoint.getMethod())) {
                 shownCount++;
                 prompt.append("- GET ").append(endpoint.getPath()).append("\n");
             }
         }

         prompt.append("\nTask: Select the BEST GET endpoint to fetch data for this parameter.\n");
         prompt.append("IMPORTANT: You MUST choose from the GET endpoints listed above ONLY.\n");
         prompt.append("DO NOT suggest endpoints that are not in the list above.\n");
         prompt.append("DO NOT suggest POST, PUT, DELETE, or PATCH endpoints.\n\n");
         prompt.append("Guidelines:\n");
         prompt.append("- For 'list' parameters: prefer GET endpoints returning collections (no path params)\n");
         prompt.append("- For 'id' parameters: prefer GET endpoints that return lists (can extract IDs)\n");
         prompt.append("- For 'name' parameters: prefer GET endpoints returning entity details\n");
         prompt.append("- Avoid utility endpoints (welcome, health, status)\n\n");
         prompt.append("Respond with ONLY the endpoint path from the list above (e.g., /api/v1/service/resource)\n");
         prompt.append("If NO GET endpoint from the list above is suitable, respond with: NO_GOOD_MATCH");

         return prompt.toString();
     }






     /**
      * Build forced prompt that doesn't allow NO_GOOD_MATCH
      */
     private String buildForcedEndpointSelectionPrompt(List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints, ParameterInfo parameterInfo, String serviceName) {
         StringBuilder prompt = new StringBuilder();
         prompt.append("Service: ").append(serviceName).append("\n");
         prompt.append("Parameter: ").append(parameterInfo.getName()).append(" (type: ").append(parameterInfo.getType()).append(")\n");
         prompt.append("Description: ").append(parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "").append("\n\n");

         prompt.append("Available GET endpoints (for data fetching):\n");
         for (int i = 0; i < Math.min(10, endpoints.size()); i++) {
             OpenAPIEndpointDiscovery.EndpointInfo endpoint = endpoints.get(i);
             // Only show GET endpoints (should already be filtered, but double-check)
             if ("GET".equalsIgnoreCase(endpoint.getMethod())) {
                 prompt.append("- GET ").append(endpoint.getPath()).append("\n");
             }
         }

         prompt.append("\nTask: You MUST select one of the GET endpoints above for this parameter.\n");
         prompt.append("Even if none seem perfect, choose the most reasonable one.\n");
         prompt.append("We need to fetch some data for test generation.\n\n");
         prompt.append("Guidelines:\n");
         prompt.append("- For 'list' parameters: prefer endpoints returning collections (no path params)\n");
         prompt.append("- For 'id' parameters: prefer endpoints that return lists (can extract IDs)\n");
         prompt.append("- For 'name' parameters: prefer endpoints returning entity details\n");
         prompt.append("- If unsure, pick the first non-utility endpoint\n\n");
         prompt.append("Respond with ONLY the endpoint path (e.g., /api/v1/service/resource)\n");
         prompt.append("DO NOT respond with NO_GOOD_MATCH - you must pick one endpoint.");

         return prompt.toString();
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