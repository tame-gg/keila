#!/usr/bin/env bash
set -euo pipefail

echo "Keila build doctor"
echo "java-version-file=$(cat .java-version 2>/dev/null || echo missing)"
java_report="/tmp/keila-build-doctor-java-$$.txt"
trap 'rm -f "$java_report"' EXIT

if [ -z "${JAVA_HOME:-}" ] && [ -x /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java ]; then
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
  export PATH="$JAVA_HOME/bin:$PATH"
fi

if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  java_bin="$JAVA_HOME/bin/java"
elif [ -x /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java ]; then
  java_bin=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java
else
  java_bin=java
fi

if "$java_bin" -version > "$java_report" 2>&1; then
  sed 's/^/java: /' "$java_report"
else
  status=$?
  sed 's/^/java-error: /' "$java_report" || true
  echo "java-status=$status"
fi
./gradlew --version | sed -n '1,12p'
echo "git-rewrite-rules:"
git config --list --show-origin | rg 'url\\..*insteadof|github' || true
echo "paperCommit=$(sed -n 's/^paperCommit=//p' gradle.properties)"
