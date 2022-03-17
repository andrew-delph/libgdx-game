package networking.connected;

import app.GameController;
import app.game.Game;
import app.user.User;
import chunk.ActiveChunkManager;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.ChunkClockMap;
import common.Coordinates;
import common.GameStore;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.ActiveEntityManager;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import generation.ChunkBuilderFactory;
import generation.ChunkGenerationService;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import networking.ConnectionStore;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.mock.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class testSingleClientRunningGame {

  Injector clientInjector;
  Injector serverInjector;

  ClientNetworkHandle clientNetworkHandle;
  ServerNetworkHandle serverNetworkHandle;

  GameStore serverGameStore;
  GameStore clientGameStore;

  Game clientGame, serverGame;

  @Before
  public void setup() throws IOException, InterruptedException, SerializationDataMissing {

    clientInjector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new BaseServerConfig());

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);

    clientGame = clientInjector.getInstance(Game.class);
    serverGame = serverInjector.getInstance(Game.class);

    serverGameStore = serverInjector.getInstance(GameStore.class);
    clientGameStore = clientInjector.getInstance(GameStore.class);

    serverGame.start();
    clientGame.start();
  }

  @After
  public void cleanup() {
    try {
      clientGame.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      serverGame.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAuthentication() throws InterruptedException {
    ConnectionStore serverConnectionStore = serverInjector.getInstance(ConnectionStore.class);
    ConnectionStore clientConnectionStore = clientInjector.getInstance(ConnectionStore.class);
    TimeUnit.SECONDS.sleep(1);
    assert serverConnectionStore.size() == 1;
    assert clientConnectionStore.size() == 1;
  }

  @Test
  public void testClientCreateEntity() throws InterruptedException, EntityNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.addEntity(clientEntityFactory.createEntity());
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateUpdateEntity()
      throws IOException, InterruptedException, EntityNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    Entity clientEntity = clientGameController.addEntity(clientEntityFactory.createEntity());
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
    clientGameController.moveEntity(clientEntity.uuid, new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);

    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateBlock() throws IOException, InterruptedException, EntityNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createDirtBlock(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateUpdateBlock()
      throws IOException, InterruptedException, EntityNotFound {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createDirtBlock(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    clientGameController.moveEntity(clientEntity.uuid, new Coordinates(0, 1));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientSubscription() throws IOException, InterruptedException {
    ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);
    User clientUser = clientInjector.getInstance(User.class);
    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        new HashSet<>(clientChunkClockMap.getChunkRangeSet()),
        new HashSet<>(serverActiveChunkManager.getUserChunkRanges(clientUser.getUserID())));
  }

  @Test
  public void testSubscriptionServerCreateEntity()
      throws IOException, InterruptedException, EntityNotFound {
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);
    User clientUser = clientInjector.getInstance(User.class);
    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        new HashSet<>(clientChunkClockMap.getChunkRangeSet()),
        new HashSet<>(serverActiveChunkManager.getUserChunkRanges(clientUser.getUserID())));
    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    Entity serverEntity = serverGameController.addEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);
    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.uuid).coordinates);
  }

  @Test
  public void testSubscriptionServerCreateEntityUpdate()
      throws IOException, InterruptedException, EntityNotFound {

    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);
    User clientUser = clientInjector.getInstance(User.class);

    TimeUnit.SECONDS.sleep(1);
    Assert.assertEquals(
        new HashSet<>(clientChunkClockMap.getChunkRangeSet()),
        new HashSet<>(serverActiveChunkManager.getUserChunkRanges(clientUser.getUserID())));
    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    Entity serverEntity = serverGameController.addEntity(clientEntityFactory.createEntity());
    TimeUnit.SECONDS.sleep(1);
    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.uuid).coordinates);
    serverGameController.moveEntity(serverEntity.uuid, new Coordinates(0, 1));
    TimeUnit.SECONDS.sleep(1);
    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.uuid).coordinates);
  }

  @Test
  public void testClientDisconnectRemoveEntity() throws InterruptedException, EntityNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    TimeUnit.SECONDS.sleep(1);
    Entity clientEntity = clientGameController.addEntity(clientEntityFactory.createEntity());
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
    this.clientNetworkHandle.close();
    TimeUnit.SECONDS.sleep(1);
    assert !serverGameStore.doesEntityExist(clientEntity.uuid);
  }

  @Test
  public void testClientReplaceBlock() throws InterruptedException, EntityNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
    EventService clientEventService = clientInjector.getInstance(EventService.class);
    serverGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
    serverGameController.addEntity(serverBlockFactory.createDirt());
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == DirtBlock.class;
    TimeUnit.SECONDS.sleep(1);
    Block clientBlock = serverGameStore.getBlock(new Coordinates(0, 0));
    clientEventService.fireEvent(
        EventTypeFactory.createReplaceBlockOutgoingEvent(
            clientBlock.uuid,
            serverBlockFactory.createSky(),
            new ChunkRange(clientBlock.coordinates)));
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == SkyBlock.class;
  }

  @Test
  public void testServerReplaceBlock() throws InterruptedException, EntityNotFound {
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
    EventService serverEventService = serverInjector.getInstance(EventService.class);
    EventService clientEventService = clientInjector.getInstance(EventService.class);
    ActiveChunkManager activeChunkManager = serverInjector.getInstance(ActiveChunkManager.class);

    ChunkGenerationService chunkGenerationService =
        serverInjector.getInstance(ChunkGenerationService.class);
    chunkGenerationService.queueChunkRangeToGenerate(new ChunkRange(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);
    clientGameStore.getBlock(new Coordinates(0, 0));

    Block serverBlockOriginal = serverGameStore.getBlock(new Coordinates(0, 0));
    assert serverBlockOriginal != null;
    serverGameController.addEntity(serverBlockOriginal);
    Block serverBlockReplacement = serverBlockFactory.createSky();
    serverGameController.replaceBlock(serverBlockOriginal, serverBlockReplacement);
    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getBlock(new Coordinates(0, 0)).equals(serverBlockReplacement);
    TimeUnit.SECONDS.sleep(1);
    clientEventService.firePostUpdateEvents();
    serverEventService.firePostUpdateEvents();
    Assert.assertEquals(clientGameStore.getBlock(new Coordinates(0, 0)).getClass(), SkyBlock.class);
  }

  @Test
  public void testClientCreateLadder() throws Exception {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkBuilderFactory chunkBuilderFactory = serverInjector.getInstance(ChunkBuilderFactory.class);
    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    serverGameStore.addChunk(chunkBuilderFactory.create(chunkRange).call());
    clientGameStore.addChunk(clientNetworkHandle.requestChunkBlocking(chunkRange));
    TimeUnit.SECONDS.sleep(1);
    Entity clientEntity = clientGameController.createLadder(coordinates);
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .equals(clientGameStore.getEntity(clientEntity.uuid));
  }

  @Test
  public void testServerCreateLadder() throws Exception {
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkBuilderFactory chunkBuilderFactory = serverInjector.getInstance(ChunkBuilderFactory.class);
    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    serverGameStore.addChunk(chunkBuilderFactory.create(chunkRange).call());
    clientGameStore.addChunk(clientNetworkHandle.requestChunkBlocking(chunkRange));
    Entity clientEntity = serverGameController.createLadder(coordinates);
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .equals(clientGameStore.getEntity(clientEntity.uuid));
  }

  @Test
  public void testClientCreateAIEntity() throws Exception {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    ActiveEntityManager serverActiveEntityManager =
        serverInjector.getInstance(ActiveEntityManager.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);

    Entity target = serverGameController.createEntity(new Coordinates(0, 0));
    Assert.assertEquals(0, serverActiveEntityManager.getActiveEntities().size());

    clientGameController.createAI(target.getUuid());
    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(1, serverActiveEntityManager.getActiveEntities().size());
  }

  @Test
  public void testRemoveClientToServer() throws InterruptedException, EntityNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameController clientGameController = clientInjector.getInstance(GameController.class);

    Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(myEntity.uuid)
        .equals(clientGameStore.getEntity(myEntity.uuid));
    clientGameController.removeEntity(myEntity.uuid);
    TimeUnit.SECONDS.sleep(1);

    assert !serverGameStore.doesEntityExist(myEntity.uuid);
    assert !clientGameStore.doesEntityExist(myEntity.uuid);
  }

  @Test
  public void testRemoveServerToClient() throws InterruptedException, EntityNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);
    User clientUser = clientInjector.getInstance(User.class);

    Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));

    serverActiveChunkManager.addUserChunkSubscriptions(
        clientUser.getUserID(), new ChunkRange(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(myEntity.uuid)
        .equals(clientGameStore.getEntity(myEntity.uuid));
    serverGameController.removeEntity(myEntity.uuid);
    TimeUnit.SECONDS.sleep(1);

    assert !serverGameStore.doesEntityExist(myEntity.uuid);
    assert !clientGameStore.doesEntityExist(myEntity.uuid);
  }

  @Test
  public void testEntityChunkSwap() throws InterruptedException, EntityNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    ChunkFactory serverChunkFactory = serverInjector.getInstance(ChunkFactory.class);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);
    ConnectionStore serverConnectionStore = serverInjector.getInstance(ConnectionStore.class);

    User clientUser = clientInjector.getInstance(User.class);
    assert serverConnectionStore.getConnection(clientUser.getUserID()) != null;

    serverActiveChunkManager.addUserChunkSubscriptions(
        clientUser.getUserID(), new ChunkRange(new Coordinates(0, 0)));
    Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
    Coordinates coordinatesToTest = new Coordinates(-1000, 1000);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    TimeUnit.SECONDS.sleep(1);

    // remove chunk from the client
    // add chunk to the server
    // move the entity to that chunk
    if (!serverGameStore.doesChunkExist(chunkRangeToTest))
      serverGameStore.addChunk(serverChunkFactory.create(chunkRangeToTest));
    if (clientGameStore.doesChunkExist(chunkRangeToTest)) {
      clientGameStore.removeChunk(chunkRangeToTest);
    }

    assert serverGameStore.doesEntityExist(myEntity.uuid);
    assert clientGameStore.doesEntityExist(myEntity.uuid);

    serverGameController.moveEntity(myEntity.uuid, coordinatesToTest);

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.doesEntityExist(myEntity.uuid);
    assert !clientGameStore.doesEntityExist(myEntity.uuid);
  }
}
