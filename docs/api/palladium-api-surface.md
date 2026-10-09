# Palladium API Surface

Palladium-owned plugin API should use the `gg.tame.palladium` namespace. Inherited upstream packages remain unchanged for compatibility and attribution.

## Current Public API Notes

- Palladium-specific events should live under `gg.tame.palladium.event`.
- Palladium-specific player events should live under `gg.tame.palladium.event.player`.
- New plugin-facing APIs should be documented before release and checked by `scripts/apiCompatibilityReport.sh`.

## Compatibility Rules

- Do not move inherited Paper, Purpur, Leaf, Gale, Pufferfish, or Lithium packages unless the move is explicitly documented as a compatibility break.
- Do not expose experimental async runtime internals as stable API.
- Every new API class needs a short release-note entry and Javadoc before publish.
