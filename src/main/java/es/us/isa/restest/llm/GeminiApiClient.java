package es.us.isa.restest.llm;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Client for communicating with Google Gemini 2.0 Flash API
 */
public class GeminiApiClient {
    
    private static final Logger logger = LogManager.getLogger(GeminiApiClient.class);
    
    private final String apiKey;
    private final String model;
    private final String baseUrl;
    private final OkHttpClient httpClient;
    
    public GeminiApiClient(String apiKey, String model, String baseUrl) {
        this.apiKey = apiKey;
        this.model = model;
        this.baseUrl = baseUrl;
        
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build();
        
        logger.info("GeminiApiClient initialized with model: {}", model);
    }
    
    /**
     * Generate content using Gemini API
     * @param systemPrompt System prompt for the model
     * @param userPrompt User prompt/question
     * @param maxTokens Maximum tokens to generate
     * @param temperature Temperature for generation (0.0 to 1.0)
     * @return Generated content or null if failed
     */
    public String generateContent(String systemPrompt, String userPrompt, int maxTokens, double temperature) {
        try {
            // Build the request URL
            String url = String.format("%s/%s:generateContent?key=%s", baseUrl, model, apiKey);
            
            // Build the request body according to Gemini API format
            JSONObject requestBody = new JSONObject();
            
            // Add generation config
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("maxOutputTokens", maxTokens);
            generationConfig.put("temperature", temperature);
            requestBody.put("generationConfig", generationConfig);
            
            // Add contents (messages)
            JSONArray contents = new JSONArray();
            
            // Add system instruction if provided
            if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
                requestBody.put("systemInstruction", new JSONObject()
                    .put("parts", new JSONArray()
                        .put(new JSONObject().put("text", systemPrompt))));
            }
            
            // Add user message
            JSONObject userContent = new JSONObject();
            userContent.put("role", "user");
            userContent.put("parts", new JSONArray()
                .put(new JSONObject().put("text", userPrompt)));
            contents.put(userContent);
            
            requestBody.put("contents", contents);
            
            logger.debug("[Gemini API] Sending request to: {}", url);
            logger.debug("[Gemini API] Request body: {}", requestBody.toString());
            
            // Create HTTP request
            RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
            
            // Execute request
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.debug("[Gemini API] Response: {}", responseBody);
                    
                    return parseGeminiResponse(responseBody);
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No error body";
                    logger.error("[Gemini API] Request failed with code {}: {}", response.code(), errorBody);
                    return null;
                }
            }
            
        } catch (Exception e) {
            logger.error("[Gemini API] Error calling Gemini API: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Parse Gemini API response to extract generated content
     */
    private String parseGeminiResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            if (jsonResponse.has("candidates")) {
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject firstCandidate = candidates.getJSONObject(0);
                    if (firstCandidate.has("content")) {
                        JSONObject content = firstCandidate.getJSONObject("content");
                        if (content.has("parts")) {
                            JSONArray parts = content.getJSONArray("parts");
                            if (parts.length() > 0) {
                                JSONObject firstPart = parts.getJSONObject(0);
                                if (firstPart.has("text")) {
                                    String text = firstPart.getString("text").trim();
                                    logger.debug("[Gemini API] Successfully extracted content: {}", text);
                                    return text;
                                }
                            }
                        }
                    }
                }
            }
            
            // Check for error in response
            if (jsonResponse.has("error")) {
                JSONObject error = jsonResponse.getJSONObject("error");
                String errorMessage = error.optString("message", "Unknown error");
                logger.error("[Gemini API] API returned error: {}", errorMessage);
            }
            
            logger.warn("[Gemini API] No valid content found in response");
            return null;
            
        } catch (Exception e) {
            logger.error("[Gemini API] Error parsing response: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Simple method for basic text generation (convenience method)
     */
    public String generateText(String prompt) {
        return generateContent(null, prompt, 200, 0.7);
    }
    
    /**
     * Generate text with system prompt (convenience method)
     */
    public String generateText(String systemPrompt, String userPrompt) {
        return generateContent(systemPrompt, userPrompt, 200, 0.7);
    }
}
