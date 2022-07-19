package core.common;

import com.google.inject.Inject;
import core.chunk.Chunk;
import core.chunk.ChunkRange;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

  Set<Chunk> getChunksOnTick(Tick tick) {
    Set<Chunk> onMyTick =
        this.map.values().stream()
            .filter(chunk -> chunk.updateTick.time == tick.time)
            .collect(Collectors.toSet());

    Set<Chunk> surroundingChunks = new HashSet<>();

    for (Chunk onTick : onMyTick) {
      surroundingChunks.addAll(onTick.getNeighborChunks());
    }

    onMyTick.addAll(surroundingChunks);

    return onMyTick;
  }
}
