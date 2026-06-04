package gg.tame.keila.startup;

import gg.tame.keila.version.KeilaVersionFetcher;
import io.papermc.paper.ServerBuildInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

public final class KeilaWelcome {

    private static final ServerBuildInfo BUILD_INFO = ServerBuildInfo.buildInfo();

    private KeilaWelcome() {
    }

    public static void sendRootHelp(CommandSender sender) {
        sender.sendMessage(Component.text("Keila", NamedTextColor.AQUA, TextDecoration.BOLD)
            .append(Component.text(" — tame.gg server fork", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("  " + BUILD_INFO.asString(VERSION_SIMPLE), NamedTextColor.WHITE));
        sender.sendMessage(Component.empty());

        sender.sendMessage(Component.text("  Version", NamedTextColor.GOLD)
            .append(Component.text("  /keila version", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Performance", NamedTextColor.GOLD)
            .append(Component.text("  /keila perf · /keila mspt", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Health", NamedTextColor.GOLD)
            .append(Component.text("  /keila health · /keila rollout", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Config", NamedTextColor.GOLD)
            .append(Component.text("  /keila reload", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Diagnostics", NamedTextColor.GOLD)
            .append(Component.text("  /keila list", NamedTextColor.YELLOW))
            .append(Component.text(" (" + KeilaStartup.featureCommandCount() + " tools)", NamedTextColor.DARK_GRAY))
            .append(Component.text(" · /keila info <#|title>", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Support", NamedTextColor.GOLD)
            .append(Component.text("  /keila export · /keila safe", NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text("  Site", NamedTextColor.GOLD)
            .append(Component.text("  " + KeilaVersionFetcher.DOWNLOAD_PAGE, NamedTextColor.GREEN, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.openUrl(KeilaVersionFetcher.DOWNLOAD_PAGE))));
        sender.sendMessage(Component.text("  Deprecated: /features and /keila features (use /keila list)", NamedTextColor.DARK_GRAY));
    }
}
