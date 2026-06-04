package gg.tame.keila.command.subcommands;

import gg.tame.keila.command.PermissionedKeilaSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public final class HealthCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "health";

    public HealthCommand() {
        super(FeaturesCommand.PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.runAction(sender, "health", args);
    }
}
