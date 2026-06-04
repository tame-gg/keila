package gg.tame.keila.config.modules.misc;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class Including5sIngetTPS extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".including-5s-in-get-tps", enabled, config.pickStringRegionBased(
            "Include the 5-second TPS average in Bukkit getTPS() results (third slot).",
            "在 Bukkit getTPS() 的第三个返回值中包含 5 秒 TPS 平均值."));
    }
}
