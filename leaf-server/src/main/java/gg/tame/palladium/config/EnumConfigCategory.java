package gg.tame.palladium.config;

/** Top-level YAML sections in {@code palladium-global.yml}. */
public enum EnumConfigCategory {
    /** High-risk async systems; see {@code docs/palladium/runtime-safety.md}. */
    ASYNC("async"),
    PERF("performance"),
    FIXES("fixes"),
    GAMEPLAY("gameplay-mechanisms"),
    NETWORK("network"),
    MISC("misc");

    private final String baseKeyName;
    private static final EnumConfigCategory[] VALUES = EnumConfigCategory.values();

    EnumConfigCategory(String baseKeyName) {
        this.baseKeyName = baseKeyName;
    }

    public String getBaseKeyName() {
        return this.baseKeyName;
    }

    public static EnumConfigCategory[] getCategoryValues() {
        return VALUES;
    }
}
