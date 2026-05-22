package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.annotations.Experimental;

public class OptimizeRandomTick extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimize-random-tick";
    }

    @Experimental
    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        Boolean old = config.getBoolean(EnumConfigCategory.PERF.getBaseKeyName() + ".optimise-random-tick");
        if (old != null && old) {
            enabled = config.getBoolean(getBasePath(), true);
            return;
        }

        enabled = config.getBoolean(getBasePath(), enabled);
    }
}
