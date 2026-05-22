#!/usr/bin/env bash
set -euo pipefail

metric="${1:-}"
baseline="${2:-}"
candidate="${3:-}"
threshold="${4:-10}"

test -n "$metric"
test -f "$baseline"
test -f "$candidate"

base_avg=$(awk '{sum+=$1; count++} END {if (count == 0) exit 1; print sum / count}' "$baseline")
candidate_avg=$(awk '{sum+=$1; count++} END {if (count == 0) exit 1; print sum / count}' "$candidate")
delta=$(awk -v b="$base_avg" -v c="$candidate_avg" 'BEGIN {print ((c - b) / b) * 100}')

echo "$metric baseline=$base_avg candidate=$candidate_avg delta-percent=$delta threshold-percent=$threshold"
awk -v d="$delta" -v t="$threshold" 'BEGIN {exit (d > t ? 1 : 0)}'
