package infra.app.client;

import com.google.inject.Inject;
import infra.app.UpdateLoop;
import infra.chunk.Chunk;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.render.BaseCamera;
import infra.generation.ChunkGenerationManager;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.EventFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class ClientUpdateLoop extends UpdateLoop {

  @Inject ChunkGenerationManager chunkGenerationManager;

  @Inject ChunkFactory chunkFactory;

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Inject EventFactory eventFactory;

  @Inject BaseCamera baseCamera;

  @Override
  public void run() {
    this.clock.tick();
    List<Callable<Chunk>> callableChunkList =
        this.gameStore.getChunkOnClock(this.clock.currentTick);

    List<ChunkRange> chunkRangeListOnCamera = baseCamera.getChunkRangeOnScreen();

    Set<ChunkRange> subscribeChunkRange = new HashSet<>();
    for (ChunkRange chunkRange : chunkRangeListOnCamera) {
      if (this.gameStore.getChunk(chunkRange) == null) {
        this.gameStore.addChunk(chunkFactory.create(chunkRange));
        subscribeChunkRange.add(chunkRange);
      }
    }

    if (subscribeChunkRange.size() > 0) {
      this.clientNetworkHandle.send(
          eventFactory
              .createSubscriptionOutgoingEvent(new LinkedList<>(subscribeChunkRange))
              .toNetworkEvent());
    }

    try {
      executor.invokeAll(callableChunkList);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
