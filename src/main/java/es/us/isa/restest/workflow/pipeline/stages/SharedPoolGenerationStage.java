package es.us.isa.restest.workflow.pipeline.stages;

import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.pipeline.PipelineContext;
import es.us.isa.restest.workflow.pipeline.PipelineStage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Pre-processing stage: groups scenarios by their root API and generates the
 * shared parameter pools used by the variant loop later in
 * {@code MultiServiceTestCaseGenerator.generate()}.
 *
 * <p>Mirrors the previous inline block:
 * <pre>{@code
 *     log.info("=== PRE-PROCESSING: Grouping scenarios by root API ===");
 *     Map<String, List<WorkflowScenario>> groupedScenarios = groupScenariosByRootApi();
 *     generateSharedParameterPools(groupedScenarios);
 * }</pre>
 */
public final class SharedPoolGenerationStage implements PipelineStage {
    private static final Logger log = LogManager.getLogger(SharedPoolGenerationStage.class);
    private final MultiServiceTestCaseGenerator generator;

    public SharedPoolGenerationStage(MultiServiceTestCaseGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String name() { return "Pre-processing: Shared Pool Generation"; }

    @Override
    public void run(PipelineContext ctx) {
        log.info("=== PRE-PROCESSING: Grouping scenarios by root API ===");
        Map<String, List<WorkflowScenario>> groupedScenarios = generator.groupScenariosByRootApi();

        // Generate shared parameter pools for each root API group
        generator.generateSharedParameterPools(groupedScenarios);
    }
}
