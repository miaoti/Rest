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
                "Given this input parameter information:\n" +
                "- Parameter name: %s\n" +
                "- Parameter type: %s\n" +
                "- Parameter description: %s\n" +
                "- Parameter location: %s\n\n" +
                "Available microservices: %s\n\n" +
                "Which services would most likely provide realistic data for this parameter?\n" +
                "Respond with a JSON array of service names in priority order.\n" +
                "Consider semantic meaning, naming patterns, and likely data relationships.");
        
        llmPrompts.put("dataExtraction",
                "Given this API response structure: %s\n" +
                "And this target parameter: %s (type: %s)\n\n" +
                "What is the best JSONPath expression to extract appropriate values?\n" +
                "Consider that we need values suitable for the parameter type and semantic meaning.\n" +
                "Respond with just the JSONPath expression.");
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
} 