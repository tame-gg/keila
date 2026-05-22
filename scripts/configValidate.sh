#!/usr/bin/env bash
set -euo pipefail

config="${1:-config/keila-global.yml}"
test -f "$config"
test -s "$config"

if ! grep -q 'config-version:' "$config"; then
  echo "Missing config-version in $config" >&2
  exit 1
fi

duplicate_keys=$(sed -E 's/[[:space:]]+#.*$//' "$config" | rg -o '^[[:space:]]*[A-Za-z0-9_.-]+:' | sed -E 's/^[[:space:]]*//; s/:$//' | sort | uniq -d)
if [ -n "$duplicate_keys" ]; then
  echo "Duplicate top-level keys in $config:" >&2
  echo "$duplicate_keys" >&2
  exit 1
fi

echo "Config validation passed: $config"
