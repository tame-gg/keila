#!/usr/bin/env bash
set -euo pipefail

echo "Dependency policy notes"
rg -n 'Bump Dependencies|TODO: Waiting Paper|Breaking changes|SNAPSHOT|implementation\\("|api\\("' purpur-api/build.gradle.kts.patch purpur-server/build.gradle.kts.patch
