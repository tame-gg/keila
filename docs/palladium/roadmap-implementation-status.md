# Palladium Roadmap Implementation Status

This file records what has actually landed in code versus what still needs behavior and benchmark validation.

## Landed In Palladium-Owned Source

- `KO-005`: Auth/profile virtual-thread pools now use bounded concurrency through `gg.tame.palladium.async.BoundedExecutorService`.
- `KO-018`: `gg.tame.palladium.roadmap.Utf8StringCache` provides capped UTF-8 byte reuse for future packet serialization hooks.
- `KO-021` and `KO-039`: `gg.tame.palladium.roadmap.DirtyIndexSet` provides stable dirty-index tracking for future section and metadata delta hooks.
- `KO-036`: `gg.tame.palladium.roadmap.SpawnWaveScheduler` provides deterministic chunk spawn-wave assignment.
- `KO-040`: `gg.tame.palladium.roadmap.AdaptiveCompressionController` provides MSPT-based compression-level selection.
- `KF-001` through `KF-050`: `gg.tame.palladium.command.subcommands.FeaturesCommand` implements the 50-feature foundation as live `/palladium features <key>` operator commands, mirrored by `gg.tame.palladium.feature.PalladiumFeatureCatalog`.

## Namespace Rebrand

- Palladium-owned Java source moved from `org.dreeam.leaf` to `gg.tame.palladium`.
- Palladium-owned `Leaf*` classes were renamed to `Palladium*` where the class name is part of our own surface.
- Patch references were updated from `org.dreeam.leaf` to `gg.tame.palladium`.
- Inherited upstream packages such as `org.leavesmc.leaves`, `org.galemc.gale`, `gg.pufferfish`, and `net.caffeinemc` remain unchanged for compatibility and attribution.

## Verification Notes

The patch queue now applies cleanly from a fresh `/tmp` checkout under Temurin 21:

- `applyAllPatches` applied 91 Mache patches, 914 Paper Minecraft source patches, and the Palladium/Leaf feature patch stack.
- `:leaf-api:compileJava` and `:leaf-server:compileJava` pass.
- `:leaf-api:test` and `:leaf-server:test` pass when forced with `--rerun-tasks --no-build-cache`.

The earlier `setupMacheSources` failure was reproduced in a clean pinned Paper checkout when running on the local Oracle `21-ea` JDK, and the same pinned Paper checkout succeeded on Temurin 21. Palladium verification should use a stable Java 21 LTS runtime. If switching from `21-ea`, rerun with `--rerun-tasks` or clear stale paperweight task caches.

Do not mark the remaining `Research` items complete until:

- Replay parity exists for behavior-changing world, entity, redstone, packet, and IO changes.
- Macrobench coverage exists for claimed throughput, MSPT, allocation, and bandwidth improvements.
- Each item has a rollback config or is disabled by default.
