package gg.tame.keila.roadmap;

public final class SpawnWaveScheduler {

    private final int waves;

    public SpawnWaveScheduler(int waves) {
        if (waves <= 0) {
            throw new IllegalArgumentException("waves must be positive");
        }
        this.waves = waves;
    }

    public int waveFor(int chunkX, int chunkZ) {
        return Math.floorMod(chunkX ^ chunkZ, this.waves);
    }

    public boolean shouldRunThisTick(int chunkX, int chunkZ, int tick) {
        return this.waveFor(chunkX, chunkZ) == Math.floorMod(tick, this.waves);
    }
}
