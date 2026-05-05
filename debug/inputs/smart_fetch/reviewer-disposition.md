# Disposition of reviewer feedback on smart-fetch refinement plan

Date: 2026-05-05
Plan author: Claude (Opus 4.7)
Reviewer document: [`reviewer-feedback.md`](./reviewer-feedback.md) (24 comments)
Plan document: [`refinement-plan.md`](./refinement-plan.md)

This document goes through every reviewer comment and judges whether it is **necessary** (✅ accept), **partially necessary** (🟡 accept with scope change), or **not necessary** (❌ reject), with justification grounded in re-grepped evidence and the partner audit / framework / dataflow-map.

**Bottom line:** I accept 19 comments fully, partially accept 4 (scope clarifications), and reject 1 outright. Five reviewer claims I spot-checked by re-grep — all verified. The plan will be revised to incorporate accepted/partial-accept feedback before any work is kicked off.

## Verification of reviewer's grep evidence

I re-ran the reviewer's spot-checks before writing the dispositions:

| Reviewer claim | My re-grep | Verdict |
|---|---|---|
| 7-8 sites of `paramName.contains("id")` | 7 sites verified at lines 931, 1661, 1725, 2206, 2515, 2535, 3347 | ✅ Reviewer's "8" was off by one, but the substance is correct: plan covers 3, leaves 4 unfixed |
| 9 sites of `2044` cap | 9 sites verified (574, 999, 1593, 2080, 3281, 3725, 4007, 4154, plus 1022 and 3516 as related buffers) | ✅ Plan miscites 859 and 2920; both fail to grep. Reviewer is right. |
| `*/query` fabricated endpoints = 56, not 60+ | `grep -cE '/query"$'` returns 56 | ✅ Plan baseline is wrong |
| literal `{paramName}` endpoints = 27, not 17 | `grep -nE 'endpoint:.*\{'` returns 27 | ✅ Plan baseline is wrong |
| `successRate: 0.0` mappings = 130 of 175 = 74.3% | Verified | ✅ Plan said 75% (130/174), close — but the denominator is 175 not 174 |
| `src/test/java/es/us/isa/restest/inputs/smart/` does not exist | `ls` confirms only `boundary, fixed, perturbation, random, stateful` and two top-level test files | ✅ Plan must create the directory in its first PR |

All five evidence-based claims are verified. The reviewer's homework is solid.

---

## Disposition table

| # | Type | Verdict | Action |
|---|---|---|---|
| 1 | Blocker | ✅ Accept | Add row to Stream 4: persist Priority-1 mutations on scenario boundary + shutdown hook |
| 2 | Blocker | ✅ Accept | Make global-fallback tier in Stream 1 mandatory, not optional |
| 3 | Major | 🟡 Partial accept (sub-claims a-e) | Re-baseline KPI counts; align thresholds with framework's amber/green bands; document the conservative initial targets |
| 4 | Major | 🟡 Partial accept | Drop JWT-exp parsing from scope; bump Stream 6 to 2.5 d for concurrency guard only |
| 5 | Major | ✅ Accept | Add ConcurrentHashMap / ReadWriteLock hardening to Stream 4; add 0.5 d |
| 6 | Major | ✅ Accept | Re-instate atomic write (temp + rename) in Stream 4 |
| 7 | Major | ✅ Accept | Expand Stream 1 #33 to gate `pickFirstReasonableEndpoint` behind same null-on-failure rule |
| 8 | Major | ✅ Accept | Expand Stream 3 to all 7 sites of `paramName.contains("id")` |
| 9 | Major | ✅ Accept | Stream 2 prefers option (c) strip-trailing-`{...}`; option (a) skip is fallback only |
| 10 | Major | 🟡 Partial accept | Keep session-scoped (per design intent) BUT emit per-run JSON log so S7 TPB is computable across runs |
| 11 | Major | ✅ Accept | Add integration-test row + create `src/test/java/es/us/isa/restest/inputs/smart/` in first PR |
| 12 | Major | ✅ Accept | Stream 8 #32 expands to delete `servicePatterns` field, defaults, YAML serialization |
| 13 | Minor | ✅ Accept | Bump Stream 5 to 0.75 d |
| 14 | Minor | ✅ Accept | Remove Stream 7 #29 row; subsumed by Stream 8 dead-code deletion |
| 15 | Minor | ✅ Accept | Move Stream 8 #21 to Stream 7 (correct severity bucket) |
| 16 | Question | ✅ Accept | Document new contract: `cleanBooleanValue` accepts only `true`/`false` after Stream 7 #17 |
| 17 | Question | ✅ Accept | Add "Migration acceptance" sub-section to Stream 1 with MS measurement |
| 18 | Nit | ✅ Accept | Add cross-reference note in Stream 6 to consume Stream 4's split-timeout config |
| 19 | Nit | ✅ Accept | Make cached registry process-wide singleton (or `registryPath`-scoped) |
| 20 | Nit | ✅ Accept | Update Finding #10 file:line list with re-grep; include 1022 and 3516 buffers |
| 21 | Nit | 🟡 Partial accept | Drop "90% reduction" claim; replace with binary "registry loaded ≤ 1× per fetcher" counter assertion |
| 22 | Nit | ✅ Accept | Add `parameterErrors` size cap (50/endpoint, 1024-char reason) to Stream 4 |
| 23 | Nit | ✅ Accept | Add note: normalization is `toLowerCase()` only; no `ts-` strip; no numeric-suffix strip |
| 24 | Nit | ✅ Accept | Document Stream 4 ↔ Stream 5 mutual dependency; recommend bundling as one PR |

**Counts:** 19 ✅ Accept, 4 🟡 Partial, 1 ❌ Reject. Wait — re-counting: 19 ✅, 5 🟡, 0 ❌. Let me redo this correctly. Actually all 24 are at least partially accepted. None outright rejected.

**Counts (corrected):** 19 ✅ Accept · 5 🟡 Partial · 0 ❌ Reject.

---

## Per-comment justifications

### Comment 1 — ✅ Accept (necessary)

The reviewer is right. `mapping.updateSuccessRate(...)` is called at `SmartInputFetcher.java:270` (success path) and `:279, :284` (failure path) but `saveRegistry()` is only invoked at `:323` inside the discovery branch. After Stream 5 fixes the recentness math, the freshness signal is computed in-memory but never persisted across runs unless discovery runs again. **Stream 5's KPI** "older mappings score lower in next run" cannot be verified without this. The dataflow-map § 7.8 documents this exact gap.

**Why it is necessary:** Without persisting, Stream 5's headline KPI is uncomputable, which means we cannot validate that the score-math fix actually works. The fix is also cheap (debounced flush at scenario boundary or on shutdown). Add a Stream 4 row.

### Comment 2 — ✅ Accept (necessary)

The reviewer caught a migration-correctness issue I had glossed over with a parenthetical "(Optional)". The bare-keyed YAML cannot be split into per-consumer keys without losing all existing learning. The fallback tier must be mandatory, with clear read order: try `<consumerApiKey>::<paramName>` first, fall back to bare `<paramName>` global tier.

**Why it is necessary:** Without the mandatory fallback, the migration day zeroes out 175 hard-earned mappings, regressing yield_smart and forcing re-discovery of every parameter. Framework MS metric (S5.2) would be near 0 across the migration boundary — exactly the failure mode it was designed to detect.

### Comment 3 — 🟡 Partial accept (necessary with scope clarification)

I accept the spirit of all five sub-claims but with nuance:

- **3a (yield_smart 0.7 vs 0.8 green):** Accept. The plan's 0.7 is the framework's amber band. Re-label as a **phased target**: Phase 1 amber (≥0.7 after Streams 1-2), Phase 2 green (≥0.8 after Streams 4-7). This is honest sequencing, not a regression.
- **3b (75%→<30% successRate=0):** Partial. The 30% number was a vibe; tie it to framework's REC (S5.1) green threshold of "EMA convergence ≥ 0.5" as the indirect proxy. Drop the precise "30%" claim; assert "REC ≥ 0.5 on 80% of mappings."
- **3c (60+ vs 56), 3d (17 vs 27):** Accept. Pure counting errors. Re-baseline before kickoff. The corrected numbers come from `grep -cE '/query"$'` and `grep -nE 'endpoint:.*\{'` — both of which I re-verified.
- **3e (capitalization KPI undefined):** Accept. Add KPI: "0 distinct services in registry that case-insensitive-collide with another."

**Why it is necessary:** The audit / plan / framework / registry must agree on numerical baselines or the work cannot be measured. Reviewer's verification grep returned the canonical numbers; my plan inherited stale / approximate ones from the audit. Errors in baseline propagate into post-fix verification ("we said 60+ → 0; we got 56 → 0; is that a fix?").

### Comment 4 — 🟡 Partial accept (necessary, but scope is narrower than reviewer thinks)

The reviewer correctly flags that JWT exp parsing is non-trivial. **However, the plan does NOT actually commit to JWT exp parsing** — that ambition lives only in the audit's Finding #11 fix sketch ("even better, decode the JWT's exp claim"). I'll be explicit: Stream 6 only makes `tokenValidityMinutes` configurable, not the exp claim parsing. The audit's "even better" addendum is intentionally NOT in scope (would require new dependency or hand-rolled JWT parsing + clock-skew + key-rotation tests).

The reviewer's concurrency point IS valid: the JWT cache's `jwtToken` and `tokenExpiry` fields are not volatile (`SmartFetchAuthManager.java:29-31`), so concurrent 401-retry-then-relogin races are real. **Bump Stream 6 to 2.5 d for the concurrency guard alone**, NOT for JWT exp parsing.

**Why it is necessary in part:** The concurrency hazard is real and cheap to fix (`synchronized` block around `performLogin`). The JWT-exp-parsing rejection is also necessary — the plan will document explicitly that the configurable-validity-minutes is the agreed scope.

### Comment 5 — ✅ Accept (necessary)

The reviewer caught that today's per-call YAML reload (the bug we are fixing) **also masks a latent concurrency bug** in `InputFetchRegistry`'s plain `HashMap`/`ArrayList` fields. After the cache fix, multiple `SmartInputFetcher`/`SmartLLMParameterGenerator` instances share one registry. Concurrent `getMappingsForParameter` (read) + `addParameterError` (write) is unsafe.

**Why it is necessary:** Skipping this would trade a perf bug for a non-deterministic correctness bug. Fix: convert `parameterMappings` to `ConcurrentHashMap<String, CopyOnWriteArrayList<ApiMapping>>` (compatible with Jackson YAML round-trip), wrap `parameterErrors` mutations in a `ReadWriteLock`. Add 0.5 d.

### Comment 6 — ✅ Accept (necessary)

Atomic write (temp file + rename) was in the audit's Finding #14 fix sketch but I dropped it. Without it, even a single save can corrupt the YAML if the JVM is interrupted between `writeValue` start and EOF. Even more relevant after debouncing: a 30-minute coalesced batch lost to crash is much worse than a per-discovery flush.

**Why it is necessary:** Data integrity for a 51K-line learned artifact is non-negotiable. The fix is small (`Files.move(tmp, registry, ATOMIC_MOVE)`) and is a one-liner around the existing `writeValue`.

### Comment 7 — ✅ Accept (necessary)

The reviewer caught a real gap: `pickFirstReasonableEndpoint` (`SmartInputFetcher.java:3925-3931, :3948-3960`) runs as an "emergency fallback" before the `/query` synthesis. My plan removes the `/query` block but leaves `pickFirstReasonableEndpoint` untouched. So if the LLM picker fails, we'll still fall through to `pickFirstReasonableEndpoint`, which selects "the first non-`welcome|health|status|info` GET endpoint regardless of relevance" — i.e., still a wrong-but-real endpoint.

**Why it is necessary:** Stream 1's stated goal is "stop persisting bad data." Half-doing it (remove fabrication, keep heuristic-but-real) leaves the registry still acquiring weakly-correlated mappings. Expand the row.

### Comment 8 — ✅ Accept (necessary)

Re-grep verified 7 sites of `paramName.contains("id")` (931, 1661, 1725, 2206, 2515, 2535, 3347). The plan addressed 3 (2515, 2535, and the 912-963 block which contains 931). The 4 remaining sites (1661, 1725, 2206, 3347) are LLM-validation and prompt-construction code — **most of which are still wrong** (e.g., `:1725` constructs a prompt that mis-classifies `paid` as ID).

**Why it is necessary:** Stream 3's stated goal is "boundary-aware ID definition is unique across the codebase." Half-doing it leaves the same inconsistency in 4 sites. Expand to all 7.

### Comment 9 — ✅ Accept (necessary)

The reviewer's option (c) — strip trailing `/{...}` and persist the collection-style endpoint — is the design intent and matches what flow.md's diagram implies. Skipping (option a) discards a substantial fraction of the OAS surface (any GET-by-ID operation), and is too conservative.

**Why it is necessary:** The whole purpose of smart-fetch is to harvest IDs from collection endpoints; option (a) would prevent harvesting from collection-by-ID endpoints, which is most of the harvestable surface in TrainTicket. Pick option (c) as primary; option (a) is the fallback when no real collection endpoint exists.

### Comment 10 — 🟡 Partial accept (necessary in modified form)

The reviewer's worry about S7 TPB cross-run measurement is real, but the design rationale for session-scoping is also real: trace-observed endpoints are highly volatile (Jaeger captures specific sessions; the "URL" may include path-baked IDs that have already been deleted on the SUT). Persisting them risks ranking stale endpoints highly across runs.

**Compromise:** Keep traceProducerEndpoints session-scoped (no code change to behavior) BUT emit them as a per-run side log file (e.g., `logs/smart-fetch/run-<id>/trace-endpoints.json`). External tooling can compute S7 TPB across runs from these logs without polluting the registry.

**Why it is partially necessary:** The reviewer's S7 concern is genuine, but the design intent the plan inherited from `flow.md:825` is also right. The compromise satisfies both — trace data for analysis, no pollution of the registry's ranking signal.

### Comment 11 — ✅ Accept (necessary)

The reviewer correctly flagged: (a) the test directory does not exist, (b) per-PR unit tests cannot catch multi-call-site interactions across `MultiServiceTestCaseGenerator`'s 5 invocation points (`:333, :811, :948, :2340, :2986`), (c) Stream 2 (skip path-templated endpoints) and Stream 7 (drop 20-char floor) both change pool-seeding semantics that ripple through.

**Why it is necessary:** RESTest is a research tool but ships test cases people run. Behavior-changing fixes must be validated against the integrated MST flow, not just unit tests. Add an integration test: run the existing `TrainTicketTwoStageTest` (or equivalent) and snapshot "registry mappings discovered," "pool sizes per parameter," "yield_smart per parameter."

### Comment 12 — ✅ Accept (necessary)

Deleting only the `discoverByPatterns` method but leaving `servicePatterns` field, `initializeDefaults` 5 default entries (`InputFetchRegistry.java:200-218`), and the YAML round-trip serialization is half a deletion. The framework's MS metric (S5.2) compares mapping triples across runs — leaving a non-functional `servicePatterns` block in the YAML invites confusion when a future reader tries to reason about it.

**Why it is necessary:** The audit's Finding #32 fix sketch had two options; the plan picked the wrong one. Expand to delete the field + defaults + YAML schema + migrate existing YAMLs to drop the section.

### Comment 13 — ✅ Accept (necessary)

The reviewer's analysis is correct. To verify the score fix, we need: (i) score(old, fresh)<score(new, fresh), (ii) score with priority diffs, (iii) score with successRate diffs, (iv) configurable decay window. That's 4-6 unit tests + a config wiring test. 0.5 d is too tight; bump to 0.75 d.

**Why it is necessary:** Score math is a regression magnet. Cheap to err in either direction. The extra 0.25 d is a bargain.

### Comment 14 — ✅ Accept (necessary)

The reviewer correctly noticed that `selectValueWithFallbackLogic` (where #29 lives) is itself dead code that Stream 8 deletes. So fixing #29 is rearranging deck chairs on a method about to be deleted.

**Why it is necessary:** Trivial scope reduction. Remove #29 from Stream 7. Stream 8's deletion subsumes it.

### Comment 15 — ✅ Accept (necessary)

Audit Finding #21 is severity High (functional bug — produces `"42_2"` for an integer parameter, which fails downstream `isValidValueForParameter`, which triggers LLM regeneration, which re-fails). The plan put it in Stream 8 hygiene ("None of these change behavior on a successful run") which is wrong-bucketing.

**Why it is necessary:** Severity buckets exist so a contributor reading Stream 8's "low/medium hygiene" framing doesn't accidentally defer a High-severity functional fix. Move #21 to Stream 7.

### Comment 16 — ✅ Accept (necessary as documentation, not as a code change)

The reviewer is correct that the closed-domain short-circuit (emits `{true, false}` for booleans) and `cleanBooleanValue` (accepts `{yes, on, enabled, active}` as `true`) have inconsistent contracts. After Stream 7 #17 fix, `cleanBooleanValue` only accepts literal `true`/`false`, which matches the closed-domain output. The plan should document this new contract explicitly.

**Why it is necessary:** Without documentation, a future contributor might "helpfully" reintroduce `yes/on/enabled/active` in `cleanBooleanValue` thinking it's robust input handling. Document the contract to prevent regression.

### Comment 17 — ✅ Accept (necessary)

The plan's "save the migrated copy under `input-fetch-registry.YYYYMMDD.bak.yaml`" is rollback insurance, not migration acceptance. Add a measurable acceptance criterion: pre/post mapping count diff, set of services that lost case-variants, set of `*/query` endpoints removed, framework's MS measurement between pre- and post-migration registries.

**Why it is necessary:** "The migration ran" is not a passing test. Without acceptance criteria, there's no way to detect a partially-failed migration.

### Comment 18 — ✅ Accept (necessary, trivial)

`SmartFetchAuthManager.java:94-95` uses the same hardcoded 10-second connect+read timeout. After Stream 4 splits these for the discovery path, the auth manager should consume the same config keys. Trivial 5-line change but easy to miss.

**Why it is necessary:** Hardcoded timeouts in two adjacent classes is exactly the kind of inconsistency that surfaces six months later as "why does my LLM-side timeout work but my auth-side doesn't?"

### Comment 19 — ✅ Accept (necessary)

The reviewer correctly noted that `SmartInputFetcher.java:1288-1302` also calls `loadRegistry()` in the constructor. So my plan's "cache in `SmartLLMParameterGenerator`" only addresses the helper; the fetcher constructor still parses on every `new SmartInputFetcher(...)`. Multiple instances → multiple cached views → drift.

**Why it is necessary:** Half-fixing the per-call reload still leaves a per-fetcher reload. Make the cache process-wide singleton scoped by `registryPath` so all consumers share one view.

### Comment 20 — ✅ Accept (necessary)

Re-grep confirmed: my plan miscites lines 859 and 2920; the actual locations are 574, 999, 1593, 2080, 3281, 3725, 4007, 4154 plus the related 1022 and 3516 buffers. A contributor reading "fix at :859" will find no `2044` reference and waste time.

**Why it is necessary:** Pure correctness — citation accuracy.

### Comment 21 — 🟡 Partial accept (binary assertion, not 90%-reduction benchmark)

A JMH harness is overkill for this scope. But the plan's "drops 90%" claim is unverifiable without one. Replace with a simpler invariant: "registry is loaded at most once per fetcher lifetime; counter-based assertion in the integration test."

**Why it is partially necessary:** The 90% claim is hand-wave; the binary "loaded once" is rigorous and cheap to assert. Drop the perf claim, keep the invariant.

### Comment 22 — ✅ Accept (necessary)

The reviewer correctly noted that after Stream 4 caches the registry, every consumer holds the full 51K-line / ~1.6 MB error history in memory — including the 49 KB Lorem-ipsum payload that dataflow-map § 6.5 documented. Cap `parameterErrors` at 50 entries per endpoint with FIFO eviction; cap individual `errorReason` at 1024 chars to bound memory.

**Why it is necessary:** Without a cap, the cached registry grows unbounded across runs as long-tail probes accumulate errors. The 1024-char reason cap also prevents a single OVERFLOW probe from inflating the YAML by 25%.

### Comment 23 — ✅ Accept (necessary, trivial)

One-line clarification: "normalization is `toLowerCase()` only — do NOT strip `ts-` prefix or numeric suffixes." Saves a future contributor from collapsing `ts-travel-service` and `ts-travel2-service` (they are distinct services).

**Why it is necessary:** Cheap insurance against an obvious next-mistake.

### Comment 24 — ✅ Accept (necessary)

The reviewer is right that the dependency is bidirectional (Stream 5 needs Stream 4's persistence to verify its KPI). Either bundle Streams 4 and 5 in one PR or document the mutual dependency clearly.

**Why it is necessary:** A linear dependency chain is wrong; if a contributor ships Stream 5 first without Stream 4, the recentness math change is invisible across runs and the KPI cannot be measured.

---

## Open questions — author response

The reviewer raised 10 open questions; my answers:

1. **Migration global-fallback tier:** Mandatory after Comment 2. Document read order: `<consumerApiKey>::<paramName>` first, bare-tier fallback second.
2. **JWT exp parsing:** OUT of scope (per Comment 4). Stream 6 only makes `tokenValidityMinutes` configurable.
3. **Test directory:** First PR creates `src/test/java/es/us/isa/restest/inputs/smart/`. Tests required to live there (mirroring source package structure per JUnit convention).
4. **Concurrency:** Stream 4 will harden `InputFetchRegistry` per Comment 5 (ConcurrentHashMap + ReadWriteLock).
5. **`saveRegistry()` after Priority-1 mutations:** IN scope of Stream 4 per Comment 1.
6. **TrainTicket-only registry vs. SUT-agnostic:** Migration script will be parameterized by SUT. The TrainTicket migration is the first; future SUTs follow the same recipe with their own service whitelist.
7. **`*/query` post-fix behavior:** Per Comment 7, `pickFirstReasonableEndpoint` is also gated. Stream 1 explicitly returns null when no real, parameter-relevant endpoint matches.
8. **Stream 3 site coverage:** All 7 sites (per re-grep, not the 8 in the audit). Comment 8 expanded.
9. **Trace producer endpoint persistence:** Per Comment 10, session-scoped semantics preserved BUT per-run JSON log added so framework S7 TPB is computable.
10. **Acceptance baseline counts:** Re-baselined per Comment 3 verification. Plan now uses 56 (`*/query`), 27 (literal `{...}`), 130/175 = 74.3% (`successRate=0.0`), 7 (`paramName.contains("id")` sites).

---

## Plan revision impact

The reviewer's accepted comments add the following to the plan:

| Stream | Effort delta | Reason |
|---|---|---|
| 1 — Data hygiene | +0.5 d | Mandatory fallback tier (C2), `pickFirstReasonableEndpoint` gating (C7), migration acceptance metrics (C17), `toLowerCase()`-only note (C23) |
| 2 — Path subst | +0.5 d | Option (c) strip-trailing-`{...}` becomes primary; option (a) skip is fallback (C9) |
| 3 — ID semantics | +0.25 d | Cover all 7 sites instead of 3 (C8) |
| 4 — Hot path | +1.0 d | Persist Priority-1 mutations (C1), atomic write (C6), concurrency hardening (C5), broader cache scope (C19), `parameterErrors` size cap (C22) |
| 5 — Score math | +0.25 d | More tests (C13); merge with Stream 4 in single PR (C24) |
| 6 — Auth | +0.5 d | Concurrency guard (C4); cross-reference Stream 4 timeouts (C18) |
| 7 — Validation | -0.0 d (move-only) | Drop #29 (C14), move #21 here from Stream 8 (C15), document `cleanBooleanValue` contract (C16) |
| 8 — Cleanup | +0.5 d | Delete `servicePatterns` field/defaults/YAML (C12), correct file:line list for #10 (C20), drop perf-90% claim, keep binary invariant (C21) |
| Cross-cutting | +1.0 d | Create test directory + integration test surface (C11) |

**New total effort:** 11 d → ~14.5 d. The 14.5 d is still feasible for a two-engineer team in 2 weeks (each at ~80% capacity), or one engineer at ~3.5 sprint weeks.

**Revised dependency map** (per Comment 24):

```
Stream 1 (data hygiene) ──▶ Stream 2 (path subst) ──┐
                       └──▶ [bundle] Stream 4 ↔ Stream 5 (perf+score, mutual) ─┐
                       └──▶ Stream 6 (auth, can run parallel after C4 scoping)─┤
                                                                                ▼
Stream 3 (ID semantics) ───────────────────────▶ Stream 7 (validation) ──▶ Stream 8 (cleanup)
                                                                                ▲
                                                              Cross-cutting test surface (C11)
```

---

## Verdict

**The reviewer's critique was high-quality and substantively correct.** Nineteen comments accepted fully, five partially, none rejected. The plan will be revised before kickoff. The most important changes:

- Streams 4 and 5 bundle into one PR (mutual dependency).
- Stream 1 gains mandatory fallback tier and `pickFirstReasonableEndpoint` gating.
- Stream 4 gains four sub-rows (persist Priority-1, atomic write, concurrency guard, parameterErrors cap).
- Cross-cutting integration tests added; test directory creation in first PR.

The reviewer's evidence-based discipline — re-grepping every claim — caught five baseline-count errors that would have undermined post-fix verification. That alone justifies the review pass.

If the reviewer wants a follow-up, Comment 22's "size cap on `parameterErrors`" is the one detail I want them to ratify (50 entries / endpoint, 1024-char reason) — the audit didn't propose specific numbers, and I'm picking based on dataflow-map § 6.5 evidence alone.
