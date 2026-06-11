#!/usr/bin/env bash
set -euo pipefail

PATCH_LIST="${PATCH_LIST:-/tmp/keila-patches-$$.txt}"
TODO_AUDIT="${TODO_AUDIT:-/tmp/keila-todo-audit-$$.txt}"

find purpur-api/paper-patches purpur-server/paper-patches purpur-server/minecraft-patches \
  -type f \
  -name '*.patch' \
  -print | sort > "$PATCH_LIST"

test -s "$PATCH_LIST"

if rg -n --hidden --glob '!**/.gradle/**' --glob '!**/build/**' '^(<<<<<<<|=======|>>>>>>>)([[:space:]]|$)' purpur-api purpur-server; then
  echo "Unresolved merge conflict markers found." >&2
  exit 1
fi

rg -n --hidden --glob '!**/.gradle/**' --glob '!**/build/**' 'TODO|FIXME|HACK|XXX' purpur-api purpur-server > "$TODO_AUDIT" || true
echo "Patch files: $(wc -l < "$PATCH_LIST")"
echo "TODO audit entries: $(wc -l < "$TODO_AUDIT")"
