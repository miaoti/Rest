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


public class MultiServiceTestCaseGenerator extends AbstractTestCaseGenerator {

    /* ------------------------------------------------------------ */
    private static final Logger log = LogManager.getLogger(MultiServiceTestCaseGenerator.class);

    private final Map<String, OpenAPISpecification>          serviceSpecs;
    private final Map<String, TestConfigurationObject>       serviceConfigs;
    private final List<WorkflowScenario>                     scenarios;
    private final boolean                                    useLLM;
    private final AiDrivenLLMGenerator                       llmGen = new AiDrivenLLMGenerator();

    public MultiServiceTestCaseGenerator(OpenAPISpecification primarySpec,
                                         TestConfigurationObject dummyPrimaryConf,
                                         Map<String, OpenAPISpecification> serviceSpecs,
                                         Map<String, TestConfigurationObject> serviceConfigs,
                                         List<WorkflowScenario> scenarios,
                                         boolean useLLMforParams,
                                         @SuppressWarnings("unused") boolean ignoreFlowsFlag) {

        /* we never call the AbstractTestCaseGenerator’s generation loop,
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
            for (WorkflowStep root : sc.getRootSteps()) {
                traverse(root, tc, context);
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
    /*  INTERNALS                                                   */
    /* ============================================================ */

    /**
     * Depth‑first traversal – parent before children.
     *
     * @param span      current WorkflowStep
     * @param tc        test‑case under construction
     * @param context   key→value outputs collected so far
     */
    private void traverse(WorkflowStep span,
                          MultiServiceTestCase tc,
                          Map<String,String> context) {

        /* 1. Basic routing data ---------------------------------------------------- */
        final String service = span.getServiceName();
        final String opName  = span.getOperationName();

        String verb, route;
        {
            String[] parts = opName.split(" ", 2);
            if (parts.length != 2) {
                log.warn("Skipping span – unparseable opName: {}", opName);
                gotoChildren(span, tc, context);
                return;
            }
            verb  = parts[0].toLowerCase(Locale.ROOT);
            route = parts[1];
        }

        /* 2. Load service‑specific test‑configuration ------------------------------ */
        TestConfigurationObject cfg = serviceConfigs.get(service);
        if (cfg == null) {
            log.warn("No test‑configuration for service '{}'", service);
            gotoChildren(span, tc, context);
            return;
        }

        Operation opCfg = findOperation(cfg, verb, route);
        if (opCfg == null) {
            log.warn("No Operation config {} {} in service '{}'", verb, route, service);
            gotoChildren(span, tc, context);
            return;
        }

        /* 3. Build parameter maps --------------------------------------------------- */
        Map<String,String> pathParams   = new LinkedHashMap<>();
        Map<String,String> queryParams  = new LinkedHashMap<>();
        Map<String,String> headerParams = new LinkedHashMap<>();
        Map<String,String> bodyFields   = new LinkedHashMap<>();

//        boolean isLoginRoute = route.equals("/login");
//        if (isLoginRoute) {
//            // override any LLM or captured values with your real credentials
//            bodyFields.clear();
//            bodyFields.put("username", "YOUR_USERNAME");
//            bodyFields.put("password", "YOUR_PASSWORD");
//        }

        String resolvedPath = route;

        if (opCfg.getTestParameters() != null) {
            for (TestParameter p : opCfg.getTestParameters()) {

                /* 3a. prefer previously captured value ----------------------------- */
                String val = context.get(p.getName());

                /* 3b. otherwise call the LLM (if enabled) -------------------------- */
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
                    log.info("LLM → {} {} = {}", service, p.getName(), val);
                }

                /* 3c. ultimate fallback ------------------------------------------- */
                if (val == null) val = "VAL_" + p.getName();

                /* 3d. store in correct container ----------------------------------- */
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
                        bodyFields.put(p.getName(), val);
                        break;
                    case "formdata":
                        bodyFields.put(p.getName(), val);
                        break;
                }
            }
        }

        String rawBody = span.getInputFields().get("http.request.body");
        String bodyJson = rawBody != null
                ? rawBody
                : (bodyFields.isEmpty() ? null : toJson(bodyFields));



        /* 4. Expected status -------------------------------------------------------- */
        int expectedStatus = 200;
        try {
            if (opCfg.getExpectedResponse() != null)
                expectedStatus = Integer.parseInt(opCfg.getExpectedResponse());
        } catch (NumberFormatException ignore) { /* keep default */ }

// override with the *actual* HTTP code recorded in the trace, if present
        Object recorded = span.getOutputFields().get("http.status_code");
        if (recorded != null) {
            try {
                expectedStatus = Integer.parseInt(recorded.toString());
            } catch (NumberFormatException ignore) { /* fallback to configured/default */ }
        }


        /* 5. Create the StepCall ---------------------------------------------------- */
        MultiServiceTestCase.StepCall call = new MultiServiceTestCase.StepCall(
                service,                      // serviceName
                opCfg,                        // Operation cfg (acts as “method” field)
                resolvedPath,                 // resolved URI
                pathParams,
                queryParams,
                headerParams,
                bodyJson,
                expectedStatus,
                bodyFields
        );

        System.out.println(">> StepCall = " + span.getServiceName() + " "
                + span.getOperationName()
                + " body=" + call.getBody()
                + " expected=" + call.getExpectedStatus()
                + " captures=" + call.getCaptureOutputKeys());
        
        /* capture all output fields so later steps can reference them */
        for (String key : span.getOutputFields().keySet()) {
            if (!key.startsWith("http.")) {
                call.addCaptureOutputKey(key);
            }
        }

        tc.addStepCall(call);

        /* 6. expose outputs for downstream dependency resolution ------------------- */
        context.putAll(span.getOutputFields());

        /* 7. recurse --------------------------------------------------------------- */
        gotoChildren(span, tc, context);
    }

    /* ----------------------------------------------------------------------------- */

    private void gotoChildren(WorkflowStep parent,
                              MultiServiceTestCase tc,
                              Map<String,String> ctx) {
        for (WorkflowStep child : parent.getChildren()) {
            traverse(child, tc, ctx);
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

    /** super‑simple JSON builder – sufficient for synthetic test bodies. */
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
