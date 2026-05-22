package gg.tame.keila.async.chunk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AsyncChunkSenderTest {

    @Test
    void emptyReceiveDoesNotChangeInFlightCount() {
        AsyncChunkSender sender = new AsyncChunkSender();

        assertEquals(0, sender.inFlight());
        assertNull(sender.recv());
        assertEquals(0, sender.inFlight());
    }

    @Test
    void clearResetsPendingChunksAndInFlightCount() {
        AsyncChunkSender sender = new AsyncChunkSender();

        sender.add(1L);
        sender.add(2L);
        sender.markInFlightForTesting();
        sender.markInFlightForTesting();

        sender.clear();

        assertEquals(0, sender.inFlight());
        assertEquals(0, sender.pendingChunks());
    }
}
