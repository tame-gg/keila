package gg.tame.keila.config.modules.gameplay;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class VanillaHopper extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.GAMEPLAY.getBaseKeyName() + ".use-vanilla-hopper";
    }

    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath(), enabled);
    }
}
