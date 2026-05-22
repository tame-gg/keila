package gg.tame.keila.command;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.util.permissions.CraftDefaultPermissions;

import java.util.HashMap;
import java.util.Map;

public final class KeilaCommands {

    public static final String COMMAND_BASE_PERM = CraftDefaultPermissions.KEILA_ROOT + ".command";

    private KeilaCommands() {
    }

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put(KeilaCommand.COMMAND_LABEL, new KeilaCommand());
    }

    public static void registerCommands(final MinecraftServer server) {
        COMMANDS.forEach((s, command) -> server.server.getCommandMap().register(s, "Keila", command));
    }
}
