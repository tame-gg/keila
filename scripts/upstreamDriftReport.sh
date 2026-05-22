#!/usr/bin/env bash
set -euo pipefail

paper_commit=$(sed -n 's/^paperCommit=//p' gradle.properties)
mc_version=$(sed -n 's/^mcVersion=//p' gradle.properties)

echo "Keila upstream drift report"
echo "mcVersion=$mc_version"
echo "paperCommit=$paper_commit"
echo "local HEAD=$(git rev-parse --short HEAD 2>/dev/null || echo unknown)"
echo
echo "Paper refs:"
GIT_CONFIG_NOSYSTEM=1 GIT_CONFIG_GLOBAL=/dev/null git ls-remote https://github.com/PaperMC/Paper.git "refs/tags/ver/$mc_version" refs/heads/main || true
