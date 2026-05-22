package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class OptimizePlayerMovementProcessing extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".optimize-player-movement", enabled, config.pickStringRegionBased("""
                Whether to optimize player movement processing by skipping unnecessary edge checks and avoiding redundant view distance updates.""",
            """
                是否优化玩家移动处理，跳过不必要的边缘检查并避免冗余的视距更新。"""));
    }
}
