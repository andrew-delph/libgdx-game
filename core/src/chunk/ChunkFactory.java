package chunk;

import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import entity.collision.EntityContactListenerFactory;

public class ChunkFactory {

  @Inject Clock clock;

  @Inject GameStore gameStore;

  @Inject EntityContactListenerFactory entityContactListenerFactory;

  @Inject
  ChunkFactory() {}

  public Chunk create(ChunkRange chunkRange) {
    return new Chunk(clock, gameStore, entityContactListenerFactory, chunkRange);
  }
  ;
}
