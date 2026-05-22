package gg.tame.keila.roadmap;

public final class AdaptiveCompressionController {

    private final double lowMsptThreshold;
    private final double highMsptThreshold;
    private final int lowLoadLevel;
    private final int balancedLevel;
    private final int highLoadLevel;

    public AdaptiveCompressionController(double lowMsptThreshold, double highMsptThreshold, int lowLoadLevel, int balancedLevel, int highLoadLevel) {
        if (lowMsptThreshold < 0.0D || highMsptThreshold <= lowMsptThreshold) {
            throw new IllegalArgumentException("MSPT thresholds must be ordered and non-negative");
        }
        this.lowMsptThreshold = lowMsptThreshold;
        this.highMsptThreshold = highMsptThreshold;
        this.lowLoadLevel = clampCompressionLevel(lowLoadLevel);
        this.balancedLevel = clampCompressionLevel(balancedLevel);
        this.highLoadLevel = clampCompressionLevel(highLoadLevel);
    }

    public int levelForMspt(double mspt) {
        if (mspt <= this.lowMsptThreshold) {
            return this.lowLoadLevel;
        }
        if (mspt >= this.highMsptThreshold) {
            return this.highLoadLevel;
        }
        return this.balancedLevel;
    }

    private static int clampCompressionLevel(int level) {
        return Math.max(0, Math.min(9, level));
    }
}
