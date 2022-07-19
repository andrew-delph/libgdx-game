package core.app.game;

import core.app.update.UpdateTask;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.chunk.world.exceptions.BodyNotFound;
import com.google.inject.Inject;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventConsumer;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.entity.attributes.msc.Coordinates;
import core.entity.collision.CollisionService;
import java.io.IOException;
import java.util.Timer;

public class Game {

  @Inject
  UpdateTask updateTask;
  @Inject CollisionService collisionService;
  @Inject
  EventConsumer eventConsumer;
  @Inject
  ChunkFactory chunkFactory;
  @Inject
  GameStore gameStore;

  Timer timer;

  @Inject
  public Game() throws Exception {}

  public void start()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
      BodyNotFound {
    init();
    timer = new Timer(true);
    timer.scheduleAtFixedRate(updateTask, 0, GameSettings.UPDATE_INTERVAL);
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
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
  }

  public void postStartInit()
      throws SerializationDataMissing, InterruptedException, IOException, WrongVersion {}
}
