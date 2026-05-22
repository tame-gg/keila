package gg.tame.keila.command.subcommands;

import net.minecraft.server.MinecraftServer;
import gg.tame.keila.command.KeilaCommand;
import gg.tame.keila.command.PermissionedKeilaSubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public final class VersionCommand extends PermissionedKeilaSubcommand {

    public final static String LITERAL_ARGUMENT = "version";
    public static final String PERM = KeilaCommand.BASE_PERM + "." + LITERAL_ARGUMENT;

    public VersionCommand() {
        super(PERM, PermissionDefault.TRUE);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        final Command ver = MinecraftServer.getServer().server.getCommandMap().getCommand("version");

        if (ver != null) {
            ver.execute(sender, KeilaCommand.COMMAND_LABEL, me.titaniumtown.ArrayConstants.emptyStringArray); // Gale - JettPack - reduce array allocations
        }

        return true;
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        return super.testPermission(sender) && sender.hasPermission("bukkit.command.version");
    }
}
