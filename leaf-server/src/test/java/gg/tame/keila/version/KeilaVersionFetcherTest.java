package gg.tame.keila.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeilaVersionFetcherTest {

    @Test
    void repositoryTargetsKeilaGitHubProject() {
        assertTrue(KeilaVersionFetcher.REPOSITORY.equals("tame-gg/keila"));
    }

    @Test
    void userAgentIncludesKeilaAndDownloadPage() {
        assertTrue(KeilaVersionFetcher.USER_AGENT.contains("Keila"));
        assertTrue(KeilaVersionFetcher.USER_AGENT.contains(KeilaVersionFetcher.DOWNLOAD_PAGE));
    }

    @Test
    void downloadPageUsesTameGg() {
        assertFalse(KeilaVersionFetcher.DOWNLOAD_PAGE.isBlank());
        assertTrue(KeilaVersionFetcher.DOWNLOAD_PAGE.startsWith("https://tame.gg/"));
    }
}
