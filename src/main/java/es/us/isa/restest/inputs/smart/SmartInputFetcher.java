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
            log.debug("Attempting smart fetch for parameter '{}' (random: {:.3f} < {:.1f}%)", 
                     parameterInfo.getName(), randomValue, config.getSmartFetchPercentage() * 100);
            try {
                String result = fetchFromSmartSource(parameterInfo);
                if (result != null) {
                    log.info("Smart Fetch → {} = {} ✅", parameterInfo.getName(), result);
                    return result;
                } else {
                    log.info("Smart Fetch → {} = FAILED, falling back to LLM", parameterInfo.getName());
                    return fallbackToLLM(parameterInfo);
                }
            } catch (Exception e) {
                log.info("Smart Fetch → {} = ERROR ({}), falling back to LLM", parameterInfo.getName(), e.getMessage());
                return fallbackToLLM(parameterInfo);
            }
        } else {
            // Use traditional LLM generation (e.g., 10% of the time if percentage = 0.9)
            log.debug("Using LLM generation for parameter '{}' (random: {:.3f} >= {:.1f}%)", 
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
        
        // If no mappings found, try to discover new ones
        if (mappings.isEmpty() && config.isLlmDiscoveryEnabled()) {
            log.debug("No existing mappings for '{}', attempting discovery", paramName);
            mappings = discoverApiMappings(parameterInfo);
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
        
        try {
            // 1. Pattern-based discovery
            discoveries.addAll(discoverByPatterns(parameterInfo));
            
            // 2. LLM-based discovery
            if (config.isLlmDiscoveryEnabled()) {
                discoveries.addAll(discoverByLLM(parameterInfo));
            }
            
            // 3. Save discovered mappings to registry
            for (ApiMapping mapping : discoveries) {
                registry.addMapping(parameterInfo.getName(), mapping);
            }
            
            if (!discoveries.isEmpty()) {
                saveRegistry(); // Persist learned mappings
            }
            
        } catch (Exception e) {
            log.warn("Discovery failed for parameter '{}': {}", parameterInfo.getName(), e.getMessage());
        }
        
        return discoveries;
    }
    
    /**
     * Discover APIs using pattern matching
     */
    private List<ApiMapping> discoverByPatterns(ParameterInfo parameterInfo) {
        List<ApiMapping> mappings = new ArrayList<>();
        String paramName = parameterInfo.getName();
        
        for (ServicePattern pattern : registry.getServicePatterns()) {
            if (Pattern.matches(pattern.getPattern(), paramName)) {
                for (String endpoint : pattern.getEndpoints()) {
                    for (String service : pattern.getServices()) {
                        // Create a basic mapping - could be improved with schema analysis
                        String extractPath = guessExtractPath(parameterInfo, endpoint);
                        ApiMapping mapping = new ApiMapping(endpoint, service, extractPath);
                        mapping.setPriority(config.getPatternDiscoveryPriority());
                        mappings.add(mapping);
                        
                        log.debug("Pattern-discovered mapping: {} -> {}", paramName, mapping);
                    }
                }
            }
        }
        
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
            
            // Create mappings for suggested services
            for (String service : suggestedServices) {
                String endpoint = inferEndpointForService(service, parameterInfo);
                if (endpoint != null) {
                    String extractPath = guessExtractPath(parameterInfo, endpoint);
                    ApiMapping mapping = new ApiMapping(endpoint, service, extractPath);
                    mapping.setPriority(config.getLlmDiscoveryPriority());
                    mappings.add(mapping);
                    
                    log.debug("LLM-discovered mapping: {} -> {}", parameterInfo.getName(), mapping);
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
        
        log.debug("Fetching data from: {}", url);
        
        // Make HTTP request
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(mapping.getMethod());
        conn.setRequestProperty("Content-Type", config.getDefaultContentType());
        conn.setConnectTimeout((int) config.getDiscoveryTimeoutMs());
        conn.setReadTimeout((int) config.getDiscoveryTimeoutMs());

        try {
            int responseCode = conn.getResponseCode();
            if (responseCode != config.getSuccessResponseCode()) {
                throw new Exception("HTTP " + responseCode + " from " + url);
            }
            
            // Read response
            String responseBody = readResponse(conn);
            
            // Extract value using JSONPath
            return extractValueFromResponse(responseBody, mapping.getExtractPath(), parameterInfo);
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Extract value from API response using JSONPath
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
                
                // Fallback: use simple logic if LLM fails
                if (result instanceof List) {
                    List<?> list = (List<?>) result;
                    if (!list.isEmpty()) {
                        Object value = list.get(0); // Take first element as fallback
                        return value != null ? value.toString() : null;
                    }
                } else {
                    return result.toString();
                }
            }
            
            return null;
            
        } catch (PathNotFoundException e) {
            log.debug("JSONPath '{}' not found in response", extractPath);
            return null;
        } catch (Exception e) {
            log.warn("Failed to extract value using path '{}': {}", extractPath, e.getMessage());
            return null;
        }
    }
    
    private String selectValueWithLLM(Object extractedData, ParameterInfo parameterInfo) {
        try {
            // Convert extracted data to a readable format for LLM
            String dataString = objectMapper.writeValueAsString(extractedData);
            
            // Build prompt for value selection
            String prompt = buildValueSelectionPrompt(dataString, parameterInfo);
            String llmResponse = askLLMForValueSelection(prompt);
            
            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                log.debug("LLM selected value '{}' for parameter '{}'", llmResponse, parameterInfo.getName());
                return llmResponse.trim();
            }
        } catch (Exception e) {
            log.debug("LLM value selection failed for parameter '{}': {}", 
                     parameterInfo.getName(), e.getMessage());
        }
        return null;
    }
    
    private String buildValueSelectionPrompt(String extractedData, ParameterInfo parameterInfo) {
        String template = registry.getLlmPrompts().get("valueSelection");
        return template
                .replace("{extractedData}", extractedData != null ? extractedData : "")
                .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                .replace("{parameterDescription}", parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "");
    }
    
    private String askLLMForValueSelection(String prompt) {
        try {
            // Call LLM directly for value selection, not parameter generation
            String rawResponse = callLLMForValueSelection(prompt);
            
            if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                // Clean any markdown formatting
                String cleaned = cleanJsonFromMarkdown(rawResponse);
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
                // Use LLM to determine the best JSONPath for extraction
                String prompt = buildDataExtractionPrompt(parameterInfo, responseSchema);
                String llmResponse = askLLMForExtractionPath(prompt);
                
                if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                    log.debug("LLM suggested JSONPath '{}' for parameter '{}'", llmResponse, parameterInfo.getName());
                    return llmResponse.trim();
                }
            }
        } catch (Exception e) {
            log.debug("LLM extraction path discovery failed for parameter '{}': {}", 
                     parameterInfo.getName(), e.getMessage());
        }
        
        // Fallback: return a generic path
        String fallbackPath = "$.data[*]";
        log.debug("Using fallback JSONPath '{}' for parameter '{}'", fallbackPath, parameterInfo.getName());
        return fallbackPath;
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
        return template
                .replace("{responseSchema}", responseSchema != null ? responseSchema : "")
                .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "");
    }
    
    private String askLLMForExtractionPath(String prompt) {
        try {
            // Call LLM directly for extraction path discovery with appropriate system prompt
            String rawResponse = callLLMForExtractionPathDiscovery(prompt);
            
            if (rawResponse != null && !rawResponse.trim().isEmpty()) {
                // Clean any markdown formatting and return the JSONPath
                String cleaned = cleanJsonFromMarkdown(rawResponse);
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
                    if (jsonResponse.isArray()) {
                        List<String> services = new ArrayList<>();
                        jsonResponse.forEach(node -> services.add(node.asText()));
                        log.debug("LLM suggested {} services: {}", services.size(), services);
                        return services;
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
        // First try OpenAPI discovery
        if (openAPIDiscovery != null && openAPIDiscovery.isLoaded()) {
            List<OpenAPIEndpointDiscovery.EndpointInfo> endpoints = openAPIDiscovery.getEndpointsForService(service);
            if (!endpoints.isEmpty()) {
                // Use the first endpoint or apply some selection logic
                String endpoint = endpoints.get(0).getPath();
                log.debug("OpenAPI discovered endpoint '{}' for service '{}'", endpoint, service);
                return endpoint;
            }
        }
        
        // Fallback to LLM discovery if OpenAPI doesn't have the service
        try {
            // Use LLM to discover the most appropriate endpoint for this service and parameter
            String prompt = buildEndpointDiscoveryPrompt(service, parameterInfo);
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
     
     private String buildEndpointDiscoveryPrompt(String serviceName, ParameterInfo parameterInfo) {
         String template = registry.getLlmPrompts().get("endpointDiscovery");
         
         // Truncate description if too long to prevent message length issues
         String description = parameterInfo.getDescription() != null ? parameterInfo.getDescription() : "";
         if (description.length() > 500) {
             description = description.substring(0, 497) + "...";
         }
         
         String prompt = template
                 .replace("{serviceName}", serviceName != null ? serviceName : "")
                 .replace("{parameterName}", parameterInfo.getName() != null ? parameterInfo.getName() : "")
                 .replace("{parameterType}", parameterInfo.getType() != null ? parameterInfo.getType() : "")
                 .replace("{parameterDescription}", description);
         
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
                 return cleanJsonFromMarkdown(rawResponse);
             }
             
             return null;
             
         } catch (Exception e) {
             log.warn("Failed to ask LLM for endpoint: {}", e.getMessage());
             return null;
         }
     }
     
     private String parseEndpointFromLLMResponse(String llmResponse) {
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
 }