package gg.tame.keila.startup;

import ca.spottedleaf.moonrise.common.time.TickData;
import gg.tame.keila.config.KeilaConfig;
import gg.tame.keila.config.modules.async.AsyncChunkSend;
import gg.tame.keila.config.modules.async.AsyncPathfinding;
import gg.tame.keila.config.modules.async.AsyncPlayerDataSave;
import gg.tame.keila.config.modules.async.MultithreadedTracker;
import gg.tame.keila.config.modules.async.SparklyPaperParallelWorldTicking;
import gg.tame.keila.config.modules.misc.StartupExperience;
import gg.tame.keila.feature.KeilaFeature;
import gg.tame.keila.feature.KeilaFeatureCatalog;
import gg.tame.keila.version.KeilaVersionFetcher;
import io.papermc.paper.ServerBuildInfo;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

/**
 * Startup-visible Keila identity: banner, onboarding, health snapshot, and feature highlights.
 */
public final class KeilaStartup {

    private KeilaStartup() {
    }

    public static void onConfigLoaded() {
        if (!StartupExperience.enabled) {
            return;
        }
        Logger logger = KeilaConfig.LOGGER;
        if (StartupExperience.banner) {
            printBanner(logger);
        }
        if (StartupExperience.onboarding && KeilaConfig.isFreshConfig()) {
            printOnboarding(logger);
        }
    }

    public static void onServerReady(MinecraftServer server) {
        if (!StartupExperience.enabled) {
            return;
        }
        Logger logger = KeilaConfig.LOGGER;
        if (StartupExperience.healthSnapshot) {
            printHealthSnapshot(logger, server);
        }
        if (StartupExperience.featureSplash) {
            printFeatureSplash(logger);
        }
    }

    public static int countEnabledAsyncFeatures() {
        int count = 0;
        if (AsyncChunkSend.enabled) count++;
        if (AsyncPlayerDataSave.enabled) count++;
        if (AsyncPathfinding.enabled) count++;
        if (MultithreadedTracker.enabled) count++;
        if (SparklyPaperParallelWorldTicking.enabled) count++;
        return count;
    }

    public static int featureCommandCount() {
        return KeilaFeatureCatalog.all().size();
    }

    private static void printBanner(Logger logger) {
        String version = versionSimple();
        logger.info("");
        logger.info("  ██╗  ██╗███████╗██╗██╗      █████╗ ");
        logger.info("  ██║ ██╔╝██╔════╝██║██║     ██╔══██╗");
        logger.info("  █████╔╝ █████╗  ██║██║     ███████║");
        logger.info("  ██╔═██╗ ██╔══╝  ██║██║     ██╔══██║");
        logger.info("  ██║  ██╗███████╗██║███████╗██║  ██║");
        logger.info("  ╚═╝  ╚═╝╚══════╝╚═╝╚══════╝╚═╝  ╚═╝");
        logger.info("  tame.gg/keila  ·  {}  ·  {}", version, KeilaVersionFetcher.DOWNLOAD_PAGE);
        logger.info("  Built on Leaf, Purpur, and Paper — upstream performance work, Keila-owned operator tooling.");
        logger.info("");
    }

    private static void printOnboarding(Logger logger) {
        logger.info("-------------------------------------------------------------------------------------");
        logger.info("Keila first boot — welcome!");
        logger.info("  1. Review config/keila-global.yml (async toggles need a restart when changed).");
        logger.info("  2. Try /keila list — {} operator diagnostics are ready.", featureCommandCount());
        logger.info("  3. Run /keila rollout before enabling risky async features.");
        logger.info("  4. Docs: https://github.com/tame-gg/keila/tree/main/docs  ·  Site: https://tame.gg/keila");
        logger.info("  5. Incident rollback: /keila safe  ·  script: scripts/safeModeProfile.sh");
        logger.info("-------------------------------------------------------------------------------------");
    }

    private static void printHealthSnapshot(Logger logger, MinecraftServer server) {
        double mspt = averageMspt(server.tickTimes5s);
        String msptLabel = mspt <= 0.0D ? "warming up (try /keila mspt)" : String.format("%.1fms avg (5s)", mspt);
        int asyncEnabled = countEnabledAsyncFeatures();
        logger.info("[Keila] Ready — MSPT {}, async features {}/5 enabled, /keila perf & /keila health",
            msptLabel, asyncEnabled);
    }

    private static void printFeatureSplash(Logger logger) {
        logger.info("[Keila] Try these first:");
        for (KeilaFeature feature : KeilaFeatureCatalog.startupHighlights()) {
            logger.info("  {} {} — {}", feature.id(), feature.title(), feature.surface());
        }
    }

    static double averageMspt(TickData tickData) {
        TickData.TickReportData report = tickData.generateTickReport(null, System.nanoTime(), MinecraftServer.getServer().tickRateManager().nanosecondsPerTick());
        return report == null ? 0.0D : report.timePerTickData().segmentAll().average() * 1.0E-6D;
    }

    private static String versionSimple() {
        try {
            return ServerBuildInfo.buildInfo().asString(VERSION_SIMPLE);
        } catch (Throwable ex) {
            return "unknown";
        }
    }
}
