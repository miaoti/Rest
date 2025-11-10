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
    private Map<String, Map<String, List<String>>> faultyParameterPools = new HashMap<>();
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
            
            // DEBUG: Log invalid parameters
            if (isFaultyVariant) {
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

            for (TestParameter p : opCfg.getTestParameters()) {
                log.info("📋 Parameter: {} (type: {}, in: {}, description: '{}')",
                        p.getName(), p.getType(), p.getIn(), p.getDescription());
                String val = null;

                if (isFirstBusinessStep) {
                    /* Step 1 (First Business Step): Check if negative test, then use smart fetch or invalid values */
                    log.info("🎯 Step 1 parameter '{}' - attempting smart fetch", p.getName());
                    
                    boolean faultyValueSet = false;

                    if (useLLM) {
                        ParameterInfo info = createParameterInfo(p);
                        
                        // Check if this is a negative test variant AND this is one of the target invalid parameters
                        if (isFaultyVariant && targetFaultyParams != null && targetFaultyParams.contains(p.getName())) {
                            log.info("🔴 NEGATIVE TEST: Making parameter '{}' invalid (target param)", p.getName());
                            
                            // Use invalid value from faulty pool
                            String rootApiKey = getRootApiKeyForCurrentStep(tc);
                            Map<String, List<String>> faultyPool = faultyParameterPools.get(rootApiKey);
                            
                            if (faultyPool != null && faultyPool.containsKey(p.getName())) {
                                List<String> faultyValues = faultyPool.get(p.getName());
                                if (!faultyValues.isEmpty()) {
                                    val = faultyValues.get(random.nextInt(faultyValues.size()));
                                    tc.addFaultyParameter(p.getName(), val);
                                    faultyValueSet = true;  // Mark that invalid value is set
                                    log.info("✅ Negative Test → {} = {} (intentionally invalid) - LOCKED", p.getName(), val);
                                }
                            } else {
                                log.warn("⚠️ No invalid value pool found for rootApiKey='{}' or parameter='{}'", rootApiKey, p.getName());
                            }
                        }

                        // Try Smart Input Fetching first for step 1 parameters (ONLY if not faulty)
                        if (!faultyValueSet && val == null && smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                            log.info("🚀 Calling smart fetch for step 1 parameter '{}'", p.getName());
                            try {
                                val = smartFetcher.fetchSmartInput(info);
                                if (val != null && !val.trim().isEmpty()) {
                                    log.info("Smart Fetch (Step 1) → {} {} = {} ✅", service, p.getName(), val);
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

                        // Fall back to traditional LLM generation if smart fetching didn't work (ONLY if not faulty)
                        if (!faultyValueSet && val == null) {
                            List<String> vals = llmGen.generateParameterValues(info);
                            val = vals.isEmpty() ? "LLM_EMPTY_" + p.getName() : vals.get(0);
                            log.info("LLM (Step 1 Fallback) → {} {} = {}", service, p.getName(), val);
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
                    if (val == null) {
                        val = getTraceParameterValue(span, p.getName());
                        if (val != null) {
                            log.info("Trace Data → {} {} = {} (from trace, step {})",
                                    service, p.getName(), val, stepNumber);
                        }
                    }

                    /* 3d. Parameter is INDEPENDENT - use Smart Input Fetching or LLM */
                    if (val == null && useLLM) {
                        log.info("Parameter '{}' is INDEPENDENT in step {} - generating new value", p.getName(), stepNumber);

                        ParameterInfo info = createParameterInfo(p);

                        // Try Smart Input Fetching first for independent parameters
                        if (smartFetcher != null && smartFetchConfig != null && smartFetchConfig.isEnabled()) {
                            try {
                                val = smartFetcher.fetchSmartInput(info);
                                if (val != null && !val.trim().isEmpty()) {
                                    log.info("Smart Fetch (Independent) → {} {} = {} ✅ (step {})", service, p.getName(), val, stepNumber);
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
                        if (val == null) {
                            List<String> vals = llmGen.generateParameterValues(info);
                            val = vals.isEmpty() ? "LLM_EMPTY" : vals.get(0);
                            log.info("LLM (Independent Fallback) → {} {} = {} (step {})", service, p.getName(), val, stepNumber);
                        }
                    }

                    /* 3e. Ultimate fallback ---------------------------------------- */
                    if (val == null) val = "VAL_" + p.getName();
                }

                /* 3e. Store in correct container ----------------------------------- */
                switch (p.getIn().toLowerCase(Locale.ROOT)) {
                    case "path":
                        pathParams.put(p.getName(), val);
                        resolvedPath = resolvedPath.replace("{"+p.getName()+"}", val);
                        break;
                    case "query":
                        queryParams.put(p.getName(), val);
                        break;
                    case "header":
                        headerParams.put(p.getName(), val);
                        break;
                    case "body":
                    case "formdata":
                        bodyFields.put(p.getName(), val);
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
     * Helper method to create ParameterInfo from TestParameter
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
        
        Map<String, List<String>> faultyPool = faultyParameterPools.get(rootApiKey);
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

    /** Simple JSON builder for test bodies. */
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
                sb.append('"').append(value.toString()
                                .replace("\\", "\\\\")
                                .replace("\"","\\\""))
                        .append('"');
            }
        }
        return sb.append('}').toString();
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
            for (TestParameter p : opCfg.getTestParameters()) {
                ParameterInfo info = new ParameterInfo();
                info.setName(p.getName());
                info.setDescription(p.getDescription());
                info.setInLocation(p.getIn());
                info.setType(p.getType());
                info.setFormat(p.getFormat());
                info.setSchemaType(p.getType());
                info.setSchemaExample(p.getExample() != null ? p.getExample().toString() : "");
                info.setRegex(p.getPattern());

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
            Map<String, List<String>> faultyPool = generateFaultyPoolForRootApi(representativeScenario, rootApiKey);
            
            faultyParameterPools.put(rootApiKey, faultyPool);
            log.info("✅ Generated faulty pool for '{}' with {} parameters: {}", 
                    rootApiKey, faultyPool.size(), faultyPool.keySet());
        }
        
        log.info("=== COMPLETED: {} faulty parameter pools generated ===", faultyParameterPools.size());
        log.info("All faulty pool keys: {}", faultyParameterPools.keySet());
    }

    /**
     * Generate a faulty parameter pool for a specific root API using the first scenario as reference
     */
    private Map<String, List<String>> generateFaultyPoolForRootApi(WorkflowScenario scenario, String rootApiKey) {
        log.info("🔨 Generating faulty pool for root API: '{}'", rootApiKey);
        Map<String, List<String>> faultyPool = new HashMap<>();
        
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
        
        // Generate faulty parameter values for all parameters in this operation
        log.info("Generating faulty values for operation: {} {} (useLLM: {}, paramCount: {})", 
                verb, route, useLLM, opCfg.getTestParameters() != null ? opCfg.getTestParameters().size() : 0);
        
        if (opCfg.getTestParameters() != null && useLLM) {
            for (TestParameter p : opCfg.getTestParameters()) {
                log.info("💉 Generating faulty values for parameter: '{}'", p.getName());
                ParameterInfo info = createParameterInfo(p);
                List<String> faultyValues = llmGen.generateFaultyParameterValues(info, 10);
                faultyPool.put(p.getName(), faultyValues);
                log.info("✅ Generated faulty pool for parameter '{}': {} values - {}", 
                        p.getName(), faultyValues.size(), faultyValues);
            }
        } else {
            log.warn("⚠️ Cannot generate faulty pool: useLLM={}, testParameters={}", 
                    useLLM, opCfg.getTestParameters() != null);
        }
        
        log.info("📦 Final faulty pool for '{}': {} parameters total", rootApiKey, faultyPool.size());
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
                    additionalValue = vals.isEmpty() ? "LLM_EMPTY_" + i : vals.get(0);
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

