# debug/inputs/smart_fetch — RESTest Smart Input Fetch Subsystem Audit

Date: 2026-05-05
Branch: `inject-detection`
Author: Claude (Opus 4.7)

This folder contains a deep audit of the **Smart Input Fetch** subsystem of RESTest's MST mode — the code under `src/main/java/es/us/isa/restest/inputs/smart/` plus the persisted registry at `src/main/resources/My-Example/trainticket/input-fetch-registry.yaml`. It was produced in response to the question "*Check our smart fetch for our tool to see if anything in there make sense or not, or if there are something we need adjust, refine, or anything*".

The audit is evidence-backed: every finding cites a specific `file:line` in the source tree, a specific entry in the persisted registry YAML, or a peer-reviewed academic source.

## Contents

| Document | What it answers |
|---|---|
| [`smart-fetch-bug-audit.md`](./smart-fetch-bug-audit.md) | 40 findings ranked by severity (4 Critical / 12 High / 18 Medium / 6 Low), each with file:line, code excerpt, bug, impact, evidence, and fix sketch. |
| [`smart-fetch-quality-framework.md`](./smart-fetch-quality-framework.md) | 7 metric families (Service Discovery, Endpoint Selection, Value Extraction, Cache Effectiveness, Learning Convergence, End-to-End Yield, Trace-Awareness), 17 metrics, 47 numbered citations (19 marked **[recent]** 2024–2026), 6-step measurement protocol, KPI thresholds. |
| [`dataflow-map.md`](./dataflow-map.md) | Implementation-reality call graph. 15 distinct LLM prompts, 24 mutable shared-state fields, 10 documented discrepancies between `flow.md` and the code. |
| [`refinement-plan.md`](./refinement-plan.md) | Distilled, prioritized work plan derived from the bug audit. Groups the 40 findings into 8 themed work-streams with rationale, expected impact, dependencies, and effort estimate. **This is the action document.** |
| [`reviewer-feedback.md`](./reviewer-feedback.md) | Independent reviewer's critique of `refinement-plan.md` with each comment + my disposition (accept / partial / reject) and justification. |

## How to use this folder

- **Reading order for understanding the subsystem**: `dataflow-map.md` → smart-fetch sections of `src/main/resources/My-Example/trainticket/flow.md` (lines 785–830, 1029–1043, 1198–1215) → `smart-fetch-bug-audit.md`.
- **Reading order for fixing bugs**: start at `refinement-plan.md` (themed plan) → drill into individual findings via `smart-fetch-bug-audit.md`. Each finding has a Severity tag, a fix sketch, and is reachable by Finding-N anchor.
- **Reading order for measuring quality**: `smart-fetch-quality-framework.md` § 1 (scope) → § 2–4 (metric families) → § 5 (operational protocol).

## Top-5 highest-impact findings

| Rank | Finding | Why critical |
|---|---|---|
| 1 | **`ApiMapping.calculateScore()` recentness math is broken** (`ApiMapping.java:73`) | `LocalDateTime.compareTo / 86400` returns ~0 always, so the freshness axis of the ranking is dead. 75% of registry mappings are stuck at `successRate: 0.0` partly because of this. |
| 2 | **`NO_GOOD_MATCH` sentinel persisted as a service** (`SmartInputFetcher.java:378-418` + `:3939`) | Two registry entries (`service: "NO_GOOD_MATCH"` at YAML lines 91, 275) plus 60+ fabricated `*/query` endpoints. Sentinel even leaked into request payloads and surfaced as Jackson `int` deserialization failures (registry YAML 32643, 47271). |
| 3 | **Path-parameter endpoints are persisted literally** (registry YAML 105, 580, 595, 871, 917; fetched at `SmartInputFetcher.java:423-431`) | Endpoints like `/api/v1/orderservice/order/{orderId}` are GET'd literally, returning 404, marking the mapping as failed, and never re-discovered. |
| 4 | **Per-parameter 51K-line YAML reload** (`SmartLLMParameterGenerator.java:213`) | `InputFetchRegistry.loadFromFile(registryFile)` is called on every parameter generation, parsing a 1.6 MB YAML each time. Profile this on a 100-parameter run and you will see seconds of wall time burned. |
| 5 | **2044-character LLM prompt cap baked into 8+ sites** (`SmartInputFetcher.java:574, 1593, 2080, 2920, 3281, 3725, 3725, 4007, …`) | This is a GPT4All limit. Production runs use Ollama `qwen2.5-coder:14b` with a 32 K context. The cap is silently dropping useful schema context and forcing weaker fallbacks. |

For the remaining 35 findings, see `smart-fetch-bug-audit.md`. **All 40 are unfixed in code; this audit is the first pass.**

## The seven metric families of the smart-fetch quality framework

| Family | Metrics | Why smart-fetch–specific |
|---|---|---|
| **S1 Service Discovery** | SDP (precision), SDR (recall) | Fraction of LLM-discovered services that actually own the parameter, vs. ground-truth set from OpenAPI tag/server URLs and Jaeger producers. |
| **S2 Endpoint Selection** | ESHR (HTTP success rate of selected endpoints) | Conditional on a correct service, did the picked endpoint return 2xx? |
| **S3 Value Extraction** | DEHR (direct-extraction hit rate), SFM-F1 (semantic-field-match F1) | Did the LLM pull a real value out of the response, or did it return JSONPath / explanatory text? |
| **S4 Cache Effectiveness** | CHR (hit rate), CVD (value diversity), TSR (TTL stale rate) | Distinct from D8 — evaluates the **smart-fetch internal** caches, not the test-suite-wide input distribution. |
| **S5 Learning Convergence** | REC (EMA recovery), MS (mappings saved per run) | Does the registry stabilize over runs, or does it churn? |
| **S6 End-to-End Yield** | yield_smart (fraction served by smart-fetch, not LLM fallback) | The headline number — what fraction of inputs were grounded in real upstream data? |
| **S7 Trace-Awareness** | TPB (trace-priority benefit) | Does Priority-0 (trace-observed endpoints) actually beat Priority-1 (registry mappings)? |

## What is intentionally out of scope

- **D1–D10 input-quality metrics** are covered by the parent framework (`debug/inputs/input-quality-measurement-framework.md`). This framework adds *smart-fetch–specific* metrics that the parent does not address.
- **MST orchestration / scenario shattering / generator-side priority chain** is documented in `debug/inputs/dataflow-map.md`. This `dataflow-map.md` covers only the smart-fetch internals.
- **LLM model selection / hosting** is in `flow.md` § "LLM Communication Path".
- **Negative test selection / 8 fault types / hardcoded invalid input pool** is in `debug/inputs/pipeline-bug-audit.md` (already audited).

## Reproducibility

All findings can be re-verified:

- **Bug audit**: each finding lists exact `file:line(s)`. Use `Read` or open in your editor. Five spot-checks were already verified: `compareTo(lastUsed)` at `ApiMapping.java:73`, `service: "NO_GOOD_MATCH"` at registry YAML 91/275, path-templated endpoints at 105/580/595/871/917, `Math.max(5, 10)` at `SmartInputFetcher.java:1999`, per-parameter `loadFromFile` at `SmartLLMParameterGenerator.java:213`.
- **Quality framework**: every metric has a citation, a formula, and a "How to compute in RESTest" section that points at the relevant log directory or source method. The proposed `SmartFetchEventLogger` is described in detail; ten of the seventeen metrics are computable today by emitting structured logs from existing `log.info` sites.
- **Dataflow map**: every box in the Mermaid diagram is annotated with the file:line where the corresponding code lives. The 10 flow.md ↔ code discrepancies each cite both the documentation paragraph and the code lines that contradict it.
- **Refinement plan**: every work-stream cross-references the originating finding numbers in `smart-fetch-bug-audit.md`. The reviewer agent's critique was applied — see `reviewer-feedback.md` for the disposition table.

If any reader believes a finding is wrong, please grep for the cited line, fetch the cited paper, or run the bench. The audit was deliberately ruthless — false positives are possible and welcome.
