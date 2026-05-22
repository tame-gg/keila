#!/usr/bin/env bash
set -euo pipefail

find leaf-api/paper-patches leaf-server/paper-patches leaf-server/minecraft-patches -type f -name '*.patch' | sort | while read -r patch; do
  score=0
  rg -qi 'async|thread|executor|volatile|synchronized|queue' "$patch" && score=$((score + 3))
  rg -qi 'packet|network|connection|payload' "$patch" && score=$((score + 2))
  rg -qi 'save|storage|region|nbt|file|io' "$patch" && score=$((score + 2))
  rg -qi 'world|chunk|entity|tracker|path' "$patch" && score=$((score + 1))
  printf '%02d %s\n' "$score" "$patch"
done | sort -rn
