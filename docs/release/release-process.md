# Keila Release Process

## Requirements

- Java 21 LTS, preferably Temurin 21. Do not use an early-access `21-ea` runtime; it can make paperweight's Mache DiffPatch step reject exact upstream hunks.
- After changing Java runtimes, rerun Paperweight tasks with `--rerun-tasks` or clear stale `.gradle/caches/paperweight` task output.
- Local SSD or CI runner with filesystem features supported by Gradle.
- Clean Git history with tags.
- Keila-owned Maven/download endpoints configured.

## Commands

```bash
./gradlew applyAllPatches
./gradlew check
./gradlew createMojmapPaperclipJar
scripts/prepareRelease.sh dist
```

## GitHub Workflow

The `keila` workflow compiles the Mojmap paperclip jar, renames it to `keila-0.1.1.jar` by default, writes release metadata, uploads the release-ready artifact, and publishes a GitHub Release when run manually or from a `v*` tag.

## Release Notes

Each release should include:

- Minecraft version.
- Paper commit.
- Leaf base.
- Keila commits.
- High-risk patch changes.
- Checksums.
- Known compatibility risks.
