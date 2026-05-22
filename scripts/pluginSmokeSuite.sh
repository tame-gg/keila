#!/usr/bin/env bash
set -euo pipefail

plugins_dir="${1:-plugins}"
mkdir -p "$plugins_dir"

echo "Plugin smoke suite"
echo "plugins-dir=$plugins_dir"
find "$plugins_dir" -maxdepth 1 -name '*.jar' -print | sort
