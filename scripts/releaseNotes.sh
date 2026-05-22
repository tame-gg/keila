#!/usr/bin/env bash
set -euo pipefail

range="${1:-HEAD~20..HEAD}"
artifact="${2:-$(find leaf-server/build/libs -maxdepth 1 -name '*paperclip*-mojmap.jar' | sort | tail -n 1 2>/dev/null || true)}"

echo "# Keila Release Notes"
echo
echo "## Changes"
git log "$range" --pretty='- %s (%h)' 2>/dev/null || echo "- Source export without git history."
echo
echo "## Checksums"
if [ -n "$artifact" ] && [ -f "$artifact" ]; then
  scripts/releaseChecksums.sh "$artifact"
else
  echo "No paperclip artifact found."
fi
