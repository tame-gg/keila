package gg.tame.keila.command.subcommands;

import gg.tame.keila.command.PermissionedKeilaSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public final class ExportCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "export";

    public ExportCommand() {
        super(FeaturesCommand.PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.runAction(sender, LITERAL_ARGUMENT, args);
    }
}
