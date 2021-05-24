package infra.generation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.Chunk;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.GameStore;
import infra.common.networkobject.Coordinates;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;

import java.util.concurrent.Callable;

public class ChunkBuilder implements Callable<Chunk> {

  @Inject ChunkFactory chunkFactory;

  @Inject GameStore gameStore;

  @Inject BlockFactory blockFactory;

  ChunkRange chunkRange;

  @Inject
  ChunkBuilder(ChunkFactory chunkFactory, GameStore gameStore, @Assisted ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
  }

  @Override
  public Chunk call() throws Exception {
    Chunk chunk = this.chunkFactory.create(this.chunkRange);
    for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
      for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
        Block block = blockFactory.create();
        block.coordinates = new Coordinates(i, j);
        chunk.addEntity(block);
      }
    }
    this.gameStore.addChunk(chunk);
    return chunk;
  }
}
