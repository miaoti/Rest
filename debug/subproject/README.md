# Sub-Projects — Master's-Student Tracks for the MIST A-Conference Push

The MIST tool paper (`paper/main_issta.tex`) currently targets the ISSTA
2026 Tool Demonstrations track. To upgrade the contribution to a
full-track A-conference submission (ICSE / FSE / ASE / ISSTA research
track), three evaluation gaps must close. The paper's own §6
*Limitations* names them:

> "The bundled case study covers one SUT (TrainTicket); a broader
> head-to-head against AutoRestTest, LogiAgent, and MACROHIVE across
> Sock Shop, Online Boutique, and DeathStarBench is in-progress
> companion work and reserved for the full research paper."

Each of the three sub-projects below closes one of those gaps. They are
sized for **3-credit CSI 5v90 (summer, ~120 hours)**, scoped so a
single master's student can own each, and **orthogonal to MIST core** so
multiple students can run them in parallel without stepping on the
adapter sever / oracle / generator code.

Each brief lists deliverables and acceptance criteria, **not** step-by-step
instructions. Students choose their own path; the mentor reviews the
artifact.

---

## The three sub-projects

| ID  | Brief | One-line | Paper section it lands in |
|-----|-------|----------|----------------------------|
| SP1 | [Multi-SUT Deployment + Trace Corpus Kit](SP1_MULTI_SUT_DEPLOYMENT.md) | Deploy ≥3 microservice SUTs + capture trace corpora MIST can ingest | §5 Setup, §6 RQ1 cross-SUT external validity |
| SP2 | [Baseline Reproducibility Harness](SP2_BASELINE_HARNESS.md) | Containerise ≥3 competing testers behind one uniform driver | §5 Setup, §6 Table 1 head-to-head |
| SP3 | [Real-Bug Replay Benchmark](SP3_REAL_BUG_BENCHMARK.md) | Curate 20-30 real historical bugs + replay harness | §6 RQ2 fault-realism, §7 threats to validity |

---

## Dependency map

```
SP1 (SUT deployment) ----+----> SP2 (baseline harness needs SUTs)
                         |
                         +----> SP3 (bug replay needs deploy infra)

SP2 ----> SP3 (real-bug catalog × every baseline is the full table)
```

SP1 is the critical-path input for SP2 and SP3. If multiple students start
in parallel:

- **SP1 student starts immediately** with Sock Shop (week 1-4), in time
  for SP2/SP3 students to start consuming.
- **SP2 student** containerises baselines against bundled TrainTicket
  while waiting for SP1's SUTs (weeks 1-6), then re-runs against SP1's
  SUTs (weeks 7-12).
- **SP3 student** mines TrainTicket bugs while waiting for SP1's other
  SUTs (weeks 1-6), then adds Sock Shop / Online Boutique bugs once SP1
  delivers (weeks 7-12).

If only one student is available, **SP1 first** — without SUTs, SP2 and
SP3 are bottlenecked. If only two students, **SP1 + SP2** is the highest
paper-impact pairing.

---

## What this work explicitly does *not* cover

These were considered and excluded from the sub-project briefs. They
remain available as future thesis topics or follow-ups:

- **Cross-LLM robustness study** — swap MIST's LLM backend across 5
  providers, measure variance in oracle quality. Good thesis topic;
  lower paper-impact than SP1-3 because the demo paper already names a
  default backend.
- **Trace-shape oracle quality study** — manually label 1000 traces as
  shape-correct / shape-violating, measure invariant precision/recall.
  Adds rigour but doesn't address the §6 Limitations gap.
- **Mutation-testing validation** — apply PIT or equivalent to TrainTicket
  service code as a ground-truth oracle. Infrastructure-heavy; better as
  a follow-up after SP3 establishes the real-bug baseline.
- **gRPC / GraphQL extensions** — extend MIST beyond REST. Architectural
  work; doesn't help the current paper's REST positioning.

---

## Shared conventions for all sub-projects

- **Branch off** `inject-detection`, not `main`. `inject-detection` is
  the project's de-facto main branch.
- **Worktrees, not branch-switching**, when working in parallel on the
  same machine — see project root `CLAUDE.md` for the convention.
- **No AI attribution** in commits, PRs, comments, or docs. AI tools may
  be used; they are not authors.
- **Reproducibility is a deliverable**, not a nice-to-have. Every
  sub-project's smoke test must run from a fresh clone on a stock Linux
  box without manual fixup.
- **Weekly check-ins** with the mentor (Tingshuo). Open a draft PR early
  per `student/<name>/<sp>-…` branch and push WIP to it — the mentor
  reviews against the PR, not the email-attachment dance.
- **Technical report (~8 pages) is the academic deliverable** that
  satisfies the CSI 5v90 credit, in addition to the working artifact.
  Write it as you go; if it's empty in week 10, you have a problem.

---

## What this folder is *not*

This folder contains **scoping briefs**, not implementation plans. Each
brief tells the student what to ship and what "done" looks like. It does
not tell them how to deploy Sock Shop, what Docker base image to use, or
what k6 script to write. Those choices are the student's, and defending
them is part of the academic deliverable.

If you (mentor or future student) find yourself wanting to write
prescriptive how-to inside these files, push back — the briefs stay at
the "what" layer. Implementation know-how belongs in each sub-project's
own `evaluation/<sp-name>/README.md` once the work is underway.
