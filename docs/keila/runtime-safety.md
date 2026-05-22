# Runtime Safety

Keila's riskiest performance wins are concurrency features. They need metrics and staged rollout before being default-enabled.

## High-Risk Features

- Async chunk send
- Async pathfinding
- Multithreaded entity tracker
- Parallel world ticking
- Async player data saving
- Virtual-thread auth/profile lookup pools
- Thread-unsafe chunk cache paths

## Required Rollout Gates

- Synthetic stress test passes.
- Real server soak test passes.
- Spark profile shows a measurable gain.
- No packet-order, entity-tracking, or world-access warnings appear.
- Rollback is one config change.

## Required Metrics

- Queue depth
- Rejected task count
- Caller-runs count
- Timeout count
- Average and p95 task duration
- Shutdown drain duration

## Virtual Thread Auth/Profile Pools

KO-005 uses virtual threads for user authentication and profile download work, but keeps both pools behind explicit concurrency caps. Keep `performance.use-virtual-thread.auth-pool-max-concurrency` and `performance.use-virtual-thread.download-pool-max-concurrency` conservative until login-storm tests show the server and Mojang/API dependencies can absorb higher parallelism.

## Roadmap Utility Scaffolds

The `gg.tame.keila.roadmap` package contains small, testable helpers for roadmap items that will later be wired into Minecraft/Paper patches. Treat these as implementation scaffolds until the relevant patch hook is added and replay-tested.
