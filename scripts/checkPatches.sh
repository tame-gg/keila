#!/usr/bin/env bash
set -euo pipefail

PATCH_LIST="${PATCH_LIST:-/tmp/keila-patches-$$.txt}"
TODO_AUDIT="${TODO_AUDIT:-/tmp/keila-todo-audit-$$.txt}"

find leaf-api/paper-patches leaf-server/paper-patches leaf-server/minecraft-patches \
  -type f \
  -name '*.patch' \
  -print | sort > "$PATCH_LIST"

test -s "$PATCH_LIST"

if rg -n --hidden --glob '!**/.gradle/**' --glob '!**/build/**' '^(<<<<<<<|=======|>>>>>>>)([[:space:]]|$)' leaf-api leaf-server; then
  echo "Unresolved merge conflict markers found." >&2
  exit 1
fi

rg -n --hidden --glob '!**/.gradle/**' --glob '!**/build/**' 'TODO|FIXME|HACK|XXX' leaf-api leaf-server > "$TODO_AUDIT" || true
echo "Patch files: $(wc -l < "$PATCH_LIST")"
echo "TODO audit entries: $(wc -l < "$TODO_AUDIT")"
