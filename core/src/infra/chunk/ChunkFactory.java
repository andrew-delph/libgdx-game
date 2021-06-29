package infra.chunk;

import com.google.inject.Inject;

import infra.common.Clock;
import infra.common.GameStore;

public class ChunkFactory {

  @Inject
  Clock clock;

  @Inject
  GameStore gameStore;

  @Inject
  ChunkFactory(){

  }
  public Chunk create(ChunkRange chunkRange){
      return new Chunk(clock, gameStore, chunkRange);
  };
}
