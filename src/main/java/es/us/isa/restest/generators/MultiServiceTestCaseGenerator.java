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

    /** Produce one test‑case per recorded scenario. */
    @Override
    public Collection<TestCase> generate() {
        List<TestCase> out = new ArrayList<>();
        int counter = 1;

        for (WorkflowScenario sc : scenarios) {
            MultiServiceTestCase tc = new MultiServiceTestCase("Scenario_" + counter++);
            tc.setScenarioName(tc.getOperationId());        // for Allure etc.

            Map<String,String> context = new HashMap<>();   // output‑values so far
            
            // Process workflow steps with hierarchical numbering
            for (WorkflowStep root : sc.getRootSteps()) {
                traverse(root, tc, context, "1");
            }
            out.add(tc);
        }
        return out;
    }

    @Override
    protected Collection<TestCase> generateOperationTestCases(Operation op) { return Collections.emptyList(); }
    @Override
    public    TestCase              generateNextTestCase(Operation op)      { return null; }
    @Override
    protected boolean               hasNext()                               { return false; }

    /* ============================================================ */

    /**
     * Depth‑first traversal with hierarchical step numbering.
     *
     * @param span      current WorkflowStep
     * @param tc        test‑case under construction
     * @param context   key→value outputs collected so far
     * @param stepNumber hierarchical step number (e.g., "1", "1.1", "1.2.1")
     */
    private void traverse(WorkflowStep span,
                          MultiServiceTestCase tc,
                          Map<String,String> context,
                          String stepNumber) {

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
                gotoChildren(span, tc, context, stepNumber);
                return;
            }
        }

        if (verb == null || route == null) {
            log.debug("Could not extract HTTP method/path from span: {} - {}", service, opName);
            gotoChildren(span, tc, context, stepNumber);
            return;
        }

        /* 2. Load service‑specific test‑configuration ------------------------------ */
        TestConfigurationObject cfg = serviceConfigs.get(service);
        if (cfg == null) {
            log.warn("No test‑configuration for service '{}' (step {})", service, stepNumber);
            gotoChildren(span, tc, context, stepNumber);
            return;
        }

        Operation opCfg = findOperation(cfg, verb, route);
        if (opCfg == null) {
            log.warn("No Operation config {} {} in service '{}' (step {})", verb, route, service, stepNumber);
            gotoChildren(span, tc, context, stepNumber);
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
                    /* For the first step, always use LLM to generate initial parameters */
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

                        List<String> vals = llmGen.generateParameterValues(info);
                        val = vals.isEmpty() ? "LLM_EMPTY" : vals.get(0);
                        log.info("LLM (First Step) → {} {} = {} (step {})", service, p.getName(), val, stepNumber);
                    } else {
                        /* Fallback if LLM is disabled */
                        val = "INIT_" + p.getName();
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

        /* 4. Expected status from trace -------------------------------------------- */
        int expectedStatus = 200;
        try {
            if (opCfg.getExpectedResponse() != null)
                expectedStatus = Integer.parseInt(opCfg.getExpectedResponse());
        } catch (NumberFormatException ignore) { /* keep default */ }

        // Override with actual HTTP code from trace
        Object recorded = span.getOutputFields().get("http.status_code");
        if (recorded != null) {
            try {
                expectedStatus = Integer.parseInt(recorded.toString());
            } catch (NumberFormatException ignore) { /* fallback to configured/default */ }
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
        gotoChildren(span, tc, context, stepNumber);
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
                              String parentStepNumber) {
        List<WorkflowStep> children = parent.getChildren();
        for (int i = 0; i < children.size(); i++) {
            String childStepNumber = parentStepNumber + "." + (i + 1);
            traverse(children.get(i), tc, ctx, childStepNumber);
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
}
