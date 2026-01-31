package es.us.isa.restest.enhancer;

import es.us.isa.restest.auth.AuthManipulationStrategy;
import es.us.isa.restest.coverage.LLMStatusCodeDiscovery;
import es.us.isa.restest.coverage.StatusCodeCoverageTracker;
import es.us.isa.restest.coverage.StatusCodeTarget;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.llm.LLMService;
import es.us.isa.restest.testcases.MultiServiceTestCase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Enhancer that creates new test cases to explore untriggered HTTP status codes.
 * 
 * This enhancer works AFTER the first execution round:
 * 1. Runs LLM Discovery to identify all possible status codes per API
 * 2. Tracks which status codes have been triggered
 * 3. For each test case, asks LLM if it's a good candidate for exploration
 * 4. Creates NEW test cases to target untriggered status codes
 * 
 * Key design principles:
 * - Creates NEW tests, doesn't modify originals
 * - Uses round-robin to avoid targeting same code twice per round
 * - Preserves original test enhancement logic (works alongside TestCaseEnhancer)
 */
public class StatusCodeExplorationEnhancer {
    
    private static final Logger log = LogManager.getLogger(StatusCodeExplorationEnhancer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final LLMService llmService;
    private final LLMStatusCodeDiscovery discovery;
    private final StatusCodeCoverageTracker tracker;
    private final AuthManipulationStrategy authStrategy;
    
    private final int maxTokens;
    private final double temperature;
    
    // Configuration
    private int maxExplorationTestsPerOriginal = 3;
    private int maxExplorationTestsPerRound = 20;
    private int maxRetryPerStatusCode = 2;  // Max retry attempts for each status code
    private boolean enabled = true;
    
    // Track failed inputs per (apiKey, statusCode) to avoid repeating them
    private final Map<String, Set<String>> failedInputsPerTarget = new HashMap<>();
    
    /**
     * Callback interface for executing a single exploration test.
     * Used to enable the feedback loop where we execute tests immediately and check results.
     */
    public interface ExplorationTestExecutor {
        /**
         * Execute a single exploration test and return the actual status code.
         * @param test The exploration test to execute
         * @return The actual HTTP status code returned, or -1 if execution failed
         */
        int executeAndGetStatusCode(MultiServiceTestCase test);
    }
    
    public StatusCodeExplorationEnhancer(LLMService llmService) {
        this(llmService, new StatusCodeCoverageTracker());
    }
    
    public StatusCodeExplorationEnhancer(LLMService llmService, StatusCodeCoverageTracker tracker) {
        this.llmService = llmService;
        this.discovery = new LLMStatusCodeDiscovery(llmService);
        this.tracker = tracker;
        this.authStrategy = new AuthManipulationStrategy();
        this.maxTokens = 1000;
        this.temperature = 0.3;
    }
    
    /**
     * Main entry point: Process executed tests and create exploration tests.
     * 
     * @param executedTests Tests that were executed
     * @param executionResults Map of test name to execution result (status code, response, etc.)
     * @return ExplorationResult containing new exploration tests and coverage summary
     */
    public ExplorationResult explore(
            List<MultiServiceTestCase> executedTests,
            Map<String, TestExecutionResult> executionResults) {
        
        if (!enabled) {
            log.info("Status code exploration is disabled");
            return new ExplorationResult(Collections.emptyList(), tracker.getOverallCoverageSummary());
        }
        
        log.info("=== STATUS CODE EXPLORATION PHASE ===");
        log.info("Processing {} executed tests for exploration", executedTests.size());
        
        // STEP 1: Run LLM Discovery for each unique API
        runDiscoveryForApis(executedTests, executionResults);
        
        // STEP 2: Update triggered status codes from execution results
        updateTriggeredStatusCodes(executionResults);
        
        // Log current coverage
        tracker.logCoverageReport();
        
        // STEP 3: Create exploration tests
        List<MultiServiceTestCase> explorationTests = new ArrayList<>();
        int totalCreated = 0;
        
        for (MultiServiceTestCase test : executedTests) {
            if (totalCreated >= maxExplorationTestsPerRound) {
                log.info("Reached max exploration tests per round ({})", maxExplorationTestsPerRound);
                break;
            }
            
            // Skip tests that are already exploration tests
            if (test.isStatusCodeExplorationTest()) {
                log.debug("Skipping exploration test: {}", test.getOperationId());
                continue;
            }
            
            TestExecutionResult result = executionResults.get(test.getOperationId());
            if (result == null) {
                log.warn("No execution result for test: {}", test.getOperationId());
                continue;
            }
            
            String apiKey = getApiKey(test);
            
            // Get untriggered codes for this API
            List<StatusCodeTarget> untriggeredCodes = tracker.getUntriggeredCodes(apiKey);
            if (untriggeredCodes.isEmpty()) {
                log.debug("All status codes already triggered/targeted for {}", apiKey);
                continue;
            }
            
            // Ask LLM if this test is a good exploration candidate
            List<ExplorationSuggestion> suggestions = evaluateExplorationCandidate(
                test, result, untriggeredCodes);
            
            // Build a map of status code -> StatusCodeTarget for quick lookup
            Map<Integer, StatusCodeTarget> targetsByCode = new HashMap<>();
            for (StatusCodeTarget target : untriggeredCodes) {
                targetsByCode.put(target.getStatusCode(), target);
            }
            
            // Create exploration tests from suggestions
            int createdForThisTest = 0;
            for (ExplorationSuggestion suggestion : suggestions) {
                if (totalCreated >= maxExplorationTestsPerRound) break;
                if (createdForThisTest >= maxExplorationTestsPerOriginal) break;
                
                int targetCode = suggestion.getTargetStatusCode();
                
                // Skip if already targeted this round (round-robin)
                if (tracker.isTargeted(apiKey, targetCode)) {
                    log.debug("Status {} already targeted for {} this round", targetCode, apiKey);
                    continue;
                }
                
                // Get the StatusCodeTarget for this status code (has suggestedInputs from discovery)
                StatusCodeTarget statusCodeTarget = targetsByCode.get(targetCode);
                
                // Create new exploration test, passing the StatusCodeTarget for fallback inputs
                MultiServiceTestCase explorationTest = createExplorationTest(test, suggestion, statusCodeTarget);
                if (explorationTest != null) {
                    explorationTests.add(explorationTest);
                    tracker.markTargeted(apiKey, targetCode);
                    totalCreated++;
                    createdForThisTest++;
                    
                    log.info("Created exploration test {} targeting status {}", 
                        explorationTest.getOperationId(), targetCode);
                }
            }
        }
        
        log.info("=== EXPLORATION COMPLETE: Created {} new tests ===", explorationTests.size());
        
        return new ExplorationResult(explorationTests, tracker.getOverallCoverageSummary());
    }
    
    /**
     * Efficient exploration: ONE LLM call per test case, batch execute all exploration tests.
     * 
     * Correct flow:
     * 1. For each test case, ask LLM ONCE: "Should we explore? Give ALL suggestions with parameters"
     * 2. LLM returns ALL suggestions at once (e.g., [{status:400, params:{...}}, {status:422, params:{...}}])
     * 3. Generate ALL exploration tests at once from LLM suggestions
     * 4. Return all exploration tests - caller will execute them in batch
     * 5. After execution, call recordExplorationResults() to update round-robin
     * 
     * @param executedTests Tests that were executed
     * @param executionResults Results from execution
     * @return ExplorationResult with all generated exploration tests (caller executes them)
     */
    public ExplorationResult exploreEfficient(
            List<MultiServiceTestCase> executedTests,
            Map<String, TestExecutionResult> executionResults) {
        
        if (!enabled || executedTests.isEmpty()) {
            return new ExplorationResult(Collections.emptyList(), tracker.getOverallCoverageSummary());
        }
        
        log.info("═══════════════════════════════════════════════════════════════════════════");
        log.info("🔬 STATUS CODE EXPLORATION (Efficient Mode)");
        log.info("═══════════════════════════════════════════════════════════════════════════");
        log.info("Processing {} executed tests for exploration", executedTests.size());
        
        // STEP 1: Run LLM Discovery for each unique API (ONCE per API)
        runDiscoveryForApis(executedTests, executionResults);
        
        // STEP 2: Update triggered status codes from execution results
        updateTriggeredStatusCodes(executionResults);
        
        // Log current coverage
        tracker.logCoverageReport();
        
        // STEP 3: For each test case, get ALL exploration suggestions in ONE LLM call
        List<MultiServiceTestCase> allExplorationTests = new ArrayList<>();
        int totalCreated = 0;
        
        for (MultiServiceTestCase test : executedTests) {
            if (totalCreated >= maxExplorationTestsPerRound) {
                log.info("Reached max exploration tests per round ({})", maxExplorationTestsPerRound);
                break;
            }
            
            // Skip exploration tests
            if (test.isStatusCodeExplorationTest()) {
                continue;
            }
            
            TestExecutionResult result = executionResults.get(test.getOperationId());
            if (result == null) {
                log.warn("No execution result for test: {}", test.getOperationId());
                continue;
            }
            
            String apiKey = getApiKey(test);
            
            // Get untriggered codes for this API (from round-robin)
            List<StatusCodeTarget> untriggeredCodes = tracker.getUntriggeredCodes(apiKey);
            if (untriggeredCodes.isEmpty()) {
                log.debug("All status codes already triggered/targeted for {}", apiKey);
                continue;
            }
            
            log.info("───────────────────────────────────────────────────────────────────────────");
            log.info("📋 Test: {} (API: {})", test.getOperationId(), apiKey);
            log.info("   Available status codes to explore: {}", untriggeredCodes.stream()
                .map(t -> String.valueOf(t.getStatusCode())).collect(Collectors.joining(", ")));
            
            // ONE LLM call: Ask which status codes to explore and get ALL parameters
            List<ExplorationSuggestion> suggestions = evaluateExplorationCandidate(test, result, untriggeredCodes);
            
            if (suggestions.isEmpty()) {
                log.info("   LLM: No exploration suggested for this test");
                continue;
            }
            
            log.info("   LLM suggested {} exploration(s): {}", suggestions.size(),
                suggestions.stream().map(s -> String.valueOf(s.getTargetStatusCode())).collect(Collectors.joining(", ")));
            
            // Build lookup map
            Map<Integer, StatusCodeTarget> targetsByCode = new HashMap<>();
            for (StatusCodeTarget target : untriggeredCodes) {
                targetsByCode.put(target.getStatusCode(), target);
            }
            
            // Generate ALL exploration tests from suggestions
            int createdForThisTest = 0;
            for (ExplorationSuggestion suggestion : suggestions) {
                if (totalCreated >= maxExplorationTestsPerRound) break;
                if (createdForThisTest >= maxExplorationTestsPerOriginal) break;
                
                int targetCode = suggestion.getTargetStatusCode();
                
                // Skip if already triggered or not in our list
                if (tracker.isTriggered(apiKey, targetCode)) {
                    continue;
                }
                
                StatusCodeTarget statusCodeTarget = targetsByCode.get(targetCode);
                
                // Create exploration test
                MultiServiceTestCase explorationTest = createExplorationTest(test, suggestion, statusCodeTarget);
                if (explorationTest != null) {
                    allExplorationTests.add(explorationTest);
                    // Mark as targeted (but not triggered yet - wait for execution results)
                    tracker.markTargeted(apiKey, targetCode);
                    totalCreated++;
                    createdForThisTest++;
                    
                    log.info("   ✅ Created: {} targeting status {}", 
                        explorationTest.getOperationId(), targetCode);
                    log.info("      Parameters: {}", suggestion.parameterChanges);
                }
            }
        }
        
        log.info("═══════════════════════════════════════════════════════════════════════════");
        log.info("🔬 EXPLORATION GENERATION COMPLETE: Created {} exploration tests", allExplorationTests.size());
        log.info("   Next: Caller will execute these tests and call recordExplorationResults()");
        log.info("═══════════════════════════════════════════════════════════════════════════");
        
        return new ExplorationResult(allExplorationTests, tracker.getOverallCoverageSummary());
    }
    
    /**
     * Record exploration test results and update round-robin.
     * Call this AFTER executing exploration tests to update coverage.
     * 
     * @param explorationResults Map of exploration test operationId -> actual status code received
     */
    public void recordExplorationResults(Map<String, Integer> explorationResults) {
        log.info("📊 Recording {} exploration test results", explorationResults.size());
        
        for (Map.Entry<String, Integer> entry : explorationResults.entrySet()) {
            String testId = entry.getKey();
            int actualStatusCode = entry.getValue();
            
            // Extract API key and target status from test ID
            // Format: test_POST_1_1_explore_400 or similar
            String apiKey = extractApiKeyFromExplorationTestId(testId);
            int targetStatus = extractTargetStatusFromExplorationTestId(testId);
            
            if (apiKey != null && targetStatus > 0) {
                if (actualStatusCode == targetStatus) {
                    // SUCCESS: Remove from round-robin (mark as triggered)
                    tracker.markTriggered(apiKey, targetStatus);
                    log.info("   ✅ {} triggered target status {} - REMOVED from round-robin", testId, targetStatus);
                } else {
                    // FAILED: Move to end of round-robin for future retry
                    tracker.moveToEndOfRoundRobin(apiKey, targetStatus);
                    log.info("   ❌ {} got {} instead of {} - MOVED to end of round-robin", 
                        testId, actualStatusCode, targetStatus);
                }
                
                // Also record the actual status we got (might discover new codes)
                if (actualStatusCode > 0 && actualStatusCode != targetStatus) {
                    tracker.markTriggered(apiKey, actualStatusCode);
                    log.debug("   📝 Incidentally triggered status {} for {}", actualStatusCode, apiKey);
                }
            }
        }
        
        log.info("📊 Updated coverage: {}", tracker.getOverallCoverageSummary());
    }
    
    /**
     * Extract API key from exploration test ID.
     * Test ID format: test_POST_1_1_explore_400
     */
    private String extractApiKeyFromExplorationTestId(String testId) {
        // This needs to be implemented based on your test ID format
        // For now, return a placeholder - will be populated from test metadata
        return null; // Will be set by caller with actual API key
    }
    
    /**
     * Extract target status code from exploration test ID.
     * Test ID format: test_POST_1_1_explore_400
     */
    private int extractTargetStatusFromExplorationTestId(String testId) {
        try {
            // Look for _explore_XXX pattern
            int idx = testId.lastIndexOf("_explore_");
            if (idx > 0) {
                String suffix = testId.substring(idx + 9); // after "_explore_"
                // Handle retry suffix: _explore_400_retry1 -> 400
                int underscoreIdx = suffix.indexOf('_');
                if (underscoreIdx > 0) {
                    suffix = suffix.substring(0, underscoreIdx);
                }
                return Integer.parseInt(suffix);
            }
        } catch (NumberFormatException e) {
            log.debug("Could not extract target status from test ID: {}", testId);
        }
        return -1;
    }
    
    /**
     * Run LLM Discovery for each unique API in the executed tests.
     */
    private void runDiscoveryForApis(
            List<MultiServiceTestCase> executedTests,
            Map<String, TestExecutionResult> executionResults) {
        
        // Group tests by API
        Map<String, List<MultiServiceTestCase>> testsByApi = new HashMap<>();
        for (MultiServiceTestCase test : executedTests) {
            String apiKey = getApiKey(test);
            testsByApi.computeIfAbsent(apiKey, k -> new ArrayList<>()).add(test);
        }
        
        log.info("Running LLM Discovery for {} unique APIs", testsByApi.size());
        
        for (Map.Entry<String, List<MultiServiceTestCase>> entry : testsByApi.entrySet()) {
            String apiKey = entry.getKey();
            List<MultiServiceTestCase> tests = entry.getValue();
            
            // Skip if already discovered
            if (tracker.hasApi(apiKey)) {
                log.debug("API {} already discovered", apiKey);
                continue;
            }
            
            // Get sample test for API info
            MultiServiceTestCase sampleTest = tests.get(0);
            
            // Collect observed status codes from execution results
            Set<Integer> observedCodes = new HashSet<>();
            List<String> sampleResponses = new ArrayList<>();
            
            for (MultiServiceTestCase test : tests) {
                TestExecutionResult result = executionResults.get(test.getOperationId());
                if (result != null) {
                    observedCodes.add(result.getActualStatusCode());
                    if (result.getResponseBody() != null && sampleResponses.size() < 3) {
                        sampleResponses.add(result.getResponseBody());
                    }
                }
            }
            
            // Run LLM Discovery
            String[] apiParts = apiKey.split(" ", 2);
            String httpMethod = apiParts[0];
            String path = apiParts.length > 1 ? apiParts[1] : "";
            String serviceName = getServiceName(sampleTest);
            
            List<StatusCodeTarget> discoveredCodes = discovery.discoverStatusCodes(
                serviceName, httpMethod, path,
                getParameterInfos(sampleTest),
                observedCodes, sampleResponses);
            
            // Register discovered codes
            tracker.registerDiscoveredCodes(apiKey, discoveredCodes);
        }
    }
    
    /**
     * Update triggered status codes from execution results.
     */
    private void updateTriggeredStatusCodes(Map<String, TestExecutionResult> executionResults) {
        for (TestExecutionResult result : executionResults.values()) {
            String apiKey = result.getApiKey();
            if (apiKey != null && tracker.hasApi(apiKey)) {
                tracker.markTriggered(apiKey, result.getActualStatusCode());
            }
        }
    }
    
    /**
     * Ask LLM if this test is a good candidate for status code exploration.
     */
    private List<ExplorationSuggestion> evaluateExplorationCandidate(
            MultiServiceTestCase test,
            TestExecutionResult result,
            List<StatusCodeTarget> untriggeredCodes) {
        
        String systemPrompt = buildExplorationSystemPrompt();
        String userPrompt = buildExplorationUserPrompt(test, result, untriggeredCodes);
        
        log.debug("Evaluating exploration candidate: {}", test.getOperationId());
        
        String llmResponse = llmService.generateText(systemPrompt, userPrompt, maxTokens, temperature);
        
        if (llmResponse == null || llmResponse.trim().isEmpty()) {
            log.warn("LLM returned empty response for exploration evaluation");
            return Collections.emptyList();
        }
        
        return parseExplorationResponse(llmResponse);
    }
    
    private String buildExplorationSystemPrompt() {
        return "You are an API testing expert specializing in HTTP status code coverage.\n" +
               "Your task is to generate exploration test cases that trigger specific HTTP status codes.\n\n" +
               "You will receive:\n" +
               "1. API operation details (method, path, service)\n" +
               "2. Current test parameters and execution result\n" +
               "3. A prioritized list of UNTRIGGERED status codes with suggested inputs\n\n" +
               "Your job:\n" +
               "- Select which status codes can realistically be triggered by modifying parameters\n" +
               "- Provide EXACT parameter values to trigger each selected status code\n" +
               "- Use the suggested inputs from discovery as a starting point\n" +
               "- Be practical - only suggest codes achievable via parameter changes\n\n" +
               "IMPORTANT: Respond with valid JSON only. No markdown, no explanations outside JSON.";
    }
    
    private String buildExplorationUserPrompt(
            MultiServiceTestCase test,
            TestExecutionResult result,
            List<StatusCodeTarget> untriggeredCodes) {
        
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("GENERATE EXPLORATION TEST CASES FOR STATUS CODE COVERAGE\n\n");
        
        // API Information (like discovery prompt)
        prompt.append("=== API OPERATION ===\n");
        if (!test.getSteps().isEmpty()) {
            MultiServiceTestCase.StepCall step = test.getSteps().get(0);
            prompt.append("Service: ").append(step.getServiceName()).append("\n");
            prompt.append("Method: ").append(step.getMethod()).append("\n");
            prompt.append("Path: ").append(step.getPath()).append("\n");
        } else {
            prompt.append("API: ").append(getApiKey(test)).append("\n");
        }
        prompt.append("\n");
        
        // Current test parameters (important for context)
        prompt.append("=== CURRENT TEST PARAMETERS ===\n");
        prompt.append("Test ID: ").append(test.getOperationId()).append("\n");
        prompt.append("Test Type: ").append(test.getFaulty() ? "NEGATIVE (invalid inputs)" : "POSITIVE (valid inputs)").append("\n");
        if (!test.getSteps().isEmpty()) {
            MultiServiceTestCase.StepCall step = test.getSteps().get(0);
            if (!step.getPathParams().isEmpty()) {
                prompt.append("Path Parameters: ").append(step.getPathParams()).append("\n");
            }
            if (!step.getQueryParams().isEmpty()) {
                prompt.append("Query Parameters: ").append(step.getQueryParams()).append("\n");
            }
            if (step.getBody() != null && !step.getBody().isEmpty()) {
                prompt.append("Request Body: ").append(truncate(step.getBody(), 500)).append("\n");
            }
            if (!step.getBodyFields().isEmpty()) {
                prompt.append("Body Fields: ").append(step.getBodyFields()).append("\n");
            }
        }
        prompt.append("\n");
        
        // Execution result
        prompt.append("=== LAST EXECUTION RESULT ===\n");
        prompt.append("Actual Status Code: ").append(result.getActualStatusCode()).append("\n");
        if (result.getResponseBody() != null && !result.getResponseBody().isEmpty()) {
            prompt.append("Response: ").append(truncate(result.getResponseBody(), 400)).append("\n");
        }
        prompt.append("\n");
        
        // Round-robin list with priorities and suggested inputs
        prompt.append("=== AVAILABLE STATUS CODES TO EXPLORE (Priority Order) ===\n");
        prompt.append("These status codes have NOT been triggered yet. They are listed in priority order.\n");
        prompt.append("You can generate exploration tests for ANY of these (suggest multiple if possible).\n\n");
        
        int priority = 1;
        for (StatusCodeTarget target : untriggeredCodes) {
            prompt.append(priority++).append(". Status ").append(target.getStatusCode())
                  .append(" - ").append(target.getCategory()).append("\n");
            prompt.append("   Description: ").append(target.getDescription()).append("\n");
            prompt.append("   Trigger Strategy: ").append(target.getTriggerStrategy()).append("\n");
            if (target.isRequiresAuthManipulation()) {
                prompt.append("   ⚠️ Requires Auth Manipulation: YES\n");
            }
            if (!target.getSuggestedInputs().isEmpty()) {
                prompt.append("   📝 Suggested Inputs: ").append(target.getSuggestedInputs()).append("\n");
            }
            prompt.append("\n");
        }
        
        // Task
        prompt.append("=== YOUR TASK ===\n");
        prompt.append("1. Review the available status codes above\n");
        prompt.append("2. For EACH status code you think can be triggered, provide:\n");
        prompt.append("   - The target status code\n");
        prompt.append("   - Your strategy to trigger it\n");
        prompt.append("   - The EXACT parameter changes (use the suggested inputs as starting point)\n");
        prompt.append("3. You can suggest MULTIPLE status codes (recommended: 2-5 per test)\n");
        prompt.append("4. Only suggest codes that are REALISTICALLY achievable\n\n");
        
        // Response format
        prompt.append("=== RESPONSE FORMAT (JSON ONLY) ===\n");
        prompt.append("{\n");
        prompt.append("  \"isGoodCandidate\": true,\n");
        prompt.append("  \"reason\": \"Brief explanation of why this test can trigger these codes\",\n");
        prompt.append("  \"explorations\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"targetStatusCode\": 400,\n");
        prompt.append("      \"strategy\": \"Send malformed request body\",\n");
        prompt.append("      \"parameterChanges\": {\"startPlace\": \"\", \"endPlace\": \"invalid\"},\n");
        prompt.append("      \"requiresAuthManipulation\": false\n");
        prompt.append("    },\n");
        prompt.append("    {\n");
        prompt.append("      \"targetStatusCode\": 404,\n");
        prompt.append("      \"strategy\": \"Request non-existent resource\",\n");
        prompt.append("      \"parameterChanges\": {\"trainId\": \"NONEXISTENT_999\"},\n");
        prompt.append("      \"requiresAuthManipulation\": false\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n\n");
        
        prompt.append("If this test is NOT a good candidate, respond:\n");
        prompt.append("{\"isGoodCandidate\": false, \"reason\": \"explanation\", \"explorations\": []}\n");
        
        return prompt.toString();
    }
    
    /**
     * Parse the LLM exploration response into suggestions.
     */
    private List<ExplorationSuggestion> parseExplorationResponse(String llmResponse) {
        List<ExplorationSuggestion> suggestions = new ArrayList<>();
        
        try {
            // Extract JSON from response
            String jsonStr = extractJson(llmResponse);
            if (jsonStr == null) {
                log.warn("Could not extract JSON from LLM exploration response");
                return suggestions;
            }
            
            JSONObject json = new JSONObject(jsonStr);
            
            boolean isGoodCandidate = json.optBoolean("isGoodCandidate", false);
            String reason = json.optString("reason", "");
            
            if (!isGoodCandidate) {
                log.debug("LLM says test is not a good candidate: {}", reason);
                return suggestions;
            }
            
            log.debug("LLM says test IS a good candidate: {}", reason);
            
            JSONArray explorations = json.optJSONArray("explorations");
            if (explorations == null) {
                return suggestions;
            }
            
            for (int i = 0; i < explorations.length(); i++) {
                try {
                    JSONObject exp = explorations.getJSONObject(i);
                    ExplorationSuggestion suggestion = new ExplorationSuggestion();
                    
                    suggestion.targetStatusCode = exp.getInt("targetStatusCode");
                    suggestion.strategy = exp.optString("strategy", "");
                    suggestion.requiresAuthManipulation = exp.optBoolean("requiresAuthManipulation", false);
                    
                    JSONObject params = exp.optJSONObject("parameterChanges");
                    if (params != null) {
                        for (String key : params.keySet()) {
                            suggestion.parameterChanges.put(key, params.optString(key, ""));
                        }
                    }
                    
                    suggestions.add(suggestion);
                    log.debug("Parsed exploration suggestion: {}", suggestion);
                    
                } catch (Exception e) {
                    log.warn("Failed to parse exploration suggestion {}: {}", i, e.getMessage());
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to parse LLM exploration response: {}", e.getMessage());
        }
        
        return suggestions;
    }
    
    /**
     * Create a new exploration test from the original test and suggestion.
     * @param original The original test case to clone
     * @param suggestion The exploration suggestion from LLM
     * @param statusCodeTarget The StatusCodeTarget from discovery (contains suggestedInputs as fallback)
     */
    private MultiServiceTestCase createExplorationTest(
            MultiServiceTestCase original,
            ExplorationSuggestion suggestion,
            StatusCodeTarget statusCodeTarget) {
        
        try {
            // Clone the original test
            MultiServiceTestCase exploration = cloneTestCase(original);
            
            // Update name
            String newName = original.getOperationId() + "_explore_" + suggestion.targetStatusCode;
            exploration.setOperationId(newName);
            
            // Mark as exploration test
            exploration.setStatusCodeExplorationTest(true);
            exploration.setTargetStatusCode(suggestion.targetStatusCode);
            
            // Determine which parameter changes to use:
            // 1. First preference: parameterChanges from LLM exploration response
            // 2. Fallback: suggestedInputs from StatusCodeTarget (from discovery phase)
            Map<String, String> paramChanges = new HashMap<>();
            if (!suggestion.parameterChanges.isEmpty()) {
                paramChanges.putAll(suggestion.parameterChanges);
                log.debug("Using {} parameter changes from LLM exploration response", paramChanges.size());
            } else if (statusCodeTarget != null && !statusCodeTarget.getSuggestedInputs().isEmpty()) {
                paramChanges.putAll(statusCodeTarget.getSuggestedInputs());
                log.info("Using {} suggested inputs from discovery phase for status {}", 
                    paramChanges.size(), suggestion.targetStatusCode);
            }
            
            // Apply parameter changes to the first step
            if (!exploration.getSteps().isEmpty() && !paramChanges.isEmpty()) {
                MultiServiceTestCase.StepCall originalStep = exploration.getSteps().get(0);
                
                log.info("Applying parameter changes for status {} exploration: {}", 
                    suggestion.targetStatusCode, paramChanges);
                
                // Create mutable copies of the maps
                Map<String, String> newPathParams = new LinkedHashMap<>(originalStep.getPathParams());
                Map<String, String> newQueryParams = new LinkedHashMap<>(originalStep.getQueryParams());
                Map<String, String> newBodyFields = new LinkedHashMap<>(originalStep.getBodyFields());
                
                for (Map.Entry<String, String> change : paramChanges.entrySet()) {
                    String paramName = change.getKey();
                    String paramValue = change.getValue();
                    
                    boolean applied = false;
                    
                    // Try to apply to path params
                    if (newPathParams.containsKey(paramName)) {
                        newPathParams.put(paramName, paramValue);
                        log.debug("Applied {} = {} to path params", paramName, paramValue);
                        applied = true;
                    }
                    
                    // Try to apply to query params
                    if (newQueryParams.containsKey(paramName)) {
                        newQueryParams.put(paramName, paramValue);
                        log.debug("Applied {} = {} to query params", paramName, paramValue);
                        applied = true;
                    }
                    
                    // Try to apply to body fields
                    if (newBodyFields.containsKey(paramName)) {
                        newBodyFields.put(paramName, paramValue);
                        log.debug("Applied {} = {} to body fields", paramName, paramValue);
                        applied = true;
                    }
                    
                    // If parameter wasn't found in existing maps, try to add to appropriate location
                    if (!applied) {
                        // For body-based APIs (POST/PUT/PATCH), add to body fields
                        String method = originalStep.getMethod().toString().toUpperCase();
                        if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH")) {
                            newBodyFields.put(paramName, paramValue);
                            log.debug("Added {} = {} to body fields (new parameter for {})", 
                                paramName, paramValue, method);
                        } else {
                            // For GET/DELETE, add to query params
                            newQueryParams.put(paramName, paramValue);
                            log.debug("Added {} = {} to query params (new parameter)", paramName, paramValue);
                        }
                    }
                }
                
                // Rebuild the body JSON from the modified body fields
                String newBody = originalStep.getBody();
                if (!newBodyFields.isEmpty()) {
                    JSONObject bodyJson = new JSONObject(newBodyFields);
                    newBody = bodyJson.toString();
                    log.info("Rebuilt request body for exploration: {}", newBody);
                }
                
                // Create a NEW StepCall with the modified parameters and body
                MultiServiceTestCase.StepCall modifiedStep = new MultiServiceTestCase.StepCall(
                    originalStep.getServiceName(),
                    originalStep.getMethod(),
                    originalStep.getPath(),
                    newPathParams,
                    newQueryParams,
                    new LinkedHashMap<>(originalStep.getHeaders()),
                    newBody,  // Use the newly constructed body
                    originalStep.getExpectedStatus(),
                    newBodyFields
                );
                
                // Copy over other properties
                for (String key : originalStep.getCaptureOutputKeys()) {
                    modifiedStep.addCaptureOutputKey(key);
                }
                
                // Replace the first step with the modified one
                exploration.getSteps().set(0, modifiedStep);
                
                log.info("Created modified step with new body: {}", newBody);
            } else if (paramChanges.isEmpty()) {
                log.warn("No parameter changes available for status {} exploration - test may not trigger expected status", 
                    suggestion.targetStatusCode);
            }
            
            // Handle auth manipulation if needed
            if (suggestion.requiresAuthManipulation) {
                StatusCodeTarget target = new StatusCodeTarget.Builder(suggestion.targetStatusCode)
                    .triggerStrategy(suggestion.strategy)
                    .requiresAuthManipulation(true)
                    .build();
                
                AuthManipulationStrategy.AuthConfig authConfig = 
                    authStrategy.getManipulatedAuth(target, null);
                exploration.setAuthManipulation(authConfig);
            }
            
            return exploration;
            
        } catch (Exception e) {
            log.error("Failed to create exploration test: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Clone a MultiServiceTestCase for modification.
     */
    private MultiServiceTestCase cloneTestCase(MultiServiceTestCase original) {
        MultiServiceTestCase clone = new MultiServiceTestCase(original.getOperationId());
        clone.setScenarioName(original.getScenarioName());
        clone.setFaulty(original.getFaulty());
        
        // Clone steps
        for (MultiServiceTestCase.StepCall originalStep : original.getSteps()) {
            MultiServiceTestCase.StepCall clonedStep = new MultiServiceTestCase.StepCall(
                originalStep.getServiceName(),
                originalStep.getMethod(),
                originalStep.getPath(),
                new LinkedHashMap<>(originalStep.getPathParams()),
                new LinkedHashMap<>(originalStep.getQueryParams()),
                new LinkedHashMap<>(originalStep.getHeaders()),
                originalStep.getBody(),
                originalStep.getExpectedStatus(),
                new LinkedHashMap<>(originalStep.getBodyFields())
            );
            
            // Clone capture output keys
            for (String key : originalStep.getCaptureOutputKeys()) {
                clonedStep.addCaptureOutputKey(key);
            }
            
            clone.addStepCall(clonedStep);
        }
        
        // Clone faulty parameters
        for (String faultyParam : original.getFaultyParameters()) {
            String[] parts = faultyParam.split("=", 2);
            if (parts.length == 2) {
                clone.addFaultyParameter(parts[0], parts[1]);
            }
        }
        
        return clone;
    }
    
    // Utility methods
    
    private String getApiKey(MultiServiceTestCase test) {
        if (test.getSteps().isEmpty()) return "UNKNOWN";
        MultiServiceTestCase.StepCall step = test.getSteps().get(0);
        String method = step.getMethod() != null ? step.getMethod().getMethod().toUpperCase() : "GET";
        return method + " " + step.getPath();
    }
    
    private String getServiceName(MultiServiceTestCase test) {
        if (test.getSteps().isEmpty()) return "unknown";
        return test.getSteps().get(0).getServiceName();
    }
    
    private List<ParameterInfo> getParameterInfos(MultiServiceTestCase test) {
        List<ParameterInfo> params = new ArrayList<>();
        if (test.getSteps().isEmpty()) return params;
        
        MultiServiceTestCase.StepCall step = test.getSteps().get(0);
        
        for (String key : step.getPathParams().keySet()) {
            ParameterInfo info = new ParameterInfo();
            info.setName(key);
            info.setInLocation("path");
            info.setType("string");
            params.add(info);
        }
        
        for (String key : step.getQueryParams().keySet()) {
            ParameterInfo info = new ParameterInfo();
            info.setName(key);
            info.setInLocation("query");
            info.setType("string");
            params.add(info);
        }
        
        return params;
    }
    
    private String extractJson(String response) {
        if (response == null) return null;
        
        // Remove markdown code blocks
        String cleaned = response.trim();
        if (cleaned.contains("```json")) {
            int start = cleaned.indexOf("```json") + 7;
            int end = cleaned.indexOf("```", start);
            if (end > start) {
                cleaned = cleaned.substring(start, end).trim();
            }
        } else if (cleaned.contains("```")) {
            int start = cleaned.indexOf("```") + 3;
            int end = cleaned.indexOf("```", start);
            if (end > start) {
                cleaned = cleaned.substring(start, end).trim();
            }
        }
        
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        
        if (start >= 0 && end > start) {
            return cleaned.substring(start, end + 1);
        }
        
        return null;
    }
    
    private String truncate(String str, int maxLen) {
        if (str == null) return "null";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen) + "...";
    }
    
    // Configuration setters
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setMaxExplorationTestsPerOriginal(int max) {
        this.maxExplorationTestsPerOriginal = max;
    }
    
    public void setMaxExplorationTestsPerRound(int max) {
        this.maxExplorationTestsPerRound = max;
    }
    
    public StatusCodeCoverageTracker getTracker() {
        return tracker;
    }
    
    /**
     * Start a new round (called between enhancer rounds).
     */
    public void startNewRound() {
        tracker.startNewRound();
    }
    
    // Inner classes
    
    /**
     * Suggestion for exploring a specific status code.
     */
    public static class ExplorationSuggestion {
        private int targetStatusCode;
        private String strategy = "";
        private Map<String, String> parameterChanges = new HashMap<>();
        private boolean requiresAuthManipulation = false;
        
        public int getTargetStatusCode() { return targetStatusCode; }
        public String getStrategy() { return strategy; }
        public Map<String, String> getParameterChanges() { return parameterChanges; }
        public boolean isRequiresAuthManipulation() { return requiresAuthManipulation; }
        
        @Override
        public String toString() {
            return String.format("ExplorationSuggestion{status=%d, params=%s, authManip=%s}",
                targetStatusCode, parameterChanges, requiresAuthManipulation);
        }
    }
    
    /**
     * Result of exploration for a single test execution round.
     */
    public static class ExplorationResult {
        private final List<MultiServiceTestCase> explorationTests;
        private final StatusCodeCoverageTracker.OverallCoverageSummary coverageSummary;
        
        public ExplorationResult(List<MultiServiceTestCase> explorationTests,
                                 StatusCodeCoverageTracker.OverallCoverageSummary coverageSummary) {
            this.explorationTests = explorationTests;
            this.coverageSummary = coverageSummary;
        }
        
        public List<MultiServiceTestCase> getExplorationTests() { return explorationTests; }
        public StatusCodeCoverageTracker.OverallCoverageSummary getCoverageSummary() { return coverageSummary; }
        
        @Override
        public String toString() {
            return String.format("ExplorationResult{tests=%d, %s}", 
                explorationTests.size(), coverageSummary);
        }
    }
    
    /**
     * Execution result for a test case.
     */
    public static class TestExecutionResult {
        private String testName;
        private String apiKey;
        private int actualStatusCode;
        private String responseBody;
        private boolean passed;
        private String errorMessage;
        private boolean softErrorDetected;
        
        public TestExecutionResult() {}
        
        public TestExecutionResult(String testName, String apiKey, int statusCode, 
                                   String responseBody, boolean passed) {
            this.testName = testName;
            this.apiKey = apiKey;
            this.actualStatusCode = statusCode;
            this.responseBody = responseBody;
            this.passed = passed;
        }
        
        // Getters and setters
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        
        public int getActualStatusCode() { return actualStatusCode; }
        public void setActualStatusCode(int actualStatusCode) { this.actualStatusCode = actualStatusCode; }
        
        public String getResponseBody() { return responseBody; }
        public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
        
        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public boolean isSoftErrorDetected() { return softErrorDetected; }
        public void setSoftErrorDetected(boolean softErrorDetected) { this.softErrorDetected = softErrorDetected; }
    }
}
