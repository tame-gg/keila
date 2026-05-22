#!/usr/bin/env bash
set -euo pipefail

kind="${1:-}"
log="${2:-}"
test -f "$log"

case "$kind" in
  region|chunk-serialization)
    rg -n "$kind|region|chunk|serialize|serialization|save" "$log" | sed -n '1,120p' || true
    ;;
  *)
    echo "Usage: scripts/ioProfileSummary.sh [region|chunk-serialization] <log-file>" >&2
    exit 2
    ;;
esac
