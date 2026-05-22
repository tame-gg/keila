package gg.tame.keila.command.subcommands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import gg.tame.keila.async.path.AsyncPathProcessor;
import gg.tame.keila.command.KeilaCommand;
import gg.tame.keila.command.PermissionedKeilaSubcommand;
import gg.tame.keila.config.modules.async.AsyncPathfinding;
import gg.tame.keila.config.modules.async.AsyncChunkSend;
import gg.tame.keila.config.modules.async.AsyncPlayerDataSave;
import gg.tame.keila.config.modules.async.MultithreadedTracker;
import gg.tame.keila.config.modules.async.SparklyPaperParallelWorldTicking;
import gg.tame.keila.config.modules.opt.VirtualThreadSupport;

import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class PerfCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "perf";
    public static final String PERM = KeilaCommand.BASE_PERM + "." + LITERAL_ARGUMENT;

    public PerfCommand() {
        super(PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        final String view = args.length == 0 ? "queues" : args[0].toLowerCase(Locale.ROOT);
        switch (view) {
            case "queues" -> sendQueueMetrics(sender);
            case "async" -> sendAsyncMetrics(sender);
            case "memory" -> sendMemoryMetrics(sender);
            case "all" -> {
                sendQueueMetrics(sender);
                sendAsyncMetrics(sender);
                sendMemoryMetrics(sender);
            }
            default -> sender.sendMessage(text("Usage: /keila perf [queues | async | memory | all]", GRAY));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String subCommand, final String[] args) {
        return args.length == 1 ? List.of("queues", "async", "memory", "all") : List.of();
    }

    private static void sendQueueMetrics(CommandSender sender) {
        sender.sendMessage(Component.text()
            .content("Keila Queue Metrics")
            .color(GOLD)
            .build());
        sender.sendMessage(line("enabled", Boolean.toString(AsyncPathfinding.enabled)));
        sender.sendMessage(line("threads", Integer.toString(AsyncPathfinding.asyncPathfindingMaxThreads)));
        sender.sendMessage(line("queue-size", Integer.toString(AsyncPathfinding.asyncPathfindingQueueSize)));
        sender.sendMessage(line("timeout-seconds", Integer.toString(AsyncPathfinding.asyncPathfindingTimeoutSeconds)));
        sender.sendMessage(line("queued", Long.toString(AsyncPathProcessor.queuedTasks())));
        sender.sendMessage(line("completed", Long.toString(AsyncPathProcessor.completedTasks())));
        sender.sendMessage(line("rejected", Long.toString(AsyncPathProcessor.rejectedTasks())));
        sender.sendMessage(line("caller-runs", Long.toString(AsyncPathProcessor.callerRunsTasks())));
        sender.sendMessage(line("timed-out", Long.toString(AsyncPathProcessor.timedOutTasks())));
    }

    private static void sendAsyncMetrics(CommandSender sender) {
        sender.sendMessage(text("Keila Async Feature State", GOLD));
        sender.sendMessage(line("async-chunk-send", Boolean.toString(AsyncChunkSend.enabled)));
        sender.sendMessage(line("async-playerdata-save", Boolean.toString(AsyncPlayerDataSave.enabled)));
        sender.sendMessage(line("async-pathfinding", Boolean.toString(AsyncPathfinding.enabled)));
        sender.sendMessage(line("multithreaded-tracker", Boolean.toString(MultithreadedTracker.enabled)));
        sender.sendMessage(line("parallel-world-ticking", Boolean.toString(SparklyPaperParallelWorldTicking.enabled)));
        sender.sendMessage(line("virtual-thread-auth-pool", Boolean.toString(VirtualThreadSupport.authPool)));
        sender.sendMessage(line("virtual-thread-auth-max-concurrency", Integer.toString(VirtualThreadSupport.authPoolMaxConcurrency)));
        sender.sendMessage(line("virtual-thread-download-pool", Boolean.toString(VirtualThreadSupport.downloadPool)));
        sender.sendMessage(line("virtual-thread-download-max-concurrency", Integer.toString(VirtualThreadSupport.downloadPoolMaxConcurrency)));
    }

    private static void sendMemoryMetrics(CommandSender sender) {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        sender.sendMessage(text("Keila JVM Memory", GOLD));
        sender.sendMessage(line("used-mb", Long.toString(used / 1024L / 1024L)));
        sender.sendMessage(line("free-mb", Long.toString(runtime.freeMemory() / 1024L / 1024L)));
        sender.sendMessage(line("total-mb", Long.toString(runtime.totalMemory() / 1024L / 1024L)));
        sender.sendMessage(line("max-mb", Long.toString(runtime.maxMemory() / 1024L / 1024L)));
        sender.sendMessage(line("processors", Integer.toString(runtime.availableProcessors())));
    }

    private static Component line(String key, String value) {
        return text("  " + key + ": ", GRAY).append(text(value, YELLOW));
    }
}
