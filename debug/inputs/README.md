# debug/inputs — RESTest Parameter Input Generation Audit

Date: 2026-04-26
Branch: inject-detection
Author: Claude (Opus 4.7)

This folder contains a deep audit of the **parameter input generation pipeline** in RESTest's MST mode (Smart Fetch + LLM positive paths, plus the eight-fault-type negative path). It was produced in response to two questions:

1. **How do we measure the quality of generated test inputs?**
2. **What logical / pipeline bugs exist in the current implementation?**

All findings are evidence-backed: every claim cites a specific `file:line` in the source tree, a specific log artefact, or a peer-reviewed academic source.

## Contents

| Document | What it answers |
|---|---|
| [`input-quality-measurement-framework.md`](./input-quality-measurement-framework.md) | Q1 — A nine-dimension, 30+ citation framework with operational measurement protocols and a 10-KPI dashboard. Grounded in RESTler / EvoMaster / ARTE / Morest / RESTGPT / LlamaRestTest / Restats / NIST t-way / OWASP API top-10. |
| [`pipeline-bug-audit.md`](./pipeline-bug-audit.md) | Q2 — 27 evidence-backed bug findings, ranked by severity, with file:line citations, code excerpts, impact analysis, and fix sketches. Includes flow.md ↔ code discrepancies. |
| [`dataflow-map.md`](./dataflow-map.md) | Companion — Implementation reality call-graph (Positive first-step / non-first-step / Smart Fetch / LLM / Negative round-robin / Negative random). Tables of shared mutable state and invariant-enforcement points. Discrepancies with `flow.md`. |

## How to use this folder

- **Reading order for understanding the system:** `dataflow-map.md` → `flow.md` (in `src/main/resources/My-Example/trainticket/`) → `pipeline-bug-audit.md`.
- **Reading order for fixing bugs:** start at `pipeline-bug-audit.md`. Each finding has a Severity tag, a fix sketch, and a verification plan.
- **Reading order for measuring quality:** start at `input-quality-measurement-framework.md` § 9 (Concrete Measurement Protocol) and § 10 (Recommended KPI Dashboard).

## Top 5 highest-impact findings (from the audit)

1. **Critical — Log4j format-string bug** in 9 sites: `{:.3f}`, `{:>6}`, `{:.1f}` Python-style placeholders are not recognised by Log4j2; metrics/decisions log as literal garbage and field arguments shift, misreporting smart-fetch and JIT binding metrics.
2. **Critical — `extractJsonObjectFields` doc-promises dot-prefixing** but the code passes the flat map without prefix; nested JSON keys collide last-wins, corrupting `inputFields`/`outputFields` and producing false cross-trace producer matches in `mergeScenariosByDataDependency`.
3. **Critical — `cleanIntegerValue` silently corrupts** every long/decimal value to `"1"`. Uses `Integer.parseInt` (rejects int64), strips decimal points, leaves embedded dashes; on any failure returns `"1"`. Most realistic IDs become `1`, killing diversity for int64 parameters.
4. **High — `normaliseIdStem` regex matches English words**: `^.+(id|uuid)$` treats `paid`, `aid`, `void`, `valid`, `humid` as ID-like; bogus stems pollute the registry and trigger false JIT bindings.
5. **High — Boolean TYPE_MISMATCH always becomes valid `Boolean.FALSE`**: `parseTypedValue` calls `Boolean.parseBoolean` on whatever the LLM returns, coercing every type-mismatched value into a perfectly valid boolean. Negative tests for boolean params lose all fault-detection power.

For the remaining 22 findings, see `pipeline-bug-audit.md`.

## Five pillars of the quality framework

1. **Validity & Conformance** — Schema Conformance Rate (SCR), Hallucination Rate (HR), IPD-Satisfaction Rate, ARTE-style Realism Score, LLM-vs-Smart-Fetch Validity Delta.
2. **Diversity & Coverage** — Shannon entropy & Simpson index of the parameter pool, average pairwise edit distance, n-gram coverage, equivalence-partition coverage, domain-range coverage.
3. **Fault-Detection Effectiveness** — Injected-Fault Detection Rate, Mean-Time-To-First-Detection, per-fault-type effectiveness across the 8 RESTest types, mutation-score proxy, real-bug count.
4. **Negative-Test Robustness** — Rejection Rate, Silent-Acceptance Rate, 5xx rate, false-negative rate, behavioral-oracle score.
5. **Pool & API-Specific Coverage** — t-way combinatorial coverage (NIST IPOG / RestCT), Restats operation/path/parameter coverage, status-code coverage, request-graph edge coverage, pool entropy/redundancy/EHR/half-life curves.

The framework cites RESTler (ICSE 2019), EvoMaster (TOSEM 2019), RESTest (ISSTA 2021), ARTE (TSE 2023), Morest (ICSE 2022), RESTTESTGEN (ICST 2020), DeepREST (ASE 2024), RESTGPT (ICSE-NIER 2024), LlamaRestTest (FSE 2025), Mirabella (DeepTest 2021), Schemathesis, NIST SP 800-142, OWASP API Security Top 10, Vectara Hallucination Leaderboard, and HalluLens (ACL 2025). See `input-quality-measurement-framework.md` for the full bibliography.

## What is intentionally **out of scope** here

- This audit does **not** touch tests, build configuration, CI, or shared infrastructure.
- This audit does **not** propose code changes — it documents bugs and a measurement framework so the user can decide which to act on.
- The Test Case Enhancer, Smart Status Code Exploration, and Soft Error Rule Cache are mentioned only where they touch input generation; their separate quality concerns are not the focus of this folder.

## Reproducibility

All findings can be re-verified:

- Bug audit: each finding lists the exact `file:line(s)`. Use `Read` or open in your editor.
- Quality framework: every metric has a citation and an "How to apply to RESTest" subsection that points at the relevant log directory or source method.
- Data-flow map: every step is cited to source — diff against the code to confirm.

If any reader believes a finding is wrong, please grep for the cited line and submit a counter-example. The audit was deliberately ruthless — false positives are possible and welcome.
