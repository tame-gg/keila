package gg.tame.keila.async;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BoundedExecutorService extends AbstractExecutorService {

    private final ExecutorService delegate;
    private final Semaphore permits;

    public BoundedExecutorService(ExecutorService delegate, int maxConcurrentTasks) {
        if (maxConcurrentTasks <= 0) {
            throw new IllegalArgumentException("maxConcurrentTasks must be positive");
        }
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.permits = new Semaphore(maxConcurrentTasks);
    }

    @Override
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> droppedTasks = this.delegate.shutdownNow();
        for (Runnable task : droppedTasks) {
            if (task instanceof PermitReleasingRunnable permitReleasingRunnable) {
                permitReleasingRunnable.releasePermit();
            }
        }
        return droppedTasks;
    }

    @Override
    public boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        Objects.requireNonNull(command, "command");
        this.permits.acquireUninterruptibly();
        try {
            this.delegate.execute(new PermitReleasingRunnable(command));
        } catch (RuntimeException | Error throwable) {
            this.permits.release();
            throw throwable;
        }
    }

    private final class PermitReleasingRunnable implements Runnable {

        private final Runnable delegate;
        private final AtomicBoolean permitReleased = new AtomicBoolean();

        private PermitReleasingRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                this.delegate.run();
            } finally {
                this.releasePermit();
            }
        }

        private void releasePermit() {
            if (this.permitReleased.compareAndSet(false, true)) {
                BoundedExecutorService.this.permits.release();
            }
        }
    }
}
