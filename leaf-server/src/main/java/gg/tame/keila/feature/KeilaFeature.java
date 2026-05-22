package gg.tame.keila.feature;

public record KeilaFeature(
    String id,
    String title,
    String category,
    FeatureStatus status,
    String surface,
    String description
) {
}
