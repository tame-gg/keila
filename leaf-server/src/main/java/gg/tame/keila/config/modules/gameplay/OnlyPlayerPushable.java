package gg.tame.keila.config.modules.gameplay;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class OnlyPlayerPushable extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.GAMEPLAY.getBaseKeyName() + ".only-player-pushable";
    }

    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath(), enabled, config.pickStringRegionBased(
            "Enable to make only player pushable",
            "是否只允许玩家被实体推动"
        ));
    }
}
