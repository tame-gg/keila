package gg.tame.keila.config;

/** Top-level YAML sections in {@code keila-global.yml}. */
public enum EnumConfigCategory {
    /** High-risk async systems; see {@code docs/keila/runtime-safety.md}. */
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
