package core.app.game;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.update.UpdateTask;
import core.chunk.ChunkFactory;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.CommonFactory;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventConsumer;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.entity.collision.CollisionService;
import java.io.IOException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Game {

  public Instant gameStartTime;
  @Inject UpdateTask updateTask;
  @Inject CollisionService collisionService;
  @Inject EventConsumer eventConsumer;
  @Inject ChunkFactory chunkFactory;
  @Inject GameStore gameStore;
  Timer timer;

  @Inject
  public Game() throws Exception {}

  public void start()
      throws IOException,
          InterruptedException,
          SerializationDataMissing,
          WrongVersion,
          BodyNotFound {
    init();
    timer = new Timer(true);
    timer.scheduleAtFixedRate(updateTask, 0, GameSettings.UPDATE_INTERVAL);
    timer.scheduleAtFixedRate(
        new TimerTask() {

          @Override
          public void run() {
            Gdx.app.log(GameSettings.LOG_TAG, "RUN GARBAGE COLLECTOR");
            System.gc();
          }
        },
        0,
        TimeUnit.MINUTES.toMillis(2));
    gameStartTime = Instant.now();
  }

  public void init()
      throws SerializationDataMissing, WrongVersion, IOException, InterruptedException {
    this.preStartInit();
    this.eventConsumer.init();
    this.collisionService.init();
    this.postStartInit();
  }

  public void stop() {
    timer.cancel();
  }

  public void preStartInit() throws SerializationDataMissing {
    gameStore.addChunk(
        chunkFactory.create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0))));
  }

  public void postStartInit()
      throws SerializationDataMissing, InterruptedException, IOException, WrongVersion {}
}
