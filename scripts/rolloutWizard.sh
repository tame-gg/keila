#!/usr/bin/env bash
set -euo pipefail

feature="${1:-high-risk-feature}"

cat <<EOF
# Keila rollout checklist: $feature
- Baseline Spark profile captured
- Synthetic benchmark completed
- Soak test completed
- Rollback config identified
- First production window selected
- Post-rollout MSPT and allocation comparison captured
EOF
