package infra.networking;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import configuration.ServerConfig;
import infra.app.GameController;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.chunk.ChunkSubscriptionService;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.EventFactory;
import infra.networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class testSingleClient {

  Injector clientInjector;

  Injector serverInjector;

  ClientNetworkHandle clientNetworkHandle;

  ServerNetworkHandle serverNetworkHandle;

  @Before
  public void setup() {
    clientInjector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new ServerConfig());

    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
  }

  @After
  public void cleanup() {
    clientNetworkHandle.close();
    serverNetworkHandle.close();
  }

  @Test
  public void testAuthentication() throws IOException, InterruptedException {

    ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);

    serverNetworkHandle.start();
    assert connectionStore.size() == 0;

    clientNetworkHandle.connect();
    TimeUnit.SECONDS.sleep(1);

    assert connectionStore.size() == 1;
  }

  @Test
  public void testCreateEntity() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createEntity(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testCreateUpdateEntity() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

    GameController clientGameController = clientInjector.getInstance(GameController.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
    clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = clientGameController.createEntity(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    clientGameController.moveEntity(clientEntity, new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);

    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testCreateBlock() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

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
  public void testCreateUpdateBlock() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

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

    clientGameController.moveEntity(clientEntity, new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);
    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);

    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testSubscription() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

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
        chunkRangeList,
        serverChunkSubscriptionService.getUserChunkRangeSubscriptions(clientNetworkHandle.uuid));
  }

  @Test
  public void testSubscriptionCreateEntity() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

    GameController serverGameController = serverInjector.getInstance(GameController.class);

    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkFactory serverChunkFactory = serverInjector.getInstance(ChunkFactory.class);

    ChunkSubscriptionService serverChunkSubscriptionService =
            serverInjector.getInstance(ChunkSubscriptionService.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    serverGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(-1, 0))));

    clientGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(-1, 0))));

    clientNetworkHandle.send(
            clientEventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
            chunkRangeList,
            serverChunkSubscriptionService.getUserChunkRangeSubscriptions(clientNetworkHandle.uuid));

    Entity serverEntity = serverGameController.createEntity(new Coordinates(-1, 0));

    TimeUnit.SECONDS.sleep(1);

    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(clientGameStore.getEntity(serverEntity.uuid).coordinates);
  }

  @Test
  public void testSubscriptionCreateEntityUpdate() throws IOException, InterruptedException {
    serverNetworkHandle.start();
    clientNetworkHandle.connect();

    GameController serverGameController = serverInjector.getInstance(GameController.class);

    EventFactory clientEventFactory = clientInjector.getInstance(EventFactory.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
    ChunkFactory serverChunkFactory = serverInjector.getInstance(ChunkFactory.class);

    ChunkSubscriptionService serverChunkSubscriptionService =
            serverInjector.getInstance(ChunkSubscriptionService.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(-1, 0)));
    serverGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(-1, 0))));

    clientGameStore.addChunk(serverChunkFactory.create(new ChunkRange(new Coordinates(-1, 0))));

    clientNetworkHandle.send(
            clientEventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    Assert.assertEquals(
            chunkRangeList,
            serverChunkSubscriptionService.getUserChunkRangeSubscriptions(clientNetworkHandle.uuid));

    Entity serverEntity = serverGameController.createEntity(new Coordinates(-1, 0));

    TimeUnit.SECONDS.sleep(1);

    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(clientGameStore.getEntity(serverEntity.uuid).coordinates);

    serverGameController.moveEntity(serverEntity,new Coordinates(-1, 1));

    TimeUnit.SECONDS.sleep(1);

    assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
    assert serverEntity.coordinates.equals(clientGameStore.getEntity(serverEntity.uuid).coordinates);
  }
}
