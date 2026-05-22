package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class CheckSurvivalBeforeGrowth extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".check-survival-before-growth";
    }

    public static boolean cactusCheckSurvivalBeforeGrowth = false;

    @Override
    public void onLoaded() {
        cactusCheckSurvivalBeforeGrowth = config.getBoolean(getBasePath() + ".cactus-check-survival", cactusCheckSurvivalBeforeGrowth,
            config.pickStringRegionBased("""
                    Check if a cactus can survive before growing.""",
                """
                    在仙人掌生长前检查其是否能够存活。"""));
    }
}
