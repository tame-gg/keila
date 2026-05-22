#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -eq 0 ]; then
  set -- leaf-server/build/libs/*paperclip*-mojmap.jar
fi

for artifact in "$@"; do
  test -f "$artifact"
  if command -v sha256sum >/dev/null 2>&1; then
    sha256sum "$artifact"
  else
    shasum -a 256 "$artifact"
  fi
done
