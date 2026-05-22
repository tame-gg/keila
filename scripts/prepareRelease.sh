#!/usr/bin/env bash
set -euo pipefail

release_name="${KEILA_RELEASE_NAME:-keila}"
release_authors="${KEILA_RELEASE_AUTHORS:-tame.gg, asianrizz, koels}"
release_version="${KEILA_RELEASE_VERSION:-0.1.1}"
dist_dir="${1:-dist}"

source_artifact="${KEILA_SOURCE_ARTIFACT:-}"
if [ -z "$source_artifact" ]; then
  source_artifact="$(find leaf-server/build/libs -maxdepth 1 -type f -name '*paperclip*-mojmap.jar' | sort | tail -n 1)"
fi

test -n "$source_artifact"
test -f "$source_artifact"

mkdir -p "$dist_dir"

release_file="${release_name}-${release_version}.jar"
metadata_file="${release_name}-${release_version}.yml"
notes_file="${release_name}-${release_version}-notes.md"
target_artifact="$dist_dir/$release_file"

cp "$source_artifact" "$target_artifact"
scripts/verifyReleaseArtifact.sh "$target_artifact"

(
  cd "$dist_dir"
  if command -v sha256sum >/dev/null 2>&1; then
    sha256sum "$release_file"
  else
    shasum -a 256 "$release_file"
  fi
) > "$target_artifact.sha256"

cat > "$dist_dir/$metadata_file" <<EOF
name: $release_name
authors: $release_authors
version: $release_version
artifact: $release_file
EOF

{
  echo "# $release_name $release_version"
  echo
  echo "- name: $release_name"
  echo "- authors: $release_authors"
  echo "- version: $release_version"
  echo "- artifact: $release_file"
  echo
  echo "## Changes"
  git log -20 --pretty='- %s (%h)' 2>/dev/null || echo "- Source export without git history."
  echo
  echo "## Checksums"
  cat "$target_artifact.sha256"
} > "$dist_dir/$notes_file"

echo "Release artifact ready: $target_artifact"
echo "Release metadata ready: $dist_dir/$metadata_file"
