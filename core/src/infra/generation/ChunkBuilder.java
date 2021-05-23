package infra.generation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.Chunk;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.GameStore;

import java.util.concurrent.Callable;

public class ChunkBuilder implements Callable<Chunk> {

  @Inject ChunkFactory chunkFactory;

  @Inject GameStore gameStore;

  ChunkRange chunkRange;

  @Inject
  ChunkBuilder(ChunkFactory chunkFactory, GameStore gameStore, @Assisted ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
  }

  @Override
  public Chunk call() throws Exception {
    Chunk chunk = this.chunkFactory.create(this.chunkRange);
    this.gameStore.addChunk(chunk);
    return chunk;
  }
}
