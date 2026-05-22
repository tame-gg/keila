package gg.tame.keila.async.world;

import gg.tame.keila.config.KeilaConfig;

import java.util.Locale;

public enum UnsafeReadPolicy {
    STRICT,
    BUFFERED,
    DISABLED;

    public static UnsafeReadPolicy fromString(String readPolicy) {
        try {
            return valueOf(readPolicy.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            KeilaConfig.LOGGER.warn("Invalid unsafe read policy: {}, falling back to {}.", readPolicy, DISABLED.toString());
            return DISABLED;
        }
    }
}
