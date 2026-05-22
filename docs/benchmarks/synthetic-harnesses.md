# Synthetic Benchmark Harnesses

The synthetic benchmark scripts define repeatable workload contracts. They do not replace real server soak tests.

## Workloads

- `scripts/syntheticBenchmark.sh mob-spawn`
- `scripts/syntheticBenchmark.sh chunk-send`
- `scripts/syntheticBenchmark.sh redstone`
- `scripts/syntheticBenchmark.sh login-storm`

## Profiles

Use `scripts/benchmarkProfile.sh safe`, `balanced`, or `stress` to pin benchmark settings before collecting data.

## Regression Checks

Use `scripts/regressionDetector.sh mspt <baseline> <candidate> <threshold-percent>` for MSPT samples and `scripts/regressionDetector.sh allocation <baseline> <candidate> <threshold-percent>` for allocation samples. Input files should contain one numeric sample per line.
