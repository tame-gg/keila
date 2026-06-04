package gg.tame.keila.command.subcommands;

import gg.tame.keila.command.PermissionedKeilaSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public final class ListCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "list";

    public ListCommand() {
        super(FeaturesCommand.PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.listFeatures(sender, args);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.tabCompleteList(sender, args);
    }

}
