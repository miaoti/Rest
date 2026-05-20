# B1 Inventory — `MultiServiceTestCaseGenerator` ↔ RESTest dependency surface

> Phase B1.A deliverable for `PROMPT_B1_SEVER_RESTEST_INHERITANCE.md`.
> Status: **closed for Phase B1.D (in-place sever)** — see § 7.
> Snapshot taken against `inject-detection` HEAD `43aff11e`.

## 1. Pre-flight state (before sever)

| Check | Command | Result |
|---|---|---|
| mist-core has zero RESTest edges | `grep -rE 'es\.us\.isa' mist-core/src/main/java` | **0 matches** |
| Inheritance edge present | `grep -n 'extends AbstractTestCaseGenerator' mist-restest-adapter/.../MultiServiceTestCaseGenerator.java` | `:37` |
| Reactor compiles | `mvn -q -DskipTests compile` | **OK** |
| Java toolchain | `mvn --version` | Maven 3.9.11 / OpenJDK 21.0.10 |

## 2. Module layout (today)

```
Rest/
├── mist-core/                       (clean — no es.us.isa edges)
├── mist-llm/                        (clean)
├── mist-restest-adapter/            (carries the generation pipeline)
│   └── es.us.isa.restest.{generators,workflow,inputs,validation,registry,configuration,llm,main,writers,...}
└── mist-cli/                        (clean — only imports MistRunner + MstConfig)
```

## 3. Category A — `AbstractTestCaseGenerator` surface MIST actually uses

`MultiServiceTestCaseGenerator extends AbstractTestCaseGenerator` was inherited
from upstream RESTest by convention, but a complete audit shows MIST consumes
**none of the inherited state** and **only one inherited setter from the
outside**:

| Element | Inherited from base | Actually used by MIST? | `file:line` |
|---|---|---|---|
| `super(spec, conf, nTests)` ctor | Yes (only mechanism to populate `spec`, `conf`, `numberOfTests` on the base) | **No** — MIST never reads `super.spec`, `super.conf`, or `super.numberOfTests`. The values are also stored on MIST's own fields (`serviceSpecs`, `serviceConfigs`, `scenarios`). | `MultiServiceTestCaseGenerator.java:291` (call) |
| `super.spec` field | Yes | No reference | — |
| `super.conf` field | Yes | No reference | — |
| `super.numberOfTests` | Yes | No reference | — |
| `super.rand` / `super.seed` | Yes | No reference — MIST owns `private Random random = SeededRandom.create(...)` instead | `MultiServiceTestCaseGenerator.java:73` |
| `super.nominalGenerators` / `super.faultyGenerators` | Yes | No reference — MIST owns `Map<String, Map<PoolKey, InvalidInputPool>> faultyParameterPools` | `MultiServiceTestCaseGenerator.java:72` |
| `super.authManager` | Yes | No reference | — |
| `super.faultyRatio` | Yes (protected) | **Shadowed** by MIST's own `private float faultyRatio` | `MultiServiceTestCaseGenerator.java:63` (declaration); `:307` (assignment from `MstConfig.faulty().ratio()`) |
| `super.n*` counters | Yes | No reference — MIST manages its own variant counters | — |
| `generateOperationTestCases(Operation)` | abstract | Override is **a no-op stub** returning `Collections.emptyList()` | `MultiServiceTestCaseGenerator.java:757` (before sever) |
| `generateNextTestCase(Operation)` | abstract | Override is **a no-op stub** returning `null` | `MultiServiceTestCaseGenerator.java:759` (before sever) |
| `hasNext()` | abstract | Override is **a no-op stub** returning `false` | `MultiServiceTestCaseGenerator.java:761` (before sever) |
| `generate()` (concrete) | concrete | **Completely replaced** — MIST's `generate()` never calls `super.generate()` | `MultiServiceTestCaseGenerator.java:407` |
| `setCheckTestCases(boolean)` (concrete) | concrete | Called externally by `MistRunner.createMstGenerator()` line 546 and `TestGenerationAndExecution.createGenerator()` line 336 but the field is never read by MIST | `MistRunner.java:546` |

**Disposition for category A: drop or inline.** The four `@Override` stubs are
dropped outright (no caller; not abstract once `extends` is gone). The
`setCheckTestCases` setter is reimplemented as a documented no-op on
`MultiServiceTestCaseGenerator` so the external CLI surface stays unchanged.

## 4. Category B — RESTest data classes consumed by MIST code

These are the `es.us.isa.restest.*` types that appear in fields, parameters,
or return types of MIST-specific code. The disposition column says what
Phase B1.B-F would do with them under the full prompt; rows marked **Defer**
are intentionally **not** moved by the in-place sever in this branch and are
tracked in `B1_FOLLOWUPS.md`.

| Type | Used in | Disposition |
|---|---|---|
| `es.us.isa.restest.specification.OpenAPISpecification` | `MultiServiceTestCaseGenerator` field + many MIST classes | Wrap (would become `io.mist.core.spi.MistSpec` in B1.E) — **Defer** |
| `es.us.isa.restest.configuration.pojos.TestConfigurationObject` | `MultiServiceTestCaseGenerator` field + writer + many | Wrap — **Defer** |
| `es.us.isa.restest.configuration.pojos.Operation` | hot path of `MultiServiceTestCaseGenerator.findOperation` etc. | Wrap — **Defer** |
| `es.us.isa.restest.configuration.pojos.TestParameter` | hot path | Wrap — **Defer** |
| `es.us.isa.restest.testcases.TestCase` | return type of `generate()` | Vendor (small, leaf data class) — **Defer** |
| `es.us.isa.restest.testcases.MultiServiceTestCase` | MIST-owned subclass already; only its parent `TestCase` is RESTest | Already-MIST in spirit (in `mist-restest-adapter` for now) — **Defer move** |
| `es.us.isa.restest.util.SeededRandom` | MIST-owned class living in RESTest packages | Pure-MIST class; should move to `mist-core` later — **Defer** |
| `es.us.isa.restest.util.ConsoleProgressBar` | MIST-owned UX helper | Pure-MIST — **Defer** |
| `es.us.isa.restest.util.RESTestException` | thrown by MIST code | Wrap or rename to `MistException` — **Defer** |
| `es.us.isa.restest.writers.IWriter` | MistRunner / TestGenerationAndExecution | Wrap (B1.E `MistTestWriter` SPI) — **Defer** |

## 5. Category C — RESTest static utilities called by MIST

Searched via `grep '^import es\.us\.isa\.restest' …util*` against MIST
packages.

| Utility | Callers | Disposition |
|---|---|---|
| `IDGenerator` | `MistRunner` (test-class IDs), `MultiServiceRESTAssuredWriter` | Pure-data — **Defer move into `mist-core`** |
| `PropertyManager` | `MistRunner`, `TestGenerationAndExecution` | Lives at the CLI boundary — **Defer** |
| `Timer` | `MistRunner` | Pure-MIST timing helper — **Defer** |

## 6. Category D — RESTest interfaces implemented by MIST

The MIST generator does **not** implement any RESTest interface other than
the (now-severed) `AbstractTestCaseGenerator` superclass contract.

## 7. Category E — Pure-MIST classes living in RESTest packages

These are MIST's own code, only located under `es.us.isa.restest.*` for
historical reasons. They have no RESTest behavioural dependency. Moving them
is mechanical and is the work of Phases B1.B-C in the full prompt.

| Class (current package) | Lines | Target package (post-rebuild) |
|---|---|---|
| `es.us.isa.restest.configuration.MstConfig` | ~600 | `io.mist.core.config.MistConfig` |
| `es.us.isa.restest.configuration.MstConfigValidator` | ~200 | `io.mist.core.config.MistConfigValidator` |
| `es.us.isa.restest.workflow.TraceWorkflowExtractor` | ~1000+ | `io.mist.core.workflow.TraceWorkflowExtractor` |
| `es.us.isa.restest.workflow.ScenarioOptimizer` | medium | `io.mist.core.workflow.ScenarioOptimizer` |
| `es.us.isa.restest.workflow.SemanticDependencyRegistry` | medium | `io.mist.core.registry.SemanticDependencyRegistry` |
| `es.us.isa.restest.workflow.NounKeyMap` | small | `io.mist.core.workflow.NounKeyMap` |
| `es.us.isa.restest.workflow.WorkflowScenario` / `WorkflowStep` | small | `io.mist.core.workflow.*` |
| `es.us.isa.restest.workflow.pipeline.*` (Pipeline + 5 stages + supports) | sizable | `io.mist.core.workflow.pipeline.*` |
| `es.us.isa.restest.registry.RootApiRegistry` | medium | `io.mist.core.registry.RootApiRegistry` |
| `es.us.isa.restest.inputs.InvalidInputPool` | medium | `io.mist.core.generation.InvalidInputPool` |
| `es.us.isa.restest.inputs.smart.*` | sizable | `io.mist.core.generation.smart.*` |
| `es.us.isa.restest.generators.MultiServiceTestCaseGenerator` | 2987 | `io.mist.core.generation.MistGenerator` |
| `es.us.isa.restest.generators.AiDrivenLLMGenerator` (75-ish) | small | `io.mist.core.generation.AiDrivenValueGenerator` |
| `es.us.isa.restest.generators.ZeroShotLLMGenerator` | small | `io.mist.core.generation.ZeroShotValueGenerator` |
| `es.us.isa.restest.testcases.MultiServiceTestCase` | small | `io.mist.core.testcase.MultiServiceTestCase` |
| `es.us.isa.restest.analysis.TraceErrorAnalyzer` | medium | already mostly subsumed by `io.mist.core.oracle.shape.*` — confirm |

## 8. What the in-place sever actually does (Phase B1.D, executed)

1. Drops `extends AbstractTestCaseGenerator` from
   `MultiServiceTestCaseGenerator` (file `:37`).
2. Removes the `super(primarySpec, dummyPrimaryConf, scenarios.size())`
   call from the constructor (file `:291`).
3. Removes the three no-op `@Override` stubs at the bottom of the file
   (`generateOperationTestCases`, `generateNextTestCase`, `hasNext`).
4. Removes the `@Override` annotation on `generate()` (now declares a
   new public method rather than overriding an inherited one).
5. Adds a documented no-op `setCheckTestCases(boolean)` so external
   CLI code (`MistRunner` line 546, `TestGenerationAndExecution`
   line 336) compiles unchanged.
6. Removes `import es.us.isa.restest.generators.AbstractTestCaseGenerator`
   from `MistRunner.java` and changes the local + return type at
   lines 251 / 399 / 400 from `AbstractTestCaseGenerator` to
   `MultiServiceTestCaseGenerator`.

## 9. Out of scope for this branch (tracked in `B1_FOLLOWUPS.md`)

- Phase B1.B vendoring data classes into `mist-core`.
- Phase B1.C moving the generation pipeline into `mist-core` as
  `MistGenerator` (the package + rename).
- Phase B1.E SPI definition (`MistSpecLoader`, `MistTestWriter`,
  `MistTestExecutor`) and the cleanup of remaining `es.us.isa.*`
  imports inside what will become `mist-core` material.
- Phase B1.F `META-INF/services/` adapter registration.
- Phase B1.G positioning-doc citation refresh, README diagram, flow.md
  brand updates, byte-identical demo proof (cluster needed).

## 10. Verification gates met by this branch

| Gate (from prompt § 7) | Status |
|---|---|
| 7.1 Gate B1.A — inventory present | **Yes** (this file). |
| 7.4 Gate B1.D — `mist-core/src` has zero `extends AbstractTestCaseGenerator` | **Yes** (trivially — no MIST class lives in `mist-core` yet that would extend it). |
| 7.4 Gate B1.D — MIST generator no longer extends the RESTest base class | **Yes** — see § 8. |
| 7.4 Gate B1.D — `mvn -q -DskipTests compile` passes for the reactor | **Yes**. |
| 7.4 Gate B1.D — Demo byte-identical to baseline | **Not run** — bundled demo needs the remote TrainTicket cluster at `http://129.62.148.112:32677`. Tracked as a follow-up so the user can wire up the cluster (or a recorded fixture) and re-verify offline. |
| 7.5 / 7.6 / 7.7 — remaining gates | **Deferred** to follow-up branches per `B1_FOLLOWUPS.md`. |
