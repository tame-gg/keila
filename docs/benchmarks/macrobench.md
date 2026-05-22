# Keila Macrobench Plan

Run macrobenchmarks on local SSD or CI hardware, not network-mounted source trees.

## Workloads

- Fresh overworld generation at fixed seed and fixed pregeneration radius.
- Nether and End generation at fixed radius.
- 10,000 hopper grid with mixed empty/full inventories.
- Dense villager trading hall.
- Dense hostile mob farm.
- Redstone clock grid with observers, pistons, and comparators.
- TNT chain explosion area.
- Elytra chunk-loading path with many players.
- 1,000 fake-player join, move, and disconnect cycle.
- Map/item-frame wall.
- Replay-based parity run for every roadmap item in `docs/keila/optimization-roadmap.md`.

## Measurements

- Average MSPT
- p95 and p99 MSPT
- GC pause time
- Allocation rate
- Loaded chunks
- Entity count
- Block entity count
- Packet send rate
- Async queue depth and rejection count
- Behavior mismatch count against the replay baseline.

Each Keila performance patch should name the workload it improves and the workload it might risk.
