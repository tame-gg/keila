package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class OptimizeBlockEntities extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        // Transfer old config
        Boolean optimiseBlockEntities = config.getBoolean(getBasePath() + ".optimise-block-entities");
        if (optimiseBlockEntities != null && optimiseBlockEntities) {
            enabled =  true;
        }

        enabled = config.getBoolean(getBasePath() + ".optimize-block-entities", enabled);
    }
}
