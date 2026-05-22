# Keila Patch Risk Index

Use this index when accepting, updating, or removing patches.

## P0 Risk

- Parallel world ticking
- Entity tracker threading
- Chunk map/cache synchronization
- Region file or world storage changes
- Player data saving changes
- Network packet ordering changes
- Any item marked `Research` in `docs/keila/optimization-roadmap.md`

Requires stress tests, soak tests, and rollback notes.

## P1 Risk

- Mob AI and pathfinding changes
- Mob spawning changes
- Hopper/block entity behavior
- Plugin event skipping
- Protocol extensions
- Dependency upgrades with runtime surface

Requires targeted regression tests, deterministic replay where behavior may change, and compatibility notes.

## P2 Risk

- Allocation reduction with no behavior change
- Cached hash/toString values
- Logging suppression
- Documentation and build changes

Requires compile/test verification and patch review.
