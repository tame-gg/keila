package gg.tame.keila.startup;

import gg.tame.keila.command.KeilaCommand;
import gg.tame.keila.config.modules.misc.StartupExperience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One-time-per-session operator tip on first join.
 */
public final class KeilaOperatorWelcome {

    private static final Set<UUID> WELCOMED = ConcurrentHashMap.newKeySet();

    private KeilaOperatorWelcome() {
    }

    public static void onPlayerJoin(Player player) {
        if (!StartupExperience.enabled || !StartupExperience.operatorWelcome) {
            return;
        }
        if (!player.isOp() && !player.hasPermission(KeilaCommand.BASE_PERM)) {
            return;
        }
        if (!WELCOMED.add(player.getUniqueId())) {
            return;
        }

        double mspt = KeilaStartup.averageMspt(MinecraftServer.getServer().tickTimes5s);
        String msptText = mspt <= 0.0D ? "warming up" : String.format("%.1fms (5s avg)", mspt);

        player.sendMessage(Component.text("Keila ", NamedTextColor.AQUA)
            .append(Component.text("— operator quick start", NamedTextColor.GOLD)));
        player.sendMessage(Component.text("  MSPT: ", NamedTextColor.GRAY)
            .append(Component.text(msptText, NamedTextColor.GREEN))
            .append(Component.text(" · ", NamedTextColor.DARK_GRAY))
            .append(Component.text(KeilaStartup.featureCommandCount() + " diagnostics — /keila list", NamedTextColor.YELLOW)));
        player.sendMessage(Component.text("  /keila health", NamedTextColor.GRAY)
            .append(Component.text(" · ", NamedTextColor.DARK_GRAY))
            .append(Component.text("/keila perf", NamedTextColor.GRAY))
            .append(Component.text(" · ", NamedTextColor.DARK_GRAY))
            .append(Component.text("/keila", NamedTextColor.GRAY)));
    }
}
