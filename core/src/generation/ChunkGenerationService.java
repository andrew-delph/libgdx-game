package generation;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import configuration.GameSettings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChunkGenerationService {

  private final ExecutorService executor =
      Executors.newFixedThreadPool(GameSettings.GENERATION_THREADS);
  private final Map<ChunkRange, Future<Chunk>> futureMap = new ConcurrentHashMap<>();

  @Inject ChunkBuilderFactory chunkBuilderFactory;

  public synchronized List<Future<Chunk>> queueChunkRangeToGenerate(
      Collection<ChunkRange> toGenerateSet) {
    List<Future<Chunk>> futureList = new LinkedList<>();
    for (ChunkRange toGenerate : toGenerateSet) {
      futureList.add(this.queueChunkRangeToGenerate(toGenerate));
    }
    return futureList;
  }

  public synchronized Future<Chunk> queueChunkRangeToGenerate(ChunkRange toGenerate) {
    futureMap.computeIfAbsent(
        toGenerate, k -> executor.submit(chunkBuilderFactory.create(toGenerate)));
    return futureMap.get(toGenerate);
  }

  public synchronized Chunk blockedChunkRangeToGenerate(ChunkRange toGenerate) throws Exception {
    return this.queueChunkRangeToGenerate(toGenerate).get();
  }
}
