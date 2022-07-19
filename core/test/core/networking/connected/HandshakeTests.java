package core.networking.connected;

import core.app.game.Game;
import core.app.game.GameController;
import core.app.user.User;
import core.chunk.ActiveChunkManager;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.Clock;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.BaseServerConfig;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.mock.GdxTestRunner;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import core.networking.client.ClientNetworkHandle;
import core.networking.server.ServerNetworkHandle;
import core.networking.sync.SyncService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class HandshakeTests {

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
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion {
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

    serverGame.init();
    clientGame.init();

    TimeUnit.SECONDS.sleep(1);
  }

  @After
  public void cleanup() {
    try {
      clientNetworkHandle.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      serverNetworkHandle.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void tickClocks(int numberOfTicks, float timeout) throws InterruptedException {
    for (int i = 0; i < numberOfTicks; i++) {
      serverInjector.getInstance(EventService.class).firePostUpdateEvents();
      clientInjector.getInstance(EventService.class).firePostUpdateEvents();
      serverClock.tick();
      clientClock.tick();
      TimeUnit.MILLISECONDS.sleep((long) timeout * 1000);
    }
  }

  @Test
  public void testServerInitServerExtra() throws InterruptedException, ChunkNotFound {
    // THIS TEST ONLY VERIFIES THE CLIENT REMOVED THE EXTRA
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    tickClocks(2, .5f);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // server only
    Entity e2 = entityFactory.createEntity(coordinatesToTest); // both
    Entity e3 = entityFactory.createEntity(coordinatesToTest); // client only

    //        serverGameStore.addEntity(e1);
    serverGameStore.addEntity(e2);
    clientGameStore.addEntity(e2);
    clientGameStore.addEntity(e3);

    tickClocks(2, 0);

    serverNetworkHandle.initHandshake(clientUser.getUserID(), chunkRangeToTest);
    AtomicReference<Boolean> serverHandshakeLocked = new AtomicReference<>(false);
    serverClock.addTaskOnTickTime(
        1,
        () -> {
          serverHandshakeLocked.set(
              serverSyncService.isHandshakeLocked(clientUser.getUserID(), chunkRangeToTest));
        });

    tickClocks(2, 2);

    assert serverHandshakeLocked.get();

    serverClock.addTaskOnTickTime(
        GameSettings.HANDSHAKE_TIMEOUT,
        () -> {
          serverHandshakeLocked.set(
              serverSyncService.isHandshakeLocked(clientUser.getUserID(), chunkRangeToTest));
        });

    tickClocks(GameSettings.HANDSHAKE_TIMEOUT, 0);

    assert !serverHandshakeLocked.get();

    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitServer() throws InterruptedException, ChunkNotFound {
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // server only
    Entity e2 = entityFactory.createEntity(coordinatesToTest); // both
    Entity e3 = entityFactory.createEntity(coordinatesToTest); // client only

    serverGameStore.addEntity(e1);
    serverGameStore.addEntity(e2);
    clientGameStore.addEntity(e2);
    clientGameStore.addEntity(e3);

    serverNetworkHandle.initHandshake(clientUser.getUserID(), chunkRangeToTest);

    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitClient() throws InterruptedException, ChunkNotFound {
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // server only
    Entity e2 = entityFactory.createEntity(coordinatesToTest); // both
    Entity e3 = entityFactory.createEntity(coordinatesToTest); // client only

    serverGameStore.addEntity(e1);
    serverGameStore.addEntity(e2);
    clientGameStore.addEntity(e2);
    clientGameStore.addEntity(e3);

    clientNetworkHandle.initHandshake(chunkRangeToTest);

    AtomicReference<Boolean> clientHandshakeLocked = new AtomicReference<>(false);
    clientClock.addTaskOnTickTime(
        1,
        () -> {
          clientHandshakeLocked.set(
              clientSyncService.isHandshakeLocked(clientUser.getUserID(), chunkRangeToTest));
        });

    tickClocks(2, 2);

    assert clientHandshakeLocked.get();

    clientClock.addTaskOnTickTime(
        GameSettings.HANDSHAKE_TIMEOUT,
        () -> {
          clientHandshakeLocked.set(
              clientSyncService.isHandshakeLocked(clientUser.getUserID(), chunkRangeToTest));
        });

    tickClocks(GameSettings.HANDSHAKE_TIMEOUT, 0);
    assert !clientHandshakeLocked.get();

    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitUpdateHandshake()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    // client updates an entity that doesn't exist
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    Coordinates coordinatesToMove = new Coordinates(1, 1);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // both
    Entity e2 = entityFactory.createEntity(coordinatesToTest); // client only

    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    clientGameStore.addEntity(e2);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    clientGameController.moveEntity(e2.getUuid(), coordinatesToMove);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitReplaceHandshake()
      throws InterruptedException, ChunkNotFound, EntityNotFound {
    // client replaces an entity that doesn't exist on the server
    BlockFactory blockFactory = clientInjector.getInstance(BlockFactory.class);
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // both
    Block blockToRemove = blockFactory.createDirt(coordinatesToTest);
    Block blockToReplace = blockFactory.createSky(coordinatesToTest);

    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getEntity(e1.getUuid()) != null;
    assert clientGameStore.getEntity(e1.getUuid()) != null;
    assert serverChunk.equals(clientChunk);

    clientGameStore.addEntity(blockToRemove);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    // this will trigger the handshake
    clientGameController.replaceBlock(blockToRemove, blockToReplace);
    tickClocks(1000, 0.1f);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testClientInitUpdateHandshake()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    // server updates an entity that doesn't exist on client
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    Coordinates coordinatesToMove = new Coordinates(1, 1);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // both
    Entity e2 = entityFactory.createEntity(coordinatesToTest); // client only

    serverActiveChunkManager.addUserChunkSubscriptions(clientUser.getUserID(), chunkRangeToTest);
    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    serverGameStore.addEntity(e2);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    serverGameController.moveEntity(e2.getUuid(), coordinatesToMove);
    TimeUnit.SECONDS.sleep(2);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testClientInitReplaceHandshake()
      throws InterruptedException, WrongVersion, SerializationDataMissing, IOException,
          ChunkNotFound {
    // server updates an entity that doesn't exist on client
    BlockFactory blockFactory = clientInjector.getInstance(BlockFactory.class);
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(coordinatesToTest); // both
    Block blockToRemove = blockFactory.createDirt(coordinatesToTest);
    Block blockToReplace = blockFactory.createSky(coordinatesToTest);

    serverActiveChunkManager.addUserChunkSubscriptions(clientUser.getUserID(), chunkRangeToTest);
    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    serverGameStore.addEntity(blockToRemove);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    serverGameController.replaceBlock(blockToRemove, blockToReplace);
    TimeUnit.SECONDS.sleep(2);
    tickClocks(2000, 0.2f);
    assert serverChunk.equals(clientChunk);
  }
}
