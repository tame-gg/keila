package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class SkipMapItemDataUpdates extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".skip-map-item-data-updates-if-map-does-not-have-craftmaprenderer", enabled);
    }
}
