# B1 follow-ups — what is still ahead of the rebuild

> Tracking file for items deferred out of the current `inject-detection`
> HEAD. See `PROMPT_B1_SEVER_RESTEST_INHERITANCE.md` for the full plan
> and `B1_INVENTORY.md` for the per-class disposition.

## What has landed already (B1.A + B1.D + B1.C-clean + B1.B-leaves)

- B1.A — full dependency inventory in `B1_INVENTORY.md`.
- B1.D — `MultiServiceTestCaseGenerator` no longer
  `extends AbstractTestCaseGenerator`. `MistRunner` uses the concrete
  type. The legacy CLI surface still compiles unchanged.
- B1.C (partial) — 33 RESTest-dependency-free MIST classes have moved
  out of `es.us.isa.restest.*` into `io.mist.core.*`:
  `analysis/*`, `config/MstConfig*`, `enhancer/*` (leaves),
  `fault/InvalidInputPool`, `generation/{Ai,Zero,Hardcoded}*`,
  `llm/ParameterInfo`, `registry/*`, `smart/*` (10 leaves),
  `util/SeededRandom`, `workflow/{WorkflowScenario, WorkflowStep,
  WorkflowScenarioUtils, NounKeyMap, TraceWorkflowExtractor}`.
- B1.C (continued) — two MIST-only util helpers moved out of
  `es.us.isa.restest.util` into `io.mist.core.util`:
  `ConsoleProgressBar`, `IDGenerator`. Adapter call sites now import
  the new locations.
- B1.C (further) — two more MIST-only classes moved:
  `inputs/smart/SmartInputFetcher` (4450 LOC; the largest single
  promotion in B1, unlocked once `ConsoleProgressBar` left the
  adapter) and `enhancer/TestCaseEnhancer` (409 LOC; only its
  package decl was RESTest-tied). Adapter callers' imports updated.
- B1.B (leaves) — seven leaf configuration POJOs vendored into
  `io.mist.core.spec` as verbatim copies: `Auth`, `Generator`,
  `GenParameter`, `Operation`, `TestParameter`, `TestConfiguration`,
  `TestConfigurationObject`. Adapter call sites still use the
  originals; the vendored copies are unreferenced until consumers
  swap one-by-one in a follow-up pass.
- **Cardinal § 7.5 gate met**: `grep -rE 'es\.us\.isa' mist-core/src/main/java`
  returns zero matches. `mvn -pl mist-core dependency:tree` shows zero
  RESTest deps.

## What is still ahead — the remaining B1.B/C/E/F/G work

### Remaining vendoring (B1.B, deferred)

| Class | Size | Blockers | Disposition |
|---|---|---|---|
| `testcases/TestResult` | 110 LOC | `CSVManager`, `FileManager`, `org.apache.commons.text` | Vendor if `exportToCSV` is dropped or its util chain is vendored |
| `testcases/TestCase` | 650 LOC | `OpenAPIParameter`, `idlreasonerchoco.Analyzer`, `IDLException` | Wrap (`MistParameter`, `MistDependencyChecker`) — too large + transitive third-party deps |
| `testcases/MultiServiceTestCase` | 474 LOC | `TestCase` (above), `AuthManipulationStrategy` | Vendor after `TestCase` is wrapped |

### Remaining MIST-only class moves (B1.C, deferred)

These all share the same blocker: they reference one of the
not-yet-vendored / not-yet-wrapped RESTest types above. They cannot
move into `mist-core` until either (a) the type is vendored and the
reference swaps, or (b) the type is wrapped as a `MistXxx` SPI.

| File (currently in adapter) | Target under `mist-core` once unblocked |
|---|---|
| `generators/MultiServiceTestCaseGenerator` | `io.mist.core.generation.MistGenerator` (rename per prompt) |
| `workflow/ScenarioOptimizer` | `io.mist.core.workflow.ScenarioOptimizer` |
| `workflow/SemanticDependencyRegistry` | `io.mist.core.registry.SemanticDependencyRegistry` |
| `workflow/pipeline/PipelineContext` | `io.mist.core.workflow.pipeline.PipelineContext` |
| `workflow/pipeline/PipelineStage` | `io.mist.core.workflow.pipeline.PipelineStage` |
| `workflow/pipeline/WorkflowPipeline` | `io.mist.core.workflow.pipeline.WorkflowPipeline` |
| `workflow/pipeline/stages/*` (9 files) | `io.mist.core.workflow.pipeline.stages.*` |
| `inputs/smart/SmartInputFetcher` | `io.mist.core.smart.SmartInputFetcher` (re-check — `ConsoleProgressBar` block is now gone) |
| `inputs/smart/SmartLLMParameterGenerator` | `io.mist.core.generation.SmartLLMParameterGenerator` |
| `inputs/llm/LLMParameterGenerator` | `io.mist.core.generation.LLMParameterGenerator` |
| `enhancer/StatusCodeExplorationEnhancer` | `io.mist.core.enhancer.StatusCodeExplorationEnhancer` |
| `enhancer/TestCaseEnhancer` | `io.mist.core.enhancer.TestCaseEnhancer` |
| `configuration/multiservice/MstConfig` (legacy loader) | `io.mist.core.config.legacy.MstConfig` |

### SPI scaffolding (B1.E + B1.F, deferred)

The prompt's § Phase B1.E enumerates a "minimum viable SPI surface"
(`MistSpecLoader`, `MistSpec`, `MistOperation`, `MistParameter`,
`MistTestWriter`, `MistTestExecutor`). These interfaces are NOT yet
defined in this branch because:

- Prompt § 6 #5 forbids inventing SPIs for hypothetical needs.
- `mist-core` currently has zero consumers that would call these
  interfaces — the generation pipeline that would consume them still
  lives in the adapter.
- The SPI shape will become obvious once the pipeline starts moving
  (Phase B1.C continuation): each `OpenAPISpecification x = …` call
  site that has to cross the package boundary into `mist-core` will
  reveal exactly which method signature `MistSpec` must expose.

When the SPI surface is defined, the matching `META-INF/services/`
registration goes in `mist-restest-adapter/src/main/resources/`.

## Other deferred items

- **Brand sweep** (`Mst` → `Mist` on moved classes, system-property keys
  `mst.*` stay). Prompt § Phase B1.C step 3 allows this once a class
  moves packages. Deferring keeps in-flight diffs reviewable.
- **Byte-identical demo proof** (`-Drandom.seed=42` against
  `trainticket-demo.properties`). Needs the remote TrainTicket cluster
  reachable from the build host, or a recorded fixture. Cannot be
  verified in CI alone.
- **Positioning doc citations** (`docs/mst-plans/PATH_B_POSITIONING.md`):
  every `file:line` citation that pointed to a now-moved class will
  resolve incorrectly. Refresh as a single editorial pass after all
  moves land.
- **`flow.md`** under
  `mist-restest-adapter/src/main/resources/My-Example/trainticket/`
  references several of the moved classes by FQN; the references read
  fine but should be updated for accuracy.
- **README architecture diagram** (lines 17-40 of the repo-root README)
  describes `mist-core` as "trace shape oracle (TSO) and adaptive
  fault taxonomy". After this batch it owns more material; refresh.

## Known constraints

- `MultiServiceTestCaseGenerator`'s constructor still takes
  `primarySpec` and `dummyPrimaryConf` parameters that became unused
  once the `super(...)` call was removed. Cleaning the signature
  requires touching the two call sites in `MistRunner.java` and
  `TestGenerationAndExecution.java`; held back for Phase B1.C, which
  moves the class wholesale.
- `WorkflowScenario#addTraceId` and `WorkflowScenario#mergeWith` are now
  `public` (used to be package-private). The widening is required by
  `TraceWorkflowExtractor` and `ScenarioOptimizer` reaching across the
  new package boundary; the two methods stay an internal contract of
  the workflow subsystem and are not part of any public API surface
  reviewers will see.
- `mist-core`'s `junit` dependency is now compile-scope (it used to be
  test-scope). `FailedTestCollector` extends `org.junit.runner.notification.RunListener`,
  so the dependency is genuine.
- `mist-core` now has a compile-scope dependency on
  `io.swagger.core.v3:swagger-core` (the OpenAPI model classes —
  *not* the parser, which stays in the adapter). The vendored
  `Operation` POJO has a `@JsonIgnore`-tagged `io.swagger.v3.oas.models.Operation`
  field that the verbatim copy preserves. `swagger.version` is hoisted
  to the parent pom for a single source of truth.

## Auto-regenerated test outputs

`mist-restest-adapter/src/test/java/trainticket_twostage_test/` contains
~56 pre-generated test classes (artifacts of an earlier MIST run). They
still reference `es.us.isa.restest.analysis.TraceErrorAnalyzer` and
similar old paths. Regenerating the demo (next time a TrainTicket
cluster is reachable) will overwrite them with current paths. The
adapter's test compile already failed pre-existingly on Java 21
("system modules path not set in conjunction with -source 11") so this
is not a new failure.
