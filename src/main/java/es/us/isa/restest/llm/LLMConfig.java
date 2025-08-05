package es.us.isa.restest.llm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Configuration class for LLM settings supporting both local and Gemini models
 */
public class LLMConfig {
    
    private static final Logger logger = LogManager.getLogger(LLMConfig.class);
    
    public enum ModelType {
        LOCAL, GEMINI, OLLAMA
    }
    
    // General LLM settings
    private boolean enabled;
    private ModelType modelType;
    
    // Local LLM settings
    private boolean localEnabled;
    private String localUrl;
    private String localModel;
    
    // Gemini API settings
    private boolean geminiEnabled;
    private String geminiApiKey;
    private String geminiModel;
    private String geminiApiUrl;

    // Ollama API settings
    private boolean ollamaEnabled;
    private String ollamaUrl;
    private String ollamaModel;

    // Rate limiting settings
    private int maxRetries;
    private boolean rateLimitRetryEnabled;
    
    // Default constructor with sensible defaults
    public LLMConfig() {
        this.enabled = true;
        this.modelType = ModelType.LOCAL;
        this.localEnabled = true;
        this.localUrl = "http://localhost:4891/v1/chat/completions";
        this.localModel = "llama-3-8b-instruct";
        this.geminiEnabled = false;
        this.geminiApiKey = "";
        this.geminiModel = "gemini-2.0-flash-exp";
        this.geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models";
        this.ollamaEnabled = false;
        this.ollamaUrl = "http://localhost:11434";
        this.ollamaModel = "gemma3:4b";
        this.maxRetries = 3;
        this.rateLimitRetryEnabled = true;
    }
    
    /**
     * Create LLMConfig from properties map
     */
    public static LLMConfig fromProperties(Map<String, String> properties) {
        LLMConfig config = new LLMConfig();
        
        config.enabled = Boolean.parseBoolean(
            properties.getOrDefault("llm.enabled", "true"));
        
        String modelTypeStr = properties.getOrDefault("llm.model.type", "local").toLowerCase();
        switch (modelTypeStr) {
            case "gemini":
                config.modelType = ModelType.GEMINI;
                break;
            case "ollama":
                config.modelType = ModelType.OLLAMA;
                break;
            case "local":
            default:
                config.modelType = ModelType.LOCAL;
                break;
        }
        
        // Local LLM settings
        config.localEnabled = Boolean.parseBoolean(
            properties.getOrDefault("llm.local.enabled", "true"));
        config.localUrl = properties.getOrDefault(
            "llm.local.url", "http://localhost:4891/v1/chat/completions");
        config.localModel = properties.getOrDefault(
            "llm.local.model", "llama-3-8b-instruct");
        
        // Gemini API settings
        config.geminiEnabled = Boolean.parseBoolean(
            properties.getOrDefault("llm.gemini.enabled", "false"));
        config.geminiApiKey = properties.getOrDefault(
            "llm.gemini.api.key", "");
        config.geminiModel = properties.getOrDefault(
            "llm.gemini.model", "gemini-2.0-flash-exp");
        config.geminiApiUrl = properties.getOrDefault(
            "llm.gemini.api.url", "https://generativelanguage.googleapis.com/v1beta/models");

        // Ollama API settings
        config.ollamaEnabled = Boolean.parseBoolean(
            properties.getOrDefault("llm.ollama.enabled", "false"));
        config.ollamaUrl = properties.getOrDefault(
            "llm.ollama.url", "http://localhost:11434");
        config.ollamaModel = properties.getOrDefault(
            "llm.ollama.model", "gemma3:4b");

        // Rate limiting settings
        config.maxRetries = Integer.parseInt(
            properties.getOrDefault("llm.rate.limit.max.retries", "3"));
        config.rateLimitRetryEnabled = Boolean.parseBoolean(
            properties.getOrDefault("llm.rate.limit.retry.enabled", "true"));

        logger.info("LLMConfig initialized: enabled={}, modelType={}, localEnabled={}, geminiEnabled={}, ollamaEnabled={}, maxRetries={}, rateLimitRetryEnabled={}",
                   config.enabled, config.modelType, config.localEnabled, config.geminiEnabled, config.ollamaEnabled, config.maxRetries, config.rateLimitRetryEnabled);
        
        return config;
    }
    
    /**
     * Validate the configuration
     */
    public boolean isValid() {
        if (!enabled) {
            return true; // Valid to be disabled
        }
        
        switch (modelType) {
            case LOCAL:
                return localEnabled && localUrl != null && !localUrl.trim().isEmpty()
                       && localModel != null && !localModel.trim().isEmpty();
            case GEMINI:
                return geminiEnabled && geminiApiKey != null && !geminiApiKey.trim().isEmpty()
                       && geminiModel != null && !geminiModel.trim().isEmpty()
                       && geminiApiUrl != null && !geminiApiUrl.trim().isEmpty();
            case OLLAMA:
                return ollamaEnabled && ollamaUrl != null && !ollamaUrl.trim().isEmpty()
                       && ollamaModel != null && !ollamaModel.trim().isEmpty();
            default:
                return false;
        }
    }
    
    // Getters and setters
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public ModelType getModelType() { return modelType; }
    public void setModelType(ModelType modelType) { this.modelType = modelType; }
    
    public boolean isLocalEnabled() { return localEnabled; }
    public void setLocalEnabled(boolean localEnabled) { this.localEnabled = localEnabled; }
    
    public String getLocalUrl() { return localUrl; }
    public void setLocalUrl(String localUrl) { this.localUrl = localUrl; }
    
    public String getLocalModel() { return localModel; }
    public void setLocalModel(String localModel) { this.localModel = localModel; }
    
    public boolean isGeminiEnabled() { return geminiEnabled; }
    public void setGeminiEnabled(boolean geminiEnabled) { this.geminiEnabled = geminiEnabled; }
    
    public String getGeminiApiKey() { return geminiApiKey; }
    public void setGeminiApiKey(String geminiApiKey) { this.geminiApiKey = geminiApiKey; }
    
    public String getGeminiModel() { return geminiModel; }
    public void setGeminiModel(String geminiModel) { this.geminiModel = geminiModel; }
    
    public String getGeminiApiUrl() { return geminiApiUrl; }
    public void setGeminiApiUrl(String geminiApiUrl) { this.geminiApiUrl = geminiApiUrl; }

    public boolean isOllamaEnabled() { return ollamaEnabled; }
    public void setOllamaEnabled(boolean ollamaEnabled) { this.ollamaEnabled = ollamaEnabled; }

    public String getOllamaUrl() { return ollamaUrl; }
    public void setOllamaUrl(String ollamaUrl) { this.ollamaUrl = ollamaUrl; }

    public String getOllamaModel() { return ollamaModel; }
    public void setOllamaModel(String ollamaModel) { this.ollamaModel = ollamaModel; }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    public boolean isRateLimitRetryEnabled() { return rateLimitRetryEnabled; }
    public void setRateLimitRetryEnabled(boolean rateLimitRetryEnabled) { this.rateLimitRetryEnabled = rateLimitRetryEnabled; }
    
    @Override
    public String toString() {
        return String.format(
            "LLMConfig{enabled=%s, modelType=%s, localEnabled=%s, localUrl='%s', localModel='%s', " +
            "geminiEnabled=%s, geminiModel='%s', geminiApiUrl='%s', ollamaEnabled=%s, ollamaUrl='%s', ollamaModel='%s', " +
            "maxRetries=%d, rateLimitRetryEnabled=%s}",
            enabled, modelType, localEnabled, localUrl, localModel,
            geminiEnabled, geminiModel, geminiApiUrl, ollamaEnabled, ollamaUrl, ollamaModel,
            maxRetries, rateLimitRetryEnabled
        );
    }
}
