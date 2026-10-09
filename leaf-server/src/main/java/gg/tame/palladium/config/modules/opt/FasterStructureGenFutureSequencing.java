package gg.tame.palladium.config.modules.opt;

import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;

public class FasterStructureGenFutureSequencing extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".faster-structure-gen-future-sequencing", enabled,
            config.pickStringRegionBased(
                "May cause the inconsistent order of future compose tasks.",
                "更快的结构生成任务分段."));
    }
}
