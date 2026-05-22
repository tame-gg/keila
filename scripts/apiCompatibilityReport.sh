#!/usr/bin/env bash
set -euo pipefail

echo "Keila API compatibility report"
find leaf-api/src/main/java -type f -name '*.java' | sort | while read -r file; do
  package=$(sed -n 's/^package //p' "$file" | sed 's/;//')
  class=$(basename "$file" .java)
  printf '%s.%s\n' "$package" "$class"
done
