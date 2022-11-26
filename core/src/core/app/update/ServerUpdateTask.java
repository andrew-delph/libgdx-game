package core.app.update;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.chunk.Chunk;
import core.common.Clock;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.exceptions.ChunkNotFound;
import core.entity.ActiveEntityManager;
import core.entity.misc.water.WaterService;
import core.generation.ChunkGenerationService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ServerUpdateTask extends UpdateTask {

  public static final ExecutorService executor = Executors.newCachedThreadPool();
  @Inject public Clock clock;
  @Inject public GameStore gameStore;
  @Inject EventService eventService;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ChunkGenerationService chunkGenerationService;

  @Inject WaterService waterService;

  public ServerUpdateTask() {}

  @Override
  public void run() {
    /*
    - queue generation for active entities
    - don't delete chunks
     */
    this.clock.tick();

    // queue generation for active entities
    chunkGenerationService.queueChunkRangeToGenerate(activeEntityManager.getActiveChunkRanges());

    // update all active chunks
    Set<Chunk> chunksOnTick = new HashSet<>();
    try {
      chunksOnTick = this.gameStore.getChunkOnClock(this.clock.getCurrentTick());
      Gdx.app.debug(GameSettings.LOG_TAG, "Updating " + chunksOnTick.size() + " chunks.");

      executor.invokeAll(chunksOnTick);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.eventService.firePostUpdateEvents();

    try {
      waterService.update(
          chunksOnTick.stream().map((Chunk c) -> c.chunkRange).collect(Collectors.toSet()));
    } catch (ChunkNotFound e) {
      e.printStackTrace();
    }
  }
}
