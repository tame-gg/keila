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
                Save player data on a background thread. High-risk: requires ordering tests before production use.
                See docs/keila/runtime-safety.md.""",
            """
                在后台线程保存玩家数据。高风险：上线前需通过排序与 soak 测试。
                参见 docs/keila/runtime-safety.md。""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);

        if (enabled) {
            gg.tame.keila.async.AsyncPlayerDataSaving.init();
        }
    }
}
