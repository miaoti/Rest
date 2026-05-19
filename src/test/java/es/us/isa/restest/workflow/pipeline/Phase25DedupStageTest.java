package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowStep;
import es.us.isa.restest.workflow.pipeline.stages.Phase25DedupStage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Exercises the Phase 2.5 stage end-to-end now that the dedup body lives in
 * the stage's helper (no generator reference required). Two identical
 * 1-root scenarios collapse to one; multi-root scenarios always survive
 * with the {@code approvedInDedupPass} tag set so Phase 3.5 lets them
 * through unchanged.
 */
public class Phase25DedupStageTest {

    private static WorkflowScenario singleRoot(String operationName, String serviceName) {
        WorkflowStep root = new WorkflowStep(
                "trace-" + System.nanoTime(),
                "span-" + System.nanoTime(),
                serviceName, operationName,
                0L, 0L,
                Collections.emptyMap(),
                Collections.emptyMap());
        WorkflowScenario sc = new WorkflowScenario();
        sc.addRootStep(root);
        return sc;
    }

    @Test
    public void stageHasStableNameAndDelegatesDedupLogic() {
        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(singleRoot("GET /stations", "station-service"));
        scenarios.add(singleRoot("GET /stations", "station-service"));
        scenarios.add(singleRoot("POST /trip", "trip-service"));

        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        Phase25DedupStage stage = new Phase25DedupStage();
        // Stage name must be stable — used for logging in WorkflowPipeline.
        assertEquals("Phase 2.5: Single-Root Dedup", stage.name());

        stage.run(ctx);

        // Duplicate GET /stations collapses to one; POST /trip survives.
        assertEquals("Three 1-root scenarios with two distinct keys collapse to two",
                2, scenarios.size());
        assertTrue("GET__stations approved", approvedKeys.contains("GET__stations"));
        assertTrue("POST__trip approved", approvedKeys.contains("POST__trip"));
        // Every survivor is tagged for downstream short-circuiting.
        for (WorkflowScenario sc : scenarios) {
            assertTrue("Survivors must be tagged approvedInDedupPass",
                    sc.isApprovedInDedupPass());
        }
    }

    @Test
    public void emptyScenariosListShortCircuitsWithoutThrow() {
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        // Empty input is a legal pipeline state (e.g. when no scenarios were
        // extracted from a trace); the stage must no-op rather than crash.
        new Phase25DedupStage().run(ctx);

        assertEquals(0, scenarios.size());
        assertEquals(0, approvedKeys.size());
    }
}
