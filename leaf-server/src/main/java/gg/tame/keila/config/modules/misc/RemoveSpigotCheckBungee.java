package gg.tame.keila.config.modules.misc;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class RemoveSpigotCheckBungee extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName() + ".remove-spigot-check-bungee-config";
    }

    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath(), enabled, config.pickStringRegionBased("""
                Enable player enter backend server through proxy
                without backend server enabling its bungee mode.""",
            """
                使服务器无需打开 bungee 模式即可让玩家加入后端服务器."""));
    }
}
