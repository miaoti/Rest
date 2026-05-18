package es.us.isa.restest.workflow.pipeline.stages;

import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.pipeline.PipelineContext;
import es.us.isa.restest.workflow.pipeline.PipelineStage;

/**
 * Phase 2.5: collapse duplicate 1-root scenarios before any downstream
 * processing.  Thin wrapper that delegates to
 * {@link MultiServiceTestCaseGenerator#runSingleRootDedupPass(String, java.util.List, java.util.Set)}
 * to preserve byte-identical semantics with the inline call previously made
 * from {@code generate()} at the top of Phase 2.5.
 */
public final class Phase25DedupStage implements PipelineStage {
    private final MultiServiceTestCaseGenerator generator;

    public Phase25DedupStage(MultiServiceTestCaseGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String name() { return "Phase 2.5: Single-Root Dedup"; }

    @Override
    public void run(PipelineContext ctx) {
        generator.runSingleRootDedupPass(
                "PHASE 2.5: SINGLE-ROOT SCENARIO DEDUPLICATION",
                ctx.scenarios,
                ctx.approvedApiKeys);
    }
}
