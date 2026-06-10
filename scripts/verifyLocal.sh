#!/usr/bin/env bash
set -euo pipefail

if [ -z "${JAVA_HOME:-}" ] && [ -x /Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home/bin/java ]; then
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home
  export PATH="$JAVA_HOME/bin:$PATH"
fi

bash scripts/checkPatches.sh
./gradlew applyAllPatches --stacktrace
./gradlew check --stacktrace
./gradlew createMojmapBundlerJar --stacktrace
bash scripts/verifyReleaseArtifact.sh
