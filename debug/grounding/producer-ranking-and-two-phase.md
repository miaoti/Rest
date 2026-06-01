# Grounding validity: producer-ranking fix (B) + two-phase completion (A)

> 2026-06-01. Researched (3 parallel investigations: two-phase code, producer-ranking code, external literature).
> Context: single-phase grounding raised *coverage* 14%→~99.7% but NOT *validity* — `endStation` grounded to
> `GaoTieOne` (from the wrong `trains` producer) → SUT 400 "some station not exists". Generic-reliable value
> grounding is an **open problem** (RESTGPT/LlamaRestTest/AutoRestTest territory) and **not our main contribution**
> (ours = trace-shape oracle / hidden-downstream / attribution). This plan fixes the parts that are genuinely
> *our* defects, correctly, without trying to solve the open problem. [[project_phase2_attribution_gap]]

## Empirical ground truth (live SUT, with admin auth)
- `endStation=nanjing` / `shinkansenbullettrain` (real `/stations`) → **200** "Save and Modify success".
- `endStation=GaoTieOne` (what ranking picked, from `trains`) → **400 "some station not exists"** → positive FAILS.
- `endStation=12345678901` (DB pollution, *is* in `/stations`) → **200** (accepted-but-garbage).

## Root causes (file:line)
1. **Producer ranking has no validity signal and rewards the wrong producer.** Selection = `calculateScore()` desc
   (`SmartInputFetcher.java:423-426`), score `= 0.5·priority + 0.3·successRate + 0.2·recentness` (`ApiMapping.java:110-114`).
   At cold-start all `successRate=0` → priority+recentness decide → freshly-LLM-discovered `trains` (priority 9 via
   `llmDiscoveryPriority=7`+rank, age-0 recentness=1.0) beats correct `stations` (priority 8). Scores: trains 0.650 vs stations 0.420.
2. **Self-inflicted poison.** `updateSuccessRate(true)` is driven by the **local format check** `isValidValueForParameter`,
   not the SUT outcome (`SmartInputFetcher.java:441-443`). `GaoTieOne` passes the format check → trains cemented at
   `successRate 0.9892` in the live registry → future runs *prefer* the wrong producer. The success signal is meaningless
   (it measures "format-valid", which nearly everything is).
3. **Recentness rewards creation, not successful use** (`ApiMapping.java:112-113`): a same-day discovery gets max recentness.
4. **Two-phase Phase-A never captures.** `mst.two.phase.enabled` IS wired (`MistRunner.java:326-337` → `runTwoPhasePipeline:468-524`;
   the `MstConfigValidator:144` "reserved flag" comment is stale), but Phase A runs with the enhancer force-disabled →
   routes through `executeGeneratedTestsWithJUnit` which never calls `TestResultCapture.enableCapture()` nor
   `drainParameterObservationsToRegistry` (only `executeTestsWithCollector:1866` does) → VERIFIED_VALID stays empty →
   Phase B `preferVerifiedValues` (`MistGenerator.java:300-313`) falls back to the raw unvalidated pool.
5. **Execution feedback is value-keyed, not producer-keyed.** `markVerified` (`InputFetchRegistry.java:367`) records
   verified *values* per consumer-endpoint+param; it cannot currently raise/lower a *producer* `ApiMapping.successRate`.

## Field validation (this is standard, not novel — so fixing it is "supporting machinery")
- **Harvest 2xx values & reuse** = RESTler response-value reuse (ICSE'19); DeepREST `ResponseDictionary`, +1 reward (ASE'24,
  arXiv 2408.08594); AutoRestTest value-agent **+2 for 2xx** (ICSE'25, arXiv 2411.07098). Reward *sign* is goal-dependent:
  ARAT-RL penalizes 2xx for *coverage* (ASE'23, arXiv 2309.04583) — so frame our harvest as the *valid-input-construction* objective.
- **Producer ranking** = rank by a similarity/structural **prior** (RESTler name match; **AutoRestTest SPDG cosine similarity**;
  foREST tree-priority arXiv 2203.02906; Morest RPG arXiv 2204.12148) **then correct with execution feedback**. Name-only is
  known-insufficient (RESTler needs manual JSON-pointer disambiguation). Our fix (name-affinity prior + feedback) is in-line.
- Robust **cross-SUT** grounding under **data pollution / multiple producers** is **under-addressed/open** (Arcuri survey
  arXiv 2212.14604: "need to setup the right data into the databases"; no source claims it solved). So we treat it as
  best-effort supporting machinery and put novelty elsewhere. [[project_amain_viability_probes]]

## Fix B (cold-start producer ranking) — do now; small, correct, generic
1. **De-poison:** stop `updateSuccessRate(true)` on the local format-check pass (`SmartInputFetcher.java:443`). Format-validity
   is not producer-correctness. `successRate` becomes a real signal only once SUT feedback wires in (Fix A). Keep the
   failure penalties (`:454,:461`) — a producer yielding format-*invalid* values is legitimately demoted.
2. **Name-affinity prior:** in the ranking comparator (`SmartInputFetcher.java:423-424`) add `AFFINITY_WEIGHT·nameAffinity`,
   where `nameAffinity` = token/stem overlap between the parameter name and the candidate's service+endpoint tokens (reuse
   `stemService`). Additive, generic (no SUT hardcoding). Makes `endStation`→`stationservice` win at cold-start; degrades
   gracefully to "no boost" when no name matches (so `consignee`→`orderservice`-style cases are unaffected).
3. **Recentness gate:** in `calculateScore()` count recentness only when `successRate>0` (reward recent *successful use*,
   not recent *creation*) — kills the freshness bonus a fresh discovery currently enjoys.

## Fix A — A1 DONE (2026-06-01), A2 still open

**A1 (Phase-A capture) — implemented + verified.** Phase A (`forceDisableEnhancer=true`) now routes through
`executeWithEnhancement(..., rounds=0, ...)` instead of `executeGeneratedTestsWithJUnit` — one capture-enabled
`executeTestsWithCollector` execution (no retries) that `enableCapture()`s + `drainParameterObservationsToRegistry()`s.
Verified end-to-end on TT: `PHASE A: capture-only execution → INTER-PHASE: resetting → reloading verified pool →
PHASE B: negatives`, VERIFIED_VALID populated. Reviewer fix applied: `executeWithEnhancement`
gains an explicit `exploreStatusCodes` param (5-arg overload defaults it true, so existing callers and a
legitimate `rounds=0 + SCE-on` config are unchanged); the Phase-A capture call passes `false` so status-code
exploration does NOT inject error-seeking tests into the positives-only baseline. (Gating on `enhancerRounds>0`
would have wrongly coupled SCE to the retry-round count — they are orthogonal.)
**Known edge (accepted):** `executeWithEnhancement`'s outer catch falls back to non-capturing
`executeGeneratedTestsWithJUnit` on any exception → on a transient Phase-A failure the verified pool stays empty
and the suite re-executes once (graceful degradation to pre-A1 behaviour; shared catch, not hardened to avoid
risking the enhancer path). Opt-in: `mst.two.phase.enabled` stays default-off (~2× SUT execution + Phase A
re-pollutes the DB).

**A2 (producer-keyed feedback) — still open.** `markVerified` is value-keyed, not producer-keyed, so a 4xx does not
yet lower the *producer* `ApiMapping.successRate`. Until A2, Fix B's name-affinity carries cold-start producer
selection. Do A2 with the attribution leg.

### Original design notes
Route Phase A through a capture-enabled executor (`executeTestsWithCollector` with `enhancerRounds=0`) so it
`enableCapture()` + `drainParameterObservationsToRegistry()` → VERIFIED_VALID populates → Phase B `preferVerifiedValues`
uses only SUT-validated values. Keep `mst.two.phase.enabled` **opt-in** (it ~2× SUT execution). To let A also correct
*producer* ranking, extend feedback to be producer-keyed (lower `successRate` of the producer whose value got 4xx) — this
is the bigger piece; B's name-affinity covers the cold-start first run that A structurally cannot.

## Decision
- **B now** (3 surgical changes above): fixes the observed cold-start bug, field-standard, low blast-radius.
- **A as opt-in follow-on**: the durable self-correcting loop; gated by execution cost.
- Neither is claimed as a contribution — both are supporting machinery for the value-grounding open problem.

## Sources
RESTler ICSE'19 (patricegodefroid icse2019.pdf) · DeepREST arXiv 2408.08594 · AutoRestTest arXiv 2411.07098 ·
ARAT-RL arXiv 2309.04583 · foREST arXiv 2203.02906 · Morest arXiv 2204.12148 · LlamaRestTest arXiv 2501.08598 ·
Arcuri "Testing RESTful APIs: A Survey" arXiv 2212.14604. (Producer-ranking + two-phase code evidence: file:line above.)
