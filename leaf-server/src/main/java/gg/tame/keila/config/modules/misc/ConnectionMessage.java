package gg.tame.keila.config.modules.misc;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class ConnectionMessage extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName() + ".connection-message";
    }

    public static boolean joinEnabled = true;
    public static String joinMessage = "<dark_gray>[</dark_gray><#6EC6FF>Keila</#6EC6FF><dark_gray>]</dark_gray> <white><player_name></white> <gray>joined</gray>";
    public static boolean quitEnabled = true;
    public static String quitMessage = "<dark_gray>[</dark_gray><#6EC6FF>Keila</#6EC6FF><dark_gray>]</dark_gray> <gray><player_name> left</gray>";

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Connection message, using MiniMessage format, set to "default" to use vanilla join message.
                available placeholders:
                <player_name> - player name
                <player_displayname> - player display name""",
            """
                自定义加入 & 退出消息 (MiniMessage 格式), 设置为 'default' 将使用原版消息.
                可用的内置变量:
                <player_name> - 玩家名称
                <player_displayname> - 玩家显示名称""");

        joinEnabled = config.getBoolean(getBasePath() + ".join.enabled", joinEnabled);
        joinMessage = config.getString(getBasePath() + ".join.message", joinMessage, config.pickStringRegionBased(
            "Join message of player",
            "玩家加入服务器时的消息"
        ));

        quitEnabled = config.getBoolean(getBasePath() + ".quit.enabled", quitEnabled);
        quitMessage = config.getString(getBasePath() + ".quit.message", quitMessage, config.pickStringRegionBased(
            "Quit message of player",
            "玩家退出服务器时的消息"));

        // Legacy %player_*% placeholders (no YAML key change)
        joinMessage = joinMessage
            .replace("%player_name%", "<player_name>")
            .replace("%player_displayname%", "<player_displayname>");
        quitMessage = quitMessage
            .replace("%player_name%", "<player_name>")
            .replace("%player_displayname%", "<player_displayname>");
    }
}
