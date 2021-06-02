package infra.networking;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import configuration.ServerConfig;
import infra.app.Game;
import infra.app.GameController;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.chunk.ChunkSubscriptionService;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.EventFactory;
import infra.networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    serverInjector = Guice.createInjector(new ServerConfig());

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

    Entity clientEntity = clientGameController.createBlock(new Coordinates(0, 0));

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

    Entity clientEntity = clientGameController.createBlock(new Coordinates(0, 0));

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

    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);

    ChunkSubscriptionService serverChunkSubscriptionService =
        serverInjector.getInstance(ChunkSubscriptionService.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));

    clientNetworkHandle.send(
        clientEventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        new HashSet<>(chunkRangeList),
        new HashSet<>(
            serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                clientNetworkHandle.uuid)));
  }

  @Test
  public void testSubscriptionServerCreateEntity() throws IOException, InterruptedException {

    GameController serverGameController = serverInjector.getInstance(GameController.class);

    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkFactory serverChunkFactory = serverInjector.getInstance(ChunkFactory.class);

    ChunkSubscriptionService serverChunkSubscriptionService =
        serverInjector.getInstance(ChunkSubscriptionService.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    serverGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    clientGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    clientNetworkHandle.send(
        clientEventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        chunkRangeList,
        serverChunkSubscriptionService.getUserChunkRangeSubscriptions(clientNetworkHandle.uuid));

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

    ChunkSubscriptionService serverChunkSubscriptionService =
        serverInjector.getInstance(ChunkSubscriptionService.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 0)));
    serverGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    clientGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    clientNetworkHandle.send(
        clientEventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
        chunkRangeList,
        serverChunkSubscriptionService.getUserChunkRangeSubscriptions(clientNetworkHandle.uuid));

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
}
