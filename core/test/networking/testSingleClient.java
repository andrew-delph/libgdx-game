package networking;

import app.Game;
import app.GameController;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.ChunkClockMap;
import common.Coordinates;
import common.GameStore;
import common.events.EventService;
import configuration.ClientConfig;
import configuration.BaseServerConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import networking.client.ClientNetworkHandle;
import networking.events.EventFactory;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class testSingleClient {

  Injector clientInjector;

  Injector serverInjector;

  ClientNetworkHandle clientNetworkHandle;

  ServerNetworkHandle serverNetworkHandle;

  Game clientGame, serverGame;

  @Before
  public void setup() throws IOException, InterruptedException {
    clientInjector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new BaseServerConfig());

    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);
    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

    clientGame = clientInjector.getInstance(Game.class);
    serverGame = serverInjector.getInstance(Game.class);

    clientGame.start();
    serverGame.start();
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
  public void testAuthentication() throws IOException, InterruptedException {

    ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);

    assert connectionStore.size() == 0;

    TimeUnit.SECONDS.sleep(1);

    assert connectionStore.size() == 1;
  }

  @Test
  public void testClientCreateEntity() throws IOException, InterruptedException {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateUpdateEntity() throws IOException, InterruptedException {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);

    Entity clientEntity = clientGameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    clientGameController.moveEntity(clientEntity.uuid, new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);

    System.out.println(serverGameStore.getEntity(clientEntity.uuid).coordinates);
    System.out.println(clientEntity.coordinates);

    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateBlock() throws IOException, InterruptedException {

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
  public void testClientCreateUpdateBlock() throws IOException, InterruptedException {

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

    ChunkSubscriptionService serverChunkSubscriptionService =
        serverInjector.getInstance(ChunkSubscriptionService.class);

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        new HashSet<>(clientChunkClockMap.getChunkRangeList()),
        new HashSet<>(
            serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                clientNetworkHandle.uuid)));
  }

  @Test
  public void testSubscriptionServerCreateEntity() throws IOException, InterruptedException {
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
    ChunkSubscriptionService serverChunkSubscriptionService =
        serverInjector.getInstance(ChunkSubscriptionService.class);

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        new HashSet<>(clientChunkClockMap.getChunkRangeList()),
        new HashSet<>(
            serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                clientNetworkHandle.uuid)));

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    Entity serverEntity = serverGameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.uuid).coordinates);
  }

  @Test
  public void testSubscriptionServerCreateEntityUpdate() throws IOException, InterruptedException {

    GameController serverGameController = serverInjector.getInstance(GameController.class);

    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkFactory serverChunkFactory = serverInjector.getInstance(ChunkFactory.class);

    ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);

    ChunkSubscriptionService serverChunkSubscriptionService =
        serverInjector.getInstance(ChunkSubscriptionService.class);

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        new HashSet<>(clientChunkClockMap.getChunkRangeList()),
        new HashSet<>(
            serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                clientNetworkHandle.uuid)));

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);

    Entity serverEntity = serverGameController.createEntity(clientEntityFactory.createEntity());

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
  public void testClientDisconnectRemoveEntity() throws InterruptedException {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    this.clientNetworkHandle.close();

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid) == null;
  }

  @Test
  public void testClientReplaceBlock() throws InterruptedException {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);
    EventService clientEventService = clientInjector.getInstance(EventService.class);

    serverGameController.createEntity(serverBlockFactory.createDirt());
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == DirtBlock.class;
    TimeUnit.SECONDS.sleep(1);
    Block clientBlock = serverGameStore.getBlock(new Coordinates(0, 0));
    clientEventService.fireEvent(
        clientEventFactory.createReplaceBlockOutgoingEvent(
            clientBlock.uuid,
            serverBlockFactory.createSky(),
            new ChunkRange(clientBlock.coordinates)));
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == SkyBlock.class;
  }

  @Test
  public void testServerReplaceBlock() throws InterruptedException {
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);
    EventService serverEventService = serverInjector.getInstance(EventService.class);
    EventService clientEventService = clientInjector.getInstance(EventService.class);

    Entity serverEntity = serverGameController.createEntity(serverBlockFactory.createDirt());
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == DirtBlock.class;
    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getBlock(new Coordinates(0, 0)).getClass() == DirtBlock.class;

    serverEventService.fireEvent(
        clientEventFactory.createReplaceBlockOutgoingEvent(
            serverEntity.uuid,
            serverBlockFactory.createSky(),
            new ChunkRange(serverEntity.coordinates)));
    TimeUnit.SECONDS.sleep(1);

    clientEventService.firePostUpdateEvents();
    TimeUnit.SECONDS.sleep(1);

    assert clientGameStore.getBlock(new Coordinates(0, 0)).getClass() == SkyBlock.class;
    System.out.println("done");
  }

  @Test
  public void testClientCreateLadder() throws IOException, InterruptedException {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createLadder(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    assert clientGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testServerCreateLadder() throws IOException, InterruptedException {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    Entity serverEntity = serverGameController.createLadder(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    assert clientGameStore.getEntity(serverEntity.uuid).uuid.equals(serverEntity.uuid);
    assert serverGameStore.getEntity(serverEntity.uuid).uuid.equals(serverEntity.uuid);
    assert clientGameStore
        .getEntity(serverEntity.uuid)
        .coordinates
        .equals(serverEntity.coordinates);
  }

  //  @Test
  //  public void testServerGetEntity() throws IOException, InterruptedException {
  //    clientNetworkHandle.
  //  }

  @Test
  public void testClientCreateAIEntity() throws IOException, InterruptedException {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    clientGameController.createAI();

    TimeUnit.SECONDS.sleep(1);

  }
}
