#!/usr/bin/env bash
set -euo pipefail

profile="${1:-}"
test -f "$profile"

echo "Spark profile summary: $profile"
rg -n 'mspt|tick|chunk|entity|packet|allocation|alloc' "$profile" | sed -n '1,80p' || true
