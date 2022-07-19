package core.networking.connected;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.game.Game;
import core.app.game.GameController;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.chunk.world.exceptions.BodyNotFound;
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
import core.entity.attributes.inventory.item.OrbInventoryItem;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.entity.block.DirtBlock;
import core.entity.block.SkyBlock;
import core.entity.misc.Ladder;
import core.entity.misc.Turret;
import core.generation.ChunkBuilderFactory;
import core.mock.GdxTestRunner;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.EventTypeFactory;
import core.networking.server.ServerNetworkHandle;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class testDoubleClient {

  Injector client_a_Injector;
  Injector client_b_Injector;

  ClientNetworkHandle client_a_NetworkHandle;
  ClientNetworkHandle client_b_NetworkHandle;

  Game serverGame;
  Game client_a_Game;
  Game client_b_Game;

  GameController serverGameController;
  GameController client_a_GameController;
  GameController client_b_GameController;

  Injector serverInjector;

  ServerNetworkHandle serverNetworkHandle;

  @Before
  public void setup()
      throws IOException, SerializationDataMissing, InterruptedException, WrongVersion,
          BodyNotFound {
    client_a_Injector = Guice.createInjector(new ClientConfig());
    client_b_Injector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new BaseServerConfig());

    client_a_NetworkHandle = client_a_Injector.getInstance(ClientNetworkHandle.class);
    client_b_NetworkHandle = client_b_Injector.getInstance(ClientNetworkHandle.class);

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

    serverGame = serverInjector.getInstance(Game.class);
    client_a_Game = client_a_Injector.getInstance(Game.class);
    client_b_Game = client_b_Injector.getInstance(Game.class);

    serverGameController = serverInjector.getInstance(GameController.class);
    client_a_GameController = client_a_Injector.getInstance(GameController.class);
    client_b_GameController = client_b_Injector.getInstance(GameController.class);

    serverGame.start();
    client_a_Game.start();
    client_b_Game.start();
  }

  @After
  public void cleanup() {
    try {
      client_a_NetworkHandle.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      client_b_NetworkHandle.close();
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
  public void testDoubleClientCreateEntity()
      throws InterruptedException, EntityNotFound, ChunkNotFound {

    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);
    client_a_GameStore.addChunk(
        client_a_ChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = client_a_Injector.getInstance(EntityFactory.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    for (ChunkRange chunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_a_ChunkFactory.create(chunkRange));
    }

    EventTypeFactory client_b_EventTypeFactory =
        client_b_Injector.getInstance(EventTypeFactory.class);

    client_b_NetworkHandle.send(
        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity =
        client_a_GameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(3);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testDoubleClientCreateUpdateEntity()
      throws InterruptedException, EntityNotFound, ChunkNotFound {

    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);
    client_a_GameStore.addChunk(
        client_a_ChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = client_a_Injector.getInstance(EntityFactory.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    for (ChunkRange chunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_a_ChunkFactory.create(chunkRange));
    }

    EventTypeFactory client_b_EventTypeFactory =
        client_b_Injector.getInstance(EventTypeFactory.class);

    client_b_NetworkHandle.send(
        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity =
        client_a_GameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    client_a_GameController.moveEntity(clientEntity.getUuid(), new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);

    assert clientEntity.coordinates.equals(new Coordinates(0, 1));

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testDoubleClientCreateThenDisconnectRemoveOther()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    EventService serverEventService = serverInjector.getInstance(EventService.class);
    EventService client_b_EventService = client_b_Injector.getInstance(EventService.class);

    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);
    client_a_GameStore.addChunk(
        client_a_ChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    EntityFactory clientEntityFactory = client_a_Injector.getInstance(EntityFactory.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    for (ChunkRange chunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_a_ChunkFactory.create(chunkRange));
    }

    EventTypeFactory client_b_EventTypeFactory =
        client_b_Injector.getInstance(EventTypeFactory.class);

    client_b_NetworkHandle.send(
        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity =
        client_a_GameController.addEntity(clientEntityFactory.createEntity(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .coordinates
        .equals(clientEntity.coordinates);

    client_a_NetworkHandle.close();

    TimeUnit.SECONDS.sleep(1);
    serverEventService.firePostUpdateEvents();
    client_b_EventService.firePostUpdateEvents();

    assert !serverGameStore.doesEntityExist(clientEntity.getUuid());
    assert !client_b_GameStore.doesEntityExist(clientEntity.getUuid());
  }

  @Test
  public void testDoubleClientCreateLadder() throws Exception {
    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = serverInjector.getInstance(ChunkBuilderFactory.class);

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    serverGameStore.addChunk(chunkBuilderFactory.create(chunkRange).call());
    client_a_GameStore.addChunk(client_a_NetworkHandle.requestChunkBlocking(chunkRange));
    client_b_GameStore.addChunk(client_b_NetworkHandle.requestChunkBlocking(chunkRange));

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    for (ChunkRange subChunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_a_ChunkFactory.create(subChunkRange));
    }

    EventTypeFactory client_b_EventTypeFactory =
        client_b_Injector.getInstance(EventTypeFactory.class);
    client_b_NetworkHandle.send(
        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);
    Entity clientLadder = client_a_GameController.createLadder(coordinates);
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientLadder.getUuid()).equals(clientLadder);
    assert client_a_GameStore.getEntity(clientLadder.getUuid()).equals(clientLadder);
    assert client_b_GameStore.getEntity(clientLadder.getUuid()).equals(clientLadder);
  }

  @Test
  public void testDoubleClientCreateTurret() throws Exception {
    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = serverInjector.getInstance(ChunkBuilderFactory.class);

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    serverGameStore.addChunk(chunkBuilderFactory.create(chunkRange).call());
    client_a_GameStore.addChunk(client_a_NetworkHandle.requestChunkBlocking(chunkRange));
    client_b_GameStore.addChunk(client_b_NetworkHandle.requestChunkBlocking(chunkRange));

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    for (ChunkRange subChunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_a_ChunkFactory.create(subChunkRange));
    }

    EventTypeFactory client_b_EventTypeFactory =
        client_b_Injector.getInstance(EventTypeFactory.class);
    client_b_NetworkHandle.send(
        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    Entity serverEntity = serverGameController.createEntity(new Coordinates(1, 1));
    serverEntity.getBag().updateItem(new OrbInventoryItem(1));

    TimeUnit.SECONDS.sleep(1);
    assert null != client_a_GameStore.getEntity(serverEntity.getUuid());
    client_a_GameController.triggerCreateTurret(serverEntity, coordinates);
    TimeUnit.SECONDS.sleep(1);

    Turret clientTurret = client_a_GameStore.getTurret(coordinates);

    assert clientTurret != null;

    assert serverGameStore.getEntity(clientTurret.getUuid()).equals(clientTurret);
    assert client_a_GameStore.getEntity(clientTurret.getUuid()).equals(clientTurret);
    assert client_b_GameStore.getEntity(clientTurret.getUuid()).equals(clientTurret);
  }

  @Test
  public void testDoubleClientCreateProjectile() throws Exception {
    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = serverInjector.getInstance(ChunkBuilderFactory.class);

    Coordinates coordinates = new Coordinates(2, 2);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    serverGameStore.addChunk(chunkBuilderFactory.create(chunkRange).call());
    client_a_GameStore.addChunk(client_a_NetworkHandle.requestChunkBlocking(chunkRange));
    client_b_GameStore.addChunk(client_b_NetworkHandle.requestChunkBlocking(chunkRange));

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    for (ChunkRange subChunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_a_ChunkFactory.create(subChunkRange));
    }

    EventTypeFactory client_b_EventTypeFactory =
        client_b_Injector.getInstance(EventTypeFactory.class);
    client_b_NetworkHandle.send(
        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);
    Entity projectile = serverGameController.createProjectile(coordinates, new Vector2(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(projectile.getUuid()).equals(projectile);
    assert client_a_GameStore.getEntity(projectile.getUuid()).equals(projectile);
    assert client_b_GameStore.getEntity(projectile.getUuid()).equals(projectile);
  }

  @Test
  public void testRemoveClientToServer()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);
    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);

    Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(myEntity.getUuid())
        .equals(client_a_GameStore.getEntity(myEntity.getUuid()));
    assert serverGameStore
        .getEntity(myEntity.getUuid())
        .equals(client_b_GameStore.getEntity(myEntity.getUuid()));
    client_a_GameController.removeEntity(myEntity.getUuid());
    TimeUnit.SECONDS.sleep(1);

    assert !serverGameStore.doesEntityExist(myEntity.getUuid());
    assert !client_a_GameStore.doesEntityExist(myEntity.getUuid());
    assert !client_b_GameStore.doesEntityExist(myEntity.getUuid());
  }

  @Test
  public void testRemoveServerToClient()
      throws InterruptedException, EntityNotFound, ChunkNotFound {
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameController serverGameController = serverInjector.getInstance(GameController.class);

    Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(myEntity.getUuid())
        .equals(client_a_GameStore.getEntity(myEntity.getUuid()));
    assert serverGameStore
        .getEntity(myEntity.getUuid())
        .equals(client_b_GameStore.getEntity(myEntity.getUuid()));
    serverGameController.removeEntity(myEntity.getUuid());
    TimeUnit.SECONDS.sleep(1);

    assert !serverGameStore.doesEntityExist(myEntity.getUuid());
    assert !client_a_GameStore.doesEntityExist(myEntity.getUuid());
    assert !client_b_GameStore.doesEntityExist(myEntity.getUuid());
  }

  @Test
  public void testClientReplaceLadder() throws Exception {
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);

    BlockFactory client_a_BlockFactory = client_a_Injector.getInstance(BlockFactory.class);

    Coordinates coordinatesToTest = new Coordinates(0, 1);
    ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

    // make sure everyone has the same chunks.
    client_a_NetworkHandle.requestChunkBlocking(chunkRangeToTest);
    client_b_NetworkHandle.requestChunkBlocking(chunkRangeToTest);
    Assert.assertEquals(
        serverGameStore.getChunk(chunkRangeToTest).getEntityList().size(),
        GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE);
    assert serverGameStore
        .getChunk(chunkRangeToTest)
        .equals(client_a_GameStore.getChunk(chunkRangeToTest));
    assert serverGameStore
        .getChunk(chunkRangeToTest)
        .equals(client_b_GameStore.getChunk(chunkRangeToTest));
    assert client_a_GameStore
        .getChunk(chunkRangeToTest)
        .equals(client_b_GameStore.getChunk(chunkRangeToTest));
    assert serverGameStore.getBlock(coordinatesToTest).getClass() == SkyBlock.class;

    Block blockToRemove = client_a_GameStore.getBlock(coordinatesToTest);
    Block blockAsReplacement = client_a_BlockFactory.createDirt(coordinatesToTest);

    Entity ladder = client_a_GameController.createLadder(coordinatesToTest);
    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        serverGameStore.getChunk(chunkRangeToTest).getEntityList().size(),
        (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE) + 1);
    // check chunks equal
    assert serverGameStore
        .getChunk(chunkRangeToTest)
        .equals(client_a_GameStore.getChunk(chunkRangeToTest));
    assert serverGameStore
        .getChunk(chunkRangeToTest)
        .equals(client_b_GameStore.getChunk(chunkRangeToTest));
    assert client_a_GameStore
        .getChunk(chunkRangeToTest)
        .equals(client_b_GameStore.getChunk(chunkRangeToTest));
    // check block is equal
    Assert.assertEquals(client_a_GameStore.getBlock(coordinatesToTest).getClass(), SkyBlock.class);
    Assert.assertEquals(serverGameStore.getBlock(coordinatesToTest).getClass(), SkyBlock.class);
    Assert.assertEquals(client_b_GameStore.getBlock(coordinatesToTest).getClass(), SkyBlock.class);
    // check ladder exists
    assert serverGameStore.getEntity(ladder.getUuid()).getClass() == Ladder.class;
    assert client_a_GameStore
        .getEntity(ladder.getUuid())
        .equals(serverGameStore.getEntity(ladder.getUuid()));
    assert client_b_GameStore
        .getEntity(ladder.getUuid())
        .equals(serverGameStore.getEntity(ladder.getUuid()));

    client_a_GameController.replaceBlock(blockToRemove, blockAsReplacement);
    TimeUnit.SECONDS.sleep(1);

    // check block is equal
    Assert.assertEquals(serverGameStore.getBlock(coordinatesToTest).getClass(), DirtBlock.class);
    Assert.assertEquals(client_a_GameStore.getBlock(coordinatesToTest).getClass(), DirtBlock.class);
    Assert.assertEquals(client_b_GameStore.getBlock(coordinatesToTest).getClass(), DirtBlock.class);

    // check sizes
    Assert.assertEquals(
        serverGameStore.getChunk(chunkRangeToTest).getEntityList().size(),
        (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE));
    Assert.assertEquals(
        client_a_GameStore.getChunk(chunkRangeToTest).getEntityList().size(),
        (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE));
    Assert.assertEquals(
        client_b_GameStore.getChunk(chunkRangeToTest).getEntityList().size(),
        (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE));

    // check chunks equal
    assert serverGameStore
        .getChunk(chunkRangeToTest)
        .equals(client_a_GameStore.getChunk(chunkRangeToTest));
    assert serverGameStore
        .getChunk(chunkRangeToTest)
        .equals(client_b_GameStore.getChunk(chunkRangeToTest));
    assert client_a_GameStore
        .getChunk(chunkRangeToTest)
        .equals(client_b_GameStore.getChunk(chunkRangeToTest));

    //         check ladder exists
    assert !serverGameStore.doesEntityExist(ladder.getUuid());
    assert !client_a_GameStore.doesEntityExist(ladder.getUuid());
    assert !client_b_GameStore.doesEntityExist(ladder.getUuid());
  }
}
