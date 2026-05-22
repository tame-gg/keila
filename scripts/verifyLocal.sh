#!/usr/bin/env bash
set -euo pipefail

if [ -z "${JAVA_HOME:-}" ] && [ -x /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java ]; then
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
  export PATH="$JAVA_HOME/bin:$PATH"
fi

scripts/checkPatches.sh
./gradlew applyAllPatches --stacktrace
./gradlew check --stacktrace
./gradlew createMojmapPaperclipJar --stacktrace
scripts/verifyReleaseArtifact.sh
