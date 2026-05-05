# Reviewer feedback on smart-fetch refinement plan

Date: 2026-05-05
Reviewer: Independent Reviewer Agent
Plan reviewed: refinement-plan.md (8 streams, 40 findings, 11 d effort)
Audit cross-checked: smart-fetch-bug-audit.md (40 findings)
Framework cross-checked: smart-fetch-quality-framework.md (17 metrics, 47 citations)

## Summary verdict

The plan is **sound in direction but ships only with revisions**. It correctly identifies the highest-leverage data-hygiene work (Streams 1-2 are essentially perfect framing), and its dependency graph and severity bucketing match the audit. However, several individual fix sketches are mis-cited or under-scoped, the KPI thresholds are inconsistent with the partner quality framework, the registry-migration story has hidden compatibility risks, and effort estimates for Streams 4 and 6 are optimistic. Strongest aspect: cohesion of the data-hygiene streams (1+2). Weakest: the migration safety story, the test-plan thinness, and the under-treatment of `MultiServiceTestCaseGenerator`'s 5 call-site surface.

## Comments

### Comment 1: Stream 4 wires ApiMapping.successRate updates that are still not persisted
**Type:** Blocker
**Targets:** Stream 4 / Finding #14, dataflow-map § 7.8
**Comment:** The plan addresses YAML write storms (good) and stream 5 fixes the score function (good), but it does NOT address dataflow-map § 7.8: `ApiMapping.successRate`/`lastUsed` mutations from Priority 1 (`SmartInputFetcher.java:270, :279`) are never persisted because `saveRegistry()` is only called from the discovery branch (`:323`). After Stream 5 fixes the recentness math, the freshness signal will only ever decay between runs if discovery runs — yet the goal of MS (S5.2 in the framework) is precisely to amortize discovery work. Net result: the EMA recency signal never accumulates across runs, contradicting Stream 5's stated KPI ("`lastUsed` evidence that the freshness signal is now live"). The plan should explicitly call `saveRegistry()` (or mark dirty + scheduled flush) after Priority-1 mutations.
**Suggested action:** Add a row to Stream 4: "Persist `ApiMapping` mutations from Priority 1." Either (a) call `saveRegistry()` periodically while the run is active, or (b) on shutdown / scenario-end. Without this, KPI #6 in §"Validation acceptance criteria" of the plan ("older mappings score lower") is not actually verifiable on the *next* run.
**Evidence:** `SmartInputFetcher.java:270, :279` (only in-memory mutate); `:323` (only persist site); dataflow-map.md:617-619.

### Comment 2: Migration "split keys by consumerApiKey::paramName" is irreversible without a fallback tier
**Type:** Blocker
**Targets:** Stream 1 / Finding #15, KPI implicit
**Comment:** The plan proposes changing the registry key from `<paramName>` to `<consumerApiKey>::<paramName>` and migrating existing rows via "split each entry into per-consumer copies." But the existing YAML has no consumer-API attribution per mapping (the bare-key format throws that information away — that's the bug). A migration script cannot reconstruct what consumer originally produced each mapping; at best it can copy the same mapping list to *every* operation that has a parameter of that name, which is exactly the cross-service collision the bug describes, just with N copies. The plan's "(Optional: keep a fallback global tier ...)" parenthetical is in fact load-bearing — without that tier, the post-migration registry is functionally an empty learning slate for Priority 1, and every parameter falls through to LLM discovery on first use. That is a regression of MS (Mapping Stability, framework S5.2) from whatever it is today to ~0% on the migration boundary, exactly the failure mode S5.2 was designed to detect.
**Suggested action:** Make the global-fallback tier mandatory, not optional, and document the read order: "first try `<consumerApiKey>::<paramName>` exact key, then fall back to bare `<paramName>` global tier." The migration script keeps the existing entries in the bare tier and only writes to the scoped tier on new discoveries.
**Evidence:** `InputFetchRegistry.java:79-89` (`getMappingsForParameter`/`addMapping`); registry YAML is keyed by bare paramName today; framework's S5.2 KPI (>= 0.85 across consecutive runs) cannot be met if migration churns the entire registry.

### Comment 3: KPI thresholds in plan are inconsistent with quality framework's green band
**Type:** Major
**Targets:** Plan § "KPIs to validate the plan" / Quality framework § 4 KPI table
**Comment:** Five inconsistencies between plan thresholds and the partner framework:
  1. Plan: `yield_smart` "climb >= 0.5 -> >= 0.7". Framework S6 green = >= 0.80, amber = 0.60-0.80. So the plan is targeting only the **amber** band, not green. Justify or relabel.
  2. Plan: "fraction of registry mappings with `successRate: 0.0` drop from 75% to < 30%". The framework has no metric named that; the closest is REC (S5.1) which measures EMA convergence, threshold green >= 0.5. The plan's number is arbitrary and not tied to any published baseline; the audit cited 130/174 = 74.7% (verified: `awk '/^    successRate:/'` returns 130 zeroes out of 174 entries), but 30% is asserted without justification. Either tie this to REC's variance-shrinking definition or drop the precise threshold.
  3. Plan: "Number of `*/query` fabricated endpoints drop from 60+ to 0." Audit and current registry actually show **56** (verified: `grep -c "/query\""`), not 60+. Trivial but the plan's own evidence base is off.
  4. Plan: "literal `{paramName}` endpoints drop from 17 to 0." Actual count is **27** (verified: `grep -nE 'endpoint:.*\{' | wc -l`). The audit Finding #3 claims 17; the plan inherits that error. Re-count.
  5. Plan defines no KPI for capitalization variants beyond "drop to 0%", but cap-variant detection requires the OpenAPI spec's canonical form; the plan should pin a target like "0 distinct services in registry that case-insensitive-collide with another."
**Suggested action:** Align plan KPI thresholds with framework green/amber bands. Re-run `grep`/`awk` on the live registry to refresh the baseline counts. Where the plan picks a number not in the framework, justify it.
**Evidence:** Plan lines 152-157; framework lines 487-500; live registry counts via `grep`/`awk` shown above.

### Comment 4: Stream 6 (Auth) at 2 d underestimates work; JWT decode is non-trivial
**Type:** Major
**Targets:** Stream 6 effort
**Comment:** Stream 6 lists three findings (#11, #12, #26) and budgets 2 d. Verified at `SmartFetchAuthManager.java:84-140`, `:167-171`. The plan proposes:
  1. Configurable login URL/body/path (mechanical: ~0.5 d).
  2. Wire/remove dead `auth.user.*` (mechanical: ~0.25 d).
  3. 401/403 invalidate-and-retry in `fetchFromApiMapping` (~0.5 d).
But the plan's text in Finding #11 is more ambitious: "Even better, decode the JWT's `exp` claim and use it as the actual expiry instead of guessing." JWT decoding requires either an extra dependency (`jjwt` / `nimbus-jose-jwt`) or a hand-rolled base64URL+JSON parser, plus tests for clock-skew, missing-exp, malformed claim, and key-rotation cases. Add ~1 d. Also, the auth manager has no current concurrency guard (dataflow-map § 4.3 fields 20-21 noted non-volatile); when 401-retry races with another concurrent fetch, both will try to re-login. That is a 0.5 d concurrency-correctness sub-task. Total realistic effort: 2.5-3 d.
**Suggested action:** Either drop the "decode JWT exp" ambition (and acknowledge the audit's Finding #11 is only partly fixed) or bump Stream 6 to 3 d.
**Evidence:** `SmartFetchAuthManager.java:32` (TOKEN_VALIDITY_MINUTES=30); `:84-140` (no JWT parsing today); dataflow-map.md:319-321 (non-volatile fields).

### Comment 5: Stream 4 caching the registry singleton is not actually concurrency-safe by default
**Type:** Major
**Targets:** Stream 4 / Finding #13
**Comment:** The plan's fix sketch: "Cache a single `InputFetchRegistry` reference in `SmartLLMParameterGenerator` and reuse it. Optionally invalidate on file mtime change." But `InputFetchRegistry`'s internal `parameterMappings`, `parameterErrors`, and `llmPrompts` are plain `HashMap`/`ArrayList` (dataflow-map § 4.2 fields 15-19, "plain `HashMap` — not thread-safe"). Today this is hidden because `SmartLLMParameterGenerator` re-loads per call (Finding #13's bug), so each call has its own copy. After the fix, multiple `SmartLLMParameterGenerator` instances may share one `InputFetchRegistry` and concurrently call `getErrorContextForParameter` while `addParameterError` is being called from the writer-emitted test code (`MultiServiceRESTAssuredWriter.java:998`). That is a real race. The plan does not mention concurrency hardening of the registry data classes.
**Suggested action:** Either (a) make `parameterMappings`/`parameterErrors` `ConcurrentHashMap` and use `CopyOnWriteArrayList` for the inner lists (mechanical, but YAML round-trip needs Jackson hint); or (b) wrap mutations in a `ReadWriteLock` inside `InputFetchRegistry`. Add ~0.5 d to Stream 4. The plan should also clarify whether the cached singleton is per-`SmartLLMParameterGenerator` or process-wide.
**Evidence:** dataflow-map.md:309-313 (plain HashMap); `InputFetchRegistry.java:86-89` (`addMapping`); `:109-112` (`addParameterError`).

### Comment 6: Stream 4's #14 "Buffer saveRegistry() until scenario boundary" is unsafe under crash
**Type:** Major
**Targets:** Stream 4 / Finding #14
**Comment:** The plan proposes to debounce `saveRegistry()` to scenario-boundary or to a coalescing timer. That trades I/O storm for crash data loss: a JVM kill mid-scenario discards every mapping discovered in that scenario. Worse, the plan never proposes the simplest correctness fix — atomic write via temp file + rename, mentioned by the audit (Finding #14 fix sketch). Without an atomic write, even a single discovery save can produce a corrupt YAML if the JVM is interrupted between `writeValue` byte 0 and EOF. The audit's fix sketch says exactly this; the plan dropped it.
**Suggested action:** Reinstate the audit's "(1) Write to `registry.yaml.tmp` then atomic rename. (2) Wrap save in `synchronized(this)` / `ReentrantLock`." Then debouncing is safe. Document in the plan that the trade-off is "slight crash-time data loss (last-N-seconds of discoveries) for I/O reduction."
**Evidence:** `InputFetchRegistry.java:64-74` (`saveToFile`); audit Finding #14 sketch (4 numbered remedies, plan only takes #1).

### Comment 7: Stream 1 plan dismisses the `*/query` fabrication path while keeping `pickFirstReasonableEndpoint`
**Type:** Major
**Targets:** Stream 1 / Finding #33
**Comment:** Plan's Finding #33 fix is "return null instead of fabricating." Verified at `SmartInputFetcher.java:3938-3942`. But the call site (`:3925-3931`) ALSO has a `pickFirstReasonableEndpoint(getEndpoints)` "emergency fallback" that runs *before* the `/query` fabrication block — and `pickFirstReasonableEndpoint` is a heuristic that picks the first non-`welcome|health|status|info` GET endpoint regardless of parameter relevance. The plan does not address this heuristic. After the `/query` block returns null, `pickFirstReasonableEndpoint` may still return a wrong-but-real endpoint that the system happily persists with the wrong service-to-parameter binding. Net: the registry will still accumulate weakly-relevant mappings, just with valid paths.
**Suggested action:** Either include `pickFirstReasonableEndpoint` in Stream 1's "stop persisting bad data" — return null if the LLM picker fails N times — or document that this heuristic is intentionally left as a noisy-but-real-endpoint fallback. The current plan straddles both positions.
**Evidence:** `SmartInputFetcher.java:3925-3931, 3948-3960`; plan row Finding #33 only addresses lines 3938-3942.

### Comment 8: Stream 3 ID-semantics is incomplete vs. the audit's grep-evidence of 8 sites
**Type:** Major
**Targets:** Stream 3 / Finding #5
**Comment:** Audit Finding #5 grep evidence: "`grep -n 'paramName.contains(\"id\")' SmartInputFetcher.java` -> lines **2515, 2535, 931, 1661, 1676, 1725, 2206, 3347** — eight loose checks." The plan's Stream 3 row addresses three of these (`:2515-2517`, `:2535`, and the 912-963 block which contains `:931`). The other five (`:1661, :1676, :1725, :2206, :3347`) are not in scope of the plan. If Stream 3's stated goal is "boundary-aware ID definition is unique across the codebase," half-doing it leaves the same inconsistency in five other sites.
**Suggested action:** Expand the Stream 3 file:line list to all 8 sites the audit identified. The plan's claim that "this single change fixes Findings 5, 18 and most of the `paramName.contains` family in one pass" is an *audit* claim; the plan should commit to actually doing the "most of the family" work, not just two sites.
**Evidence:** Audit Finding #5 evidence string lists 8 file:line pairs; plan's Stream 3 cites only 3.

### Comment 9: Stream 2 "skip endpoints with `{...}` placeholders" loses ID-harvesting endpoints unfairly
**Type:** Major
**Targets:** Stream 2 / Finding #3
**Comment:** Plan offers options (a) skip / (b) substitute / (c) strip trailing `/{...}`. The plan calls option (a) "far simpler and matches the existing intent." But registry rows like `/api/v1/orderservice/order/{orderId}` are precisely the operations whose response shape contains the candidate IDs we want to harvest. Skipping them means smart-fetch can no longer learn from any path-templated GET-by-ID operation — that's a substantial fraction of the OAS surface. Option (c) is the design intent (strip suffix, hit the collection endpoint) but the plan doesn't commit to it. Worse: option (c) interacts badly with the registry-key change in Stream 1 (Finding #15) — if the bare-keyed mapping points at `/api/v1/orderservice/order` and gets reused for *every* parameter named `id`, you re-introduce the cross-service collision the same plan is trying to remove.
**Suggested action:** Pick option (c) — strip the trailing `/{...}` and persist the collection-style endpoint — and document the constraint that the resulting mapping must only serve the bare-paramName tier (per Comment 2's fallback tier proposal). Option (a) should be the second-choice fallback when option (c) cannot find a stripped collection endpoint that's a real OAS path.
**Evidence:** `SmartInputFetcher.java:423-432`; registry has 27 literal-`{}` endpoints (re-counted; audit/plan said 17).

### Comment 10: Plan dismisses `traceProducerEndpoints` persistence (#19) but it contradicts Stream 6's spirit
**Type:** Major
**Targets:** Stream 8 / Finding #19
**Comment:** Plan moves Finding #19 to Stream 8 with "no code change, just document in flow.md." But the audit explicitly notes (Finding #19): "the most reliable signal — 'this exact endpoint was observed producing this exact parameter value in a real trace' — is discarded after one run." The framework's S7 (Trace-Priority Beat) measures whether the trace path beats the registry path; if traces are session-scoped, S7 starts at 0 every run and TPB_uplift can never be computed across runs. The plan's other streams (1-7) all aim to make the registry better; Stream 8's "documentation-only" treatment of #19 leaves the *most authoritative* signal (Jaeger-observed producer) outside that improvement loop. Either (a) persist trace mappings (audit's fix sketch — small code change) or (b) declare S7 a single-run-only metric and update the framework. The "do nothing" middle position is not internally consistent.
**Suggested action:** Reclassify #19 to Stream 1 with the audit's fix sketch ("On successful trace fetch, also `registry.addMapping(paramName, traceMapping)`"). The "stale URL" concern is already handled by the (now-fixed) freshness decay from Stream 5's score math.
**Evidence:** Audit Finding #19 fix sketch (lines 568-572); framework S7 (lines 376-405); plan line 126 dismisses to flow.md.

### Comment 11: Plan does not address 5 `MultiServiceTestCaseGenerator` invocation sites individually
**Type:** Major
**Targets:** Plan § "Things I am explicitly NOT proposing" / Risk of regression in adjacent subsystems
**Comment:** Dataflow-map § 1.2 enumerates 5 invocation points of `fetchSmartInput` (lines 333, 811, 948, 2340, 2986) and notes that only call site #3 wires `traceProducerEndpoints`. After the plan's changes, several behavior-changing fixes will affect all 5 sites uniformly:
  - Stream 2 (skip path-templated endpoints): the pool-seed loop at `:2340` will see fewer mapping candidates and may exit early without filling the pool. The pool-size logic in `MultiServiceTestCaseGenerator.computeTargetPoolSize` is not in scope. If the pool ends up smaller, `generateAdditionalArrayValues` (`:2984`) starves.
  - Stream 5 (recentness math): a fresh registry ranks differently; pool seeding may pick different mappings on the first run vs. on the migration day.
  - Stream 7 (drop 20-char floor; honor envelope shapes): the array top-up call site #5 (`:2986`) will accept smaller payloads it previously rejected, changing the diversity baseline.
  - The plan's only test-plan note is "every Stream PR should add at least one test under `src/test/java/es/us/isa/restest/inputs/smart/`" — but this directory **does not exist today** (`ls src/test/java/es/us/isa/restest/inputs/` shows boundary, fixed, perturbation, random, stateful — no smart subdir). And per-PR unit tests do not catch the multi-call-site interactions.
**Suggested action:** Add an integration-test row to the plan: at least one end-to-end run of `MultiServiceTestCaseGenerator` against the TrainTicket spec, with golden snapshots of "registry mappings discovered," "pool sizes per parameter," and "yield_smart per parameter." Mention that tests live in a yet-to-be-created `src/test/java/es/us/isa/restest/inputs/smart/` directory.
**Evidence:** dataflow-map.md:35-49 (5 call sites); `ls src/test/java/es/us/isa/restest/inputs/` (no `smart/` subdirectory); plan line 188.

### Comment 12: Stream 8's `discoverByPatterns` deletion conflicts with audit's "delete servicePatterns" recommendation
**Type:** Major
**Targets:** Stream 8 / Finding #32
**Comment:** Plan: "Delete the method and its caller invocation." Audit's fix sketch had two options: (a) re-enable pattern discovery or (b) **also delete `servicePatterns` from `InputFetchRegistry`** to remove dead state. The plan picks "delete method only" but `InputFetchRegistry.servicePatterns` (plus its `initializeDefaults` 5 default entries at `:200-218` and the YAML serialization) is unaddressed. The result: a YAML schema field that no code reads, but that downstream tools (anyone parsing the YAML) might still try to honor. This makes the registry harder to reason about, not easier. The framework's MS metric (S5.2) compares mapping triples across runs — leaving a non-functional `servicePatterns` block in the YAML invites confusion.
**Suggested action:** Expand Stream 8's #32 row to include "Remove `servicePatterns` field from `InputFetchRegistry`, `initializeDefaults` block, and the YAML serialization roundtrip; migrate existing YAMLs to drop the section." This is the same cleanup pattern the audit recommends.
**Evidence:** Audit Finding #32 (lines 904-913); `InputFetchRegistry.java:200-218`; plan line 123.

### Comment 13: Stream 5 budget of 0.5 d is too low given test surface
**Type:** Minor
**Targets:** Stream 5 effort
**Comment:** Stream 5 is one-line code change (#1) plus two trivial constants (#27 in Stream 4, #6 = `Math.max(5, 10)` -> `10`). But it has the highest *functional* impact on registry ranking. The plan note says "Add a unit test asserting older mapping scores below newer." That's one assertion. To verify the fix is correct, also need: (i) score comparison with mappings differing only in `priority`, (ii) score comparison with mappings differing only in `successRate`, (iii) verify the recentness-decay window is configurable per the plan's text "divide by a configurable decay window (default 30 d)." Realistic test surface: 4-6 unit tests + a config wiring test. Estimate becomes 0.75-1.0 d.
**Suggested action:** Bump Stream 5 to 0.75 d.
**Evidence:** `ApiMapping.java:69-76` (3-line method); plan line 80.

### Comment 14: Stream 7 #29 (skip array element 0) is dismissed as latent but is reachable post-Stream 8 cleanup
**Type:** Minor
**Targets:** Stream 7 / Finding #29
**Comment:** Plan addresses #29 with "Drop the 'element 1 is more representative' hack." Audit notes the method (`selectValueWithFallbackLogic`) is dead per Finding #9. After Stream 8 deletes the dead JSONPath helpers, the method dies with them. So fixing #29 is making changes to code that Stream 8 will then delete. Either schedule #29 *after* Stream 8 (do not touch the dead code) or merge them. As written they're independent rows in different streams that touch the same lines.
**Suggested action:** Mark #29 as "no fix needed; resolved by Stream 8 deletion of `selectValueWithFallbackLogic`" — or move to Stream 8.
**Evidence:** `SmartInputFetcher.java:912-963` (selectValueWithFallbackLogic); audit Finding #9 marks it dead; plan line 106 still proposes a fix.

### Comment 15: Stream 8 #21 (the `_<idx>` suffix bug) is mis-classified as cleanup
**Type:** Minor
**Targets:** Stream 8 / Finding #21
**Comment:** Audit #21 severity is High; plan keeps it under "code-hygiene cleanup" Stream 8 ("None of these change behavior on a successful run"). But the audit explicitly demonstrates this is a **functional** bug producing values like `"42_2"` for an integer parameter — which is precisely the kind of value that fails downstream `isValidValueForParameter`, then triggers LLM regeneration, then re-fails. This is value-correctness, not hygiene. Putting it last conflicts with the dependency-graph entry that has Stream 7 (validation) as a prerequisite for Stream 8.
**Suggested action:** Move #21 to Stream 7 (Validation) where High-severity functional fixes belong. Stream 8 should be only Low/Medium hygiene.
**Evidence:** Audit Finding #21 (severity High); plan line 128.

### Comment 16: Plan's "out of scope" list elides the closed-domain short-circuit's one bug
**Type:** Question
**Targets:** Plan § "Things I am explicitly NOT proposing"
**Comment:** Plan: "The closed-domain short-circuit for booleans/enums (`SmartInputFetcher.java:2046-2057`)." Plan says "do not regress them." Audit Finding #17 (`cleanBooleanValue`) explicitly shows that closed-domain short-circuit *is bypassed* downstream by `cleanBooleanValue` reintroducing `yes/on/enabled/active` as boolean variants. So claiming the short-circuit is correct *and* fixing #17 are not the same thing. The short-circuit + the cleaner together produce the bug.
**Suggested action:** Question to the plan author: does Stream 7's #17 fix coexist with the closed-domain short-circuit, or does it require one of them to change? If both stay, document the new contract: "closed-domain short-circuit emits LLM-style synonyms; cleanBooleanValue ignores synonyms and only accepts true/false/1/0."
**Evidence:** Audit Finding #17 (lines 519-522); plan line 182 claims short-circuit is good and unchanged.

### Comment 17: Plan does not propose a metric for the migration's success
**Type:** Question
**Targets:** Stream 1 / Migration safety
**Comment:** Plan says "Save the migrated copy under `input-fetch-registry.YYYYMMDD.bak.yaml` before overwriting." But there's no defined acceptance criterion that the migration is "successful." The framework's S5.2 MS metric is the natural fit: "MS between pre-migration and post-migration registries should be >= 0.85" (or some explicitly-justified lower threshold given that we are deliberately changing some mappings). Without that, "the migration ran" is the only check.
**Suggested action:** Add a "Migration acceptance" sub-section: pre/post mapping count diff, the set of services that lost case-variants, the set of `*/query` endpoints removed, and an MS measurement.
**Evidence:** Framework S5.2 (lines 319-339); plan line 25.

### Comment 18: Stream 4's #28 (split connect/read timeouts) duplicates #11 (auth login timeout)
**Type:** Nit
**Targets:** Stream 4 / Finding #28; Stream 6 / Finding #11
**Comment:** `SmartFetchAuthManager.performLogin()` at `:94-95` also uses `setConnectTimeout(10000); setReadTimeout(10000);` — same hardcoded value as the discovery path. Plan's Stream 4 splits the discovery timeouts but doesn't propagate the same split to the auth manager. After Stream 6 makes the auth manager generic, the timeouts should also use the new config keys.
**Suggested action:** Add a note in Stream 6 to consume the new connect/read timeout keys from Stream 4. Trivial 5-minute change but easy to forget.
**Evidence:** `SmartFetchAuthManager.java:94-95`; plan rows for #28 and #11.

### Comment 19: Plan lists `SmartLLMParameterGenerator.java:204-237` for Finding #13 but the cache fix needs broader scope
**Type:** Nit
**Targets:** Stream 4 / Finding #13
**Comment:** The plan's fix sketch focuses on the per-call YAML reload at `SmartLLMParameterGenerator.java:213`. But the audit notes the YAML is also re-read by every fetcher constructor at `SmartInputFetcher.java:1288-1302`. Multiple `SmartInputFetcher` instances (one per `MultiServiceTestCaseGenerator`) all parse the same YAML on startup. The plan's wording "Cache a single `InputFetchRegistry` reference in `SmartLLMParameterGenerator`" only addresses the helper, not the constructor reload. Two separate caches → two separate views → potential drift.
**Suggested action:** Make the cached registry a process-wide singleton (or scoped to `SmartInputFetchConfig.registryPath`) so both the fetcher constructor and the `SmartLLMParameterGenerator` helper share it.
**Evidence:** dataflow-map.md:240 ("loaded at `loadRegistry` runs once in the fetcher constructor; also re-loaded from disk per parameter").

### Comment 20: Plan miscites the Finding #10 file:line list
**Type:** Nit
**Targets:** Stream 8 / Finding #10
**Comment:** Plan cites "`SmartInputFetcher.java:574, 859, 1593, 2080, 2920, 3281, 3725, 4007`" for the 2044 prompt-cap. Verified by `grep -n 2044 SmartInputFetcher.java`: actual occurrences are at **574, 999, 1593, 2080, 3281, 3725, 4007, 4154** (plus related 1844/1944/1500 buffers at 1022, 3482, 3529, 3516). Lines 859 and 2920 do NOT contain a `2044` reference. The plan inherited the audit's slightly-imprecise file:line list verbatim. If a contributor reads "fix at :859" they will not find anything to fix.
**Suggested action:** Re-grep and update the file:line list. Include the related 1844/1944/1500 buffers in the same row since they're computed from the same effective cap.
**Evidence:** `grep -n 2044 SmartInputFetcher.java` returns 9 lines; plan cites 8 with two wrong (859, 2920).

### Comment 21: Test plan thinness — no perf-regression test for Stream 4
**Type:** Nit
**Targets:** Stream 4 acceptance
**Comment:** Stream 4 says "Acceptance: after this stream, a run that previously took N seconds in YAML parsing should drop by >= 90%." But the plan's "every Stream PR should add at least one test" provision is a unit test under a non-existent test directory (Comment 11). Perf assertions like "YAML parse time drops 90%" require a benchmark harness, not a JUnit. Without one, the acceptance is a vibe.
**Suggested action:** Either (a) add a JMH benchmark or simple `System.nanoTime` harness to the plan's deliverables, or (b) drop the 90% number to a binary "registry is loaded at most once per fetcher lifetime" invariant testable by adding a counter.
**Evidence:** Plan line 70.

### Comment 22: Plan does not address `parameterErrors` size in registry (51K-line file)
**Type:** Nit
**Targets:** Implicit; dataflow-map § 6.5
**Comment:** Dataflow-map § 6.5 records that the registry's error section (51232 - 2745 = 48,487 lines, ~94% of the file) is dominated by a single OVERFLOW probe with a 49 KB Lorem-ipsum payload. The plan's Stream 8 fixes #40 (key by encoded URL) but does not propose any size cap or eviction on `parameterErrors`. After Stream 4 starts caching the registry, every consumer holds the full error history in memory. A size-bounded LRU on `parameterErrors` would close that loop.
**Suggested action:** Add to Stream 4 or Stream 8: cap `parameterErrors` per-endpoint at N (e.g., 50) entries with FIFO eviction. Defer the exact cap to a follow-up if needed but flag it.
**Evidence:** dataflow-map.md:519 ("That single entry inflates the registry by ~25%."); Stream 8 row #40 only addresses keying, not size.

### Comment 23: Stream 1 should rename or alias Finding #4 fix to also remove "ts-" prefix collisions
**Type:** Nit
**Targets:** Stream 1 / Finding #4
**Comment:** Plan: "Normalize service strings to lowercase before lookup, persist, or validation." But the registry shows `ts-travel-service` and `ts-travel2-service` as distinct services (former 22 entries, latter 11 — verified by `awk '/^    service:/'`). Lowercase normalization will keep them separate, which is correct. However, the audit's example list also shows `ts-admin-User-service` which after lowercasing becomes `ts-admin-user-service` (5 entries). Make sure the migration script doesn't collapse `ts-admin-user-service` (real) with anything else through over-eager normalization.
**Suggested action:** Add a one-line note: "normalization is `toLowerCase()` only — do NOT strip `ts-` prefix or numeric suffixes." Will save someone 30 minutes of registry corruption.
**Evidence:** `awk '/^    service:/'` count in registry shows `ts-travel-service` vs `ts-travel2-service` separately.

### Comment 24: Plan's dependency map shows Stream 4 depends on Stream 5, but the inverse is also true
**Type:** Nit
**Targets:** Plan § Dependency map
**Comment:** Plan: "Stream 4 wants Stream 5's score fix (otherwise the registry persistence cost dominates)." The reverse is also true: Stream 5's "older mapping scores below newer" KPI requires Stream 4's persistence fix (see Comment 1). The dependency is bidirectional. Either ship them as a single PR pair or document that the KPI for Stream 5 is verifiable only after both ship.
**Suggested action:** Update dependency map to show Stream 4 ↔ Stream 5 mutual dependency, or merge them.
**Evidence:** Comment 1 above; plan line 147.

## Open questions for the plan author

1. **Migration global-fallback tier:** Is the bare-keyed global tier mandatory or optional? The plan says "(Optional)" but the migration logic relies on it (Comment 2).

2. **JWT exp parsing:** Does Stream 6 commit to JWT exp parsing or only to making the validity-minutes configurable? The plan text suggests both; effort is much larger if both (Comment 4).

3. **Test directory:** The plan says PRs add tests under `src/test/java/es/us/isa/restest/inputs/smart/`. This directory does not exist. Does the first PR create it? Are tests required to live there or is `src/test/java/...InputFetchTest.java` (top-level) acceptable?

4. **Concurrency:** Will Stream 4 also harden `InputFetchRegistry` for shared access, or are we accepting a known race once the per-call reload is removed (Comment 5)?

5. **`saveRegistry` after Priority-1 mutations:** Is this in scope of Stream 4 or out of scope (Comment 1)? If out of scope, how does Stream 5's KPI ("older mappings score lower in next run") get verified?

6. **TrainTicket-only registry vs. SUT-agnostic:** Stream 6 makes the auth manager generic; Stream 1 migrates the trainticket registry. After Stream 6, if a non-trainticket SUT produces a different registry layout, do all migration scripts get parametrized?

7. **`*/query` post-fix behavior:** When the LLM picker fails after retries, the plan returns null (good). But `pickFirstReasonableEndpoint` (Comment 7) still runs first. Is that intended? If so, document it; if not, expand Stream 1 to gate it.

8. **Stream 3 site coverage:** Will Stream 3 fix all 8 `paramName.contains("id")` sites the audit identified, or only the 3 cited in the plan (Comment 8)?

9. **Trace producer endpoint persistence:** Is the "documentation only" position on Finding #19 final? If so, framework S7's TPB cross-run analysis becomes unreproducible (Comment 10).

10. **Acceptance baseline counts:** The plan states `successRate: 0.0` is 75% (130/174 verified) but cites 60+ for `*/query` (actual 56) and 17 for literal `{...}` (actual 27). Will baseline counts be re-measured before the work starts (Comment 3)?
