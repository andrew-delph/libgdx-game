package app.update;

import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import entity.ActiveEntityManager;
import generation.ChunkGenerationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUpdateTask extends UpdateTask {

  public final ExecutorService executor = Executors.newCachedThreadPool();
  @Inject public Clock clock;
  @Inject public GameStore gameStore;
  @Inject EventService eventService;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ChunkGenerationService chunkGenerationService;

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
    try {
      executor.invokeAll(this.gameStore.getChunkOnClock(this.clock.currentTick));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.eventService.firePostUpdateEvents();
  }
}
