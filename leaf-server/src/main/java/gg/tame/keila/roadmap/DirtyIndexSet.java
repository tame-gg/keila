package gg.tame.keila.roadmap;

import java.util.Arrays;

public final class DirtyIndexSet {

    private final boolean[] dirty;
    private int count;

    public DirtyIndexSet(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.dirty = new boolean[capacity];
    }

    public void mark(int index) {
        this.checkIndex(index);
        if (!this.dirty[index]) {
            this.dirty[index] = true;
            this.count++;
        }
    }

    public boolean isDirty(int index) {
        this.checkIndex(index);
        return this.dirty[index];
    }

    public int count() {
        return this.count;
    }

    public int[] drain() {
        int[] indexes = new int[this.count];
        int outputIndex = 0;
        for (int index = 0; index < this.dirty.length; index++) {
            if (this.dirty[index]) {
                indexes[outputIndex++] = index;
            }
        }
        Arrays.fill(this.dirty, false);
        this.count = 0;
        return indexes;
    }

    public void clear() {
        Arrays.fill(this.dirty, false);
        this.count = 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this.dirty.length) {
            throw new IndexOutOfBoundsException(index);
        }
    }
}
