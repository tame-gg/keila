package gg.tame.keila.config.modules.misc;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;

public class StartupExperience extends ConfigModules {

    public static boolean enabled = true;
    public static boolean banner = true;
    public static boolean onboarding = true;
    public static boolean healthSnapshot = true;
    public static boolean featureSplash = true;
    public static boolean operatorWelcome = true;

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName() + ".startup-experience";
    }

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Console and in-game polish shown at startup and for operators.
                Disable the whole section to silence Keila welcome messaging.""",
            """
                启动时在控制台与管理员可见的 Keila 欢迎与引导信息。
                关闭 enabled 可禁用整组提示。""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);
        banner = config.getBoolean(getBasePath() + ".banner", banner, config.pickStringRegionBased(
            "ASCII Keila banner, version line, and upstream credits in the console.",
            "在控制台打印 Keila ASCII 横幅、版本与上游致谢。"));
        onboarding = config.getBoolean(getBasePath() + ".onboarding", onboarding, config.pickStringRegionBased(
            "First-boot hints when keila-global.yml is newly created.",
            "首次生成 keila-global.yml 时显示配置引导。"));
        healthSnapshot = config.getBoolean(getBasePath() + ".health-snapshot", healthSnapshot, config.pickStringRegionBased(
            "One-line health summary after the server finishes starting.",
            "服务器启动完成后输出一行健康摘要。"));
        featureSplash = config.getBoolean(getBasePath() + ".feature-splash", featureSplash, config.pickStringRegionBased(
            "Log three highlighted /keila commands operators can try first.",
            "启动时列出三条建议尝试的 /keila 功能。"));
        operatorWelcome = config.getBoolean(getBasePath() + ".operator-welcome", operatorWelcome, config.pickStringRegionBased(
            "Tip the first operator once per server session on join.",
            "每位管理员在本会话首次加入时显示一次提示。"));
    }
}
