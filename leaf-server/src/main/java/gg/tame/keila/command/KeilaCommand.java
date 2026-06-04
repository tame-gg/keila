package gg.tame.keila.command;



import io.papermc.paper.command.CommandUtil;

import it.unimi.dsi.fastutil.Pair;

import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.format.NamedTextColor;

import net.minecraft.util.Util;

import gg.tame.keila.command.subcommands.ExportCommand;

import gg.tame.keila.command.subcommands.FeaturesCommand;

import gg.tame.keila.command.subcommands.HealthCommand;

import gg.tame.keila.command.subcommands.InfoCommand;

import gg.tame.keila.command.subcommands.ListCommand;

import gg.tame.keila.command.subcommands.MSPTCommand;

import gg.tame.keila.command.subcommands.PerfCommand;

import gg.tame.keila.command.subcommands.ReloadCommand;

import gg.tame.keila.command.subcommands.RolloutCommand;

import gg.tame.keila.command.subcommands.SafeCommand;

import gg.tame.keila.command.subcommands.VersionCommand;

import gg.tame.keila.startup.KeilaWelcome;

import org.jspecify.annotations.Nullable;

import org.bukkit.Bukkit;

import org.bukkit.Location;

import org.bukkit.command.Command;

import org.bukkit.command.CommandSender;

import org.bukkit.permissions.Permission;

import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collection;

import java.util.Collections;

import java.util.HashMap;

import java.util.LinkedHashSet;

import java.util.List;

import java.util.Locale;

import java.util.Map;

import java.util.Set;

import java.util.stream.Collectors;



public final class KeilaCommand extends Command {



    public static final String COMMAND_LABEL = "keila";

    public static final String BASE_PERM = KeilaCommands.COMMAND_BASE_PERM + "." + COMMAND_LABEL;

    private static final Permission basePermission = new Permission(BASE_PERM, PermissionDefault.OP);

    private static final KeilaSubcommand RELOAD_SUBCOMMAND = new ReloadCommand();

    private static final KeilaSubcommand VERSION_SUBCOMMAND = new VersionCommand();

    private static final KeilaSubcommand MSPT_SUBCOMMAND = new MSPTCommand();

    private static final KeilaSubcommand PERF_SUBCOMMAND = new PerfCommand();

    private static final KeilaSubcommand FEATURES_SUBCOMMAND = new FeaturesCommand();

    private static final KeilaSubcommand HEALTH_SUBCOMMAND = new HealthCommand();

    private static final KeilaSubcommand LIST_SUBCOMMAND = new ListCommand();

    private static final KeilaSubcommand INFO_SUBCOMMAND = new InfoCommand();

    private static final KeilaSubcommand EXPORT_SUBCOMMAND = new ExportCommand();

    private static final KeilaSubcommand SAFE_SUBCOMMAND = new SafeCommand();

    private static final KeilaSubcommand ROLLOUT_SUBCOMMAND = new RolloutCommand();

    private static final Map<String, KeilaSubcommand> SUBCOMMANDS = Util.make(() -> {

        final Map<Set<String>, KeilaSubcommand> commands = new HashMap<>();



        commands.put(Set.of(ReloadCommand.LITERAL_ARGUMENT), RELOAD_SUBCOMMAND);

        commands.put(Set.of(VersionCommand.LITERAL_ARGUMENT), VERSION_SUBCOMMAND);

        commands.put(Set.of(MSPTCommand.LITERAL_ARGUMENT), MSPT_SUBCOMMAND);

        commands.put(Set.of(PerfCommand.LITERAL_ARGUMENT), PERF_SUBCOMMAND);

        commands.put(Set.of(FeaturesCommand.LITERAL_ARGUMENT), FEATURES_SUBCOMMAND);

        commands.put(Set.of(HealthCommand.LITERAL_ARGUMENT), HEALTH_SUBCOMMAND);

        commands.put(Set.of(ListCommand.LITERAL_ARGUMENT), LIST_SUBCOMMAND);

        commands.put(Set.of(InfoCommand.LITERAL_ARGUMENT), INFO_SUBCOMMAND);

        commands.put(Set.of(ExportCommand.LITERAL_ARGUMENT), EXPORT_SUBCOMMAND);

        commands.put(Set.of(SafeCommand.LITERAL_ARGUMENT), SAFE_SUBCOMMAND);

        commands.put(Set.of(RolloutCommand.LITERAL_ARGUMENT), ROLLOUT_SUBCOMMAND);



        return commands.entrySet().stream()

            .flatMap(entry -> entry.getKey().stream().map(s -> Map.entry(s, entry.getValue())))

            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    });

    private static final Map<String, String> ALIASES = Util.make(() -> {

        final Map<String, Set<String>> aliases = new HashMap<>();



        aliases.put(VersionCommand.LITERAL_ARGUMENT, Set.of("ver"));

        aliases.put(SafeCommand.LITERAL_ARGUMENT, Set.of("safe-mode"));

        aliases.put(RolloutCommand.LITERAL_ARGUMENT, Set.of("rollout-check"));



        return aliases.entrySet().stream()

            .flatMap(entry -> entry.getValue().stream().map(s -> Map.entry(s, entry.getKey())))

            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    });

    /** Subcommand labels shown in /keila help (excludes deprecated {@code features}). */

    private static final List<String> HELP_SUBCOMMANDS = List.of(

        VersionCommand.LITERAL_ARGUMENT,

        PerfCommand.LITERAL_ARGUMENT,

        MSPTCommand.LITERAL_ARGUMENT,

        HealthCommand.LITERAL_ARGUMENT,

        RolloutCommand.LITERAL_ARGUMENT,

        ListCommand.LITERAL_ARGUMENT,

        InfoCommand.LITERAL_ARGUMENT,

        ExportCommand.LITERAL_ARGUMENT,

        SafeCommand.LITERAL_ARGUMENT,

        ReloadCommand.LITERAL_ARGUMENT

    );



    private String createUsageMessage(Collection<String> arguments) {

        return "/" + COMMAND_LABEL + " [" + String.join(" | ", arguments) + "]";

    }



    public KeilaCommand() {
        super(COMMAND_LABEL);
        this.description = "Keila related commands";
        this.usageMessage = this.createUsageMessage(HELP_SUBCOMMANDS);
        this.setPermission(BASE_PERM);
    }

    static java.util.Collection<Permission> permissionsToRegister() {
        final Map<String, Permission> byName = new HashMap<>();
        byName.put(basePermission.getName(), basePermission);
        for (final KeilaSubcommand subcommand : SUBCOMMANDS.values()) {
            final Permission permission = subcommand.getPermission();
            if (permission != null) {
                byName.putIfAbsent(permission.getName(), permission);
            }
        }
        return byName.values();
    }



    @Override

    public List<String> tabComplete(

        final CommandSender sender,

        final String alias,

        final String[] args,

        final @Nullable Location location

    ) throws IllegalArgumentException {

        if (args.length <= 1) {

            Set<String> completions = new LinkedHashSet<>();

            for (Map.Entry<String, KeilaSubcommand> subCommandEntry : SUBCOMMANDS.entrySet()) {

                if (subCommandEntry.getValue().testPermission(sender)) {

                    completions.add(subCommandEntry.getKey());

                }

            }

            for (Map.Entry<String, String> aliasEntry : ALIASES.entrySet()) {

                KeilaSubcommand subcommand = SUBCOMMANDS.get(aliasEntry.getValue());

                if (subcommand != null && subcommand.testPermission(sender)) {

                    completions.add(aliasEntry.getKey());

                }

            }

            return CommandUtil.getListMatchingLast(sender, args, new ArrayList<>(completions));

        }



        final Pair<String, KeilaSubcommand> subCommand = resolveCommand(args[0]);



        if (subCommand != null && subCommand.second().testPermission(sender)) {

            return subCommand.second().tabComplete(sender, subCommand.first(), Arrays.copyOfRange(args, 1, args.length));

        }



        return Collections.emptyList();

    }



    private boolean testHasOnePermission(CommandSender sender) {

        if (sender.hasPermission(basePermission)) {

            return true;

        }

        for (Map.Entry<String, KeilaSubcommand> subCommandEntry : SUBCOMMANDS.entrySet()) {

            if (subCommandEntry.getValue().testPermission(sender)) {

                return true;

            }

        }

        return false;

    }



    @Override

    public boolean execute(

        final CommandSender sender,

        final String commandLabel,

        final String[] args

    ) {

        if (!this.testHasOnePermission(sender)) {

            sender.sendMessage(Bukkit.permissionMessage());

            return true;

        }



        if (args.length == 0) {

            KeilaWelcome.sendRootHelp(sender);

            return true;

        }



        final Pair<String, KeilaSubcommand> subCommand = resolveCommand(args[0]);

        if (subCommand != null) {

            if (!subCommand.second().testPermission(sender)) {

                sender.sendMessage(Component.text("You do not have permission for /" + COMMAND_LABEL + " " + subCommand.first(), NamedTextColor.RED));

                return false;

            }

            final String[] choppedArgs = Arrays.copyOfRange(args, 1, args.length);

            return subCommand.second().execute(sender, subCommand.first(), choppedArgs);

        }



        sender.sendMessage(Component.text("Unknown /" + COMMAND_LABEL + " subcommand. Run /" + COMMAND_LABEL + " for help.", NamedTextColor.RED));

        sender.sendMessage(Component.text("Tip: /keila list then /keila info <#|title> — keys are not command literals.", NamedTextColor.GRAY));

        return false;

    }



    private static @Nullable Pair<String, KeilaSubcommand> resolveCommand(String label) {

        label = label.toLowerCase(Locale.ENGLISH);

        KeilaSubcommand subCommand = SUBCOMMANDS.get(label);

        if (subCommand == null) {

            final String command = ALIASES.get(label);

            if (command != null) {

                label = command;

                subCommand = SUBCOMMANDS.get(command);

            }

        }



        if (subCommand != null) {

            return Pair.of(label, subCommand);

        }



        return null;

    }

}


