package gg.tame.palladium.config.modules.network;

import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;

public class AlternativeJoin extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.NETWORK.getBaseKeyName();
    }

    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".async-switch-state", enabled, config.pickStringRegionBased(
            "Async switch connection state.",
            "异步切换连接状态."));
    }
}
