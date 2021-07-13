package infra.generation;

import com.google.inject.Inject;

import infra.app.GameController;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.GameStore;

public class ChunkBuilderFactory {
  @Inject
  ChunkFactory chunkFactory;
  @Inject
  GameStore gameStore;
  @Inject
  BlockGenerator blockGenerator;
  @Inject
  GameController gameController;

  @Inject
  ChunkBuilderFactory(){

  }

  public ChunkBuilder create(ChunkRange chunkRange){
    return new ChunkBuilder(chunkFactory, gameStore, blockGenerator, gameController, chunkRange);
  };
}
