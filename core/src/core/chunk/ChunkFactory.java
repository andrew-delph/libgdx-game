package core.chunk;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.Clock;
import core.common.GameStore;
import core.entity.collision.EntityContactListenerFactory;

public class ChunkFactory {

  @Inject Clock clock;

  @Inject GameStore gameStore;

  @Inject EntityContactListenerFactory entityContactListenerFactory;

  @Inject GameController gameController;

  @Inject
  ChunkFactory() {}

  public Chunk create(ChunkRange chunkRange) {
    return new Chunk(clock, gameStore, gameController, entityContactListenerFactory, chunkRange);
  }
}
