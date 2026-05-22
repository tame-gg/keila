#!/usr/bin/env bash
set -euo pipefail

out="${1:-docs/keila/generated-config-modules.md}"
mkdir -p "$(dirname "$out")"

{
  echo "# Keila Config Modules"
  echo
  echo "Generated from \`leaf-server/src/main/java/gg/tame/keila/config/modules\`."
  echo
  find leaf-server/src/main/java/gg/tame/keila/config/modules -type f -name '*.java' | sort | while read -r file; do
    module=${file#leaf-server/src/main/java/}
    module=${module%.java}
    echo "- \`${module//\//.}\`"
  done
} > "$out"

echo "Wrote $out"
