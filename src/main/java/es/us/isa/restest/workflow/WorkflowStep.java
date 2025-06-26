package es.us.isa.restest.workflow;
import java.util.*;

/**
 * Represents a single step in a workflow scenario, corresponding to a single OpenTelemetry span or operation.
 * A WorkflowStep contains identifying information (trace and span IDs, service and operation names, timing)
 * and the extracted input/output data of that operation. It is linked to other steps via parent and children
 * references to reflect the workflow's execution order (including both call hierarchy and sequence).
 * <p>
 * This class is designed for extension so that specialized steps (e.g., representing asynchronous events or
 * fault injections) could subclass WorkflowStep in the future if needed.
 */
public class WorkflowStep {
    /** The trace ID this span belongs to (empty if not applicable). */
    private final String traceId;
    /** The span ID of this operation (empty if not applicable). */
    private final String spanId;
    /** The name of the service or component that produced this span. */
    private final String serviceName;
    /** The operation name or descriptive route of this span (e.g., HTTP method and path). */
    private final String operationName;
    /** The start timestamp of this span (epoch time in milliseconds or nanoseconds, as provided). */
    private final long startTime;
    /** The end timestamp of this span (epoch time). */
    private final long endTime;
    /** Key-value map of input fields for this step (e.g., request parameters or message attributes). */
    private final Map<String, String> inputFields;
    /** Key-value map of output fields for this step (e.g., response fields or emitted data). */
    private final Map<String, String> outputFields;

    private long StartTime;
    private long Endtime;

    /** Reference to the parent step in the workflow (null if this step has no parent in the scenario). */
    private WorkflowStep parent;
    /** Temporary storage for parent span ID during construction phase. */
    private String parentSpanIdTemp;
    /** List of child steps directly triggered by this step in the workflow. */
    private final List<WorkflowStep> children = new ArrayList<>();

    /**
     * Constructs a WorkflowStep with the given properties. All fields are required except parent (which
     * will be set when the step is attached to a scenario). The inputFields and outputFields maps will be
     * wrapped to prevent external modification.
     *
     * @param traceId       the trace ID of the span (can be null or empty if not applicable)
     * @param spanId        the span ID (can be null or empty if not applicable)
     * @param serviceName   the service name that produced this span (or "unknown" if not available)
     * @param operationName the operation/route name for this span (or "unknown" if not available)
     * @param startTime     the start timestamp of the span
     * @param endTime       the end timestamp of the span
     * @param inputFields   map of input field keys to values (may be empty or null if none)
     * @param outputFields  map of output field keys to values (may be empty or null if none)
     */
    public WorkflowStep(String traceId, String spanId, String serviceName, String operationName,
                        long startTime, long endTime,
                        Map<String, String> inputFields, Map<String, String> outputFields) {
        this.traceId = (traceId != null ? traceId : "");
        this.spanId = (spanId != null ? spanId : "");
        this.serviceName = (serviceName != null && !serviceName.isEmpty() ? serviceName : "unknown");
        this.operationName = (operationName != null && !operationName.isEmpty() ? operationName : "unknown");
        this.startTime = startTime;
        this.endTime = endTime;
        // Wrap input and output maps in unmodifiable collections to enforce immutability from outside
        if (inputFields != null) {
            this.inputFields = Collections.unmodifiableMap(new HashMap<>(inputFields));
        } else {
            this.inputFields = Collections.emptyMap();
        }
        if (outputFields != null) {
            this.outputFields = Collections.unmodifiableMap(new HashMap<>(outputFields));
        } else {
            this.outputFields = Collections.emptyMap();
        }
        this.parent = null;
    }

    /** Returns the trace ID associated with this step (could be empty if not applicable). */
    public String getTraceId() {
        return traceId;
    }

    /** Returns the span ID of this step (could be empty if not applicable). */
    public String getSpanId() {
        return spanId;
    }

    /** Returns the service/component name that generated this step. */
    public String getServiceName() {
        return serviceName;
    }

    /** Returns the operation name or HTTP method/path for this step. */
    public String getOperationName() {
        return operationName;
    }

    /** Returns the start timestamp of the span (in the original epoch time unit provided). */
    public long getStartTime() {
        return startTime;
    }

    /** Returns the end timestamp of the span. */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Returns an unmodifiable map of input fields for this step.
     * This includes parsed request parameters, query parameters, or message attributes that served as inputs.
     */
    public Map<String, String> getInputFields() {
        return inputFields;
    }

    /**
     * Returns an unmodifiable map of output fields for this step.
     * This includes parsed response fields or data emitted by this operation that may feed into subsequent steps.
     */
    public Map<String, String> getOutputFields() {
        return outputFields;
    }

    /** Returns the parent step in the workflow scenario, or null if this step is a root. */
    public WorkflowStep getParent() {
        return parent;
    }

    /**
     * Sets the parent of this step.
     * @param parentStep the new parent step (or null to mark this as a root)
     */
    public void setParent(WorkflowStep parentStep) {
        this.parent = parentStep;
    }

    /**
     * Returns an unmodifiable list of child steps triggered by this step, in chronological order of start time.
     * These represent operations (in the same or other services) that were initiated as a result of this step.
     */
    public List<WorkflowStep> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Adds a child step triggered by this step. This will automatically set the child's parent to this step
     * and update the child list. If the child was previously attached under a different parent, it will be removed from that parent.
     *
     * @param child the WorkflowStep that was caused by this step (e.g., an outgoing call or emitted event)
     */
    public void addChild(WorkflowStep child) {
        if (child == null) return;
        // If the child already had a parent, remove it from that parent's children list to avoid orphaning it there
        WorkflowStep oldParent = child.parent;
        if (oldParent != null) {
            oldParent.children.remove(child);
        }
        // Set this step as the child's parent and add child to this step's list
        child.parent = this;
        children.add(child);
    }

    @Override
    public String toString() {
        // Provide a concise identification of the step for debugging (not a full tree).
        return "WorkflowStep{" + serviceName + " - " + operationName +
                " (trace " + traceId + ", span " + spanId + ")}";
    }

    public void setParentSpanIdTemp(String parentSpanId) {
        this.parentSpanIdTemp = parentSpanId;
    }

    public String getParentSpanIdTemp() {
        return  parentSpanIdTemp;
    }

    public void setStartTime(long l) {
        this.StartTime = l;
    }

    public void setEndTime(long l) {
        this.Endtime = l;
    }

    public void sortChildrenByStartTime() {
        // sort the *internal* mutable list
        children.sort(Comparator.comparingLong(WorkflowStep::getStartTime));
        // then recurse
        for (WorkflowStep c : children) {
            c.sortChildrenByStartTime();
        }
    }
}
