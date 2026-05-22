# Keila Optimization Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Convert the imported Leaf tree into a Keila baseline with build governance, runtime safety checks, benchmark scaffolding, and the first concrete async fixes.

**Architecture:** Keep upstream patch structure intact and add Keila-owned files around it first. Runtime code changes are limited to self-contained utilities where tests can describe the intended behavior without requiring a fully applied Paper workspace.

**Tech Stack:** Java 21, Gradle, paperweight patcher, GitHub Actions, JUnit, JMH-style benchmark harness docs, Minecraft server macrobench recipes.

---

### Task 1: Keila Identity

**Files:**
- Modify: `settings.gradle.kts`
- Modify: `gradle.properties`
- Modify: `README.md`
- Modify: `scripts/prepareRelease.sh`
- Modify: `leaf-server/build.gradle.kts.patch`
- Modify: `leaf-api/build.gradle.kts.patch`
- Modify: `leaf-server/src/main/java/gg/tame/keila/version/KeilaVersionFetcher.java`
- Create: `docs/keila/identity.md`

- [ ] Rename the root project to `keila`.
- [ ] Use a Keila/tame.gg Maven group and artifact naming scheme.
- [ ] Replace Leaf-facing release names and user-facing README text with Keila text.
- [ ] Keep upstream attribution explicit.

### Task 2: Build And CI Guardrails

**Files:**
- Create: `.github/workflows/build.yml`
- Create: `.github/workflows/patch-audit.yml`
- Create: `.github/dependabot.yml`
- Create: `gradle/libs.versions.toml`
- Create: `docs/release/release-process.md`

- [ ] Require Java 21 for build jobs.
- [ ] Run `./gradlew applyAllPatches`, `./gradlew check`, and `./gradlew createMojmapPaperclipJar`.
- [ ] Add patch naming and TODO-audit jobs.
- [ ] Add dependency update visibility without auto-merging runtime upgrades.

### Task 3: Async Queue Safety

**Files:**
- Modify: `leaf-server/src/main/java/gg/tame/keila/async/chunk/AsyncChunkSender.java`
- Test: `leaf-server/src/test/java/gg/tame/keila/async/chunk/AsyncChunkSenderTest.java`
- Test: `leaf-server/src/test/java/gg/tame/keila/util/queue/MpmcQueueTest.java`

- [ ] Write tests for receive accounting, clear accounting, capacity enforcement, and queue round trips.
- [ ] Fix `recv()` so failed receives do not decrement the in-flight size.
- [ ] Fix `clear()` so it drains only queued packets and resets accounting.

### Task 4: Runtime Observability And Safety

**Files:**
- Modify: `leaf-server/src/main/java/gg/tame/keila/async/path/AsyncPathProcessor.java`
- Modify: `leaf-server/src/main/java/gg/tame/keila/config/modules/async/AsyncPathfinding.java`
- Create: `docs/keila/runtime-safety.md`

- [ ] Add queue/rejection/timeout counters to async pathfinding.
- [ ] Make async pathfinding timeout configurable.
- [ ] Document production risk for async pathfinding, async tracker, parallel world ticking, and async chunk send.

### Task 5: Benchmark Program

**Files:**
- Create: `docs/benchmarks/macrobench.md`
- Create: `docs/benchmarks/microbench.md`
- Create: `docs/keila/tuning-presets.md`

- [ ] Define macrobench workloads for worldgen, hoppers, redstone, TNT, mob farms, chunk loading, and fake players.
- [ ] Define microbench requirements for custom collections, queues, VarInt/VarLong, random tick, and chunk caches.
- [ ] Add compatibility, performance, and vanilla-correctness presets.

### Task 6: Upstream And Patch Governance

**Files:**
- Create: `docs/upstream/sync-policy.md`
- Create: `docs/upstream/patch-risk-index.md`
- Modify: `scripts/upstreamCommit.sh`

- [ ] Track Paper, Purpur, Leaf, and Keila-specific patches separately.
- [ ] Rank patch risk by concurrency, world storage, networking, config, and micro-optimization impact.
- [ ] Require a conflict report and benchmark notes for high-risk patches.
