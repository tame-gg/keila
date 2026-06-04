package gg.tame.keila.config.modules.async;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class AsyncMobSpawning extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".async-mob-spawning";
    }

    public static boolean enabled = true;
    private static boolean asyncMobSpawningInitialized;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Offload mob-spawn eligibility work to a background thread (mobs still spawn on the main thread).
                Requires paper.yml per-player-mob-spawns. Distinct from performance.optimize-mob-spawning and
                performance.throttle-mob-spawning.""",
            """
                在后台线程计算生物生成条件（实际生成仍在主线程）。
                需要 paper.yml 开启 per-player-mob-spawns。与 performance.optimize-mob-spawning、
                performance.throttle-mob-spawning 不同。""");

        // This prevents us from changing the value during a reload.
        if (asyncMobSpawningInitialized) {
            config.getConfigSection(getBasePath());
            return;
        }
        asyncMobSpawningInitialized = true;

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);
    }
}
