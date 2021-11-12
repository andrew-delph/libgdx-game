package app.server;

import app.Game;
import chunk.ChunkFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import common.GameStore;
import common.events.EventConsumer;
import configuration.ServerConfig;
import entity.collision.CollisionService;
import generation.ChunkGenerationManager;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class ServerGame extends Game {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Inject
  public ServerGame(
      GameStore gameStore,
      ChunkFactory chunkFactory,
      ChunkGenerationManager chunkGenerationManager,
      EventConsumer eventConsumer,
      CollisionService collisionService)
      throws Exception {
    super(gameStore, chunkFactory, chunkGenerationManager, eventConsumer, collisionService);
  }

  public static void main(String[] args) throws InterruptedException, IOException {
    Injector injector = Guice.createInjector(new ServerConfig());
    Game game = injector.getInstance(Game.class);
    game.start();

    while (true) {
      Thread.sleep(Long.MAX_VALUE);
    }
  }

  @Override
  public void start() throws IOException, InterruptedException {
    super.start();
    serverNetworkHandle.start();
  }

  @Override
  public void stop() {
    super.stop();
    this.serverNetworkHandle.close();
  }
}
