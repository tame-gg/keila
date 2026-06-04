package gg.tame.keila.command;

import gg.tame.keila.command.subcommands.FeaturesCommand;
import gg.tame.keila.startup.KeilaWelcome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Deprecated root {@code /features} — redirects to {@code /keila list} (no key passthrough).
 */
public final class FeaturesAliasCommand extends Command {

    public static final String COMMAND_LABEL = "features";
    /** Same permission node as diagnostics ({@link gg.tame.keila.command.subcommands.FeaturesCommand#PERM}). */
    public static final String PERM = gg.tame.keila.command.subcommands.FeaturesCommand.PERM;
    private static final Permission permission = new Permission(PERM, PermissionDefault.OP);

    public FeaturesAliasCommand() {
        super(COMMAND_LABEL);
        this.description = "Deprecated alias for Keila diagnostics (use /keila list)";
        this.usageMessage = "/keila list";
        this.setPermission(PERM);
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Bukkit.permissionMessage());
            return true;
        }
        sender.sendMessage(Component.text("/features is deprecated — use /keila or /keila list", NamedTextColor.YELLOW));
        if (args.length > 0) {
            sender.sendMessage(Component.text("Diagnostic keys are not accepted. Run /keila list, then /keila info <#|title>.", NamedTextColor.GRAY));
            return true;
        }
        KeilaWelcome.sendRootHelp(sender);
        return FeaturesCommand.listFeatures(sender, new String[0]);
    }

    @Override
    public List<String> tabComplete(
        final CommandSender sender,
        final String alias,
        final String[] args,
        final @Nullable Location location
    ) {
        if (!sender.hasPermission(permission)) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}
