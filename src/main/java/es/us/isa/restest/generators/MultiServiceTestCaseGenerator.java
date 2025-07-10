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

    /** Produce test cases using two-stage LLM + semantic expansion approach. */
    @Override
    public Collection<TestCase> generate() {
        List<TestCase> out = new ArrayList<>();
        int counter = 1;

        for (WorkflowScenario sc : scenarios) {
            // Generate multiple variants per scenario using expanded parameter sets
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
            scenarioId = firstApiName.replaceAll("[^a-zA-Z0-9_]", "_")
                                       .replaceAll("_+", "_")
                                       .replaceAll("^_|_$", "");
        } else {
            String sourceFileName = sc.getSourceFileName();
            if (sourceFileName != null && !sourceFileName.isEmpty()) {
                scenarioId = sourceFileName.replaceAll("[^a-zA-Z0-9_]", "_");
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
                    /* For the first step, use two-stage approach: LLM + semantic expansion */
                    if (useLLM) {
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
                            
                            // Select value based on variant index
                            if (variantIndex < expandedValues.size()) {
                                val = expandedValues.get(variantIndex);
                                log.info("Two-Stage (LLM+Semantic) → {} {} = {} (variant {}, step {})", 
                                        service, p.getName(), val, variantIndex, stepNumber);
                            } else {
                                // Fallback to cycling through available values
                                val = expandedValues.get(variantIndex % expandedValues.size());
                                log.info("Two-Stage (Cycled) → {} {} = {} (variant {}, step {})", 
                                        service, p.getName(), val, variantIndex, stepNumber);
                            }
                        } else {
                            val = "LLM_EMPTY";
                            log.warn("LLM returned no values for {} {}", service, p.getName());
                        }
                    } else {
                        /* Fallback if LLM is disabled */
                        val = "INIT_" + p.getName() + "_v" + variantIndex;
                    }
                } else {
                    /* For subsequent steps, use the dependency-based logic */

                    /* 3a. Use previously captured value from context (dependency) ------ */
                    val = context.get(p.getName());

                    /* 3b. Use trace data if available and no context value ------------- */
                    if (val == null) {
                        val = getTraceParameterValue(span, p.getName());
                    }

                    /* 3c. Call LLM if enabled and no trace/context value --------------- */
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

                    /* 3d. Ultimate fallback ---------------------------------------- */
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
     */
    private void setStepDependencies(MultiServiceTestCase.StepCall call, 
                                   WorkflowStep span, 
                                   int currentStepIndex) {
        // Analyze trace data to determine if this step depends on previous steps
        // This is where we implement the logic to chain parameters based on trace dependencies
        
        // For now, we'll implement a simple version that doesn't stop on failures
        // but uses trace data dependencies
        
        // TODO: Implement sophisticated dependency analysis based on trace field matching
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
                log.debug("Skipping login/auth operation: {} in service {}", opName, serviceName);
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
                        // Check if we can extract from attributes/tags
                        Map<String, String> outputs = step.getOutputFields();
                        String httpMethod = outputs.get("http.method");
                        String httpTarget = outputs.get("http.target");
                        String httpUrl = outputs.get("http.url");
                        
                        if (httpMethod != null && (httpTarget != null || httpUrl != null)) {
                            verb = httpMethod.toLowerCase(Locale.ROOT);
                            route = httpTarget != null ? httpTarget : extractPathFromUrl(httpUrl);
                        } else if (httpMethod != null) {
                            // If we only have the method, try to construct a meaningful name
                            verb = httpMethod.toLowerCase(Locale.ROOT);
                            route = serviceName.replaceAll("[^a-zA-Z0-9_]", "_"); // Use service name as route
                        }
                    }
                }
                
                // Return a descriptive name if we can extract method and path
                if (verb != null && route != null) {
                    String descriptiveName = verb.toUpperCase() + "_" + route.replaceAll("[^a-zA-Z0-9_]", "_");
                    log.debug("Found first business API operation: {} {} -> {}", verb, route, descriptiveName);
                    return descriptiveName;
                } else {
                    // Fallback to original operation name
                    log.debug("Found first business API operation: {} in service {}", opName, serviceName);
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
     * Read variant count from properties file with fallback to defaults
     */
    private int getVariantCountFromProperties() {
        try {
            // Debug: Print all system properties related to test generation
            log.info("DEBUG: Checking variant count properties...");
            log.info("DEBUG: System property 'test.variants.per.scenario' = {}", System.getProperty("test.variants.per.scenario"));
            log.info("DEBUG: System property 'testsperoperation' = {}", System.getProperty("testsperoperation"));
            
            // Try to read from system properties first (set by TestGenerationAndExecution)
            String variantsProp = System.getProperty("test.variants.per.scenario");
            if (variantsProp != null) {
                int count = Integer.parseInt(variantsProp);
                log.info("✅ Using test.variants.per.scenario from properties: {}", count);
                return count;
            }
            
            // Try testsperoperation as fallback
            String testsProp = System.getProperty("testsperoperation");
            if (testsProp != null) {
                int count = Integer.parseInt(testsProp);
                log.info("✅ Using testsperoperation from properties: {}", count);
                return count;
            }
            
            // Default behavior - FIXED to generate 10 variants as requested
            int defaultCount = useLLM ? 10 : 1; // Changed from 4 to 10 to match user requirement
            log.warn("❌ No variant count found in properties, using default: {} (useLLM={})", defaultCount, useLLM);
            return defaultCount;
        } catch (NumberFormatException e) {
            int defaultCount = useLLM ? 10 : 1; // Changed from 4 to 10 to match user requirement
            log.warn("❌ Invalid variant count in properties, using default: {} (error: {})", defaultCount, e.getMessage());
            return defaultCount;
        }
    }
}
