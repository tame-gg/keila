package gg.tame.keila.async.path;

import gg.tame.keila.config.KeilaConfig;

import java.util.Locale;

public enum PathfindTaskRejectPolicy {
    FLUSH_ALL,
    CALLER_RUNS;

    public static PathfindTaskRejectPolicy fromString(String policy) {
        try {
            return PathfindTaskRejectPolicy.valueOf(policy.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            KeilaConfig.LOGGER.warn("Invalid pathfind task reject policy: {}, falling back to {}.", policy, FLUSH_ALL.toString());
            return FLUSH_ALL;
        }
    }
}
