package gg.tame.keila.command.subcommands;

import ca.spottedleaf.moonrise.common.time.TickData;
import gg.tame.keila.async.path.AsyncPathProcessor;
import gg.tame.keila.command.KeilaCommand;
import gg.tame.keila.command.PermissionedKeilaSubcommand;
import gg.tame.keila.config.modules.async.AsyncChunkSend;
import gg.tame.keila.config.modules.async.AsyncPathfinding;
import gg.tame.keila.config.modules.async.AsyncPlayerDataSave;
import gg.tame.keila.config.modules.async.MultithreadedTracker;
import gg.tame.keila.config.modules.async.SparklyPaperParallelWorldTicking;
import gg.tame.keila.config.modules.opt.VirtualThreadSupport;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class FeaturesCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "features";
    public static final String PERM = KeilaCommand.BASE_PERM + "." + LITERAL_ARGUMENT;
    private static final DecimalFormat ONE_DECIMAL = new DecimalFormat("########0.0");
    private static final List<Path> CONFIG_FILES = List.of(
        Path.of("server.properties"),
        Path.of("bukkit.yml"),
        Path.of("spigot.yml"),
        Path.of("purpur.yml"),
        Path.of("keila.yml"),
        Path.of("config", "paper-global.yml"),
        Path.of("config", "paper-world-defaults.yml")
    );
    private static final List<FeatureAction> ACTIONS = List.of(
        action("KF-001", "summary", "Server summary", "Overview", "Get the current server, player, world, chunk, and entity counts.", FeaturesCommand::serverSummary),
        action("KF-002", "health", "Health score", "Overview", "Classify current tick, memory, and scheduler pressure.", FeaturesCommand::healthScore),
        action("KF-003", "tps", "TPS report", "Performance", "Read 5s, 10s, and 60s server tick health.", FeaturesCommand::tpsReport),
        action("KF-004", "mspt", "MSPT report", "Performance", "Read server MSPT without requiring parallel world ticking.", FeaturesCommand::msptReport),
        action("KF-005", "memory", "Memory report", "Performance", "Inspect JVM heap and non-heap pressure.", FeaturesCommand::memoryReport),
        action("KF-006", "gc", "Request garbage collection", "Performance", "Trigger GC and compare heap usage before and after.", FeaturesCommand::requestGc),
        action("KF-007", "threads", "Thread summary", "Diagnostics", "Count live JVM threads by state.", FeaturesCommand::threadSummary),
        action("KF-008", "thread-states", "Thread state sample", "Diagnostics", "List a bounded sample of current JVM threads.", FeaturesCommand::threadStates),
        action("KF-009", "uptime", "Uptime report", "Diagnostics", "Show JVM uptime and process start metadata.", FeaturesCommand::uptimeReport),
        action("KF-010", "jvm", "JVM report", "Diagnostics", "Show Java, VM, OS, and CPU runtime information.", FeaturesCommand::jvmReport),
        action("KF-011", "disk", "Disk report", "Diagnostics", "Inspect usable and total disk space for the server directory.", FeaturesCommand::diskReport),
        action("KF-012", "worlds", "World list", "Worlds", "List loaded worlds with players, chunks, and entities.", FeaturesCommand::worldsReport),
        action("KF-013", "world", "World details", "Worlds", "Inspect one world by name, or the first loaded world.", FeaturesCommand::worldReport),
        action("KF-014", "chunks", "Chunk report", "Worlds", "Show loaded chunk counts per world.", FeaturesCommand::chunksReport),
        action("KF-015", "chunk-hotspots", "Chunk hotspots", "Worlds", "Find loaded chunks with the highest entity counts.", FeaturesCommand::chunkHotspots),
        action("KF-016", "entities", "Entity report", "Entities", "Show entity counts per world.", FeaturesCommand::entitiesReport),
        action("KF-017", "entity-types", "Entity type top list", "Entities", "Find the most common loaded entity types.", FeaturesCommand::entityTypes),
        action("KF-018", "tile-entities", "Block entity report", "Worlds", "Show loaded block entity counts per world.", FeaturesCommand::tileEntities),
        action("KF-019", "players", "Player list", "Players", "List online players with world and ping.", FeaturesCommand::playersReport),
        action("KF-020", "player", "Player details", "Players", "Inspect one online player by name.", FeaturesCommand::playerReport),
        action("KF-021", "pings", "Ping report", "Players", "Show min, average, max, and slow player pings.", FeaturesCommand::pingReport),
        action("KF-022", "plugins", "Plugin report", "Plugins", "Show enabled and disabled plugin counts.", FeaturesCommand::pluginsReport),
        action("KF-023", "plugin", "Plugin details", "Plugins", "Inspect one plugin by name.", FeaturesCommand::pluginReport),
        action("KF-024", "scheduler", "Scheduler report", "Diagnostics", "Show pending scheduler tasks by plugin and sync mode.", FeaturesCommand::schedulerReport),
        action("KF-025", "permissions", "Permission report", "Diagnostics", "Count registered permissions and defaults.", FeaturesCommand::permissionReport),
        action("KF-026", "gamerules", "Game rule report", "Worlds", "List game rules for one world.", FeaturesCommand::gamerulesReport),
        action("KF-027", "difficulties", "Difficulty report", "Worlds", "Show difficulty and environment for each loaded world.", FeaturesCommand::difficultiesReport),
        action("KF-028", "spawn-limits", "Spawn limit report", "Entities", "Show per-world Bukkit spawn limits.", FeaturesCommand::spawnLimitsReport),
        action("KF-029", "view-distance", "View distance report", "Network", "Show server view and simulation distance.", FeaturesCommand::viewDistanceReport),
        action("KF-030", "recipes", "Recipe count", "Content", "Count currently registered recipes.", FeaturesCommand::recipesReport),
        action("KF-031", "advancements", "Advancement count", "Content", "Count currently registered advancements.", FeaturesCommand::advancementsReport),
        action("KF-032", "scoreboard", "Scoreboard report", "Content", "Inspect main scoreboard objectives and teams.", FeaturesCommand::scoreboardReport),
        action("KF-033", "save-worlds", "Save worlds", "Operations", "Force-save every loaded world and report the result.", FeaturesCommand::saveWorlds),
        action("KF-034", "config-files", "Config file report", "Configuration", "Check expected config files and sizes.", FeaturesCommand::configFiles),
        action("KF-035", "config-search", "Config search", "Configuration", "Search expected config files for a key or value.", FeaturesCommand::configSearch),
        action("KF-036", "async-state", "Async state", "Configuration", "Show high-risk async feature enablement.", FeaturesCommand::asyncState),
        action("KF-037", "path-queue", "Pathfinding queue", "Performance", "Show async pathfinding queue counters.", FeaturesCommand::pathQueue),
        action("KF-038", "chunk-send", "Async chunk send", "Configuration", "Show async chunk-send state and rollout note.", FeaturesCommand::chunkSend),
        action("KF-039", "playerdata-save", "Async playerdata save", "Configuration", "Show async playerdata save state and rollout note.", FeaturesCommand::playerdataSave),
        action("KF-040", "tracker", "Async tracker", "Configuration", "Show async entity tracker state and thread count.", FeaturesCommand::tracker),
        action("KF-041", "parallel-worlds", "Parallel world ticking", "Configuration", "Show parallel world ticking state and safeguards.", FeaturesCommand::parallelWorlds),
        action("KF-042", "virtual-threads", "Virtual thread pools", "Configuration", "Show Java 21 virtual-thread pool settings.", FeaturesCommand::virtualThreads),
        action("KF-043", "rollout-check", "Rollout check", "Operations", "Warn about high-risk features before production rollout.", FeaturesCommand::rolloutCheck),
        action("KF-044", "safe-mode", "Safe mode profile", "Operations", "Print a conservative runtime profile for incident rollback.", FeaturesCommand::safeMode),
        action("KF-045", "network", "Network report", "Network", "Show bind, port, online players, and ping pressure.", FeaturesCommand::networkReport),
        action("KF-046", "mob-density", "Mob density report", "Entities", "Show living entity pressure per loaded chunk.", FeaturesCommand::mobDensity),
        action("KF-047", "world-files", "World file report", "Worlds", "Inspect world folder size and region file count.", FeaturesCommand::worldFiles),
        action("KF-048", "plugin-authors", "Plugin author report", "Plugins", "Group installed plugins by declared authors.", FeaturesCommand::pluginAuthors),
        action("KF-049", "support-bundle", "Support bundle", "Operations", "Print compact support facts for bug reports.", FeaturesCommand::supportBundle),
        action("KF-050", "command-help", "Command help", "Operations", "Show feature command examples and argument forms.", FeaturesCommand::commandHelp)
    );
    private static final Map<String, FeatureAction> ACTIONS_BY_KEY = buildActionLookup(ACTIONS);

    public FeaturesCommand() {
        super(PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            commandHelp(sender, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            listActions(sender, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("categories")) {
            listCategories(sender);
            return true;
        }

        FeatureAction action = ACTIONS_BY_KEY.get(args[0].toLowerCase(Locale.ROOT));
        if (action == null) {
            listCategoryOrUnknown(sender, args[0]);
            return true;
        }

        sendHeader(sender, action.id() + " " + action.title());
        sender.sendMessage(line("use-case", action.useCase()));
        action.handler().run(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String subCommand, final String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("help");
            completions.add("list");
            completions.add("categories");
            completions.addAll(ACTIONS.stream().map(FeatureAction::id).toList());
            completions.addAll(ACTIONS.stream().map(FeatureAction::key).toList());
            completions.addAll(categories());
            return completions;
        }

        if (args.length == 2) {
            String key = args[0].toLowerCase(Locale.ROOT);
            if (key.equals("world") || key.equals("gamerules") || key.equals("world-files")) {
                return Bukkit.getWorlds().stream().map(World::getName).toList();
            }
            if (key.equals("player")) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
            if (key.equals("plugin")) {
                return Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).toList();
            }
            if (key.equals("config-search")) {
                return List.of("async", "enabled", "view-distance", "simulation-distance");
            }
        }

        return List.of();
    }

    private static void listActions(CommandSender sender, String[] args) {
        String category = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)).toLowerCase(Locale.ROOT) : "";
        List<FeatureAction> actions = category.isBlank()
            ? ACTIONS
            : ACTIONS.stream().filter(action -> action.category().toLowerCase(Locale.ROOT).contains(category)).toList();

        if (actions.isEmpty()) {
            sender.sendMessage(text("No feature category matched: " + category, RED));
            return;
        }

        sendHeader(sender, "Keila runnable features (" + actions.size() + ")");
        for (FeatureAction action : actions) {
            sender.sendMessage(text(action.id() + " ", AQUA)
                .append(text(action.key(), YELLOW))
                .append(text(" - " + action.title(), GRAY)));
        }
    }

    private static void listCategories(CommandSender sender) {
        sendHeader(sender, "Keila feature categories");
        for (String category : categories()) {
            long count = ACTIONS.stream().filter(action -> action.category().equals(category)).count();
            sender.sendMessage(line(category, count + " commands"));
        }
    }

    private static void listCategoryOrUnknown(CommandSender sender, String query) {
        String normalized = query.toLowerCase(Locale.ROOT);
        List<FeatureAction> actions = ACTIONS.stream()
            .filter(action -> action.category().toLowerCase(Locale.ROOT).contains(normalized))
            .toList();

        if (actions.isEmpty()) {
            sender.sendMessage(text("Unknown Keila feature: " + query, RED));
            sender.sendMessage(text("Use /keila features list or /keila features help.", GRAY));
            return;
        }

        listActions(sender, new String[] {"list", query});
    }

    private static void commandHelp(CommandSender sender, String[] args) {
        sendHeader(sender, "Keila feature commands");
        sender.sendMessage(line("run", "/keila features <KF-###|key> [args]"));
        sender.sendMessage(line("list", "/keila features list [category]"));
        sender.sendMessage(line("examples", "/keila features health"));
        sender.sendMessage(line("examples", "/keila features player <name>"));
        sender.sendMessage(line("examples", "/keila features config-search async"));
        sender.sendMessage(line("count", ACTIONS.size() + " runnable feature commands"));
    }

    private static void serverSummary(CommandSender sender, String[] args) {
        int chunks = Bukkit.getWorlds().stream().mapToInt(world -> world.getLoadedChunks().length).sum();
        int entities = Bukkit.getWorlds().stream().mapToInt(world -> world.getEntities().size()).sum();
        sender.sendMessage(line("server", Bukkit.getName() + " " + Bukkit.getVersion()));
        sender.sendMessage(line("bukkit", Bukkit.getBukkitVersion()));
        sender.sendMessage(line("players", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
        sender.sendMessage(line("worlds", Integer.toString(Bukkit.getWorlds().size())));
        sender.sendMessage(line("loaded-chunks", Integer.toString(chunks)));
        sender.sendMessage(line("loaded-entities", Integer.toString(entities)));
        sender.sendMessage(line("plugins", Integer.toString(Bukkit.getPluginManager().getPlugins().length)));
    }

    private static void healthScore(CommandSender sender, String[] args) {
        double mspt = tickAverage(MinecraftServer.getServer().tickTimes5s);
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        double heapUsed = runtime.maxMemory() <= 0L ? 0.0D : used * 100.0D / runtime.maxMemory();
        int pendingTasks = Bukkit.getScheduler().getPendingTasks().size();
        String status = mspt >= 50.0D || heapUsed >= 90.0D ? "critical" : mspt >= 40.0D || heapUsed >= 80.0D || pendingTasks > 500 ? "warning" : "ok";

        sender.sendMessage(line("status", status));
        sender.sendMessage(line("avg-mspt-5s", formatMs(mspt)));
        sender.sendMessage(line("heap-used", ONE_DECIMAL.format(heapUsed) + "%"));
        sender.sendMessage(line("pending-tasks", Integer.toString(pendingTasks)));
    }

    private static void tpsReport(CommandSender sender, String[] args) {
        sendTickLine(sender, "5s", MinecraftServer.getServer().tickTimes5s);
        sendTickLine(sender, "10s", MinecraftServer.getServer().tickTimes10s);
        sendTickLine(sender, "60s", MinecraftServer.getServer().tickTimes1m);
    }

    private static void msptReport(CommandSender sender, String[] args) {
        sendMsptLine(sender, "5s", MinecraftServer.getServer().tickTimes5s);
        sendMsptLine(sender, "10s", MinecraftServer.getServer().tickTimes10s);
        sendMsptLine(sender, "60s", MinecraftServer.getServer().tickTimes1m);
    }

    private static void memoryReport(CommandSender sender, String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        sender.sendMessage(line("heap-used", bytes(used)));
        sender.sendMessage(line("heap-free", bytes(runtime.freeMemory())));
        sender.sendMessage(line("heap-total", bytes(runtime.totalMemory())));
        sender.sendMessage(line("heap-max", bytes(runtime.maxMemory())));
        sender.sendMessage(line("processors", Integer.toString(runtime.availableProcessors())));
    }

    private static void requestGc(CommandSender sender, String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();
        System.gc();
        long after = runtime.totalMemory() - runtime.freeMemory();
        sender.sendMessage(line("before", bytes(before)));
        sender.sendMessage(line("after", bytes(after)));
        sender.sendMessage(line("delta", bytes(before - after)));
    }

    private static void threadSummary(CommandSender sender, String[] args) {
        Map<Thread.State, Long> counts = Thread.getAllStackTraces().keySet().stream()
            .collect(Collectors.groupingBy(Thread::getState, LinkedHashMap::new, Collectors.counting()));
        counts.forEach((state, count) -> sender.sendMessage(line(state.name().toLowerCase(Locale.ROOT), Long.toString(count))));
    }

    private static void threadStates(CommandSender sender, String[] args) {
        Thread.getAllStackTraces().keySet().stream()
            .sorted(Comparator.comparing(Thread::getName))
            .limit(20)
            .forEach(thread -> sender.sendMessage(line(thread.getName(), thread.getState().name())));
    }

    private static void uptimeReport(CommandSender sender, String[] args) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        sender.sendMessage(line("uptime", duration(runtime.getUptime())));
        sender.sendMessage(line("started", Long.toString(runtime.getStartTime())));
        sender.sendMessage(line("pid-name", runtime.getName()));
    }

    private static void jvmReport(CommandSender sender, String[] args) {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        sender.sendMessage(line("java", System.getProperty("java.version")));
        sender.sendMessage(line("vm", System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version")));
        sender.sendMessage(line("os", System.getProperty("os.name") + " " + System.getProperty("os.version")));
        sender.sendMessage(line("arch", System.getProperty("os.arch")));
        sender.sendMessage(line("threads", Integer.toString(threads.getThreadCount())));
    }

    private static void diskReport(CommandSender sender, String[] args) {
        Path path = Path.of(".").toAbsolutePath().normalize();
        sender.sendMessage(line("path", path.toString()));
        sender.sendMessage(line("usable", bytes(path.toFile().getUsableSpace())));
        sender.sendMessage(line("free", bytes(path.toFile().getFreeSpace())));
        sender.sendMessage(line("total", bytes(path.toFile().getTotalSpace())));
    }

    private static void worldsReport(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(line(world.getName(), "players=" + world.getPlayers().size() + ", chunks=" + world.getLoadedChunks().length + ", entities=" + world.getEntities().size()));
        }
    }

    private static void worldReport(CommandSender sender, String[] args) {
        Optional<World> world = findWorld(args);
        if (world.isEmpty()) {
            sender.sendMessage(text("World not found.", RED));
            return;
        }
        World value = world.get();
        Location spawn = value.getSpawnLocation();
        sender.sendMessage(line("name", value.getName()));
        sender.sendMessage(line("environment", value.getEnvironment().name()));
        sender.sendMessage(line("difficulty", value.getDifficulty().name()));
        sender.sendMessage(line("players", Integer.toString(value.getPlayers().size())));
        sender.sendMessage(line("loaded-chunks", Integer.toString(value.getLoadedChunks().length)));
        sender.sendMessage(line("entities", Integer.toString(value.getEntities().size())));
        sender.sendMessage(line("spawn", spawn.getBlockX() + "," + spawn.getBlockY() + "," + spawn.getBlockZ()));
        sender.sendMessage(line("time", Long.toString(value.getTime())));
    }

    private static void chunksReport(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(line(world.getName(), world.getLoadedChunks().length + " loaded chunks"));
        }
    }

    private static void chunkHotspots(CommandSender sender, String[] args) {
        Bukkit.getWorlds().stream()
            .flatMap(world -> Arrays.stream(world.getLoadedChunks()).map(chunk -> new ChunkStat(world, chunk, chunk.getEntities().length)))
            .sorted(Comparator.comparingInt(ChunkStat::entities).reversed())
            .limit(10)
            .forEach(stat -> sender.sendMessage(line(stat.world().getName() + " [" + stat.chunk().getX() + "," + stat.chunk().getZ() + "]", stat.entities() + " entities")));
    }

    private static void entitiesReport(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(line(world.getName(), world.getEntities().size() + " entities"));
        }
    }

    private static void entityTypes(CommandSender sender, String[] args) {
        Map<EntityType, Long> counts = Bukkit.getWorlds().stream()
            .flatMap(world -> world.getEntities().stream())
            .collect(Collectors.groupingBy(Entity::getType, Collectors.counting()));
        counts.entrySet().stream()
            .sorted(Map.Entry.<EntityType, Long>comparingByValue().reversed())
            .limit(15)
            .forEach(entry -> sender.sendMessage(line(entry.getKey().name(), Long.toString(entry.getValue()))));
    }

    private static void tileEntities(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            int count = Arrays.stream(world.getLoadedChunks()).mapToInt(chunk -> chunk.getTileEntities().length).sum();
            sender.sendMessage(line(world.getName(), count + " loaded block entities"));
        }
    }

    private static void playersReport(CommandSender sender, String[] args) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            sender.sendMessage(text("No players online.", GRAY));
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            sender.sendMessage(line(player.getName(), player.getWorld().getName() + ", ping=" + player.getPing() + "ms, mode=" + player.getGameMode().name()));
        }
    }

    private static void playerReport(CommandSender sender, String[] args) {
        Optional<Player> player = findPlayer(args);
        if (player.isEmpty()) {
            sender.sendMessage(text("Player not found. Usage: /keila features player <name>", RED));
            return;
        }
        Player value = player.get();
        Location location = value.getLocation();
        sender.sendMessage(line("name", value.getName()));
        sender.sendMessage(line("uuid", value.getUniqueId().toString()));
        sender.sendMessage(line("world", value.getWorld().getName()));
        sender.sendMessage(line("location", location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ()));
        sender.sendMessage(line("ping", value.getPing() + "ms"));
        sender.sendMessage(line("health", ONE_DECIMAL.format(value.getHealth()) + "/" + ONE_DECIMAL.format(value.getMaxHealth())));
        sender.sendMessage(line("gamemode", value.getGameMode().name()));
    }

    private static void pingReport(CommandSender sender, String[] args) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.isEmpty()) {
            sender.sendMessage(text("No players online.", GRAY));
            return;
        }
        IntStats stats = players.stream().mapToInt(Player::getPing).collect(IntStats::new, IntStats::add, IntStats::merge);
        sender.sendMessage(line("players", Integer.toString(players.size())));
        sender.sendMessage(line("min", stats.min + "ms"));
        sender.sendMessage(line("avg", ONE_DECIMAL.format(stats.average()) + "ms"));
        sender.sendMessage(line("max", stats.max + "ms"));
    }

    private static void pluginsReport(CommandSender sender, String[] args) {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        long enabled = Arrays.stream(plugins).filter(Plugin::isEnabled).count();
        sender.sendMessage(line("enabled", Long.toString(enabled)));
        sender.sendMessage(line("disabled", Long.toString(plugins.length - enabled)));
        Arrays.stream(plugins).limit(20).forEach(plugin -> sender.sendMessage(line(plugin.getName(), plugin.isEnabled() ? "enabled" : "disabled")));
    }

    private static void pluginReport(CommandSender sender, String[] args) {
        Optional<Plugin> plugin = findPlugin(args);
        if (plugin.isEmpty()) {
            sender.sendMessage(text("Plugin not found. Usage: /keila features plugin <name>", RED));
            return;
        }
        Plugin value = plugin.get();
        sender.sendMessage(line("name", value.getName()));
        sender.sendMessage(line("version", value.getDescription().getVersion()));
        sender.sendMessage(line("enabled", Boolean.toString(value.isEnabled())));
        sender.sendMessage(line("authors", String.join(", ", value.getDescription().getAuthors())));
        sender.sendMessage(line("main", value.getDescription().getMain()));
    }

    private static void schedulerReport(CommandSender sender, String[] args) {
        List<BukkitTask> tasks = Bukkit.getScheduler().getPendingTasks();
        sender.sendMessage(line("pending", Integer.toString(tasks.size())));
        Map<String, Long> byPlugin = tasks.stream()
            .collect(Collectors.groupingBy(task -> task.getOwner().getName(), Collectors.counting()));
        byPlugin.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> sender.sendMessage(line(entry.getKey(), entry.getValue() + " tasks")));
    }

    private static void permissionReport(CommandSender sender, String[] args) {
        Map<String, Long> defaults = Bukkit.getPluginManager().getPermissions().stream()
            .collect(Collectors.groupingBy(permission -> permission.getDefault().name(), Collectors.counting()));
        sender.sendMessage(line("registered", Integer.toString(Bukkit.getPluginManager().getPermissions().size())));
        defaults.forEach((key, value) -> sender.sendMessage(line(key.toLowerCase(Locale.ROOT), Long.toString(value))));
    }

    private static void gamerulesReport(CommandSender sender, String[] args) {
        Optional<World> world = findWorld(args);
        if (world.isEmpty()) {
            sender.sendMessage(text("World not found. Usage: /keila features gamerules <world>", RED));
            return;
        }
        for (String rule : world.get().getGameRules()) {
            sender.sendMessage(line(rule, Objects.toString(world.get().getGameRuleValue(rule))));
        }
    }

    private static void difficultiesReport(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(line(world.getName(), world.getDifficulty().name() + ", " + world.getEnvironment().name()));
        }
    }

    private static void spawnLimitsReport(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(line(world.getName(), "monsters=" + world.getMonsterSpawnLimit() + ", animals=" + world.getAnimalSpawnLimit() + ", water=" + world.getWaterAnimalSpawnLimit() + ", ambient=" + world.getAmbientSpawnLimit()));
        }
    }

    private static void viewDistanceReport(CommandSender sender, String[] args) {
        sender.sendMessage(line("view-distance", Integer.toString(Bukkit.getViewDistance())));
        sender.sendMessage(line("simulation-distance", Integer.toString(Bukkit.getSimulationDistance())));
    }

    private static void recipesReport(CommandSender sender, String[] args) {
        int count = 0;
        Iterator<?> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        sender.sendMessage(line("recipes", Integer.toString(count)));
    }

    private static void advancementsReport(CommandSender sender, String[] args) {
        int count = 0;
        Iterator<?> iterator = Bukkit.advancementIterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        sender.sendMessage(line("advancements", Integer.toString(count)));
    }

    private static void scoreboardReport(CommandSender sender, String[] args) {
        if (Bukkit.getScoreboardManager() == null) {
            sender.sendMessage(text("Scoreboard manager is not available yet.", RED));
            return;
        }
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        sender.sendMessage(line("objectives", Integer.toString(scoreboard.getObjectives().size())));
        sender.sendMessage(line("teams", Integer.toString(scoreboard.getTeams().size())));
        sender.sendMessage(line("entries", Integer.toString(scoreboard.getEntries().size())));
    }

    private static void saveWorlds(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            world.save();
            sender.sendMessage(line(world.getName(), "saved"));
        }
    }

    private static void configFiles(CommandSender sender, String[] args) {
        for (Path file : CONFIG_FILES) {
            sender.sendMessage(line(file.toString(), Files.isRegularFile(file) ? bytes(file.toFile().length()) : "missing"));
        }
    }

    private static void configSearch(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(text("Usage: /keila features config-search <text>", RED));
            return;
        }
        String needle = String.join(" ", args).toLowerCase(Locale.ROOT);
        int matches = 0;
        for (Path file : CONFIG_FILES) {
            if (!Files.isRegularFile(file)) {
                continue;
            }
            try {
                List<String> lines = Files.readAllLines(file);
                for (int index = 0; index < lines.size(); index++) {
                    if (lines.get(index).toLowerCase(Locale.ROOT).contains(needle)) {
                        sender.sendMessage(line(file + ":" + (index + 1), lines.get(index).trim()));
                        matches++;
                        if (matches >= 20) {
                            sender.sendMessage(text("Stopped after 20 matches.", GRAY));
                            return;
                        }
                    }
                }
            } catch (IOException ex) {
                sender.sendMessage(line(file.toString(), "read failed: " + ex.getMessage()));
            }
        }
        if (matches == 0) {
            sender.sendMessage(text("No config matches found.", GRAY));
        }
    }

    private static void asyncState(CommandSender sender, String[] args) {
        sender.sendMessage(line("async-chunk-send", Boolean.toString(AsyncChunkSend.enabled)));
        sender.sendMessage(line("async-playerdata-save", Boolean.toString(AsyncPlayerDataSave.enabled)));
        sender.sendMessage(line("async-pathfinding", Boolean.toString(AsyncPathfinding.enabled)));
        sender.sendMessage(line("async-entity-tracker", Boolean.toString(MultithreadedTracker.enabled)));
        sender.sendMessage(line("parallel-world-ticking", Boolean.toString(SparklyPaperParallelWorldTicking.enabled)));
    }

    private static void pathQueue(CommandSender sender, String[] args) {
        sender.sendMessage(line("enabled", Boolean.toString(AsyncPathfinding.enabled)));
        sender.sendMessage(line("threads", Integer.toString(AsyncPathfinding.asyncPathfindingMaxThreads)));
        sender.sendMessage(line("queue-size", Integer.toString(AsyncPathfinding.asyncPathfindingQueueSize)));
        sender.sendMessage(line("queued", Long.toString(AsyncPathProcessor.queuedTasks())));
        sender.sendMessage(line("completed", Long.toString(AsyncPathProcessor.completedTasks())));
        sender.sendMessage(line("rejected", Long.toString(AsyncPathProcessor.rejectedTasks())));
        sender.sendMessage(line("caller-runs", Long.toString(AsyncPathProcessor.callerRunsTasks())));
        sender.sendMessage(line("timed-out", Long.toString(AsyncPathProcessor.timedOutTasks())));
    }

    private static void chunkSend(CommandSender sender, String[] args) {
        sender.sendMessage(line("enabled", Boolean.toString(AsyncChunkSend.enabled)));
        sender.sendMessage(line("use-case", "Reduce main-thread chunk packet preparation pressure during joins and flight."));
        sender.sendMessage(line("rollback", "set async.async-chunk-send.enabled=false and restart"));
    }

    private static void playerdataSave(CommandSender sender, String[] args) {
        sender.sendMessage(line("enabled", Boolean.toString(AsyncPlayerDataSave.enabled)));
        sender.sendMessage(line("use-case", "Move playerdata disk writes away from the main thread."));
        sender.sendMessage(line("rollback", "set async.async-playerdata-save.enabled=false and restart"));
    }

    private static void tracker(CommandSender sender, String[] args) {
        sender.sendMessage(line("enabled", Boolean.toString(MultithreadedTracker.enabled)));
        sender.sendMessage(line("threads", Integer.toString(MultithreadedTracker.threads)));
        sender.sendMessage(line("rollback", "set async.async-entity-tracker.enabled=false and restart"));
    }

    private static void parallelWorlds(CommandSender sender, String[] args) {
        sender.sendMessage(line("enabled", Boolean.toString(SparklyPaperParallelWorldTicking.enabled)));
        sender.sendMessage(line("threads", Integer.toString(SparklyPaperParallelWorldTicking.threads)));
        sender.sendMessage(line("unsafe-read-handling", SparklyPaperParallelWorldTicking.asyncUnsafeReadHandling.toString()));
        sender.sendMessage(line("rollback", "set async.parallel-world-ticking.enabled=false and restart"));
    }

    private static void virtualThreads(CommandSender sender, String[] args) {
        sender.sendMessage(line("auth-pool", VirtualThreadSupport.authPool + ", max=" + VirtualThreadSupport.authPoolMaxConcurrency));
        sender.sendMessage(line("download-pool", VirtualThreadSupport.downloadPool + ", max=" + VirtualThreadSupport.downloadPoolMaxConcurrency));
        sender.sendMessage(line("async-chat-executor", Boolean.toString(VirtualThreadSupport.asyncChatExecutor)));
        sender.sendMessage(line("bukkit-async-scheduler", Boolean.toString(VirtualThreadSupport.bukkitAsyncScheduler)));
        sender.sendMessage(line("folia-async-scheduler", Boolean.toString(VirtualThreadSupport.foliaAsyncScheduler)));
    }

    private static void rolloutCheck(CommandSender sender, String[] args) {
        int warnings = 0;
        warnings += warnIf(sender, AsyncChunkSend.enabled, "async chunk send is enabled; validate chunk packet ordering under player flight.");
        warnings += warnIf(sender, AsyncPlayerDataSave.enabled, "async playerdata save is enabled; validate shutdown and crash recovery.");
        warnings += warnIf(sender, MultithreadedTracker.enabled, "async entity tracker is enabled; validate dense entity areas.");
        warnings += warnIf(sender, SparklyPaperParallelWorldTicking.enabled, "parallel world ticking is enabled; validate plugin thread assumptions.");
        sender.sendMessage(line("warnings", Integer.toString(warnings)));
        sender.sendMessage(line("status", warnings == 0 ? "conservative" : "needs staging validation"));
    }

    private static void safeMode(CommandSender sender, String[] args) {
        sender.sendMessage(line("async.async-chunk-send.enabled", "false"));
        sender.sendMessage(line("async.async-playerdata-save.enabled", "false"));
        sender.sendMessage(line("async.async-pathfinding.enabled", "false"));
        sender.sendMessage(line("async.async-entity-tracker.enabled", "false"));
        sender.sendMessage(line("async.parallel-world-ticking.enabled", "false"));
        sender.sendMessage(line("restart", "required after changing hot-reload-unsupported async settings"));
    }

    private static void networkReport(CommandSender sender, String[] args) {
        sender.sendMessage(line("ip", Bukkit.getIp().isBlank() ? "*" : Bukkit.getIp()));
        sender.sendMessage(line("port", Integer.toString(Bukkit.getPort())));
        sender.sendMessage(line("online", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
        pingReport(sender, args);
    }

    private static void mobDensity(CommandSender sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            int chunks = Math.max(world.getLoadedChunks().length, 1);
            long living = world.getEntities().stream().filter(entity -> entity instanceof org.bukkit.entity.LivingEntity).count();
            sender.sendMessage(line(world.getName(), ONE_DECIMAL.format((double) living / chunks) + " living entities/chunk"));
        }
    }

    private static void worldFiles(CommandSender sender, String[] args) {
        Optional<World> world = findWorld(args);
        if (world.isEmpty()) {
            sender.sendMessage(text("World not found. Usage: /keila features world-files <world>", RED));
            return;
        }
        Path folder = world.get().getWorldFolder().toPath();
        sender.sendMessage(line("folder", folder.toString()));
        sender.sendMessage(line("region-files", Long.toString(countFiles(folder.resolve("region"), ".mca"))));
        sender.sendMessage(line("poi-files", Long.toString(countFiles(folder.resolve("poi"), ".mca"))));
        sender.sendMessage(line("entity-files", Long.toString(countFiles(folder.resolve("entities"), ".mca"))));
    }

    private static void pluginAuthors(CommandSender sender, String[] args) {
        Map<String, Long> counts = new HashMap<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            List<String> authors = plugin.getDescription().getAuthors();
            if (authors.isEmpty()) {
                counts.merge("unknown", 1L, Long::sum);
            } else {
                for (String author : authors) {
                    counts.merge(author, 1L, Long::sum);
                }
            }
        }
        counts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(15)
            .forEach(entry -> sender.sendMessage(line(entry.getKey(), entry.getValue() + " plugins")));
    }

    private static void supportBundle(CommandSender sender, String[] args) {
        serverSummary(sender, args);
        sender.sendMessage(line("avg-mspt-5s", formatMs(tickAverage(MinecraftServer.getServer().tickTimes5s))));
        sender.sendMessage(line("async-pathfinding", Boolean.toString(AsyncPathfinding.enabled)));
        sender.sendMessage(line("parallel-world-ticking", Boolean.toString(SparklyPaperParallelWorldTicking.enabled)));
        sender.sendMessage(line("java", System.getProperty("java.version")));
    }

    private static void sendTickLine(CommandSender sender, String label, TickData data) {
        double mspt = tickAverage(data);
        double tps = mspt <= 0.0D ? 20.0D : Math.min(20.0D, 1000.0D / mspt);
        sender.sendMessage(line(label, ONE_DECIMAL.format(tps) + " TPS"));
    }

    private static void sendMsptLine(CommandSender sender, String label, TickData data) {
        sender.sendMessage(line(label, formatMs(tickAverage(data))));
    }

    private static double tickAverage(TickData tickData) {
        TickData.TickReportData reportData = tickData.generateTickReport(null, System.nanoTime(), MinecraftServer.getServer().tickRateManager().nanosecondsPerTick());
        return reportData == null ? 0.0D : reportData.timePerTickData().segmentAll().average() * 1.0E-6D;
    }

    private static String formatMs(double value) {
        return ONE_DECIMAL.format(value) + "ms";
    }

    private static Optional<World> findWorld(String[] args) {
        if (args.length == 0) {
            return Bukkit.getWorlds().stream().findFirst();
        }
        String query = String.join(" ", args);
        return Bukkit.getWorlds().stream().filter(world -> world.getName().equalsIgnoreCase(query)).findFirst();
    }

    private static Optional<Player> findPlayer(String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }
        String query = String.join(" ", args);
        return Bukkit.getOnlinePlayers().stream()
            .filter(player -> player.getName().equalsIgnoreCase(query))
            .map(Player.class::cast)
            .findFirst();
    }

    private static Optional<Plugin> findPlugin(String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }
        String query = String.join(" ", args);
        return Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin.getName().equalsIgnoreCase(query)).findFirst();
    }

    private static int warnIf(CommandSender sender, boolean condition, String message) {
        if (condition) {
            sender.sendMessage(text("warning: " + message, YELLOW));
            return 1;
        }
        return 0;
    }

    private static long countFiles(Path directory, String suffix) {
        if (!Files.isDirectory(directory)) {
            return 0L;
        }
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.filter(path -> path.getFileName().toString().endsWith(suffix)).count();
        } catch (IOException ignored) {
            return 0L;
        }
    }

    private static String bytes(long bytes) {
        double value = bytes;
        List<String> units = List.of("B", "KiB", "MiB", "GiB", "TiB");
        int unit = 0;
        while (Math.abs(value) >= 1024.0D && unit < units.size() - 1) {
            value /= 1024.0D;
            unit++;
        }
        return ONE_DECIMAL.format(value) + " " + units.get(unit);
    }

    private static String duration(long millis) {
        Duration duration = Duration.ofMillis(millis);
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }

    private static List<String> categories() {
        return ACTIONS.stream().map(FeatureAction::category).distinct().sorted().toList();
    }

    private static void sendHeader(CommandSender sender, String title) {
        sender.sendMessage(text("Keila - " + title, GOLD));
    }

    private static Component line(String key, String value) {
        return text("  " + key + ": ", GRAY).append(text(value, value.equalsIgnoreCase("critical") ? RED : value.equalsIgnoreCase("warning") ? YELLOW : GREEN));
    }

    private static FeatureAction action(String id, String key, String title, String category, String useCase, FeatureHandler handler) {
        return new FeatureAction(id, key, title, category, useCase, handler);
    }

    private static Map<String, FeatureAction> buildActionLookup(List<FeatureAction> actions) {
        Map<String, FeatureAction> lookup = new LinkedHashMap<>();
        for (FeatureAction action : actions) {
            lookup.put(action.id().toLowerCase(Locale.ROOT), action);
            lookup.put(action.key().toLowerCase(Locale.ROOT), action);
        }
        return lookup;
    }

    private record FeatureAction(String id, String key, String title, String category, String useCase, FeatureHandler handler) {
    }

    private record ChunkStat(World world, Chunk chunk, int entities) {
    }

    private interface FeatureHandler {
        void run(CommandSender sender, String[] args);
    }

    private static final class IntStats {
        private int count;
        private int total;
        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;

        private void add(int value) {
            count++;
            total += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        private void merge(IntStats other) {
            count += other.count;
            total += other.total;
            min = Math.min(min, other.min);
            max = Math.max(max, other.max);
        }

        private double average() {
            return count == 0 ? 0.0D : (double) total / count;
        }
    }
}
