package infra.networking;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import configuration.ServerConfig;
import infra.app.GameController;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.EventFactory;
import infra.networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class testDoubleClient {

  Injector client_a_Injector;
  Injector client_b_Injector;

  ClientNetworkHandle client_a_NetworkHandle;
  ClientNetworkHandle client_b_NetworkHandle;

  Injector serverInjector;

  ServerNetworkHandle serverNetworkHandle;

  @Before
  public void setup() throws IOException {
    client_a_Injector = Guice.createInjector(new ClientConfig());
    client_b_Injector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new ServerConfig());

    client_a_NetworkHandle = client_a_Injector.getInstance(ClientNetworkHandle.class);
    client_b_NetworkHandle = client_b_Injector.getInstance(ClientNetworkHandle.class);

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
    serverNetworkHandle.start();

    client_a_NetworkHandle.connect();
    client_b_NetworkHandle.connect();
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
  public void testDoubleClientCreateEntity() throws InterruptedException {

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

    EventFactory client_b_EventFactory = client_b_Injector.getInstance(EventFactory.class);

    client_b_NetworkHandle.send(
        client_b_EventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    //    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = client_a_GameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert client_b_GameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testDoubleClientCreateUpdateEntity() throws InterruptedException {

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

    EventFactory client_b_EventFactory = client_b_Injector.getInstance(EventFactory.class);

    client_b_NetworkHandle.send(
        client_b_EventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    Entity clientEntity = client_a_GameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert client_b_GameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    client_a_GameController.moveEntity(clientEntity.uuid, new Coordinates(0, 1));

    TimeUnit.SECONDS.sleep(1);

    assert clientEntity.coordinates.equals(new Coordinates(0, 1));

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert client_b_GameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }

  @Test
  public void testDoubleClientCreateThenDisconnectRemoveOther() throws InterruptedException {
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

    EventFactory client_b_EventFactory = client_b_Injector.getInstance(EventFactory.class);

    client_b_NetworkHandle.send(
        client_b_EventFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    //    TimeUnit.SECONDS.sleep(1);

    Entity clientEntity = client_a_GameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    assert client_b_GameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert client_b_GameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    client_a_NetworkHandle.close();

    TimeUnit.SECONDS.sleep(1);
    serverEventService.firePostUpdateEvents();
    client_b_EventService.firePostUpdateEvents();

    assert serverGameStore.getEntity(clientEntity.uuid) == null;
    assert client_b_GameStore.getEntity(clientEntity.uuid) == null;
  }
}
