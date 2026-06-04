package gg.tame.keila.command;

import gg.tame.keila.command.subcommands.ExportCommand;
import gg.tame.keila.command.subcommands.HealthCommand;
import gg.tame.keila.command.subcommands.InfoCommand;
import gg.tame.keila.command.subcommands.ListCommand;
import gg.tame.keila.command.subcommands.RolloutCommand;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeilaCommandStructureTest {

    @Test
    void namedSubcommandsExcludeCatalogKeys() {
        Set<String> named = Set.of(
            "reload", "version", "ver", "mspt", "perf",
            "health", "list", "info", "export", "safe", "safe-mode",
            "rollout", "rollout-check", "features"
        );
        assertFalse(named.contains("summary"));
        assertFalse(named.contains("kf-002"));
        assertFalse(named.contains("chunk-send"));
        assertTrue(named.contains(HealthCommand.LITERAL_ARGUMENT));
        assertTrue(named.contains(ListCommand.LITERAL_ARGUMENT));
        assertTrue(named.contains(InfoCommand.LITERAL_ARGUMENT));
        assertTrue(named.contains(ExportCommand.LITERAL_ARGUMENT));
        assertEquals("rollout", RolloutCommand.LITERAL_ARGUMENT);
    }
}
