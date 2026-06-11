# Keila Roadmap Implementation Status

> [!IMPORTANT]
> This document describes the optimization work as it existed on the **pre-rebase
> Leaf-based line**. None of these `gg.tame.keila` subsystems are in the current
> **Purpur 26.1.2** base — they are preserved in git history and tracked for
> re-port in a build-capable environment. See
> [26.1.2-purpur-rebase.md](../upstream/26.1.2-purpur-rebase.md) for the inventory
> and recovery refs. Treat the status below as the roadmap target, not the
> current build.

This file records what landed in code on the prior line versus what still needs behavior and benchmark validation after re-port.

## Landed In Keila-Owned Source

- `KO-005`: Auth/profile virtual-thread pools now use bounded concurrency through `gg.tame.keila.async.BoundedExecutorService`.
- `KO-018`: `gg.tame.keila.roadmap.Utf8StringCache` provides capped UTF-8 byte reuse for future packet serialization hooks.
- `KO-021` and `KO-039`: `gg.tame.keila.roadmap.DirtyIndexSet` provides stable dirty-index tracking for future section and metadata delta hooks.
- `KO-036`: `gg.tame.keila.roadmap.SpawnWaveScheduler` provides deterministic chunk spawn-wave assignment.
- `KO-040`: `gg.tame.keila.roadmap.AdaptiveCompressionController` provides MSPT-based compression-level selection.
- `KF-001` through `KF-050`: `gg.tame.keila.command.subcommands.FeaturesCommand` implements the 50-feature foundation as live `/keila features <key>` operator commands, mirrored by `gg.tame.keila.feature.KeilaFeatureCatalog`.

## Namespace Rebrand

- Keila-owned Java source moved from `org.dreeam.leaf` to `gg.tame.keila`.
- Keila-owned `Leaf*` classes were renamed to `Keila*` where the class name is part of our own surface.
- Patch references were updated from `org.dreeam.leaf` to `gg.tame.keila`.
- Inherited upstream packages such as `org.leavesmc.leaves`, `org.galemc.gale`, `gg.pufferfish`, and `net.caffeinemc` remain unchanged for compatibility and attribution.

## Verification Notes

These notes are historical, from the pre-rebase Leaf-based line (`applyAllPatches`
+ compile/test of the old `leaf-api`/`leaf-server` modules under Java 21). They do
**not** apply to the current Purpur 26.1.2 base. When the optimization stack is
re-ported (see [26.1.2-purpur-rebase.md](../upstream/26.1.2-purpur-rebase.md)),
verification must run on **Java 25** against `purpur-server`/`purpur-api`.

Do not mark the remaining `Research` items complete until:

- Replay parity exists for behavior-changing world, entity, redstone, packet, and IO changes.
- Macrobench coverage exists for claimed throughput, MSPT, allocation, and bandwidth improvements.
- Each item has a rollback config or is disabled by default.
