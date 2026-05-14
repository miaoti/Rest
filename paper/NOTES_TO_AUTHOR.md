# NOTES_TO_AUTHOR — ICSME 2026 Tool Demo

> Living document. Read top-to-bottom before each writing session. Resolve every `Q*` and `TODO` before submission.

---

## Phase 1 — Discovery report (preserved)

### Tool name (verbatim)

**MIST** — *Microservice Integration & Scenario Tester*. User-facing brand is **MIST**; the underlying implementation keeps **MST** in class names (`MstConfig`, `MstAuthHandler`, ...), config keys (`mst.config.path`, `mst.generate.only.first.step`), the `generator=MST` value, and `trainticket-mst.properties`. The brand layer is in the README, this paper, and the abstract; the code layer matches RESTest convention. Built on top of [RESTest](https://github.com/isa-group/RESTest).

### One-sentence elevator pitch (synthesis)

MIST turns OpenTelemetry/Jaeger traces and OpenAPI specs into runnable, cross-service workflow tests with LLM-generated parameters, single-fault sniper mutations on root APIs, and trace-aware oracles, replayed against microservice systems that today's REST API testing tools handle only weakly.

### Architecture (5 components, mapped to source)

| Paper-side name | Repository class / module | Path |
|---|---|---|
| (i) Spec Ingestor | `OpenAPISpecification` + `MicroserviceTestConfigurationGenerator` | `src/main/java/.../specification/`, `.../configuration/multiservice/` |
| (ii) Semantic Dependency Registry + JIT Binder | `SemanticDependencyRegistry`, called from `MultiServiceTestCaseGenerator.traverse(...)` | `src/main/java/.../registry/` |
| (iii) Sequence Generator (Root API Mode) | `TraceWorkflowExtractor` (5 phases) → `MultiServiceTestCaseGenerator` (gated by `mst.generate.only.first.step=true`) | `.../workflow/`, `.../generators/MultiServiceTestCaseGenerator.java` |
| (iv) Sniper Mutator | Round-robin negative mode (`faulty.round-robin=true`); pool: `HardcodedInvalidInputGenerator`; selection: `MultiServiceTestCaseGenerator` | `.../inputs/HardcodedInvalidInputGenerator.java` |
| (v) Trace-Aware Oracle | Jaeger fetch in writer-emitted code; `TraceErrorAnalyzer`; `SoftErrorRuleCache`; `FaultDetectionTracker` | `.../writers/restassured/MultiServiceRESTAssuredWriter.java`, `.../analysis/`, `.../validation/` |

### Data flow (8 stages)

Entry → spec+traces → 5-phase scenario pipeline (Phase 1 cross-trace data merge → Phase 2 session merge → Phase 2.5 dedup → Phase 3 component shattering → Phase 4 baseline decomposition) → variant generation → test emission → in-process execution → optional enhancer loop → reporting.

### TrainTicket integration evidence

| Asset | Path | Stat |
|---|---|---|
| Merged OpenAPI spec | `src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml` | 265 operations |
| MIST test configuration | `src/main/resources/My-Example/trainticket/real-system-conf.yaml` | 37 `ts-*-service` entries (black-box-discovered) |
| Recorded traces | `src/main/resources/My-Example/trainticket/test-trace/` | 1 file: `traces-1772605095842.json` |
| Injected-fault registry | `injectedFaults/injected-faults.json` + `INJECTED_FAULTS.md` | 10 faults across 5 services |
| Bundled config | `trainticket-demo.properties` + `trainticket-mst.properties` | Public TrainTicket: `http://129.62.148.112:32677` |
| Algorithm doc | `flow.md` | 1672 lines |

### Suggested figures (final state)

| # | Figure | Source |
|---|---|---|
| 1 | Architecture diagram (5 components, dotted SUT boundary) | `paper/figures/architecture.tex` (TikZ, generated) |
| 2 | Real captured Jaeger trace (gateway → admin-route → route → MySQL) from this repo's allure-results | `paper/figures/trace_oracle.tex` (TikZ, faithful to trace `032af4e6...f480`) |
| 3 | (Omitted in v1 draft) CLI screenshot — see Q9 |

---

## Phase 1 questions — answers and resolution status

| # | Resolved? | Notes |
|---|:---:|---|
| Q1 | ✅ | Service count framed as "37 black-box-discovered services" — written that way in §3 |
| Q2 | ⚠️ partial | Author confirmed 10/10 detection on the most-current run, **but that run's report is not on this machine.** The local most-recent report (`fault-detection-summary-...1778039778981-20260506-172440.txt`) shows 4/10. Table I cites 10/10 per author confirmation; the per-fault \textbf{Mechanism} column is left as `\todo{mech}` until the current report is on this machine. See Inconsistencies §1 below. |
| Q3 | ✅ | Variant count `2733` extracted from the local-most-recent report. Run time `~20h, ~12h LLM` written verbatim per author. **If the most-current run has different numbers, replace 2733 before submission.** |
| Q4 | ✅ | Canonical model identifier in the codebase: `deepseek-chat` (in `trainticket-mst.properties` and `deepseek-config.properties`). Wrote `DeepSeek deepseek-chat` in §5. The author's "v4flash" appears to be colloquial; not used in the paper. |
| Q5 | ⚠️ partial | Used real captured trace `032af4e629d9075292977fc74a20f480` from `src/main/resources/My-Example/trainticket/allure-results/00fc19f1-...txt`. It is a real MIST/TrainTicket capture from this repo. **It is not from the most-current 10/10 run** (that run's allure data is on the other machine). The figure caption is honest about this. If a fault-detection trace from the current run is available, swap it in. |
| Q6 | ✅ | `paper/refs.bib` populated; entries with uncertain DBLP keys carry a `note = {TODO-...}` line. Verify each on DBLP before submission. |
| Q7 | ✅ | `\author{}` block uses `\todo{AUTHOR NAMES}` and `\todo{AFFILIATION, EMAIL}`. Single-anonymous track (authors visible) — fill before submission. |
| Q8 | ✅ | `https://github.com/miaoti/Rest/` written in abstract and §6. |
| Q9 | ⚠️ pending | Abstract ends `Screencast: \url{\todo{SCREENCAST-URL}}`. Record + replace before submission. |
| Q10 | ✅ | DOI omitted from this draft (no Zenodo plan). Add for camera-ready if accepted. |
| Q11 | ✅ | Ablation dropped (paper has no ablation section). |
| Q12 | ✅ | Author placeholder per Q7. |

---

## Inconsistencies found (between artifacts on this machine)

### §1. Fault detection rate: local report says 4/10, author says 10/10

The most recent fault-detection report file on this machine is:

```
logs/fault-detection-reports/fault-detection-summary-trainticket_twostage_test_1778039778981-20260506-172440.txt
```

It reports:
- Total Test Cases: **2733**
- Detected Faults: **4 (40.0%)** — INSUFFICIENT_STATIONS_FAULT, INVALID_CONTACTS_NAME_FAULT, INVALID_SEAT_NUMBER_FAULT, INVALID_STATION_LENGTH_FAULT
- Undetected Faults: **6** — INVALID_PRICE_RATE_FAULT, INVALID_ROUTE_ID_FAULT, INVALID_STATION_NAME_FAULT, INVALID_STATION_NAME_LENGTH_FAULT, INVALID_TRIP_ID_FORMAT_FAULT, INVALID_TRIP_ID_LENGTH_FAULT

The author has confirmed via direct message that the 10/10 detection result is from a more recent run that is **not on this machine**. The paper goes with 10/10 per author truth.

**Action before submission:**
1. Copy the most-current fault-detection report onto this machine.
2. Verify the 10/10 number and the run identifier; update the footnote in §5 ("fault-detection report `trainticket_twostage_test_1778039778981`") to the current run's identifier if different.
3. Fill the **Mechanism** column of Table~\ref{tab:faults} from the current report, drawing each row from one of: HTTP $\geq 4xx$, LLM soft-error rule, Jaeger error span, injected-fault registry match.

If the current report does not exist as a structured artifact in the runtime, it is also acceptable to re-run the bundled demo on this machine to re-derive it; that run is reproducible by construction (same .properties file, same OAS spec, same trace input, deterministic with `random.seed=...`).

### §2. Field name: `injected` (code) vs `isInjected` (docs)

`MultiServiceRESTAssuredWriter.java:1820` reads `dataObj.optBoolean("injected", false)` from the response body. `INJECTED_FAULTS.md` documents the same field as `isInjected`. Verified via the captured Allure attachment `00487ff8-...txt`: the actual response uses `"injected":true` (matching the code, not the doc). The doc is wrong; the code is right. **Action**: fix `INJECTED_FAULTS.md` to use `injected` to match the deployed contract. (Low priority for the paper; high priority for repository hygiene.)

### §3. Variant count: 2733 in local report

The 2733 figure used in §5 footnote comes from the local-most-recent report. The most-current run (the 10/10 run) may have a different variant count. Re-extract before submission.

---

## Phase 2 — TODO ledger

### Resolved during writing

- All five §3 component descriptions written from the actual code paths
- Figure 1 (architecture) generated as TikZ
- Figure 2 (trace-as-oracle) drawn from a real captured Jaeger trace in this repo, with honest caption
- Algorithm 1 (Sniper) written from the actual `MultiServiceTestCaseGenerator` round-robin code path
- Bibliography populated; uncertain entries marked `note = {TODO-...}`
- Citation count: 14 entries — well within the ~20 budget

### Still open in `paper/main.tex`

- `\todo{AUTHOR NAMES}` and `\todo{AFFILIATION, EMAIL}` in `\author{}` block
- `\todo{SCREENCAST-URL}` in the abstract's last sentence
- 10 × `\todo{mech}` in Table~\ref{tab:faults} mechanism column

### Asset gaps

- Most-current fault-detection report (Q2)
- Most-current variant count (Q3) — currently 2733 from local report
- Most-current run identifier — currently `trainticket_twostage_test_1778039778981`
- (Optional) Allure CLI screenshot for an optional Figure 3

### Verification list (re-check before submission)

- 265 operations in OpenAPI spec — counted directly from the YAML
- 37 black-box-discovered services — counted from `real-system-conf.yaml`
- 10 injected faults — counted from `injectedFaults/injected-faults.json`
- 5 services with injected faults — per `INJECTED_FAULTS.md`
- 8 fault categories (TYPE_MISMATCH, REGEX_MISMATCH, SEMANTIC_MISMATCH, OVERFLOW, EMPTY, NULL, SPECIAL_CHARACTERS, BOUNDARY_VIOLATION) — counted from `flow.md`
- DeepSeek model = `deepseek-chat` — verified in two config files
- 2733 variants — only from local report, may need update
- ~20h / ~12h LLM split — author-stated, not auto-extractable from artifacts on this machine

---

## Phase 2 follow-up — author review fixes (5 risks + 1 polish)

Author flagged five reviewer-bait issues plus one positive polish on the Phase 2 draft. All addressed:

| Risk | Fix applied |
|---|---|
| **R2** — Figure 2 (happy-path GET trace, all-200 spans) did not match §2 (POST /adminbasic/prices, INVALID_PRICE_RATE_FAULT). | Located a real fault-injecting trace in this repo's Allure attachments: trace ID `d4c577d47bcba04a00ef9b3edcbcacf6`, test `test_negative_POST_1_81` against `POST /adminrouteservice/adminroute`, response body `{"status":0,"msg":"start or end station not include in stationList.","data":null}` with HTTP 200 on every span. Redrew Figure 2 from this real trace and rewrote §2 to match. The new §2 example is **honest** about HTTP 200 (the previous draft incorrectly claimed INVALID_PRICE_RATE_FAULT returns 200; per `INJECTED_FAULTS.md` and the corresponding captured trace `ba375a3372e88399a1f8b4a1c40a8795`, that fault actually returns HTTP 400 -- another inconsistency, see §4 below). |
| **R3** — "black-box auto-discovery" framing in §3.(i). | Replaced with: "Parses an OpenAPI specification --- obtained from the SUT operator or aggregated from per-service `/v3/api-docs` endpoints --- and emits a multi-service test configuration. The bundled TrainTicket spec contains 265 operations across 37 REST-exposed services, defining MIST's test scope. Components without a discoverable HTTP surface (asynchronous workers, message brokers, internal-only RPC services) lie outside any black-box tool's reach by construction." |
| **R4** — §4.2 "ablation-style use" sentence invited reviewers to ask for an ablation we don't have. | Changed to: "This paper reports only the root-only mode." |
| **R5** — 12h LLM vs "cache caps at $\sim$2 calls per API" looked like bad arithmetic. | §5 rewritten to clarify cost distribution: the bulk of LLM cost is positive-input synthesis (one prompt per parameter per variant under `testsperoperation=100`), and the soft-error rule cache contributes a one-time $\sim$2 prompts per distinct API. Reviewer can now do the arithmetic and it lands. |
| **Polish** — three contributions previously stood alone; reviewer could attack each as incremental. | Added one sentence at the start of §4: "The Sniper Strategy's single-fault discipline is what makes Trace-as-Oracle's downstream signals causally interpretable; Root API Mode's external-only driving is what allows traces to be the primary oracle without confounding from direct internal calls. Either of the three on its own is incremental; the combination is what closes the oracle and state-fabrication gaps together." |

## Inconsistencies found (post-Phase-2-review additions)

### §4. The previous §2 motivating example fabricated "HTTP 200" for `INVALID_PRICE_RATE_FAULT`

In the Phase-2 first draft, §2 said `POST /adminbasic/prices` with a non-positive rate "returns *HTTP 200 OK*". This was wrong: per the bundled `INJECTED_FAULTS.md` and the captured trace `ba375a3372e88399a1f8b4a1c40a8795`, that fault actually returns HTTP **400** with the soft-error envelope. The error originated from carrying over the user's earlier framing without verifying against the real captured response body and trace.

Fix: §2 was rewritten around `POST /adminrouteservice/adminroute` with a soft-error response that *is* genuinely HTTP 200 (verified: real test `test_negative_POST_1_81`, real trace `d4c577d47bcba04a00ef9b3edcbcacf6`). The motivating example is now backed by data on this machine.

Lesson for the rest of the paper: every concrete claim about the SUT's response shape should be re-checked against the captured Allure attachments before submission. There may be other small claims that drifted from the data (e.g., "every span is 200" -- verified for trace `d4c577d4...acf6`; if some other fault path has a different shape, the §4.3 bullet on "no descendant carries error frames" is correct only for this case).

---

## Phase 3 — Self-review

### Estimated page count

Counting the IEEEtran two-column 10pt layout, lines per element:
- Abstract: ~7 lines column-equivalent
- §1 Introduction: ~25 lines
- §2 Background: ~14 lines + 4-line listing
- §3 Architecture: ~37 lines + Figure 1 (full-page-width, ~14 lines)
- §4.1 Sniper: ~22 lines + Algorithm 1 (10 lines)
- §4.2 Root API Mode: ~14 lines
- §4.3 Trace-as-Oracle: ~16 lines + Figure 2 (full-page-width, ~16 lines)
- §5 Case study: ~22 lines + Table I (12 lines)
- §6 Related Work + Conclusion: ~22 lines
- Bibliography: 14 entries, ~4 lines each = ~56 lines

Total estimate: roughly **5.0–5.2 pages** with both figures spanning full width. **Tight.** First trims if over budget:
1. Drop the "End-to-end flow" paragraph at the bottom of §3 (~5 lines, redundant with Figure 1).
2. Compress Figure 2 caption from 4 sentences to 2.
3. Drop one or two of the four bullet points in §4.3 (the Trace-as-Oracle assertion families).
4. Move the soft-error cache sentence in §4.3 into a footnote.
5. Compress Algorithm 1 from 9 lines to 6 (merge lines 5–7 into one line).

Do **not** cut: the motivating example in §2 (it is the visual hook), Figure 1 (the architecture is the demo), Figure 2 (the trace-as-oracle visual), or the BibTeX entries for the eight prior REST-API-testing tools (positioning is required for tool-demo review).

### ICSME tool-demo review criteria

1. **Value, usefulness, and reusability of the tool.** *Strong.* Three named contributions (Sniper / Root API Mode / Trace-as-Oracle), open-source code, bundled demo with one-command launch, configurable LLM backend, fault-detection report. *Weak.* Table I has `\todo{}` cells until the current report lands. Soft-error cache adds an LLM dependency that some reviewers may flag as a reproducibility hazard — defended in §4.3 by the cache's bounded call count.

2. **Quality of presentation.** *Strong.* Two real figures, one algorithm, one results table; concrete CLI invocation in §5; concrete numbers (265 ops, 37 services, 10 faults, 2733 variants, 20h, 12h LLM) throughout. *Weak.* Three `\todo{}` blocks must be filled. Figure 2 caption is honest about source asymmetry.

3. **Clarity of relation with previous work.** *Strong.* Eight prior REST API testing tools cited with one-sentence positioning each. The "first to use traces as primary oracle for REST API testing" claim is conservatively stated and defensible. *Weak.* Trace-based testing in OTHER domains (microbenchmarks, anomaly detection) is broader than the §6 paragraph implies; if a reviewer flags this, the rebuttal is "we restrict the claim to REST API testing tools, which we cite eight of."

4. **Availability of the tool.** *Strong.* Public GitHub URL, LGPL 3.0 license inherited from RESTest, one properties file launches the bundled demo, requirements documented. *Weak.* Screencast URL is `\todo{}`. Java 11 requirement may exclude readers on JDK 21+ — `mvn -DskipTests compile` is verified on JDK 21 (this session), but the runtime path uses Java 11 as compile target. If a reviewer cannot run on their JDK, that is a footgun; consider testing on JDK 17 + JDK 21 before submission.

### Top 5 reviewer objections + rebuttal plan

1. **"`\todo{mech}` in Table I is unprofessional."** *Rebuttal:* the cells will be filled from the current report; in this draft they are explicit gaps for author review. Camera-ready will be clean.

2. **"How is this different from RESTest? Just an MST mode?"** *Rebuttal:* §3 makes the boundary explicit ("MIST is built on top of RESTest's MST mode"), and the three named contributions (Sniper Strategy, Root API Mode, Trace-as-Oracle) are independent of the underlying generator framework — the reviewer should evaluate those as the contribution, not the codebase split.

3. **"Trace-based oracles are not new (cite Dapper, OTel, anomaly detection literature)."** *Rebuttal:* §6 stipulates the claim is "first openly available REST API testing tool that uses span-level evidence as a primary oracle." The novelty is the integration into a REST testing flow, not the invention of distributed tracing.

4. **"20-hour run time is unreasonable."** *Rebuttal:* §5 itemizes the cost as 12h LLM inference latency + 8h pipeline. The Sniper Mutator and Root API Mode are LLM-free; the cost is in input synthesis and soft-error validation. The soft-error rule cache caps the LLM cost at ~2 calls per distinct API for validation; remaining LLM cost is variant generation, which is configurable via `testsperoperation` and `faulty.ratio`.

5. **"Why only 10 faults? Industrial systems have hundreds."** *Rebuttal:* the 10 are representative across the documented TrainTicket validation gates and the 8 fault categories that MIST's Sniper Mutator generates. They are designed to exercise each mutation type at least once. We do not claim exhaustive coverage; we claim the architecture detects the 10 documented faults.

---

## Pre-submission checklist

- [ ] Paper compiles cleanly with the official `IEEEtran.cls` (placeholder removed)
- [ ] PDF is exactly 5 pages including references — first proof in TeX, run trims if needed
- [ ] Abstract ≤200 words, ends with `Screencast: <URL>` (Q9 resolved)
- [ ] Every `\todo{...}` in `main.tex` resolved (currently: AUTHOR NAMES, AFFILIATION+EMAIL, SCREENCAST-URL, 10× mech)
- [ ] Q1–Q12 all answered above
- [ ] All citations resolved (`note = {TODO-...}` removed from refs.bib)
- [ ] Repository URL public or reviewer-accessible link in place
- [ ] Screencast URL embedded
- [ ] LICENSE in repo (LGPL 3.0 inherited from RESTest — confirm acceptable for ICSME)
- [ ] First-author affiliation + email correct
- [ ] No reviewer-trigger phrases ("perfect accuracy", "all values 1.000", "we solve X", "novel/elegant/powerful")
- [ ] Bibliography uses IEEEtran style; final entry count ≤20
- [ ] Figures are vector or ≥300 dpi raster — both figures are TikZ, vector by construction
- [ ] All figure captions self-contained
- [ ] Most-current fault-detection report copied onto submission machine and Table I mechanism column populated
- [ ] Inconsistency §2 (`injected` vs `isInjected`) fixed in repo's `INJECTED_FAULTS.md` if author chooses
