package networking.connected;

import app.GameController;
import app.game.Game;
import app.user.User;
import chunk.ChunkFactory;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Clock;
import common.GameStore;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.EntityFactory;
import java.io.IOException;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import networking.sync.SyncService;
import org.junit.Before;
import org.junit.Test;

public class testClientServerAttributes {

  Injector clientInjector;
  Injector serverInjector;
  ClientNetworkHandle clientNetworkHandle;
  ServerNetworkHandle serverNetworkHandle;

  Game serverGame;
  Game clientGame;

  GameStore serverGameStore;
  GameStore clientGameStore;

  ChunkFactory chunkFactory;
  EntityFactory entityFactory;

  GameController clientGameController;
  GameController serverGameController;

  User serverUser;
  User clientUser;

  Clock serverClock;
  Clock clientClock;

  SyncService serverSyncService;
  SyncService clientSyncService;

  @Before
  public void setup()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
          BodyNotFound {
    clientInjector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new BaseServerConfig());

    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);
    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

    chunkFactory = serverInjector.getInstance(ChunkFactory.class);
    entityFactory = serverInjector.getInstance(EntityFactory.class);

    serverGameStore = serverInjector.getInstance(GameStore.class);
    clientGameStore = clientInjector.getInstance(GameStore.class);

    serverGameController = serverInjector.getInstance(GameController.class);
    clientGameController = clientInjector.getInstance(GameController.class);

    serverGame = serverInjector.getInstance(Game.class);
    clientGame = clientInjector.getInstance(Game.class);

    serverUser = serverInjector.getInstance(User.class);
    clientUser = clientInjector.getInstance(User.class);

    serverClock = serverInjector.getInstance(Clock.class);
    clientClock = clientInjector.getInstance(Clock.class);

    serverSyncService = serverInjector.getInstance(SyncService.class);
    clientSyncService = clientInjector.getInstance(SyncService.class);

    serverGame.start();
    //    clientGame.start();
  }

  @Test
  void testCreateHealth() {
    // connect
    // create entity, make sure equal
  }

  @Test
  void testCreateHealthNotDefault() {
    // create entity, health is not default
  }

  @Test
  void testUpdateHealth() {
    // update health
  }

  // test connect with entity, inventory is the same
  // test connect with entity where inventory non default

  /*
  update tests:
  - all empty - > all orbs
  - 1 orb -> 2 orbs
   */
}
