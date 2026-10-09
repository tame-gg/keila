package gg.tame.palladium.feature;

public record PalladiumFeature(
    String id,
    String title,
    String category,
    FeatureStatus status,
    String surface,
    String description
) {
}
