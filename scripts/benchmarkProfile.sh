#!/usr/bin/env bash
set -euo pipefail

profile="${1:-balanced}"

case "$profile" in
  safe)
    echo "view-distance=6"
    echo "simulation-distance=4"
    echo "async-pathfinding=false"
    ;;
  balanced)
    echo "view-distance=8"
    echo "simulation-distance=6"
    echo "async-pathfinding=false"
    ;;
  stress)
    echo "view-distance=12"
    echo "simulation-distance=8"
    echo "async-pathfinding=true"
    ;;
  *)
    echo "Usage: scripts/benchmarkProfile.sh [safe|balanced|stress]" >&2
    exit 2
    ;;
esac
