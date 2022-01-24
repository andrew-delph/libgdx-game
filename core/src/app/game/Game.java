package app.game;

import app.update.UpdateTask;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import common.exceptions.SerializationDataMissing;
import configuration.GameSettings;
import entity.Entity;
import entity.collision.CollisionService;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

public class Game {

    @Inject
    UpdateTask updateTask;
    @Inject CollisionService collisionService;
    @Inject EventConsumer eventConsumer;
    @Inject ChunkFactory chunkFactory;
    @Inject GameStore gameStore;

    Timer timer;

    @Inject
    public Game() throws Exception {
    }

    public void start() throws IOException, InterruptedException, SerializationDataMissing {
        this.eventConsumer.init();
        this.collisionService.init();
        timer = new Timer(true);
        this.init();
        timer.scheduleAtFixedRate(updateTask, 0, GameSettings.UPDATE_INTERVAL);
    }

    public void stop() {
        timer.cancel();
    }

    public List<Entity> getEntityListInRange(int x1, int y1, int x2, int y2) {
        return this.gameStore.getEntityListInRange(x1, y1, x2, y2);
    }

    public void init() throws SerializationDataMissing {
        gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
    }
}
