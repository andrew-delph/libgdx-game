package infra.common;

import infra.chunk.Chunk;
import infra.chunk.ChunkRange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChunkClockMap {

  Map<ChunkRange, Chunk> map;

  ChunkClockMap() {
    this.map = new ConcurrentHashMap<>();
  }

  void add(Chunk chunk) {
    this.map.put(chunk.chunkRange, chunk);
  }

  Chunk get(ChunkRange chunkRange) {
    return this.map.get(chunkRange);
  }

  List<Callable<Chunk>> getChunksOnTick(Tick tick) {
    return this.map.values().stream()
        .filter(chunk -> chunk.updateTick.time == tick.time)
        .collect(Collectors.toList());
  }
}
