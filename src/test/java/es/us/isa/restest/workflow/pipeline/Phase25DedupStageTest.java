package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.stages.Phase25DedupStage;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Locks in the contract that {@link Phase25DedupStage#run} delegates to
 * {@code MultiServiceTestCaseGenerator.runSingleRootDedupPass} with the exact
 * label and the {@link PipelineContext}'s {@code scenarios} and
 * {@code approvedApiKeys} references — same-arity, same-identity arguments
 * the byte-for-byte lift requires.
 */
public class Phase25DedupStageTest {

    @Test
    public void delegatesToRunSingleRootDedupPassWithExactLabel() {
        MultiServiceTestCaseGenerator gen = Mockito.mock(MultiServiceTestCaseGenerator.class);
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, MstConfig.instance());

        Phase25DedupStage stage = new Phase25DedupStage(gen);

        // Stage name must be stable — used for logging in WorkflowPipeline.
        assertEquals("Phase 2.5: Single-Root Dedup", stage.name());

        stage.run(ctx);

        // Same-identity for scenarios/approvedKeys so the generator's in-place
        // mutations are visible to the next stage via the same context.
        verify(gen).runSingleRootDedupPass(
                eq("PHASE 2.5: SINGLE-ROOT SCENARIO DEDUPLICATION"),
                same(scenarios),
                same(approvedKeys));
        verifyNoMoreInteractions(gen);
    }
}
