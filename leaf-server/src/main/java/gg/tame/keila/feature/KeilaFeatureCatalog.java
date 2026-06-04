package gg.tame.keila.feature;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class KeilaFeatureCatalog {

    private static final List<KeilaFeature> FEATURES = List.of(
        feature("KF-001", "Server summary", "Overview", FeatureStatus.COMMAND, surface("KF-001"), "Shows live server, player, world, chunk, entity, and plugin counts."),
        feature("KF-002", "Health score", "Overview", FeatureStatus.COMMAND, surface("KF-002"), "Classifies tick, memory, and scheduler pressure."),
        feature("KF-003", "TPS report", "Performance", FeatureStatus.COMMAND, surface("KF-003"), "Shows 5s, 10s, and 60s TPS."),
        feature("KF-004", "MSPT report", "Performance", FeatureStatus.COMMAND, surface("KF-004"), "Shows 5s, 10s, and 60s MSPT (detailed world breakdown)."),
        feature("KF-005", "Memory report", "Performance", FeatureStatus.COMMAND, surface("KF-005"), "Shows JVM heap pressure."),
        feature("KF-006", "Request garbage collection", "Performance", FeatureStatus.COMMAND, surface("KF-006"), "Requests GC and reports heap delta."),
        feature("KF-007", "Thread summary", "Diagnostics", FeatureStatus.COMMAND, surface("KF-007"), "Counts live JVM threads by state."),
        feature("KF-008", "Thread state sample", "Diagnostics", FeatureStatus.COMMAND, surface("KF-008"), "Lists a bounded sample of current JVM threads."),
        feature("KF-009", "Uptime report", "Diagnostics", FeatureStatus.COMMAND, surface("KF-009"), "Shows JVM uptime and process metadata."),
        feature("KF-010", "JVM report", "Diagnostics", FeatureStatus.COMMAND, surface("KF-010"), "Shows Java, VM, OS, and thread information."),
        feature("KF-011", "Disk report", "Diagnostics", FeatureStatus.COMMAND, surface("KF-011"), "Shows server directory disk space."),
        feature("KF-012", "World list", "Worlds", FeatureStatus.COMMAND, surface("KF-012"), "Lists loaded worlds with players, chunks, and entities."),
        feature("KF-013", "World details", "Worlds", FeatureStatus.COMMAND, surface("KF-013"), "Shows details for one loaded world."),
        feature("KF-014", "Chunk report", "Worlds", FeatureStatus.COMMAND, surface("KF-014"), "Shows loaded chunk counts per world."),
        feature("KF-015", "Chunk hotspots", "Worlds", FeatureStatus.COMMAND, surface("KF-015"), "Finds loaded chunks with the most entities."),
        feature("KF-016", "Entity report", "Entities", FeatureStatus.COMMAND, surface("KF-016"), "Shows entity counts per world."),
        feature("KF-017", "Entity type top list", "Entities", FeatureStatus.COMMAND, surface("KF-017"), "Shows the most common loaded entity types."),
        feature("KF-018", "Block entity report", "Worlds", FeatureStatus.COMMAND, surface("KF-018"), "Shows loaded block entity counts."),
        feature("KF-019", "Player list", "Players", FeatureStatus.COMMAND, surface("KF-019"), "Lists online players with world and ping."),
        feature("KF-020", "Player details", "Players", FeatureStatus.COMMAND, surface("KF-020"), "Shows details for one online player."),
        feature("KF-021", "Ping report", "Players", FeatureStatus.COMMAND, surface("KF-021"), "Shows min, average, and max player ping."),
        feature("KF-022", "Plugin report", "Plugins", FeatureStatus.COMMAND, surface("KF-022"), "Shows enabled and disabled plugin counts."),
        feature("KF-023", "Plugin details", "Plugins", FeatureStatus.COMMAND, surface("KF-023"), "Shows details for one plugin."),
        feature("KF-024", "Scheduler report", "Diagnostics", FeatureStatus.COMMAND, surface("KF-024"), "Shows pending scheduler task pressure by plugin."),
        feature("KF-025", "Permission report", "Diagnostics", FeatureStatus.COMMAND, surface("KF-025"), "Counts registered permissions by default state."),
        feature("KF-026", "Game rule report", "Worlds", FeatureStatus.COMMAND, surface("KF-026"), "Lists game rules for one world."),
        feature("KF-027", "Difficulty report", "Worlds", FeatureStatus.COMMAND, surface("KF-027"), "Shows world difficulty and environment."),
        feature("KF-028", "Spawn limit report", "Entities", FeatureStatus.COMMAND, surface("KF-028"), "Shows Bukkit spawn limits per world."),
        feature("KF-029", "View distance report", "Network", FeatureStatus.COMMAND, surface("KF-029"), "Shows view and simulation distance."),
        feature("KF-030", "Recipe count", "Content", FeatureStatus.COMMAND, surface("KF-030"), "Counts registered recipes."),
        feature("KF-031", "Advancement count", "Content", FeatureStatus.COMMAND, surface("KF-031"), "Counts registered advancements."),
        feature("KF-032", "Scoreboard report", "Content", FeatureStatus.COMMAND, surface("KF-032"), "Shows scoreboard objectives, teams, and entries."),
        feature("KF-033", "Save worlds", "Operations", FeatureStatus.COMMAND, surface("KF-033"), "Force-saves every loaded world."),
        feature("KF-034", "Config file report", "Configuration", FeatureStatus.COMMAND, surface("KF-034"), "Checks expected config files and sizes."),
        feature("KF-035", "Config search", "Configuration", FeatureStatus.COMMAND, surface("KF-035"), "Searches expected config files."),
        feature("KF-036", "Async state", "Configuration", FeatureStatus.COMMAND, surface("KF-036"), "Shows high-risk async feature enablement."),
        feature("KF-037", "Pathfinding queue", "Performance", FeatureStatus.COMMAND, surface("KF-037"), "Shows async pathfinding queue counters."),
        feature("KF-038", "Async chunk send", "Configuration", FeatureStatus.COMMAND, surface("KF-038"), "Shows async chunk-send state and rollback path."),
        feature("KF-039", "Async playerdata save", "Configuration", FeatureStatus.COMMAND, surface("KF-039"), "Shows async playerdata save state and rollback path."),
        feature("KF-040", "Async tracker", "Configuration", FeatureStatus.COMMAND, surface("KF-040"), "Shows async entity tracker state and thread count."),
        feature("KF-041", "Parallel world ticking", "Configuration", FeatureStatus.COMMAND, surface("KF-041"), "Shows parallel world ticking state and safeguards."),
        feature("KF-042", "Virtual thread pools", "Configuration", FeatureStatus.COMMAND, surface("KF-042"), "Shows Java 21 virtual-thread pool settings."),
        feature("KF-043", "Rollout check", "Operations", FeatureStatus.COMMAND, surface("KF-043"), "Warns about high-risk enabled features."),
        feature("KF-044", "Safe mode profile", "Operations", FeatureStatus.COMMAND, surface("KF-044"), "Prints conservative rollback settings."),
        feature("KF-045", "Network report", "Network", FeatureStatus.COMMAND, surface("KF-045"), "Shows bind, port, online players, and ping pressure."),
        feature("KF-046", "Mob density report", "Entities", FeatureStatus.COMMAND, surface("KF-046"), "Shows living entity pressure per loaded chunk."),
        feature("KF-047", "World file report", "Worlds", FeatureStatus.COMMAND, surface("KF-047"), "Shows region, POI, and entity file counts."),
        feature("KF-048", "Plugin author report", "Plugins", FeatureStatus.COMMAND, surface("KF-048"), "Groups installed plugins by declared authors."),
        feature("KF-049", "Support bundle", "Operations", FeatureStatus.COMMAND, surface("KF-049"), "Prints compact facts for bug reports."),
        feature("KF-050", "Support bundle export", "Operations", FeatureStatus.COMMAND, surface("KF-050"), "Writes keila-support-<timestamp>.txt for bug reports."),
        feature("KF-051", "Command help", "Operations", FeatureStatus.COMMAND, surface("KF-051"), "Shows the categorized /keila command menu.")
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

    /** Three features surfaced at server startup when {@code misc.startup-experience.feature-splash} is enabled. */
    public static List<KeilaFeature> startupHighlights() {
        return List.of(
            byId("KF-002").orElseThrow(),
            byId("KF-036").orElseThrow(),
            byId("KF-051").orElseThrow()
        );
    }

    private static String surface(String id) {
        return switch (id) {
            case "KF-002" -> "/keila health";
            case "KF-004" -> "/keila mspt";
            case "KF-043" -> "/keila rollout";
            case "KF-044" -> "/keila safe";
            case "KF-049" -> "/keila info support-bundle";
            case "KF-050" -> "/keila export";
            case "KF-051" -> "/keila";
            default -> "/keila info";
        };
    }

    private static KeilaFeature feature(String id, String title, String category, FeatureStatus status, String surface, String description) {
        return new KeilaFeature(id, title, category, status, surface, description);
    }
}
