package infra.app;

import com.google.inject.Inject;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.events.EventConsumer;
import infra.entity.Entity;
import infra.generation.ChunkGenerationManager;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

public class Game {

  @Inject GameStore gameStore;

  @Inject UpdateLoop updateLoop;

  Timer timer;

  @Inject
  public Game(
      GameStore gameStore,
      ChunkFactory chunkFactory,
      ChunkGenerationManager chunkGenerationManager,
      EventConsumer eventConsumer)
      throws Exception {
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
    eventConsumer.init();
  }

  public void start() throws IOException, InterruptedException {
    timer = new Timer(true);
    timer.scheduleAtFixedRate(updateLoop, 0, 16);
  }

  public void stop() {
    timer.cancel();
  }

  List<Entity> getEntityListInRange(int x1, int y1, int x2, int y2) {
    return this.gameStore.getEntityListInRange(x1, y1, x2, y2);
  }
}
