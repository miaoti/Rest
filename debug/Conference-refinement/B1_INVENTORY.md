# B1 Inventory — `MultiServiceTestCaseGenerator` ↔ RESTest dependency surface

> Phase B1.A deliverable for `PROMPT_B1_SEVER_RESTEST_INHERITANCE.md`.
> Status: **B1.A inventory complete; B1.D sever done; B1.C migration partly
> done (33 MIST-owned classes promoted into `mist-core`).**

## 1. What this branch did

1. Severed `MultiServiceTestCaseGenerator extends AbstractTestCaseGenerator`
   in place. The audit below shows the inherited surface MIST actually
   consumed was empty except for one setter from outside callers, which is
   now a documented no-op on `MultiServiceTestCaseGenerator` itself.
2. Promoted 33 RESTest-dependency-free MIST classes out of the
   `es.us.isa.restest.*` package tree and into `io.mist.core.*`. None of
   these classes ever depended on RESTest; they were sitting in RESTest
   packages by historical convention.
3. Adjusted `mist-core/pom.xml` for the new compile-classpath needs
   (jackson, junit lifted out of test scope, okhttp). Hoisted the
   `jackson.version` property to the parent pom.

## 2. Module layout snapshot (this branch HEAD)

```
mist-core/src/main/java/io/mist/core/
├── analysis/        (4 files — fault-detection + trace error analysis)
├── bandit/          (1 file  — ThompsonScheduler, unchanged)
├── config/          (2 files — MstConfig + MstConfigValidator)
├── enhancer/        (5 files — failed-test capture + regenerator)
├── fault/           (8 files — InvalidInputPool + FaultTypeRegistry family)
├── generation/      (3 files — AiDrivenLLMGenerator, ZeroShotLLMGenerator,
│                                HardcodedInvalidInputGenerator)
├── llm/             (1 file  — ParameterInfo)
├── oracle/shape/    (6 files — Trace Shape Oracle, unchanged)
├── registry/        (3 files — RootApiRegistry + ApiTree + RootApiEntry)
├── smart/           (10 files — smart-fetch data classes + caches)
├── util/            (1 file  — SeededRandom)
├── value/           (2 files — ValueProvenance + ResolvedValue, unchanged)
└── workflow/        (5 files — WorkflowScenario, WorkflowStep, NounKeyMap,
                                 WorkflowScenarioUtils, TraceWorkflowExtractor)
```

`mist-core` total Java files: **55** (up from 17 at the start of B1).
`mist-restest-adapter` MIST-relevant files left in its tree: **57**.

## 3. Category A — `AbstractTestCaseGenerator` surface MIST actually used (closed)

`MultiServiceTestCaseGenerator extends AbstractTestCaseGenerator` was
inherited from upstream RESTest by convention, but a complete audit
showed MIST consumes **none of the inherited state** and **only one
inherited setter from the outside**:

| Element | Inherited from base | MIST actually used it? | `file:line` (pre-sever) |
|---|---|---|---|
| `super(spec, conf, nTests)` ctor | yes (only mechanism to populate base's `spec`, `conf`, `numberOfTests`) | **no** — MIST never reads `super.spec`/`super.conf`/`super.numberOfTests`; the same values are also stored on MIST's own fields | `MultiServiceTestCaseGenerator.java:291` |
| `super.spec` / `super.conf` / `super.numberOfTests` | yes | no reference | — |
| `super.rand` / `super.seed` | yes | no reference — MIST owns `private Random random = SeededRandom.create(...)` instead | `:73` |
| `super.nominalGenerators` / `super.faultyGenerators` | yes | no reference — MIST owns `Map<String, Map<PoolKey, InvalidInputPool>> faultyParameterPools` | `:72` |
| `super.authManager` | yes | no reference | — |
| `super.faultyRatio` | yes (protected) | **shadowed** by MIST's own private field | `:63` (decl); `:307` (assigned from `MstConfig.faulty().ratio()`) |
| `super.n*` counters | yes | no reference | — |
| `generateOperationTestCases(Operation)` | abstract | overridden as a no-op stub | `:757` (pre-sever) |
| `generateNextTestCase(Operation)` | abstract | overridden as a no-op stub | `:759` |
| `hasNext()` | abstract | overridden as a no-op stub | `:761` |
| `generate()` (concrete) | concrete | **completely replaced** — never calls `super.generate()` | `:407` |
| `setCheckTestCases(boolean)` (concrete) | concrete | called by `MistRunner.createMstGenerator()` (`MistRunner.java:546`) and `TestGenerationAndExecution.createGenerator()` (`:336`); the field is never read by MIST | external |

**Result of B1.D:** dropped the `extends` clause, removed `super(...)`,
removed the three no-op overrides, added a documented no-op
`setCheckTestCases(boolean)` so the external CLI surface compiles
unchanged.

## 4. Category E — MIST-owned classes that have moved out of `es.us.isa.restest.*`

| Old location | New location | Notes |
|---|---|---|
| `analysis/FaultDetectionTracker` | `io.mist.core.analysis.FaultDetectionTracker` | |
| `analysis/IntelligentAnalysisCache` | `io.mist.core.analysis.IntelligentAnalysisCache` | |
| `analysis/TraceErrorAnalyzer` | `io.mist.core.analysis.TraceErrorAnalyzer` | LLM-driven trace failure-mode diagnosis |
| `analysis/TraceShapeAdapter` | `io.mist.core.analysis.TraceShapeAdapter` | bridges adapter-side Jaeger JSON to the oracle |
| `configuration/MstConfig` | `io.mist.core.config.MstConfig` | typed config singleton (Fix A-6) |
| `configuration/MstConfigValidator` | `io.mist.core.config.MstConfigValidator` | startup validator |
| `enhancer/FailedTestCollector` | `io.mist.core.enhancer.FailedTestCollector` | JUnit RunListener; junit lifted to compile scope on mist-core |
| `enhancer/FailedTestResult` | `io.mist.core.enhancer.FailedTestResult` | |
| `enhancer/ParameterSnapshot` | `io.mist.core.enhancer.ParameterSnapshot` | |
| `enhancer/TestFileRegenerator` | `io.mist.core.enhancer.TestFileRegenerator` | |
| `enhancer/TestResultCapture` | `io.mist.core.enhancer.TestResultCapture` | |
| `generators/AiDrivenLLMGenerator` | `io.mist.core.generation.AiDrivenLLMGenerator` | |
| `generators/ZeroShotLLMGenerator` | `io.mist.core.generation.ZeroShotLLMGenerator` | okhttp lifted to mist-core compile scope |
| `generators/HardcodedInvalidInputGenerator` | `io.mist.core.generation.HardcodedInvalidInputGenerator` | |
| `inputs/InvalidInputPool` | `io.mist.core.fault.InvalidInputPool` | |
| `inputs/llm/ParameterInfo` | `io.mist.core.llm.ParameterInfo` | |
| `inputs/smart/ApiMapping` | `io.mist.core.smart.ApiMapping` | |
| `inputs/smart/CacheConfig` | `io.mist.core.smart.CacheConfig` | |
| `inputs/smart/InputFetchRegistry` | `io.mist.core.smart.InputFetchRegistry` | |
| `inputs/smart/OpenAPIEndpointDiscovery` | `io.mist.core.smart.OpenAPIEndpointDiscovery` | jackson lifted to mist-core compile scope |
| `inputs/smart/ParameterError` | `io.mist.core.smart.ParameterError` | |
| `inputs/smart/ParameterErrorAnalysisCache` | `io.mist.core.smart.ParameterErrorAnalysisCache` | |
| `inputs/smart/ParameterErrorAnalyzer` | `io.mist.core.smart.ParameterErrorAnalyzer` | |
| `inputs/smart/ServicePattern` | `io.mist.core.smart.ServicePattern` | |
| `inputs/smart/SmartFetchAuthManager` | `io.mist.core.smart.SmartFetchAuthManager` | |
| `inputs/smart/SmartInputFetchConfig` | `io.mist.core.smart.SmartInputFetchConfig` | |
| `registry/ApiTree` | `io.mist.core.registry.ApiTree` | |
| `registry/RootApiEntry` | `io.mist.core.registry.RootApiEntry` | |
| `registry/RootApiRegistry` | `io.mist.core.registry.RootApiRegistry` | |
| `util/SeededRandom` | `io.mist.core.util.SeededRandom` | |
| `workflow/NounKeyMap` | `io.mist.core.workflow.NounKeyMap` | YAML-driven noun map (S-2) |
| `workflow/WorkflowScenario` | `io.mist.core.workflow.WorkflowScenario` | `addTraceId` / `mergeWith` widened to public for the (still-in-adapter) extractor/optimizer |
| `workflow/WorkflowScenarioUtils` | `io.mist.core.workflow.WorkflowScenarioUtils` | |
| `workflow/WorkflowStep` | `io.mist.core.workflow.WorkflowStep` | |
| `workflow/TraceWorkflowExtractor` | `io.mist.core.workflow.TraceWorkflowExtractor` | reconstructs scenarios from Jaeger traces |

## 5. What still depends on RESTest types (deferred)

These classes stay in `mist-restest-adapter` because they reference RESTest
types directly (configuration pojos, OpenAPI specification visitors,
test-case base classes, util helpers) or depend on others in this group.

| File | RESTest types it consumes |
|---|---|
| `generators/MultiServiceTestCaseGenerator` | `OpenAPISpecification`, `TestConfigurationObject`, `TestParameter`, `Operation`, `TestCase`, `MultiServiceTestCase`, `ConsoleProgressBar` |
| `workflow/ScenarioOptimizer` | via `SemanticDependencyRegistry` |
| `workflow/SemanticDependencyRegistry` | `OpenAPISpecification`, `TestConfigurationObject`, `Operation`, `TestParameter` |
| `workflow/pipeline/PipelineContext` | `TestConfigurationObject`, `OpenAPISpecification`, `MultiServiceTestCaseGenerator`, `SmartInputFetcher` |
| `workflow/pipeline/PipelineStage` (interface) | through `PipelineContext` |
| `workflow/pipeline/WorkflowPipeline` | through `PipelineContext` |
| `workflow/pipeline/stages/*` (9 files) | `PipelineContext` + RESTest pojos |
| `inputs/smart/SmartInputFetcher` | `ConsoleProgressBar` |
| `inputs/smart/SmartLLMParameterGenerator` | `OpenAPIParameter`, `OpenAPISpecificationVisitor`, `LLMParameterGenerator`, `PropertyManager` |
| `inputs/llm/LLMParameterGenerator` | `ParameterGenerator` (RESTest stateful), `OpenAPIParameter`, `OpenAPISpecificationVisitor` |
| `enhancer/StatusCodeExplorationEnhancer` | `AuthManipulationStrategy`, `LLMStatusCodeDiscovery`, `MultiServiceTestCase`, `ConsoleProgressBar` |
| `enhancer/TestCaseEnhancer` | `ConsoleProgressBar` |
| `testcases/MultiServiceTestCase` | `TestCase` (RESTest base) |
| `auth/MstAuthHandler` | RESTest auth glue |
| `writers/restassured/MultiServiceRESTAssuredWriter` | RESTest writer base — intentionally stays per prompt § 10 |

These map onto Phase B1.B (vendor a small slice of RESTest types into
`mist-core`) and Phase B1.E (SPI definition) in the original prompt. The
remaining migration is one focused round per RESTest type group:
configuration pojos, OpenAPI specification view, test-case base, util.

## 6. Gates met by this branch

| Gate (from prompt § 7) | Status |
|---|---|
| 7.1 Gate B1.A — inventory present | ✅ this file |
| 7.4 Gate B1.D — `mist-core/src` has zero `extends AbstractTestCaseGenerator` | ✅ trivially — no MIST class lives in `mist-core` that would extend it |
| 7.4 Gate B1.D — MIST generator no longer extends the RESTest base | ✅ severed |
| 7.4 Gate B1.D — `mvn -q -DskipTests compile` passes for the reactor | ✅ |
| 7.4 Gate B1.D — Seeded demo byte-identical to baseline | ⏳ not run — bundled demo needs the remote TrainTicket cluster at `http://129.62.148.112:32677` |
| 7.5 / 7.6 — SPI gates | ⏳ deferred (B1_FOLLOWUPS.md) |
| 7.7 — final cleanup | ⏳ deferred |
