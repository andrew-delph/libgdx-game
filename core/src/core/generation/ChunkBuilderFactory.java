package core.generation;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.chunk.ChunkFactory;
import core.common.ChunkRange;
import core.common.GameStore;

public class ChunkBuilderFactory {
  @Inject ChunkFactory chunkFactory;
  @Inject GameStore gameStore;
  @Inject BlockGenerator blockGenerator;
  @Inject GameController gameController;

  @Inject
  ChunkBuilderFactory() {}

  public ChunkBuilder create(ChunkRange chunkRange) {
    return new ChunkBuilder(chunkFactory, gameStore, blockGenerator, gameController, chunkRange);
  }
}
