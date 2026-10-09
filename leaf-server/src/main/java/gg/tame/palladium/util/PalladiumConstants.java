package gg.tame.palladium.util;

public final class PalladiumConstants {

    private PalladiumConstants() {
    }

    public static final boolean DISABLE_VANILLA_PROFILER = systemPropertyEnabled("Palladium.disable-vanilla-profiler", "Leaf.disable-vanilla-profiler");
    public static final boolean ENABLE_FMA = systemPropertyEnabled("Palladium.enableFMA", "Leaf.enableFMA");
    public static final boolean ENABLE_IO_URING = systemPropertyEnabled("Palladium.enable-io-uring", "Leaf.enable-io-uring");
    public static final boolean ENABLE_BASE64CODER_WARNING = systemPropertyEnabled("Palladium.enable-base64coder-warning", "Leaf.enable-base64coder-warning");

    public static final String DISABLE_VANILLA_PROFILER_DOCS_URL = "https://tame.gg/palladium";

    private static boolean systemPropertyEnabled(String property, String legacyProperty) {
        return Boolean.getBoolean(property) || Boolean.getBoolean(legacyProperty);
    }
}
