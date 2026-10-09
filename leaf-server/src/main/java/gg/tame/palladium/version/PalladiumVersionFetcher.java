package gg.tame.palladium.version;

import io.papermc.paper.ServerBuildInfo;
import org.galemc.gale.version.AbstractPaperVersionFetcher;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

public class PalladiumVersionFetcher extends AbstractPaperVersionFetcher {

    public static final String DOWNLOAD_PAGE = "https://tame.gg/palladium";
    // GitHub repository has not been renamed. Update checks still query this slug.
    public static final String REPOSITORY = "tame-gg/keila";
    public static final String USER_AGENT = "Palladium/" + versionSimple() + " (" + DOWNLOAD_PAGE + ")";

    private static String versionSimple() {
        try {
            return ServerBuildInfo.buildInfo().asString(VERSION_SIMPLE);
        } catch (Throwable ex) {
            return "unknown";
        }
    }

    public PalladiumVersionFetcher() {
        super(
            DOWNLOAD_PAGE,
            "tame.gg",
            "Palladium",
            "tame-gg",
            REPOSITORY.substring(REPOSITORY.indexOf('/') + 1),
            null,
            USER_AGENT,
            ApiType.GITHUB
        );
    }

    public static void getUpdateStatusStartupMessage() {
        AbstractPaperVersionFetcher.getUpdateStatusStartupMessage(
            REPOSITORY,
            DOWNLOAD_PAGE,
            null,
            USER_AGENT,
            ApiType.GITHUB
        );
    }
}
