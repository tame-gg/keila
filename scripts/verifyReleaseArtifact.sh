#!/usr/bin/env bash
set -euo pipefail

artifact="${1:-$(find leaf-server/build/libs -maxdepth 1 -name '*paperclip*-mojmap.jar' | sort | tail -n 1)}"
test -n "$artifact"
test -f "$artifact"
test -s "$artifact"

size=$(wc -c < "$artifact")
if [ "$size" -lt 1048576 ]; then
  echo "Artifact is unexpectedly small: $artifact" >&2
  exit 1
fi

manifest="/tmp/keila-release-manifest-$$.txt"
listing="/tmp/keila-release-listing-$$.txt"
trap 'rm -f "$manifest" "$listing"' EXIT

unzip -p "$artifact" META-INF/MANIFEST.MF > "$manifest"
unzip -l "$artifact" > "$listing"

grep -Eq '^Main-Class: ' "$manifest"
grep -Eq 'LICENSE.txt|META-INF/MANIFEST.MF' "$listing"

echo "Release artifact verified: $artifact"
