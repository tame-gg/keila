package gg.tame.keila.feature;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class KeilaFeatureCatalog {

    private static final List<KeilaFeature> FEATURES = List.of(
        feature("KF-001", "Server summary", "Overview", FeatureStatus.COMMAND, "/keila features summary", "Shows live server, player, world, chunk, entity, and plugin counts."),
        feature("KF-002", "Health score", "Overview", FeatureStatus.COMMAND, "/keila features health", "Classifies tick, memory, and scheduler pressure."),
        feature("KF-003", "TPS report", "Performance", FeatureStatus.COMMAND, "/keila features tps", "Shows 5s, 10s, and 60s TPS."),
        feature("KF-004", "MSPT report", "Performance", FeatureStatus.COMMAND, "/keila features mspt", "Shows 5s, 10s, and 60s MSPT."),
        feature("KF-005", "Memory report", "Performance", FeatureStatus.COMMAND, "/keila features memory", "Shows JVM heap pressure."),
        feature("KF-006", "Request garbage collection", "Performance", FeatureStatus.COMMAND, "/keila features gc", "Requests GC and reports heap delta."),
        feature("KF-007", "Thread summary", "Diagnostics", FeatureStatus.COMMAND, "/keila features threads", "Counts live JVM threads by state."),
        feature("KF-008", "Thread state sample", "Diagnostics", FeatureStatus.COMMAND, "/keila features thread-states", "Lists a bounded sample of current JVM threads."),
        feature("KF-009", "Uptime report", "Diagnostics", FeatureStatus.COMMAND, "/keila features uptime", "Shows JVM uptime and process metadata."),
        feature("KF-010", "JVM report", "Diagnostics", FeatureStatus.COMMAND, "/keila features jvm", "Shows Java, VM, OS, and thread information."),
        feature("KF-011", "Disk report", "Diagnostics", FeatureStatus.COMMAND, "/keila features disk", "Shows server directory disk space."),
        feature("KF-012", "World list", "Worlds", FeatureStatus.COMMAND, "/keila features worlds", "Lists loaded worlds with players, chunks, and entities."),
        feature("KF-013", "World details", "Worlds", FeatureStatus.COMMAND, "/keila features world <world>", "Shows details for one loaded world."),
        feature("KF-014", "Chunk report", "Worlds", FeatureStatus.COMMAND, "/keila features chunks", "Shows loaded chunk counts per world."),
        feature("KF-015", "Chunk hotspots", "Worlds", FeatureStatus.COMMAND, "/keila features chunk-hotspots", "Finds loaded chunks with the most entities."),
        feature("KF-016", "Entity report", "Entities", FeatureStatus.COMMAND, "/keila features entities", "Shows entity counts per world."),
        feature("KF-017", "Entity type top list", "Entities", FeatureStatus.COMMAND, "/keila features entity-types", "Shows the most common loaded entity types."),
        feature("KF-018", "Block entity report", "Worlds", FeatureStatus.COMMAND, "/keila features tile-entities", "Shows loaded block entity counts."),
        feature("KF-019", "Player list", "Players", FeatureStatus.COMMAND, "/keila features players", "Lists online players with world and ping."),
        feature("KF-020", "Player details", "Players", FeatureStatus.COMMAND, "/keila features player <name>", "Shows details for one online player."),
        feature("KF-021", "Ping report", "Players", FeatureStatus.COMMAND, "/keila features pings", "Shows min, average, and max player ping."),
        feature("KF-022", "Plugin report", "Plugins", FeatureStatus.COMMAND, "/keila features plugins", "Shows enabled and disabled plugin counts."),
        feature("KF-023", "Plugin details", "Plugins", FeatureStatus.COMMAND, "/keila features plugin <name>", "Shows details for one plugin."),
        feature("KF-024", "Scheduler report", "Diagnostics", FeatureStatus.COMMAND, "/keila features scheduler", "Shows pending scheduler task pressure by plugin."),
        feature("KF-025", "Permission report", "Diagnostics", FeatureStatus.COMMAND, "/keila features permissions", "Counts registered permissions by default state."),
        feature("KF-026", "Game rule report", "Worlds", FeatureStatus.COMMAND, "/keila features gamerules <world>", "Lists game rules for one world."),
        feature("KF-027", "Difficulty report", "Worlds", FeatureStatus.COMMAND, "/keila features difficulties", "Shows world difficulty and environment."),
        feature("KF-028", "Spawn limit report", "Entities", FeatureStatus.COMMAND, "/keila features spawn-limits", "Shows Bukkit spawn limits per world."),
        feature("KF-029", "View distance report", "Network", FeatureStatus.COMMAND, "/keila features view-distance", "Shows view and simulation distance."),
        feature("KF-030", "Recipe count", "Content", FeatureStatus.COMMAND, "/keila features recipes", "Counts registered recipes."),
        feature("KF-031", "Advancement count", "Content", FeatureStatus.COMMAND, "/keila features advancements", "Counts registered advancements."),
        feature("KF-032", "Scoreboard report", "Content", FeatureStatus.COMMAND, "/keila features scoreboard", "Shows scoreboard objectives, teams, and entries."),
        feature("KF-033", "Save worlds", "Operations", FeatureStatus.COMMAND, "/keila features save-worlds", "Force-saves every loaded world."),
        feature("KF-034", "Config file report", "Configuration", FeatureStatus.COMMAND, "/keila features config-files", "Checks expected config files and sizes."),
        feature("KF-035", "Config search", "Configuration", FeatureStatus.COMMAND, "/keila features config-search <text>", "Searches expected config files."),
        feature("KF-036", "Async state", "Configuration", FeatureStatus.COMMAND, "/keila features async-state", "Shows high-risk async feature enablement."),
        feature("KF-037", "Pathfinding queue", "Performance", FeatureStatus.COMMAND, "/keila features path-queue", "Shows async pathfinding queue counters."),
        feature("KF-038", "Async chunk send", "Configuration", FeatureStatus.COMMAND, "/keila features chunk-send", "Shows async chunk-send state and rollback path."),
        feature("KF-039", "Async playerdata save", "Configuration", FeatureStatus.COMMAND, "/keila features playerdata-save", "Shows async playerdata save state and rollback path."),
        feature("KF-040", "Async tracker", "Configuration", FeatureStatus.COMMAND, "/keila features tracker", "Shows async entity tracker state and thread count."),
        feature("KF-041", "Parallel world ticking", "Configuration", FeatureStatus.COMMAND, "/keila features parallel-worlds", "Shows parallel world ticking state and safeguards."),
        feature("KF-042", "Virtual thread pools", "Configuration", FeatureStatus.COMMAND, "/keila features virtual-threads", "Shows Java 21 virtual-thread pool settings."),
        feature("KF-043", "Rollout check", "Operations", FeatureStatus.COMMAND, "/keila features rollout-check", "Warns about high-risk enabled features."),
        feature("KF-044", "Safe mode profile", "Operations", FeatureStatus.COMMAND, "/keila features safe-mode", "Prints conservative rollback settings."),
        feature("KF-045", "Network report", "Network", FeatureStatus.COMMAND, "/keila features network", "Shows bind, port, online players, and ping pressure."),
        feature("KF-046", "Mob density report", "Entities", FeatureStatus.COMMAND, "/keila features mob-density", "Shows living entity pressure per loaded chunk."),
        feature("KF-047", "World file report", "Worlds", FeatureStatus.COMMAND, "/keila features world-files <world>", "Shows region, POI, and entity file counts."),
        feature("KF-048", "Plugin author report", "Plugins", FeatureStatus.COMMAND, "/keila features plugin-authors", "Groups installed plugins by declared authors."),
        feature("KF-049", "Support bundle", "Operations", FeatureStatus.COMMAND, "/keila features support-bundle", "Prints compact facts for bug reports."),
        feature("KF-050", "Command help", "Operations", FeatureStatus.COMMAND, "/keila features command-help", "Shows feature command examples.")
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
