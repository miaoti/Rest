package es.us.isa.restest.inputs.smart;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a mapping from a parameter to a source API that can provide realistic values
 */
public class ApiMapping {
    
    private String endpoint;
    private String method;
    private String service;
    private String extractPath;  // JSONPath expression to extract values
    private int priority;        // Higher values = higher priority
    private LocalDateTime lastUsed;
    private double successRate;  // 0.0 to 1.0
    private String description;
    
    // Default constructor
    public ApiMapping() {
        this.method = "GET";
        this.priority = 5; // Will be overridden by configuration
        this.successRate = 0.0;
        this.lastUsed = LocalDateTime.now();
    }
    
    // Constructor with configurable priority
    public ApiMapping(int defaultPriority) {
        this.method = "GET";
        this.priority = defaultPriority;
        this.successRate = 0.0;
        this.lastUsed = LocalDateTime.now();
    }
    
    // Constructor with essential fields
    public ApiMapping(String endpoint, String service, String extractPath) {
        this();
        this.endpoint = endpoint;
        this.service = service;
        this.extractPath = extractPath;
    }
    
    // Full constructor
    public ApiMapping(String endpoint, String method, String service, String extractPath, 
                     int priority, double successRate) {
        this.endpoint = endpoint;
        this.method = method;
        this.service = service;
        this.extractPath = extractPath;
        this.priority = priority;
        this.successRate = successRate;
        this.lastUsed = LocalDateTime.now();
    }
    
    /**
     * Update success rate based on fetch result
     */
    public void updateSuccessRate(boolean success) {
        // Simple exponential moving average
        double alpha = 0.1; // Learning rate
        this.successRate = alpha * (success ? 1.0 : 0.0) + (1 - alpha) * this.successRate;
        this.lastUsed = LocalDateTime.now();
    }
    
    /**
     * Calculate overall score for ranking APIs
     */
    public double calculateScore() {
        // Combine priority, success rate, and recency
        double priorityScore = priority / 10.0;
        double recentnessScore = Math.max(0, 1.0 - 
            (LocalDateTime.now().compareTo(lastUsed) / (24.0 * 60 * 60))); // Days ago
        
        return (0.5 * priorityScore) + (0.3 * successRate) + (0.2 * recentnessScore);
    }
    
    // Getters and setters
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    
    public String getExtractPath() { return extractPath; }
    public void setExtractPath(String extractPath) { this.extractPath = extractPath; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public LocalDateTime getLastUsed() { return lastUsed; }
    public void setLastUsed(LocalDateTime lastUsed) { this.lastUsed = lastUsed; }
    
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiMapping that = (ApiMapping) o;
        return Objects.equals(endpoint, that.endpoint) &&
               Objects.equals(method, that.method) &&
               Objects.equals(service, that.service) &&
               Objects.equals(extractPath, that.extractPath);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(endpoint, method, service, extractPath);
    }
    
    @Override
    public String toString() {
        return String.format(
            "ApiMapping{endpoint='%s', method='%s', service='%s', extractPath='%s', " +
            "priority=%d, successRate=%.2f, score=%.2f}",
            endpoint, method, service, extractPath, priority, successRate, calculateScore()
        );
    }
}