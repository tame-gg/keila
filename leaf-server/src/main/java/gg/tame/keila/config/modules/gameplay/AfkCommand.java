package gg.tame.keila.config.modules.gameplay;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class AfkCommand extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.GAMEPLAY.getBaseKeyName() + ".afk-command";
    }

    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".enabled", enabled, config.pickStringRegionBased("""
                Enable Keila's /afk command (uses the vanilla idle-timeout mechanism).
                Other AFK options remain in purpur.yml.""",
            """
                启用 Keila 的 /afk 指令（基于原版 idle-timeout）。
                其余 AFK 选项仍在 purpur.yml 中配置。"""));
    }
}
