package infra.app.client;

import com.google.inject.Inject;
import infra.app.Game;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.generation.ChunkGenerationManager;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.consumer.NetworkConsumer;
import infra.networking.events.EventFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClientGame extends Game {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Inject EventFactory eventFactory;

  @Inject
  public ClientGame(
      GameStore gameStore,
      ChunkFactory chunkFactory,
      ChunkGenerationManager chunkGenerationManager,
      NetworkConsumer networkConsumer)
      throws Exception {
    super(gameStore, chunkFactory, chunkGenerationManager, networkConsumer);
  }

  @Override
  public void stop() {
    super.stop();
    this.clientNetworkHandle.close();
  }

  @Override
  public void start() throws IOException, InterruptedException {
    this.clientNetworkHandle.connect();
    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    this.clientNetworkHandle.send(
        eventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    super.start();
  }
}
