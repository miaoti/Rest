package es.us.isa.restest.enhancer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.us.isa.restest.llm.LLMService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Test Case Enhancer that uses LLM to improve failed test inputs.
 * 
 * This class analyzes failed test cases, sends their context to the LLM,
 * and receives improved parameter values that might help the test pass.
 */
public class TestCaseEnhancer {
    
    private static final Logger log = LogManager.getLogger(TestCaseEnhancer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    private final LLMService llmService;
    private final int maxTokens;
    private final double temperature;
    
    // Cache of enhanced parameters by test key
    private final Map<String, Map<String, String>> enhancedParametersCache = new HashMap<>();
    
    public TestCaseEnhancer(LLMService llmService) {
        this(llmService, 500, 0.7);
    }
    
    public TestCaseEnhancer(LLMService llmService, int maxTokens, double temperature) {
        this.llmService = llmService;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }
    
    /**
     * Enhance a single failed test case.
     * 
     * @param failedTest The failed test result with full context
     * @return EnhancementResult containing new parameter values and reasoning
     */
    public EnhancementResult enhance(FailedTestResult failedTest) {
        log.info("🔧 Enhancing test: {} (status: {}, params: {})", 
                failedTest.getTestMethodName(), 
                failedTest.getActualStatusCode(),
                failedTest.getParameters().size());
        
        String systemPrompt = buildSystemPrompt();
        String userPrompt = buildUserPrompt(failedTest);
        
        log.debug("📝 LLM Enhancement Prompt:\n{}", userPrompt);
        
        String llmResponse = llmService.generateText(systemPrompt, userPrompt, maxTokens, temperature);
        
        if (llmResponse == null || llmResponse.trim().isEmpty()) {
            log.warn("⚠️  LLM returned empty response for test: {}", failedTest.getTestMethodName());
            return EnhancementResult.failed("LLM returned empty response");
        }
        
        log.debug("🤖 LLM Enhancement Response:\n{}", llmResponse);
        
        return parseEnhancementResponse(llmResponse, failedTest);
    }
    
    /**
     * Enhance multiple failed tests in batch.
     */
    public List<EnhancementResult> enhanceBatch(List<FailedTestResult> failedTests) {
        log.info("🔧 Enhancing {} failed tests...", failedTests.size());
        
        List<EnhancementResult> results = new ArrayList<>();
        int enhanced = 0;
        int failed = 0;
        
        for (FailedTestResult failedTest : failedTests) {
            try {
                EnhancementResult result = enhance(failedTest);
                results.add(result);
                
                if (result.isSuccess()) {
                    enhanced++;
                    // Cache the enhanced parameters
                    String testKey = failedTest.getTestClassName() + "." + failedTest.getTestMethodName();
                    enhancedParametersCache.put(testKey, result.getEnhancedParameters());
                } else {
                    failed++;
                }
                
            } catch (Exception e) {
                log.error("Error enhancing test {}: {}", failedTest.getTestMethodName(), e.getMessage());
                results.add(EnhancementResult.failed("Exception: " + e.getMessage()));
                failed++;
            }
        }
        
        log.info("✅ Enhancement complete: {} enhanced, {} failed", enhanced, failed);
        return results;
    }
    
    /**
     * Get cached enhanced parameters for a test.
     */
    public Map<String, String> getEnhancedParameters(String testClassName, String testMethodName) {
        String testKey = testClassName + "." + testMethodName;
        return enhancedParametersCache.get(testKey);
    }
    
    /**
     * Save enhancement results to file.
     */
    public void saveEnhancementResults(List<EnhancementResult> results, String outputDir, int round) {
        try {
            File dir = new File(outputDir, "round-" + round);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            File outputFile = new File(dir, "enhancement-results.json");
            objectMapper.writeValue(outputFile, results);
            log.info("💾 Saved {} enhancement results to: {}", results.size(), outputFile.getAbsolutePath());
            
        } catch (IOException e) {
            log.error("Failed to save enhancement results: {}", e.getMessage());
        }
    }
    
    private String buildSystemPrompt() {
        return "You are an expert API test case analyzer and enhancer.\n\n" +
               "Your task is to analyze a failed API test case and suggest improved parameter values " +
               "that are more likely to make the test pass.\n\n" +
               "IMPORTANT RULES:\n" +
               "1. Suggest realistic, valid values that match the API's expectations.\n" +
            //    "2. For NEGATIVE tests (testing invalid inputs), suggest values that are still invalid but might trigger different error responses.\n" +
               "2. Analyze the error response message carefully to understand WHY the test failed.\n" +
               "3. Consider the parameter descriptions, types, and examples when suggesting new values.\n" +
               "4. Return your response in valid JSON format ONLY.\n\n" +
               "RESPONSE FORMAT:\n" +
               "{\n" +
               "  \"enhancedParameters\": [\n" +
               "    {\"name\": \"paramName\", \"value\": \"newValue\"}\n" +
               "  ],\n" +
               "  \"reasoning\": \"Brief explanation of why these values were chosen\"\n" +
               "}";
    }
    
    private String buildUserPrompt(FailedTestResult failedTest) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("ANALYZE THIS FAILED TEST AND SUGGEST IMPROVED PARAMETER VALUES:\n\n");
        
        prompt.append("TEST INFORMATION:\n");
        prompt.append("- Test Name: ").append(failedTest.getTestMethodName()).append("\n");
        prompt.append("- Endpoint: ").append(failedTest.getHttpMethod()).append(" ")
              .append(failedTest.getEndpoint()).append("\n");
        prompt.append("- Test Type: ").append(failedTest.isNegativeTest() ? "NEGATIVE (invalid inputs)" : "POSITIVE (valid inputs)").append("\n");
        prompt.append("- Service: ").append(failedTest.getServiceName()).append("\n\n");
        
        prompt.append("EXECUTION RESULT:\n");
        prompt.append("- HTTP Status: ").append(failedTest.getActualStatusCode()).append("\n");
        prompt.append("- Response: ").append(truncateResponse(failedTest.getResponseBody())).append("\n");
        prompt.append("- Error: ").append(failedTest.getErrorMessage()).append("\n\n");
        
        // For negative tests, show which parameters are intentionally invalid
        if (failedTest.isNegativeTest() && failedTest.getInvalidParameters() != null 
                && !failedTest.getInvalidParameters().isEmpty()) {
            prompt.append("⚠️ INTENTIONALLY INVALID PARAMETERS (DO NOT CHANGE THESE):\n");
            for (String invalidParam : failedTest.getInvalidParameters()) {
                prompt.append("- ").append(invalidParam).append("\n");
            }
            prompt.append("\n");
            prompt.append("NOTE: This is a NEGATIVE test. The parameters above are INTENTIONALLY INVALID.\n");
            prompt.append("You should ONLY suggest changes to OTHER parameters to make the test trigger a different error response.\n");
            prompt.append("DO NOT change the intentionally invalid parameters listed above.\n\n");
        }
        
        prompt.append("PARAMETERS USED:\n");
        prompt.append("```json\n");
        prompt.append(formatParametersForPrompt(failedTest.getParameters()));
        prompt.append("\n```\n\n");
        
        prompt.append("Based on the error response, suggest improved values for the parameters.\n");
        if (failedTest.isNegativeTest()) {
            prompt.append("Remember: DO NOT change the intentionally invalid parameters. Only adjust other parameters.\n");
        }
        prompt.append("Return ONLY a valid JSON response in the specified format.");
        
        return prompt.toString();
    }
    
    private String formatParametersForPrompt(List<ParameterSnapshot> parameters) {
        try {
            List<Map<String, Object>> paramList = new ArrayList<>();
            for (ParameterSnapshot param : parameters) {
                Map<String, Object> paramMap = new LinkedHashMap<>();
                paramMap.put("name", param.getName());
                paramMap.put("value", param.getValue());
                paramMap.put("type", param.getType());
                paramMap.put("location", param.getLocation());
                // Always include required flag
                paramMap.put("required", param.isRequired());
                // Include description and example if available
                if (param.getDescription() != null && !param.getDescription().isEmpty()) {
                    paramMap.put("description", param.getDescription());
                }
                if (param.getExample() != null && !param.getExample().isEmpty()) {
                    paramMap.put("example", param.getExample());
                }
                paramList.add(paramMap);
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(paramList);
        } catch (Exception e) {
            return "[]";
        }
    }
    
    private String truncateResponse(String response) {
        if (response == null) return "null";
        if (response.length() > 500) {
            return response.substring(0, 500) + "... (truncated)";
        }
        return response;
    }
    
    private EnhancementResult parseEnhancementResponse(String llmResponse, FailedTestResult originalTest) {
        try {
            // Extract JSON from response (LLM might add explanatory text)
            String jsonStr = extractJson(llmResponse);
            if (jsonStr == null) {
                return EnhancementResult.failed("Could not extract JSON from LLM response");
            }
            
            JsonNode root = objectMapper.readTree(jsonStr);
            
            Map<String, String> enhancedParams = new HashMap<>();
            String reasoning = "";
            
            // Parse enhanced parameters
            if (root.has("enhancedParameters") && root.get("enhancedParameters").isArray()) {
                for (JsonNode paramNode : root.get("enhancedParameters")) {
                    String name = paramNode.has("name") ? paramNode.get("name").asText() : null;
                    String value = paramNode.has("value") ? paramNode.get("value").asText() : null;
                    if (name != null && value != null) {
                        enhancedParams.put(name, value);
                    }
                }
            }
            
            // Parse reasoning
            if (root.has("reasoning")) {
                reasoning = root.get("reasoning").asText();
            }
            
            if (enhancedParams.isEmpty()) {
                return EnhancementResult.failed("No enhanced parameters found in LLM response");
            }
            
            return EnhancementResult.success(
                    originalTest.getTestClassName(),
                    originalTest.getTestMethodName(),
                    enhancedParams,
                    reasoning
            );
            
        } catch (Exception e) {
            log.error("Failed to parse LLM enhancement response: {}", e.getMessage());
            return EnhancementResult.failed("Parse error: " + e.getMessage());
        }
    }
    
    private String extractJson(String response) {
        // Try to find JSON object in the response
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        
        return null;
    }
    
    /**
     * Result of an enhancement attempt.
     */
    public static class EnhancementResult {
        private boolean success;
        private String testClassName;
        private String testMethodName;
        private Map<String, String> enhancedParameters;
        private String reasoning;
        private String errorMessage;
        
        public static EnhancementResult success(String testClassName, String testMethodName,
                                                Map<String, String> enhancedParameters, String reasoning) {
            EnhancementResult result = new EnhancementResult();
            result.success = true;
            result.testClassName = testClassName;
            result.testMethodName = testMethodName;
            result.enhancedParameters = enhancedParameters;
            result.reasoning = reasoning;
            return result;
        }
        
        public static EnhancementResult failed(String errorMessage) {
            EnhancementResult result = new EnhancementResult();
            result.success = false;
            result.errorMessage = errorMessage;
            return result;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getTestClassName() { return testClassName; }
        public String getTestMethodName() { return testMethodName; }
        public Map<String, String> getEnhancedParameters() { return enhancedParameters; }
        public String getReasoning() { return reasoning; }
        public String getErrorMessage() { return errorMessage; }
        
        @Override
        public String toString() {
            if (success) {
                return "EnhancementResult{SUCCESS, test=" + testMethodName + 
                       ", params=" + enhancedParameters.size() + "}";
            } else {
                return "EnhancementResult{FAILED, error=" + errorMessage + "}";
            }
        }
    }
}

