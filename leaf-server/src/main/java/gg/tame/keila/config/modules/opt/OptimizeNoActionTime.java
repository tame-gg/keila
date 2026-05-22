package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.annotations.Experimental;

public class OptimizeNoActionTime extends ConfigModules {
    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimize-no-action-time";
    }

    @Experimental
    public static boolean disableLightCheck = false;

    @Override
    public void onLoaded() {
        disableLightCheck = config.getBoolean(getBasePath() + ".disable-light-check", disableLightCheck);
    }
}
