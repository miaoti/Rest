#!/usr/bin/env python3
"""
Roll D1 / D2 / D3 outputs up into a single markdown report.

Reads the JSON summaries written by validate_d1.py, validate_d2.py, and
validate_d3.py, and produces a human-readable report that highlights pass/fail
against the thresholds in `input-quality-measurement-framework.md`.
"""

from __future__ import annotations

import argparse
import json
import sys
from datetime import datetime, timezone
from pathlib import Path


def load(p: Path) -> dict | None:
    if not p.is_file():
        return None
    try:
        return json.loads(p.read_text(encoding="utf-8"))
    except json.JSONDecodeError:
        return None


def fmt_pct(x: float | None) -> str:
    if x is None:
        return "N/A"
    return f"{x * 100:.2f}%"


def status(passed: bool | None) -> str:
    if passed is None:
        return "—"
    return "✅ Pass" if passed else "❌ Fail"


def fmt_threshold(metric: str) -> str:
    return {
        "D1": "≥ 90%",
        "D2": "≥ 85%",
        "D3": "≤ 20%",
    }[metric]


def section_d1(d1: dict | None) -> list[str]:
    if d1 is None:
        return ["## D1 Schema Conformance Rate", "", "_No `d1_summary.json` found._", ""]
    out = [
        "## D1 — Schema Conformance Rate",
        "",
        f"- **SCR (positive variants): {fmt_pct(d1.get('scr'))}** "
        f"({d1.get('rows_valid', 0)} / {d1.get('rows_validated', 0)} validated)",
        f"- Rows total: {d1.get('rows_total', 0)} "
        f"(no-schema: {d1.get('rows_no_schema', 0)}, missing-op: {d1.get('rows_missing_operation', 0)}, "
        f"skipped negatives: {d1.get('rows_skipped_negative', 0)})",
        f"- Threshold: {fmt_threshold('D1')}  →  {status(d1.get('scr_threshold_pass'))}",
        "",
    ]
    bk = d1.get("by_test_kind") or {}
    if bk:
        out += ["### Breakdown by test kind", "",
                "| Kind | Total | Valid | SCR |",
                "| --- | ---: | ---: | ---: |"]
        for k, v in sorted(bk.items()):
            out.append(f"| {k} | {v.get('total', 0)} | {v.get('valid', 0)} | {fmt_pct(v.get('scr'))} |")
        out.append("")
    worst = d1.get("worst_parameters") or []
    if worst:
        out += ["### Worst-performing parameters (≥ 3 rows)", "",
                "| Operation | Parameter | Location | Total | Valid | SCR |",
                "| --- | --- | --- | ---: | ---: | ---: |"]
        for w in worst[:10]:
            out.append(
                f"| `{w['operation']}` | `{w['parameter']}` | {w['location']} | "
                f"{w['total']} | {w['valid']} | {fmt_pct(w['scr'])} |"
            )
        out.append("")
        # Show example violations for the worst
        out += ["### Example violations", ""]
        for w in worst[:5]:
            ex = w.get("invalid_examples") or []
            if not ex:
                continue
            out.append(f"- `{w['operation']}` / `{w['parameter']}` ({w['location']}):")
            for e in ex[:3]:
                val = e.get("value", "")
                if len(val) > 80:
                    val = val[:77] + "..."
                out.append(f"  - value `{val}` → {e.get('error', '')}")
        out.append("")
    return out


def section_d2(d2: dict | None) -> list[str]:
    if d2 is None:
        return ["## D2 — IPD Satisfaction Rate", "", "_No `d2_summary.json` found._", ""]
    out = ["## D2 — Inter-Parameter-Dependency Satisfaction Rate", ""]
    if (d2.get("status") or "").startswith("N/A"):
        out += [
            f"- **Status: {d2['status']}**",
            f"- {d2.get('note', '')}",
            "",
            "To enable: copy `debug/inputs/scripts/curated_idl.example.yaml` to "
            "`curated_idl.yaml` (without the `.example`) and add rules.",
            "",
        ]
        return out
    out += [
        f"- **IPD-SR: {fmt_pct(d2.get('ipd_sr'))}** "
        f"({d2.get('payloads_fully_satisfied', 0)} / {d2.get('payloads_evaluated', 0)} payloads)",
        f"- Rules loaded: {d2.get('rules_loaded', 0)} "
        f"(curated file present: {d2.get('rules_from_curated')})",
        f"- Total payloads observed: {d2.get('payloads_total', 0)}",
        f"- Threshold: {fmt_threshold('D2')}  →  {status(d2.get('ipd_sr_threshold_pass'))}",
        "",
    ]
    by_op = d2.get("by_operation") or {}
    if by_op:
        out += ["### Per-operation breakdown", "",
                "| Operation | Total | Satisfied | IPD-SR |",
                "| --- | ---: | ---: | ---: |"]
        rows = sorted(
            ((op, v) for op, v in by_op.items()),
            key=lambda x: ((x[1].get("ipd_sr") or 0.0), x[0]),
        )
        for op, v in rows[:15]:
            out.append(f"| `{op}` | {v.get('total', 0)} | {v.get('satisfied', 0)} | {fmt_pct(v.get('ipd_sr'))} |")
        out.append("")
    rv = d2.get("rule_violation_counts") or {}
    if rv:
        out += ["### Most-violated rules", "",
                "| Rule | Violations |",
                "| --- | ---: |"]
        for rule, count in list(rv.items())[:10]:
            out.append(f"| `{rule}` | {count} |")
        out.append("")
    return out


def section_d3(d3: dict | None) -> list[str]:
    if d3 is None:
        return ["## D3 — LLM Hallucination Rate", "", "_No `d3_summary.json` found._", ""]
    out = [
        "## D3 — LLM Hallucination Rate",
        "",
        f"- **LHR: {fmt_pct(d3.get('lhr'))}** "
        f"({d3.get('rows_hallucinated', 0)} / {d3.get('rows_actionable', 0)} actionable values)",
        f"- Definition: `{d3.get('lhr_definition', 'hallucinated / actionable')}` — abstentions are not hallucinations",
        f"- Total LLM values seen: {d3.get('rows_total', 0)} "
        f"(scored: {d3.get('rows_scored', 0)}; valid: {d3.get('rows_valid', 0)}; "
        f"abstained: {d3.get('rows_abstained', 0)})",
        f"- Scored categories: {', '.join(d3.get('scored_categories', []))}",
        f"- Abstain rate: {fmt_pct(d3.get('abstain_rate'))} "
        f"(LLM emitted `NO_GOOD_MATCH` / `NO_VALUES_GENERATED` instead of a value)",
        f"- Threshold: {fmt_threshold('D3')}  →  {status(d3.get('lhr_threshold_pass'))}",
        "",
    ]
    bv = d3.get("by_constraint_violation") or {}
    if bv:
        out += ["### Violations by constraint type", "",
                "| Constraint | Violations |",
                "| --- | ---: |"]
        for k, v in bv.items():
            out.append(f"| {k} | {v} |")
        out.append("")
    bm = d3.get("by_model") or {}
    if bm:
        out += ["### Per-model breakdown", "",
                "| Model | Total | Valid | Hallucinated | Abstained | LHR |",
                "| --- | ---: | ---: | ---: | ---: | ---: |"]
        for m, v in sorted(bm.items()):
            out.append(
                f"| `{m or 'unknown'}` | {v.get('total', 0)} | {v.get('valid', 0)} | "
                f"{v.get('hallucinated', 0)} | {v.get('abstained', 0)} | {fmt_pct(v.get('lhr'))} |"
            )
        out.append("")
    bc = d3.get("by_prompt_category") or {}
    if bc:
        out += ["### Per prompt category", "",
                "| Category | Total | Valid | Hallucinated | Abstained | LHR |",
                "| --- | ---: | ---: | ---: | ---: | ---: |"]
        for c, v in sorted(bc.items()):
            out.append(
                f"| `{c}` | {v.get('total', 0)} | {v.get('valid', 0)} | "
                f"{v.get('hallucinated', 0)} | {v.get('abstained', 0)} | {fmt_pct(v.get('lhr'))} |"
            )
        out.append("")
    worst = d3.get("worst_parameters") or []
    if worst:
        out += ["### Worst-performing parameters (≥ 3 actionable values)", "",
                "| Parameter | Total | Hallucinated | Abstained | LHR |",
                "| --- | ---: | ---: | ---: | ---: |"]
        for w in worst[:10]:
            out.append(
                f"| `{w['parameter']}` | {w['total']} | {w['hallucinated']} | "
                f"{w.get('abstained', 0)} | {fmt_pct(w['lhr'])} |"
            )
        out.append("")
        out += ["### Example violations", ""]
        for w in worst[:5]:
            ex = w.get("examples") or []
            if not ex:
                continue
            out.append(f"- `{w['parameter']}`:")
            for e in ex[:3]:
                val = e.get("value", "")
                if len(val) > 80:
                    val = val[:77] + "..."
                vstr = " | ".join(e.get("violations") or [])
                out.append(f"  - value `{val}` → {vstr}")
        out.append("")
    return out


def main(argv: list[str]) -> int:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--measurements", required=True, type=Path,
                   help="Output directory used by validate_d{1,2,3}.py")
    p.add_argument("--out", required=True, type=Path, help="Markdown report path")
    p.add_argument("--run-id", default=None, help="Run identifier to include in the header")
    p.add_argument("--run-dir", default=None, help="Path to the test directory (for the header)")
    p.add_argument("--llm-log", default=None, help="LLM log file used for D3 (for the header)")
    p.add_argument("--oas", default=None, help="OAS spec used for D1/D2 (for the header)")
    args = p.parse_args(argv)

    base = args.measurements
    d1 = load(base / "d1_summary.json")
    d2 = load(base / "d2_summary.json")
    d3 = load(base / "d3_summary.json")

    title_run = args.run_id or base.name
    lines = [
        f"# Input Quality Metrics — {title_run}",
        "",
        f"_Generated {datetime.now(timezone.utc).isoformat(timespec='seconds').replace("+00:00", "")}Z_",
        "",
        "## Source artefacts",
        "",
    ]
    if args.run_dir:
        lines.append(f"- Generated tests: `{args.run_dir}`")
    if args.llm_log:
        lines.append(f"- LLM log: `{args.llm_log}`")
    if args.oas:
        lines.append(f"- OpenAPI spec: `{args.oas}`")
    lines.append("")

    # Top-line summary
    lines += [
        "## Summary",
        "",
        "| Metric | Score | Threshold | Status |",
        "| --- | --- | --- | --- |",
    ]
    if d1:
        lines.append(f"| D1 SCR (positive) | **{fmt_pct(d1.get('scr'))}** | {fmt_threshold('D1')} | {status(d1.get('scr_threshold_pass'))} |")
    if d2:
        sc = d2.get("ipd_sr")
        if (d2.get("status") or "").startswith("N/A"):
            lines.append(f"| D2 IPD-SR | N/A — no IDL declared | {fmt_threshold('D2')} | — |")
        else:
            lines.append(f"| D2 IPD-SR | **{fmt_pct(sc)}** | {fmt_threshold('D2')} | {status(d2.get('ipd_sr_threshold_pass'))} |")
    if d3:
        lines.append(f"| D3 LHR | **{fmt_pct(d3.get('lhr'))}** | {fmt_threshold('D3')} | {status(d3.get('lhr_threshold_pass'))} |")
    lines.append("")

    lines += section_d1(d1)
    lines += section_d2(d2)
    lines += section_d3(d3)

    lines += [
        "---",
        "",
        "_See `debug/inputs/input-quality-measurement-framework.md` for the full definitions and citations._",
        "",
    ]

    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text("\n".join(lines), encoding="utf-8")
    print(f"generate_report: → {args.out}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))
