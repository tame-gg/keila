package gg.tame.palladium.config.modules.misc;

import gg.tame.palladium.config.ConfigModules;
import gg.tame.palladium.config.EnumConfigCategory;

public class ServerBrand extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName() + ".rebrand";
    }

    public static String serverModName = io.papermc.paper.ServerBuildInfo.buildInfo().brandName();
    public static String serverGUIName = io.papermc.paper.ServerBuildInfo.buildInfo().brandName() + " Console";

    @Override
    public void onLoaded() {
        serverModName = config.getString(getBasePath() + ".server-mod-name", serverModName, config.pickStringRegionBased(
            "Brand string shown to clients (defaults to the Palladium build brand).",
            "向客户端展示的服务器品牌名（默认为 Palladium 构建品牌）。"));
        serverGUIName = config.getString(getBasePath() + ".server-gui-name", serverGUIName, config.pickStringRegionBased(
            "Title for the server console window.",
            "服务器控制台窗口标题。"));
    }
}
