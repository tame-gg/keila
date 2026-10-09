package gg.tame.palladium.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PalladiumVersionFetcherTest {

    @Test
    void repositoryTargetsExistingGitHubProject() {
        assertTrue(PalladiumVersionFetcher.REPOSITORY.equals("tame-gg/keila"));
    }

    @Test
    void userAgentIncludesPalladiumAndDownloadPage() {
        assertTrue(PalladiumVersionFetcher.USER_AGENT.contains("Palladium"));
        assertTrue(PalladiumVersionFetcher.USER_AGENT.contains(PalladiumVersionFetcher.DOWNLOAD_PAGE));
    }

    @Test
    void downloadPageUsesTameGg() {
        assertFalse(PalladiumVersionFetcher.DOWNLOAD_PAGE.isBlank());
        assertTrue(PalladiumVersionFetcher.DOWNLOAD_PAGE.startsWith("https://tame.gg/"));
    }
}
