package gg.tame.keila.command.subcommands;

import gg.tame.keila.command.KeilaCommand;
import gg.tame.keila.command.PermissionedKeilaSubcommand;
import gg.tame.keila.feature.KeilaFeature;
import gg.tame.keila.feature.KeilaFeatureCatalog;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class FeaturesCommand extends PermissionedKeilaSubcommand {

    public static final String LITERAL_ARGUMENT = "features";
    public static final String PERM = KeilaCommand.BASE_PERM + "." + LITERAL_ARGUMENT;

    public FeaturesCommand() {
        super(PERM, PermissionDefault.OP);
    }

    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(text("Keila Feature Foundation", GOLD));
            sender.sendMessage(text("  features: ", GRAY).append(text(Integer.toString(KeilaFeatureCatalog.all().size()), YELLOW)));
            sender.sendMessage(text("  categories: ", GRAY).append(text(String.join(", ", KeilaFeatureCatalog.categories()), YELLOW)));
            sender.sendMessage(text("Use /keila features <category|KF-###> for details.", GRAY));
            return true;
        }

        final String query = String.join(" ", args).toLowerCase(Locale.ROOT);
        final String id = query.toUpperCase(Locale.ROOT);
        if (id.matches("KF-\\d{3}")) {
            KeilaFeatureCatalog.byId(id).ifPresentOrElse(
                feature -> sendFeature(sender, feature),
                () -> sender.sendMessage(text("Unknown Keila feature id: " + id, GRAY))
            );
            return true;
        }

        List<KeilaFeature> matches = KeilaFeatureCatalog.all().stream()
            .filter(feature -> feature.category().toLowerCase(Locale.ROOT).equals(query))
            .toList();
        if (matches.isEmpty()) {
            matches = KeilaFeatureCatalog.all().stream()
                .filter(feature -> feature.category().toLowerCase(Locale.ROOT).contains(query))
                .toList();
        }

        if (matches.isEmpty()) {
            sender.sendMessage(text("No Keila feature category matched: " + query, GRAY));
            return true;
        }

        sender.sendMessage(text("Keila Features: " + matches.getFirst().category(), GOLD));
        for (KeilaFeature feature : matches) {
            sender.sendMessage(text("  " + feature.id() + " ", AQUA)
                .append(text(feature.title(), YELLOW))
                .append(text(" -> " + feature.surface(), GRAY)));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String subCommand, final String[] args) {
        if (args.length != 1) {
            return List.of();
        }
        List<String> completions = new ArrayList<>();
        completions.addAll(KeilaFeatureCatalog.categories().stream().map(category -> category.toLowerCase(Locale.ROOT)).toList());
        completions.addAll(KeilaFeatureCatalog.all().stream().map(KeilaFeature::id).toList());
        return completions;
    }

    private static void sendFeature(CommandSender sender, KeilaFeature feature) {
        sender.sendMessage(text(feature.id() + " " + feature.title(), GOLD));
        sender.sendMessage(line("category", feature.category()));
        sender.sendMessage(line("status", feature.status().name()));
        sender.sendMessage(line("surface", feature.surface()));
        sender.sendMessage(text("  " + feature.description(), GRAY));
    }

    private static Component line(String key, String value) {
        return text("  " + key + ": ", GRAY).append(text(value, YELLOW));
    }
}
