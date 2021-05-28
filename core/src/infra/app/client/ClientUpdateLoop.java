package infra.app.client;

import infra.app.UpdateLoop;
import infra.chunk.Chunk;

import java.util.List;
import java.util.concurrent.Callable;

public class ClientUpdateLoop extends UpdateLoop {

  @Override
  public void run() {
    this.clock.tick();
    List<Callable<Chunk>> callableChunkList =
        this.gameStore.getChunkOnClock(this.clock.currentTick);

    try {
      executor.invokeAll(callableChunkList);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
