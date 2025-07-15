package es.us.isa.restest.generators;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.inputs.llm.ParameterInfo;
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
            String testName = "test_" + scenarioId + "_" + (v + 1);
            
            MultiServiceTestCase tc = new MultiServiceTestCase(testName);
            tc.setScenarioName(scenarioId);
            
            log.info("--- Generating variant {} ({}) ---", v, tc.getOperationId());
            
            Map<String,String> context = new HashMap<>();
            
            // Process workflow steps with variant-specific parameter generation
            for (WorkflowStep root : sc.getRootSteps()) {
                traverse(root, tc, context, "1", v);
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
            
            variants.add(tc);
            
            log.info("--- Completed variant {} with {} steps ---", v, tc.getSteps().size());
        }
        
        log.info("=== Generated {} total test case variants ===", variants.size());
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
     */
    private void traverse(WorkflowStep span,
                          MultiServiceTestCase tc,
                          Map<String,String> context,
                          String stepNumber,
                          int variantIndex) {

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
            gotoChildren(span, tc, context, stepNumber, variantIndex);
            return;
            }
        }

        if (verb == null || route == null) {
            log.debug("Could not extract HTTP method/path from span: {} - {}", service, opName);
            gotoChildren(span, tc, context, stepNumber, variantIndex);
            return;
        }

        /* 2. Load service‑specific test‑configuration ------------------------------ */
        TestConfigurationObject cfg = serviceConfigs.get(service);
        if (cfg == null) {
            log.warn("No test‑configuration for service '{}' (step {})", service, stepNumber);
            gotoChildren(span, tc, context, stepNumber, variantIndex);
            return;
        }

        Operation opCfg = findOperation(cfg, verb, route);
        if (opCfg == null) {
            log.warn("No Operation config {} {} in service '{}' (step {})", verb, route, service, stepNumber);
            gotoChildren(span, tc, context, stepNumber, variantIndex);
            return;
        }

        /* 3. Build parameter maps from trace data and LLM --------------------------- */
        Map<String,String> pathParams   = new LinkedHashMap<>();
        Map<String,String> queryParams  = new LinkedHashMap<>();
        Map<String,String> headerParams = new LinkedHashMap<>();
        Map<String,String> bodyFields   = new LinkedHashMap<>();

        String resolvedPath = route;

        // Check if this is the first step
        boolean isFirstStep = context.isEmpty() || stepNumber.equals("1");

        // Extract parameters from trace data only for non-first steps
        if (!isFirstStep) {
            extractParametersFromTrace(span, bodyFields, queryParams, pathParams, headerParams);
        }

        if (opCfg.getTestParameters() != null) {
            for (TestParameter p : opCfg.getTestParameters()) {
                String val = null;

                if (isFirstStep) {
                    /* For the first step, use shared parameter pools */
                    if (useLLM) {
                        // Determine the root API key for this step
                        String currentRootApiKey = verb.toUpperCase() + "_" + route;
                        
                        // Get shared parameter pool for this root API
                        Map<String, List<String>> parameterPool = sharedParameterPools.get(currentRootApiKey);
                        
                        if (parameterPool != null && parameterPool.containsKey(p.getName())) {
                            List<String> sharedValues = parameterPool.get(p.getName());
                            
                            // Select value based on variant index from shared pool
                            if (variantIndex < sharedValues.size()) {
                                val = sharedValues.get(variantIndex);
                                log.info("Shared Pool → {} {} = {} (variant {}, step {}, pool: {})", 
                                        service, p.getName(), val, variantIndex, stepNumber, currentRootApiKey);
                            } else {
                                // Fallback to cycling through available values
                                val = sharedValues.get(variantIndex % sharedValues.size());
                                log.info("Shared Pool (Cycled) → {} {} = {} (variant {}, step {}, pool: {})", 
                                        service, p.getName(), val, variantIndex, stepNumber, currentRootApiKey);
                            }
                        } else {
                            // Fallback: generate new values if no shared pool found (shouldn't happen)
                            log.warn("No shared pool found for {} {} in root API {}, generating fallback", 
                                    service, p.getName(), currentRootApiKey);
                            
                        ParameterInfo info = new ParameterInfo();
                        info.setName(p.getName());
                        info.setDescription(p.getDescription());
                        info.setInLocation(p.getIn());
                        info.setType(p.getType());
                        info.setFormat(p.getFormat());
                        info.setSchemaType(p.getType());
                        info.setSchemaExample(p.getExample() != null ? p.getExample().toString() : "");
                        info.setRegex(p.getPattern());

                        List<String> llmSeedValues = llmGen.generateParameterValues(info);
                        
                        if (!llmSeedValues.isEmpty()) {
                            List<String> expandedValues = expander.expandValues(llmSeedValues, 15);
                                val = variantIndex < expandedValues.size() 
                                        ? expandedValues.get(variantIndex) 
                                        : expandedValues.get(variantIndex % expandedValues.size());
                                log.info("Fallback Two-Stage → {} {} = {} (variant {}, step {})", 
                                        service, p.getName(), val, variantIndex, stepNumber);
                            } else {
                                val = "LLM_EMPTY_" + variantIndex;
                                log.warn("Fallback: LLM returned no values for {} {}", service, p.getName());
                            }
                        }
                    } else {
                        /* Fallback if LLM is disabled */
                        val = "INIT_" + p.getName() + "_v" + variantIndex;
                    }
                } else {
                    /* For subsequent steps, use the dependency-based logic */

                    /* 3a. Use previously captured OUTPUT value from context (dependency) ------ */
                    val = context.get(p.getName());

                    /* 3b. Use previously captured INPUT value for consistency --------------- */
                    if (val == null) {
                        val = context.get("input." + p.getName());
                        if (val != null) {
                            log.info("Input Consistency → {} {} = {} (reusing from previous API input, step {})", 
                                    service, p.getName(), val, stepNumber);
                        }
                    }

                    /* 3c. Use trace data if available and no context value ------------- */
                    if (val == null) {
                        val = getTraceParameterValue(span, p.getName());
                    }

                    /* 3d. Call LLM if enabled and no trace/context value --------------- */
                    if (val == null && useLLM) {
                        ParameterInfo info = new ParameterInfo();
                        info.setName(p.getName());
                        info.setDescription(p.getDescription());
                        info.setInLocation(p.getIn());
                        info.setType(p.getType());
                        info.setFormat(p.getFormat());
                        info.setSchemaType(p.getType());
                        info.setSchemaExample(p.getExample() != null ? p.getExample().toString() : "");
                        info.setRegex(p.getPattern());

                        List<String> vals = llmGen.generateParameterValues(info);
                        val = vals.isEmpty() ? "LLM_EMPTY" : vals.get(0);
                        log.info("LLM (Fallback) → {} {} = {} (step {})", service, p.getName(), val, stepNumber);
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
        
        if (isFirstStep) {
            // For first step, always use LLM-generated bodyFields
            bodyJson = bodyFields.isEmpty() ? null : toJson(bodyFields);
            log.info("Using LLM-generated body for first step {}: {}", stepNumber, bodyJson);
        } else {
            // For subsequent steps, prefer trace body, fallback to generated fields
            bodyJson = rawBody != null
                    ? rawBody
                    : (bodyFields.isEmpty() ? null : toJson(bodyFields));
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
                bodyFields
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
        gotoChildren(span, tc, context, stepNumber, variantIndex);
    }

    /**
     * Extract parameter values from trace input/output fields.
     */
    private void extractParametersFromTrace(WorkflowStep span, 
                                           Map<String,String> bodyFields,
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
                parseFormData(requestBody, bodyFields);
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
                                         Map<String, String> bodyFields) {
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
        for (Map.Entry<String, String> entry : bodyFields.entrySet()) {
            context.put("input." + entry.getKey(), entry.getValue());
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

    /**
     * Process children with hierarchical numbering.
     */
    private void gotoChildren(WorkflowStep parent,
                              MultiServiceTestCase tc,
                              Map<String,String> ctx,
                              String parentStepNumber,
                              int variantIndex) {
        List<WorkflowStep> children = parent.getChildren();
        for (int i = 0; i < children.size(); i++) {
            String childStepNumber = parentStepNumber + "." + (i + 1);
            traverse(children.get(i), tc, ctx, childStepNumber, variantIndex);
        }
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
    private static String toJson(Map<String,String> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String,String> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(e.getKey()).append("\":")
                    .append('"').append(e.getValue()
                            .replace("\\", "\\\\")
                            .replace("\"","\\\""))
                    .append('"');
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
        
        // Skip login/auth related operations
        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();
            
            if (opLower.contains("login") || opLower.contains("auth") || 
                serviceLower.contains("login") || serviceLower.contains("auth") ||
                opLower.contains("signin") || opLower.contains("token")) {
                // Skip login/auth operations
            } else {
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
                    return verb.toUpperCase() + "_" + route;
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

                // Stage 1: Get 5 seed values from LLM
                List<String> llmSeedValues = llmGen.generateParameterValues(info);
                
                if (!llmSeedValues.isEmpty()) {
                    // Stage 2: Expand using semantic models to get more variants
                    List<String> expandedValues = expander.expandValues(llmSeedValues, 15);
                    parameterPool.put(p.getName(), expandedValues);
                    
                    log.info("Generated shared pool for parameter '{}': {} values", 
                            p.getName(), expandedValues.size());
                } else {
                    // Fallback values
                    List<String> fallbackValues = new ArrayList<>();
                    for (int i = 0; i < 15; i++) {
                        fallbackValues.add("LLM_EMPTY_" + i);
                    }
                    parameterPool.put(p.getName(), fallbackValues);
                    log.warn("LLM returned no values for parameter '{}', using fallback", p.getName());
                }
            }
        }
        
        return parameterPool;
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
        
        // Skip login/auth operations
        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();
            
            if (!(opLower.contains("login") || opLower.contains("auth") || 
                  serviceLower.contains("login") || serviceLower.contains("auth") ||
                  opLower.contains("signin") || opLower.contains("token"))) {
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
}
