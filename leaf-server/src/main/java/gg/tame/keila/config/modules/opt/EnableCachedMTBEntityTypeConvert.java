package gg.tame.keila.config.modules.opt;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class EnableCachedMTBEntityTypeConvert extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".enable-cached-minecraft-to-bukkit-entitytype-convert", enabled, config.pickStringRegionBased("""
                Whether to cache expensive CraftEntityType#minecraftToBukkit call.""",
            """
                是否缓存Minecraft到Bukkit的实体类型转换."""));
    }
}
