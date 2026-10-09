package gg.tame.palladium.startup;

import gg.tame.palladium.command.PalladiumCommand;
import gg.tame.palladium.config.modules.misc.StartupExperience;
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
public final class PalladiumOperatorWelcome {

    private static final Set<UUID> WELCOMED = ConcurrentHashMap.newKeySet();

    private PalladiumOperatorWelcome() {
    }

    public static void onPlayerJoin(Player player) {
        if (!StartupExperience.enabled || !StartupExperience.operatorWelcome) {
            return;
        }
        if (!player.isOp() && !player.hasPermission(PalladiumCommand.BASE_PERM)) {
            return;
        }
        if (!WELCOMED.add(player.getUniqueId())) {
            return;
        }

        double mspt = PalladiumStartup.averageMspt(MinecraftServer.getServer().tickTimes5s);
        String msptText = mspt <= 0.0D ? "warming up" : String.format("%.1fms (5s avg)", mspt);

        player.sendMessage(Component.text("Palladium ", NamedTextColor.AQUA)
            .append(Component.text("— operator quick start", NamedTextColor.GOLD)));
        player.sendMessage(Component.text("  MSPT: ", NamedTextColor.GRAY)
            .append(Component.text(msptText, NamedTextColor.GREEN))
            .append(Component.text(" · ", NamedTextColor.DARK_GRAY))
            .append(Component.text(PalladiumStartup.featureCommandCount() + " diagnostics — /palladium list", NamedTextColor.YELLOW)));
        player.sendMessage(Component.text("  /palladium health", NamedTextColor.GRAY)
            .append(Component.text(" · ", NamedTextColor.DARK_GRAY))
            .append(Component.text("/palladium perf", NamedTextColor.GRAY))
            .append(Component.text(" · ", NamedTextColor.DARK_GRAY))
            .append(Component.text("/palladium", NamedTextColor.GRAY)));
    }
}
