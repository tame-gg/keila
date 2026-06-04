package gg.tame.keila.command.subcommands;

import gg.tame.keila.command.PermissionedKeilaSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public final class InfoCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "info";

    public InfoCommand() {
        super(FeaturesCommand.PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.executeInfo(sender, args);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.tabCompleteInfo(sender, args);
    }
}
