package gg.tame.palladium.config.modules.async;

import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;
import gg.tame.palladium.config.annotations.HotReloadUnsupported;

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
                See docs/palladium/runtime-safety.md.""",
            """
                在后台线程保存玩家数据。高风险：上线前需通过排序与 soak 测试。
                参见 docs/palladium/runtime-safety.md。""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);

        if (enabled) {
            gg.tame.palladium.async.AsyncPlayerDataSaving.init();
        }
    }
}
