package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.stages.SharedPoolGenerationStage;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Asserts the pre-processing stage runs {@code groupScenariosByRootApi}
 * THEN passes its result to {@code generateSharedParameterPools} — order
 * is significant because the pool generator consumes the grouped map.
 */
public class SharedPoolGenerationStageTest {

    @Test
    public void groupsThenGeneratesPools() {
        MultiServiceTestCaseGenerator gen = Mockito.mock(MultiServiceTestCaseGenerator.class);

        // Hand-rolled grouping result the mock should return — verifies the
        // exact instance flows from grouping into pool generation (no copies).
        Map<String, List<WorkflowScenario>> grouped = new LinkedHashMap<>();
        grouped.put("GET__stations", new ArrayList<>());
        when(gen.groupScenariosByRootApi()).thenReturn(grouped);

        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        SharedPoolGenerationStage stage = new SharedPoolGenerationStage(gen);
        assertEquals("Pre-processing: Shared Pool Generation", stage.name());

        stage.run(ctx);

        InOrder order = Mockito.inOrder(gen);
        order.verify(gen).groupScenariosByRootApi();
        order.verify(gen).generateSharedParameterPools(same(grouped));
    }
}
