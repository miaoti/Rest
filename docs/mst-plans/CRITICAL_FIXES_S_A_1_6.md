# MIST — Critical Architecture Fixes (S-tier + A-tier issues 1-6)

> **Audience.** This document is a self-contained execution brief for an
> agent (Claude Code or equivalent) that will land six surgical fixes on
> the MIST tool. The agent has read access to the whole repo and write
> access via `Read`/`Edit`/`Write`/`Bash`. **Do not deviate from this brief.**

---

## 0. Rules of engagement (read this first)

### 0.1 Branding
- User-facing name is **MIST** (Microservice Integration & Scenario Tester).
- Code-internal name stays **MST** (class names like `MstAuthHandler`,
  `MultiServiceTestCaseGenerator`; property keys like `mst.config.path`;
  generator enum value `MST`). The two-layer brand strategy is locked
  in by commit `51266a4b` — do **not** rename anything in code, config,
  or property files.
- Documentation files you create may use `MIST` (the brand).

### 0.2 Branch and base
- **Base your work on `origin/inject-detection`** (the active development
  branch). The auth + loop-streak fixes from commit `67337db9` are in
  this branch and **must not be reverted**.
- Create one fix-specific branch per fix:
  `claude/fix-mst-<NN>-<short-slug>` (e.g. `claude/fix-mst-01-phase-pipeline`).
- Land each fix as its own PR-shaped branch. Do **not** combine fixes.
- Sequence is fixed (see § 0.6). Skipping ahead breaks downstream fixes.

### 0.3 Scope boundaries
You may modify code under:
- `src/main/java/es/us/isa/restest/workflow/**`
- `src/main/java/es/us/isa/restest/generators/MultiServiceTestCaseGenerator.java`
- `src/main/java/es/us/isa/restest/generators/AiDrivenLLMGenerator.java`
- `src/main/java/es/us/isa/restest/generators/ZeroShotLLMGenerator.java`
- `src/main/java/es/us/isa/restest/inputs/InvalidInputPool.java`
- `src/main/java/es/us/isa/restest/inputs/smart/**`
- `src/main/java/es/us/isa/restest/llm/**`
- `src/main/java/es/us/isa/restest/configuration/MstConfig*.java` (new)
- `src/main/resources/My-Example/trainticket/**` (config files only)
- `src/test/java/es/us/isa/restest/{workflow,generators,inputs,llm}/**` (new tests)

You **may not** modify:
- Any class **not** prefixed `Mst`, **not** under `workflow/`, **not**
  under `inputs/smart/`, and **not** in the explicit allow-list above.
  These are upstream RESTest classes; touching them is out of scope.
- Anything under `src/test/java/trainticket_twostage_test/**`
  (auto-generated test cases).
- Any `*.tex`, `*.bib` file (paper layer).
- `README.md` (brand layer).

### 0.4 Tooling
- Use `Read` / `Edit` / `Write` over `cat`/`sed`/`awk`.
- For every fix, run `mvn -q -DskipTests compile` after edits to verify
  the codebase still builds. If `compile` fails, **stop and fix the
  compile error before continuing**.
- After all six fixes land, run `mvn -q test` once. New tests added by
  this brief must pass; pre-existing failures are documented in
  § 9 and may be tolerated only if they predate your branch.
- Invoke the `simplify` skill on every fix before committing it.
  This is the user's standing instruction. Use the skill to double-check
  that the change is minimal, that no unused code was added, and that
  no dead error-handling sneaked in.

### 0.5 Forbidden patterns ("don't")
1. **No new features.** Every fix below is a refactor or a correctness
   patch. If you find yourself adding a property, a class, or a method
   that is not explicitly enumerated here, stop and check the brief.
2. **No backwards-compatibility shims.** Don't keep deprecated
   `applySingleRootDedup` after Fix S-3; delete it. Don't re-export
   removed symbols.
3. **No comments that restate code.** Per the user's coding standard,
   only add comments when the *why* is non-obvious.
4. **No `_v2` / `_old` / `_legacy` duplicate classes.** Delete or replace.
5. **No new system properties** unless explicitly added by Fix A-6
   (which *consolidates*, not expands, the property surface).
6. **No LLM-based generation in the verification step.** Tests must be
   deterministic.
7. **No ablation infrastructure.** Ablation is deferred to Path B
   (see `PATH_B_REBUILD_PLAN.md`). Fix S-1 produces clean
   pipeline boundaries; it does **not** add `--disable-phase-N` flags.
8. **No evaluation extensions.** Don't add new SUTs, new baselines, new
   metrics. That work belongs to Path B's future tasks.

### 0.6 Required execution order

```
Fix S-4 (LLM determinism)    ─┐
                              ├─> Fix S-2 (semantic registry config)
Fix A-6 (MstConfig POJO)     ─┘                                    │
                                                                   ▼
                                                          Fix S-3 (dedup correctness)
                                                                   │
                                                                   ▼
                                                          Fix S-1 (phase pipeline)
                                                                   │
                                                                   ▼
                                                          Fix A-5 (Sniper coverage)
```

**Rationale.** S-4 and A-6 are isolated infrastructure (LLM cache, config
POJO) with zero dependencies — land them first to stabilise the surface.
S-2 reads the new config (A-6 first). S-3 (dedup correctness) must
precede S-1 (phase extraction) because S-1 will lift the dedup code
into its own phase class, which is only safe once the dedup itself is
correct. A-5 (Sniper coverage) edits the new phase boundary, so it must
come last.

### 0.7 Per-fix commit policy
- Each fix lands as exactly one commit (plus its test commit if a test
  is added separately).
- Commit message body lists the symbols touched and the acceptance
  criteria that pass.
- Do **not** push to `inject-detection`. Push the fix branch and stop;
  the user will review and merge.

---

## Fix S-4 — LLM determinism (LAND FIRST)

### Problem
`AiDrivenLLMGenerator` (and the `LLMService` it wraps) call out to
Ollama / Gemini / OpenAI-compatible endpoints with no on-disk caching
and no fixed sampling seed. Two consequences:
- Re-running the test generator on the same trace + same OpenAPI spec
  produces different test bodies, so reported numbers are not
  reproducible.
- A reviewer asked to reproduce results will see drift even on the
  same hardware.

Note: a `random.seed` system property is already honoured by
`SeededRandom.create(scope)` for Java-side randomness (see flow.md
§ "Reproducibility"). The gap is the LLM side.

### Files
- `src/main/java/es/us/isa/restest/llm/LLMService.java`
- `src/main/java/es/us/isa/restest/llm/LLMCallCache.java` *(new)*
- `src/main/java/es/us/isa/restest/llm/LLMConfig.java` (read only;
  no writes)

### Surgical change
1. Create `LLMCallCache` (~120 LOC, single file). It is a file-backed
   JSON cache. Key = SHA-256 of
   `(model_id || backend || prompt || temperature || max_tokens)`.
   Value = the model's text response. Cache file is one JSON object
   keyed by hash, written via atomic rename (write `tmp` then rename).
   Lookup and store are both `O(1)` after the file is loaded.
2. In `LLMService.generateText`, wrap the existing backend dispatch
   (`GeminiApiClient` / OpenAI-compatible HTTP / `OllamaApiClient`)
   with a cache check:
   - If `random.seed` system property is set AND the cache has a hit,
     return the cached value.
   - Otherwise call the backend as today and *also* store the response
     in the cache (so a subsequent seeded run benefits from this run's
     cold call).
3. When `random.seed` is set, force `temperature = 0` on every
   backend's request payload. The `LLMConfig.getTemperature()` accessor
   should be gated to return `0.0` when `random.seed` is set. Implement
   this gating inside `LLMConfig` (not in every call site).
4. The cache file location is `${user.dir}/target/llm-call-cache.json`.
   No new property — it's a fixed path. If the file doesn't exist,
   start empty.

### Acceptance criteria
- [ ] Two consecutive runs with `-Drandom.seed=42` produce byte-identical
  generated test classes for the same scenario set. Verify by running:
  ```
  java -Drandom.seed=42 -jar target/restest.jar <demo>.properties \
      ; sha256sum target/test-cases/Flow_Scenario_1.java
  ```
  twice, comparing checksums.
- [ ] When `random.seed` is unset, behaviour is unchanged from
  `inject-detection` HEAD (cache writes happen, cache reads do not).
- [ ] `target/llm-call-cache.json` exists after a run and parses as
  valid JSON.
- [ ] No new system property is introduced (the cache path is fixed;
  the seed property already exists).
- [ ] `mvn -q -DskipTests compile` succeeds.

### Tests to add
- `src/test/java/es/us/isa/restest/llm/LLMCallCacheTest.java`:
  unit tests for put/get/round-trip, key collision (different prompts
  ⇒ different keys), atomic rename behaviour (corrupt write does not
  leave a half-written file).

### Rollback
The cache file is the only side effect. To roll back: delete
`target/llm-call-cache.json` and revert the branch.

---

## Fix A-6 — MstConfig POJO (LAND SECOND)

### Problem
30+ MST-related system properties are read with `System.getProperty(...)`
scattered across the code. Two specific harms:
- Property precedence is implicit. `mst.generate.only.first.step=true`
  and `scenario.shattering.enabled=false` interact, but the interaction
  is not documented. Reviewers cannot reconstruct a run.
- A typo (`smart.input.fetch.percetage` vs `…percentage`) silently
  produces the default value with no warning.

### Files
- `src/main/java/es/us/isa/restest/configuration/MstConfig.java` *(new)*
- `src/main/java/es/us/isa/restest/configuration/MstConfigValidator.java` *(new)*
- Every existing call site of
  `System.getProperty("mst.*" | "smart.input.fetch.*" |
  "scenario.shattering.*" | "faulty.*" | "trace.merge.*" |
  "test.enhancer.*" | "status.code.exploration.*" |
  "soft.error.cache.*" | "jaeger.*" | "negative.input.generation.*" |
  "llm.response.validation.*")` (must be migrated)

### Surgical change
1. Create `MstConfig` as an immutable POJO. Fields are typed and named
   exactly after the existing property keys (use `kebab-case → camelCase`
   transformation; `mst.generate.only.first.step` ⇒ `generateOnlyFirstStep`).
   Group the fields into sub-records by area:
   - `core` (MST primary toggles)
   - `smartFetch`
   - `llm`
   - `faulty` (negative test config)
   - `scenarioMerge` (Phase 1/2 thresholds)
   - `scenarioShattering`
   - `softErrorCache`
   - `statusCodeExploration`
   - `enhancer`
   - `jaeger`
2. Single entry point: `MstConfig.fromSystemProperties()`. It reads
   every key once, applies defaults from the existing call sites,
   builds the POJO, runs `MstConfigValidator.validate(cfg)`, and
   returns the instance.
3. `MstConfigValidator` checks:
   - **Unknown keys.** If `System.getProperty(key)` returns a value
     for a key that starts with one of the MST namespaces above but is
     **not** in the known-keys whitelist, log `WARN` and exit non-zero
     if `mst.config.strict=true` (this is the only new property; it
     defaults to `false`).
   - **Conflicting keys.** Hard-code the documented conflicts: e.g.
     when `mst.generate.only.first.step=false`, `scenario.shattering.enabled`
     is ignored — emit `INFO` if user set it explicitly.
   - **Range checks.** `faulty.ratio` ∈ [0,1]; `trace.merge.max.session.gap.micros`
     ≥ 0; etc.
4. Make `MstConfig` available as a singleton via
   `MstConfig.instance()` (lazy). Wire every existing call site to
   read from this singleton. The call site change is mechanical:
   replace `System.getProperty("mst.generate.only.first.step", "true")`
   with `MstConfig.instance().core().generateOnlyFirstStep()`.
5. Update **only the documented properties** in
   `flow.md § "Conditions, Flags, and Inputs"` to add a precedence note
   per group. Do **not** rewrite the section.

### Acceptance criteria
- [ ] Zero `System.getProperty(...)` calls remain in MST-mode code for
  keys in the namespaces listed above. Verify:
  ```
  grep -rE "System\.getProperty\(\"(mst|smart\.input\.fetch|scenario\.shattering|faulty|trace\.merge|test\.enhancer|status\.code\.exploration|soft\.error\.cache|jaeger|negative\.input\.generation|llm\.response\.validation)\." \
       src/main/java/es/us/isa/restest
  ```
  returns nothing.
- [ ] With no properties set, defaults match `inject-detection` HEAD
  (run the demo with `mvn -q exec:java …` and confirm test count is
  unchanged ± noise).
- [ ] With a deliberately bogus key
  `-Dmst.config.strict=true -Dmst.generrate.only.first.step=true`
  (note typo), the run exits non-zero.
- [ ] With the same bogus key without `strict=true`, the run continues
  with a `WARN` log.
- [ ] `MstConfigValidatorTest` covers: unknown key, out-of-range
  `faulty.ratio`, conflicting Root-API-Mode + shattering.

### Tests to add
- `src/test/java/es/us/isa/restest/configuration/MstConfigTest.java`
- `src/test/java/es/us/isa/restest/configuration/MstConfigValidatorTest.java`

### Rollback
Revert the branch. No state side effects (the POJO is in-memory).

---

## Fix S-2 — Semantic Dependency Registry: externalise the noun map

### Problem
`TraceWorkflowExtractor.NOUN_TO_KEY` is a hard-coded `HashMap`
(currently at line 634 of the file) listing TrainTicket-domain plurals
like `orders → orderId`, `trips → tripId`, `trains → trainNumber`.
Side effects:
1. `isMeaningfulPathNoun()` only accepts lowercase, ≥3-char, no-hyphen
   segments. AWS-style `/order-items/{order-item-id}` is silently
   dropped. So is `/customers/{cid}/orders/{oid}` (the second ID).
2. To support a new SUT (Sock Shop, Online Boutique, Spotify), users
   must edit Java source and recompile. The tool is not portable.

The paper's P/R/F₁ = 1.000 claim is true *only* on TrainTicket and
*only* because the map was hand-tuned for TrainTicket.

### Files
- `src/main/java/es/us/isa/restest/workflow/TraceWorkflowExtractor.java`
- `src/main/resources/mist/noun-map.default.yaml` *(new)*
- `src/main/resources/My-Example/trainticket/mist-noun-map.yaml` *(new)*
- `src/main/java/es/us/isa/restest/workflow/NounKeyMap.java` *(new)*

### Surgical change
1. Create `NounKeyMap` (~80 LOC). It is a thin wrapper over a
   `Map<String, String>` loaded from YAML at startup. Two construction
   paths:
   - `NounKeyMap.fromDefault()` loads
     `src/main/resources/mist/noun-map.default.yaml` (TrainTicket
     entries verbatim, so behaviour is unchanged on the demo).
   - `NounKeyMap.fromPath(Path)` overrides the default with a
     per-SUT file; missing keys fall back to the default map.
2. Allow hyphenated and underscored keys. Update
   `isMeaningfulPathNoun()` to accept `[a-z]+([-_][a-z]+)*` of total
   length ≥ 3, and to **not** silently drop unrecognised nouns. Instead:
   - If the segment is in the map ⇒ map directly.
   - If the segment is not in the map but matches the meaningful-noun
     regex ⇒ derive a stem by stripping the trailing `s` (plural-safe
     list still applies) and append `Id`. Log at `DEBUG` so users can
     audit.
   - If the segment is not meaningful (`api`, `v1`, length < 3, etc.) ⇒
     skip and log at `TRACE`.
3. Read the map path from `MstConfig.instance().core().nounMapPath()`
   (add this single field to `MstConfig.Core`; this is the **one**
   exception to the "no new properties" rule, because it directly
   substitutes for hard-coded constants. Default value:
   `mist/noun-map.default.yaml` on classpath. **Document it in flow.md.**
4. Make `NOUN_TO_KEY` private and final, populated from the loaded
   `NounKeyMap`. Delete the static initialiser block.

### Acceptance criteria
- [ ] On the bundled TrainTicket demo, scenario extraction produces
  the same merged-scenario count as `inject-detection` HEAD ± 0
  (the default YAML must reproduce TrainTicket behaviour exactly).
- [ ] A test that constructs a URL like
  `/api/v1/order-items/123e4567-e89b-12d3-a456-426614174000` and runs
  `extractFieldsFromUrl()` produces a key (e.g. `orderItemId`) instead
  of silently dropping the segment.
- [ ] A test that constructs a nested URL
  `/api/v1/customers/AAA-BBB/orders/CCC-DDD` produces both
  `customerId=AAA-BBB` and `orderId=CCC-DDD` in `inputFields`.
- [ ] Loading a malformed YAML raises a startup-time exception with a
  clear message (don't fall back silently).
- [ ] `mvn -q -DskipTests compile` succeeds.

### Tests to add
- `src/test/java/es/us/isa/restest/workflow/NounKeyMapTest.java`
- `src/test/java/es/us/isa/restest/workflow/TraceWorkflowExtractorUrlExtractionTest.java`
  with at least: TrainTicket regression, hyphenated, nested, all-caps
  acronym, unknown-meaningful-noun fallback.

### Rollback
Restore the static initialiser block in `TraceWorkflowExtractor` and
delete the new files. Property `mist.noun.map.path` becomes inert.

---

## Fix S-3 — Phase 2.5 dedup correctness

### Problem
`MultiServiceTestCaseGenerator` (`inject-detection` HEAD) maintains two
side-by-side dedup structures:

```java
private final Set<String> seenSingleRootApis = new LinkedHashSet<>();
private final Set<WorkflowScenario> dedupApprovedScenarios =
        Collections.newSetFromMap(new IdentityHashMap<>());
```

`applySingleRootDedup` (around line 3107) uses **identity comparison**
(`dedupApprovedScenarios.contains(sc)`) as a pass-through guard so that
scenarios already kept in Phase 2.5 are not re-evaluated in Phase 3.5.

Failure mode: Phase 3 (`ScenarioOptimizer.optimizeScenarios`) returns
**newly constructed** `WorkflowScenario` instances after shattering.
Those new instances are not in `dedupApprovedScenarios`, so Phase 3.5
re-evaluates them against `seenSingleRootApis` — which by that point
already contains the API keys from Phase 2.5. A shattered single-root
component can be erroneously dropped, breaking the "Phase 3 preserves
coverage" invariant.

### Files
- `src/main/java/es/us/isa/restest/generators/MultiServiceTestCaseGenerator.java`
  (touch only the dedup region: ~line 60–80, ~line 3088–3220)

### Surgical change
1. **Replace identity tracking with phase tagging.** Add a single
   `boolean approvedInDedupPass` field on `WorkflowScenario` (and a
   setter). Phase 2.5 sets `approvedInDedupPass=true` on every scenario
   it keeps. Phase 3 (shattering) propagates the tag from the parent
   scenario to every child component. Phase 3.5 / Phase 4 honour the
   tag instead of consulting `dedupApprovedScenarios`.
2. **Delete `dedupApprovedScenarios`.** It is no longer needed.
   Identity tracking was the bug.
3. **Make `seenSingleRootApis` reset-safe.** Add a `Set<String>
   approvedApiKeys` that records the keys of approved (kept) scenarios
   only. Phase 3.5 / Phase 4 dedup against `approvedApiKeys`, not
   `seenSingleRootApis`. The semantic difference: `seenSingleRootApis`
   was "every key we've ever seen", which over-suppresses; the new set
   is "every key that survived dedup".
4. **Consolidate the two methods.** Delete `deduplicateSingleRootScenarios`
   (line 3092) and `applySingleRootDedup` (line 3107) and replace with
   a single private method `runSingleRootDedupPass(String label,
   List<WorkflowScenario> scenarios, Set<String> approvedKeys)` that
   takes the input list, the approved-key tracker, and the log label.
   Phase 2.5 calls it with an empty `approvedKeys`; Phase 3.5 calls it
   with the populated set from Phase 2.5.
5. **Update `decomposeMultiRootScenarios()`** (referenced in the same
   class around line 3210) to read from `approvedApiKeys`, not
   `seenSingleRootApis`.

### Acceptance criteria
- [ ] On the bundled TrainTicket demo, the number of generated
  `Flow_Scenario_N.java` files is **unchanged** from
  `inject-detection` HEAD.
- [ ] A targeted unit test constructs three scenarios:
  - scenario A: single root `GET /stations` (kept in Phase 2.5)
  - scenario B: multi-root `[POST /trip, GET /stations]` (kept in
    Phase 2.5, shattered in Phase 3 into component A' = `GET /stations`,
    component B' = `POST /trip`)
  - Phase 3.5 must keep both shattered components. Pre-fix behaviour:
    A' is dropped because `/stations` was already in `seenSingleRootApis`.
    Post-fix: A' is dropped only if scenario A's `GET /stations` is
    semantically identical to A' (same canonical key); otherwise both
    survive.
- [ ] `dedupApprovedScenarios` no longer appears in the source.
- [ ] `applySingleRootDedup` and `deduplicateSingleRootScenarios` are
  replaced by exactly one method `runSingleRootDedupPass`.

### Tests to add
- `src/test/java/es/us/isa/restest/generators/PhaseTwoFiveDedupTest.java`
  with the three-scenario regression and a fan-out shatter case.

### Rollback
Revert the branch. The `approvedInDedupPass` field is additive on
`WorkflowScenario` and has no persisted side effects.

---

## Fix S-1 — Extract phase pipeline from the god class

### Problem
`MultiServiceTestCaseGenerator.generate()` (line 286 in
`inject-detection` HEAD, ~52 lines) sequentially invokes Phase 2.5,
shared-pool generation, Phase 3, Phase 4, fault-queue construction,
variant generation. The class is **3,332 lines** (now ~3,422 after
67337db9) and concentrates six responsibilities in one method body. Two
concrete harms:
- Adding or reordering a phase requires editing this one method.
- Phase logic cannot be unit-tested in isolation because the surrounding
  state (`scenarios`, `serviceSpecs`, `serviceConfigs`,
  `dependencyRegistry`, `sharedParameterPools`, `seenSingleRootApis`)
  is private mutable state.

### Files
- `src/main/java/es/us/isa/restest/generators/MultiServiceTestCaseGenerator.java`
- `src/main/java/es/us/isa/restest/workflow/pipeline/WorkflowPipeline.java` *(new)*
- `src/main/java/es/us/isa/restest/workflow/pipeline/PipelineStage.java` *(new)*
- `src/main/java/es/us/isa/restest/workflow/pipeline/PipelineContext.java` *(new)*
- `src/main/java/es/us/isa/restest/workflow/pipeline/stages/*.java` *(new — five files)*

### Surgical change

This is a refactor, **not** a rewrite. The logic of each phase moves
verbatim; only the surrounding plumbing changes. **Do not** simplify or
improve any phase's algorithm in this fix. Behavioural drift = bug.

1. Create the pipeline interfaces:

   ```java
   public interface PipelineStage {
       String name();
       void run(PipelineContext ctx);
   }

   public final class PipelineContext {
       public List<WorkflowScenario> scenarios; // mutated in place
       public final Map<String, OpenAPISpecification> serviceSpecs;
       public final Map<String, MultiServiceConfig> serviceConfigs;
       public final SemanticDependencyRegistry dependencyRegistry;
       public final Set<String> approvedApiKeys; // from S-3
       public final MstConfig config; // from A-6
       // … only the state that is actually read or written by stages
   }

   public final class WorkflowPipeline {
       private final List<PipelineStage> stages;
       public WorkflowPipeline(List<PipelineStage> stages) { /* … */ }
       public void execute(PipelineContext ctx) {
           for (PipelineStage s : stages) {
               log.info("[Pipeline] running stage: {}", s.name());
               s.run(ctx);
           }
       }
   }
   ```

2. Create five stage classes under `workflow/pipeline/stages/`:
   - `Phase25DedupStage`         (calls the new `runSingleRootDedupPass`)
   - `SharedPoolGenerationStage` (lifts `generateSharedParameterPools()`)
   - `Phase3ShatteringStage`     (wraps `ScenarioOptimizer.optimizeScenarios`)
   - `Phase35DedupStage`         (second call to `runSingleRootDedupPass`)
   - `Phase4DecompositionStage`  (lifts `decomposeMultiRootScenarios()`)

3. In `MultiServiceTestCaseGenerator.generate()`, the first ~30 lines
   are replaced by:

   ```java
   PipelineContext ctx = new PipelineContext(
           scenarios, serviceSpecs, serviceConfigs,
           dependencyRegistry, approvedApiKeys, mstConfig);
   List<PipelineStage> stages = List.of(
           new Phase25DedupStage(),
           new SharedPoolGenerationStage(),
           new Phase3ShatteringStage(),
           new Phase35DedupStage(),
           new Phase4DecompositionStage());
   new WorkflowPipeline(stages).execute(ctx);
   ```

   The rest of `generate()` (fault-queue construction and variant loop)
   stays inline — it is the variant generator, not a phase. This fix
   does **not** extract that.

4. **Verbatim lift.** Move the body of each phase method into the
   `run()` method of its stage class. Replace `this.<field>` reads with
   `ctx.<field>`. Resolve any private-method dependencies by either
   making the helper `package-private` on `MultiServiceTestCaseGenerator`
   or duplicating it into the stage class (prefer the former; the
   stages and the generator live in adjacent packages — adjust the
   visibility minimally).

### Acceptance criteria
- [ ] On the bundled TrainTicket demo, the **byte-identical** set of
  `Flow_Scenario_N.java` files is produced compared with the pre-fix
  branch (run with the same `-Drandom.seed=42` after S-4 lands, and
  diff `target/test-cases/` recursively).
- [ ] `MultiServiceTestCaseGenerator.java` line count drops by ≥ 200
  lines (the phase bodies move out).
- [ ] Each stage class has a unit test that constructs a minimal
  `PipelineContext` with hand-rolled scenarios and asserts the stage's
  output on the context. Five tests total.
- [ ] No phase logic is altered. (Verify by inspection: each stage's
  `run()` body matches the pre-fix method body line-for-line modulo
  `this.` → `ctx.` substitution.)

### Tests to add
- `src/test/java/es/us/isa/restest/workflow/pipeline/Phase25DedupStageTest.java`
- `src/test/java/es/us/isa/restest/workflow/pipeline/SharedPoolGenerationStageTest.java`
- `src/test/java/es/us/isa/restest/workflow/pipeline/Phase3ShatteringStageTest.java`
- `src/test/java/es/us/isa/restest/workflow/pipeline/Phase35DedupStageTest.java`
- `src/test/java/es/us/isa/restest/workflow/pipeline/Phase4DecompositionStageTest.java`

### Rollback
Revert the branch. The new files are standalone; the only mutated file
is `MultiServiceTestCaseGenerator.java`.

### Explicit non-goals (don't do these in this fix)
- Don't add per-stage `--enable / --disable` flags. (Ablation is Path B.)
- Don't make the pipeline parallel. Stages stay sequential.
- Don't change the call site of the pipeline from `AbstractTestCaseGenerator`
  upstream code paths.

---

## Fix A-5 — Sniper FaultTarget queue: cover path / header / cookie parameters

### Problem
`generateSharedParameterPools()` (referenced from `MultiServiceTestCaseGenerator`
line ~298 and now moved into `SharedPoolGenerationStage`) builds
`InvalidInputPool` instances keyed on `(rootApiKey, parameter)`. Inspection
reveals it iterates only over **body** and **query** parameters. Path
parameters (e.g. `{orderId}` in `/orders/{orderId}`), header parameters
(`Authorization`, `X-API-Key`, custom headers), and cookie parameters are
not enrolled in the pool. Consequence: `BuildFaultInjectionQueue` never
emits a `FaultTarget` for those parameter locations, so the paper's
"exhaustive 8-fault coverage per parameter" claim is false on every
endpoint with a path or header parameter (most REST endpoints).

### Files
- `src/main/java/es/us/isa/restest/workflow/pipeline/stages/SharedPoolGenerationStage.java`
  (post-S-1) — the loop body that walks an operation's parameters
- `src/main/java/es/us/isa/restest/inputs/InvalidInputPool.java`
  (only if `ParameterLocation` is not already first-class in the pool key)

### Surgical change
1. Audit `SharedPoolGenerationStage.run(ctx)` to confirm which parameter
   locations are iterated. The current iteration likely calls
   `operation.getTestParameters()` and filters by `in == "body" || in == "query"`.
   Remove the filter. Iterate **all** locations: `path`, `query`, `header`,
   `cookie`, `body`. `formData` is rare in modern REST; treat it as `body`.
2. Verify the pool key includes the parameter's location, so a `path`
   `id` and a `query` `id` (same name, different location) do not
   collide. If not, extend the pool key to `(rootApiKey, paramName,
   paramLocation)`.
3. For `header` parameters, apply the schema-aware applicability matrix
   already implemented (flow.md § "Schema-aware fault applicability").
   Most headers are `string`-typed, so most fault categories already
   apply. **Do not** invent new fault categories.
4. For `path` parameters, all eight categories apply per the matrix;
   no extra work needed beyond enrolment.
5. Confirm `MultiServiceRESTAssuredWriter` already emits the request
   builder for `path` / `header` / `cookie` invalid values (it should,
   since positive tests use these locations). If it doesn't, this fix
   is out of scope and the user must be told (see § "Out of scope below").

### Out of scope here
- Authentication-specific exploration (invalid token, expired JWT,
  wrong-role user) is already covered by `AuthManipulationStrategy`
  under Status Code Exploration. **Do not** duplicate it here.
- Do **not** add new fault categories. The eight in
  `InvalidInputType` are the contract.

### Acceptance criteria
- [ ] On the bundled TrainTicket demo, the count of `FaultTarget`
  tuples emitted by `BuildFaultInjectionQueue` increases by ≥ 30 %
  (most TrainTicket admin endpoints use a path `id` that was
  previously skipped). Log the count before/after at `INFO` level for
  the user to read.
- [ ] A generated negative variant for `GET
  /api/v1/orderservice/order/{orderId}` exists with a path-parameter
  fault (e.g. `OVERFLOW: orderId = AAAAA…` of length 5000).
- [ ] A generated negative variant for an endpoint with a header
  parameter exercises that header with a fault.
- [ ] No upstream classes outside the allow-list in § 0.3 are
  modified. (`MultiServiceRESTAssuredWriter` is on the allow-list
  in spirit — confirm with the user before touching it; if a writer
  change is needed and the user agrees, add it to the allow-list and
  proceed.)

### Tests to add
- `src/test/java/es/us/isa/restest/workflow/pipeline/SharedPoolGenerationStagePathParamTest.java`
- `src/test/java/es/us/isa/restest/workflow/pipeline/SharedPoolGenerationStageHeaderParamTest.java`

### Rollback
Revert the branch. The pool-key extension is the only schema-level
change; if the change is keyed on `(rootApiKey, paramName,
paramLocation)`, the new tuple shape is compatible with the existing
`Map<String, InvalidInputPool>` value type.

---

## 7. Global verification (run after all six fixes land)

```bash
# 1. Build
mvn -q -DskipTests compile

# 2. Compile-time tests (the new ones added by this brief)
mvn -q test -Dtest='LLMCallCacheTest,MstConfigTest,MstConfigValidatorTest,NounKeyMapTest,TraceWorkflowExtractorUrlExtractionTest,PhaseTwoFiveDedupTest,Phase25DedupStageTest,SharedPoolGenerationStageTest,Phase3ShatteringStageTest,Phase35DedupStageTest,Phase4DecompositionStageTest,SharedPoolGenerationStagePathParamTest,SharedPoolGenerationStageHeaderParamTest'

# 3. End-to-end smoke on the bundled demo, twice, with seed
java -Drandom.seed=42 -jar target/restest.jar \
     src/main/resources/My-Example/trainticket-demo.properties \
     > run1.log 2>&1
sha256sum target/test-cases/Flow_Scenario_*.java > run1.sums

# Reset target/test-cases so the second run regenerates cleanly
mvn -q clean -DskipTests

java -Drandom.seed=42 -jar target/restest.jar \
     src/main/resources/My-Example/trainticket-demo.properties \
     > run2.log 2>&1
sha256sum target/test-cases/Flow_Scenario_*.java > run2.sums

diff run1.sums run2.sums   # must be empty
```

The diff being empty proves S-4 (determinism) AND S-1 (no behavioural
drift in the pipeline refactor) AND S-3 (dedup correctness) all hold
together.

---

## 8. Master execution checklist

Tick each box only after the acceptance criteria for that fix pass.

- [ ] Branch `claude/fix-mst-04-llm-determinism` created from
      `origin/inject-detection`
  - [ ] `LLMCallCache.java` added
  - [ ] `LLMService.generateText` wraps backend calls with cache
  - [ ] `LLMConfig.getTemperature()` gated by `random.seed`
  - [ ] `LLMCallCacheTest` passes
  - [ ] Two seeded runs produce identical scenario files
  - [ ] `simplify` skill invoked, no unused additions
  - [ ] Single commit pushed; PR not opened (user merges)

- [ ] Branch `claude/fix-mst-06-mst-config` created from S-4 tip
  - [ ] `MstConfig` POJO + sub-records
  - [ ] `MstConfigValidator` checks unknown keys, ranges, conflicts
  - [ ] All `System.getProperty("mst.*"|"smart.input.fetch.*"|…)`
        replaced; grep returns empty
  - [ ] `mst.config.strict=true` exits non-zero on typo
  - [ ] `MstConfigValidatorTest` passes
  - [ ] `simplify` skill invoked
  - [ ] Single commit pushed

- [ ] Branch `claude/fix-mst-02-noun-map-yaml` created from A-6 tip
  - [ ] `NounKeyMap.java` added; loads YAML
  - [ ] `mist/noun-map.default.yaml` shipped with TrainTicket entries
  - [ ] `isMeaningfulPathNoun` accepts hyphen/underscore
  - [ ] `MstConfig.Core.nounMapPath()` added (the single new property)
  - [ ] Hyphenated-URL test passes; nested-URL test passes
  - [ ] Bundled demo produces unchanged scenario count
  - [ ] `simplify` skill invoked
  - [ ] Single commit pushed

- [ ] Branch `claude/fix-mst-03-dedup-correctness` created from S-2 tip
  - [ ] `dedupApprovedScenarios` deleted
  - [ ] `WorkflowScenario.approvedInDedupPass` field added
  - [ ] `approvedApiKeys` set introduced; `seenSingleRootApis` renamed
        or its semantics narrowed
  - [ ] `runSingleRootDedupPass` replaces the two old methods
  - [ ] `decomposeMultiRootScenarios()` updated
  - [ ] `PhaseTwoFiveDedupTest` passes
  - [ ] Bundled demo scenario count unchanged
  - [ ] `simplify` skill invoked
  - [ ] Single commit pushed

- [ ] Branch `claude/fix-mst-01-phase-pipeline` created from S-3 tip
  - [ ] `WorkflowPipeline`, `PipelineStage`, `PipelineContext` added
  - [ ] Five stage classes added under
        `workflow/pipeline/stages/`
  - [ ] `MultiServiceTestCaseGenerator.generate()` rewires to pipeline
  - [ ] Verbatim lift: stage bodies match pre-fix phase methods
  - [ ] Generator class line count drops by ≥ 200 lines
  - [ ] Five stage unit tests pass
  - [ ] Seeded run output is byte-identical with pre-fix output
        (modulo S-2 and S-3 behaviour changes that the user has
        already approved)
  - [ ] `simplify` skill invoked
  - [ ] Single commit pushed

- [ ] Branch `claude/fix-mst-05-sniper-locations` created from S-1 tip
  - [ ] `SharedPoolGenerationStage` enrolls path / header / cookie
        parameters in `InvalidInputPool`
  - [ ] Pool key extended to `(rootApi, name, location)` if needed
  - [ ] `FaultTarget` count up ≥ 30 % on bundled demo (logged)
  - [ ] Path-parameter negative variant exists
  - [ ] Header-parameter negative variant exists
  - [ ] Two new tests pass
  - [ ] `simplify` skill invoked
  - [ ] Single commit pushed

- [ ] Global verification (§ 7) executed; `run1.sums` == `run2.sums`
- [ ] All branches pushed to `origin`; user notified

---

## 9. Known pre-existing failures (tolerated)

Document here any pre-existing failing tests in `src/test/java/es/...`
that predate `claude/fix-mst-04-llm-determinism`. If a test fails on
`origin/inject-detection` at the moment of branching, **do not** try
to fix it. List it here and move on.

(Empty at time of writing. The executing agent fills this in after the
first `mvn -q test` run on `inject-detection`.)

---

## 10. Out-of-scope follow-ups (for Path B, not now)

These are observations from the audit that are **not** part of this
brief. Do not act on them. They are recorded for the user's reference
and will be picked up under `PATH_B_REBUILD_PLAN.md`.

- The `Test Case Enhancer`, `Status Code Exploration`, `Parameter
  Error Analysis cache`, and `Soft Error Rule Cache` subsystems feel
  bolted on rather than architected. Path B will decide whether to
  promote them to first-class contributions or relegate them to
  appendix material.
- The `MultiServiceTestCaseGenerator` variant loop (post-S-1, the part
  *not* extracted) is still ~2,800 lines and concentrates
  fault-injection + parameter resolution + LLM dispatch. Path B will
  split this further.
- The `SemanticDependencyRegistry` uses purely lexical patterns to
  match IDs (`Id`, `ID`, `uuid` suffixes). Path B contemplates a
  learned name-embedding approach for non-canonical REST naming.
- Trace-as-Oracle is currently rule-based + LLM. Path B turns it into
  a first-class "trace shape oracle" that learns invariants over
  per-API span trees.

---

*End of brief.*
