package gg.tame.keila.roadmap;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Utf8StringCache {

    private final int maxEntries;
    private final ConcurrentMap<String, byte[]> encodedByString = new ConcurrentHashMap<>();

    public Utf8StringCache(int maxEntries) {
        if (maxEntries <= 0) {
            throw new IllegalArgumentException("maxEntries must be positive");
        }
        this.maxEntries = maxEntries;
    }

    public byte[] encoded(String value) {
        Objects.requireNonNull(value, "value");
        byte[] existing = this.encodedByString.get(value);
        if (existing != null) {
            return existing;
        }
        if (this.encodedByString.size() >= this.maxEntries) {
            this.encodedByString.clear();
        }
        return this.encodedByString.computeIfAbsent(value, key -> key.getBytes(StandardCharsets.UTF_8));
    }

    public int size() {
        return this.encodedByString.size();
    }

    public void clear() {
        this.encodedByString.clear();
    }
}
