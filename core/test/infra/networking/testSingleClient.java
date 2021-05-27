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
import infra.entity.Entity;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.server.ServerNetworkHandle;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
}
