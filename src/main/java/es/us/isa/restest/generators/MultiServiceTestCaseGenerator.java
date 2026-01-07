package es.us.isa.restest.generators;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.inputs.smart.SmartInputFetcher;
import es.us.isa.restest.inputs.smart.SmartInputFetchConfig;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowStep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MultiServiceTestCaseGenerator extends AbstractTestCaseGenerator {

    /* ------------------------------------------------------------ */
    private static final Logger log = LogManager.getLogger(MultiServiceTestCaseGenerator.class);

    private final Map<String, OpenAPISpecification>          serviceSpecs;
    private final Map<String, TestConfigurationObject>       serviceConfigs;
    private final List<WorkflowScenario>                     scenarios;
    private final boolean                                    useLLM;
    private final AiDrivenLLMGenerator                       llmGen = new AiDrivenLLMGenerator();
    private final SemanticParameterExpander                 expander = new SemanticParameterExpander();
    
    // Configuration: when enabled, only generate first business step (writer keeps login as step 0)
    private final boolean                                    onlyFirstBusinessStep;
    
    // Smart Input Fetching System
    private SmartInputFetcher smartFetcher;
    private SmartInputFetchConfig smartFetchConfig;
    
    // Negative Test Generation System (tests with intentionally invalid inputs)
    private float faultyRatio;
    private boolean faultyRoundRobin = true;  // true = round-robin, false = random
    private Map<String, Map<String, es.us.isa.restest.inputs.InvalidInputPool>> faultyParameterPools = new HashMap<>();
    private Random random = new Random();
    
    // Track which parameter should have invalid value in current test case (round-robin mode)
    private List<String> parameterRotation = new ArrayList<>();
    private int currentFaultyParamIndex = 0;

    // Pattern to match HTTP operations in operation names
    private static final Pattern HTTP_OPERATION_PATTERN = 
        Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    public MultiServiceTestCaseGenerator(OpenAPISpecification primarySpec,
                                         TestConfigurationObject dummyPrimaryConf,
                                         Map<String, OpenAPISpecification> serviceSpecs,
                                         Map<String, TestConfigurationObject> serviceConfigs,
                                         List<WorkflowScenario> scenarios,
                                         boolean useLLMforParams,
                                         @SuppressWarnings("unused") boolean ignoreFlowsFlag) {

        /* we never call the AbstractTestCaseGenerator's generation loop,
           but super‑ctor still needs something sane */
        super(primarySpec, dummyPrimaryConf, scenarios.size());

        this.serviceSpecs     = serviceSpecs;
        this.serviceConfigs   = serviceConfigs;
        this.scenarios        = scenarios;
        this.useLLM           = useLLMforParams;
        this.onlyFirstBusinessStep = Boolean.parseBoolean(System.getProperty("mst.generate.only.first.step", "false"));
        this.faultyRatio = Float.parseFloat(System.getProperty("faulty.ratio", "0.1"));
        this.faultyRoundRobin = Boolean.parseBoolean(System.getProperty("faulty.round-robin", "true"));
        
        log.info("=== NEGATIVE TEST CONFIGURATION ===");
        log.info("faulty.ratio from system property: {}", System.getProperty("faulty.ratio", "0.1"));
        log.info("Parsed faultyRatio: {}", this.faultyRatio);
        log.info("This means {}% of test variants will be negative tests (invalid inputs)", this.faultyRatio * 100);
        log.info("Invalid parameter selection mode: {}", this.faultyRoundRobin ? "ROUND-ROBIN" : "RANDOM");
        
        // Initialize Smart Input Fetching System
        initializeSmartInputFetching();
    }

    /**
     * Initialize the Smart Input Fetching System
     */
    private void initializeSmartInputFetching() {
        try {
            log.info("🔧 Initializing Smart Input Fetching System for MultiServiceTestCaseGenerator...");
            
            // Load configuration from system properties
            Map<String, String> properties = new HashMap<>();
            System.getProperties().entrySet().stream()
                    .filter(entry -> entry.getKey().toString().startsWith("smart.input.fetch"))
                    .forEach(entry -> {
                        properties.put(entry.getKey().toString(), entry.getValue().toString());
                        log.debug("Found smart property: {} = {}", entry.getKey(), entry.getValue());
                    });
            
            // Also load base.url
            if (System.getProperty("base.url") != null) {
                properties.put("base.url", System.getProperty("base.url"));
                log.debug("Found base.url: {}", System.getProperty("base.url"));
            }
            
            if (properties.isEmpty()) {
                log.warn("❌ No smart input fetching properties found, using traditional LLM generation only");
                log.warn("   Make sure properties like 'smart.input.fetch.enabled=true' are in your properties file");
                return;
            }
            
            log.info("✅ Found {} smart input fetching properties", properties.size());
            for (String key : properties.keySet()) {
                log.info("   - {}: {}", key, properties.get(key));
            }
            
            smartFetchConfig = SmartInputFetchConfig.fromProperties(properties);
            
            if (smartFetchConfig.isEnabled()) {
                String baseUrl = properties.getOrDefault("base.url", "http://localhost:8080");
                smartFetcher = new SmartInputFetcher(smartFetchConfig, baseUrl);
                log.info("🚀 SmartInputFetcher initialized successfully!");
                log.info("   - Base URL: {}", baseUrl);
                log.info("   - Registry: {}", smartFetchConfig.getRegistryPath());
                log.info("   - Smart Fetch Percentage: {}%", 
                         smartFetchConfig.getSmartFetchPercentage() * 100);
                log.info("   - LLM Discovery: {}", smartFetchConfig.isLlmDiscoveryEnabled());
                log.info("🎯 YOU SHOULD NOW SEE 'Smart Fetch →' LOGS DURING PARAMETER GENERATION!");
            } else {
                log.warn("❌ Smart input fetching is DISABLED (smart.input.fetch.enabled=false)");
                log.warn("   Enable it by setting smart.input.fetch.enabled=true in your properties file");
                smartFetcher = null;
                smartFetchConfig = null;
            }
            
        } catch (Exception e) {
            log.error("❌ Failed to initialize Smart Input Fetching: {}", e.getMessage(), e);
            log.warn("Falling back to traditional LLM generation");
            smartFetcher = null;
            smartFetchConfig = null;
        }
    }

    /*  PUBLIC API – called by RESTest                              */

    // Shared parameter pools grouped by root API to avoid redundant LLM/semantic generation
    private Map<String, Map<String, List<String>>> sharedParameterPools = new HashMap<>();

    /** Produce test cases using two-stage LLM + semantic expansion approach. */
    @Override
    public Collection<TestCase> generate() {
        List<TestCase> out = new ArrayList<>();
        int counter = 1;

        // Pre-process: Group scenarios by root API and generate shared parameter pools
        log.info("=== PRE-PROCESSING: Grouping scenarios by root API ===");
        Map<String, List<WorkflowScenario>> groupedScenarios = groupScenariosByRootApi();
        
        // Generate shared parameter pools for each root API group
        generateSharedParameterPools(groupedScenarios);

        // Generate test cases using shared pools
        for (WorkflowScenario sc : scenarios) {
            // Generate multiple variants per scenario using shared parameter pools
            List<MultiServiceTestCase> variants = generateScenarioVariants(sc, counter);
            out.addAll(variants);
            counter += variants.size();
        }
        return out;
    }
    
    /**
     * Generate multiple test case variants for a single scenario using the two-stage approach:
     * 1. LLM generates initial seed values (5 per parameter)
     * 2. Semantic expansion generates additional variants using Word2Vec/BERT
     */
    private List<MultiServiceTestCase> generateScenarioVariants(WorkflowScenario sc, int baseCounter) {
        List<MultiServiceTestCase> variants = new ArrayList<>();
        
        // Read variant count from properties file or use default
        int variantCount = getVariantCountFromProperties();
        
        log.info("=== TWO-STAGE PARAMETER GENERATION TEST ===");
        log.info("Generating {} test case variants for scenario {}", variantCount, baseCounter);
        log.info("LLM enabled: {}, Semantic expansion enabled: {}", useLLM, useLLM);
        
        // Calculate negative test variants
        log.info("=== NEGATIVE TEST VARIANT CALCULATION ===");
        log.info("Total variants: {}", variantCount);
        log.info("Negative test ratio: {}", faultyRatio);
        log.info("Calculation: {} * {} = {}", variantCount, faultyRatio, variantCount * faultyRatio);
        int faultyCount = Math.round(variantCount * faultyRatio);
        log.info("Math.round({}) = {} negative test variants", variantCount * faultyRatio, faultyCount);
        
        Set<Integer> faultyVariantIndices = new HashSet<>();
        while (faultyVariantIndices.size() < faultyCount) {
            faultyVariantIndices.add(random.nextInt(variantCount));
        }
        log.info("Selected negative test variant indices (0-based): {}", faultyVariantIndices);
        log.info("Marking {} out of {} variants as negative tests", faultyCount, variantCount);
        
        // Initialize parameter rotation for round-robin invalid parameter selection
        String rootApiKey = getRootApiKeyForScenario(sc);
        initializeParameterRotation(rootApiKey);
        log.info("Parameter rotation initialized with {} parameters: {}", parameterRotation.size(), parameterRotation);
        
        // FIX: Skip negative tests for GET methods without parameters (nothing to make invalid)
        if (parameterRotation.isEmpty()) {
            String httpMethod = getFirstApiHttpMethod(sc);
            if ("GET".equalsIgnoreCase(httpMethod)) {
                log.info("⚠️ Skipping negative tests for GET method without parameters (nothing to invalidate)");
                faultyVariantIndices.clear(); // Remove all negative test indices
                faultyCount = 0;
            } else {
                log.warn("⚠️ No parameters found for {} method. Negative tests will have no invalid inputs.", httpMethod);
            }
        }
        
        // Determine scenario identifier based on first API call
            String firstApiName = getFirstApiOperationName(sc);
        String scenarioId;
        
            if (firstApiName != null && !firstApiName.isEmpty()) {
            // Make scenario ID unique by adding counter even when we have API name
            scenarioId = firstApiName.replaceAll("[^a-zA-Z0-9_]", "_")
                                                 .replaceAll("_+", "_")
                                       .replaceAll("^_|_$", "") + "_" + baseCounter;
            } else {
                String sourceFileName = sc.getSourceFileName();
                if (sourceFileName != null && !sourceFileName.isEmpty()) {
                scenarioId = sourceFileName.replaceAll("[^a-zA-Z0-9_]", "_") + "_" + baseCounter;
                } else {
                scenarioId = "Scenario_" + baseCounter;
                }
            }

        for (int v = 0; v < variantCount; v++) {
            boolean isFaultyVariant = faultyVariantIndices.contains(v);
            String testName = isFaultyVariant ? 
                "test_negative_" + scenarioId + "_" + (v + 1) :
                "test_" + scenarioId + "_" + (v + 1);
            
            MultiServiceTestCase tc = new MultiServiceTestCase(testName);
            tc.setScenarioName(scenarioId);
            tc.setFaulty(isFaultyVariant);
            
            // For negative test variants, determine which parameter(s) should have invalid values
            List<String> targetFaultyParams = new ArrayList<>();
            if (isFaultyVariant && !parameterRotation.isEmpty()) {
                if (faultyRoundRobin) {
                    // ROUND-ROBIN MODE: Select exactly ONE parameter in rotation
                    String singleParam = parameterRotation.get(currentFaultyParamIndex);
                    targetFaultyParams.add(singleParam);
                    log.info("🔴 [ROUND-ROBIN] Target invalid parameter for this variant: '{}'", singleParam);
                    // Move to next parameter for next negative test
                    currentFaultyParamIndex = (currentFaultyParamIndex + 1) % parameterRotation.size();
                } else {
                    // RANDOM MODE: Randomly select one or more parameters
                    int numFaultyParams = 1 + random.nextInt(Math.min(3, parameterRotation.size())); // 1 to 3 params
                    List<String> availableParams = new ArrayList<>(parameterRotation);
                    Collections.shuffle(availableParams, random);
                    targetFaultyParams.addAll(availableParams.subList(0, Math.min(numFaultyParams, availableParams.size())));
                    log.info("🔴 [RANDOM] Target invalid parameters for this variant ({} params): {}", 
                            targetFaultyParams.size(), targetFaultyParams);
                }
            }
            
            String faultyMarker = isFaultyVariant ? 
                "🔴 NEGATIVE TEST (invalid params: " + String.join(", ", targetFaultyParams) + ")" : "✅ POSITIVE TEST";
            log.info("--- Generating variant {}/{}: {} [{}] ---", (v + 1), variantCount, tc.getOperationId(), faultyMarker);
            
            Map<String,String> context = new HashMap<>();
            
            // Process workflow steps with variant-specific parameter generation
            for (WorkflowStep root : sc.getRootSteps()) {
                traverse(root, tc, context, "1", v, isFaultyVariant, targetFaultyParams);
            }
            
            // If configured, keep only the first business step (step 1). Login (step 0) is handled by writer
            if (onlyFirstBusinessStep && tc.getSteps().size() > 1) {
                log.info("First-step-only mode enabled: trimming scenario '{}' steps from {} to 1", scenarioId, tc.getSteps().size());
                tc.getSteps().subList(1, tc.getSteps().size()).clear();
            }

            // After processing, update scenario name based on actual first business step
            if (!tc.getSteps().isEmpty()) {
                MultiServiceTestCase.StepCall firstStep = tc.getSteps().get(0);
                String actualApiName = extractApiNameFromStep(firstStep);
                                 if (actualApiName != null && !actualApiName.isEmpty()) {
                     String improvedScenarioId = actualApiName + "_" + baseCounter;
                     tc.setScenarioName(improvedScenarioId);
                 }
            }
            
            // FIX: If marked as negative but no faulty parameters were actually set, convert to positive test
            // Also check if faulty parameters contain error markers (INVALID_VALUE_MISSING_, VAL_)
            boolean hasValidInvalidParams = false;
            if (isFaultyVariant && !tc.getFaultyParameters().isEmpty()) {
                // Check if any faulty parameter has a real invalid value (not an error marker)
                for (String faultyParam : tc.getFaultyParameters()) {
                    // Format is "paramName=value"
                    String value = faultyParam.contains("=") ? faultyParam.substring(faultyParam.indexOf("=") + 1) : faultyParam;
                    if (value != null && 
                        !value.startsWith("INVALID_VALUE_MISSING_") && 
                        !value.startsWith("VAL_") &&
                        !value.startsWith("STEP1_")) {
                        hasValidInvalidParams = true;
                        break;
                    }
                }
            }
            
            if (isFaultyVariant && (tc.getFaultyParameters().isEmpty() || !hasValidInvalidParams)) {
                log.warn("⚠️ Test variant {} was marked as NEGATIVE but no valid invalid parameters were set (pool exhausted or fallback used). Converting to POSITIVE test.", (v + 1));
                tc.setFaulty(false);
                tc.getFaultyParameters().clear(); // Clear error markers
                // Update test name to remove "negative" prefix
                String correctedName = tc.getOperationId().replace("test_negative_", "test_");
                tc.setOperationId(correctedName);
                log.info("✅ Renamed test from {} to {} (now POSITIVE)", testName, correctedName);
            }
            
            // DEBUG: Log invalid parameters
            if (tc.getFaulty()) {
                log.info("🔴 NEGATIVE TEST: {} has {} invalid parameters: {}", 
                        tc.getOperationId(), tc.getFaultyParameters().size(), tc.getFaultyParameters());
            }
            
            variants.add(tc);
            
            log.info("--- Completed variant {} with {} steps ---", v, tc.getSteps().size());
        }
        
        // Summary of generated variants
        long actualFaultyCount = variants.stream().filter(TestCase::getFaulty).count();
        long actualNormalCount = variants.size() - actualFaultyCount;
        log.info("=== GENERATION SUMMARY ===");
        log.info("Total variants generated: {}", variants.size());
        log.info("🔴 Negative test variants: {} ({}%)", actualFaultyCount, (actualFaultyCount * 100.0 / variants.size()));
        log.info("✅ Positive test variants: {} ({}%)", actualNormalCount, (actualNormalCount * 100.0 / variants.size()));
        log.info("Expected negative test ratio: {}%", faultyRatio * 100);
        
        return variants;
    }

    @Override
    protected Collection<TestCase> generateOperationTestCases(Operation op) { return Collections.emptyList(); }
    @Override
    public    TestCase              generateNextTestCase(Operation op)      { return null; }
    @Override
    protected boolean               hasNext()                               { return false; }

    /* ============================================================ */

    /**
     * Depth‑first traversal with hierarchical step numbering and variant-specific parameter generation.
     *
     * @param span      current WorkflowStep
     * @param tc        test‑case under construction
     * @param context   key→value outputs collected so far
     * @param stepNumber hierarchical step number (e.g., "1", "1.1", "1.2.1")
     * @param variantIndex index of current test variant for parameter selection
     * @param isFaultyVariant whether this test variant should use faulty parameters
     * @param targetFaultyParams list of parameter names that should be faulty (empty if not faulty test)
     */
    private void traverse(WorkflowStep span,
                          MultiServiceTestCase tc,
                          Map<String,String> context,
                          String stepNumber,
                          int variantIndex,
                          boolean isFaultyVariant,
                          List<String> targetFaultyParams) {

        // In first-step-only mode: if we've already added one business step, stop further traversal
        if (onlyFirstBusinessStep && !tc.getSteps().isEmpty()) {
            return;
        }

        /* 1. Extract HTTP operation info from span ---------------------------------- */
        final String service = span.getServiceName();
        final String opName  = span.getOperationName();

        // Try to extract HTTP method and path from various operation name formats
        String verb = null, route = null;
        
        // Check if it's an HTTP operation pattern (e.g., "POST /api/v1/path")
        Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
        if (httpMatcher.matches()) {
            verb = httpMatcher.group(1).toLowerCase(Locale.ROOT);
            route = httpMatcher.group(2);
        } else {
            // Check if we can extract from attributes/tags
            Map<String, String> outputs = span.getOutputFields();
            String httpMethod = outputs.get("http.method");
            String httpTarget = outputs.get("http.target");
            String httpUrl = outputs.get("http.url");
            
            if (httpMethod != null && (httpTarget != null || httpUrl != null)) {
                verb = httpMethod.toLowerCase(Locale.ROOT);
                route = httpTarget != null ? httpTarget : extractPathFromUrl(httpUrl);
            } else {
                            // Skip non-HTTP operations (internal spans, database calls, etc.)
            log.debug("Skipping non-HTTP span: {} - {}", service, opName);
            gotoChildren(span, tc, context, stepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
            return;
            }
        }

        if (verb == null || route == null) {
            log.debug("Could not extract HTTP method/path from span: {} - {}", service, opName);
            gotoChildren(span, tc, context, stepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
            return;
        }

        // Skip login/auth related operations (writer handles login as Step 0)
        if (isLoginOrAuthOperation(service, opName)) {
            log.debug("Skipping login/auth operation in generator: {} - {} {}", service, verb, route);
            gotoChildren(span, tc, context, stepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
            return;
        }

        /* 2. Load service‑specific test‑configuration ------------------------------ */
        TestConfigurationObject cfg = serviceConfigs.get(service);
        if (cfg == null) {
            log.warn("No test‑configuration for service '{}' (step {})", service, stepNumber);
            gotoChildren(span, tc, context, stepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
            return;
        }

        Operation opCfg = findOperation(cfg, verb, route);
        if (opCfg == null) {
            log.warn("No Operation config {} {} in service '{}' (step {})", verb, route, service, stepNumber);
            gotoChildren(span, tc, context, stepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
            return;
        }

        /* 3. Build parameter maps from trace data and LLM --------------------------- */
        Map<String,String> pathParams   = new LinkedHashMap<>();
        Map<String,String> queryParams  = new LinkedHashMap<>();
        Map<String,String> headerParams = new LinkedHashMap<>();
        Map<String,Object> bodyFields   = new LinkedHashMap<>();

        String resolvedPath = route;

        // Define step roles for parameter generation
        boolean isFirstBusinessStep = tc.getSteps().isEmpty();
        boolean isSubsequentStep = !isFirstBusinessStep;

        // Extract parameters from trace data only for subsequent steps
        if (isSubsequentStep) {
            extractParametersFromTrace(span, bodyFields, queryParams, pathParams, headerParams);
        }

        if (opCfg.getTestParameters() != null) {
            log.info("🔍 Processing {} parameters for step {} (firstBusiness: {}, subsequent: {})",
                    opCfg.getTestParameters().size(), stepNumber, isFirstBusinessStep, isSubsequentStep);

            // Collect all parameter names for this API (for LLM context)
            List<String> allParamNames = new java.util.ArrayList<>();
            for (TestParameter tp : opCfg.getTestParameters()) {
                allParamNames.add(tp.getName());
            }
            
            // Build API name for context (e.g., "POST /api/v1/adminorder")
            String apiName = (verb != null && route != null) ? verb.toUpperCase() + " " + route : opName;

            for (TestParameter p : opCfg.getTestParameters()) {
                log.info("📋 Parameter: {} (type: {}, in: {}, description: '{}')",
                        p.getName(), p.getType(), p.getIn(), p.getDescription());
                String val = null;  // For path/query/header params (must be strings)
                Object typedVal = null;  // For body params (can be typed objects)

                if (isFirstBusinessStep) {
                    /* Step 1 (First Business Step): Check if negative test, then use smart fetch or invalid values */
                    log.info("🎯 Step 1 parameter '{}' - attempting smart fetch", p.getName());
                    
                    boolean faultyValueSet = false;

                    if (useLLM) {
                        // Create ParameterInfo with full API context for better LLM generation
                        ParameterInfo info = createParameterInfoWithContext(p, apiName, service, allParamNames);
                        
                        // Check if this is a negative test variant AND this is one of the target invalid parameters
                        if (isFaultyVariant && targetFaultyParams != null && targetFaultyParams.contains(p.getName())) {
                            log.info("🔴 NEGATIVE TEST: Making parameter '{}' invalid (target param)", p.getName());
                            
                            // Use invalid value from faulty pool
                            // 🔥 FIX: Build rootApiKey directly from current step's verb and route
                            // instead of using getRootApiKeyForCurrentStep which can return wrong key
                            String rootApiKey = verb.toUpperCase() + "_" + route.replaceAll("[^a-zA-Z0-9_]", "_");
                            log.debug("Looking up faulty pool with key: '{}' (verb={}, route={})", rootApiKey, verb, route);
                            Map<String, es.us.isa.restest.inputs.InvalidInputPool> faultyPool = faultyParameterPools.get(rootApiKey);
                            
                            if (faultyPool != null && faultyPool.containsKey(p.getName())) {
                                es.us.isa.restest.inputs.InvalidInputPool pool = faultyPool.get(p.getName());
                                
                                // Get next invalid value based on mode
                                Object invalidValue;
                                if (faultyRoundRobin) {
                                    invalidValue = pool.getNextRoundRobin();
                                    if (invalidValue == null) {
                                        log.warn("⚠️ All invalid values exhausted for '{}' in round-robin mode. Skipping negative test.", p.getName());
                                        // Mark as not faulty variant - will generate positive test instead
                                        faultyValueSet = false;
                                    } else {
                                        // Get the invalid type that was selected for logging
                                        String invalidTypeName = pool.getLastSelectedType() != null 
                                                ? pool.getLastSelectedType().getDisplayName() 
                                                : "Unknown";
                                        
                                        // 🔥 FIX: For TYPE_MISMATCH, preserve the actual type (Integer, Boolean, etc.)
                                        // For body/formData params, store in typedVal; for path/query/header, convert to string
                                        if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                            typedVal = invalidValue; // Keep typed (Integer 123, not "123")
                                            val = convertObjectToString(invalidValue, p.getType()); // String for logging/tracking
                                        } else {
                                            // Path/query/header params must be strings (URL construction)
                                            val = convertObjectToString(invalidValue, p.getType());
                                        }
                                    tc.addFaultyParameter(p.getName(), val);
                                        faultyValueSet = true;
                                        log.info("✅ Negative Test (Round-Robin) → {} = {} [InvalidType: {}] (javaType: {}) - LOCKED", 
                                                p.getName(), 
                                                val.length() > 50 ? val.substring(0, 50) + "..." : val, 
                                                invalidTypeName,
                                                invalidValue.getClass().getSimpleName());
                                }
                            } else {
                                    // Random mode - can repeat
                                    invalidValue = pool.getRandomValue(random);
                                    if (invalidValue == null) {
                                        log.warn("⚠️ No invalid values in pool for '{}'", p.getName());
                                        faultyValueSet = false;
                                    } else {
                                        // 🔥 FIX: For TYPE_MISMATCH, preserve the actual type (Integer, Boolean, etc.)
                                        // For body/formData params, store in typedVal; for path/query/header, convert to string
                                        if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                            typedVal = invalidValue; // Keep typed (Integer 123, not "123")
                                            val = convertObjectToString(invalidValue, p.getType()); // String for logging/tracking
                                        } else {
                                            // Path/query/header params must be strings (URL construction)
                                            val = convertObjectToString(invalidValue, p.getType());
                                        }
                                        tc.addFaultyParameter(p.getName(), val);
                                        faultyValueSet = true;
                                        log.info("✅ Negative Test (Random) → {} = {} (type: {}, intentionally invalid) - LOCKED", 
                                                p.getName(), 
                                                val.length() > 50 ? val.substring(0, 50) + "..." : val, 
                                                invalidValue.getClass().getSimpleName());
                                    }
                                }
                            } else {
                                log.warn("⚠️ No invalid value pool found for rootApiKey='{}' or parameter='{}'", rootApiKey, p.getName());
                            }
                        }

                        // Try Smart Input Fetching first for step 1 parameters
                        // CRITICAL: Skip smart fetch for negative test parameters - they must use invalid values only
                        boolean isTargetNegativeParam = isFaultyVariant && targetFaultyParams != null && targetFaultyParams.contains(p.getName());
                        
                        if (!faultyValueSet && !isTargetNegativeParam && val == null && smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                            log.info("🚀 Calling smart fetch for step 1 parameter '{}'", p.getName());
                            try {
                                String smartFetchValue = smartFetcher.fetchSmartInput(info);
                                if (smartFetchValue != null && !smartFetchValue.trim().isEmpty()) {
                                    // Convert to proper type for body/formData params, keep as string for path/query/header
                                    if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                        typedVal = convertStringToTypedValue(smartFetchValue, p);
                                        val = smartFetchValue;  // Keep string representation for logging
                                        log.info("Smart Fetch (Step 1) → {} {} = {} (type: {}) ✅", 
                                                service, p.getName(), typedVal, typedVal.getClass().getSimpleName());
                                    } else {
                                        val = smartFetchValue;
                                        log.info("Smart Fetch (Step 1) → {} {} = {} ✅", 
                                                service, p.getName(), val);
                                    }
                                } else {
                                    log.info("Smart Fetch (Step 1) → {} {} = NULL, falling back to LLM", service, p.getName());
                                    val = null; // Fall back to LLM
                                }
                            } catch (Exception e) {
                                log.warn("Smart fetching failed for step 1 {}.{}, falling back to LLM: {}",
                                         service, p.getName(), e.getMessage());
                                val = null; // Fall back to LLM
                            }
                        } else {
                            log.warn("❌ Smart fetch not available for step 1 parameter '{}' (fetcher: {}, config: {}, enabled: {})",
                                    p.getName(), smartFetcher != null, smartFetchConfig != null,
                                    smartFetchConfig != null ? smartFetchConfig.isEnabled() : "N/A");
                        }

                        // Fall back to traditional LLM generation if smart fetching didn't work
                        // Skip LLM generation for negative test parameters that failed to get invalid value
                        if (!faultyValueSet && !isTargetNegativeParam && val == null && typedVal == null) {
                            List<String> vals = llmGen.generateParameterValues(info);
                            // 🔄 FIX: Rotate through cached values instead of always using first value
                            String llmValue;
                            if (vals.isEmpty()) {
                                llmValue = "LLM_EMPTY_" + p.getName();
                            } else if (vals.size() == 1) {
                                llmValue = vals.get(0);
                            } else {
                                // Rotate through the cached values using variant index
                                int rotationIndex = (variantIndex % vals.size());
                                llmValue = vals.get(rotationIndex);
                                log.debug("🔄 Rotated to LLM value [{}] for '{}' (Step 1): {}", rotationIndex, p.getName(), llmValue);
                            }
                            // Convert to proper type for body/formData params, keep as string for path/query/header
                            if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                typedVal = convertStringToTypedValue(llmValue, p);
                                val = llmValue;  // Keep string for logging
                                log.info("LLM (Step 1 Fallback) → {} {} = {} (type: {})", 
                                        service, p.getName(), typedVal, typedVal.getClass().getSimpleName());
                            } else {
                                val = llmValue;
                                log.info("LLM (Step 1 Fallback) → {} {} = {}", 
                                        service, p.getName(), val);
                            }
                        }
                        
                        // 🔥 FIX: If negative test parameter failed to get invalid value, get a VALID value instead
                        // This ensures the parameter is included when test converts to positive
                        if (isTargetNegativeParam && !faultyValueSet && val == null && typedVal == null) {
                            log.warn("⚠️ Negative test parameter '{}' failed to get invalid value. Getting valid value instead (test will convert to positive).", p.getName());
                            
                            // Try smart fetch to get a valid value
                            if (smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                                try {
                                    String smartFetchValue = smartFetcher.fetchSmartInput(info);
                                    if (smartFetchValue != null && !smartFetchValue.trim().isEmpty()) {
                                        if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                            typedVal = convertStringToTypedValue(smartFetchValue, p);
                                            val = smartFetchValue;
                                            log.info("Smart Fetch (Fallback for failed negative) → {} {} = {} (type: {}) ✅", 
                                                    service, p.getName(), typedVal, typedVal.getClass().getSimpleName());
                                        } else {
                                            val = smartFetchValue;
                                            log.info("Smart Fetch (Fallback for failed negative) → {} {} = {} ✅", 
                                                    service, p.getName(), val);
                                        }
                                    }
                                } catch (Exception e) {
                                    log.warn("Smart fetch fallback failed for '{}': {}", p.getName(), e.getMessage());
                                }
                            }
                            
                            // Try LLM if smart fetch didn't work
                            if (val == null && typedVal == null) {
                                List<String> vals = llmGen.generateParameterValues(info);
                                String llmValue = vals.isEmpty() ? "FALLBACK_" + p.getName() : vals.get(variantIndex % Math.max(1, vals.size()));
                                if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                    typedVal = convertStringToTypedValue(llmValue, p);
                                    val = llmValue;
                                    log.info("LLM (Fallback for failed negative) → {} {} = {} (type: {})", 
                                            service, p.getName(), typedVal, typedVal.getClass().getSimpleName());
                                } else {
                                    val = llmValue;
                                    log.info("LLM (Fallback for failed negative) → {} {} = {}", 
                                            service, p.getName(), val);
                                }
                            }
                        }
                    } else {
                        if (!faultyValueSet) {
                            log.info("🚫 LLM disabled for step 1 parameter '{}'", p.getName());
                            val = "STEP1_" + p.getName() + "_v" + variantIndex;
                        }
                    }
                } else {
                    /* Subsequent Steps (2+): Check dependencies first, then use smart fetch for independent parameters */

                    /* 3a. Use previously captured OUTPUT value from context (dependency) ------ */
                    val = context.get(p.getName());
                    if (val != null) {
                        log.info("Dependency (Output) → {} {} = {} (from previous step output, step {})",
                                service, p.getName(), val, stepNumber);
                    }

                    /* 3b. Use previously captured INPUT value for consistency --------------- */
                    if (val == null) {
                        val = context.get("input." + p.getName());
                        if (val != null) {
                            log.info("Dependency (Input) → {} {} = {} (reusing from previous API input, step {})",
                                    service, p.getName(), val, stepNumber);
                        }
                    }

                    /* 3c. Use trace data if available and no context value ------------- */
                    // CRITICAL: Skip trace data for negative test parameters - they must use invalid values only
                    boolean isTargetNegativeParam = isFaultyVariant && targetFaultyParams != null && targetFaultyParams.contains(p.getName());
                    
                    if (val == null && !isTargetNegativeParam) {
                        val = getTraceParameterValue(span, p.getName());
                        if (val != null) {
                            log.info("Trace Data → {} {} = {} (from trace, step {})",
                                    service, p.getName(), val, stepNumber);
                        }
                    }

                    /* 3d. Parameter is INDEPENDENT - use Smart Input Fetching or LLM */
                    // CRITICAL: Skip smart fetch for negative test parameters - they must use invalid values only
                    if (val == null && !isTargetNegativeParam && useLLM) {
                        log.info("Parameter '{}' is INDEPENDENT in step {} - generating new value", p.getName(), stepNumber);

                        // Create ParameterInfo with full API context for better LLM generation
                        ParameterInfo info = createParameterInfoWithContext(p, apiName, service, allParamNames);

                        // Try Smart Input Fetching first for independent parameters
                        if (smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                            try {
                                String smartFetchValue = smartFetcher.fetchSmartInput(info);
                                if (smartFetchValue != null && !smartFetchValue.trim().isEmpty()) {
                                    // Convert to proper type for body/formData params, keep as string for path/query/header
                                    if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                        typedVal = convertStringToTypedValue(smartFetchValue, p);
                                        val = smartFetchValue;
                                        log.info("Smart Fetch (Independent) → {} {} = {} (type: {}) ✅ (step {})", 
                                                service, p.getName(), typedVal, typedVal.getClass().getSimpleName(), stepNumber);
                                    } else {
                                        val = smartFetchValue;
                                        log.info("Smart Fetch (Independent) → {} {} = {} ✅ (step {})", 
                                                service, p.getName(), val, stepNumber);
                                    }
                                } else {
                                    val = null; // Ensure we fall back to LLM
                                }
                            } catch (Exception e) {
                                log.debug("Smart fetching failed for independent parameter {}.{}, falling back to LLM: {}",
                                         service, p.getName(), e.getMessage());
                                val = null; // Ensure we fall back to LLM
                            }
                        }

                        // Fall back to traditional LLM generation if smart fetching didn't work
                        if (val == null && typedVal == null) {
                            List<String> vals = llmGen.generateParameterValues(info);
                            // 🔄 FIX: Rotate through cached values instead of always using first value
                            String llmValue;
                            if (vals.isEmpty()) {
                                llmValue = "LLM_EMPTY";
                            } else if (vals.size() == 1) {
                                llmValue = vals.get(0);
                            } else {
                                // Rotate through the cached values using variant index
                                int rotationIndex = (variantIndex % vals.size());
                                llmValue = vals.get(rotationIndex);
                                log.debug("🔄 Rotated to LLM value [{}] for '{}': {}", rotationIndex, p.getName(), llmValue);
                            }
                            // Convert to proper type for body/formData params, keep as string for path/query/header
                            if (p.getIn() != null && (p.getIn().equalsIgnoreCase("body") || p.getIn().equalsIgnoreCase("formData"))) {
                                typedVal = convertStringToTypedValue(llmValue, p);
                                val = llmValue;
                                log.info("LLM (Independent Fallback) → {} {} = {} (type: {}) (step {})", 
                                        service, p.getName(), typedVal, typedVal.getClass().getSimpleName(), stepNumber);
                            } else {
                                val = llmValue;
                                log.info("LLM (Independent Fallback) → {} {} = {} (step {})", 
                                        service, p.getName(), val, stepNumber);
                            }
                        }
                    }

                    /* 3e. Error handling for negative test parameters without invalid values */
                    if (val == null && isTargetNegativeParam) {
                        log.error("❌ CRITICAL: Negative test parameter '{}' in step {} has no invalid value. Skipping for negative testing.", 
                                p.getName(), stepNumber);
                        // DON'T add error fallback - let it remain null to trigger positive test conversion
                    }
                    
                    /* 3f. Ultimate fallback ---------------------------------------- */
                    if (val == null) val = "VAL_" + p.getName();
                }

                /* 3e. Store in correct container ----------------------------------- */
                // Skip parameters with null values (e.g., failed negative test params that will trigger test conversion)
                if (val == null && typedVal == null) {
                    log.warn("⚠️ Skipping parameter '{}' - no value available (will trigger test type conversion if needed)", p.getName());
                    continue;
                }
                
                // Use typedVal for body params (already converted), val for path/query/header (strings)
                switch (p.getIn().toLowerCase(Locale.ROOT)) {
                    case "path":
                        pathParams.put(p.getName(), val); // Path params must be strings for URL construction
                        if (val != null) {
                            // 🔥 FIX: URL-encode path parameter values for proper handling of whitespace and special characters
                            // This is critical for EMPTY_INPUT testing where values like " ", "\t", "\n" must be encoded
                            String encodedVal;
                            try {
                                encodedVal = java.net.URLEncoder.encode(val, java.nio.charset.StandardCharsets.UTF_8)
                                        .replace("+", "%20"); // URLEncoder uses + for space, but URL paths need %20
                            } catch (Exception e) {
                                log.warn("Failed to URL-encode path parameter '{}' value '{}': {}", p.getName(), val, e.getMessage());
                                encodedVal = val; // Fall back to original value
                            }
                            resolvedPath = resolvedPath.replace("{"+p.getName()+"}", encodedVal);
                            if (!val.equals(encodedVal)) {
                                log.debug("Path parameter '{}' URL-encoded: '{}' -> '{}'", p.getName(), val, encodedVal);
                            }
                        }
                        break;
                    case "query":
                        queryParams.put(p.getName(), val); // Query params must be strings for URL construction
                        break;
                    case "header":
                        headerParams.put(p.getName(), val); // Header params must be strings
                        break;
                    case "body":
                    case "formdata":
                        // Use typedVal if available (already typed), otherwise use val
                        Object bodyValue = (typedVal != null) ? typedVal : val;
                        bodyFields.put(p.getName(), bodyValue); // Body fields can be typed objects
                        log.debug("Storing body parameter '{}' = {} (type: {})", 
                                p.getName(), bodyValue, bodyValue != null ? bodyValue.getClass().getSimpleName() : "null");
                        break;
                }
            }
        }

        // For first step, always use LLM-generated body fields, not trace body
        String rawBody = span.getInputFields().get("http.request.body");
        String bodyJson;
        
        if (isFirstBusinessStep) {
            // For login step and first business step, always use generated bodyFields
            bodyJson = bodyFields.isEmpty() ? null : generateRequestBody(bodyFields, opCfg);
            log.info("Using generated body for step {}: {}", stepNumber, bodyJson);
        } else {
            // For subsequent steps, prefer trace body, fallback to generated fields
            bodyJson = rawBody != null
                    ? rawBody
                    : (bodyFields.isEmpty() ? null : generateRequestBody(bodyFields, opCfg));
        }

        /* 4. Expected status from configuration (no hardcoding) ------------------- */
        int expectedStatus = 200; // Default fallback only
        
        // Priority 1: Read expected status from configuration file
        try {
            if (opCfg.getExpectedResponse() != null && !opCfg.getExpectedResponse().trim().isEmpty()) {
                expectedStatus = Integer.parseInt(opCfg.getExpectedResponse().trim());
                log.debug("Using configured expected status {} for {} {}", expectedStatus, verb, route);
            } else {
                log.warn("No expected status configured for {} {} - using default 200", verb, route);
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid expected status '{}' in config for {} {} - using default 200", 
                    opCfg.getExpectedResponse(), verb, route);
        }

        // Priority 2: If configured status is still 200, check trace for actual successful status
        // Only use trace status if config doesn't specify a different expected value
        Object recorded = span.getOutputFields().get("http.status_code");
        if (recorded != null && expectedStatus == 200) {
            try {
                int traceStatus = Integer.parseInt(recorded.toString());
                if (traceStatus >= 200 && traceStatus < 300) {
                    expectedStatus = traceStatus;
                    log.debug("Using successful trace status {} for {} {}", traceStatus, verb, route);
                } else {
                    log.debug("Trace shows error status {}, keeping configured expected status {}", traceStatus, expectedStatus);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid status code in trace: {}", recorded);
            }
        }

        /* 5. Create the StepCall with hierarchical naming -------------------------- */
        MultiServiceTestCase.StepCall call = new MultiServiceTestCase.StepCall(
                service,                      // serviceName
                opCfg,                        // Operation cfg
                resolvedPath,                 // resolved URI
                pathParams,
                queryParams,
                headerParams,
                bodyJson,
                expectedStatus,
                convertObjectMapToStringMap(bodyFields)
        );

        // Set step dependencies based on trace relationships
        setStepDependencies(call, span, tc.getSteps().size());

        System.out.println(">> Step " + stepNumber + ": " + span.getServiceName() + " "
                + verb.toUpperCase() + " " + route
                + " body=" + call.getBody()
                + " expected=" + call.getExpectedStatus());
        
        /* Capture output fields for downstream steps */
        for (String key : span.getOutputFields().keySet()) {
            if (!key.startsWith("http.")) {
                call.addCaptureOutputKey(key);
            }
        }

        tc.addStepCall(call);

        /* 6. Update context with outputs ------------------------------------------- */
        context.putAll(span.getOutputFields());
        
        /* 6b. Update context with inputs for consistency across subsequent steps --- */
        // Store all input parameters used in this step for consistency in future steps
        storeUsedInputsInContext(context, pathParams, queryParams, headerParams, bodyFields);
        
        log.debug("Step {}: Stored {} input parameters and {} output fields in context for consistency", 
                stepNumber, 
                pathParams.size() + queryParams.size() + headerParams.size() + bodyFields.size(),
                span.getOutputFields().size());

        /* 7. Process children with hierarchical numbering -------------------------- */
        if (onlyFirstBusinessStep) {
            // Do not traverse further in first-step-only mode
            return;
        }
        gotoChildren(span, tc, context, stepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
    }

    /**
     * Helper method to create ParameterInfo from TestParameter with full API context
     */
    private ParameterInfo createParameterInfo(TestParameter p) {
        ParameterInfo info = new ParameterInfo();
        info.setName(p.getName());
        info.setDescription(p.getDescription());
        info.setInLocation(p.getIn());
        info.setType(p.getType());
        info.setFormat(p.getFormat());
        info.setSchemaType(p.getType());
        info.setSchemaExample(p.getExample() != null ? p.getExample().toString() : "");
        info.setRegex(p.getPattern());
        info.setRequired(p.getRequired());
        return info;
    }
    
    /**
     * Enhanced helper method to create ParameterInfo with full API context for better LLM generation
     */
    private ParameterInfo createParameterInfoWithContext(TestParameter p, String apiName, String serviceName, List<String> allParamNames) {
        ParameterInfo info = createParameterInfo(p);
        info.setApiName(apiName);
        info.setServiceName(serviceName);
        info.setAllParameterNames(allParamNames);
        return info;
    }

    /**
     * Helper method to convert Map<String,Object> to Map<String,String>
     */
    private Map<String,String> convertObjectMapToStringMap(Map<String,Object> objectMap) {
        Map<String,String> stringMap = new LinkedHashMap<>();
        for (Map.Entry<String,Object> entry : objectMap.entrySet()) {
            Object value = entry.getValue();
            // Handle null values (e.g., from faulty test cases)
            stringMap.put(entry.getKey(), value == null ? null : value.toString());
        }
        return stringMap;
    }

    /**
     * Extract parameter values from trace input/output fields.
     */
    private void extractParametersFromTrace(WorkflowStep span,
                                           Map<String,Object> bodyFields,
                                           Map<String,String> queryParams,
                                           Map<String,String> pathParams,
                                           Map<String,String> headerParams) {
        // Extract from request body
        String requestBody = span.getInputFields().get("http.request.body");
        if (requestBody != null && !requestBody.trim().isEmpty()) {
            try {
                // Try to parse JSON body
                org.json.JSONObject jsonBody = new org.json.JSONObject(requestBody);
                for (String key : jsonBody.keySet()) {
                    Object value = jsonBody.get(key);
                    bodyFields.put(key, value.toString());
                }
            } catch (Exception e) {
                // If not JSON, try form data parsing
                parseFormDataToObjectMap(requestBody, bodyFields);
            }
        }
        
        // Extract query parameters from URL
        String httpUrl = span.getInputFields().get("http.url");
        if (httpUrl != null && httpUrl.contains("?")) {
            String queryString = httpUrl.substring(httpUrl.indexOf("?") + 1);
            parseFormData(queryString, queryParams);
        }
    }

    /**
     * Get parameter value from trace data.
     */
    private String getTraceParameterValue(WorkflowStep span, String paramName) {
        // Check input fields first
        String value = span.getInputFields().get(paramName);
        if (value != null) return value;
        
        // Check output fields
        value = span.getOutputFields().get(paramName);
        if (value != null) return value;
        
        return null;
    }

    /**
     * Parse form data or query string into key-value pairs.
     */
    private void parseFormData(String data, Map<String,String> target) {
        if (data == null || data.trim().isEmpty()) return;

        String[] pairs = data.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                try {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    target.put(key, value);
                } catch (Exception e) {
                    target.put(kv[0], kv[1]);
                }
            }
        }
    }

    /**
     * Parse form data or query string into key-value pairs (Object version).
     */
    private void parseFormDataToObjectMap(String data, Map<String,Object> target) {
        if (data == null || data.trim().isEmpty()) return;

        String[] pairs = data.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                try {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    target.put(key, value);
                } catch (Exception e) {
                    target.put(kv[0], kv[1]);
                }
            }
        }
    }

    /**
     * Set step dependencies based on trace relationships.
     * This analyzes trace data to determine different types of dependencies:
     * 1. DATA_DEPENDENCY: Step needs output data from a previous step (skip if dependency fails)
     * 2. WORKFLOW_DEPENDENCY: Step is part of a logical sequence (skip if workflow predecessors fail)
     * 3. INDEPENDENT: Step can execute regardless of other step failures
     */
    private void setStepDependencies(MultiServiceTestCase.StepCall call, 
                                   WorkflowStep span, 
                                   int currentStepIndex) {
        log.debug("Analyzing dependencies for step {}: {} {}", 
                currentStepIndex, span.getServiceName(), span.getOperationName());
        
        // Get all previous steps in the test case for dependency analysis
        List<MultiServiceTestCase.StepCall> previousSteps = getCurrentTestSteps();
        
        // Analyze trace-based dependencies
        analyzeDependencies(call, span, currentStepIndex, previousSteps);
    }
    
    /**
     * Analyze and categorize dependencies between the current step and previous steps
     */
    private void analyzeDependencies(MultiServiceTestCase.StepCall currentCall,
                                   WorkflowStep currentSpan,
                                   int currentStepIndex,
                                   List<MultiServiceTestCase.StepCall> previousSteps) {
        
        // Track what we find
        boolean hasDataDependency = false;
        boolean hasWorkflowDependency = false;
        
        for (int i = 0; i < previousSteps.size(); i++) {
            MultiServiceTestCase.StepCall previousCall = previousSteps.get(i);
            
            // Get the corresponding WorkflowStep for the previous call (if available)
            WorkflowStep previousSpan = findCorrespondingSpan(previousCall, currentSpan);
            
            if (previousSpan != null) {
                // Check for data dependencies (output -> input field matching)
                Map<String, String> dataMatches = findDataDependencies(previousSpan, currentSpan);
                if (!dataMatches.isEmpty()) {
                    hasDataDependency = true;
                    for (Map.Entry<String, String> match : dataMatches.entrySet()) {
                        currentCall.addParamDependency(match.getKey(), i + 1, match.getValue());
                        log.info("DATA_DEPENDENCY: Step {} param '{}' depends on Step {} output '{}'",
                                currentStepIndex, match.getKey(), i + 1, match.getValue());
                    }
                }
                
                // Check for workflow dependencies (parent-child relationships in trace)
                if (isWorkflowDependent(previousSpan, currentSpan)) {
                    hasWorkflowDependency = true;
                    currentCall.addWorkflowDependency(i + 1);
                    log.info("WORKFLOW_DEPENDENCY: Step {} depends on workflow Step {}",
                            currentStepIndex, i + 1);
                }
            }
            
            // Check for service-level dependencies (same service, likely sequential)
            if (isSameServiceDependency(previousCall, currentCall)) {
                hasWorkflowDependency = true;
                currentCall.addWorkflowDependency(i + 1);
                log.info("SERVICE_DEPENDENCY: Step {} (same service dependency) depends on Step {}",
                        currentStepIndex, i + 1);
            }
        }
        
        // Set the overall dependency type
        if (hasDataDependency) {
            currentCall.setDependencyType(MultiServiceTestCase.DependencyType.DATA_DEPENDENCY);
            log.info("Step {} classified as DATA_DEPENDENT", currentStepIndex);
        } else if (hasWorkflowDependency) {
            currentCall.setDependencyType(MultiServiceTestCase.DependencyType.WORKFLOW_DEPENDENCY);
            log.info("Step {} classified as WORKFLOW_DEPENDENT", currentStepIndex);
        } else {
            currentCall.setDependencyType(MultiServiceTestCase.DependencyType.INDEPENDENT);
            log.info("Step {} classified as INDEPENDENT", currentStepIndex);
        }
    }
    
    /**
     * Find data dependencies by matching output fields from previous step to input fields of current step
     */
    private Map<String, String> findDataDependencies(WorkflowStep previousSpan, WorkflowStep currentSpan) {
        Map<String, String> dependencies = new LinkedHashMap<>();
        
        // Set of fields to ignore for dependency matching (too common/generic)
        Set<String> ignoreFields = Set.of(
                "http.status_code", "status_code", "timestamp", "value", 
                "id", "type", "version", "success", "error", "message"
        );
        
        Map<String, String> previousOutputs = previousSpan.getOutputFields();
        Map<String, String> currentInputs = currentSpan.getInputFields();
        
        for (Map.Entry<String, String> output : previousOutputs.entrySet()) {
            String outputKey = output.getKey();
            String outputValue = output.getValue();
            
            // Skip ignored fields and empty values
            if (ignoreFields.contains(outputKey) || outputValue == null || outputValue.isEmpty()) {
                continue;
            }
            
            // Check if this output value appears in current step's inputs
            for (Map.Entry<String, String> input : currentInputs.entrySet()) {
                String inputKey = input.getKey();
                String inputValue = input.getValue();
                
                if (inputValue != null && inputValue.equals(outputValue)) {
                    dependencies.put(inputKey, outputKey);
                    log.debug("Found data dependency: {} ({}) -> {} ({})", 
                            outputKey, outputValue, inputKey, inputValue);
                }
            }
        }
        
        return dependencies;
    }
    
    /**
     * Check if current step is workflow-dependent on previous step based on trace relationships
     */
    private boolean isWorkflowDependent(WorkflowStep previousSpan, WorkflowStep currentSpan) {
        // Check if currentSpan is a child of previousSpan in the trace hierarchy
        WorkflowStep parent = currentSpan.getParent();
        while (parent != null) {
            if (parent.equals(previousSpan)) {
                return true;
            }
            parent = parent.getParent();
        }
        
        // Check if they're in the same trace and sequential
        if (previousSpan.getTraceId().equals(currentSpan.getTraceId())) {
            // If in same trace and current starts after previous ends, it's likely workflow dependent
            return currentSpan.getStartTime() >= previousSpan.getEndTime();
        }
        
        return false;
    }
    
    /**
     * Check if steps are from the same service and likely sequential
     */
    private boolean isSameServiceDependency(MultiServiceTestCase.StepCall previousCall, 
                                          MultiServiceTestCase.StepCall currentCall) {
        // Steps from the same service are often workflow dependent
        return previousCall.getServiceName().equals(currentCall.getServiceName());
    }
    
    /**
     * Find the WorkflowStep that corresponds to a given StepCall
     */
    private WorkflowStep findCorrespondingSpan(MultiServiceTestCase.StepCall call, WorkflowStep contextSpan) {
        // This is a simplified implementation - in a full implementation,
        // you would maintain a mapping between StepCalls and WorkflowSteps
        // For now, we'll use the contextSpan's siblings and parents
        
        // Check if the call matches the current context span
        if (matchesStep(call, contextSpan)) {
            return contextSpan;
        }
        
        // Check siblings and ancestors
        WorkflowStep parent = contextSpan.getParent();
        if (parent != null) {
            for (WorkflowStep sibling : parent.getChildren()) {
                if (matchesStep(call, sibling)) {
                    return sibling;
                }
            }
        }
        
        return null; // Not found
    }
    
    /**
     * Check if a StepCall matches a WorkflowStep
     */
    private boolean matchesStep(MultiServiceTestCase.StepCall call, WorkflowStep span) {
        return call.getServiceName().equals(span.getServiceName()) &&
               call.getPath() != null && 
               span.getOperationName().contains(call.getPath().replaceFirst("^/+", ""));
    }
    
    /**
     * Get current test steps (implementation depends on how you track the current test being built)
     */
    private List<MultiServiceTestCase.StepCall> getCurrentTestSteps() {
        // This would need to be implemented based on your current test case building context
        // For now, return empty list - this method would be properly implemented 
        // with access to the current MultiServiceTestCase being built
        return new ArrayList<>();
    }

    /**
     * Store all input parameters used in this step in the context for consistency in subsequent steps.
     * This ensures that if the same parameter is needed again (e.g., loginId), we reuse the same value
     * instead of generating a new one, maintaining consistency across the test case.
     */
    private void storeUsedInputsInContext(Map<String, String> context,
                                         Map<String, String> pathParams,
                                         Map<String, String> queryParams,
                                         Map<String, String> headerParams,
                                         Map<String, Object> bodyFields) {
        // Store path parameters with "input." prefix for consistency tracking
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            context.put("input." + entry.getKey(), entry.getValue());
        }
        
        // Store query parameters with "input." prefix for consistency tracking  
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            context.put("input." + entry.getKey(), entry.getValue());
        }
        
        // Store header parameters with "input." prefix for consistency tracking
        for (Map.Entry<String, String> entry : headerParams.entrySet()) {
            context.put("input." + entry.getKey(), entry.getValue());
        }
        
        // Store body fields with "input." prefix for consistency tracking
        for (Map.Entry<String, Object> entry : bodyFields.entrySet()) {
            Object value = entry.getValue();
            // Handle null values (e.g., from faulty test cases)
            context.put("input." + entry.getKey(), value == null ? null : value.toString());
        }
    }

    /**
     * Extract path from full URL.
     */
    private String extractPathFromUrl(String url) {
        if (url == null) return null;
        try {
            java.net.URL parsed = new java.net.URL(url);
            return parsed.getPath();
        } catch (Exception e) {
            // If URL parsing fails, try to extract path manually
            int pathStart = url.indexOf("://");
            if (pathStart >= 0) {
                int pathBegin = url.indexOf("/", pathStart + 3);
                if (pathBegin >= 0) {
                    int queryStart = url.indexOf("?", pathBegin);
                    return queryStart >= 0 ? url.substring(pathBegin, queryStart) : url.substring(pathBegin);
                }
            }
            return url;
        }
    }

    /** Detect login/auth related operations to skip as generator steps */
    private boolean isLoginOrAuthOperation(String service, String opName) {
        if (service == null && opName == null) return false;
        String s = service != null ? service.toLowerCase(Locale.ROOT) : "";
        String o = opName != null ? opName.toLowerCase(Locale.ROOT) : "";
        return s.contains("login") || s.contains("auth") || s.contains("signin") || s.contains("token")
                || o.contains("login") || o.contains("auth") || o.contains("signin") || o.contains("token");
    }

    /**
     * Process children with hierarchical numbering.
     */
    private void gotoChildren(WorkflowStep parent,
                              MultiServiceTestCase tc,
                              Map<String,String> ctx,
                              String parentStepNumber,
                              int variantIndex,
                              boolean isFaultyVariant,
                              List<String> targetFaultyParams) {
        List<WorkflowStep> children = parent.getChildren();
        for (int i = 0; i < children.size(); i++) {
            String childStepNumber = parentStepNumber + "." + (i + 1);
            traverse(children.get(i), tc, ctx, childStepNumber, variantIndex, isFaultyVariant, targetFaultyParams);
        }
    }
    
    /**
     * Initialize parameter rotation list for round-robin faulty parameter selection
     */
    private void initializeParameterRotation(String rootApiKey) {
        parameterRotation.clear();
        currentFaultyParamIndex = 0;
        
        Map<String, es.us.isa.restest.inputs.InvalidInputPool> faultyPool = faultyParameterPools.get(rootApiKey);
        if (faultyPool != null) {
            parameterRotation.addAll(faultyPool.keySet());
        }
        
        log.info("Initialized parameter rotation for '{}': {}", rootApiKey, parameterRotation);
    }
    
    /**
     * Get root API key for a scenario (used for parameter rotation initialization)
     */
    private String getRootApiKeyForScenario(WorkflowScenario scenario) {
        // Find first business step
        WorkflowStep firstBusinessStep = findFirstBusinessStep(scenario);
        if (firstBusinessStep == null) {
            return null;
        }
        
        String opName = firstBusinessStep.getOperationName();
        
        // Extract HTTP method and path
        String verb = null, route = null;
        Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
        if (httpMatcher.matches()) {
            verb = httpMatcher.group(1).toUpperCase();
            route = httpMatcher.group(2);
        }
        
        if (verb != null && route != null) {
            String rootApiKey = verb + "_" + route.replaceAll("[^a-zA-Z0-9_]", "_");
            return rootApiKey;
        }
        
        return null;
    }
    
    /**
     * Get the root API key for the current step being processed.
     * This is used to look up the faulty parameter pool.
     */
    private String getRootApiKeyForCurrentStep(MultiServiceTestCase tc) {
        // Get scenario name which contains the root API key pattern
        String scenarioName = tc.getScenarioName();
        if (scenarioName != null) {
            // Extract the base scenario name (before the counter suffix)
            // E.g., "POST_api_v1_travelservice_trips_1" -> "POST_api_v1_travelservice_trips"
            int lastUnderscore = scenarioName.lastIndexOf('_');
            if (lastUnderscore > 0) {
                String baseScenario = scenarioName.substring(0, lastUnderscore);
                // Check if this matches any root API key in our pools
                for (String key : faultyParameterPools.keySet()) {
                    if (key.contains(baseScenario) || baseScenario.contains(key)) {
                        return key;
                    }
                }
            }
        }
        // Fallback: return any available key
        if (!faultyParameterPools.isEmpty()) {
            return faultyParameterPools.keySet().iterator().next();
        }
        return null;
    }

    /** Locate the corresponding Operation object by method + path. */
    private Operation findOperation(TestConfigurationObject cfg,
                                    String verb, String path) {

        if (cfg.getTestConfiguration() == null ||
                cfg.getTestConfiguration().getOperations() == null)
            return null;

        return cfg.getTestConfiguration().getOperations().stream()
                .filter(o -> verb.equalsIgnoreCase(o.getMethod()) &&
                        path.equals(o.getTestPath()))
                .findFirst().orElse(null);
    }

    /** Simple JSON builder for test bodies. Properly handles typed objects. */
    private static String toJson(Map<String,Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String,Object> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(e.getKey()).append("\":");
            
            // Handle null values (e.g., from faulty test cases)
            Object value = e.getValue();
            if (value == null) {
                sb.append("null");
            } else {
                // Properly serialize typed objects - don't wrap numbers/booleans in quotes
                sb.append(serializeJsonValue(value));
            }
        }
        return sb.append('}').toString();
    }
    
    /**
     * Serialize a value to JSON, preserving types (numbers, booleans, lists, etc.)
     * This ensures that Integer(123) becomes 123, not "123" in JSON
     */
    private static String serializeJsonValue(Object value) {
        if (value == null) {
            return "null";
        }
        
        // Handle numbers (Integer, Long, Double, Float, etc.)
        if (value instanceof Number) {
            return value.toString();
        }
        
        // Handle booleans
        if (value instanceof Boolean) {
            return value.toString();
        }
        
        // Handle lists/arrays
        if (value instanceof java.util.List) {
            java.util.List<?> list = (java.util.List<?>) value;
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(',');
                first = false;
                sb.append(serializeJsonValue(item));
            }
            sb.append("]");
            return sb.toString();
        }
        
        // Handle maps (nested objects)
        if (value instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) value;
            return toJson(map);
        }
        
        // Handle strings - escape and quote
        String str = value.toString();
        return '"' + str.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t") + '"';
    }

    /**
     * Get the first API operation name from the scenario (excluding login/pre-calls)
     * This is used for meaningful test naming based on the actual business API being tested
     */
    private String getFirstApiOperationName(WorkflowScenario scenario) {
        // Traverse through root steps to find the first non-login API operation
        for (WorkflowStep rootStep : scenario.getRootSteps()) {
            String apiName = findFirstBusinessApiOperation(rootStep);
            if (apiName != null) {
                return apiName;
            }
        }
        return null;
    }
    
    /**
     * Get HTTP method of the first business API operation
     */
    private String getFirstApiHttpMethod(WorkflowScenario scenario) {
        for (WorkflowStep rootStep : scenario.getRootSteps()) {
            String httpMethod = findFirstBusinessApiHttpMethod(rootStep);
            if (httpMethod != null) {
                return httpMethod;
            }
        }
        return "UNKNOWN";
    }
    
    /**
     * Recursively search for HTTP method of the first business API (not login/auth)
     */
    private String findFirstBusinessApiHttpMethod(WorkflowStep step) {
        String opName = step.getOperationName();
        String serviceName = step.getServiceName();
        
        // Skip login/auth related operations
        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();
            
            if (opLower.contains("login") || opLower.contains("auth") || 
                serviceLower.contains("login") || serviceLower.contains("auth") ||
                opLower.contains("signin") || opLower.contains("token")) {
                // Skip login/auth, continue to children
            } else {
                // Extract HTTP method from operation name
                Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
                if (httpMatcher.matches()) {
                    return httpMatcher.group(1).toUpperCase();
                }
                
                // Try service-prefixed format
                Pattern servicePattern = Pattern.compile(".*?\\s+(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+.+$", Pattern.CASE_INSENSITIVE);
                Matcher serviceMatcher = servicePattern.matcher(opName);
                if (serviceMatcher.matches()) {
                    return serviceMatcher.group(1).toUpperCase();
                }
            }
        }
        
        // Recursively search children
        for (WorkflowStep child : step.getChildren()) {
            String result = findFirstBusinessApiHttpMethod(child);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }
    
    /**
     * Recursively search for the first business API operation (not login/auth related)
     */
    private String findFirstBusinessApiOperation(WorkflowStep step) {
        String opName = step.getOperationName();
        String serviceName = step.getServiceName();
        

        
        // Skip login/auth related operations (case-insensitive)
        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();
            
            // Skip common login/auth patterns
            if (opLower.contains("login") || opLower.contains("auth") || 
                serviceLower.contains("login") || serviceLower.contains("auth") ||
                opLower.contains("signin") || opLower.contains("token")) {
                // This is likely a login/auth operation, skip it
            } else {
                // This looks like a business API operation
                // Try to extract HTTP method and path for better naming
                String verb = null, route = null;
                
                // Check if it's an HTTP operation pattern (e.g., "POST /api/v1/path")
                Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
                if (httpMatcher.matches()) {
                    verb = httpMatcher.group(1).toLowerCase(Locale.ROOT);
                    route = httpMatcher.group(2);
                } else {
                    // Try to extract from service-prefixed format (e.g., "ts-service POST /api/v1/path")
                    Pattern servicePattern = Pattern.compile(".*?\\s+(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
                    Matcher serviceMatcher = servicePattern.matcher(opName);
                    if (serviceMatcher.matches()) {
                        verb = serviceMatcher.group(1).toLowerCase(Locale.ROOT);
                        route = serviceMatcher.group(2);
                    } else {
                        // For simple HTTP method operations, use the operation name as method
                        // and try to extract the path from the service context
                        if (opName.matches("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)$")) {
                            verb = opName.toLowerCase(Locale.ROOT);
                            
                            // Try to get HTTP target from trace data
                            Map<String, String> outputs = step.getOutputFields();
                            Map<String, String> inputs = step.getInputFields();
                            
                            String httpTarget = outputs.get("http.target");
                            String httpUrl = outputs.get("http.url");
                            
                            // Also check input fields as fallback
                            if (httpTarget == null) httpTarget = inputs.get("http.target");
                            if (httpUrl == null) httpUrl = inputs.get("http.url");
                            
                            if (httpTarget != null) {
                                route = httpTarget;
                            } else if (httpUrl != null) {
                                route = extractPathFromUrl(httpUrl);
                            } else {
                                // Use service name as fallback to create a meaningful route
                                route = "/api/v1/" + serviceName.replace("ts-", "").replace("-service", "");
                            }
                        } else {
                            // Check if we can extract from attributes/tags for other formats
                            Map<String, String> outputs = step.getOutputFields();
                            Map<String, String> inputs = step.getInputFields();
                            
                            String httpMethod = outputs.get("http.method");
                            String httpTarget = outputs.get("http.target");
                            String httpUrl = outputs.get("http.url");
                            
                            // Also check input fields as fallback
                            if (httpMethod == null) httpMethod = inputs.get("http.method");
                            if (httpTarget == null) httpTarget = inputs.get("http.target");
                            if (httpUrl == null) httpUrl = inputs.get("http.url");
                            
                            if (httpMethod != null && (httpTarget != null || httpUrl != null)) {
                                verb = httpMethod.toLowerCase(Locale.ROOT);
                                route = httpTarget != null ? httpTarget : extractPathFromUrl(httpUrl);
                            } else if (httpMethod != null) {
                                // If we only have the method, try to construct a meaningful name
                                verb = httpMethod.toLowerCase(Locale.ROOT);
                                route = "/api/v1/" + serviceName.replace("ts-", "").replace("-service", "");
                            }
                        }
                    }
                }
                
                // Return a descriptive name if we can extract method and path
                if (verb != null && route != null) {
                    String descriptiveName = verb.toUpperCase() + "_" + route.replaceAll("[^a-zA-Z0-9_]", "_");
                    return descriptiveName;
                } else {
                    // Fallback to original operation name
                return opName;
                }
            }
        }
        
        // Check children recursively
        for (WorkflowStep child : step.getChildren()) {
            String apiName = findFirstBusinessApiOperation(child);
            if (apiName != null) {
                return apiName;
            }
        }
        
        return null;
    }

    /**
     * Group scenarios by their root API (method + path) to enable parameter sharing
     */
    private Map<String, List<WorkflowScenario>> groupScenariosByRootApi() {
        Map<String, List<WorkflowScenario>> groups = new LinkedHashMap<>();
        
        for (WorkflowScenario sc : scenarios) {
            String rootApiKey = getRootApiKey(sc);
            if (rootApiKey != null) {
                groups.computeIfAbsent(rootApiKey, k -> new ArrayList<>()).add(sc);
                log.info("Grouped scenario {} under root API: {}", sc.getSourceFileName(), rootApiKey);
            } else {
                // Fallback: use scenario-specific key for scenarios without clear root API
                String fallbackKey = "scenario_" + sc.getSourceFileName();
                groups.computeIfAbsent(fallbackKey, k -> new ArrayList<>()).add(sc);
                log.warn("Using fallback key for scenario {}: {}", sc.getSourceFileName(), fallbackKey);
            }
        }
        
        log.info("=== GROUPED {} scenarios into {} root API groups ===", scenarios.size(), groups.size());
        for (Map.Entry<String, List<WorkflowScenario>> entry : groups.entrySet()) {
            log.info("Root API '{}' has {} scenarios", entry.getKey(), entry.getValue().size());
        }
        
        return groups;
    }

    /**
     * Extract root API key (method_path) from a scenario's first business operation
     */
    private String getRootApiKey(WorkflowScenario scenario) {
        for (WorkflowStep rootStep : scenario.getRootSteps()) {
            String apiKey = extractRootApiFromStep(rootStep);
            if (apiKey != null) {
                return apiKey;
            }
        }
        return null;
    }

    /**
     * Recursively find the first business API operation and return method_path key
     */
    private String extractRootApiFromStep(WorkflowStep step) {
        String opName = step.getOperationName();
        String serviceName = step.getServiceName();
        
        // Skip login/auth AND gateway operations
        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();
            
            // Skip login/auth
            boolean isLoginAuth = opLower.contains("login") || opLower.contains("auth") || 
                                  serviceLower.contains("login") || serviceLower.contains("auth") ||
                                  opLower.contains("signin") || opLower.contains("token");
            
            // Skip gateway services (they're just proxies, not business services)
            boolean isGateway = serviceLower.contains("gateway") || 
                               opName.equals("POST /*") || opName.equals("GET /*") ||
                               opName.equals("PUT /*") || opName.equals("DELETE /*");
            
            if (!isLoginAuth && !isGateway) {
                // Try to extract HTTP method and path
                String verb = null, route = null;
                
                // Check various operation name formats
                Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
                if (httpMatcher.matches()) {
                    verb = httpMatcher.group(1).toLowerCase();
                    route = httpMatcher.group(2);
                } else {
                    // Try service-prefixed format
                    Pattern servicePattern = Pattern.compile(".*?\\s+(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
                    Matcher serviceMatcher = servicePattern.matcher(opName);
                    if (serviceMatcher.matches()) {
                        verb = serviceMatcher.group(1).toLowerCase();
                        route = serviceMatcher.group(2);
                    } else {
                        // Try extracting from trace data
                        Map<String, String> outputs = step.getOutputFields();
                        String httpMethod = outputs.get("http.method");
                        String httpTarget = outputs.get("http.target");
                        String httpUrl = outputs.get("http.url");
                        
                        if (httpMethod != null && (httpTarget != null || httpUrl != null)) {
                            verb = httpMethod.toLowerCase();
                            route = httpTarget != null ? httpTarget : extractPathFromUrl(httpUrl);
                        }
                    }
                }
                
                if (verb != null && route != null) {
                    // Normalize the route by replacing non-alphanumeric characters with underscores
                    String normalizedRoute = route.replaceAll("[^a-zA-Z0-9_]", "_");
                    return verb.toUpperCase() + "_" + normalizedRoute;
                }
            }
        }
        
        // Check children recursively
        for (WorkflowStep child : step.getChildren()) {
            String apiKey = extractRootApiFromStep(child);
            if (apiKey != null) {
                return apiKey;
            }
        }
        
        return null;
    }

    /**
     * Generate shared parameter pools for each root API group
     */
    private void generateSharedParameterPools(Map<String, List<WorkflowScenario>> groupedScenarios) {
        log.info("=== GENERATING SHARED PARAMETER POOLS ===");
        
        for (Map.Entry<String, List<WorkflowScenario>> entry : groupedScenarios.entrySet()) {
            String rootApiKey = entry.getKey();
            List<WorkflowScenario> scenariosInGroup = entry.getValue();
            
            log.info("Generating shared parameters for root API: {} (scenarios: {})", 
                    rootApiKey, scenariosInGroup.size());
            
            // Use the first scenario in the group to extract parameter structure
            WorkflowScenario representativeScenario = scenariosInGroup.get(0);
            Map<String, List<String>> parameterPool = generateParameterPoolForRootApi(representativeScenario, rootApiKey);
            
            sharedParameterPools.put(rootApiKey, parameterPool);
            
            log.info("Generated parameter pool for '{}' with {} parameters", 
                    rootApiKey, parameterPool.size());
        }
        
        log.info("=== COMPLETED: {} shared parameter pools generated ===", sharedParameterPools.size());
        
        // Generate faulty parameter pools
        generateFaultyParameterPools(groupedScenarios);
    }

    /**
     * Generate a parameter pool for a specific root API using the first scenario as reference
     */
    private Map<String, List<String>> generateParameterPoolForRootApi(WorkflowScenario scenario, String rootApiKey) {
        Map<String, List<String>> parameterPool = new HashMap<>();
        
        // Find the first business API step to extract its parameters
        WorkflowStep firstBusinessStep = findFirstBusinessStep(scenario);
        if (firstBusinessStep == null) {
            log.warn("No business step found for root API: {}", rootApiKey);
            return parameterPool;
        }
        
        // Extract HTTP operation info
        String service = firstBusinessStep.getServiceName();
        String opName = firstBusinessStep.getOperationName();
        
        String verb = null, route = null;
        Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
        if (httpMatcher.matches()) {
            verb = httpMatcher.group(1).toLowerCase();
            route = httpMatcher.group(2);
        } else {
            // Try extracting from trace data
            Map<String, String> outputs = firstBusinessStep.getOutputFields();
            String httpMethod = outputs.get("http.method");
            String httpTarget = outputs.get("http.target");
            
            if (httpMethod != null && httpTarget != null) {
                verb = httpMethod.toLowerCase();
                route = httpTarget;
            }
        }
        
        if (verb == null || route == null) {
            log.warn("Could not extract HTTP method/path for root API: {}", rootApiKey);
            return parameterPool;
        }
        
        // Get service configuration
        TestConfigurationObject cfg = serviceConfigs.get(service);
        if (cfg == null) {
            log.warn("No configuration for service '{}' for root API: {}", service, rootApiKey);
            return parameterPool;
        }
        
        Operation opCfg = findOperation(cfg, verb, route);
        if (opCfg == null) {
            log.warn("No operation config for {} {} in service '{}' for root API: {}", verb, route, service, rootApiKey);
            return parameterPool;
        }
        
        // Generate parameter values for all parameters in this operation
        if (opCfg.getTestParameters() != null && useLLM) {
            // Collect all parameter names for context
            List<String> allParamNames = new java.util.ArrayList<>();
            for (TestParameter tp : opCfg.getTestParameters()) {
                allParamNames.add(tp.getName());
            }
            
            // Build API name for context
            String apiName = verb.toUpperCase() + " " + route;
            
            for (TestParameter p : opCfg.getTestParameters()) {
                // Create ParameterInfo with full API context for shared parameter pool generation
                ParameterInfo info = createParameterInfoWithContext(p, apiName, service, allParamNames);

                // 🚀 FIXED: Try Smart Input Fetching first for shared parameter pool generation
                List<String> finalValues = new ArrayList<>();

                if (smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                    try {
                        // Generate multiple smart-fetched values for the pool
                        for (int i = 0; i < 15; i++) {
                            String smartValue = smartFetcher.fetchSmartInput(info);
                            if (smartValue != null && !smartValue.trim().isEmpty()) {
                                finalValues.add(smartValue);
                            }
                        }

                        if (!finalValues.isEmpty()) {
                            log.info("Smart Fetch Pool → parameter '{}': {} smart values generated",
                                    p.getName(), finalValues.size());
                        }
                    } catch (Exception e) {
                        log.debug("Smart fetching failed for shared pool parameter '{}': {}",
                                 p.getName(), e.getMessage());
                    }
                }

                // If smart fetch didn't provide enough values, supplement with LLM
                if (finalValues.size() < 15) {
                    int needed = 15 - finalValues.size();
                    log.info("Smart fetch provided {} values for '{}', generating {} more with LLM",
                            finalValues.size(), p.getName(), needed);

                    // Stage 1: Get seed values from LLM
                    List<String> llmSeedValues = llmGen.generateParameterValues(info);

                    if (!llmSeedValues.isEmpty()) {
                        // Stage 2: Expand using semantic models to get more variants
                        List<String> expandedValues = expander.expandValues(llmSeedValues, needed);
                        finalValues.addAll(expandedValues);

                        log.info("LLM Pool → parameter '{}': {} additional values generated",
                                p.getName(), expandedValues.size());
                    } else {
                        // Fallback values
                        for (int i = finalValues.size(); i < 15; i++) {
                            finalValues.add("LLM_EMPTY_" + i);
                        }
                        log.warn("LLM returned no values for parameter '{}', using fallback", p.getName());
                    }
                }

                parameterPool.put(p.getName(), finalValues);
                log.info("Generated shared pool for parameter '{}': {} total values (smart + LLM + fallback)",
                        p.getName(), finalValues.size());
            }
        }
        
        return parameterPool;
    }

    /**
     * Generate faulty parameter pools for each root API group
     */
    private void generateFaultyParameterPools(Map<String, List<WorkflowScenario>> groupedScenarios) {
        log.info("=== GENERATING FAULTY PARAMETER POOLS ===");
        log.info("Number of scenario groups: {}", groupedScenarios.size());
        
        for (Map.Entry<String, List<WorkflowScenario>> entry : groupedScenarios.entrySet()) {
            String rootApiKey = entry.getKey();
            log.info("Processing root API key: '{}'", rootApiKey);
            WorkflowScenario representativeScenario = entry.getValue().get(0);
            Map<String, es.us.isa.restest.inputs.InvalidInputPool> faultyPool = generateFaultyPoolForRootApi(representativeScenario, rootApiKey);
            
            faultyParameterPools.put(rootApiKey, faultyPool);
            log.info("✅ Generated faulty pool for '{}' with {} parameters: {}", 
                    rootApiKey, faultyPool.size(), faultyPool.keySet());
        }
        
        log.info("=== COMPLETED: {} faulty parameter pools generated ===", faultyParameterPools.size());
        log.info("All faulty pool keys: {}", faultyParameterPools.keySet());
    }

    /**
     * Convert Object (Integer, Boolean, null, etc.) to String for API parameter value
     * Handles type mismatches properly - preserves type information in string form
     */
    private String convertObjectToString(Object value, String paramType) {
        if (value == null) {
            return null;
        }
        
        // If the value is already a String, return as-is
        if (value instanceof String) {
            return (String) value;
        }
        
        // For type mismatches, we want to preserve the wrong type
        // E.g., if parameter expects String but we have Integer, keep it as integer representation
        // The REST Assured serialization will handle this correctly in JSON
        
        if (value instanceof Integer || value instanceof Long) {
            return value.toString(); // "123" but will be serialized as number in JSON
        }
        
        if (value instanceof Boolean) {
            return value.toString(); // "true" but will be serialized as boolean in JSON
        }
        
        if (value instanceof Double || value instanceof Float) {
            return value.toString(); // "12.34" but will be serialized as number in JSON
        }
        
        // For arrays and objects (represented as JSON strings)
        return value.toString();
    }

    /**
     * Convert string values from LLM/Smart Fetch/Word2Vec to proper typed objects for positive tests
     * This ensures that parameter types match OpenAPI schema requirements.
     * 
     * @param stringValue The string value from generator
     * @param param The parameter with type information
     * @return Properly typed object (Integer, Boolean, List, etc.) or string if conversion fails
     */
    private Object convertStringToTypedValue(String stringValue, TestParameter param) {
        if (stringValue == null) {
            return null;
        }
        
        String type = param.getType();
        String format = param.getFormat();
        
        if (type == null) {
            return stringValue; // No type info, keep as string
        }
        
        try {
            switch (type.toLowerCase()) {
                case "integer":
                    // Handle formats: int32, int64
                    if ("int64".equals(format)) {
                        return Long.parseLong(stringValue.trim());
                    } else {
                        return Integer.parseInt(stringValue.trim());
                    }
                    
                case "number":
                    // Handle formats: float, double
                    if ("float".equals(format)) {
                        return Float.parseFloat(stringValue.trim());
                    } else {
                        return Double.parseDouble(stringValue.trim());
                    }
                    
                case "boolean":
                    return Boolean.parseBoolean(stringValue.trim());
                    
                case "array":
                    // Parse array from string
                    return parseArrayValue(stringValue, param);
                    
                case "object":
                    // Keep as string - will be handled by JSON serialization
                    return stringValue;
                    
                case "string":
                default:
                    // Keep as string
                    return stringValue;
            }
        } catch (IllegalArgumentException e) {
            // Catches NumberFormatException (subclass) and other IllegalArgumentExceptions
            log.warn("Failed to convert value '{}' to type '{}' for parameter '{}': {}. Keeping as string.", 
                    stringValue, type, param.getName(), e.getMessage());
            return stringValue; // Fallback to string if conversion fails
        }
    }
    
    /**
     * Parse array value from string representation
     * Supports: "[1,2,3]", "1,2,3", "value1, value2, value3"
     */
    private Object parseArrayValue(String stringValue, TestParameter param) {
        if (stringValue == null || stringValue.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        String trimmed = stringValue.trim();
        
        // Remove brackets if present
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        
        if (trimmed.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        // Split by comma
        String[] items = trimmed.split(",");
        java.util.List<Object> result = new java.util.ArrayList<>();
        
        // Try to determine item type from format or example
        String itemType = inferArrayItemType(param);
        
        for (String item : items) {
            String cleanItem = item.trim();
            if (cleanItem.isEmpty()) {
                continue;
            }
            
            // Remove quotes if present
            if (cleanItem.startsWith("\"") && cleanItem.endsWith("\"")) {
                cleanItem = cleanItem.substring(1, cleanItem.length() - 1);
            }
            
            // Convert based on item type
            try {
                switch (itemType) {
                    case "integer":
                        result.add(Integer.parseInt(cleanItem));
                        break;
                    case "number":
                        result.add(Double.parseDouble(cleanItem));
                        break;
                    case "boolean":
                        result.add(Boolean.parseBoolean(cleanItem));
                        break;
                    default:
                        result.add(cleanItem); // Keep as string
                }
            } catch (NumberFormatException e) {
                log.debug("Could not parse array item '{}' as {}, keeping as string", cleanItem, itemType);
                result.add(cleanItem);
            }
        }
        
        return result;
    }
    
    /**
     * Infer array item type from parameter metadata
     */
    private String inferArrayItemType(TestParameter param) {
        // Check if example gives us a hint
        if (param.getExample() != null) {
            Object example = param.getExample();
            if (example instanceof java.util.List && !((java.util.List<?>) example).isEmpty()) {
                Object firstItem = ((java.util.List<?>) example).get(0);
                if (firstItem instanceof Integer || firstItem instanceof Long) {
                    return "integer";
                } else if (firstItem instanceof Double || firstItem instanceof Float) {
                    return "number";
                } else if (firstItem instanceof Boolean) {
                    return "boolean";
                }
            }
        }
        
        // Check format for hints
        String format = param.getFormat();
        if (format != null) {
            if (format.contains("int")) return "integer";
            if (format.contains("double") || format.contains("float")) return "number";
            if (format.contains("bool")) return "boolean";
        }
        
        // Default to string
        return "string";
    }

    /**
     * Generate a faulty parameter pool for a specific root API using the first scenario as reference
     * Returns Map of parameter name to InvalidInputPool (with 8 fault types)
     */
    private Map<String, es.us.isa.restest.inputs.InvalidInputPool> generateFaultyPoolForRootApi(WorkflowScenario scenario, String rootApiKey) {
        log.info("🔨 Generating comprehensive invalid input pools for root API: '{}'", rootApiKey);
        Map<String, es.us.isa.restest.inputs.InvalidInputPool> faultyPool = new HashMap<>();
        
        // Find the first business API step to extract its parameters
        WorkflowStep firstBusinessStep = findFirstBusinessStep(scenario);
        if (firstBusinessStep == null) {
            log.warn("❌ No business step found for root API: {}", rootApiKey);
            return faultyPool;
        }
        log.info("✅ Found first business step: service='{}', operation='{}'", 
                firstBusinessStep.getServiceName(), firstBusinessStep.getOperationName());
        
        // Extract HTTP operation info
        String service = firstBusinessStep.getServiceName();
        String opName = firstBusinessStep.getOperationName();
        
        String verb = null, route = null;
        Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
        if (httpMatcher.matches()) {
            verb = httpMatcher.group(1).toLowerCase();
            route = httpMatcher.group(2);
        } else {
            // Try extracting from trace data
            Map<String, String> outputs = firstBusinessStep.getOutputFields();
            String httpMethod = outputs.get("http.method");
            String httpTarget = outputs.get("http.target");
            
            if (httpMethod != null && httpTarget != null) {
                verb = httpMethod.toLowerCase();
                route = httpTarget;
            }
        }
        
        if (verb == null || route == null) {
            log.warn("Could not extract HTTP method/path for root API: {}", rootApiKey);
            return faultyPool;
        }
        
        // Get service configuration
        TestConfigurationObject cfg = serviceConfigs.get(service);
        if (cfg == null) {
            log.warn("No configuration for service '{}' for root API: {}", service, rootApiKey);
            return faultyPool;
        }
        
        Operation opCfg = findOperation(cfg, verb, route);
        if (opCfg == null) {
            log.warn("No operation config for {} {} in service '{}' for root API: {}", verb, route, service, rootApiKey);
            return faultyPool;
        }
        
        // Generate comprehensive invalid input pools for all parameters in this operation
        log.info("🔴 Generating COMPREHENSIVE invalid input pools for operation: {} {} (useLLM: {}, paramCount: {})", 
                verb, route, useLLM, opCfg.getTestParameters() != null ? opCfg.getTestParameters().size() : 0);
        log.info("📋 Will generate 8 types of invalid inputs: TYPE_MISMATCH, REGEX_MISMATCH, SEMANTIC_MISMATCH, OVERFLOW, EMPTY_INPUT, NULL_INPUT, SPECIAL_CHARACTERS, BOUNDARY_VIOLATION");
        
        if (opCfg.getTestParameters() != null && useLLM) {
            // Collect all parameter names for context
            List<String> allParamNames = new java.util.ArrayList<>();
            for (TestParameter tp : opCfg.getTestParameters()) {
                allParamNames.add(tp.getName());
            }
            
            // Build API name for context
            String apiName = verb.toUpperCase() + " " + route;
            
            for (TestParameter p : opCfg.getTestParameters()) {
                log.info("💉 Generating comprehensive invalid input pool for parameter: '{}' (type: {})", 
                        p.getName(), p.getType());
                
                // Create ParameterInfo with full API context for better invalid input generation
                ParameterInfo info = createParameterInfoWithContext(p, apiName, service, allParamNames);
                
                // Use new comprehensive invalid input generation
                es.us.isa.restest.inputs.InvalidInputPool pool = llmGen.generateInvalidInputPool(info);
                
                faultyPool.put(p.getName(), pool);
                
                log.info("✅ Generated invalid input pool for parameter '{}':", p.getName());
                log.info("   {}", pool.getPoolSummary().replace("\n", "\n   "));
            }
        } else {
            log.warn("⚠️ Cannot generate invalid input pools: useLLM={}, testParameters={}", 
                    useLLM, opCfg.getTestParameters() != null);
        }
        
        // Calculate total invalid values across all parameters
        int totalInvalidValues = faultyPool.values().stream()
                .mapToInt(es.us.isa.restest.inputs.InvalidInputPool::getTotalCount)
                .sum();
        
        log.info("📦 Final invalid input pools for '{}': {} parameters, {} total invalid values", 
                rootApiKey, faultyPool.size(), totalInvalidValues);
        
        return faultyPool;
    }

    /**
     * Find the first business step (non-login) in a scenario
     */
    private WorkflowStep findFirstBusinessStep(WorkflowScenario scenario) {
        for (WorkflowStep rootStep : scenario.getRootSteps()) {
            WorkflowStep businessStep = findFirstBusinessStepRecursive(rootStep);
            if (businessStep != null) {
                return businessStep;
            }
        }
        return null;
    }

    /**
     * Recursively find the first business step
     */
    private WorkflowStep findFirstBusinessStepRecursive(WorkflowStep step) {
        String opName = step.getOperationName();
        String serviceName = step.getServiceName();
        
        // Skip login/auth operations AND gateway operations
        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();
            
            // Skip login/auth
            boolean isLoginAuth = opLower.contains("login") || opLower.contains("auth") || 
                                  serviceLower.contains("login") || serviceLower.contains("auth") ||
                                  opLower.contains("signin") || opLower.contains("token");
            
            // Skip gateway services (they're just proxies, not business services)
            boolean isGateway = serviceLower.contains("gateway") || 
                               opName.equals("POST /*") || opName.equals("GET /*") ||
                               opName.equals("PUT /*") || opName.equals("DELETE /*");
            
            if (!isLoginAuth && !isGateway) {
                return step; // This is a business step
            }
        }
        
        // Check children
        for (WorkflowStep child : step.getChildren()) {
            WorkflowStep businessStep = findFirstBusinessStepRecursive(child);
            if (businessStep != null) {
                return businessStep;
            }
        }
        
        return null;
    }

    /**
     * Extract API name from a processed step call (guaranteed to have valid HTTP method/path)
     */
    private String extractApiNameFromStep(MultiServiceTestCase.StepCall step) {
        if (step == null) return null;
        
        String method = step.getMethod() != null && step.getMethod().getMethod() != null 
                        ? step.getMethod().getMethod().toUpperCase() 
                        : "GET";
        String path = step.getPath();
        
        if (path != null && !path.isEmpty()) {
            // Create a clean file-safe name from method and path
            String apiName = method + "_" + path.replaceAll("[^a-zA-Z0-9_]", "_")
                                              .replaceAll("_+", "_")
                                              .replaceAll("^_|_$", "");
            
            // Windows path limit is 260 chars. Account for:
            // - Base path (e.g., "src/test/java/trainticket_twostage_test/")
            // - Timestamp (e.g., "TrainTicketTwoStageTest_1762921209504/")
            // - Test variant suffix (e.g., "_123")
            // - File extension (".java")
            // Safe limit for class name: ~100 characters
            final int MAX_CLASS_NAME_LENGTH = 100;
            
            if (apiName.length() > MAX_CLASS_NAME_LENGTH) {
                // Truncate and add hash to maintain uniqueness
                String truncated = apiName.substring(0, MAX_CLASS_NAME_LENGTH - 9); // Leave room for hash
                int hash = apiName.hashCode();
                // Use positive hash value for consistency
                String hashSuffix = String.format("_%08X", hash & 0xFFFFFFFF);
                apiName = truncated + hashSuffix;
                
                log.warn("⚠️  API name truncated due to length: {} -> {} (original length: {})", 
                         path, apiName, apiName.length() + (apiName.length() - MAX_CLASS_NAME_LENGTH));
            }
            
            return apiName;
        }
        
        return null;
    }

    /**
     * Read variant count from properties file with fallback to defaults
     */
    private int getVariantCountFromProperties() {
        try {
            // Try testsperoperation first (this is the main property for test count)
            String testsProp = System.getProperty("testsperoperation");
            if (testsProp != null) {
                int count = Integer.parseInt(testsProp);
                log.info("✅ Using testsperoperation from properties: {}", count);
                return count;
            }
            
            // Try test.variants.per.scenario as fallback
            String variantsProp = System.getProperty("test.variants.per.scenario");
            if (variantsProp != null) {
                int count = Integer.parseInt(variantsProp);
                log.info("✅ Using test.variants.per.scenario from properties: {}", count);
                return count;
            }
            
            // Default behavior
            int defaultCount = 1;
            log.warn("❌ No variant count found in properties, using default: {}", defaultCount);
            return defaultCount;
        } catch (NumberFormatException e) {
            int defaultCount = 1;
            log.warn("❌ Invalid variant count in properties, using default: {} (error: {})", defaultCount, e.getMessage());
            return defaultCount;
        }
    }

    /**
     * Generates the JSON body for a given set of body parameters.
     * Handles special case: single array-type body parameter should generate entire body as array.
     * Otherwise, generates standard JSON object.
     */
    private String generateRequestBody(Map<String, Object> bodyFields, Operation opCfg) {
        // 🔥 FIX: Check if we have a single body parameter with type "array"
        if (bodyFields.size() == 1 && bodyFields.containsKey("body")) {
            // Find the body parameter in the configuration to check its type
            if (opCfg != null && opCfg.getTestParameters() != null) {
                for (TestParameter p : opCfg.getTestParameters()) {
                    if ("body".equals(p.getName()) && "body".equals(p.getIn()) && "array".equals(p.getType())) {
                        // This is an array-type body parameter - generate entire body as array
                        String singleValue = bodyFields.get("body").toString();
                        return generateJsonArray(singleValue, p);
                    }
                }
            }
        }
        
        // Default behavior: generate JSON object
        return toJson(bodyFields);
    }
    
    /**
     * Generates a JSON array for array-type body parameters.
     * Creates multiple array elements to provide realistic test data.
     */
    private String generateJsonArray(String singleValue, TestParameter arrayParam) {
        StringBuilder arrayJson = new StringBuilder("[");
        
        // Generate 2-4 array elements for more realistic testing
        int arraySize = 2 + (int)(Math.random() * 3); // Random between 2-4
        List<String> arrayValues = new ArrayList<>();
        
        // Add the original value first
        arrayValues.add(singleValue);
        
        // Generate additional values using smart fetch or LLM
        for (int i = 1; i < arraySize; i++) {
            String additionalValue = null;
            
            if (useLLM) {
                ParameterInfo info = new ParameterInfo();
                info.setName(arrayParam.getName());
                info.setDescription(arrayParam.getDescription());
                info.setInLocation(arrayParam.getIn());
                info.setType(arrayParam.getType());
                info.setFormat(arrayParam.getFormat());
                info.setSchemaType(arrayParam.getType());
                info.setSchemaExample(arrayParam.getExample() != null ? arrayParam.getExample().toString() : "");
                info.setRegex(arrayParam.getPattern());
                
                // Try Smart Input Fetching first if available
                if (smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                    try {
                        additionalValue = smartFetcher.fetchSmartInput(info);
                        if (additionalValue != null && !additionalValue.trim().isEmpty()) {
                            log.debug("Smart Fetch (Array) → {} = {}", arrayParam.getName(), additionalValue);
                        } else {
                            additionalValue = null; // Ensure we fall back to LLM
                        }
                    } catch (Exception e) {
                        log.debug("Smart fetching failed for array {}, falling back to LLM: {}", 
                                 arrayParam.getName(), e.getMessage());
                        additionalValue = null; // Ensure we fall back to LLM
                    }
                }
                
                // Fall back to traditional LLM generation if smart fetching didn't work
                if (additionalValue == null) {
                    List<String> vals = llmGen.generateParameterValues(info);
                    // 🔄 FIX: Rotate through cached values instead of always using first value
                    if (vals.isEmpty()) {
                        additionalValue = "LLM_EMPTY_" + i;
                    } else if (vals.size() == 1) {
                        additionalValue = vals.get(0);
                    } else {
                        // Rotate through the cached values using array element index
                        int rotationIndex = (i % vals.size());
                        additionalValue = vals.get(rotationIndex);
                        log.debug("🔄 Rotated to LLM value [{}] for array '{}' element {}: {}", rotationIndex, arrayParam.getName(), i, additionalValue);
                    }
                    log.debug("LLM (Array Fallback) → {} = {}", arrayParam.getName(), additionalValue);
                }
            }
            
            // Ultimate fallback
            if (additionalValue == null || additionalValue.trim().isEmpty()) {
                additionalValue = "VAL_" + arrayParam.getName() + "_" + i;
            }
            
            arrayValues.add(additionalValue);
        }
        
        // Build JSON array
        for (int i = 0; i < arrayValues.size(); i++) {
            if (i > 0) arrayJson.append(", ");
            arrayJson.append('"').append(escapeJsonString(arrayValues.get(i))).append('"');
        }
        
        arrayJson.append("]");
        return arrayJson.toString();
    }
    
    /**
     * Escape special characters in JSON string values.
     */
    private String escapeJsonString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}


