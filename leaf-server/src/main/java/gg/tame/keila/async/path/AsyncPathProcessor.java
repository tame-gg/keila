package gg.tame.keila.async.path;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.util.Util;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gg.tame.keila.config.modules.async.AsyncPathfinding;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

/**
 * used to handle the scheduling of async path processing
 */
public class AsyncPathProcessor {

    private static final String THREAD_PREFIX = "Keila Async Pathfinding";
    private static final Logger LOGGER = LogManager.getLogger(THREAD_PREFIX);
    private static long lastWarnMillis = System.currentTimeMillis();
    public static @Nullable ThreadPoolExecutor PATH_PROCESSING_EXECUTOR = null;
    private static final LongAdder QUEUED_TASKS = new LongAdder();
    private static final LongAdder COMPLETED_TASKS = new LongAdder();
    private static final LongAdder TIMED_OUT_TASKS = new LongAdder();
    private static final LongAdder REJECTED_TASKS = new LongAdder();
    private static final LongAdder CALLER_RUNS_TASKS = new LongAdder();

    public static void init() {
        if (PATH_PROCESSING_EXECUTOR == null) {
            PATH_PROCESSING_EXECUTOR = new ThreadPoolExecutor(
                getCorePoolSize(),
                getMaxPoolSize(),
                getKeepAliveTime(), TimeUnit.SECONDS,
                getQueueImpl(),
                getThreadFactory(),
                getRejectedPolicy()
            );
        } else {
            // Temp no-op
            //throw new IllegalStateException();
        }
    }

    protected static CompletableFuture<Void> queue(Runnable path) {
        QUEUED_TASKS.increment();
        return CompletableFuture.runAsync(() -> {
                try {
                    path.run();
                } finally {
                    COMPLETED_TASKS.increment();
                }
            }, PATH_PROCESSING_EXECUTOR)
            .orTimeout(AsyncPathfinding.asyncPathfindingTimeoutSeconds, TimeUnit.SECONDS)
            .exceptionally(throwable -> {
                Throwable cause = throwable instanceof java.util.concurrent.CompletionException && throwable.getCause() != null ? throwable.getCause() : throwable;
                if (cause instanceof TimeoutException e) {
                    TIMED_OUT_TASKS.increment();
                    LOGGER.warn("Async Pathfinding process timed out", e);
                } else LOGGER.warn("Error occurred while processing async path", cause);
                return null;
            });
    }

    /**
     * takes a possibly unprocessed path, and waits until it is completed
     * the consumer will be immediately invoked if the path is already processed
     * the consumer will always be called on the main thread
     *
     * @param path            a path to wait on
     * @param afterProcessing a consumer to be called
     */
    public static void awaitProcessing(@Nullable Path path, Consumer<@Nullable Path> afterProcessing) {
        if (path != null && !path.isProcessed() && path instanceof AsyncPath asyncPath) {
            asyncPath.schedulePostProcessing(afterProcessing); // Reduce double lambda allocation
        } else {
            afterProcessing.accept(path);
        }
    }

    private static int getCorePoolSize() {
        return 1;
    }

    private static int getMaxPoolSize() {
        return AsyncPathfinding.asyncPathfindingMaxThreads;
    }

    private static long getKeepAliveTime() {
        return AsyncPathfinding.asyncPathfindingKeepalive;
    }

    private static BlockingQueue<Runnable> getQueueImpl() {
        final int queueCapacity = AsyncPathfinding.asyncPathfindingQueueSize;

        return new LinkedBlockingQueue<>(queueCapacity);
    }

    private static ThreadFactory getThreadFactory() {
        return new ThreadFactoryBuilder()
            .setNameFormat(THREAD_PREFIX + " Thread - %d")
            .setPriority(Thread.NORM_PRIORITY - 2)
            .setUncaughtExceptionHandler(Util::onThreadException)
            .build();
    }

    private static RejectedExecutionHandler getRejectedPolicy() {
        return (Runnable rejectedTask, ThreadPoolExecutor executor) -> {
            BlockingQueue<Runnable> workQueue = executor.getQueue();
            if (!executor.isShutdown()) {
                REJECTED_TASKS.increment();
                switch (AsyncPathfinding.asyncPathfindingRejectPolicy) {
                    case FLUSH_ALL -> {
                        if (!workQueue.isEmpty()) {
                            List<Runnable> pendingTasks = new ArrayList<>(workQueue.size());

                            workQueue.drainTo(pendingTasks);

                            for (Runnable pendingTask : pendingTasks) {
                                CALLER_RUNS_TASKS.increment();
                                pendingTask.run();
                            }
                        }
                        CALLER_RUNS_TASKS.increment();
                        rejectedTask.run();
                    }
                    case CALLER_RUNS -> {
                        CALLER_RUNS_TASKS.increment();
                        rejectedTask.run();
                    }
                }
            }

            if (System.currentTimeMillis() - lastWarnMillis > 30000L) {
                LOGGER.warn("Async pathfinding processor is busy! Pathfinding tasks will be treated as policy defined in config. Increasing max-threads in Keila config may help.");
                lastWarnMillis = System.currentTimeMillis();
            }
        };
    }

    public static long queuedTasks() {
        return QUEUED_TASKS.sum();
    }

    public static long completedTasks() {
        return COMPLETED_TASKS.sum();
    }

    public static long timedOutTasks() {
        return TIMED_OUT_TASKS.sum();
    }

    public static long rejectedTasks() {
        return REJECTED_TASKS.sum();
    }

    public static long callerRunsTasks() {
        return CALLER_RUNS_TASKS.sum();
    }
}
