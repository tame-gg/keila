package gg.tame.palladium.feature;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PalladiumFeatureCatalogTest {

    @Test
    void catalogContainsAllRequestedFeatures() {
        assertEquals(51, PalladiumFeatureCatalog.all().size());

        Set<String> titles = PalladiumFeatureCatalog.all().stream()
            .map(PalladiumFeature::title)
            .collect(Collectors.toUnmodifiableSet());

        assertTrue(titles.contains("Server summary"));
        assertTrue(titles.contains("MSPT report"));
        assertTrue(titles.contains("Config search"));
        assertTrue(titles.contains("Pathfinding queue"));
        assertTrue(titles.contains("Async chunk send"));
        assertTrue(titles.contains("Rollout check"));
        assertTrue(titles.contains("Safe mode profile"));
        assertTrue(titles.contains("Support bundle"));
        assertTrue(titles.contains("Command help"));
    }

    @Test
    void featureIdsAreUniqueAndLookupWorks() {
        Set<String> ids = PalladiumFeatureCatalog.all().stream()
            .map(PalladiumFeature::id)
            .collect(Collectors.toUnmodifiableSet());

        assertEquals(PalladiumFeatureCatalog.all().size(), ids.size());
        assertTrue(PalladiumFeatureCatalog.byId("KF-001").isPresent());
        assertTrue(PalladiumFeatureCatalog.byId("kf-051").isPresent());
        assertTrue(PalladiumFeatureCatalog.byId("KF-051").orElseThrow().surface().equals("/palladium"));
        assertTrue(PalladiumFeatureCatalog.byId("KF-002").orElseThrow().surface().equals("/palladium health"));
        assertTrue(PalladiumFeatureCatalog.byId("KF-043").orElseThrow().surface().equals("/palladium rollout"));
        assertTrue(PalladiumFeatureCatalog.byId("KF-001").orElseThrow().surface().equals("/palladium info"));
    }

    @Test
    void surfacesDoNotExposeInternalKeys() {
        for (PalladiumFeature feature : PalladiumFeatureCatalog.all()) {
            String surface = feature.surface();
            assertFalse(surface.contains("summary"), feature.id());
            assertFalse(surface.contains("chunk-send"), feature.id());
            assertFalse(surface.contains("KF-"), feature.id());
        }
    }

    @Test
    void categoriesAndSurfacesArePopulated() {
        assertFalse(PalladiumFeatureCatalog.categories().isEmpty());
        assertTrue(PalladiumFeatureCatalog.categories().contains("Performance"));
        assertTrue(PalladiumFeatureCatalog.categories().contains("Operations"));

        for (PalladiumFeature feature : PalladiumFeatureCatalog.all()) {
            assertFalse(feature.id().isBlank(), "id must be populated");
            assertFalse(feature.title().isBlank(), "title must be populated");
            assertFalse(feature.category().isBlank(), "category must be populated");
            assertFalse(feature.surface().isBlank(), "surface must be populated");
            assertFalse(feature.description().isBlank(), "description must be populated");
        }
    }
}
