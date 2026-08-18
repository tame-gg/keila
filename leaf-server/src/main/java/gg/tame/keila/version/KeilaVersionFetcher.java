package gg.tame.keila.version;

import io.papermc.paper.ServerBuildInfo;
import org.galemc.gale.version.AbstractPaperVersionFetcher;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

public class KeilaVersionFetcher extends AbstractPaperVersionFetcher {

    public static final String DOWNLOAD_PAGE = "https://tame.gg/keila";
    public static final String REPOSITORY = "tame-gg/keila";
    public static final String USER_AGENT = "Keila/" + versionSimple() + " (" + DOWNLOAD_PAGE + ")";

    private static String versionSimple() {
        try {
            return ServerBuildInfo.buildInfo().asString(VERSION_SIMPLE);
        } catch (Throwable ex) {
            return "unknown";
        }
    }

    public KeilaVersionFetcher() {
        super(
            DOWNLOAD_PAGE,
            "tame.gg",
            "Keila",
            "tame-gg",
            "keila",
            null,
            USER_AGENT,
            ApiType.GITHUB
        );
    }

    public static void getUpdateStatusStartupMessage() {
        AbstractPaperVersionFetcher.getUpdateStatusStartupMessage(); // Keila - 26.2 API is parameterless (repo driven by build info)
    }
}
