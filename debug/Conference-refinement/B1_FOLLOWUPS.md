# B1 follow-ups — what is still ahead of the rebuild

> Tracking file for items deferred out of the current `inject-detection`
> HEAD. See `PROMPT_B1_SEVER_RESTEST_INHERITANCE.md` for the full plan
> and `B1_INVENTORY.md` for the per-class disposition.

## What has landed already (B1.A + B1.D + most of B1.C-clean)

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

## What is still ahead — the remaining B1.B/C/E/F/G work

The remaining classes in `mist-restest-adapter/src/main/java/es/us/isa/restest/`
all share the same blocker: they reference one of these RESTest types
directly. To move them, the RESTest types need to be either vendored
into `mist-core` (Phase B1.B) or wrapped as an SPI implemented by the
adapter (Phase B1.E).

| Blocking RESTest type | Where it lives upstream | Disposition under the prompt |
|---|---|---|
| `configuration.pojos.Operation` | `es.us.isa.restest.configuration.pojos` | Vendor (small POJO) |
| `configuration.pojos.TestParameter` | same | Vendor |
| `configuration.pojos.TestConfigurationObject` | same | Vendor |
| `configuration.pojos.Auth` / `TestConfiguration` | same | Vendor |
| `specification.OpenAPISpecification` | `es.us.isa.restest.specification` | Wrap as `io.mist.core.spi.MistSpec` |
| `specification.OpenAPIParameter` | same | Wrap as `io.mist.core.spi.MistParameter` |
| `specification.OpenAPISpecificationVisitor` | same | Wrap (static utility — fold into `MistSpec`) |
| `testcases.TestCase` | `es.us.isa.restest.testcases` | Vendor (small data carrier) |
| `testcases.MultiServiceTestCase` | same | Vendor (MIST-owned subclass of `TestCase`) |
| `util.ConsoleProgressBar` | `es.us.isa.restest.util` | Move (MIST-only UX helper) |
| `util.PropertyManager` | same | Wrap or vendor (RESTest-style properties reader) |
| `util.IDGenerator` | same | Move (MIST-only) |
| `util.Timer` | same | Move (MIST-only) |
| `inputs.stateful.ParameterGenerator` / `BodyGenerator` | RESTest stateful | Wrap if MIST keeps using; else drop |

Once these are dealt with the following classes can move:

| File (currently in adapter) | Target under `mist-core` once unblocked |
|---|---|
| `generators/MultiServiceTestCaseGenerator` | `io.mist.core.generation.MistGenerator` (rename per prompt) |
| `workflow/ScenarioOptimizer` | `io.mist.core.workflow.ScenarioOptimizer` |
| `workflow/SemanticDependencyRegistry` | `io.mist.core.registry.SemanticDependencyRegistry` |
| `workflow/pipeline/PipelineContext` | `io.mist.core.workflow.pipeline.PipelineContext` |
| `workflow/pipeline/PipelineStage` | `io.mist.core.workflow.pipeline.PipelineStage` |
| `workflow/pipeline/WorkflowPipeline` | `io.mist.core.workflow.pipeline.WorkflowPipeline` |
| `workflow/pipeline/stages/*` (9 files) | `io.mist.core.workflow.pipeline.stages.*` |
| `inputs/smart/SmartInputFetcher` | `io.mist.core.smart.SmartInputFetcher` |
| `inputs/smart/SmartLLMParameterGenerator` | `io.mist.core.generation.SmartLLMParameterGenerator` |
| `inputs/llm/LLMParameterGenerator` | `io.mist.core.generation.LLMParameterGenerator` |
| `enhancer/StatusCodeExplorationEnhancer` | `io.mist.core.enhancer.StatusCodeExplorationEnhancer` |
| `enhancer/TestCaseEnhancer` | `io.mist.core.enhancer.TestCaseEnhancer` |
| `testcases/MultiServiceTestCase` | `io.mist.core.testcase.MultiServiceTestCase` (after vendoring `TestCase`) |
| `configuration/multiservice/MstConfig` (legacy loader) | `io.mist.core.config.legacy.MstConfig` |

## Other deferred items

- **Brand sweep** (`Mst` → `Mist` on moved classes, system-property keys
  `mst.*` stay). Prompt § Phase B1.C step 3 allows this once a class
  moves packages. Deferring keeps in-flight diffs reviewable.
- **Byte-identical demo proof** (`-Drandom.seed=42` against
  `trainticket-demo.properties`). Needs the remote TrainTicket cluster
  reachable from the build host, or a recorded fixture.
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

## Auto-regenerated test outputs

`mist-restest-adapter/src/test/java/trainticket_twostage_test/` contains
~56 pre-generated test classes (artifacts of an earlier MIST run). They
still reference `es.us.isa.restest.analysis.TraceErrorAnalyzer` and
similar old paths. Regenerating the demo (next time a TrainTicket
cluster is reachable) will overwrite them with current paths. The
adapter's test compile already failed pre-existingly on Java 21
("system modules path not set in conjunction with -source 11") so this
is not a new failure.
