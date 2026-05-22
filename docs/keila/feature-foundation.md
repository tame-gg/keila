# Keila Feature Foundation

This document maps the 50 requested feature additions to concrete `/keila features <key>` operator commands. The in-game command is the primary surface: each entry returns live server data or performs a bounded operator action instead of sending admins to external docs.

## Operator Commands

- `/keila features` lists every runnable feature command.
- `/keila features list [category]` filters the command list.
- `/keila features categories` lists available categories.
- `/keila features <KF-###|key> [args]` runs the selected feature.
- `/keila features help` shows usage examples.
- `/keila mspt` reports server and world MSPT without requiring parallel world ticking.
- `/keila perf queues` shows async pathfinding queue counters.
- `/keila perf async` shows high-risk async feature state and virtual-thread concurrency caps.
- `/keila perf memory` shows JVM memory and processor data.
- `/keila perf all` prints all three perf views.

## Feature Commands

| Feature | Surface |
| --- | --- |
| KF-001 Server summary | `/keila features summary` |
| KF-002 Health score | `/keila features health` |
| KF-003 TPS report | `/keila features tps` |
| KF-004 MSPT report | `/keila features mspt` |
| KF-005 Memory report | `/keila features memory` |
| KF-006 Request garbage collection | `/keila features gc` |
| KF-007 Thread summary | `/keila features threads` |
| KF-008 Thread state sample | `/keila features thread-states` |
| KF-009 Uptime report | `/keila features uptime` |
| KF-010 JVM report | `/keila features jvm` |
| KF-011 Disk report | `/keila features disk` |
| KF-012 World list | `/keila features worlds` |
| KF-013 World details | `/keila features world <world>` |
| KF-014 Chunk report | `/keila features chunks` |
| KF-015 Chunk hotspots | `/keila features chunk-hotspots` |
| KF-016 Entity report | `/keila features entities` |
| KF-017 Entity type top list | `/keila features entity-types` |
| KF-018 Block entity report | `/keila features tile-entities` |
| KF-019 Player list | `/keila features players` |
| KF-020 Player details | `/keila features player <name>` |
| KF-021 Ping report | `/keila features pings` |
| KF-022 Plugin report | `/keila features plugins` |
| KF-023 Plugin details | `/keila features plugin <name>` |
| KF-024 Scheduler report | `/keila features scheduler` |
| KF-025 Permission report | `/keila features permissions` |
| KF-026 Game rule report | `/keila features gamerules <world>` |
| KF-027 Difficulty report | `/keila features difficulties` |
| KF-028 Spawn limit report | `/keila features spawn-limits` |
| KF-029 View distance report | `/keila features view-distance` |
| KF-030 Recipe count | `/keila features recipes` |
| KF-031 Advancement count | `/keila features advancements` |
| KF-032 Scoreboard report | `/keila features scoreboard` |
| KF-033 Save worlds | `/keila features save-worlds` |
| KF-034 Config file report | `/keila features config-files` |
| KF-035 Config search | `/keila features config-search <text>` |
| KF-036 Async state | `/keila features async-state` |
| KF-037 Pathfinding queue | `/keila features path-queue` |
| KF-038 Async chunk send | `/keila features chunk-send` |
| KF-039 Async playerdata save | `/keila features playerdata-save` |
| KF-040 Async tracker | `/keila features tracker` |
| KF-041 Parallel world ticking | `/keila features parallel-worlds` |
| KF-042 Virtual thread pools | `/keila features virtual-threads` |
| KF-043 Rollout check | `/keila features rollout-check` |
| KF-044 Safe mode profile | `/keila features safe-mode` |
| KF-045 Network report | `/keila features network` |
| KF-046 Mob density report | `/keila features mob-density` |
| KF-047 World file report | `/keila features world-files <world>` |
| KF-048 Plugin author report | `/keila features plugin-authors` |
| KF-049 Support bundle | `/keila features support-bundle` |
| KF-050 Command help | `/keila features command-help` |

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
- a hot-reload-safe config path documented by the relevant `/keila features <key>` output,
- or a safe-mode profile entry emitted by `scripts/safeModeProfile.sh`.

## Generated Catalog

The authoritative in-code catalog lives in `gg.tame.keila.feature.KeilaFeatureCatalog`. Tests enforce that all 50 feature entries remain present, have unique IDs, and expose a concrete surface.
