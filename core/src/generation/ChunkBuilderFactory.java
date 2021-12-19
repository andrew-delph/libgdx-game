package generation;

import app.GameController;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;

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
    ChunkBuilderFactory() {
    }

    public ChunkBuilder create(ChunkRange chunkRange) {
        return new ChunkBuilder(chunkFactory, gameStore, blockGenerator, gameController, chunkRange);
    }
}
