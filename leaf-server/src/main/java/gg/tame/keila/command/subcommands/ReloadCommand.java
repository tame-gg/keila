package gg.tame.keila.command.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import gg.tame.keila.command.KeilaCommand;
import gg.tame.keila.command.PermissionedKeilaSubcommand;
import gg.tame.keila.config.KeilaConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.permissions.PermissionDefault;

public final class ReloadCommand extends PermissionedKeilaSubcommand {

    public final static String LITERAL_ARGUMENT = "reload";
    public static final String PERM = KeilaCommand.BASE_PERM + "." + LITERAL_ARGUMENT;

    public ReloadCommand() {
        super(PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        this.doGaleReload(sender);
        this.doKeilaReload(sender);
        return true;
    }

    // Gale start - Gale commands - /gale reload command
    private void doGaleReload(final CommandSender sender) {
        Command.broadcastCommandMessage(sender, Component.text("Reloading Gale config...", NamedTextColor.GREEN));

        MinecraftServer server = ((CraftServer) sender.getServer()).getServer();
        server.galeConfigurations.reloadConfigs(server);
        server.server.reloadCount++;

        Command.broadcastCommandMessage(sender, Component.text("Gale config reload complete.", NamedTextColor.GREEN));
    }
    // Gale end - Gale commands - /gale reload command

    private void doKeilaReload(final CommandSender sender) {
        Command.broadcastCommandMessage(sender, Component.text("Reloading Keila config...", NamedTextColor.GREEN));

        KeilaConfig.reloadAsync(sender);
    }
}
