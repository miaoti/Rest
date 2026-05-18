package es.us.isa.restest.workflow.pipeline.stages;

import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.pipeline.PipelineContext;
import es.us.isa.restest.workflow.pipeline.PipelineStage;

/**
 * Phase 4: Trace Decomposition — extract individual 1-Root baseline scenarios
 * from multi-root workflows to guarantee per-API coverage.
 *
 * <p>Delegates to
 * {@link MultiServiceTestCaseGenerator#decomposeMultiRootScenarios()} which
 * reads the generator's own {@code scenarios} and {@code approvedApiKeys}
 * fields (the {@link PipelineContext} just signals the stage is its turn —
 * the data flow is via the generator instance to keep the lift verbatim).
 */
public final class Phase4DecompositionStage implements PipelineStage {
    private final MultiServiceTestCaseGenerator generator;

    public Phase4DecompositionStage(MultiServiceTestCaseGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String name() { return "Phase 4: Trace Decomposition"; }

    @Override
    public void run(PipelineContext ctx) {
        generator.decomposeMultiRootScenarios();
    }
}
