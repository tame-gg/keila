package gg.tame.palladium.config.modules.async;

import gg.tame.palladium.async.tracker.AsyncTracker;
import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;
import gg.tame.palladium.config.PalladiumConfig;
import gg.tame.palladium.config.annotations.Experimental;
import gg.tame.palladium.config.annotations.HotReloadUnsupported;

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
                Requires packet-order regression tests; see docs/palladium/runtime-safety.md.""", """
                实验性功能：在后台线程执行实体跟踪，实体密集时收益明显。
                需通过数据包顺序回归测试，参见 docs/palladium/runtime-safety.md。""");

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

        if (enabled) {
            PalladiumConfig.LOGGER.info("Using {} threads for Async Entity Tracker", threads);
            AsyncTracker.init();
        }
    }
}
