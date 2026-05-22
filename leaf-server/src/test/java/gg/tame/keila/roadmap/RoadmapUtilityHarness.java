package gg.tame.keila.roadmap;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class RoadmapUtilityHarness {

    private RoadmapUtilityHarness() {
    }

    public static void main(String[] args) {
        adaptiveCompressionUsesLoadTiers();
        spawnWaveSchedulerIsDeterministicAndBounded();
        utf8StringCacheReusesEncodedBytes();
        dirtyIndexSetDrainsInAscendingOrder();
    }

    private static void adaptiveCompressionUsesLoadTiers() {
        AdaptiveCompressionController controller = new AdaptiveCompressionController(20.0D, 35.0D, 6, 3, 1);

        assertEquals(6, controller.levelForMspt(12.0D), "low MSPT should use bandwidth-saving compression");
        assertEquals(3, controller.levelForMspt(27.0D), "middle MSPT should use balanced compression");
        assertEquals(1, controller.levelForMspt(42.0D), "high MSPT should use CPU-saving compression");
    }

    private static void spawnWaveSchedulerIsDeterministicAndBounded() {
        SpawnWaveScheduler scheduler = new SpawnWaveScheduler(4);

        int wave = scheduler.waveFor(-3, 7);
        assertTrue(wave >= 0 && wave < 4, "wave must stay inside configured wave count");
        assertEquals(wave, scheduler.waveFor(-3, 7), "same chunk must stay on same wave");
        assertTrue(scheduler.shouldRunThisTick(-3, 7, wave), "matching tick should run");
        assertFalse(scheduler.shouldRunThisTick(-3, 7, wave + 1), "non-matching tick should not run");
    }

    private static void utf8StringCacheReusesEncodedBytes() {
        Utf8StringCache cache = new Utf8StringCache(4);

        byte[] first = cache.encoded("minecraft:zombie");
        byte[] second = cache.encoded("minecraft:zombie");
        byte[] other = cache.encoded("minecraft:skeleton");

        assertSame(first, second, "same string should reuse cached UTF-8 bytes");
        assertTrue(Arrays.equals("minecraft:zombie".getBytes(StandardCharsets.UTF_8), first), "bytes must match UTF-8 encoding");
        assertFalse(first == other, "different strings need different byte arrays");
    }

    private static void dirtyIndexSetDrainsInAscendingOrder() {
        DirtyIndexSet dirty = new DirtyIndexSet(8);

        dirty.mark(5);
        dirty.mark(2);
        dirty.mark(5);

        assertEquals(2, dirty.count(), "duplicate marks should not increase count");
        assertTrue(dirty.isDirty(5), "marked index should be dirty");
        assertArrayEquals(new int[] {2, 5}, dirty.drain(), "drain should be stable and sorted");
        assertEquals(0, dirty.count(), "drain should clear dirty state");
        assertFalse(dirty.isDirty(5), "drained index should not stay dirty");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ": expected " + expected + " but got " + actual);
        }
    }

    private static void assertSame(Object expected, Object actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message);
        }
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message + ": expected " + Arrays.toString(expected) + " but got " + Arrays.toString(actual));
        }
    }

    private static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean value, String message) {
        if (value) {
            throw new AssertionError(message);
        }
    }
}
