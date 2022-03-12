package networking.connected;

import app.GameController;
import app.game.Game;
import app.user.User;
import chunk.ActiveChunkManager;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

  @Before
  public void setup() throws IOException, InterruptedException, SerializationDataMissing {
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

    serverGame.start();
    clientGame.start();

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

  @Test
  public void testServerInitServerExtra() throws InterruptedException {
    // THIS TEST ONLY VERIFIES THE CLIENT REMOVED THE EXTRA
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(); // server only
    Entity e2 = entityFactory.createEntity(); // both
    Entity e3 = entityFactory.createEntity(); // client only

    e1.coordinates = coordinatesToTest;
    e2.coordinates = coordinatesToTest;
    e3.coordinates = coordinatesToTest;

    //        serverGameStore.addEntity(e1);
    serverGameStore.addEntity(e2);
    clientGameStore.addEntity(e2);
    clientGameStore.addEntity(e3);

    serverNetworkHandle.initHandshake(clientUser.getUserID(), chunkRangeToTest);

    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitServer() throws InterruptedException {
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(); // server only
    Entity e2 = entityFactory.createEntity(); // both
    Entity e3 = entityFactory.createEntity(); // client only

    e1.coordinates = coordinatesToTest;
    e2.coordinates = coordinatesToTest;
    e3.coordinates = coordinatesToTest;

    serverGameStore.addEntity(e1);
    serverGameStore.addEntity(e2);
    clientGameStore.addEntity(e2);
    clientGameStore.addEntity(e3);

    serverNetworkHandle.initHandshake(clientUser.getUserID(), chunkRangeToTest);

    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitClient() throws InterruptedException {
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(); // server only
    Entity e2 = entityFactory.createEntity(); // both
    Entity e3 = entityFactory.createEntity(); // client only

    e1.coordinates = coordinatesToTest;
    e2.coordinates = coordinatesToTest;
    e3.coordinates = coordinatesToTest;

    serverGameStore.addEntity(e1);
    serverGameStore.addEntity(e2);
    clientGameStore.addEntity(e2);
    clientGameStore.addEntity(e3);

    clientNetworkHandle.initHandshake(chunkRangeToTest);

    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitUpdateHandshake() throws InterruptedException, EntityNotFound {
    // client updates an entity that doesn't exist
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    Coordinates coordinatesToMove = new Coordinates(1, 1);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(); // both
    Entity e2 = entityFactory.createEntity(); // client only

    e1.coordinates = coordinatesToTest;
    e2.coordinates = coordinatesToTest;

    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    clientGameStore.addEntity(e2);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    clientGameController.moveEntity(e2.uuid, coordinatesToMove);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testServerInitReplaceHandshake() throws InterruptedException {
    // client replaces an entity that doesn't exist on the server
    BlockFactory blockFactory = clientInjector.getInstance(BlockFactory.class);
    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
    clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

    Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
    Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
    assert serverChunk.equals(clientChunk);

    Entity e1 = entityFactory.createEntity(); // both
    Block blockToRemove = blockFactory.createDirt();
    Block blockToReplace = blockFactory.createSky();

    e1.coordinates = coordinatesToTest;
    blockToRemove.coordinates = coordinatesToTest;
    blockToReplace.coordinates = coordinatesToTest;

    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    clientGameStore.addEntity(blockToRemove);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    clientGameController.replaceBlock(blockToRemove, blockToReplace);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testClientInitUpdateHandshake() throws InterruptedException, EntityNotFound {
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

    Entity e1 = entityFactory.createEntity(); // both
    Entity e2 = entityFactory.createEntity(); // client only
    e1.coordinates = coordinatesToTest;
    e2.coordinates = coordinatesToTest;

    serverActiveChunkManager.addUserChunkSubscriptions(clientUser.getUserID(), chunkRangeToTest);
    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    serverGameStore.addEntity(e2);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    serverGameController.moveEntity(e2.uuid, coordinatesToMove);
    TimeUnit.SECONDS.sleep(2);
    assert serverChunk.equals(clientChunk);
  }

  @Test
  public void testClientInitReplaceHandshake() throws InterruptedException {
    // server updates an entity that doesn't exist on client
    BlockFactory blockFactory = clientInjector.getInstance(BlockFactory.class);
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

    Entity e1 = entityFactory.createEntity(); // both
    Block blockToRemove = blockFactory.createDirt();
    Block blockToReplace = blockFactory.createSky();

    e1.coordinates = coordinatesToTest;
    blockToRemove.coordinates = coordinatesToTest;
    blockToReplace.coordinates = coordinatesToTest;

    serverActiveChunkManager.addUserChunkSubscriptions(clientUser.getUserID(), chunkRangeToTest);
    clientGameController.addEntity(e1);
    TimeUnit.SECONDS.sleep(1);
    assert serverChunk.equals(clientChunk);

    serverGameStore.addEntity(blockToRemove);
    TimeUnit.SECONDS.sleep(1);
    assert !serverChunk.equals(clientChunk);

    serverGameController.replaceBlock(blockToRemove, blockToReplace);
    TimeUnit.SECONDS.sleep(2);
    assert serverChunk.equals(clientChunk);
  }
}
