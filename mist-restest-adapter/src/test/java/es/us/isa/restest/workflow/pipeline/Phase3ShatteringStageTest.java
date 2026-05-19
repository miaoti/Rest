package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.stages.Phase3ShatteringStage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Phase 3 shattering stage must respect the {@code mst.scenarioShattering.enabled}
 * gate. When disabled, the stage is a no-op — running it must NOT mutate the
 * scenario list and must NOT throw, even when {@code dependencyRegistry} is
 * {@code null} (the code path that constructs {@code ScenarioOptimizer} would
 * NPE on a null registry, so we use a null context dependency to prove the
 * guard is hit and the optimizer is skipped).
 */
public class Phase3ShatteringStageTest {

    /** Snapshot of the relevant property so we can restore it after each test. */
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
    public void skipsWhenShatteringDisabled() {
        // Force a config that reports shattering=false.
        System.setProperty("scenario.shattering.enabled", "false");
        MstConfig cfg = MstConfig.fromSystemProperties();
        org.junit.Assert.assertFalse("precondition: shattering must be disabled in this test",
                cfg.scenarioShattering().enabled());

        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(new WorkflowScenario());  // identity-tracked sentinel
        Set<String> approvedKeys = new LinkedHashSet<>();
        // dependencyRegistry==null is intentional: if the stage tried to call
        // `new ScenarioOptimizer(null).optimizeScenarios(...)` it would NPE
        // somewhere downstream. A successful no-throw is the test signal.
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, /*dependencyRegistry*/ null, approvedKeys, cfg);

        Phase3ShatteringStage stage = new Phase3ShatteringStage();
        assertEquals("Phase 3: Scenario Shattering", stage.name());

        stage.run(ctx);  // must not throw

        assertEquals("Scenario list must be untouched when shattering disabled",
                1, scenarios.size());
        assertEquals("approvedApiKeys must be untouched when shattering disabled",
                0, approvedKeys.size());
    }
}
