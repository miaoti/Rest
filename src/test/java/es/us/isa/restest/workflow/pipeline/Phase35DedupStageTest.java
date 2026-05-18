package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.stages.Phase35DedupStage;

import org.junit.After;
import org.junit.Before;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Verifies both branches of the Phase 3.5 stage:
 * <ul>
 *   <li>shattering enabled → delegates to {@code runSingleRootDedupPass} with
 *       the post-shatter label and the context's scenario/approval refs;</li>
 *   <li>shattering disabled → no delegation, no mutation, no throw.</li>
 * </ul>
 */
public class Phase35DedupStageTest {

    private String savedShatterEnabled;

    @Before
    public void saveProps() {
        savedShatterEnabled = System.getProperty("scenario.shattering.enabled");
    }

    @After
    public void restoreProps() {
        if (savedShatterEnabled == null) {
            System.clearProperty("scenario.shattering.enabled");
        } else {
            System.setProperty("scenario.shattering.enabled", savedShatterEnabled);
        }
    }

    @Test
    public void delegatesToRunSingleRootDedupPassWhenShatteringEnabled() {
        System.setProperty("scenario.shattering.enabled", "true");
        MstConfig cfg = MstConfig.fromSystemProperties();

        MultiServiceTestCaseGenerator gen = Mockito.mock(MultiServiceTestCaseGenerator.class);
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, cfg);

        Phase35DedupStage stage = new Phase35DedupStage(gen);
        assertEquals("Phase 3.5: Post-Shatter Single-Root Dedup", stage.name());

        stage.run(ctx);

        verify(gen).runSingleRootDedupPass(
                eq("PHASE 3.5: POST-SHATTER SINGLE-ROOT DEDUPLICATION"),
                same(scenarios),
                same(approvedKeys));
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void skipsWhenShatteringDisabled() {
        System.setProperty("scenario.shattering.enabled", "false");
        MstConfig cfg = MstConfig.fromSystemProperties();

        MultiServiceTestCaseGenerator gen = Mockito.mock(MultiServiceTestCaseGenerator.class);
        List<WorkflowScenario> scenarios = new ArrayList<>();
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, cfg);

        new Phase35DedupStage(gen).run(ctx);

        // Stage must not invoke the generator at all when shattering is off:
        // the dedup pass would re-process scenarios that are byte-identical to
        // Phase 2.5's output, wasting work and double-logging.
        verifyNoInteractions(gen);
    }
}
