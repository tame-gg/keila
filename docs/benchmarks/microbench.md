# Keila Microbench Plan

Microbenchmarks are required for custom low-level utilities and repeated hot-path rewrites.

## Required Targets

- `gg.tame.keila.util.queue.MpmcQueue`
- `gg.tame.keila.world.ChunkCache`
- VarInt and VarLong packet encoding
- Random tick sampling
- Entity distance checks
- Optimized collection replacements
- Identifier and NamespacedKey caching

## Rules

- Compare against a baseline implementation.
- Include single-thread and contended-thread cases for queue/map code.
- Report throughput and allocation rate.
- Keep benchmark source in-repo when possible.
- Do not default-enable behavior-changing optimizations from microbenchmarks alone.
