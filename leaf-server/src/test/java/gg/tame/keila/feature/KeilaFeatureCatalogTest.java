package gg.tame.keila.feature;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeilaFeatureCatalogTest {

    @Test
    void catalogContainsAllRequestedFeatures() {
        assertEquals(50, KeilaFeatureCatalog.all().size());

        Set<String> titles = KeilaFeatureCatalog.all().stream()
            .map(KeilaFeature::title)
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
        Set<String> ids = KeilaFeatureCatalog.all().stream()
            .map(KeilaFeature::id)
            .collect(Collectors.toUnmodifiableSet());

        assertEquals(KeilaFeatureCatalog.all().size(), ids.size());
        assertTrue(KeilaFeatureCatalog.byId("KF-001").isPresent());
        assertTrue(KeilaFeatureCatalog.byId("kf-050").isPresent());
        assertTrue(KeilaFeatureCatalog.byId("KF-050").orElseThrow().surface().contains("/keila features"));
    }

    @Test
    void categoriesAndSurfacesArePopulated() {
        assertFalse(KeilaFeatureCatalog.categories().isEmpty());
        assertTrue(KeilaFeatureCatalog.categories().contains("Performance"));
        assertTrue(KeilaFeatureCatalog.categories().contains("Operations"));

        for (KeilaFeature feature : KeilaFeatureCatalog.all()) {
            assertFalse(feature.id().isBlank(), "id must be populated");
            assertFalse(feature.title().isBlank(), "title must be populated");
            assertFalse(feature.category().isBlank(), "category must be populated");
            assertFalse(feature.surface().isBlank(), "surface must be populated");
            assertFalse(feature.description().isBlank(), "description must be populated");
        }
    }
}
