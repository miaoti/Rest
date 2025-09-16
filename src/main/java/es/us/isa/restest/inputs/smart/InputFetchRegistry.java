package es.us.isa.restest.inputs.smart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Registry for managing input fetch mappings and service patterns
 * Handles loading, saving, and querying of parameter-to-API mappings
 */
public class InputFetchRegistry {
    
    private static final Logger log = LogManager.getLogger(InputFetchRegistry.class);
    
    private String version;
    private LocalDateTime lastUpdated;
    private Map<String, List<ApiMapping>> parameterMappings;
    private List<ServicePattern> servicePatterns;
    private Map<String, String> llmPrompts;
    private CacheConfig cacheConfig;
    private Map<String, Map<String, List<ParameterError>>> parameterErrors; // API endpoint -> parameter -> errors
    
    // Jackson mapper for YAML serialization
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory())
            .registerModule(new JavaTimeModule());
    
    public InputFetchRegistry() {
        this.version = "1.0";
        this.lastUpdated = LocalDateTime.now();
        this.parameterMappings = new HashMap<>();
        this.servicePatterns = new ArrayList<>();
        this.llmPrompts = new HashMap<>();
        this.cacheConfig = new CacheConfig();
        this.parameterErrors = new HashMap<>();
        
        initializeDefaults();
    }
    
    /**
     * Load registry from YAML file
     */
    public static InputFetchRegistry loadFromFile(File file) throws IOException {
        try {
            RegistryData data = yamlMapper.readValue(file, RegistryData.class);
            return data.toRegistry();
        } catch (IOException e) {
            log.error("Failed to load registry from {}: {}", file.getPath(), e.getMessage());
            throw e;
        }
    }
    
    /**
     * Save registry to YAML file
     */
    public void saveToFile(File file) throws IOException {
        try {
            this.lastUpdated = LocalDateTime.now();
            RegistryData data = RegistryData.fromRegistry(this);
            yamlMapper.writeValue(file, data);
            log.debug("Saved registry to {}", file.getPath());
        } catch (IOException e) {
            log.error("Failed to save registry to {}: {}", file.getPath(), e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get mappings for a specific parameter
     */
    public List<ApiMapping> getMappingsForParameter(String parameterName) {
        return parameterMappings.getOrDefault(parameterName, new ArrayList<>());
    }
    
    /**
     * Add a new mapping for a parameter
     */
    public void addMapping(String parameterName, ApiMapping mapping) {
        parameterMappings.computeIfAbsent(parameterName, k -> new ArrayList<>()).add(mapping);
        log.debug("Added mapping for parameter '{}': {}", parameterName, mapping);
    }
    
    /**
     * Remove a mapping for a parameter
     */
    public boolean removeMapping(String parameterName, ApiMapping mapping) {
        List<ApiMapping> mappings = parameterMappings.get(parameterName);
        if (mappings != null) {
            boolean removed = mappings.remove(mapping);
            if (mappings.isEmpty()) {
                parameterMappings.remove(parameterName);
            }
            return removed;
        }
        return false;
    }
    
    /**
     * Add a parameter error for specific API endpoint and parameter
     */
    public void addParameterError(String apiEndpoint, String parameterName, ParameterError error) {
        Map<String, List<ParameterError>> endpointErrors = parameterErrors.computeIfAbsent(apiEndpoint, k -> new HashMap<>());
        List<ParameterError> paramErrors = endpointErrors.computeIfAbsent(parameterName, k -> new ArrayList<>());
        
        // Check for semantic similarity to avoid redundant information
        ParameterError existingSimilar = findSemanticallyEquivalentError(paramErrors, error);
        
        if (existingSimilar == null) {
            // No similar error found, add the new one
            paramErrors.add(error);
            log.debug("Added new parameter error for {}.{}: {} - {}", 
                     apiEndpoint, parameterName, error.getErrorType(), 
                     truncateForLog(error.getErrorReason(), 50));
        } else {
            // Similar error exists, merge information if the new one is more detailed
            if (shouldUpdateExistingError(existingSimilar, error)) {
                mergeErrorInformation(existingSimilar, error);
                log.debug("Updated existing parameter error for {}.{}: {} with more details", 
                         apiEndpoint, parameterName, error.getErrorType());
            } else {
                log.debug("Skipped semantically similar parameter error for {}.{}: {}", 
                         apiEndpoint, parameterName, error.getErrorType());
            }
        }
    }
    
    /**
     * Get parameter errors for specific API endpoint and parameter
     */
    public List<ParameterError> getParameterErrors(String apiEndpoint, String parameterName) {
        return parameterErrors.getOrDefault(apiEndpoint, new HashMap<>())
                .getOrDefault(parameterName, new ArrayList<>());
    }
    
    /**
     * Get all parameter errors for an API endpoint
     */
    public Map<String, List<ParameterError>> getParameterErrorsForEndpoint(String apiEndpoint) {
        return parameterErrors.getOrDefault(apiEndpoint, new HashMap<>());
    }
    
    /**
     * Get error context for LLM parameter generation
     */
    public String getErrorContextForParameter(String apiEndpoint, String parameterName) {
        List<ParameterError> errors = getParameterErrors(apiEndpoint, parameterName);
        if (errors.isEmpty()) {
            return "";
        }
        
        StringBuilder context = new StringBuilder();
        context.append("⚠️ KNOWN ERROR PATTERNS for parameter '").append(parameterName).append("':\n");
        
        Set<String> uniqueErrorTypes = new HashSet<>();
        for (ParameterError error : errors) {
            String errorInfo = String.format("%s: %s", error.getErrorType(), error.getErrorReason());
            if (uniqueErrorTypes.add(errorInfo)) {
                context.append("- ").append(errorInfo).append("\n");
            }
        }
        
        context.append("Please generate values that avoid these known error patterns.\n");
        return context.toString();
    }
    
    /**
     * Get all service names from mappings and patterns
     */
    public List<String> getAllServices() {
        Set<String> services = new HashSet<>();
        
        // From parameter mappings
        parameterMappings.values().stream()
                .flatMap(List::stream)
                .map(ApiMapping::getService)
                .filter(Objects::nonNull)
                .forEach(services::add);
        
        // From service patterns
        servicePatterns.stream()
                .flatMap(pattern -> pattern.getServices().stream())
                .forEach(services::add);
        
        return new ArrayList<>(services);
    }
    
    /**
     * Initialize default patterns and prompts
     */
    private void initializeDefaults() {
        // Default service patterns
        servicePatterns.add(new ServicePattern(".*[Ss]tation.*", 
                Arrays.asList("ts-station-service"), 
                Arrays.asList("/api/v1/stationservice/stations")));
        
        servicePatterns.add(new ServicePattern(".*[Uu]ser.*", 
                Arrays.asList("ts-user-service", "ts-contacts-service"), 
                Arrays.asList("/api/v1/userservice/users", "/api/v1/contactservice/contacts")));
        
        servicePatterns.add(new ServicePattern(".*[Tt]rain.*", 
                Arrays.asList("ts-train-service", "ts-travel-service"), 
                Arrays.asList("/api/v1/trainservice/trains", "/api/v1/travelservice/trips")));
        
        servicePatterns.add(new ServicePattern(".*[Rr]oute.*", 
                Arrays.asList("ts-route-service", "ts-route-plan-service"), 
                Arrays.asList("/api/v1/routeservice/routes", "/api/v1/routeplanservice/routePlans")));
        
        servicePatterns.add(new ServicePattern(".*[Oo]rder.*", 
                Arrays.asList("ts-order-service", "ts-order-other-service"), 
                Arrays.asList("/api/v1/orderservice/orders", "/api/v1/orderOtherservice/orderOthers")));
        
        // Default LLM prompts
        llmPrompts.put("apiDiscovery", 
                "Parameter: {parameterName} (type: {parameterType}, location: {parameterLocation})\n" +
                "Description: {parameterDescription}\n\n" +
                "Services: {availableServices}\n\n" +
                "Task: Select the TOP 3 services most likely to provide realistic data for this parameter.\n" +
                "Consider semantic meaning and naming patterns.\n\n" +
                "If you find good matches, respond with a JSON array of 1-3 service names in priority order:\n" +
                "[\"service1\", \"service2\", \"service3\"]\n\n" +
                "If NO services seem suitable for this parameter, respond with:\n" +
                "NO_GOOD_MATCH\n\n" +
                "Respond ONLY with the JSON array OR 'NO_GOOD_MATCH', no explanations.");
        
        llmPrompts.put("directValueExtraction",
                "API Response: {responseSchema}\n\n" +
                "Target Parameter: {parameterName} (type: {parameterType})\n" +
                "Description: {parameterDescription}\n\n" +
                "Task: Extract or derive a suitable value for this parameter from the API response above.\n" +
                "You must use ONLY values that appear in the response - do not generate new values.\n\n" +
                "Guidelines:\n" +
                "- Look for exact field matches first\n" +
                "- Consider semantically related fields\n" +
                "- Use any reasonable value from the response\n" +
                "- For list parameters: you can combine multiple values with commas\n" +
                "- Ensure the returned value matches the parameter type\n\n" +
                "Examples:\n" +
                "- For 'stationName': use values from 'from', 'to', or similar fields\n" +
                "- For 'price': use values from 'price' or cost-related fields\n" +
                "- For 'id': use any ID field from the response\n" +
                "- For 'distanceList': use numeric values or station names that could represent distances\n\n" +
                "Respond with ONLY the extracted value (e.g., 'Shanghai' or '100.0' or 'G1237')\n" +
                "If no suitable value exists in the response: NO_GOOD_MATCH");
        
        llmPrompts.put("valueSelection",
                "Data: {extractedData}\n" +
                "Parameter: {parameterName} (type: {parameterType})\n" +
                "Description: {parameterDescription}\n\n" +
                "Task: Select the most suitable value for this parameter based on its name, type, and description.\n\n" +
                "If you find a suitable value, respond with just the value:\n" +
                "selectedValue\n\n" +
                "If NO value seems appropriate for this parameter, respond with:\n" +
                "NO_GOOD_MATCH");
    }
    
    // Getters and setters
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public Map<String, List<ApiMapping>> getParameterMappings() { return parameterMappings; }
    public void setParameterMappings(Map<String, List<ApiMapping>> parameterMappings) { 
        this.parameterMappings = parameterMappings; 
    }
    
    public List<ServicePattern> getServicePatterns() { return servicePatterns; }
    public void setServicePatterns(List<ServicePattern> servicePatterns) { 
        this.servicePatterns = servicePatterns; 
    }
    
    public Map<String, String> getLlmPrompts() { return llmPrompts; }
    public void setLlmPrompts(Map<String, String> llmPrompts) { this.llmPrompts = llmPrompts; }
    
    public CacheConfig getCacheConfig() { return cacheConfig; }
    public void setCacheConfig(CacheConfig cacheConfig) { this.cacheConfig = cacheConfig; }
    
    public Map<String, Map<String, List<ParameterError>>> getParameterErrors() { return parameterErrors; }
    public void setParameterErrors(Map<String, Map<String, List<ParameterError>>> parameterErrors) { 
        this.parameterErrors = parameterErrors; 
    }
    
    /**
     * Data transfer object for YAML serialization
     */
    private static class RegistryData {
        public String version;
        public LocalDateTime lastUpdated;
        public Map<String, List<ApiMappingData>> parameterMappings;
        public List<ServicePatternData> servicePatterns;
        public Map<String, String> llmPrompts;
        public CacheConfigData cache;
        public Map<String, Map<String, List<ParameterErrorData>>> parameterErrors;
        
        public static RegistryData fromRegistry(InputFetchRegistry registry) {
            RegistryData data = new RegistryData();
            data.version = registry.version;
            data.lastUpdated = registry.lastUpdated;
            data.llmPrompts = registry.llmPrompts;
            
            // Convert parameter mappings
            data.parameterMappings = new HashMap<>();
            for (Map.Entry<String, List<ApiMapping>> entry : registry.parameterMappings.entrySet()) {
                List<ApiMappingData> mappingDataList = entry.getValue().stream()
                        .map(ApiMappingData::fromApiMapping)
                        .collect(Collectors.toList());
                data.parameterMappings.put(entry.getKey(), mappingDataList);
            }
            
            // Convert service patterns
            data.servicePatterns = registry.servicePatterns.stream()
                    .map(ServicePatternData::fromServicePattern)
                    .collect(Collectors.toList());
            
            // Convert cache config
            data.cache = CacheConfigData.fromCacheConfig(registry.cacheConfig);
            
            // Convert parameter errors
            data.parameterErrors = new HashMap<>();
            for (Map.Entry<String, Map<String, List<ParameterError>>> endpointEntry : registry.parameterErrors.entrySet()) {
                Map<String, List<ParameterErrorData>> parameterErrorsData = new HashMap<>();
                for (Map.Entry<String, List<ParameterError>> paramEntry : endpointEntry.getValue().entrySet()) {
                    List<ParameterErrorData> errorDataList = paramEntry.getValue().stream()
                            .map(ParameterErrorData::fromParameterError)
                            .collect(Collectors.toList());
                    parameterErrorsData.put(paramEntry.getKey(), errorDataList);
                }
                data.parameterErrors.put(endpointEntry.getKey(), parameterErrorsData);
            }
            
            return data;
        }
        
        public InputFetchRegistry toRegistry() {
            InputFetchRegistry registry = new InputFetchRegistry();
            registry.version = this.version;
            registry.lastUpdated = this.lastUpdated;
            registry.llmPrompts = this.llmPrompts != null ? this.llmPrompts : new HashMap<>();
            
            // Convert parameter mappings
            registry.parameterMappings = new HashMap<>();
            if (this.parameterMappings != null) {
                for (Map.Entry<String, List<ApiMappingData>> entry : this.parameterMappings.entrySet()) {
                    List<ApiMapping> mappings = entry.getValue().stream()
                            .map(ApiMappingData::toApiMapping)
                            .collect(Collectors.toList());
                    registry.parameterMappings.put(entry.getKey(), mappings);
                }
            }
            
            // Convert service patterns
            if (this.servicePatterns != null) {
                registry.servicePatterns = this.servicePatterns.stream()
                        .map(ServicePatternData::toServicePattern)
                        .collect(Collectors.toList());
            }
            
            // Convert cache config
            if (this.cache != null) {
                registry.cacheConfig = this.cache.toCacheConfig();
            }
            
            // Convert parameter errors
            registry.parameterErrors = new HashMap<>();
            if (this.parameterErrors != null) {
                for (Map.Entry<String, Map<String, List<ParameterErrorData>>> endpointEntry : this.parameterErrors.entrySet()) {
                    Map<String, List<ParameterError>> parameterErrors = new HashMap<>();
                    for (Map.Entry<String, List<ParameterErrorData>> paramEntry : endpointEntry.getValue().entrySet()) {
                        List<ParameterError> errors = paramEntry.getValue().stream()
                                .map(data -> data.toParameterError())
                                .collect(Collectors.toList());
                        parameterErrors.put(paramEntry.getKey(), errors);
                    }
                    registry.parameterErrors.put(endpointEntry.getKey(), parameterErrors);
                }
            }
            
            return registry;
        }
    }
    
    // Data transfer classes for YAML serialization
    private static class ApiMappingData {
        public String endpoint;
        public String method;
        public String service;
        public String extractPath;
        public int priority;
        public LocalDateTime lastUsed;
        public double successRate;
        public String description;
        
        public static ApiMappingData fromApiMapping(ApiMapping mapping) {
            ApiMappingData data = new ApiMappingData();
            data.endpoint = mapping.getEndpoint();
            data.method = mapping.getMethod();
            data.service = mapping.getService();
            data.extractPath = mapping.getExtractPath();
            data.priority = mapping.getPriority();
            data.lastUsed = mapping.getLastUsed();
            data.successRate = mapping.getSuccessRate();
            data.description = mapping.getDescription();
            return data;
        }
        
        public ApiMapping toApiMapping() {
            ApiMapping mapping = new ApiMapping();
            mapping.setEndpoint(this.endpoint);
            mapping.setMethod(this.method);
            mapping.setService(this.service);
            mapping.setExtractPath(this.extractPath);
            mapping.setPriority(this.priority);
            mapping.setLastUsed(this.lastUsed);
            mapping.setSuccessRate(this.successRate);
            mapping.setDescription(this.description);
            return mapping;
        }
    }
    
    private static class ServicePatternData {
        public String pattern;
        public List<String> services;
        public List<String> endpoints;
        
        public static ServicePatternData fromServicePattern(ServicePattern pattern) {
            ServicePatternData data = new ServicePatternData();
            data.pattern = pattern.getPattern();
            data.services = pattern.getServices();
            data.endpoints = pattern.getEndpoints();
            return data;
        }
        
        public ServicePattern toServicePattern() {
            return new ServicePattern(this.pattern, this.services, this.endpoints);
        }
    }
    
    private static class CacheConfigData {
        public boolean enabled;
        public int maxEntries;
        public int ttlSeconds;
        
        public static CacheConfigData fromCacheConfig(CacheConfig config) {
            CacheConfigData data = new CacheConfigData();
            data.enabled = config.isEnabled();
            data.maxEntries = config.getMaxEntries();
            data.ttlSeconds = config.getTtlSeconds();
            return data;
        }
        
        public CacheConfig toCacheConfig() {
            return new CacheConfig(this.enabled, this.maxEntries, this.ttlSeconds);
        }
    }
    
    private static class ParameterErrorData {
        public String errorType;
        public String errorReason;
        public String apiEndpoint;
        public String parameterName;
        public LocalDateTime timestamp;
        public String additionalInfo;
        
        public static ParameterErrorData fromParameterError(ParameterError error) {
            ParameterErrorData data = new ParameterErrorData();
            data.errorType = error.getErrorType();
            data.errorReason = error.getErrorReason();
            data.apiEndpoint = error.getApiEndpoint();
            data.parameterName = error.getParameterName();
            data.timestamp = error.getTimestamp();
            data.additionalInfo = error.getAdditionalInfo();
            return data;
        }
        
        public ParameterError toParameterError() {
            return new ParameterError(this.errorType, this.errorReason, this.apiEndpoint, 
                                    this.parameterName, this.timestamp, this.additionalInfo);
        }
    }
    
    /**
     * Finds semantically equivalent error using text similarity algorithms
     */
    private ParameterError findSemanticallyEquivalentError(List<ParameterError> existingErrors, ParameterError newError) {
        if (existingErrors.isEmpty()) {
            return null;
        }
        
        // First check for exact matches (fast path)
        for (ParameterError existing : existingErrors) {
            if (existing.getErrorType().equals(newError.getErrorType()) && 
                existing.getErrorReason().equals(newError.getErrorReason())) {
                return existing;
            }
        }
        
        // Then check for semantic similarity (only if error types match)
        for (ParameterError existing : existingErrors) {
            if (existing.getErrorType().equals(newError.getErrorType()) && 
                areErrorReasonsSemanticallyEquivalent(existing.getErrorReason(), newError.getErrorReason())) {
                return existing;
            }
        }
        
        return null;
    }
    
    /**
     * Determines if two error reasons are semantically equivalent using multiple similarity metrics
     */
    private boolean areErrorReasonsSemanticallyEquivalent(String reason1, String reason2) {
        if (reason1 == null || reason2 == null) {
            return reason1 == reason2;
        }
        
        // Normalize both reasons for comparison
        String normalized1 = normalizeForComparison(reason1);
        String normalized2 = normalizeForComparison(reason2);
        
        // If normalized versions are identical, they're equivalent
        if (normalized1.equals(normalized2)) {
            return true;
        }
        
        // Calculate multiple similarity scores
        double jaccardSimilarity = calculateJaccardSimilarity(normalized1, normalized2);
        double levenshteinSimilarity = calculateLevenshteinSimilarity(normalized1, normalized2);
        double keywordOverlap = calculateKeywordOverlap(normalized1, normalized2);
        
        // Consider errors semantically equivalent if they meet similarity thresholds
        return jaccardSimilarity >= 0.75 || 
               levenshteinSimilarity >= 0.80 || 
               keywordOverlap >= 0.85;
    }
    
    /**
     * Normalizes error reason text for semantic comparison
     */
    private String normalizeForComparison(String text) {
        return text.toLowerCase()
                   .replaceAll("[^a-z0-9\\s]", " ")  // Remove punctuation
                   .replaceAll("\\b(the|a|an|is|are|was|were|this|that|these|those|it|its|caused?|resulting?|due|because)\\b", " ")  // Remove common words
                   .replaceAll("\\bjackson\\b", "parser")  // Normalize technical terms
                   .replaceAll("\\bdeseriali[sz]ation?\\b", "parsing")
                   .replaceAll("\\bmismatchedinputexception\\b", "type error")
                   .replaceAll("\\bapi\\s+expects?\\b", "expected")
                   .replaceAll("\\bprovided\\s+value\\b", "value")
                   .replaceAll("\\s+", " ")  // Normalize whitespace
                   .trim();
    }
    
    /**
     * Calculates Jaccard similarity between two text strings
     */
    private double calculateJaccardSimilarity(String text1, String text2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.split("\\s+")));
        
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    /**
     * Calculates Levenshtein similarity (normalized edit distance)
     */
    private double calculateLevenshteinSimilarity(String text1, String text2) {
        int distance = calculateLevenshteinDistance(text1, text2);
        int maxLength = Math.max(text1.length(), text2.length());
        return maxLength == 0 ? 1.0 : 1.0 - (double) distance / maxLength;
    }
    
    /**
     * Calculates Levenshtein distance between two strings
     */
    private int calculateLevenshteinDistance(String a, String b) {
        if (a.length() == 0) return b.length();
        if (b.length() == 0) return a.length();
        
        int[][] matrix = new int[a.length() + 1][b.length() + 1];
        
        for (int i = 0; i <= a.length(); i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            matrix[0][j] = j;
        }
        
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                matrix[i][j] = Math.min(Math.min(
                    matrix[i - 1][j] + 1,      // deletion
                    matrix[i][j - 1] + 1),     // insertion
                    matrix[i - 1][j - 1] + cost // substitution
                );
            }
        }
        
        return matrix[a.length()][b.length()];
    }
    
    /**
     * Calculates keyword overlap similarity focusing on technical terms
     */
    private double calculateKeywordOverlap(String text1, String text2) {
        Set<String> keywords1 = extractKeywords(text1);
        Set<String> keywords2 = extractKeywords(text2);
        
        if (keywords1.isEmpty() && keywords2.isEmpty()) {
            return 1.0;
        }
        
        Set<String> intersection = new HashSet<>(keywords1);
        intersection.retainAll(keywords2);
        
        Set<String> union = new HashSet<>(keywords1);
        union.addAll(keywords2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    /**
     * Extracts important keywords from error text
     */
    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new HashSet<>();
        String[] words = text.split("\\s+");
        
        for (String word : words) {
            if (word.length() >= 3 && isImportantKeyword(word)) {
                keywords.add(word);
            }
        }
        
        return keywords;
    }
    
    /**
     * Determines if a word is an important keyword for error comparison
     */
    private boolean isImportantKeyword(String word) {
        return word.matches("(?i).*(array|list|string|number|numeric|integer|json|type|error|format|parsing|expected|value|parameter|field|mismatch).*");
    }
    
    /**
     * Determines if existing error should be updated with new error information
     */
    private boolean shouldUpdateExistingError(ParameterError existing, ParameterError newError) {
        // Update if new error has more detailed reason
        if (newError.getErrorReason().length() > existing.getErrorReason().length() + 20) {
            return true;
        }
        
        // Update if new error has additional info and existing doesn't
        if (newError.getAdditionalInfo() != null && existing.getAdditionalInfo() == null) {
            return true;
        }
        
        // Update if new error is more recent (within reason)
        if (newError.getTimestamp().isAfter(existing.getTimestamp().plusMinutes(5))) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Merges information from new error into existing error
     */
    private void mergeErrorInformation(ParameterError existing, ParameterError newError) {
        // Update with more detailed reason if applicable
        if (newError.getErrorReason().length() > existing.getErrorReason().length()) {
            existing.setErrorReason(newError.getErrorReason());
        }
        
        // Update additional info if new error has it and existing doesn't
        if (newError.getAdditionalInfo() != null && existing.getAdditionalInfo() == null) {
            existing.setAdditionalInfo(newError.getAdditionalInfo());
        }
        
        // Update timestamp to latest
        if (newError.getTimestamp().isAfter(existing.getTimestamp())) {
            existing.setTimestamp(newError.getTimestamp());
        }
    }
    
    /**
     * Truncates text for logging purposes only (doesn't affect stored data)
     */
    private String truncateForLog(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
} 