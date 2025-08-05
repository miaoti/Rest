package es.us.isa.restest.inputs.smart;

import java.util.Map;
import java.util.List;

/**
 * Configuration for the Smart Input Fetching System
 * Loads settings from properties and registry files
 */
public class SmartInputFetchConfig {
    
    private boolean enabled;
    private double smartFetchPercentage;
    private String registryPath;
    private String openApiSpecPath;
    private boolean llmDiscoveryEnabled;
    private boolean llmEndpointSelectionEnabled;
    private int maxCandidates;
    private boolean dependencyResolutionEnabled;
    private long discoveryTimeoutMs;
    private boolean cacheEnabled;
    private int cacheTtlSeconds;
    private int defaultPriority;
    private int patternDiscoveryPriority;
    private int llmDiscoveryPriority;
    private String defaultContentType;
    private int successResponseCode;
    private long schemaDiscoveryTimeoutMs;

    // Authentication settings
    private String authAdminUsername;
    private String authAdminPassword;
    private String authUserUsername;
    private String authUserPassword;
    
    // Default constructor
    public SmartInputFetchConfig() {
        // Default values
        this.enabled = false;
        this.smartFetchPercentage = 0.3;
        this.registryPath = "input-fetch-registry.yaml";
        this.openApiSpecPath = "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml";
        this.llmDiscoveryEnabled = true;
        this.llmEndpointSelectionEnabled = true;
        this.maxCandidates = 5;
        this.dependencyResolutionEnabled = true;
        this.discoveryTimeoutMs = 5000;
        this.cacheEnabled = true;
        this.cacheTtlSeconds = 300;
        this.defaultPriority = 5;
        this.patternDiscoveryPriority = 5;
        this.llmDiscoveryPriority = 7;
        this.defaultContentType = "application/json";
        this.successResponseCode = 200;
        this.schemaDiscoveryTimeoutMs = 3000;
    }
    
    /**
     * Load configuration from properties
     */
    public static SmartInputFetchConfig fromProperties(Map<String, String> properties) {
        SmartInputFetchConfig config = new SmartInputFetchConfig();
        
        config.enabled = Boolean.parseBoolean(
            properties.getOrDefault("smart.input.fetch.enabled", "false"));
        
        config.smartFetchPercentage = Double.parseDouble(
            properties.getOrDefault("smart.input.fetch.percentage", "0.3"));
        
        config.registryPath = properties.getOrDefault(
            "smart.input.fetch.registry.path", "input-fetch-registry.yaml");
        
        config.openApiSpecPath = properties.getOrDefault(
            "smart.input.fetch.openapi.spec.path", "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml");
        
        config.llmDiscoveryEnabled = Boolean.parseBoolean(
            properties.getOrDefault("smart.input.fetch.llm.discovery.enabled", "true"));

        config.llmEndpointSelectionEnabled = Boolean.parseBoolean(
            properties.getOrDefault("smart.input.fetch.llm.endpoint.selection.enabled", "true"));

        config.maxCandidates = Integer.parseInt(
            properties.getOrDefault("smart.input.fetch.max.candidates", "5"));
        
        config.dependencyResolutionEnabled = Boolean.parseBoolean(
            properties.getOrDefault("smart.input.fetch.dependency.resolution.enabled", "true"));
        
        config.discoveryTimeoutMs = Long.parseLong(
            properties.getOrDefault("smart.input.fetch.discovery.timeout.ms", "5000"));
        
        config.cacheEnabled = Boolean.parseBoolean(
            properties.getOrDefault("smart.input.fetch.cache.enabled", "true"));
        
        config.cacheTtlSeconds = Integer.parseInt(
            properties.getOrDefault("smart.input.fetch.cache.ttl.seconds", "300"));
        
        config.defaultPriority = Integer.parseInt(
            properties.getOrDefault("smart.input.fetch.default.priority", "5"));
        
        config.patternDiscoveryPriority = Integer.parseInt(
            properties.getOrDefault("smart.input.fetch.pattern.discovery.priority", "5"));
        
        config.llmDiscoveryPriority = Integer.parseInt(
            properties.getOrDefault("smart.input.fetch.llm.discovery.priority", "7"));
        
        config.defaultContentType = properties.getOrDefault(
            "smart.input.fetch.http.content.type", "application/json");
        
        config.successResponseCode = Integer.parseInt(
            properties.getOrDefault("smart.input.fetch.http.success.code", "200"));
        
        config.schemaDiscoveryTimeoutMs = Long.parseLong(
            properties.getOrDefault("smart.input.fetch.schema.discovery.timeout.ms", "3000"));

        // Authentication settings
        config.authAdminUsername = properties.getOrDefault("auth.admin.username", "admin");
        config.authAdminPassword = properties.getOrDefault("auth.admin.password", "222222");
        config.authUserUsername = properties.getOrDefault("auth.user.username", "fdse_microservice");
        config.authUserPassword = properties.getOrDefault("auth.user.password", "111111");

        return config;
    }
    
    // Getters and setters
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public double getSmartFetchPercentage() { return smartFetchPercentage; }
    public void setSmartFetchPercentage(double smartFetchPercentage) { 
        this.smartFetchPercentage = smartFetchPercentage; 
    }
    
    public String getRegistryPath() { return registryPath; }
    public void setRegistryPath(String registryPath) { this.registryPath = registryPath; }
    
    public String getOpenApiSpecPath() { return openApiSpecPath; }
    public void setOpenApiSpecPath(String openApiSpecPath) { this.openApiSpecPath = openApiSpecPath; }
    
    public boolean isLlmDiscoveryEnabled() { return llmDiscoveryEnabled; }
    public void setLlmDiscoveryEnabled(boolean llmDiscoveryEnabled) {
        this.llmDiscoveryEnabled = llmDiscoveryEnabled;
    }

    public boolean isLlmEndpointSelectionEnabled() { return llmEndpointSelectionEnabled; }
    public void setLlmEndpointSelectionEnabled(boolean llmEndpointSelectionEnabled) {
        this.llmEndpointSelectionEnabled = llmEndpointSelectionEnabled;
    }
    
    public int getMaxCandidates() { return maxCandidates; }
    public void setMaxCandidates(int maxCandidates) { this.maxCandidates = maxCandidates; }
    
    public boolean isDependencyResolutionEnabled() { return dependencyResolutionEnabled; }
    public void setDependencyResolutionEnabled(boolean dependencyResolutionEnabled) { 
        this.dependencyResolutionEnabled = dependencyResolutionEnabled; 
    }
    
    public long getDiscoveryTimeoutMs() { return discoveryTimeoutMs; }
    public void setDiscoveryTimeoutMs(long discoveryTimeoutMs) { 
        this.discoveryTimeoutMs = discoveryTimeoutMs; 
    }
    
    public boolean isCacheEnabled() { return cacheEnabled; }
    public void setCacheEnabled(boolean cacheEnabled) { this.cacheEnabled = cacheEnabled; }
    
    public int getCacheTtlSeconds() { return cacheTtlSeconds; }
    public void setCacheTtlSeconds(int cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }
    
    public int getDefaultPriority() { return defaultPriority; }
    public void setDefaultPriority(int defaultPriority) { this.defaultPriority = defaultPriority; }
    
    public int getPatternDiscoveryPriority() { return patternDiscoveryPriority; }
    public void setPatternDiscoveryPriority(int patternDiscoveryPriority) { 
        this.patternDiscoveryPriority = patternDiscoveryPriority; 
    }
    
    public int getLlmDiscoveryPriority() { return llmDiscoveryPriority; }
    public void setLlmDiscoveryPriority(int llmDiscoveryPriority) { 
        this.llmDiscoveryPriority = llmDiscoveryPriority; 
    }
    
    public String getDefaultContentType() { return defaultContentType; }
    public void setDefaultContentType(String defaultContentType) { 
        this.defaultContentType = defaultContentType; 
    }
    
    public int getSuccessResponseCode() { return successResponseCode; }
    public void setSuccessResponseCode(int successResponseCode) { 
        this.successResponseCode = successResponseCode; 
    }
    
    public long getSchemaDiscoveryTimeoutMs() { return schemaDiscoveryTimeoutMs; }
    public void setSchemaDiscoveryTimeoutMs(long schemaDiscoveryTimeoutMs) {
        this.schemaDiscoveryTimeoutMs = schemaDiscoveryTimeoutMs;
    }

    public String getAuthAdminUsername() { return authAdminUsername; }
    public void setAuthAdminUsername(String authAdminUsername) { this.authAdminUsername = authAdminUsername; }

    public String getAuthAdminPassword() { return authAdminPassword; }
    public void setAuthAdminPassword(String authAdminPassword) { this.authAdminPassword = authAdminPassword; }

    public String getAuthUserUsername() { return authUserUsername; }
    public void setAuthUserUsername(String authUserUsername) { this.authUserUsername = authUserUsername; }

    public String getAuthUserPassword() { return authUserPassword; }
    public void setAuthUserPassword(String authUserPassword) { this.authUserPassword = authUserPassword; }
    
    @Override
    public String toString() {
        return String.format(
            "SmartInputFetchConfig{enabled=%s, smartFetchPercentage=%.2f, registryPath='%s', " +
            "openApiSpecPath='%s', llmDiscoveryEnabled=%s, maxCandidates=%d, dependencyResolutionEnabled=%s, " +
            "discoveryTimeoutMs=%d, cacheEnabled=%s, cacheTtlSeconds=%d}",
            enabled, smartFetchPercentage, registryPath, openApiSpecPath, llmDiscoveryEnabled, maxCandidates,
            dependencyResolutionEnabled, discoveryTimeoutMs, cacheEnabled, cacheTtlSeconds
        );
    }
}