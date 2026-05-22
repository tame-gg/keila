package gg.tame.keila.feature;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class KeilaFeatureCatalog {

    private static final List<KeilaFeature> FEATURES = List.of(
        feature("KF-001", "Async chunk-send stress harness", "Runtime Safety", FeatureStatus.SCRIPT, "scripts/syntheticBenchmark.sh chunk-send", "Runs repeatable chunk-send pressure scenarios before async chunk send is enabled broadly."),
        feature("KF-002", "Packet-order replay tests", "Runtime Safety", FeatureStatus.RUNTIME_SCAFFOLD, "docs/keila/feature-foundation.md", "Defines replay checks for chunk, entity, and custom payload packet ordering."),
        feature("KF-003", "Playerdata save ordering tests", "Runtime Safety", FeatureStatus.RUNTIME_SCAFFOLD, "docs/keila/feature-foundation.md", "Tracks the required save ordering and cancellation checks for async player data writes."),
        feature("KF-004", "Shutdown drain metrics", "Runtime Safety", FeatureStatus.COMMAND, "/keila perf async", "Exposes async queue counters and shutdown-facing health signals in one operator command."),
        feature("KF-005", "Built-in queue metrics command", "Operator Commands", FeatureStatus.COMMAND, "/keila perf queues", "Shows async path queue depth, completion, rejection, caller-runs, and timeout counters."),
        feature("KF-006", "Built-in async metrics command", "Operator Commands", FeatureStatus.COMMAND, "/keila perf async", "Shows enabled state and concurrency settings for high-risk async systems."),
        feature("KF-007", "Built-in memory metrics command", "Operator Commands", FeatureStatus.COMMAND, "/keila perf memory", "Shows JVM heap use and available processor count without requiring external tooling."),
        feature("KF-008", "Runtime async health panel", "Operator Commands", FeatureStatus.COMMAND, "/keila features runtime", "Lists runtime-safety features and their shipped control surfaces."),
        feature("KF-009", "Per-feature rollback command", "Operator Commands", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Documents rollback-by-config expectations for each high-risk runtime feature."),
        feature("KF-010", "Config diff command", "Configuration", FeatureStatus.SCRIPT, "scripts/configDiff.sh", "Compares two config files with normalized paths for support and rollout reviews."),
        feature("KF-011", "Config validation before boot", "Configuration", FeatureStatus.SCRIPT, "scripts/configValidate.sh", "Checks Keila YAML configuration for required headers, duplicate keys, and empty files before boot."),
        feature("KF-012", "Config migration framework", "Configuration", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Defines the migration contract for future config version transitions."),
        feature("KF-013", "Config docs generator", "Configuration", FeatureStatus.SCRIPT, "scripts/configDocs.sh", "Extracts config module class names and config paths into a generated Markdown reference."),
        feature("KF-014", "Patch risk score generator", "Patch Governance", FeatureStatus.SCRIPT, "scripts/patchRiskReport.sh", "Scores patch files by touched subsystem and risky async/network/storage keywords."),
        feature("KF-015", "Patch ownership metadata", "Patch Governance", FeatureStatus.DOCUMENTED, "docs/upstream/patch-risk-index.md", "Keeps Keila-owned behavior tied to explicit patch ownership and risk notes."),
        feature("KF-016", "Patch source attribution validator", "Patch Governance", FeatureStatus.SCRIPT, "scripts/checkPatches.sh", "Audits patch queues for source hygiene, conflict markers, and TODO density."),
        feature("KF-017", "Upstream sync dashboard", "Upstream Sync", FeatureStatus.SCRIPT, "scripts/upstreamDriftReport.sh", "Prints current Paper pin, branch state, and upstream remote refs for sync planning."),
        feature("KF-018", "Paper/Purpur/Leaf drift report", "Upstream Sync", FeatureStatus.SCRIPT, "scripts/upstreamDriftReport.sh", "Gives maintainers a single command to start upstream drift analysis."),
        feature("KF-019", "Release artifact verifier", "Release", FeatureStatus.SCRIPT, "scripts/verifyReleaseArtifact.sh", "Checks generated paperclip jars for existence, manifest, size, and license entry."),
        feature("KF-020", "Release checksum generator", "Release", FeatureStatus.SCRIPT, "scripts/releaseChecksums.sh", "Generates SHA-256 and platform checksum data for release artifacts."),
        feature("KF-021", "Release notes generator", "Release", FeatureStatus.SCRIPT, "scripts/releaseNotes.sh", "Creates release-note skeletons from git history and artifact checksums."),
        feature("KF-022", "Benchmark profile presets", "Benchmarks", FeatureStatus.SCRIPT, "scripts/benchmarkProfile.sh", "Prints recommended benchmark JVM and server profiles for repeatable runs."),
        feature("KF-023", "Synthetic mob-spawn benchmark", "Benchmarks", FeatureStatus.SCRIPT, "scripts/syntheticBenchmark.sh mob-spawn", "Defines a repeatable mob-spawn workload and expected output contract."),
        feature("KF-024", "Synthetic chunk-send benchmark", "Benchmarks", FeatureStatus.SCRIPT, "scripts/syntheticBenchmark.sh chunk-send", "Defines a repeatable chunk-send workload and expected output contract."),
        feature("KF-025", "Synthetic redstone benchmark", "Benchmarks", FeatureStatus.SCRIPT, "scripts/syntheticBenchmark.sh redstone", "Defines a repeatable redstone workload and expected output contract."),
        feature("KF-026", "Login storm benchmark", "Benchmarks", FeatureStatus.SCRIPT, "scripts/syntheticBenchmark.sh login-storm", "Defines a repeatable login/auth pressure workload for virtual-thread pools."),
        feature("KF-027", "Plugin compatibility smoke suite", "Compatibility", FeatureStatus.SCRIPT, "scripts/pluginSmokeSuite.sh", "Provides a deterministic plugin smoke-test manifest and artifact checks."),
        feature("KF-028", "Spark profile auto-summary", "Observability", FeatureStatus.SCRIPT, "scripts/sparkProfileSummary.sh", "Summarizes Spark profiler exports into top hot paths and allocation notes."),
        feature("KF-029", "MSPT regression detector", "Observability", FeatureStatus.SCRIPT, "scripts/regressionDetector.sh mspt", "Compares baseline and candidate MSPT samples with a threshold."),
        feature("KF-030", "Allocation regression detector", "Observability", FeatureStatus.SCRIPT, "scripts/regressionDetector.sh allocation", "Compares baseline and candidate allocation samples with a threshold."),
        feature("KF-031", "Region-file IO profiler", "Observability", FeatureStatus.SCRIPT, "scripts/ioProfileSummary.sh region", "Summarizes region-file IO timings from log samples."),
        feature("KF-032", "Chunk serialization profiler", "Observability", FeatureStatus.SCRIPT, "scripts/ioProfileSummary.sh chunk-serialization", "Summarizes chunk serialization timing samples."),
        feature("KF-033", "Entity tracker contention metrics", "Observability", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Defines the contention counters required before tracker changes are default-enabled."),
        feature("KF-034", "Pathfinding queue visualizer", "Observability", FeatureStatus.COMMAND, "/keila perf queues", "Shows queue counters that can be graphed by external monitoring."),
        feature("KF-035", "Async timeout event logging", "Observability", FeatureStatus.COMMAND, "/keila perf queues", "Surfaces timed-out async pathfinding tasks already recorded by AsyncPathProcessor."),
        feature("KF-036", "Per-world async toggles", "Configuration", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Defines the future world-level toggle contract without changing runtime semantics yet."),
        feature("KF-037", "Safe-mode startup profile", "Configuration", FeatureStatus.SCRIPT, "scripts/safeModeProfile.sh", "Prints the conservative config settings for first boot and incident rollback."),
        feature("KF-038", "Production rollout wizard", "Release", FeatureStatus.SCRIPT, "scripts/rolloutWizard.sh", "Generates a staged rollout checklist for risky features."),
        feature("KF-039", "Live feature flag reload report", "Configuration", FeatureStatus.COMMAND, "/keila features configuration", "Lists which config and feature surfaces are hot-reload safe versus restart-only."),
        feature("KF-040", "Server brand/version endpoint", "Operator Commands", FeatureStatus.COMMAND, "/keila version", "Keeps the operator-facing Keila identity command wired to the server version path."),
        feature("KF-041", "Admin web status endpoint", "Observability", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Defines the status endpoint contract without opening a network listener by default."),
        feature("KF-042", "Sentry breadcrumbs for async failures", "Observability", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Defines what async failures should attach to Sentry once breadcrumb hooks are added."),
        feature("KF-043", "Crash report Keila section", "Observability", FeatureStatus.DOCUMENTED, "docs/keila/feature-foundation.md", "Defines the Keila crash-report section content and privacy boundaries."),
        feature("KF-044", "Plugin-facing Keila API namespace", "API", FeatureStatus.DOCUMENTED, "docs/api/keila-api-surface.md", "Defines the stable gg.tame.keila API namespace for future plugin integrations."),
        feature("KF-045", "Keila-specific event docs", "API", FeatureStatus.DOCUMENTED, "docs/api/keila-api-surface.md", "Documents Keila event namespace expectations and compatibility notes."),
        feature("KF-046", "API compatibility report", "API", FeatureStatus.SCRIPT, "scripts/apiCompatibilityReport.sh", "Reports current Keila API classes and package placement."),
        feature("KF-047", "Javadoc publish workflow", "API", FeatureStatus.DOCUMENTED, ".github/workflows/build.yml", "Documents where Javadoc publishing should attach to the existing build workflow."),
        feature("KF-048", "Dependency bump policy checker", "Build Governance", FeatureStatus.SCRIPT, "scripts/dependencyPolicyCheck.sh", "Scans build patches for pinned dependency bump comments and policy notes."),
        feature("KF-049", "Build environment doctor", "Build Governance", FeatureStatus.SCRIPT, "scripts/buildDoctor.sh", "Checks Java, Gradle, Git rewrite, and paperweight environment prerequisites."),
        feature("KF-050", "One-command local verification script", "Build Governance", FeatureStatus.SCRIPT, "scripts/verifyLocal.sh", "Runs patch audit, patch application, check, and paperclip packaging in one command.")
    );

    private KeilaFeatureCatalog() {
    }

    public static List<KeilaFeature> all() {
        return FEATURES;
    }

    public static Optional<KeilaFeature> byId(String id) {
        String normalized = id.toUpperCase(Locale.ROOT);
        return FEATURES.stream().filter(feature -> feature.id().equals(normalized)).findFirst();
    }

    public static Map<String, List<KeilaFeature>> byCategory() {
        return FEATURES.stream()
            .sorted(Comparator.comparing(KeilaFeature::id))
            .collect(Collectors.groupingBy(KeilaFeature::category, Collectors.toList()));
    }

    public static List<String> categories() {
        return byCategory().keySet().stream().sorted().toList();
    }

    private static KeilaFeature feature(String id, String title, String category, FeatureStatus status, String surface, String description) {
        return new KeilaFeature(id, title, category, status, surface, description);
    }
}
