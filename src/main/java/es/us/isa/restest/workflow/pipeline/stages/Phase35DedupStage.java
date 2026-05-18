package es.us.isa.restest.workflow.pipeline.stages;

import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.pipeline.PipelineContext;
import es.us.isa.restest.workflow.pipeline.PipelineStage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Phase 3.5: Post-shatter single-root deduplication.
 *
 * <p>Shattering can emit NEW 1-root partitions (isolated connected components)
 * that never went through the Phase 2.5 dedup filter.  Re-applies the 1-root
 * dedup against the same {@code approvedApiKeys} set to prevent byte-identical
 * duplicate test classes for the same parameterless endpoint.  Phase 3
 * propagates the {@code approvedInDedupPass} tag to each shattered child so the
 * pass-through guard in {@code runSingleRootDedupPass} keeps Phase-2.5-approved
 * scenarios intact even after they are reconstructed as new instances.
 *
 * <p>This stage is gated by the same {@code mst.scenarioShattering.enabled}
 * flag as {@link Phase3ShatteringStage}.  Running it without shattering would
 * be wasted work (the input is unchanged from Phase 2.5).
 */
public final class Phase35DedupStage implements PipelineStage {
    private static final Logger log = LogManager.getLogger(Phase35DedupStage.class);
    private final MultiServiceTestCaseGenerator generator;

    public Phase35DedupStage(MultiServiceTestCaseGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String name() { return "Phase 3.5: Post-Shatter Single-Root Dedup"; }

    @Override
    public void run(PipelineContext ctx) {
        if (!ctx.config.scenarioShattering().enabled()) {
            log.info("[Phase35DedupStage] shattering disabled — skipping post-shatter dedup");
            return;
        }
        generator.runSingleRootDedupPass(
                "PHASE 3.5: POST-SHATTER SINGLE-ROOT DEDUPLICATION",
                ctx.scenarios,
                ctx.approvedApiKeys);
    }
}
