package core.networking.connected;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.game.GameController;
import core.chunk.ChunkFactory;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.GameStore;
import core.common.events.EventConsumer;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.BaseServerConfig;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.mock.GdxTestRunner;
import core.networking.client.ClientNetworkHandle;
import core.networking.server.ServerNetworkHandle;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
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

    client_a_Injector.getInstance(EventConsumer.class).init();
    client_b_Injector.getInstance(EventConsumer.class).init();
    serverInjector.getInstance(EventConsumer.class).init();

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
  public void testDoubleClientCreateEntity()
      throws InterruptedException,
          EntityNotFound,
          SerializationDataMissing,
          WrongVersion,
          ChunkNotFound {
    client_a_NetworkHandle.connect();

    GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);
    GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
    GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
    GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
    ChunkFactory client_a_ChunkFactory = client_a_Injector.getInstance(ChunkFactory.class);
    client_a_GameStore.addChunk(
        client_a_ChunkFactory.create(
            CommonFactory.createChunkRange(CommonFactory.createCoordinates(2, 3))));

    serverGameStore.addChunk(
        client_a_ChunkFactory.create(
            CommonFactory.createChunkRange(CommonFactory.createCoordinates(2, 3))));

    EntityFactory clientEntityFactory = client_a_Injector.getInstance(EntityFactory.class);

    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)));

    Entity clientEntity =
        client_a_GameController.addEntity(
            clientEntityFactory.createEntity(CommonFactory.createCoordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);

    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert serverGameStore
        .getEntity(clientEntity.getUuid())
        .getCoordinatesWrapper()
        .getCoordinates()
        .equals(clientEntity.getCoordinatesWrapper().getCoordinates());

    client_b_NetworkHandle.connect();

    for (ChunkRange chunkRange : chunkRangeList) {
      client_b_GameStore.addChunk(client_b_NetworkHandle.requestChunkBlocking(chunkRange));
    }

    //    client_b_NetworkHandle.send(
    //
    // client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);

    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .getUuid()
        .equals(clientEntity.getUuid());
    assert client_b_GameStore
        .getEntity(clientEntity.getUuid())
        .getCoordinatesWrapper()
        .getCoordinates()
        .equals(clientEntity.getCoordinatesWrapper().getCoordinates());
  }
}
