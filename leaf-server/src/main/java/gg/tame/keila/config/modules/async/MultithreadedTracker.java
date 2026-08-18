package gg.tame.keila.config.modules.async;

import org.dreeam.leaf.async.tracker.AsyncTracker;
import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.KeilaConfig;
import gg.tame.keila.config.annotations.Experimental;
import gg.tame.keila.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class MultithreadedTracker extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".async-entity-tracker";
    }

    @Experimental
    public static boolean enabled = false;
    public static int threads = 0;
    private static boolean asyncMultithreadedTrackerInitialized;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Experimental: run entity tracking off the main thread. Can help when many entities share few chunks.
                Requires packet-order regression tests; see docs/keila/runtime-safety.md.""", """
                实验性功能：在后台线程执行实体跟踪，实体密集时收益明显。
                需通过数据包顺序回归测试，参见 docs/keila/runtime-safety.md。""");

        if (asyncMultithreadedTrackerInitialized) {
            config.getConfigSection(getBasePath());
            return;
        }
        asyncMultithreadedTrackerInitialized = true;

        enabled = config.getBoolean(getBasePath() + ".enabled", false);
        threads = config.getInt(getBasePath() + ".threads", 0);

        if (threads <= 0) {
            threads = Math.min(Runtime.getRuntime().availableProcessors(), 4);
        }
        threads = Math.max(threads, 1);

        // Keila - config bridge: Keila's config drives Leaf's AsyncTracker implementation.
        // Propagate Keila's values into Leaf's config statics before AsyncTracker class-loads
        // (its ENABLED/THREADS/TRACKER_EXECUTOR are captured on first use).
        org.dreeam.leaf.config.modules.async.MultithreadedTracker.enabled = enabled;
        org.dreeam.leaf.config.modules.async.MultithreadedTracker.threads = threads;

        if (enabled) {
            KeilaConfig.LOGGER.info("Using {} threads for Async Entity Tracker", threads);
            AsyncTracker.init();
        }
    }
}
