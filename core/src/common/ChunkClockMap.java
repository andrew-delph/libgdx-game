package common;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChunkClockMap {

    Map<ChunkRange, Chunk> map;

    @Inject
    ChunkClockMap() {
        this.map = new ConcurrentHashMap<>();
    }

    void add(Chunk chunk) {
        this.map.put(chunk.chunkRange, chunk);
    }

    Chunk get(ChunkRange chunkRange) {
        return this.map.get(chunkRange);
    }

    Chunk remove(ChunkRange chunkRange) {
        Chunk toRemove = this.map.get(chunkRange);
        this.map.remove(chunkRange);
        return toRemove;
    }

    Boolean doesChunkExist(ChunkRange chunkRange) {
        return this.map.containsKey(chunkRange);
    }

    public Set<ChunkRange> getChunkRangeSet() {
        return this.map.keySet();
    }

    List<Callable<Chunk>> getChunksOnTick(Tick tick) {
        return this.map.values().stream()
                .filter(chunk -> chunk.updateTick.time == tick.time)
                .collect(Collectors.toList());
    }
}
