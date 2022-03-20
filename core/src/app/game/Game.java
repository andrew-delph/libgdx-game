package app.game;

import app.update.UpdateTask;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameSettings;
import common.GameStore;
import common.events.EventConsumer;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import entity.collision.CollisionService;
import java.io.IOException;
import java.util.Timer;

public class Game {

  @Inject UpdateTask updateTask;
  @Inject CollisionService collisionService;
  @Inject EventConsumer eventConsumer;
  @Inject ChunkFactory chunkFactory;
  @Inject GameStore gameStore;

  Timer timer;

  @Inject
  public Game() throws Exception {}

  public void start()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion {
    this.preStartInit();
    this.eventConsumer.init();
    this.collisionService.init();
    this.postStartInit();
    timer = new Timer(true);
    timer.scheduleAtFixedRate(updateTask, 0, GameSettings.UPDATE_INTERVAL);
  }

  public void stop() {
    timer.cancel();
  }

  public void preStartInit() throws SerializationDataMissing {
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
  }

  public void postStartInit()
      throws SerializationDataMissing, InterruptedException, IOException, WrongVersion {}
}
