package core.generation;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.GameStore;
import java.util.concurrent.Callable;

public class ChunkBuilder implements Callable<Chunk> {

  ChunkFactory chunkFactory;

  GameStore gameStore;

  BlockGenerator blockGenerator;

  GameController gameController;

  ChunkRange chunkRange;

  @Inject
  ChunkBuilder(
      ChunkFactory chunkFactory,
      GameStore gameStore,
      BlockGenerator blockGenerator,
      GameController gameController,
      ChunkRange chunkRange) {
    this.chunkFactory = chunkFactory;
    this.gameStore = gameStore;
    this.blockGenerator = blockGenerator;
    this.gameController = gameController;
    this.chunkRange = chunkRange;
  }

  @Override
  public Chunk call() throws Exception {
    try {
      Chunk chunk;
      if (this.gameStore.getChunk(this.chunkRange) == null) {
        chunk = this.chunkFactory.create(this.chunkRange);
        this.gameStore.addChunk(chunk);
      } else {
        chunk = this.gameStore.getChunk(this.chunkRange);
      }
      for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
        for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
          blockGenerator.generate(CommonFactory.createCoordinates(i, j));
        }
      }

      return chunk;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
