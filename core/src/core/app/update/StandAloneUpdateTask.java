package core.app.update;

import com.google.inject.Inject;
import core.app.screen.BaseCamera;
import core.chunk.Chunk;
import core.common.ChunkRange;
import core.common.Clock;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StandAloneUpdateTask extends UpdateTask {

  static final Logger LOGGER = LogManager.getLogger();

  private final ExecutorService executor = Executors.newCachedThreadPool();

  @Inject public Clock clock;
  @Inject public GameStore gameStore;
  @Inject EventService eventService;
  @Inject BaseCamera baseCamera;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ChunkGenerationService chunkGenerationService;
  @Inject WaterService waterService;

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
      Set<Chunk> chunksOnTick = this.gameStore.getChunkOnClock(this.clock.getCurrentTick());
      LOGGER.debug("Updating " + chunksOnTick.size() + " chunks.");
      executor.invokeAll(this.gameStore.getChunkOnClock(this.clock.getCurrentTick()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    this.eventService.firePostUpdateEvents();

    try {
      waterService.update();
    } catch (ChunkNotFound e) {
      e.printStackTrace();
    }
  }
}
