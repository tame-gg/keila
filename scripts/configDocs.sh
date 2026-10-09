#!/usr/bin/env bash
set -euo pipefail

out="${1:-docs/palladium/generated-config-modules.md}"
mkdir -p "$(dirname "$out")"

{
  echo "# Palladium Config Modules"
  echo
  echo "Generated from \`leaf-server/src/main/java/gg/tame/palladium/config/modules\`."
  echo
  find leaf-server/src/main/java/gg/tame/palladium/config/modules -type f -name '*.java' | sort | while read -r file; do
    module=${file#leaf-server/src/main/java/}
    module=${module%.java}
    echo "- \`${module//\//.}\`"
  done
} > "$out"

echo "Wrote $out"
