package gg.tame.palladium.command;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.util.permissions.CraftDefaultPermissions;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public final class PalladiumCommands {

    public static final String COMMAND_BASE_PERM = CraftDefaultPermissions.PALLADIUM_ROOT + ".command";

    private PalladiumCommands() {
    }

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put(PalladiumCommand.COMMAND_LABEL, new PalladiumCommand());
        COMMANDS.put(FeaturesAliasCommand.COMMAND_LABEL, new FeaturesAliasCommand());
    }

    public static void registerCommands(final MinecraftServer server) {
        final PluginManager pluginManager = server.server.getPluginManager();
        registerPermissions(pluginManager);
        COMMANDS.forEach((s, command) -> server.server.getCommandMap().register(s, "Palladium", command));
    }

    private static void registerPermissions(final PluginManager pluginManager) {
        for (final Permission permission : PalladiumCommand.permissionsToRegister()) {
            if (pluginManager.getPermission(permission.getName()) == null) {
                pluginManager.addPermission(permission);
            }
        }
    }
}
