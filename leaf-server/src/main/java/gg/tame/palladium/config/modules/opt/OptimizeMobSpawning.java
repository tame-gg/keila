package gg.tame.palladium.config.modules.opt;

import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;
import gg.tame.palladium.config.annotations.Experimental;

public class OptimizeMobSpawning extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimize-mob-spawning";
    }

    @Experimental
    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath(), enabled, config.pickStringRegionBased(
            "Main-thread micro-optimizations for natural mob spawning (not async.async-mob-spawning).",
            "主线程自然生物生成微优化（不是 async.async-mob-spawning）。"));
    }
}
