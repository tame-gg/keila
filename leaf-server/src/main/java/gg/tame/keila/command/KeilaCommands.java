package gg.tame.keila.command;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.util.permissions.CraftDefaultPermissions;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public final class KeilaCommands {

    public static final String COMMAND_BASE_PERM = CraftDefaultPermissions.KEILA_ROOT + ".command";

    private KeilaCommands() {
    }

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put(KeilaCommand.COMMAND_LABEL, new KeilaCommand());
        COMMANDS.put(FeaturesAliasCommand.COMMAND_LABEL, new FeaturesAliasCommand());
    }

    public static void registerCommands(final MinecraftServer server) {
        FeaturesAliasCommand.registerPermission(server.server.getPluginManager());
        COMMANDS.forEach((s, command) -> server.server.getCommandMap().register(s, "Keila", command));
    }
}
