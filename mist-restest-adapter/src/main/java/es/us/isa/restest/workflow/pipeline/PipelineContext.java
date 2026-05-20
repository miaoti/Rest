package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.generators.AiDrivenLLMGenerator;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.inputs.InvalidInputPool;
import io.mist.core.smart.SmartInputFetchConfig;
import es.us.isa.restest.inputs.smart.SmartInputFetcher;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.workflow.SemanticDependencyRegistry;
import es.us.isa.restest.workflow.WorkflowScenario;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mutable state container threaded through the {@link WorkflowPipeline} stages.
 *
 * <p>Field types mirror those used by {@code MultiServiceTestCaseGenerator}:
 * {@code serviceConfigs} is a {@code Map<String, TestConfigurationObject>} (not the
 * speculative {@code MultiServiceConfig} name used in the design brief), and
 * {@code scenarios} is the same {@link List} instance the generator mutates in place
 * (Phase 2.5/3.5 remove duplicates from it; Phase 4 appends decomposed baselines).
 *
 * <p>The shared-pool maps ({@code sharedParameterPools}, {@code faultyParameterPools})
 * are the SAME {@link Map} instances the generator's variant loop reads after the
 * pipeline completes — Phase 2 writes through the context to those maps so the
 * downstream loop sees populated pools without a separate hand-off step.
 *
 * <p>This class is intentionally a struct-style holder with package-public fields:
 * the pipeline stages are themselves owned by this package and need read/write
 * access without ceremony. Down-stream extraction (e.g. an additional stage that
 * threads new state) only requires adding a field here.
 */
public final class PipelineContext {
    /** Mutated in place: dedup stages remove elements; decomposition appends. */
    public final List<WorkflowScenario> scenarios;
    public final Map<String, OpenAPISpecification> serviceSpecs;
    public final Map<String, TestConfigurationObject> serviceConfigs;
    public final SemanticDependencyRegistry dependencyRegistry;
    /** Shared dedup set carried across Phase 2.5, 3.5, and 4. */
    public final Set<String> approvedApiKeys;
    public final MstConfig config;

    /* ── Shared-pool generation inputs ─────────────────────────────── */
    /** LLM facade used to populate parameter / fault pools. */
    public final AiDrivenLLMGenerator llmGen;
    /** Optional smart-input fetcher (may be {@code null} when disabled). */
    public final SmartInputFetcher smartFetcher;
    /** Smart-fetch configuration (may be {@code null} when disabled). */
    public final SmartInputFetchConfig smartFetchConfig;
    /** Toggle: gate the LLM/fault pool generation pathways. */
    public final boolean useLLM;

    /* ── Shared-pool generation outputs ────────────────────────────── */
    /** Output map: rootApiKey → (paramName → list of pre-generated values). */
    public final Map<String, Map<String, List<String>>> sharedParameterPools;
    /** Output map: rootApiKey → (paramName → InvalidInputPool of fault values). */
    public final Map<String, Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool>> faultyParameterPools;

    public PipelineContext(List<WorkflowScenario> scenarios,
                           Map<String, OpenAPISpecification> serviceSpecs,
                           Map<String, TestConfigurationObject> serviceConfigs,
                           SemanticDependencyRegistry dependencyRegistry,
                           Set<String> approvedApiKeys,
                           MstConfig config) {
        this(scenarios, serviceSpecs, serviceConfigs, dependencyRegistry,
                approvedApiKeys, config,
                /*llmGen*/ null, /*smartFetcher*/ null, /*smartFetchConfig*/ null,
                /*useLLM*/ false,
                /*sharedParameterPools*/ null, /*faultyParameterPools*/ null);
    }

    public PipelineContext(List<WorkflowScenario> scenarios,
                           Map<String, OpenAPISpecification> serviceSpecs,
                           Map<String, TestConfigurationObject> serviceConfigs,
                           SemanticDependencyRegistry dependencyRegistry,
                           Set<String> approvedApiKeys,
                           MstConfig config,
                           AiDrivenLLMGenerator llmGen,
                           SmartInputFetcher smartFetcher,
                           SmartInputFetchConfig smartFetchConfig,
                           boolean useLLM,
                           Map<String, Map<String, List<String>>> sharedParameterPools,
                           Map<String, Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool>> faultyParameterPools) {
        this.scenarios = scenarios;
        this.serviceSpecs = serviceSpecs;
        this.serviceConfigs = serviceConfigs;
        this.dependencyRegistry = dependencyRegistry;
        this.approvedApiKeys = approvedApiKeys;
        this.config = config;
        this.llmGen = llmGen;
        this.smartFetcher = smartFetcher;
        this.smartFetchConfig = smartFetchConfig;
        this.useLLM = useLLM;
        this.sharedParameterPools = sharedParameterPools;
        this.faultyParameterPools = faultyParameterPools;
    }
}
