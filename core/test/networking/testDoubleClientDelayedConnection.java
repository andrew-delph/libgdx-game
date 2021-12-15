package networking;

import app.GameController;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import configuration.ClientConfig;
import configuration.BaseServerConfig;
import entity.Entity;
import entity.EntityFactory;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class testDoubleClientDelayedConnection {

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
    serverInjector = Guice.createInjector(new BaseServerConfig());

    client_a_NetworkHandle = client_a_Injector.getInstance(ClientNetworkHandle.class);
    client_b_NetworkHandle = client_b_Injector.getInstance(ClientNetworkHandle.class);

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
    serverNetworkHandle.start();

    //        client_a_NetworkHandle.connect();
    //        client_b_NetworkHandle.connect();
  }

  @After
  public void cleanup() {
    client_a_NetworkHandle.close();
    client_b_NetworkHandle.close();
    serverNetworkHandle.close();
  }

  @Test
  public void testDoubleClientCreateEntity() throws InterruptedException {

    client_a_NetworkHandle.connect();

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


    EventTypeFactory client_b_EventTypeFactory = client_b_Injector.getInstance(EventTypeFactory.class);

    Entity clientEntity = client_a_GameController.createEntity(clientEntityFactory.createEntity());

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert serverGameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);

    client_b_NetworkHandle.connect();

    for (ChunkRange chunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_b_NetworkHandle.getChunk(chunkRange));
    }

//    client_b_NetworkHandle.send(
//        client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    assert client_b_GameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
    assert client_b_GameStore
        .getEntity(clientEntity.uuid)
        .coordinates
        .equals(clientEntity.coordinates);
  }
}
