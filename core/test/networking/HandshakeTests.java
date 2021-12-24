package networking;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HandshakeTests {

    Injector clientInjector;
    Injector serverInjector;
    ClientNetworkHandle clientNetworkHandle;
    ServerNetworkHandle serverNetworkHandle;

    GameStore serverGameStore;
    GameStore clientGameStore;

    ChunkFactory chunkFactory;
    EntityFactory entityFactory;

    @Before
    public void setup() throws IOException, InterruptedException {
        clientInjector = Guice.createInjector(new ClientConfig());
        serverInjector = Guice.createInjector(new BaseServerConfig());

        clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);
        serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

        chunkFactory = serverInjector.getInstance(ChunkFactory.class);
        entityFactory = serverInjector.getInstance(EntityFactory.class);

        serverGameStore = serverInjector.getInstance(GameStore.class);
        clientGameStore = clientInjector.getInstance(GameStore.class);

        serverNetworkHandle.start();
        clientNetworkHandle.connect();

        TimeUnit.SECONDS.sleep(1);
    }

    @After
    public void cleanup() {
        try {
            clientNetworkHandle.close();
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
    public void testServerInitServerExtra() throws InterruptedException {
        //THIS TEST ONLY VERIFIES THE CLIENT REMOVED THE EXTRA
        Coordinates coordinatesToTest = new Coordinates(0, 0);
        ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

        serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
        clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

        Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
        Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
        assert serverChunk.equals(clientChunk);

        Entity e1 = entityFactory.createEntity();//server only
        Entity e2 = entityFactory.createEntity();//both
        Entity e3 = entityFactory.createEntity();//client only

        e1.coordinates = coordinatesToTest;
        e2.coordinates = coordinatesToTest;
        e3.coordinates = coordinatesToTest;

//        serverGameStore.addEntity(e1);
        serverGameStore.addEntity(e2);
        clientGameStore.addEntity(e2);
        clientGameStore.addEntity(e3);

        serverNetworkHandle.initHandshake(clientNetworkHandle.uuid, chunkRangeToTest);

        TimeUnit.SECONDS.sleep(1);
        assert serverChunk.equals(clientChunk);
    }

    @Test
    public void testServerInitServer() throws InterruptedException {
        Coordinates coordinatesToTest = new Coordinates(0, 0);
        ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

        serverGameStore.addChunk(chunkFactory.create(chunkRangeToTest));
        clientGameStore.addChunk(chunkFactory.create(chunkRangeToTest));

        Chunk serverChunk = serverGameStore.getChunk(chunkRangeToTest);
        Chunk clientChunk = clientGameStore.getChunk(chunkRangeToTest);
        assert serverChunk.equals(clientChunk);

        Entity e1 = entityFactory.createEntity();//server only
        Entity e2 = entityFactory.createEntity();//both
        Entity e3 = entityFactory.createEntity();//client only

        e1.coordinates = coordinatesToTest;
        e2.coordinates = coordinatesToTest;
        e3.coordinates = coordinatesToTest;

        serverGameStore.addEntity(e1);
        serverGameStore.addEntity(e2);
        clientGameStore.addEntity(e2);
        clientGameStore.addEntity(e3);

        serverNetworkHandle.initHandshake(clientNetworkHandle.uuid, chunkRangeToTest);

        TimeUnit.SECONDS.sleep(1);
        assert serverChunk.equals(clientChunk);
    }
}
