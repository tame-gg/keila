package gg.tame.keila.command.subcommands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeaturesInfoResolveTest {

    @Test
    void resolvesByListNumber() {
        var resolved = FeaturesCommand.resolveActionQuery(new String[] {"4"});
        assertTrue(resolved.isPresent());
        assertEquals("mspt", resolved.get().action().key());
    }

    @Test
    void resolvesByPartialTitle() {
        var resolved = FeaturesCommand.resolveActionQuery(new String[] {"health"});
        assertTrue(resolved.isPresent());
        assertEquals("health", resolved.get().action().key());
    }

    @Test
    void resolvesPlayerDetailsWithArgs() {
        var resolved = FeaturesCommand.resolveActionQuery(new String[] {"20", "Steve"});
        assertTrue(resolved.isPresent());
        assertEquals("player", resolved.get().action().key());
        assertEquals(1, resolved.get().handlerArgs().length);
        assertEquals("Steve", resolved.get().handlerArgs()[0]);
    }
}
