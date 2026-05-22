package gg.tame.keila.async.chunk;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import gg.tame.keila.util.queue.MpmcQueue;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

@NullMarked
public final class AsyncChunkSender {

    private static final int CAPACITY = 255;

    private final MpmcQueue<ClientboundLevelChunkWithLightPacket> channel;
    private final LongOpenHashSet pending;
    private int size = 0;

    public AsyncChunkSender() {
        this.channel = new MpmcQueue<>(ClientboundLevelChunkWithLightPacket.class, CAPACITY);
        this.pending = new LongOpenHashSet();
    }

    public boolean add(long k) {
        return size < CAPACITY && pending.size() < CAPACITY && pending.add(k);
    }

    public boolean remove(long k) {
        return pending.remove(k);
    }

    public boolean contains(long k) {
        return pending.contains(k);
    }

    public void clear() {
        pending.clear();
        while (this.channel.recv() != null) ;
        size = 0;
    }

    public void submit(Supplier<ClientboundLevelChunkWithLightPacket> task) {
        size++;
        AsyncChunkSend.POOL.submit(() -> {
            ClientboundLevelChunkWithLightPacket chunk = task.get();
            while (!channel.send(chunk)) ;
        });
    }

    public @Nullable ClientboundLevelChunkWithLightPacket recv() {
        ClientboundLevelChunkWithLightPacket packet = this.channel.recv();
        if (packet != null) {
            size--;
        }
        return packet;
    }

    int inFlight() {
        return size;
    }

    int pendingChunks() {
        return pending.size();
    }

    void markInFlightForTesting() {
        size++;
    }
}
