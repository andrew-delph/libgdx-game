package app.client;

import com.google.inject.Inject;
import app.Game;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import entity.collision.CollisionService;
import generation.ChunkGenerationManager;
import networking.client.ClientNetworkHandle;
import networking.events.EventFactory;

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
      EventConsumer eventConsumer,
      CollisionService collisionService)
      throws Exception {
    super(gameStore, chunkFactory, chunkGenerationManager, eventConsumer, collisionService);
  }

  @Override
  public void stop() {
    super.stop();
    this.clientNetworkHandle.close();
  }

  @Override
  public void start() throws IOException, InterruptedException {
    this.clientNetworkHandle.connect();
    super.start();
  }
}
