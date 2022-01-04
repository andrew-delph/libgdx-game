package networking;

import app.Game;
import app.GameController;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.ChunkClockMap;
import common.Coordinates;
import common.GameStore;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import generation.ChunkBuilderFactory;
import generation.ChunkGenerationManager;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class testSingleClient {

    Injector clientInjector;
    Injector serverInjector;

    ClientNetworkHandle clientNetworkHandle;
    ServerNetworkHandle serverNetworkHandle;

    Game clientGame, serverGame;

    @Before
    public void setup() throws IOException, InterruptedException, SerializationDataMissing {
        clientInjector = Guice.createInjector(new ClientConfig());
        serverInjector = Guice.createInjector(new BaseServerConfig());

        clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);
        serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

        clientGame = clientInjector.getInstance(Game.class);
        serverGame = serverInjector.getInstance(Game.class);

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
    }

    @Test
    public void testAuthentication() throws InterruptedException {
        ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
        TimeUnit.SECONDS.sleep(1);
        assert connectionStore.size() == 1;
    }

    @Test
    public void testClientCreateEntity() throws InterruptedException, EntityNotFound {
        GameController clientGameController = clientInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
        clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

        EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
        TimeUnit.SECONDS.sleep(1);

        Entity clientEntity = clientGameController.addEntity(clientEntityFactory.createEntity());
        TimeUnit.SECONDS.sleep(1);

        assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
        assert serverGameStore
                .getEntity(clientEntity.uuid)
                .coordinates
                .equals(clientEntity.coordinates);
    }

    @Test
    public void testClientCreateUpdateEntity() throws IOException, InterruptedException, EntityNotFound {

        GameController clientGameController = clientInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
        clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));

        TimeUnit.SECONDS.sleep(1);

        EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
        Entity clientEntity = clientGameController.addEntity(clientEntityFactory.createEntity());
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
    public void testClientCreateBlock() throws IOException, InterruptedException, EntityNotFound {

        GameController clientGameController = clientInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
        clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
        TimeUnit.SECONDS.sleep(1);

        Entity clientEntity = clientGameController.createDirtBlock(new Coordinates(0, 0));
        TimeUnit.SECONDS.sleep(1);

        assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
        assert serverGameStore
                .getEntity(clientEntity.uuid)
                .coordinates
                .equals(clientEntity.coordinates);
    }

    @Test
    public void testClientCreateUpdateBlock() throws IOException, InterruptedException, EntityNotFound {

        GameController clientGameController = clientInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
        clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
        TimeUnit.SECONDS.sleep(1);

        Entity clientEntity = clientGameController.createDirtBlock(new Coordinates(0, 0));
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

        ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
        ChunkSubscriptionService serverChunkSubscriptionService =
                serverInjector.getInstance(ChunkSubscriptionService.class);
        TimeUnit.SECONDS.sleep(1);

        Assert.assertEquals(
                new HashSet<>(clientChunkClockMap.getChunkRangeList()),
                new HashSet<>(
                        serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                                clientNetworkHandle.uuid)));
    }

    @Test
    public void testSubscriptionServerCreateEntity() throws IOException, InterruptedException, EntityNotFound {
        GameController serverGameController = serverInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
        ChunkSubscriptionService serverChunkSubscriptionService =
                serverInjector.getInstance(ChunkSubscriptionService.class);
        TimeUnit.SECONDS.sleep(1);

        Assert.assertEquals(
                new HashSet<>(clientChunkClockMap.getChunkRangeList()),
                new HashSet<>(
                        serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                                clientNetworkHandle.uuid)));
        EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
        Entity serverEntity = serverGameController.addEntity(clientEntityFactory.createEntity());

        TimeUnit.SECONDS.sleep(1);
        assert serverEntity.uuid.equals(clientGameStore.getEntity(serverEntity.uuid).uuid);
        assert serverEntity.coordinates.equals(
                clientGameStore.getEntity(serverEntity.uuid).coordinates);
    }

    @Test
    public void testSubscriptionServerCreateEntityUpdate() throws IOException, InterruptedException, EntityNotFound {

        GameController serverGameController = serverInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        ChunkClockMap clientChunkClockMap = clientInjector.getInstance(ChunkClockMap.class);
        ChunkSubscriptionService serverChunkSubscriptionService =
                serverInjector.getInstance(ChunkSubscriptionService.class);
        TimeUnit.SECONDS.sleep(1);
        Assert.assertEquals(
                new HashSet<>(clientChunkClockMap.getChunkRangeList()),
                new HashSet<>(
                        serverChunkSubscriptionService.getUserChunkRangeSubscriptions(
                                clientNetworkHandle.uuid)));
        EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
        Entity serverEntity = serverGameController.addEntity(clientEntityFactory.createEntity());
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
    public void testClientDisconnectRemoveEntity() throws InterruptedException, EntityNotFound {
        GameController clientGameController = clientInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
        clientGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
        EntityFactory clientEntityFactory = clientInjector.getInstance(EntityFactory.class);
        TimeUnit.SECONDS.sleep(1);
        Entity clientEntity = clientGameController.addEntity(clientEntityFactory.createEntity());
        TimeUnit.SECONDS.sleep(1);
        assert serverGameStore.getEntity(clientEntity.uuid).uuid.equals(clientEntity.uuid);
        assert serverGameStore
                .getEntity(clientEntity.uuid)
                .coordinates
                .equals(clientEntity.coordinates);
        this.clientNetworkHandle.close();
        TimeUnit.SECONDS.sleep(1);
        assert !serverGameStore.doesEntityExist(clientEntity.uuid);
    }

    @Test
    public void testClientReplaceBlock() throws InterruptedException, EntityNotFound {
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory clientChunkFactory = clientInjector.getInstance(ChunkFactory.class);
        GameController serverGameController = serverInjector.getInstance(GameController.class);
        BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
        EventService clientEventService = clientInjector.getInstance(EventService.class);
        serverGameStore.addChunk(clientChunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
        serverGameController.addEntity(serverBlockFactory.createDirt());
        assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == DirtBlock.class;
        TimeUnit.SECONDS.sleep(1);
        Block clientBlock = serverGameStore.getBlock(new Coordinates(0, 0));
        clientEventService.fireEvent(
                EventTypeFactory.createReplaceBlockOutgoingEvent(
                        clientBlock.uuid,
                        serverBlockFactory.createSky(),
                        new ChunkRange(clientBlock.coordinates)));
        TimeUnit.SECONDS.sleep(1);
        assert serverGameStore.getBlock(new Coordinates(0, 0)).getClass() == SkyBlock.class;
    }

    @Test
    public void testServerReplaceBlock() throws InterruptedException, EntityNotFound {
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        GameController serverGameController = serverInjector.getInstance(GameController.class);
        BlockFactory serverBlockFactory = serverInjector.getInstance(BlockFactory.class);
        EventService serverEventService = serverInjector.getInstance(EventService.class);
        EventService clientEventService = clientInjector.getInstance(EventService.class);
        TimeUnit.SECONDS.sleep(1);
        Entity serverEntity = serverGameController.addEntity(serverBlockFactory.createDirt());
        assert serverGameStore.getBlock(new Coordinates(0, 0)).equals(serverEntity);
        TimeUnit.SECONDS.sleep(1);
        assert clientGameStore.getBlock(new Coordinates(0, 0)).equals(serverEntity);
        serverEventService.fireEvent(
                EventTypeFactory.createReplaceBlockOutgoingEvent(
                        serverEntity.uuid,
                        serverBlockFactory.createSky(),
                        new ChunkRange(serverEntity.coordinates)));
        TimeUnit.SECONDS.sleep(1);
        assert clientGameStore.getBlock(new Coordinates(0, 0)).getClass() == SkyBlock.class;
    }

    @Test
    public void testClientCreateLadder() throws Exception {
        GameController clientGameController = clientInjector.getInstance(GameController.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkBuilderFactory chunkBuilderFactory = serverInjector.getInstance(ChunkBuilderFactory.class);
        Coordinates coordinates = new Coordinates(0, 1);
        ChunkRange chunkRange = new ChunkRange(coordinates);
        serverGameStore.addChunk(chunkBuilderFactory.create(chunkRange).call());
        clientGameStore.addChunk(clientNetworkHandle.getChunk(chunkRange));
        Entity clientEntity = clientGameController.createLadder(coordinates);
        TimeUnit.SECONDS.sleep(1);
        assert serverGameStore.getEntity(clientEntity.uuid).equals(clientGameStore.getEntity(clientEntity.uuid));
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
        clientGameStore.addChunk(clientNetworkHandle.getChunk(chunkRange));
        Entity clientEntity = serverGameController.createLadder(coordinates);
        TimeUnit.SECONDS.sleep(1);
        assert serverGameStore.getEntity(clientEntity.uuid).equals(clientGameStore.getEntity(clientEntity.uuid));
    }

    @Test
    public void testClientCreateAIEntity() throws InterruptedException, SerializationDataMissing {
        GameController clientGameController = clientInjector.getInstance(GameController.class);
        ChunkGenerationManager chunkGenerationManager = serverInjector.getInstance(ChunkGenerationManager.class);

        assert chunkGenerationManager.getActiveEntityList().size()==0;
        clientGameController.createAI();
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void testGetChunk() throws SerializationDataMissing, EntityNotFound {
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        ChunkFactory chunkFactory = serverInjector.getInstance(ChunkFactory.class);
        Coordinates coordinates = new Coordinates(-1, 0);
        ChunkRange chunkRange = new ChunkRange(coordinates);
        serverGameStore.addChunk(chunkFactory.create(chunkRange));
        Chunk serverChunk = serverGameStore.getChunk(chunkRange);
        EntityFactory serverEntityFactory = serverInjector.getInstance(EntityFactory.class);
        Entity serverEntity = serverEntityFactory.createEntity();
        serverEntity.coordinates = coordinates;
        serverGameStore.addEntity(serverEntity);
        Chunk clientChunk = clientNetworkHandle.getChunk(chunkRange);
        assert clientChunk.equals(serverChunk);
        assert clientChunk.getEntity(serverEntity.uuid) != null;
    }

    @Test
    public void testRemoveClientToServer() throws InterruptedException, EntityNotFound {
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameController serverGameController = serverInjector.getInstance(GameController.class);
        GameController clientGameController = clientInjector.getInstance(GameController.class);

        Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
        TimeUnit.SECONDS.sleep(1);

        assert serverGameStore.getEntity(myEntity.uuid).equals(clientGameStore.getEntity(myEntity.uuid));
        clientGameController.removeEntity(myEntity.uuid);
        TimeUnit.SECONDS.sleep(1);

        assert !serverGameStore.doesEntityExist(myEntity.uuid);
        assert !clientGameStore.doesEntityExist(myEntity.uuid);
    }

    @Test
    public void testRemoveServerToClient() throws InterruptedException, EntityNotFound {
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        GameStore clientGameStore = clientInjector.getInstance(GameStore.class);
        GameController serverGameController = serverInjector.getInstance(GameController.class);
        ChunkSubscriptionService serverChunkSubscriptionService = serverInjector.getInstance(ChunkSubscriptionService.class);
        Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));

        serverChunkSubscriptionService.registerSubscription(clientNetworkHandle.uuid, new ChunkRange(new Coordinates(0, 0)));

        TimeUnit.SECONDS.sleep(1);
        assert serverGameStore.getEntity(myEntity.uuid).equals(clientGameStore.getEntity(myEntity.uuid));
        serverGameController.removeEntity(myEntity.uuid);
        TimeUnit.SECONDS.sleep(1);

        assert !serverGameStore.doesEntityExist(myEntity.uuid);
        assert !clientGameStore.doesEntityExist(myEntity.uuid);
    }
}
