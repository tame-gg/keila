package gg.tame.palladium.startup;

import ca.spottedleaf.moonrise.common.time.TickData;
import gg.tame.palladium.config.PalladiumConfig;
import gg.tame.palladium.config.modules.async.AsyncChunkSend;
import gg.tame.palladium.config.modules.async.AsyncPathfinding;
import gg.tame.palladium.config.modules.async.AsyncPlayerDataSave;
import gg.tame.palladium.config.modules.async.MultithreadedTracker;
import gg.tame.palladium.config.modules.async.SparklyPaperParallelWorldTicking;
import gg.tame.palladium.config.modules.misc.StartupExperience;
import gg.tame.palladium.feature.PalladiumFeature;
import gg.tame.palladium.feature.PalladiumFeatureCatalog;
import gg.tame.palladium.version.PalladiumVersionFetcher;
import io.papermc.paper.ServerBuildInfo;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

/**
 * Startup-visible Palladium identity: banner, onboarding, health snapshot, and feature highlights.
 */
public final class PalladiumStartup {

    private PalladiumStartup() {
    }

    public static void onConfigLoaded() {
        if (!StartupExperience.enabled) {
            return;
        }
        Logger logger = PalladiumConfig.LOGGER;
        if (StartupExperience.banner) {
            printBanner(logger);
        }
        if (StartupExperience.onboarding && PalladiumConfig.isFreshConfig()) {
            printOnboarding(logger);
        }
    }

    public static void onServerReady(MinecraftServer server) {
        if (!StartupExperience.enabled) {
            return;
        }
        Logger logger = PalladiumConfig.LOGGER;
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
        return PalladiumFeatureCatalog.all().size();
    }

    private static void printBanner(Logger logger) {
        String version = versionSimple();
        logger.info("");
        logger.info("  _  __ _  __    _    ");
        logger.info(" | |/ /| |/ /  / \\   ");
        logger.info(" | ' / | ' /  / _ \\  ");
        logger.info(" | . \\ | . \\ / ___ \\ ");
        logger.info(" |_|\\_\\|_|\\_\\/_/   \\_\\");
        logger.info("  tame.gg/palladium  |  {}  |  {}", version, PalladiumVersionFetcher.DOWNLOAD_PAGE);
        logger.info("  Built on Leaf, Purpur, and Paper — upstream performance work, Palladium-owned operator tooling.");
        logger.info("");
    }

    private static void printOnboarding(Logger logger) {
        logger.info("-------------------------------------------------------------------------------------");
        logger.info("Palladium first boot — welcome!");
        logger.info("  1. Review config/palladium-global.yml (async toggles need a restart when changed).");
        logger.info("  2. Try /palladium list — {} operator diagnostics are ready.", featureCommandCount());
        logger.info("  3. Run /palladium rollout before enabling risky async features.");
        logger.info("  4. Docs: https://github.com/tame-gg/palladium/tree/main/docs  ·  Site: https://tame.gg/palladium");
        logger.info("  5. Incident rollback: /palladium safe  ·  script: scripts/safeModeProfile.sh");
        logger.info("-------------------------------------------------------------------------------------");
    }

    private static void printHealthSnapshot(Logger logger, MinecraftServer server) {
        double mspt = averageMspt(server.tickTimes5s);
        String msptLabel = mspt <= 0.0D ? "warming up (try /palladium mspt)" : String.format("%.1fms avg (5s)", mspt);
        int asyncEnabled = countEnabledAsyncFeatures();
        logger.info("[Palladium] Ready — MSPT {}, async features {}/5 enabled, /palladium perf & /palladium health",
            msptLabel, asyncEnabled);
    }

    private static void printFeatureSplash(Logger logger) {
        logger.info("[Palladium] Try these first:");
        for (PalladiumFeature feature : PalladiumFeatureCatalog.startupHighlights()) {
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
