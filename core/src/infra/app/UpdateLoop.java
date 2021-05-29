package infra.app;

import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.common.Clock;
import infra.common.GameStore;
import infra.generation.ChunkGenerationManager;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateLoop extends TimerTask {

  @Inject public Clock clock;

  @Inject public GameStore gameStore;
  public ExecutorService executor;
  @Inject ChunkGenerationManager chunkGenerationManager;

  public UpdateLoop() {
    executor = Executors.newCachedThreadPool();
  }

  @Override
  public void run() {
    this.clock.tick();
    List<Callable<Chunk>> callableChunkList =
        this.gameStore.getChunkOnClock(this.clock.currentTick);

    callableChunkList.addAll(this.chunkGenerationManager.generateActiveEntities());

    try {
      executor.invokeAll(callableChunkList);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
