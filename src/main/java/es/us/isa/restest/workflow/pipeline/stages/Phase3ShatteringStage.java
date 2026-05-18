package es.us.isa.restest.workflow.pipeline.stages;

import es.us.isa.restest.workflow.ScenarioOptimizer;
import es.us.isa.restest.workflow.pipeline.PipelineContext;
import es.us.isa.restest.workflow.pipeline.PipelineStage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Phase 3: Scenario Shattering — partition fat multi-root scenarios into
 * semantically cohesive components using the dependency graph.
 *
 * <p>Gated by {@code mst.scenarioShattering.enabled}.  When disabled the stage
 * is a no-op so the rest of the pipeline still runs (mirrors the previous
 * inline {@code if (shatterEnabled) { ... }} guard in {@code generate()}).
 *
 * <p>Phase 3.5 (post-shatter single-root dedup) is intentionally kept in its
 * own {@link Phase35DedupStage} so the on/off gating cleanly skips both.
 */
public final class Phase3ShatteringStage implements PipelineStage {
    private static final Logger log = LogManager.getLogger(Phase3ShatteringStage.class);

    @Override
    public String name() { return "Phase 3: Scenario Shattering"; }

    @Override
    public void run(PipelineContext ctx) {
        if (!ctx.config.scenarioShattering().enabled()) {
            log.info("[Phase3ShatteringStage] disabled by config — skipping");
            return;
        }
        new ScenarioOptimizer(ctx.dependencyRegistry).optimizeScenarios(ctx.scenarios);
    }
}
