package gg.tame.keila.version;

import io.papermc.paper.ServerBuildInfo;
import org.galemc.gale.version.AbstractPaperVersionFetcher;

import static io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE;

public class KeilaVersionFetcher extends AbstractPaperVersionFetcher {

    public static final String DOWNLOAD_PAGE = "https://tame.gg/keila";
    public static final String REPOSITORY = "tame-gg/keila";
    private static final ServerBuildInfo BUILD_INFO = ServerBuildInfo.buildInfo();
    public static final String USER_AGENT = BUILD_INFO.brandName() + "/" + BUILD_INFO.asString(VERSION_SIMPLE) + " (" + DOWNLOAD_PAGE + ")";

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
        AbstractPaperVersionFetcher.getUpdateStatusStartupMessage(
            REPOSITORY,
            DOWNLOAD_PAGE,
            null,
            USER_AGENT,
            ApiType.GITHUB
        );
    }
}
