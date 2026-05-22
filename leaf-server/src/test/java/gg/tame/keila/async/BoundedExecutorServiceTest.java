package gg.tame.keila.async;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundedExecutorServiceTest {

    @Test
    void rejectsInvalidConcurrencyLimit() {
        ExecutorService delegate = Executors.newSingleThreadExecutor();

        try {
            assertThrows(IllegalArgumentException.class, () -> new BoundedExecutorService(delegate, 0));
        } finally {
            delegate.shutdownNow();
        }
    }

    @Test
    void waitsForPermitBeforeSubmittingMoreWork() throws Exception {
        BoundedExecutorService executor = new BoundedExecutorService(Executors.newCachedThreadPool(), 1);
        CountDownLatch firstStarted = new CountDownLatch(1);
        CountDownLatch releaseFirst = new CountDownLatch(1);
        AtomicBoolean secondStarted = new AtomicBoolean(false);

        try {
            executor.execute(() -> {
                firstStarted.countDown();
                await(releaseFirst);
            });
            assertTrue(firstStarted.await(1, TimeUnit.SECONDS));

            Thread submitter = new Thread(() -> executor.execute(() -> secondStarted.set(true)));
            submitter.start();

            Thread.sleep(100L);
            assertFalse(secondStarted.get());
            assertTrue(submitter.isAlive());

            releaseFirst.countDown();
            submitter.join(1_000L);

            executor.shutdown();
            assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
            assertTrue(secondStarted.get());
        } finally {
            executor.shutdownNow();
        }
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }
    }
}
