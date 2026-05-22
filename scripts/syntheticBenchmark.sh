#!/usr/bin/env bash
set -euo pipefail

workload="${1:-}"
case "$workload" in
  mob-spawn)
    echo "workload=mob-spawn chunks=289 duration-seconds=300 metric=spawn-cycle-ms"
    ;;
  chunk-send)
    echo "workload=chunk-send players=32 view-distance=10 duration-seconds=300 metric=packet-build-ms"
    ;;
  redstone)
    echo "workload=redstone contraptions=64 duration-seconds=300 metric=tick-ms"
    ;;
  login-storm)
    echo "workload=login-storm clients=128 ramp-seconds=60 metric=auth-latency-ms"
    ;;
  *)
    echo "Usage: scripts/syntheticBenchmark.sh [mob-spawn|chunk-send|redstone|login-storm]" >&2
    exit 2
    ;;
esac
