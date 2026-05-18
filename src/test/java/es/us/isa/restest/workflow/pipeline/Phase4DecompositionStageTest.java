package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.stages.Phase4DecompositionStage;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Phase 4 stage is a one-line delegator: invoke
 * {@code decomposeMultiRootScenarios()} on the generator. The generator reads
 * its own {@code scenarios}/{@code approvedApiKeys} fields directly (verbatim
 * lift from the inline call site), so the stage carries no parameters.
 */
public class Phase4DecompositionStageTest {

    @Test
    public void delegatesToDecomposeMultiRootScenarios() {
        MultiServiceTestCaseGenerator gen = Mockito.mock(MultiServiceTestCaseGenerator.class);
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        Phase4DecompositionStage stage = new Phase4DecompositionStage(gen);
        assertEquals("Phase 4: Trace Decomposition", stage.name());

        stage.run(ctx);

        verify(gen).decomposeMultiRootScenarios();
        verifyNoMoreInteractions(gen);
    }
}
