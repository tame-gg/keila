# Keila Feature Foundation

This document maps the 50 requested feature additions to concrete command, script, documentation, or runtime-scaffold surfaces. High-risk server behavior stays behind explicit verification and rollout gates before it can become default runtime behavior.

## Operator Commands

- `/keila features` lists every feature in the Keila feature catalog.
- `/keila features <KF-###>` shows one feature, its status, and its implementation surface.
- `/keila features <category>` lists features in one category.
- `/keila perf queues` shows async pathfinding queue counters.
- `/keila perf async` shows high-risk async feature state and virtual-thread concurrency caps.
- `/keila perf memory` shows JVM memory and processor data.
- `/keila perf all` prints all three perf views.

## Script Surfaces

| Feature | Surface |
| --- | --- |
| Config diff command | `scripts/configDiff.sh` |
| Config validation before boot | `scripts/configValidate.sh` |
| Config docs generator | `scripts/configDocs.sh` |
| Patch risk score generator | `scripts/patchRiskReport.sh` |
| Patch source attribution validator | `scripts/checkPatches.sh` |
| Upstream sync dashboard | `scripts/upstreamDriftReport.sh` |
| Paper/Purpur/Leaf drift report | `scripts/upstreamDriftReport.sh` |
| Release artifact verifier | `scripts/verifyReleaseArtifact.sh` |
| Release checksum generator | `scripts/releaseChecksums.sh` |
| Release notes generator | `scripts/releaseNotes.sh` |
| Benchmark profile presets | `scripts/benchmarkProfile.sh` |
| Synthetic mob-spawn benchmark | `scripts/syntheticBenchmark.sh mob-spawn` |
| Synthetic chunk-send benchmark | `scripts/syntheticBenchmark.sh chunk-send` |
| Synthetic redstone benchmark | `scripts/syntheticBenchmark.sh redstone` |
| Login storm benchmark | `scripts/syntheticBenchmark.sh login-storm` |
| Plugin compatibility smoke suite | `scripts/pluginSmokeSuite.sh` |
| Spark profile auto-summary | `scripts/sparkProfileSummary.sh` |
| MSPT regression detector | `scripts/regressionDetector.sh mspt` |
| Allocation regression detector | `scripts/regressionDetector.sh allocation` |
| Region-file IO profiler | `scripts/ioProfileSummary.sh region` |
| Chunk serialization profiler | `scripts/ioProfileSummary.sh chunk-serialization` |
| Safe-mode startup profile | `scripts/safeModeProfile.sh` |
| Production rollout wizard | `scripts/rolloutWizard.sh` |
| API compatibility report | `scripts/apiCompatibilityReport.sh` |
| Dependency bump policy checker | `scripts/dependencyPolicyCheck.sh` |
| Build environment doctor | `scripts/buildDoctor.sh` |
| One-command local verification | `scripts/verifyLocal.sh` |

## Runtime Safety Gates

The following items are intentionally runtime scaffolds until replay tests, stress tests, and soak tests exist for each behavior:

- Async chunk-send stress harness
- Packet-order replay tests
- Playerdata save ordering tests
- Config migration framework
- Per-feature rollback command
- Per-world async toggles
- Admin web status endpoint
- Sentry breadcrumbs for async failures
- Crash report Keila section
- Entity tracker contention metrics

Each of these must satisfy the rollout gates in `docs/keila/runtime-safety.md` before being changed from scaffold to default-enabled runtime behavior.

## Rollback Contract

Any high-risk feature must have one of these rollback paths before production use:

- a boolean config key that disables the feature after restart,
- a hot-reload-safe config path documented by `/keila features configuration`,
- or a safe-mode profile entry emitted by `scripts/safeModeProfile.sh`.

## Generated Catalog

The authoritative in-code catalog lives in `gg.tame.keila.feature.KeilaFeatureCatalog`. Tests enforce that all 50 feature entries remain present, have unique IDs, and expose a concrete surface.
