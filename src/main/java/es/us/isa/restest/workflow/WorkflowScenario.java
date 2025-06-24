package es.us.isa.restest.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a multi-step workflow scenario, which may consist of multiple trace segments.
 * A WorkflowScenario contains a collection of {@link WorkflowStep} instances organized
 * by parent-child relationships (reflecting call hierarchy and sequence). It can also
 * encompass multiple trace IDs if data dependencies link separate traces into one logical workflow.
 * <p>
 * This class provides methods to collect trace IDs and root steps, and to merge scenarios
 * when cross-trace dependencies are detected. It is designed for extensibility, so new types of
 * steps (e.g., asynchronous events or fault injection steps) can be integrated in the future.
 */
public class WorkflowScenario {
    /** The set of all trace IDs represented in this scenario. */
    private final Set<String> traceIds = new LinkedHashSet<>();
    /** The list of root steps (steps with no parent in this scenario). */
    private final List<WorkflowStep> rootSteps = new ArrayList<>();

    /** Creates an empty WorkflowScenario. */
    public WorkflowScenario() {
        // Nothing to initialize beyond empty collections.
    }

    /**
     * Returns an unmodifiable set of all trace IDs that are represented in this scenario.
     * If multiple traces have been merged, all their IDs will be included.
     */
    public Set<String> getTraceIds() {
        // Return a safe copy or unmodifiable view to preserve encapsulation
        return Collections.unmodifiableSet(traceIds);
    }

    /**
     * Returns an unmodifiable list of root steps of the scenario.
     * Root steps are those with no parent step within this scenario (entry points of the workflow).
     */
    public List<WorkflowStep> getRootSteps() {
        return Collections.unmodifiableList(rootSteps);
    }

    /**
     * Adds a root step to this scenario (a step that starts an independent workflow).
     * This also registers the step's trace ID with the scenario.
     *
     * @param step the WorkflowStep to add as a root of this scenario
     */
    public void addRootStep(WorkflowStep step) {
        if (step == null) return;
        // Ensure the step is marked as a root in this scenario
        step.setParent(null);
        rootSteps.add(step);
        // Record the trace ID of this step in the scenario (if present)
        String traceId = step.getTraceId();
        if (traceId != null && !traceId.isEmpty()) {
            traceIds.add(traceId);
        }
    }

    /**
     * Internal helper to register an additional trace ID in this scenario.
     * This is used when merging scenarios.
     *
     * @param traceId the trace ID to add
     */
    void addTraceId(String traceId) {
        if (traceId != null && !traceId.isEmpty()) {
            traceIds.add(traceId);
        }
    }

    /**
     * Merges another WorkflowScenario into this one by attaching a given root step of the other
     * scenario as a child of a step in this scenario. All trace IDs from the other scenario
     * are absorbed, and any remaining root steps of the other scenario (if its trace was incomplete)
     * are also added as roots to this scenario.
     *
     * @param other the other scenario to merge into this one
     * @param attachParent the step in this scenario that will become parent of the attachChild
     * @param attachChild the root step from the other scenario that will be attached as child
     */
    void mergeWith(WorkflowScenario other, WorkflowStep attachParent, WorkflowStep attachChild) {
        if (other == null || attachParent == null || attachChild == null) {
            return;
        }
        // Remove the child from other's roots (if it was a root there)
        other.rootSteps.remove(attachChild);
        // Attach the child under the specified parent in this scenario
        attachParent.addChild(attachChild);
        // Transfer all trace IDs from the other scenario
        for (String tid : other.traceIds) {
            this.traceIds.add(tid);
        }
        // If the other scenario has any additional root steps (e.g., if that trace had multiple roots or missing parent spans),
        // bring them into this scenario as separate roots.
        for (WorkflowStep remainingRoot : other.rootSteps) {
            if (remainingRoot != attachChild) {
                // Mark as a new root in this scenario
                remainingRoot.setParent(null);
                this.rootSteps.add(remainingRoot);
            }
        }
    }

    @Override
    public String toString() {
        // Produce a human-readable representation of the scenario for debugging.
        StringBuilder sb = new StringBuilder();
        sb.append("WorkflowScenario{traceIds=").append(traceIds).append("}\n");
        for (WorkflowStep root : rootSteps) {
            printStep(root, 0, sb);
        }
        return sb.toString();
    }

    // Recursive helper to print the tree of steps with indentation.
    private void printStep(WorkflowStep step, int indent, StringBuilder sb) {
        for (int i = 0; i < indent; i++) {
            sb.append("    ");
        }
        sb.append("- ").append(step.getServiceName())
                .append("::").append(step.getOperationName())
                .append(" (trace ").append(step.getTraceId())
                .append(", span ").append(step.getSpanId()).append(")");
        sb.append("\n");
        for (WorkflowStep child : step.getChildren()) {
            printStep(child, indent + 1, sb);
        }
    }
}
