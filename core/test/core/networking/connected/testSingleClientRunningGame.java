package core.networking.connected;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;
import core.app.game.Game;
import core.app.game.GameController;
import core.app.user.User;
import core.chunk.ActiveChunkManager;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.ChunkClockMap;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.BaseServerConfig;
import core.configuration.ClientConfig;
import core.entity.ActiveEntityManager;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.inventory.Equipped;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.entity.block.DirtBlock;
import core.entity.block.SkyBlock;
import core.generation.ChunkBuilderFactory;
import core.generation.ChunkGenerationService;
import core.mock.GdxTestRunner;
import core.networking.ConnectionStore;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.EventTypeFactory;
import core.networking.events.consumer.client.incoming.HandshakeIncomingConsumerClient;
import core.networking.server.ServerNetworkHandle;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

class MockHandshakeIncomingConsumerClient extends HandshakeIncomingConsumerClient {

  private Boolean called = false;

  public Boolean getCalled() {
    return called;
  }

  public void setCalled(Boolean called) {
    this.called = called;
  }

  public void accept(EventType eventType) {
    System.out.println("OVERRIDE FOR HandshakeIncomingConsumerClient");
    setCalled(true);
  }
}

@RunWith(GdxTestRunner.class)
public class testSingleClientRunningGame {

  Injector clientInjector;
  Injector serverInjector;

  ClientNetworkHandle clientNetworkHandle;
  ServerNetworkHandle serverNetworkHandle;

  GameStore serverGameStore;
  GameStore clientGameStore;

  Game clientGame, serverGame;

  MockHandshakeIncomingConsumerClient mockHandshakeIncomingConsumerClient;

  @Before
  public void setup()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
          BodyNotFound {
    // block implementation of handshake
    clientInjector =
        Guice.createInjector(
            Modules.override(new ClientConfig())
                .with(
                    new AbstractModule() {
                      @Override
                      protected void configure() {
                        bind(HandshakeIncomingConsumerClient.class)
                            .toProvider(Providers.of(new MockHandshakeIncomingConsumerClient()));
                      }
                    }));
    serverInjector = Guice.createInjector(new BaseServerConfig());

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);

    mockHandshakeIncomingConsumerClient =
        (MockHandshakeIncomingConsumerClient)
            clientInjector.getInstance(HandshakeIncomingConsumerClient.class);

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
    assert !mockHandshakeIncomingConsumerClient.getCalled();
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
  public void testClientCreateEntity() throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity =
        clientGameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateUpdateEntity()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    Entity clientEntity =
        clientGameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
    clientGameController.moveEntity(clientEntity.getUuid(), new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateUpdateEntityEquipped()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameController serverGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    Entity clientEntity =
        clientGameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    Equipped equipped = new Equipped(2);

    assert !serverGameStore
        .getEntity(clientEntity.getUuid())
        .getBag()
        .getEquipped()
        .equals(equipped);
    clientGameController.updateEntityAttribute(clientEntity.getUuid(), equipped);

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getBag()
        .getEquipped()
        .equals(equipped);

    equipped = new Equipped(4);

    serverGameController.updateEntityAttribute(clientEntity.getUuid(), equipped);

    TimeUnit.SECONDS.sleep(2);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    assert clientEntity.getBag().getEquipped().equals(equipped);
  }

  @Test
  public void testClientCreateBlock() throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createDirtBlock(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateOrb() throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createOrb(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testClientCreateUpdateBlock()
      throws IOException, InterruptedException, EntityNotFound, ChunkNotFound {

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createDirtBlock(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    clientGameController.moveEntity(clientEntity.getUuid(), new Coordinates(0, 1));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
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
      throws IOException, InterruptedException, EntityNotFound, ChunkNotFound {
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
    Entity serverEntity =
        serverGameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);
    assert serverEntity
        .getUuid()
        .equals(clientGameStore.getEntity(serverEntity.getUuid()).getUuid());
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.getUuid()).coordinates);
  }

  @Test
  public void testSubscriptionServerCreateEntityUpdate()
      throws IOException, InterruptedException, EntityNotFound, ChunkNotFound {

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
    Entity serverEntity =
        serverGameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);
    assert serverEntity
        .getUuid()
        .equals(clientGameStore.getEntity(serverEntity.getUuid()).getUuid());
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.getUuid()).coordinates);
    serverGameController.moveEntity(serverEntity.getUuid(), new Coordinates(0, 1));
    TimeUnit.SECONDS.sleep(1);
    assert serverEntity
        .getUuid()
        .equals(clientGameStore.getEntity(serverEntity.getUuid()).getUuid());
    assert serverEntity.coordinates.equals(
        clientGameStore.getEntity(serverEntity.getUuid()).coordinates);
  }

  @Test
  public void testClientDisconnectRemoveEntity()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
    TimeUnit.SECONDS.sleep(1);
    Entity clientEntity =
        clientGameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
    this.clientNetworkHandle.close();
    TimeUnit.SECONDS.sleep(1);
    assert !serverGameStore.doesEntityExist(clientEntity.getUuid());
  }

  @Test
  public void testClientReplaceBlock() throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
    EventService clientEventService = clientInjector.getInstance(EventService.class);
    serverGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
    serverGameController.addEntity(serverBlockFactory.createDirt(new Coordinates(0, 0)));
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == DirtBlock.class;
    TimeUnit.SECONDS.sleep(1);
    Block clientBlock = serverGameStore.getBlock(new Coordinates(0, 0));
    clientEventService.fireEvent(
        EventTypeFactory.createReplaceBlockOutgoingEvent(
            clientBlock.getUuid(),
            serverBlockFactory.createSky(clientBlock.coordinates),
            new ChunkRange(clientBlock.coordinates)));
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == SkyBlock.class;
  }

  @Test
  public void testServerReplaceBlock() throws InterruptedException, EntityNotFound, ChunkNotFound {
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
    Block serverBlockReplacement = serverBlockFactory.createSky(new Coordinates(0, 0));
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
    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    clientNetworkHandle.requestChunkBlocking(chunkRange);
    TimeUnit.SECONDS.sleep(1);
    Entity clientEntity = clientGameController.createLadder(coordinates);
    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .equals(clientGameStore.getEntity(clientEntity.getUuid()));
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
        .getEntity(clientEntity.getUuid())
        .equals(clientGameStore.getEntity(clientEntity.getUuid()));
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
  public void testRemoveClientToServer()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameController clientGameController = clientInjector.getInstance(GameController.class);

    Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(myEntity.getUuid())
        .equals(clientGameStore.getEntity(myEntity.getUuid()));
    clientGameController.removeEntity(myEntity.getUuid());
    TimeUnit.SECONDS.sleep(1);

    assert !serverGameStore.doesEntityExist(myEntity.getUuid());
    assert !clientGameStore.doesEntityExist(myEntity.getUuid());
  }

  @Test
  public void testRemoveServerToClient()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
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
        .getEntity(myEntity.getUuid())
        .equals(clientGameStore.getEntity(myEntity.getUuid()));
    serverGameController.removeEntity(myEntity.getUuid());
    TimeUnit.SECONDS.sleep(1);

    assert !serverGameStore.doesEntityExist(myEntity.getUuid());
    assert !clientGameStore.doesEntityExist(myEntity.getUuid());
  }

  @Test
  public void testEntityChunkSwapRemovalEntity()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
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

    assert serverGameStore.doesEntityExist(myEntity.getUuid());
    assert clientGameStore.doesEntityExist(myEntity.getUuid());

    serverGameController.moveEntity(myEntity.getUuid(), coordinatesToTest);

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.doesEntityExist(myEntity.getUuid());
    assert (new ChunkRange(myEntity.coordinates)).equals(chunkRangeToTest);
    assert !clientGameStore.doesEntityExist(myEntity.getUuid());
  }

  @Test
  public void testEntityChunkSwapCreateEntity()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);

    GameController serverGameController = serverInjector.getInstance(GameController.class);
    ChunkFactory serverChunkFactory = serverInjector.getInstance(ChunkFactory.class);
    ActiveChunkManager serverActiveChunkManager =
        serverInjector.getInstance(ActiveChunkManager.class);
    ConnectionStore serverConnectionStore = serverInjector.getInstance(ConnectionStore.class);

    User clientUser = clientInjector.getInstance(User.class);
    assert serverConnectionStore.getConnection(clientUser.getUserID()) != null;

    Coordinates coordinatesInit = new Coordinates(-1000, 1000);

    serverGameStore.addChunk(serverChunkFactory.create(new ChunkRange(coordinatesInit)));

    Coordinates coordinatesToTest = new Coordinates(0, 0);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    serverActiveChunkManager.addUserChunkSubscriptions(
        clientUser.getUserID(), new ChunkRange(coordinatesToTest));
    Entity myEntity = serverGameController.createEntity(coordinatesInit);

    TimeUnit.SECONDS.sleep(1);

    // assert client doesn't have chunk
    // assert client doesnt have entity
    assert !clientGameStore.doesChunkExist(new ChunkRange(coordinatesInit));
    assert !clientGameStore.doesEntityExist(myEntity.getUuid());

    serverGameController.moveEntity(myEntity.getUuid(), coordinatesToTest);

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.doesEntityExist(myEntity.getUuid());
    assert (new ChunkRange(myEntity.coordinates)).equals(chunkRangeToTest);
    assert clientGameStore.doesEntityExist(myEntity.getUuid());
  }
}
