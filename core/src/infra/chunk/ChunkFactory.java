package infra.chunk;

import com.google.inject.Inject;
import infra.common.Clock;
import infra.common.GameStore;
import infra.entity.collision.EntityContactListenerFactory;

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
