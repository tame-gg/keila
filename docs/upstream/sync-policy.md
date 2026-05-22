# Keila Upstream Sync Policy

Keila tracks Paper, Purpur, Leaf, and Keila-specific patches.

## Sync Steps

1. Update upstream refs.
2. Apply patches.
3. Generate conflict report.
4. Run compile and tests.
5. Run macrobench smoke scenario for high-risk patches.
6. Record changed patches and risks in release notes.

## Conflict Report

Every upstream sync should list:

- Paper commits pulled in.
- Purpur commits pulled in.
- Leaf commits pulled in.
- Keila patches touched.
- High-risk systems touched.
- Tests and benchmarks run.

## Keila Patch Rule

Keila-specific behavior must live in clearly named patches or Keila-owned source files. Avoid burying Keila behavior inside upstream rebrand patches.
