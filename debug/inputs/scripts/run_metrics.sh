#!/usr/bin/env bash
# Run the full input-quality measurement pipeline on a RESTest run.
#
# Defaults to the most recent TrainTicket two-stage run and the most recent
# LLM log. Override any of them with the flags below.
#
# Usage:
#   ./debug/inputs/scripts/run_metrics.sh
#   ./debug/inputs/scripts/run_metrics.sh --run-dir <PATH>
#   ./debug/inputs/scripts/run_metrics.sh \
#         --run-dir src/test/java/trainticket_twostage_test/<RUN_ID> \
#         --llm-log logs/llm-communications/llm-communication-<TS>.log \
#         --oas     "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml" \
#         --out     debug/inputs/measurements/<RUN_ID>

set -euo pipefail

usage() {
    cat <<EOF
Usage: $0 [options]

Options:
  --run-dir DIR      Generated test directory (default: most-recent TrainTicketTwoStageTest_*)
  --llm-log FILE     LLM communication log (default: most-recent in logs/llm-communications/)
  --oas FILE         OpenAPI spec (default: merged_openapi_spec 1.yaml)
  --out DIR          Output directory (default: debug/inputs/measurements/<run_id>)
  --include-negatives  Also score negative variants in D1
  --python BIN       Python executable (default: python3)
  -h | --help        Show this help

EOF
}

RUN_DIR=""
LLM_LOG=""
OAS=""
OUT_DIR=""
INCLUDE_NEG=""
PYTHON_BIN="python3"

while [[ $# -gt 0 ]]; do
    case "$1" in
        --run-dir) RUN_DIR="$2"; shift 2 ;;
        --llm-log) LLM_LOG="$2"; shift 2 ;;
        --oas)     OAS="$2"; shift 2 ;;
        --out)     OUT_DIR="$2"; shift 2 ;;
        --include-negatives) INCLUDE_NEG="--include-negatives"; shift ;;
        --python)  PYTHON_BIN="$2"; shift 2 ;;
        -h|--help) usage; exit 0 ;;
        *) echo "unknown arg: $1" >&2; usage; exit 2 ;;
    esac
done

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ---- Defaults ----
if [[ -z "$RUN_DIR" ]]; then
    RUN_DIR=$(ls -dt "$REPO_ROOT"/src/test/java/trainticket_twostage_test/TrainTicketTwoStageTest_* 2>/dev/null | head -n1 || true)
fi
if [[ -z "$RUN_DIR" || ! -d "$RUN_DIR" ]]; then
    echo "error: no run dir found; pass --run-dir" >&2
    exit 2
fi
RUN_ID=$(basename "$RUN_DIR")

if [[ -z "$LLM_LOG" ]]; then
    LLM_LOG=$(ls -t "$REPO_ROOT"/logs/llm-communications/*.log 2>/dev/null | head -n1 || true)
fi
if [[ -z "$LLM_LOG" || ! -f "$LLM_LOG" ]]; then
    echo "warning: no LLM log found; D3 will be empty (use --llm-log)" >&2
fi

if [[ -z "$OAS" ]]; then
    OAS="$REPO_ROOT/src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml"
fi
if [[ ! -f "$OAS" ]]; then
    echo "error: OAS file not found: $OAS" >&2
    exit 2
fi

if [[ -z "$OUT_DIR" ]]; then
    OUT_DIR="$REPO_ROOT/debug/inputs/measurements/$RUN_ID"
fi
mkdir -p "$OUT_DIR"

echo "================================================================"
echo " run_id   : $RUN_ID"
echo " run_dir  : $RUN_DIR"
echo " llm_log  : ${LLM_LOG:-<none>}"
echo " oas      : $OAS"
echo " out      : $OUT_DIR"
echo "================================================================"

# ---- Stage 1: Mining ----
"$PYTHON_BIN" "$SCRIPT_DIR/mine_test_inputs.py" \
    --run-dir "$RUN_DIR" \
    --out "$OUT_DIR/inputs.csv"

if [[ -n "$LLM_LOG" && -f "$LLM_LOG" ]]; then
    "$PYTHON_BIN" "$SCRIPT_DIR/mine_llm_log.py" \
        --log "$LLM_LOG" \
        --out "$OUT_DIR/llm_pairs.csv"
else
    : > "$OUT_DIR/llm_pairs.csv"
fi

# ---- Stage 2: Validation ----
"$PYTHON_BIN" "$SCRIPT_DIR/validate_d1.py" \
    --inputs "$OUT_DIR/inputs.csv" \
    --oas "$OAS" \
    --out-dir "$OUT_DIR" \
    $INCLUDE_NEG

if [[ -s "$OUT_DIR/llm_pairs.csv" ]]; then
    "$PYTHON_BIN" "$SCRIPT_DIR/validate_d3.py" \
        --pairs "$OUT_DIR/llm_pairs.csv" \
        --out-dir "$OUT_DIR"
else
    echo '{"metric":"D3 LLM Hallucination Rate","status":"no LLM log mined","lhr":null}' > "$OUT_DIR/d3_summary.json"
fi

"$PYTHON_BIN" "$SCRIPT_DIR/validate_d2.py" \
    --inputs "$OUT_DIR/inputs.csv" \
    --oas "$OAS" \
    --out-dir "$OUT_DIR"

# ---- Stage 3: Report ----
"$PYTHON_BIN" "$SCRIPT_DIR/generate_report.py" \
    --measurements "$OUT_DIR" \
    --out "$OUT_DIR/report.md" \
    --run-id "$RUN_ID" \
    --run-dir "$RUN_DIR" \
    --llm-log "${LLM_LOG:-}" \
    --oas "$OAS"

echo ""
echo "Done. Open: $OUT_DIR/report.md"
