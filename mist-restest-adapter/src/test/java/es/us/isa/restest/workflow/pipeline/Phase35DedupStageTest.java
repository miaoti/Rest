package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowStep;
import es.us.isa.restest.workflow.pipeline.stages.Phase35DedupStage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Exercises both branches of the Phase 3.5 stage now that the body has been
 * lifted into the stage's helper.
 *
 * <ul>
 *   <li>shattering enabled → runs the dedup pass; duplicate 1-root entries
 *       collapse against the running approvedApiKeys set.</li>
 *   <li>shattering disabled → no-op; scenarios remain untouched.</li>
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
    public void runsDedupPassWhenShatteringEnabled() {
        System.setProperty("scenario.shattering.enabled", "true");
        MstConfig cfg = MstConfig.fromSystemProperties();

        // Phase 3 just shattered a multi-root and produced two NEW 1-root
        // scenarios for GET /stations — they didn't carry the approval tag.
        // Phase 3.5 must collapse them against the existing approval set.
        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(singleRoot("GET /stations", "station-service"));
        scenarios.add(singleRoot("GET /stations", "station-service"));

        Set<String> approvedKeys = new LinkedHashSet<>();
        approvedKeys.add("GET__stations");  // pretend Phase 2.5 approved this
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, cfg);

        Phase35DedupStage stage = new Phase35DedupStage();
        assertEquals("Phase 3.5: Post-Shatter Single-Root Dedup", stage.name());

        stage.run(ctx);

        // Both untagged duplicates of an already-approved key drop out.
        assertEquals("Both untagged duplicates of already-approved GET__stations drop",
                0, scenarios.size());
        assertTrue("approvedKeys set retains GET__stations",
                approvedKeys.contains("GET__stations"));
    }

    @Test
    public void skipsWhenShatteringDisabled() {
        System.setProperty("scenario.shattering.enabled", "false");
        MstConfig cfg = MstConfig.fromSystemProperties();

        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(singleRoot("GET /stations", "station-service"));
        scenarios.add(singleRoot("GET /stations", "station-service"));
        Set<String> approvedKeys = new LinkedHashSet<>();
        PipelineContext ctx = new PipelineContext(
                scenarios, null, null, null, approvedKeys, cfg);

        new Phase35DedupStage().run(ctx);

        // Stage must not mutate state when shattering is off: the dedup pass
        // would re-process scenarios that are byte-identical to Phase 2.5's
        // output, wasting work and double-logging.
        assertEquals("Scenario list untouched when shattering disabled",
                2, scenarios.size());
        assertEquals("approvedKeys untouched when shattering disabled",
                0, approvedKeys.size());
    }
}
