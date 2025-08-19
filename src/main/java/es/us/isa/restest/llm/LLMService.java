package es.us.isa.restest.llm;

import es.us.isa.restest.util.LLMCommunicationLogger;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Unified LLM service that can route requests to either local model or Gemini API
 * based on configuration
 */
public class LLMService {
    
    private static final Logger logger = LogManager.getLogger(LLMService.class);
    
    private final LLMConfig config;
    private final GeminiApiClient geminiClient;
    private final OllamaApiClient ollamaClient;
    private final OkHttpClient httpClient;
    private final LLMCommunicationLogger communicationLogger;

    // Singleton instance
    private static LLMService instance;
    
    private LLMService(LLMConfig config) {
        this(config, new Properties());
    }

    private LLMService(LLMConfig config, Properties properties) {
        this.config = config;

        // Initialize communication logger with provided properties
        Properties loggerProps = new Properties();
        // Copy LLM communication logging AND resource monitoring properties
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("llm.communication.logging.") || key.startsWith("llm.resource.monitoring.")) {
                loggerProps.setProperty(key, properties.getProperty(key));
            }
        }
        // Set default if not specified
        if (!loggerProps.containsKey("llm.communication.logging.enabled")) {
            loggerProps.setProperty("llm.communication.logging.enabled", "true");
        }
        this.communicationLogger = LLMCommunicationLogger.getInstance(loggerProps);
        
        // Initialize Gemini client if needed
        if (config.getModelType() == LLMConfig.ModelType.GEMINI && config.isGeminiEnabled()) {
            this.geminiClient = new GeminiApiClient(
                config.getGeminiApiKey(),
                config.getGeminiModel(),
                config.getGeminiApiUrl(),
                config.getMaxRetries(),
                config.isRateLimitRetryEnabled()
            );
        } else {
            this.geminiClient = null;
        }

        // Initialize Ollama client if needed
        if (config.getModelType() == LLMConfig.ModelType.OLLAMA && config.isOllamaEnabled()) {
            this.ollamaClient = new OllamaApiClient(
                config.getOllamaUrl(),
                config.getOllamaModel(),
                config.getMaxRetries(),
                config.isRateLimitRetryEnabled()
            );
        } else {
            this.ollamaClient = null;
        }
        
        // Initialize HTTP client for local model
        // Disable timeouts for long-running local generations per user requirement
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .pingInterval(30, TimeUnit.SECONDS)
                .callTimeout(0, TimeUnit.MILLISECONDS)
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
     * Get or create singleton instance with communication logging properties
     */
    public static synchronized LLMService getInstance(LLMConfig config, Properties properties) {
        if (instance == null) {
            instance = new LLMService(config, properties);
        }
        return instance;
    }

    /**
     * Get instance from properties map
     */
    public static synchronized LLMService getInstance(Map<String, String> properties) {
        LLMConfig config = LLMConfig.fromProperties(properties);

        // Convert Map to Properties for communication logger
        Properties props = new Properties();
        props.putAll(properties);

        return getInstance(config, props);
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

        // Log the request
        String modelType = config.getModelType().toString();
        String modelName = getModelName();
        String endpoint = getEndpoint();
        Object metadata = createMetadata(maxTokens, temperature);

        LLMCommunicationLogger.LLMRequestContext context = communicationLogger.logRequest(
            modelType, modelName, systemPrompt, userPrompt, endpoint, metadata);

        long startTime = System.currentTimeMillis();
        String result = null;
        boolean success = false;
        String errorMessage = null;

        try {
            switch (config.getModelType()) {
                case GEMINI:
                    result = generateWithGemini(systemPrompt, userPrompt, maxTokens, temperature);
                    break;
                case LOCAL:
                    result = generateWithLocal(systemPrompt, userPrompt, maxTokens, temperature);
                    break;
                case OLLAMA:
                    result = generateWithOllama(systemPrompt, userPrompt, maxTokens, temperature);
                    break;
                default:
                    errorMessage = "Unknown model type: " + config.getModelType();
                    logger.error(errorMessage);
                    break;
            }

            success = (result != null);

        } catch (Exception e) {
            errorMessage = e.getMessage();
            logger.error("Error during LLM generation: {}", errorMessage, e);
        } finally {
            // Log the response
            communicationLogger.logResponse(context, result, success, errorMessage);
        }

        return result;
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
            
            // Ensure per-request no-timeout client in case singleton was initialized earlier with timeouts
            OkHttpClient noTimeoutClient = httpClient.newBuilder()
                    .connectTimeout(0, TimeUnit.MILLISECONDS)
                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .callTimeout(0, TimeUnit.MILLISECONDS)
                    .build();

            try (Response response = noTimeoutClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.debug("[Local LLM] Response: {}", responseBody);
                    
                    return parseLocalLLMResponse(responseBody);
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.body() != null ? response.body().string() : "No error body";
                    } catch (Exception ignore) { }
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
     * Generate text using Ollama API
     */
    private String generateWithOllama(String systemPrompt, String userPrompt, int maxTokens, double temperature) {
        if (ollamaClient == null) {
            logger.error("Ollama client is not initialized. Check configuration.");
            return null;
        }

        logger.debug("[LLMService] Using Ollama API for generation");
        return ollamaClient.generateContent(systemPrompt, userPrompt, maxTokens, temperature);
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

    /**
     * Get model name for logging
     */
    private String getModelName() {
        switch (config.getModelType()) {
            case GEMINI:
                return config.getGeminiModel();
            case LOCAL:
                return config.getLocalModel();
            case OLLAMA:
                return config.getOllamaModel();
            default:
                return "Unknown";
        }
    }

    /**
     * Get endpoint for logging
     */
    private String getEndpoint() {
        switch (config.getModelType()) {
            case GEMINI:
                return config.getGeminiApiUrl();
            case LOCAL:
                return config.getLocalUrl();
            case OLLAMA:
                return config.getOllamaUrl();
            default:
                return "Unknown";
        }
    }

    /**
     * Create metadata object for logging
     */
    private Object createMetadata(int maxTokens, double temperature) {
        return String.format("maxTokens=%d, temperature=%.2f", maxTokens, temperature);
    }

    /**
     * Close communication logger
     */
    public void close() {
        if (communicationLogger != null) {
            communicationLogger.close();
        }
    }
}
