package infra.app;

import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.common.Clock;
import infra.common.GameStore;
import infra.generation.GenerationManager;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateLoop extends TimerTask {

  @Inject Clock clock;

  @Inject GameStore gameStore;

  @Inject
  GenerationManager generationManager;

  ExecutorService executor;

  public UpdateLoop() {
    executor = Executors.newCachedThreadPool();
  }

  @Override
  public void run() {
    this.clock.tick();
    List<Callable<Chunk>> callableChunkList = this.gameStore.getChunkOnClock(this.clock.currentTick);

    for (ChunkRange chunkRange : this.gameStore.getActiveChunkRangeSet()) {
      callableChunkList.addAll(this.generationManager.generateAround(chunkRange));
    }

    try {
      executor.invokeAll(callableChunkList);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
