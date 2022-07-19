package core.app.update;

import com.google.inject.Inject;
import core.app.screen.BaseCamera;
import core.chunk.ChunkRange;
import core.common.Clock;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.ActiveEntityManager;
import core.generation.ChunkGenerationService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StandAloneUpdateTask extends UpdateTask {

  private final ExecutorService executor = Executors.newCachedThreadPool();

  @Inject public Clock clock;
  @Inject public GameStore gameStore;
  @Inject EventService eventService;
  @Inject BaseCamera baseCamera;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ChunkGenerationService chunkGenerationService;

  public StandAloneUpdateTask() {}

  @Override
  public void run() {
    /*
    - create requested chunks from screen. send them to the generation manager
    - generate around active entities
    - don't delete chunks
     */
    this.clock.tick();

    Set<ChunkRange> requiredChunkRanges = new HashSet<>();

    // get the set of onscreen chunks
    requiredChunkRanges.addAll(baseCamera.getChunkRangeOnScreen());
    // get the set of active entities. get their chunks
    requiredChunkRanges.addAll(activeEntityManager.getActiveChunkRanges());
    // generate them all
    chunkGenerationService.queueChunkRangeToGenerate(requiredChunkRanges);

    try {
      executor.invokeAll(this.gameStore.getChunkOnClock(this.clock.getCurrentTick()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    this.eventService.firePostUpdateEvents();
  }
}
