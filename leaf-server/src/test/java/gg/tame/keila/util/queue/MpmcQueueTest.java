package gg.tame.keila.util.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class MpmcQueueTest {

    @Test
    void returnsNullWhenReceivingFromEmptyQueue() {
        MpmcQueue<String> queue = new MpmcQueue<>(String.class, 2);

        assertNull(queue.recv());
        assertEquals(0, queue.length());
        assertEquals(1, queue.remaining());
    }

    @Test
    void sendsAndReceivesInOrder() {
        MpmcQueue<String> queue = new MpmcQueue<>(String.class, 4);

        queue.send("first");
        queue.send("second");

        assertEquals("first", queue.recv());
        assertEquals("second", queue.recv());
        assertNull(queue.recv());
        assertEquals(0, queue.length());
    }

    @Test
    void rejectsWhenRingIsFull() {
        MpmcQueue<String> queue = new MpmcQueue<>(String.class, 2);

        queue.send("first");

        assertFalse(queue.send("second"));
    }
}
