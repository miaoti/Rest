package es.us.isa.restest.testcases;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.us.isa.restest.auth.AuthManipulationStrategy;
import es.us.isa.restest.configuration.pojos.Operation;
import io.swagger.v3.oas.models.PathItem.HttpMethod;


public class MultiServiceTestCase extends TestCase {

    /* -------- synthetic “root” values needed by super‑class -------- */
    private static String newSyntheticId() {
        /* completely ASCII: “workflow_” + UUID with ‘-’ swapped for ‘_’            */
        return "workflow_" + java.util.UUID
                .randomUUID()
                .toString()
                .replace('-', '_');
    }

    /* -------- constructors -------- */

    /** Default constructor used by the generator. */
    public MultiServiceTestCase() {
        super(newSyntheticId(),                 // id
                false,                            // faulty
                "workflow",                       // operationId placeholder
                "/",                              // path placeholder
                HttpMethod.POST);                 // dummy HTTP verb
        setEnableOracles(false);                // we assert per‑step, not “root”
    }

    /* Optional convenience: create and name a test */
    public MultiServiceTestCase(String name) {
        this();
        setOperationId(name);
    }

    /* -------- multi‑step data -------- */

    private final List<StepCall> steps = new ArrayList<>();

    /* name of the logical scenario this test case belongs to */
    private String scenarioName;
    
    /* track faulty parameters for Allure reporting */
    private final List<String> faultyParameters = new ArrayList<>();

    /** The hierarchical root ID that was targeted by the sniper fault injection (e.g. "Root 2"). */
    private String targetFaultRootId;

    /** The fault-type id used for this negative test (e.g. "OVERFLOW", "BOUNDARY_VIOLATION"). */
    private String faultTypeCategory;

    /** The actual API path of the targeted fault root (e.g. "POST /api/v1/orderservice/order"). */
    private String targetFaultRootApiPath;

    /**
     * Normalised location of the faulted parameter ({@code path|query|header|cookie|body}).
     * Recorded so the writer can route the invalid value into the correct request slot
     * (e.g. {@code .header(name, value)} vs {@code .cookie(name, value)}). The same name
     * may appear at different locations within one operation (e.g. path {@code {id}} +
     * header {@code Id}), so the location is required to disambiguate the target.
     */
    private String targetFaultParamLocation;

    /* -------- status code exploration fields -------- */
    
    /** Flag indicating this test was created for status code exploration */
    private boolean isStatusCodeExplorationTest = false;
    
    /** Target status code this exploration test is trying to trigger (-1 = not targeting specific code) */
    private int targetStatusCode = -1;
    
    /** Auth manipulation configuration for this test (null = use default auth) */
    private AuthManipulationStrategy.AuthConfig authManipulation = null;

    /** Add a step (request/response) to the workflow. */
    public void addStepCall(StepCall step) {
        steps.add(step);
    }

    /** Ordered list of workflow steps. */
    public List<StepCall> getSteps() {
        return steps;
    }

    public void setScenarioName(String s) {
        this.scenarioName = s;
    }

    public String getScenarioName() { return scenarioName; }
    
    /** Track a parameter that was made faulty in this test case */
    public void addFaultyParameter(String paramName, String faultyValue) {
        faultyParameters.add(paramName + "=" + faultyValue);
    }
    
    /** Get list of faulty parameters for reporting */
    public List<String> getFaultyParameters() {
        return faultyParameters;
    }

    public String getTargetFaultRootId() { return targetFaultRootId; }
    public void setTargetFaultRootId(String targetFaultRootId) { this.targetFaultRootId = targetFaultRootId; }

    public String getFaultTypeCategory() { return faultTypeCategory; }
    public void setFaultTypeCategory(String faultTypeCategory) { this.faultTypeCategory = faultTypeCategory; }

    public String getTargetFaultRootApiPath() { return targetFaultRootApiPath; }
    public void setTargetFaultRootApiPath(String targetFaultRootApiPath) { this.targetFaultRootApiPath = targetFaultRootApiPath; }

    public String getTargetFaultParamLocation() { return targetFaultParamLocation; }
    public void setTargetFaultParamLocation(String targetFaultParamLocation) { this.targetFaultParamLocation = targetFaultParamLocation; }

    /**
     * Pre-recorded invalid value selected for the targeted faulty parameter, captured at
     * fault-queue build time so the generator does not need to re-rotate
     * {@code InvalidInputPool} state at fire time. Decouples the fault label (already on
     * {@link #faultTypeCategory}) from the value, eliminating any drift if pool state changes
     * between queue construction and variant emission.
     */
    private Object targetFaultValue;
    private boolean hasTargetFaultValue = false;

    public Object getTargetFaultValue() { return targetFaultValue; }
    public boolean hasTargetFaultValue() { return hasTargetFaultValue; }
    public void setTargetFaultValue(Object value) {
        this.targetFaultValue = value;
        this.hasTargetFaultValue = true;
    }

    /* -------- status code exploration methods -------- */
    
    /** 
     * Mark this test as a status code exploration test.
     * Exploration tests are created to trigger specific HTTP status codes.
     */
    public void setStatusCodeExplorationTest(boolean isExplorationTest) {
        this.isStatusCodeExplorationTest = isExplorationTest;
    }
    
    /** Check if this test is a status code exploration test */
    public boolean isStatusCodeExplorationTest() {
        return isStatusCodeExplorationTest;
    }
    
    /**
     * Set the target status code this test is trying to trigger.
     * @param statusCode The HTTP status code to target (e.g., 404, 401, 409)
     */
    public void setTargetStatusCode(int statusCode) {
        this.targetStatusCode = statusCode;
    }
    
    /** Get the target status code (-1 if not targeting specific code) */
    public int getTargetStatusCode() {
        return targetStatusCode;
    }
    
    /**
     * Set auth manipulation configuration for this test.
     * Used for testing 401/403 status codes.
     */
    public void setAuthManipulation(AuthManipulationStrategy.AuthConfig authConfig) {
        this.authManipulation = authConfig;
    }
    
    /** Get auth manipulation configuration (null = use default auth) */
    public AuthManipulationStrategy.AuthConfig getAuthManipulation() {
        return authManipulation;
    }
    
    /**
     * Get a description of the target status code for Allure reporting.
     */
    public String getTargetStatusCodeDescription() {
        if (!isStatusCodeExplorationTest || targetStatusCode < 0) {
            return "Default";
        }
        
        String category;
        if (targetStatusCode >= 200 && targetStatusCode < 300) {
            category = "Success";
        } else if (targetStatusCode >= 400 && targetStatusCode < 500) {
            category = "Client Error";
        } else if (targetStatusCode >= 500 && targetStatusCode < 600) {
            category = "Server Error";
        } else {
            category = "Other";
        }
        
        return targetStatusCode + " " + category;
    }


    /**
     * One REST call inside the workflow.
     */
    public static class StepCall {
        private final String serviceName;
        private final Operation method;               // "get", "post", …
        private final String path;                 // URI template
        private final Map<String,String> pathParams;
        private final Map<String,String> queryParams;
        private final Map<String,String> headers;
        /**
         * Cookie parameters keyed by cookie name (OpenAPI {@code in: cookie}). Previously
         * cookie-located parameters were silently dropped on the generator path and
         * therefore not emitted on the writer side. Initialised to an empty
         * {@link LinkedHashMap} so legacy {@link StepCall} construction (the 9-arg
         * ctor) keeps working without a separate cookies argument.
         */
        private final Map<String,String> cookies = new LinkedHashMap<>();
        private final String body;                 // JSON or form string
        private final int expectedStatus;
        private List<String> outputKeys;

        /* keeps the original body parsed so later steps can reference fields */
        private final Map<String,String> bodyFields;

        /* output keys to capture → later steps can reference them */
        private final List<String> captureOutputKeys = new ArrayList<>();

        /* dependencies: paramName -> (stepIndex,keyInThatStep) */
        private final Map<String,Dependency> paramDependencies = new LinkedHashMap<>();
        
        /* NEW: workflow dependencies: list of step indices this step depends on for workflow flow */
        private final List<Integer> workflowDependencies = new ArrayList<>();
        
        /* NEW: dependency type classification */
        private DependencyType dependencyType = DependencyType.INDEPENDENT;

        /** Hierarchical step ID (e.g., "R1", "R2", "R1.1", "R1.2.3"). */
        private String hierarchicalId = "";
        /** True if this step represents a top-level Root API (not an internal span). */
        private boolean topLevelRoot = false;
        /** True if this step was a Root API merged from another trace via data dependency. */
        private boolean mergedRootStep = false;
        /** 1-based index of the producer root this merged step depends on, or -1. */
        private int producerRootIndex = -1;
        /** Provenance bindings: paramKey -> concrete value inherited from the producer root. */
        private final Map<String, String> provenanceBindings = new LinkedHashMap<>();

        /** Raw response body from the trace span (for jsonPath extraction by the writer). */
        private String traceResponseBody;

        public StepCall(String serviceName, Operation method, String path,
                        Map<String,String> pathParams,
                        Map<String,String> queryParams,
                        Map<String,String> headers,
                        String body, int expectedStatus,
                        Map<String,String> bodyFields) {

            this.serviceName = serviceName;
            this.method      = method;
            this.path        = path;
            this.pathParams  = (pathParams  != null ? pathParams  : new LinkedHashMap<>());
            this.queryParams = (queryParams != null ? queryParams : new LinkedHashMap<>());
            this.headers     = (headers     != null ? headers     : new LinkedHashMap<>());
            this.body        = body;
            this.expectedStatus = expectedStatus;
            this.bodyFields  = (bodyFields  != null ? new LinkedHashMap<>(bodyFields)
                    : new LinkedHashMap<>());
        }

        /* getters */
        public String getServiceName()           { return serviceName; }
        public Operation getMethod()                { return method; }
        public String getPath()                  { return path; }
        public Map<String,String> getPathParams(){ return pathParams; }
        public Map<String,String> getQueryParams(){ return queryParams; }
        public Map<String,String> getHeaders()   { return headers; }
        public Map<String,String> getCookies()   { return cookies; }
        public String getBody()                  { return body; }
        public int getExpectedStatus()           { return expectedStatus; }
        public Map<String,String> getBodyFields(){ return bodyFields; }
        public List<String> getCaptureOutputKeys(){ return captureOutputKeys; }
        public Map<String,Dependency> getParamDependencies(){ return paramDependencies; }
        
        /* NEW: getters for enhanced dependency management */
        public List<Integer> getWorkflowDependencies() { return workflowDependencies; }
        public DependencyType getDependencyType() { return dependencyType; }

        /* helpers for dependency wiring */
        public void addCaptureOutputKey(String key) {
            if (!captureOutputKeys.contains(key)) captureOutputKeys.add(key);
        }
        public void addParamDependency(String param,
                                       int sourceStepIdx,
                                       String sourceKey) {
            paramDependencies.put(param, new Dependency(sourceStepIdx, sourceKey));
        }
        
        /* Hierarchical naming accessors */
        public String getHierarchicalId() { return hierarchicalId; }
        public void setHierarchicalId(String id) { this.hierarchicalId = id; }

        public boolean isTopLevelRoot() { return topLevelRoot; }
        public void setTopLevelRoot(boolean topLevelRoot) { this.topLevelRoot = topLevelRoot; }

        public boolean isMergedRootStep() { return mergedRootStep; }
        public void setMergedRootStep(boolean mergedRootStep) { this.mergedRootStep = mergedRootStep; }

        public int getProducerRootIndex() { return producerRootIndex; }
        public void setProducerRootIndex(int idx) { this.producerRootIndex = idx; }

        public Map<String, String> getProvenanceBindings() { return provenanceBindings; }

        public String getTraceResponseBody() { return traceResponseBody; }
        public void setTraceResponseBody(String body) { this.traceResponseBody = body; }
        public void addProvenanceBinding(String key, String value) {
            provenanceBindings.put(key, value);
        }

        /* NEW: methods for enhanced dependency management */
        public void addWorkflowDependency(int stepIndex) {
            if (!workflowDependencies.contains(stepIndex)) {
                workflowDependencies.add(stepIndex);
            }
        }
        
        public void setDependencyType(DependencyType type) {
            this.dependencyType = type;
        }
        
        /**
         * Check if this step should execute based on the results of previous steps
         * @param stepResults Map of step index -> success/failure
         * @return ExecutionDecision indicating whether to execute, skip, or what type of skip
         */
        public ExecutionDecision shouldExecute(Map<Integer, Boolean> stepResults) {
            switch (dependencyType) {
                case DATA_DEPENDENCY:
                    // Check if any data dependency failed
                    for (Dependency dep : paramDependencies.values()) {
                        if (!stepResults.getOrDefault(dep.sourceStepIndex, false)) {
                            return new ExecutionDecision(false, SkipReason.DATA_DEPENDENCY_FAILED,
                                    "Required data from step " + dep.sourceStepIndex + " is not available");
                        }
                    }
                    return new ExecutionDecision(true, null, null);
                    
                case WORKFLOW_DEPENDENCY:
                    // Check if any workflow dependency failed
                    for (int workflowDep : workflowDependencies) {
                        if (!stepResults.getOrDefault(workflowDep, false)) {
                            return new ExecutionDecision(false, SkipReason.WORKFLOW_DEPENDENCY_FAILED,
                                    "Workflow predecessor step " + workflowDep + " failed");
                        }
                    }
                    return new ExecutionDecision(true, null, null);
                    
                case INDEPENDENT:
                default:
                    // Independent steps always execute
                    return new ExecutionDecision(true, null, null);
            }
        }

        public void addParameter(String key, String value) {
        }

        public void setCaptureOutputKeys(List<String> outputKeys) {
            this.outputKeys = outputKeys;
        }
    }

    public static class Dependency {
        public final int    sourceStepIndex;
        public final String sourceOutputKey;
        /** Pre-computed type-safe fallback value for resilient bypass when source step fails. */
        public String fallbackValue;
        public Dependency(int idx, String key) {
            this.sourceStepIndex = idx;
            this.sourceOutputKey = key;
            this.fallbackValue   = null;
        }
    }
    
    /**
     * NEW: Enum to classify different types of step dependencies
     */
    public enum DependencyType {
        /**
         * Step needs data output from previous steps to function correctly.
         * Should be skipped if data dependencies fail.
         */
        DATA_DEPENDENCY,
        
        /**
         * Step is part of a workflow sequence and depends on workflow flow.
         * Should be skipped if workflow predecessors fail.
         */
        WORKFLOW_DEPENDENCY,
        
        /**
         * Step can execute independently of other step results.
         * Should always execute regardless of other failures.
         */
        INDEPENDENT
    }
    
    /**
     * NEW: Result of execution decision for a step
     */
    public static class ExecutionDecision {
        public final boolean shouldExecute;
        public final SkipReason skipReason;
        public final String skipMessage;
        
        public ExecutionDecision(boolean shouldExecute, SkipReason skipReason, String skipMessage) {
            this.shouldExecute = shouldExecute;
            this.skipReason = skipReason;
            this.skipMessage = skipMessage;
        }
    }
    
    /**
     * NEW: Reasons why a step might be skipped
     */
    public enum SkipReason {
        DATA_DEPENDENCY_FAILED("Data dependency failed"),
        WORKFLOW_DEPENDENCY_FAILED("Workflow dependency failed"),
        SERVICE_UNAVAILABLE("Service unavailable"),
        AUTH_FAILED("Authentication failed");
        
        public final String description;
        
        SkipReason(String description) {
            this.description = description;
        }
    }
}
