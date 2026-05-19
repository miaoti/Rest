package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.inputs.InvalidInputPool;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.stages.SharedPoolGenerationStage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Sanity checks for the SharedPoolGenerationStage after the lift.
 *
 * <p>The heavy LLM / smart-fetch path is covered by the path/header/cookie
 * fault-pool tests that drive the helper class directly; here we focus on
 * the stage's orchestration surface:
 * <ul>
 *   <li>stable name (used by the pipeline logger)</li>
 *   <li>safe no-op when the pool maps are missing from the context (a
 *       dedup-only test would not wire them up)</li>
 *   <li>empty pipeline produces no entries in either pool map</li>
 * </ul>
 */
public class SharedPoolGenerationStageTest {

    @Test
    public void stageHasStableName() {
        SharedPoolGenerationStage stage = new SharedPoolGenerationStage();
        assertEquals("Pre-processing: Shared Pool Generation", stage.name());
    }

    @Test
    public void skipsWhenPoolMapsAreMissing() {
        // Constructor without pool maps mirrors a context built for the
        // dedup-only stages. Running shared-pool generation against it must
        // be a no-op rather than NPE.
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        new SharedPoolGenerationStage().run(ctx);  // must not throw
    }

    @Test
    public void emptyScenariosLeavesPoolMapsEmpty() {
        // Wiring the pool maps but feeding no scenarios proves the stage
        // walks the (empty) grouping without producing spurious entries.
        Map<String, Map<String, List<String>>> sharedPools = new HashMap<>();
        Map<String, Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool>> faultyPools = new HashMap<>();
        PipelineContext ctx = new PipelineContext(
                new ArrayList<>(), null, new HashMap<>(), null, new LinkedHashSet<>(),
                MstConfig.instance(),
                null, null, null, false,
                sharedPools, faultyPools);

        new SharedPoolGenerationStage().run(ctx);

        assertNotNull(sharedPools);
        assertNotNull(faultyPools);
        assertEquals("No scenarios -> no shared pool entries", 0, sharedPools.size());
        assertEquals("No scenarios -> no fault pool entries", 0, faultyPools.size());
    }
}
