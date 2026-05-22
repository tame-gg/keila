#!/usr/bin/env bash
set -euo pipefail

cat <<'EOF'
performance:
  use-virtual-thread:
    auth-pool: false
    download-pool: false
async:
  async-chunk-send:
    enabled: false
  async-playerdata-save:
    enabled: false
  async-pathfinding:
    enabled: false
  parallel-world-ticking:
    enabled: false
EOF
