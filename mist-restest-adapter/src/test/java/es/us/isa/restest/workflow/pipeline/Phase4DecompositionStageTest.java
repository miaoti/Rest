package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowStep;
import es.us.isa.restest.workflow.pipeline.stages.Phase4DecompositionStage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Phase 4 stage now owns the decomposition loop directly via the
 * package-private helper. The tests below drive the stage with hand-built
 * scenarios and assert on the post-decomposition list.
 */
public class Phase4DecompositionStageTest {

    private static WorkflowStep root(String op, String svc) {
        return new WorkflowStep(
                "trace-" + System.nanoTime(),
                "span-" + System.nanoTime(),
                svc, op,
                0L, 0L,
                Collections.emptyMap(),
                Collections.emptyMap());
    }

    @Test
    public void stageHasStableName() {
        Phase4DecompositionStage stage = new Phase4DecompositionStage();
        assertEquals("Phase 4: Trace Decomposition", stage.name());
    }

    @Test
    public void emptyScenarioListShortCircuits() {
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        new Phase4DecompositionStage().run(ctx);  // must not throw

        assertEquals(0, scenarios.size());
        assertEquals(0, approvedKeys.size());
    }

    @Test
    public void singleRootScenarioIsNotDecomposed() {
        // Single-root scenarios pass through unchanged — there's nothing to
        // pull apart, so the survivor count equals the input count.
        WorkflowScenario sc = new WorkflowScenario();
        sc.addRootStep(root("GET /stations", "station-service"));

        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(sc);
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        new Phase4DecompositionStage().run(ctx);

        assertEquals("1-root scenario survives untouched", 1, scenarios.size());
    }

    @Test
    public void multiRootScenarioYieldsBaselines() {
        // A 2-root scenario produces 2 new 1-root baselines (Flow_Scenario_N_RT1/_RT2)
        // appended to the list; the original multi-root scenario is preserved.
        WorkflowScenario sc = new WorkflowScenario();
        sc.addRootStep(root("POST /trip", "trip-service"));
        sc.addRootStep(root("GET /stations", "station-service"));

        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(sc);
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        new Phase4DecompositionStage().run(ctx);

        assertEquals("Multi-root + 2 baselines = 3 scenarios in total",
                3, scenarios.size());
    }
}
