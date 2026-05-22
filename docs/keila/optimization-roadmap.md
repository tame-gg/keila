# Keila Optimization Roadmap

This roadmap tracks the full optimization backlog for Keila. Each item needs a benchmark, a behavior-preservation test, and a rollback switch before it is enabled by default.

## Status Key

- `Research`: needs Minecraft/Paper internals validation before code starts.
- `Prototype`: can be built behind a disabled config flag.
- `Scaffolded`: Keila-owned helper/config code exists, but the Minecraft/Paper patch hook is not wired yet.
- `Candidate`: safe enough for guarded implementation.
- `Blocked`: depends on upstream patch stability or external infrastructure.

## Phase 1: Highest Impact

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-011 | Off-heap chunk section storage with Java 21 `MemorySegment`. | Research | Chunk save/load parity, heap profile, corruption recovery. |
| KO-019 | Zero-copy chunk packet deduplication keyed by chunk and light hashes. | Prototype | Anti-xray parity, packet ref-count tests, multi-player chunk replay. |
| KO-028 | Entity type-indexed storage arrays for exact-class queries. | Candidate | Spawn/despawn index consistency tests and entity query benchmarks. |
| KO-039 | Delta entity metadata encoding with dirty bitmasks. | Scaffolded | Client protocol replay, plugin metadata mutation compatibility. |

## Phase 2: Tick-Time Reduction

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-001 | Lock-free POI manager with region-striped concurrent maps. | Research | Villager behavior replay, POI claim/release race tests. |
| KO-003 | Parallel entity collision resolution by disconnected collision islands. | Research | Deterministic replay, collision-order compatibility tests. |
| KO-020 | Adaptive per-player simulation distance. | Prototype | Fairness policy, per-player load attribution, visible behavior tests. |
| KO-046 | Redstone update wavefront scheduling. | Research | Contraption replay suite and vanilla order-compatibility audit. |

## Phase 3: Allocation, Generation, Compression

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-012 | Thread-local object pooling for pathfinding nodes. | Candidate | Async pathfinding microbench, leak/reset invariants. |
| KO-015 | Thread-local noise buffer pools for world generation. | Candidate | Chunk generation allocation profile and seed parity. |
| KO-024 | Memory-mapped region files with prefetch hints. | Research | Filesystem compatibility, crash recovery, region corruption tests. |
| KO-040 | Packet compression tiering by MSPT. | Scaffolded | Bandwidth/CPU benchmark and latency guardrails. |

## Phase 4: Micro-Optimizations

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-008 | Per-dimension CPU thread affinity. | Blocked | Linux-only implementation path and no-op fallback for other OSes. |
| KO-018 | String/UTF-8 interning for entity type names in packets. | Scaffolded | Allocation benchmark and memory cap. |
| KO-025 | Parallel biome blending. | Research | Worldgen seed parity and border correctness. |
| KO-049 | Fluid edge-only ticking. | Research | Fluid behavior replay and block update invalidation tests. |

## Async And Parallelism

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-002 | Async structure template placement with batched chunk section commits. | Research | Structure parity, chunk status ordering, block entity NBT tests. |
| KO-004 | Lock-free chunk ticket queues per region. | Research | Ticket propagation invariants and deadlock tests. |
| KO-005 | Virtual-thread Mojang API lookups with bounded semaphore. | Started | Login storm test and timeout/cancellation behavior. |
| KO-006 | Deferred async light engine updates. | Research | Lighting correctness replay and forced-sync chunk send path. |
| KO-007 | Async command suggestion generation with TTL prefix cache. | Prototype | Brigadier compatibility and plugin callback thread-safety. |
| KO-009 | Async scoreboard and team delta packet computation. | Research | Scoreboard plugin compatibility and ordering tests. |
| KO-010 | Sharded entity brain ticking with snapshot/write-back. | Research | AI determinism and memory write conflict tests. |

## Memory Efficiency And GC

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-013 | Lazy `CompoundTag` backing map initialization. | Candidate | NBT mutation tests and allocation benchmark. |
| KO-014 | Canonical immutable `BlockState` reference table. | Research | Registry lifecycle audit and memory/perf proof. |
| KO-016 | Entity movement stack allocator for short-lived vectors and boxes. | Research | Escape-analysis proof and mutation safety. |
| KO-017 | Pooled direct chunk packet `ByteBuf` allocation. | Candidate | Netty ref-count tests and leak detector clean run. |

## Chunk, World, And Generation

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-021 | Incremental chunk section sending with dirty section bitmask. | Scaffolded | Client chunk state replay and fallback full-resend path. |
| KO-022 | Chunk generation work-stealing queue. | Prototype | Region task fairness and worldgen throughput benchmark. |
| KO-023 | Predictive chunk loading for high-velocity projectiles. | Prototype | Abuse limits and no synchronous load regression. |
| KO-026 | Sparse chunk storage for empty sections. | Research | Chunk serialization and random access compatibility. |
| KO-027 | Deduplicated anti-xray chunk serialization. | Research | Per-player seed/settings compatibility. |

## Entities, AI, And Collision

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-029 | Hierarchical entity collision broadphase. | Candidate | Dense-farm collision benchmark and behavior replay. |
| KO-030 | Temporal AI goal selector caching. | Prototype | Invalidation audit for damage, target, block, and fluid events. |
| KO-031 | Batched pathfinding for entity groups targeting the same player. | Research | Path quality parity and herd behavior tests. |
| KO-032 | Full entity hibernation mode for inactive entities. | Prototype | Wake event coverage and plugin event compatibility. |
| KO-033 | Villager brain memory compaction. | Candidate | Villager behavior replay and memory profile. |
| KO-034 | Projectile trajectory prediction cache. | Research | Collision/liquid/parry behavior parity. |
| KO-035 | Fast UUID-to-entity ID resolution cache. | Candidate | Spawn/despawn invalidation tests. |
| KO-036 | Mob spawn wave scheduling across four ticks. | Scaffolded | Spawn-rate parity and CPU spike benchmark. |
| KO-037 | Dirty-bit AI goal evaluation. | Research | Goal invalidation proof and replay tests. |
| KO-038 | Static block collision shape caching. | Candidate | Shape invalidation tests and cache memory cap. |

## Networking, Protocol, And Packets

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-041 | Adaptive Netty flush batching by MSPT. | Candidate | Latency benchmark and packet ordering tests. |
| KO-042 | Connection quality-aware send view distance. | Prototype | Fairness rules and player-visible warning policy. |
| KO-043 | Entity movement delta compression. | Research | Client interpolation replay and anti-cheat compatibility. |
| KO-044 | Protocol compression offloading to virtual threads. | Prototype | Backpressure, packet order, and event loop safety tests. |
| KO-045 | NPC/phantom player packet suppression. | Prototype | Detection API and plugin compatibility. |

## Redstone And Blocks

| ID | Optimization | Status | Gate |
| --- | --- | --- | --- |
| KO-047 | Smart hopper pipeline optimization. | Research | Inventory order replay and plugin event compatibility. |
| KO-048 | Proximity-prioritized block event queue. | Research | Vanilla ordering audit and contraption replay. |
| KO-050 | Block update dependency graph for redstone contraptions. | Research | Cycle handling and vanilla order compatibility. |

## Strategic Differentiators

| ID | Initiative | Status | Gate |
| --- | --- | --- | --- |
| KS-001 | Deterministic replay-based regression testing. | Candidate | Capture/replay harness and golden behavior checks. |
| KS-002 | Self-tuning configuration from JFR/Spark data. | Prototype | Read-only advisor before any automatic writes. |
| KS-003 | JVM-native integration through Java 21 FFM. | Research | Platform fallback and crash isolation. |
