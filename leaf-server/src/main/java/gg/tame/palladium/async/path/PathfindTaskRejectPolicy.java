package gg.tame.palladium.async.path;

import gg.tame.palladium.config.PalladiumConfig;

import java.util.Locale;

public enum PathfindTaskRejectPolicy {
    FLUSH_ALL,
    CALLER_RUNS;

    public static PathfindTaskRejectPolicy fromString(String policy) {
        try {
            return PathfindTaskRejectPolicy.valueOf(policy.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            PalladiumConfig.LOGGER.warn("Invalid pathfind task reject policy: {}, falling back to {}.", policy, FLUSH_ALL.toString());
            return FLUSH_ALL;
        }
    }
}
