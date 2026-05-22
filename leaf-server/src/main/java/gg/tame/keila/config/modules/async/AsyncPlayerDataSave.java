package gg.tame.keila.config.modules.async;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class AsyncPlayerDataSave extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".async-playerdata-save";
    }

    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Make PlayerData saving asynchronously.""",
            """
                异步保存玩家数据.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);

        if (enabled) {
            gg.tame.keila.async.AsyncPlayerDataSaving.init();
        }
    }
}
