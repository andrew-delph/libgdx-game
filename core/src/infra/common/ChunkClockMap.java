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
    System.out.println("HEERE");
  }

  void add(Chunk chunk) {
    this.map.put(chunk.chunkRange, chunk);
  }

  Chunk get(ChunkRange chunkRange) {
    //    System.out.println("before");
    //    for (ChunkRange chunkRange1 : this.map.keySet()) {
    //      System.out.println(chunkRange1);
    //    }
    return this.map.get(chunkRange);
  }

  List<Callable<Chunk>> getChunksOnTick(Tick tick) {
    return this.map.values().stream()
        .filter(chunk -> chunk.updateTick.time == tick.time)
        .collect(Collectors.toList());
  }
}
