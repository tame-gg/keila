package gg.tame.palladium.startup;

import gg.tame.palladium.version.PalladiumVersionFetcher;
import io.papermc.paper.ServerBuildInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

public final class PalladiumWelcome {

    private static final ServerBuildInfo BUILD_INFO = ServerBuildInfo.buildInfo();

    private PalladiumWelcome() {
    }

    public static void sendRootHelp(CommandSender sender) {
        sender.sendMessage(Component.text("Palladium", NamedTextColor.AQUA, TextDecoration.BOLD)
            .append(Component.text(" — tame.gg server fork", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("  " + BUILD_INFO.asString(VERSION_SIMPLE), NamedTextColor.WHITE));
        sender.sendMessage(Component.empty());

        sender.sendMessage(Component.text("  Version", NamedTextColor.GOLD)
            .append(Component.text("  /palladium version", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Performance", NamedTextColor.GOLD)
            .append(Component.text("  /palladium perf · /palladium mspt", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Health", NamedTextColor.GOLD)
            .append(Component.text("  /palladium health · /palladium rollout", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Config", NamedTextColor.GOLD)
            .append(Component.text("  /palladium reload", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Diagnostics", NamedTextColor.GOLD)
            .append(Component.text("  /palladium list", NamedTextColor.YELLOW))
            .append(Component.text(" (" + PalladiumStartup.featureCommandCount() + " tools)", NamedTextColor.DARK_GRAY))
            .append(Component.text(" · /palladium info <#|title>", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Support", NamedTextColor.GOLD)
            .append(Component.text("  /palladium export · /palladium safe", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Site", NamedTextColor.GOLD)
            .append(Component.text("  " + PalladiumVersionFetcher.DOWNLOAD_PAGE, NamedTextColor.GREEN, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.openUrl(PalladiumVersionFetcher.DOWNLOAD_PAGE))));
        sender.sendMessage(Component.text("  Deprecated: /features and /palladium features (use /palladium list)", NamedTextColor.DARK_GRAY));
    }
}
