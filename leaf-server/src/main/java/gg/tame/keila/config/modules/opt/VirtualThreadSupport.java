package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class VirtualThreadSupport extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".use-virtual-thread";
    }

    public static boolean bukkitAsyncScheduler = false;
    public static boolean foliaAsyncScheduler = false;
    public static boolean asyncChatExecutor = true;
    public static boolean downloadPool = true;
    public static boolean authPool = true;
    public static boolean paperConfigurationPool = true;
    public static int downloadPoolMaxConcurrency = 4;
    public static int authPoolMaxConcurrency = 16;

    @Override
    public void onLoaded() {
        bukkitAsyncScheduler = config.getBoolean(getBasePath() + ".bukkit-async-scheduler", bukkitAsyncScheduler,
            config.pickStringRegionBased(
                "Use the new Virtual Thread introduced in JDK 21 for CraftAsyncScheduler.",
                "是否为 Bukkit 异步任务调度器使用虚拟线程."));
        foliaAsyncScheduler = config.getBoolean(getBasePath() + ".folia-async-scheduler", foliaAsyncScheduler,
            config.pickStringRegionBased(
                "Use the new Virtual Thread introduced in JDK 21 for FoliaAsyncScheduler.",
                "是否为 Folia 异步任务调度器使用虚拟线程."));
        asyncChatExecutor = config.getBoolean(getBasePath() + ".async-chat-executor", asyncChatExecutor,
            config.pickStringRegionBased(
                "Use the new Virtual Thread introduced in JDK 21 for Async Chat Executor.",
                "是否为异步聊天线程使用虚拟线程."));
        downloadPool = config.getBoolean(getBasePath() + ".download-pool", downloadPool,
            config.pickStringRegionBased(
                "Use the new Virtual Thread introduced in JDK 21 for profile fetching executor.",
                "是否为档案查询执行器使用虚拟线程。"));
        downloadPoolMaxConcurrency = Math.max(1, config.getInt(getBasePath() + ".download-pool-max-concurrency", downloadPoolMaxConcurrency,
            config.pickStringRegionBased(
                "Maximum concurrent profile download tasks when the virtual thread download pool is enabled.",
                "启用虚拟线程下载池时的最大并发档案下载任务数。")));
        authPool = config.getBoolean(getBasePath() + ".auth-pool", authPool,
            config.pickStringRegionBased(
                "Use the new Virtual Thread introduced in JDK 21 for user authentication.",
                "是否为用户验证使用虚拟线程."));
        authPoolMaxConcurrency = Math.max(1, config.getInt(getBasePath() + ".auth-pool-max-concurrency", authPoolMaxConcurrency,
            config.pickStringRegionBased(
                "Maximum concurrent user authentication tasks when the virtual thread auth pool is enabled.",
                "启用虚拟线程验证池时的最大并发用户验证任务数。")));
        paperConfigurationPool = config.getBoolean(getBasePath() + ".paper-configuration-pool", paperConfigurationPool,
            config.pickStringRegionBased(
                "Use the new Virtual Thread introduced in JDK 21 for Paper task pool in configuration phase.",
                "是否为 Paper 在配置阶段的任务池使用虚拟线程."));
    }
}
