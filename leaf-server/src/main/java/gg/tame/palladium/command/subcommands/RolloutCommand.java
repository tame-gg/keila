package gg.tame.palladium.command.subcommands;

import gg.tame.palladium.command.PermissionedPalladiumSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public final class RolloutCommand extends PermissionedPalladiumSubcommand {

    public static final String LITERAL_ARGUMENT = "rollout";
    private static final String ACTION_KEY = "rollout-check";

    public RolloutCommand() {
        super(FeaturesCommand.PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        return FeaturesCommand.runAction(sender, ACTION_KEY, args);
    }
}
