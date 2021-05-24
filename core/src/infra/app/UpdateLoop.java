package infra.app;

import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.networkobject.Coordinates;
import infra.generation.ChunkGenerationManager;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateLoop extends TimerTask {

  @Inject Clock clock;

  @Inject GameStore gameStore;

  @Inject
  ChunkGenerationManager chunkGenerationManager;

  ExecutorService executor;

  public UpdateLoop() {
    executor = Executors.newCachedThreadPool();
  }

  @Override
  public void run() {
    this.clock.tick();
    List<Callable<Chunk>> callableChunkList = this.gameStore.getChunkOnClock(this.clock.currentTick);

//    for (ChunkRange chunkRange : this.gameStore.getActiveChunkRangeSet()) {
//      callableChunkList.addAll(this.chunkGenerationManager.generateAround(chunkRange));
//    }
    callableChunkList.addAll(this.chunkGenerationManager.generateAround(new ChunkRange(new Coordinates(0,0))));

    try {
      executor.invokeAll(callableChunkList);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
