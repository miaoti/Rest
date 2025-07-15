package es.us.isa.restest.testcases;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        public Dependency(int idx, String key) {
            this.sourceStepIndex = idx;
            this.sourceOutputKey = key;
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
