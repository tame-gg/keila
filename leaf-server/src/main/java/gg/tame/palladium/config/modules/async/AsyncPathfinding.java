package gg.tame.palladium.config.modules.async;

import gg.tame.palladium.async.path.PathfindTaskRejectPolicy;
import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;
import gg.tame.palladium.config.PalladiumConfig;
import gg.tame.palladium.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class AsyncPathfinding extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".async-pathfinding";
    }

    public static boolean enabled = false;
    public static int asyncPathfindingMaxThreads = 0;
    public static int asyncPathfindingKeepalive = 60;
    public static int asyncPathfindingQueueSize = 0;
    public static int asyncPathfindingTimeoutSeconds = 60;
    public static PathfindTaskRejectPolicy asyncPathfindingRejectPolicy = PathfindTaskRejectPolicy.FLUSH_ALL;
    private static boolean asyncPathfindingInitialized;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Offload pathfinding to worker threads with bounded queues. Inspect via /palladium perf queues.
                High-risk: see docs/palladium/runtime-safety.md.""",
            """
                在 worker 线程上执行寻路并使用有界队列。通过 /palladium perf queues 查看状态。
                高风险：参见 docs/palladium/runtime-safety.md。""");
        config.addCommentRegionBased(getBasePath() + ".reject-policy", """
                The policy to use when the queue is full and a new task is submitted.
                FLUSH_ALL: All pending tasks will be run on server thread.
                CALLER_RUNS: Newly submitted task will be run on server thread.""",
            """
                当队列满时, 新提交的任务将使用以下策略处理.
                FLUSH_ALL: 所有等待中的任务都将在主线程上运行.
                CALLER_RUNS: 新提交的任务将在主线程上运行."""
        );
        if (asyncPathfindingInitialized) {
            config.getConfigSection(getBasePath());
            return;
        }
        asyncPathfindingInitialized = true;

        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);
        asyncPathfindingMaxThreads = config.getInt(getBasePath() + ".max-threads", asyncPathfindingMaxThreads);
        asyncPathfindingKeepalive = config.getInt(getBasePath() + ".keepalive", asyncPathfindingKeepalive);
        asyncPathfindingQueueSize = config.getInt(getBasePath() + ".queue-size", asyncPathfindingQueueSize);
        asyncPathfindingTimeoutSeconds = config.getInt(getBasePath() + ".timeout-seconds", asyncPathfindingTimeoutSeconds);

        if (asyncPathfindingMaxThreads <= 0) {
            asyncPathfindingMaxThreads = Math.max(availableProcessors / 4, 1);
        }

        if (!enabled) {
            asyncPathfindingMaxThreads = 0;
        }

        if (asyncPathfindingQueueSize <= 0) {
            asyncPathfindingQueueSize = asyncPathfindingMaxThreads * 256;
        }

        if (asyncPathfindingTimeoutSeconds <= 0) {
            asyncPathfindingTimeoutSeconds = 60;
        }

        asyncPathfindingRejectPolicy = PathfindTaskRejectPolicy.fromString(config.getString(getBasePath() + ".reject-policy",
            availableProcessors >= 12 && asyncPathfindingQueueSize < 512
                ? PathfindTaskRejectPolicy.FLUSH_ALL.toString()
                : PathfindTaskRejectPolicy.CALLER_RUNS.toString())
        );

        if (enabled) {
            PalladiumConfig.LOGGER.info("Using {} threads for Async Pathfinding", asyncPathfindingMaxThreads);
            gg.tame.palladium.async.path.AsyncPathProcessor.init();
        }
    }
}
