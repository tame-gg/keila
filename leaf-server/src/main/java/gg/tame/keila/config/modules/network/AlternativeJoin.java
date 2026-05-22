package gg.tame.keila.config.modules.network;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

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
