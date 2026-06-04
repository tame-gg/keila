package gg.tame.keila.startup;

import gg.tame.keila.feature.KeilaFeature;
import gg.tame.keila.feature.KeilaFeatureCatalog;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeilaStartupTest {

    @Test
    void startupHighlightsAreThreeKeilaFeatures() {
        List<KeilaFeature> highlights = KeilaFeatureCatalog.startupHighlights();
        assertEquals(3, highlights.size());
        assertEquals("KF-002", highlights.get(0).id());
        assertEquals("KF-036", highlights.get(1).id());
        assertEquals("KF-051", highlights.get(2).id());
        for (KeilaFeature feature : highlights) {
            assertFalse(feature.surface().isBlank());
        }
    }

    @Test
    void asyncFeatureCountIsBounded() {
        int enabled = KeilaStartup.countEnabledAsyncFeatures();
        assertTrue(enabled >= 0);
        assertTrue(enabled <= 5);
    }

    @Test
    void featureCommandCountMatchesCatalog() {
        assertEquals(KeilaFeatureCatalog.all().size(), KeilaStartup.featureCommandCount());
    }
}
