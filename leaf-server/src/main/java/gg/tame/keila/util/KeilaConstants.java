package gg.tame.keila.util;

public final class KeilaConstants {

    private KeilaConstants() {
    }

    public static final boolean DISABLE_VANILLA_PROFILER = systemPropertyEnabled("Keila.disable-vanilla-profiler", "Leaf.disable-vanilla-profiler");
    public static final boolean ENABLE_FMA = systemPropertyEnabled("Keila.enableFMA", "Leaf.enableFMA");
    public static final boolean ENABLE_IO_URING = systemPropertyEnabled("Keila.enable-io-uring", "Leaf.enable-io-uring");
    public static final boolean ENABLE_BASE64CODER_WARNING = systemPropertyEnabled("Keila.enable-base64coder-warning", "Leaf.enable-base64coder-warning");

    public static final String DISABLE_VANILLA_PROFILER_DOCS_URL = "https://tame.gg/keila";

    private static boolean systemPropertyEnabled(String property, String legacyProperty) {
        return Boolean.getBoolean(property) || Boolean.getBoolean(legacyProperty);
    }
}
