package es.us.isa.restest.llm;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Unified LLM service that can route requests to either local model or Gemini API
 * based on configuration
 */
public class LLMService {
    
    private static final Logger logger = LogManager.getLogger(LLMService.class);
    
    private final LLMConfig config;
    private final GeminiApiClient geminiClient;
    private final OkHttpClient httpClient;
    
    // Singleton instance
    private static LLMService instance;
    
    private LLMService(LLMConfig config) {
        this.config = config;
        
        // Initialize Gemini client if needed
        if (config.getModelType() == LLMConfig.ModelType.GEMINI && config.isGeminiEnabled()) {
            this.geminiClient = new GeminiApiClient(
                config.getGeminiApiKey(),
                config.getGeminiModel(),
                config.getGeminiApiUrl()
            );
        } else {
            this.geminiClient = null;
        }
        
        // Initialize HTTP client for local model
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build();
        
        logger.info("LLMService initialized with model type: {}", config.getModelType());
    }
    
    /**
     * Get or create singleton instance
     */
    public static synchronized LLMService getInstance(LLMConfig config) {
        if (instance == null) {
            instance = new LLMService(config);
        }
        return instance;
    }
    
    /**
     * Get instance from properties map
     */
    public static synchronized LLMService getInstance(Map<String, String> properties) {
        LLMConfig config = LLMConfig.fromProperties(properties);
        return getInstance(config);
    }
    
    /**
     * Generate text using the configured LLM
     * @param systemPrompt System prompt for the model
     * @param userPrompt User prompt/question
     * @param maxTokens Maximum tokens to generate
     * @param temperature Temperature for generation (0.0 to 1.0)
     * @return Generated content or null if failed
     */
    public String generateText(String systemPrompt, String userPrompt, int maxTokens, double temperature) {
        if (!config.isEnabled()) {
            logger.warn("LLM is disabled in configuration");
            return null;
        }
        
        if (!config.isValid()) {
            logger.error("LLM configuration is invalid: {}", config);
            return null;
        }
        
        switch (config.getModelType()) {
            case GEMINI:
                return generateWithGemini(systemPrompt, userPrompt, maxTokens, temperature);
            case LOCAL:
                return generateWithLocal(systemPrompt, userPrompt, maxTokens, temperature);
            default:
                logger.error("Unknown model type: {}", config.getModelType());
                return null;
        }
    }
    
    /**
     * Convenience method with default parameters
     */
    public String generateText(String systemPrompt, String userPrompt) {
        return generateText(systemPrompt, userPrompt, 200, 0.7);
    }
    
    /**
     * Generate text using Gemini API
     */
    private String generateWithGemini(String systemPrompt, String userPrompt, int maxTokens, double temperature) {
        if (geminiClient == null) {
            logger.error("Gemini client not initialized");
            return null;
        }
        
        logger.debug("[LLMService] Using Gemini API for generation");
        return geminiClient.generateContent(systemPrompt, userPrompt, maxTokens, temperature);
    }
    
    /**
     * Generate text using local LLM
     */
    private String generateWithLocal(String systemPrompt, String userPrompt, int maxTokens, double temperature) {
        logger.debug("[LLMService] Using local LLM for generation");
        
        try {
            // Build the request body compatible with OpenAI API format (which gpt4all supports)
            JSONArray messages = new JSONArray();
            
            if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
                messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
            }
            
            messages.put(new JSONObject().put("role", "user").put("content", userPrompt));
            
            JSONObject requestBody = new JSONObject()
                    .put("model", config.getLocalModel())
                    .put("messages", messages)
                    .put("max_tokens", maxTokens)
                    .put("temperature", temperature);
            
            logger.debug("[Local LLM] Sending request to: {}", config.getLocalUrl());
            logger.debug("[Local LLM] Request body: {}", requestBody.toString());
            
            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                    .url(config.getLocalUrl())
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.debug("[Local LLM] Response: {}", responseBody);
                    
                    return parseLocalLLMResponse(responseBody);
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No error body";
                    logger.error("[Local LLM] Request failed with code {}: {}", response.code(), errorBody);
                    return null;
                }
            }
            
        } catch (Exception e) {
            logger.error("[Local LLM] Error calling local LLM API: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Parse local LLM response (OpenAI format)
     */
    private String parseLocalLLMResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            if (jsonResponse.has("choices")) {
                JSONArray choices = jsonResponse.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    String content = message.getString("content").trim();
                    
                    logger.debug("[Local LLM] Successfully extracted content: {}", content);
                    return content;
                }
            }
            
            logger.warn("[Local LLM] No choices in response");
            return null;
            
        } catch (Exception e) {
            logger.error("[Local LLM] Error parsing response: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Get current configuration
     */
    public LLMConfig getConfig() {
        return config;
    }
    
    /**
     * Check if service is properly configured and ready
     */
    public boolean isReady() {
        return config.isEnabled() && config.isValid();
    }
}
