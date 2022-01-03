package chunk;

import app.GameController;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import entity.collision.EntityContactListenerFactory;

public class ChunkFactory {

    @Inject
    Clock clock;

    @Inject
    GameStore gameStore;

    @Inject
    EntityContactListenerFactory entityContactListenerFactory;

    @Inject
    GameController gameController;

    @Inject
    ChunkFactory() {
    }

    public Chunk create(ChunkRange chunkRange) {
        return new Chunk(clock, gameStore,gameController, entityContactListenerFactory, chunkRange);
    }
}
