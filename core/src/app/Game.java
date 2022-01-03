package app;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import entity.collision.CollisionService;
import generation.ChunkGenerationManager;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

public class Game {

    @Inject
    protected GameStore gameStore;

    @Inject
    UpdateLoop updateLoop;

    Timer timer;

    ChunkFactory chunkFactory;

    @Inject
    public Game(
            GameStore gameStore,
            ChunkFactory chunkFactory,
            ChunkGenerationManager chunkGenerationManager,
            EventConsumer eventConsumer,
            CollisionService collisionService)
            throws Exception {
        this.chunkFactory = chunkFactory;
        this.gameStore = gameStore;
        eventConsumer.init();
        collisionService.init();
    }

    public void start() throws IOException, InterruptedException, SerializationDataMissing {
        timer = new Timer(true);
        this.init();
        timer.scheduleAtFixedRate(updateLoop, 0, GameSettings.UPDATE_INTERVAL);
    }

    public void stop() {
        timer.cancel();
    }

    List<Entity> getEntityListInRange(int x1, int y1, int x2, int y2) {
        return this.gameStore.getEntityListInRange(x1, y1, x2, y2);
    }

    public void init() throws SerializationDataMissing {
        gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
    }
}
