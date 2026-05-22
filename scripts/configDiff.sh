#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -ne 2 ]; then
  echo "Usage: scripts/configDiff.sh <old-config.yml> <new-config.yml>" >&2
  exit 2
fi

old_file="$1"
new_file="$2"
test -f "$old_file"
test -f "$new_file"

normalize() {
  sed -E 's/[[:space:]]+#.*$//; /^[[:space:]]*$/d' "$1" | sort
}

diff -u <(normalize "$old_file") <(normalize "$new_file")
