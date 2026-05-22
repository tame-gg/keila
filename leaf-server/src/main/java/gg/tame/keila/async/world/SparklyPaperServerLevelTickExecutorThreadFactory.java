package gg.tame.keila.async.world;

import ca.spottedleaf.moonrise.common.util.TickThread;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.ThreadFactory;

@NullMarked
public class SparklyPaperServerLevelTickExecutorThreadFactory implements ThreadFactory {

    private final String worldName;

    public SparklyPaperServerLevelTickExecutorThreadFactory(final String worldName) {
        this.worldName = worldName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        TickThread.ServerLevelTickThread tickThread = new TickThread.ServerLevelTickThread(runnable, "Keila Level Ticking Thread - " + this.worldName);

        if (tickThread.isDaemon()) {
            tickThread.setDaemon(false);
        }

        tickThread.setPriority(Thread.NORM_PRIORITY + 1);
        if (Runtime.getRuntime().availableProcessors() > 4) {
            tickThread.setPriority(7);
        }

        return tickThread;
    }
}
