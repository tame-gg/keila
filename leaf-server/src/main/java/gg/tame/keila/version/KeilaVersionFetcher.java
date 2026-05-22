package gg.tame.keila.version;

import org.galemc.gale.version.AbstractPaperVersionFetcher;

public class KeilaVersionFetcher extends AbstractPaperVersionFetcher {

    public static final String DOWNLOAD_PAGE = "https://tame.gg/keila";
    public KeilaVersionFetcher() {
        super(
            DOWNLOAD_PAGE,
            "tame.gg",
            "Keila",
            "tame-gg",
            "keila",
            ApiType.GITHUB
        );
    }
}
