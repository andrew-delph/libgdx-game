package networking.connected;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import generation.ChunkGenerationService;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class testSingleClientNoRunningGame {
    Injector clientInjector;
    Injector serverInjector;

    ClientNetworkHandle clientNetworkHandle;
    ServerNetworkHandle serverNetworkHandle;

    GameStore serverGameStore;
    GameStore clientGameStore;

    EventConsumer serverEventConsumer;
    EventConsumer clientEventConsumer;

    ChunkGenerationService serverChunkGenerationService;


    @Before
    public void setup() throws IOException, InterruptedException, SerializationDataMissing {
        clientInjector = Guice.createInjector(new ClientConfig());
        serverInjector = Guice.createInjector(new BaseServerConfig());

        clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);
        serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

        serverGameStore = serverInjector.getInstance(GameStore.class);
        clientGameStore = clientInjector.getInstance(GameStore.class);

        serverEventConsumer = serverInjector.getInstance(EventConsumer.class);
        clientEventConsumer = clientInjector.getInstance(EventConsumer.class);

        serverChunkGenerationService = serverInjector.getInstance(ChunkGenerationService.class);

        serverEventConsumer.init();
        clientEventConsumer.init();

        serverNetworkHandle.start();
        clientNetworkHandle.connect();
    }

    @After
    public void cleanup() {
        try {
            serverNetworkHandle.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            clientNetworkHandle.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequestChunkAsyncExisting() throws InterruptedException {
        // make sure there is a chunk generated on the server
        // async request the chunk on the client
        // make sure it worked

        Coordinates coordinatesToTest = new Coordinates(-100, -100);
        ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

        Assert.assertEquals(false, serverGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertEquals(false, clientGameStore.doesChunkExist(chunkRangeToTest));

        // generate the chunk
        serverChunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
        TimeUnit.SECONDS.sleep(1);

        // client request the chunk
        Assert.assertTrue(clientNetworkHandle.requestChunkAsync(chunkRangeToTest));
        Assert.assertTrue(clientGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertFalse(clientNetworkHandle.requestChunkAsync(chunkRangeToTest));
        TimeUnit.SECONDS.sleep(1);

        Assert.assertTrue(serverGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertTrue(clientGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertEquals(serverGameStore.getChunk(chunkRangeToTest), clientGameStore.getChunk(chunkRangeToTest));
    }

    @Test
    public void testRequestChunkAsyncNonExisting() throws InterruptedException {
        // make sure there is NOT a chunk generated on the server
        // async request the chunk on the client
        // make sure it worked
        Coordinates coordinatesToTest = new Coordinates(-100, -100);
        ChunkRange chunkRangeToTest = new ChunkRange(coordinatesToTest);

        Assert.assertEquals(false, serverGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertEquals(false, clientGameStore.doesChunkExist(chunkRangeToTest));

        // client request the chunk
        Assert.assertTrue(clientNetworkHandle.requestChunkAsync(chunkRangeToTest));
        Assert.assertTrue(clientGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertFalse(clientNetworkHandle.requestChunkAsync(chunkRangeToTest));
        TimeUnit.SECONDS.sleep(1);

        Assert.assertTrue(serverGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertTrue(clientGameStore.doesChunkExist(chunkRangeToTest));
        Assert.assertEquals(serverGameStore.getChunk(chunkRangeToTest), clientGameStore.getChunk(chunkRangeToTest));
    }

    @Test
    public void testRequestChunkBlockingNotGenerated() throws SerializationDataMissing, EntityNotFound, InterruptedException {
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        Coordinates coordinates = new Coordinates(-1, 0);
        ChunkRange chunkRange = new ChunkRange(coordinates);
        Chunk clientChunk = clientNetworkHandle.requestChunkBlocking(chunkRange);
        Chunk serverChunk = serverGameStore.getChunk(chunkRange);
        assert clientChunk.equals(serverChunk);
        assert clientChunk.getEntityList().size() > 5;
        assert serverChunk.getEntityList().size() > 5;
        Assert.assertEquals(serverChunk.getEntityList().size(), clientChunk.getEntityList().size());
        assert clientChunk.equals(serverChunk);
    }

    @Test
    public void testRequestChunkBlockingGenerated() throws Exception {
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        Coordinates coordinates = new Coordinates(-1, 0);
        ChunkRange chunkRange = new ChunkRange(coordinates);
        serverChunkGenerationService.blockedChunkRangeToGenerate(chunkRange);
        Chunk clientChunk = clientNetworkHandle.requestChunkBlocking(chunkRange);
        Chunk serverChunk = serverGameStore.getChunk(chunkRange);
        assert clientChunk.getEntityList().size() > 5;
        assert serverChunk.getEntityList().size() > 5;
        Assert.assertEquals(serverChunk.getEntityList().size(), clientChunk.getEntityList().size());
        assert clientChunk.equals(serverChunk);
    }
}
