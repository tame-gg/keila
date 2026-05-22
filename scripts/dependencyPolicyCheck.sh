#!/usr/bin/env bash
set -euo pipefail

echo "Dependency policy notes"
rg -n 'Bump Dependencies|TODO: Waiting Paper|Breaking changes|SNAPSHOT|implementation\\("|api\\("' leaf-api/build.gradle.kts.patch leaf-server/build.gradle.kts.patch
