package gg.tame.palladium.config.modules.async;

import gg.tame.palladium.async.world.UnsafeReadPolicy;
import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;
import gg.tame.palladium.config.PalladiumConfig;
import gg.tame.palladium.config.annotations.Experimental;
import gg.tame.palladium.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class SparklyPaperParallelWorldTicking extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".parallel-world-ticking";
    }

    @Experimental
    public static boolean enabled = false;
    public static int threads = 8;
    public static boolean logContainerCreationStacktraces = false;
    public static boolean disableHardThrow = false;
    @Deprecated
    public static Boolean runAsyncTasksSync;
    public static UnsafeReadPolicy asyncUnsafeReadHandling = UnsafeReadPolicy.DISABLED;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Experimental Palladium parallel world ticking for multi-core hosts. High plugin-interaction risk;
                see docs/palladium/runtime-safety.md and /palladium list (parallel world ticking).""",
            """
                Palladium 实验性并行世界 tick，适合多核主机。插件交互风险高；
                参见 docs/palladium/runtime-safety.md 与 /palladium parallel-worlds。""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);
        threads = config.getInt(getBasePath() + ".threads", threads);
        if (enabled) {
            if (threads <= 0) threads = 8;
        } else {
            threads = 0;
        }

        logContainerCreationStacktraces = config.getBoolean(getBasePath() + ".log-container-creation-stacktraces", logContainerCreationStacktraces);
        logContainerCreationStacktraces = enabled && logContainerCreationStacktraces;
        disableHardThrow = config.getBoolean(getBasePath() + ".disable-hard-throw", disableHardThrow);
        disableHardThrow = enabled && disableHardThrow;
        asyncUnsafeReadHandling = UnsafeReadPolicy.fromString(config.getString(getBasePath() + ".async-unsafe-read-handling", asyncUnsafeReadHandling.toString()));

        // Transfer old config
        runAsyncTasksSync = config.getBoolean(getBasePath() + ".run-async-tasks-sync");
        if (runAsyncTasksSync != null && runAsyncTasksSync) {
            PalladiumConfig.LOGGER.warn("The setting '{}.run-async-tasks-sync' is deprecated, removed automatically. Use 'async-unsafe-read-handling: BUFFERED' for buffered reads instead.", getBasePath());
        }

        if (enabled) {
            PalladiumConfig.LOGGER.info("Using {} threads for Parallel World Ticking", threads);
        }
    }
}
