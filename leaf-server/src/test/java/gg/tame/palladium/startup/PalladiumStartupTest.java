package gg.tame.palladium.startup;

import gg.tame.palladium.feature.PalladiumFeature;
import gg.tame.palladium.feature.PalladiumFeatureCatalog;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PalladiumStartupTest {

    @Test
    void startupHighlightsAreThreePalladiumFeatures() {
        List<PalladiumFeature> highlights = PalladiumFeatureCatalog.startupHighlights();
        assertEquals(3, highlights.size());
        assertEquals("KF-002", highlights.get(0).id());
        assertEquals("KF-036", highlights.get(1).id());
        assertEquals("KF-051", highlights.get(2).id());
        for (PalladiumFeature feature : highlights) {
            assertFalse(feature.surface().isBlank());
        }
    }

    @Test
    void asyncFeatureCountIsBounded() {
        int enabled = PalladiumStartup.countEnabledAsyncFeatures();
        assertTrue(enabled >= 0);
        assertTrue(enabled <= 5);
    }

    @Test
    void featureCommandCountMatchesCatalog() {
        assertEquals(PalladiumFeatureCatalog.all().size(), PalladiumStartup.featureCommandCount());
    }
}
