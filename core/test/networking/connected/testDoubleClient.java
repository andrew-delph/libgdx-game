package networking.connected;

import app.GameController;
import app.game.Game;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import configuration.GameSettings;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import entity.misc.Ladder;
import generation.ChunkBuilderFactory;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Assert;
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

    Game serverGame;
    Game client_a_Game;
    Game client_b_Game;

    GameController serverGameController;
    GameController client_a_GameController;
    GameController client_b_GameController;

    Injector serverInjector;

    ServerNetworkHandle serverNetworkHandle;

    @Before
    public void setup() throws IOException, SerializationDataMissing, InterruptedException {
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
    public void testDoubleClientCreateEntity() throws InterruptedException, EntityNotFound {

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

        EventTypeFactory client_b_EventTypeFactory = client_b_Injector.getInstance(EventTypeFactory.class);

        client_b_NetworkHandle.send(
                client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

        TimeUnit.SECONDS.sleep(1);

        Entity clientEntity = client_a_GameController.addEntity(clientEntityFactory.createEntity());

        TimeUnit.SECONDS.sleep(3);

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
    public void testDoubleClientCreateUpdateEntity() throws InterruptedException, EntityNotFound {

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

        EventTypeFactory client_b_EventTypeFactory = client_b_Injector.getInstance(EventTypeFactory.class);

        client_b_NetworkHandle.send(
                client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

        TimeUnit.SECONDS.sleep(1);

        Entity clientEntity = client_a_GameController.addEntity(clientEntityFactory.createEntity());

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
    public void testDoubleClientCreateThenDisconnectRemoveOther() throws InterruptedException, EntityNotFound {
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

        EventTypeFactory client_b_EventTypeFactory = client_b_Injector.getInstance(EventTypeFactory.class);

        client_b_NetworkHandle.send(
                client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

        TimeUnit.SECONDS.sleep(1);

        Entity clientEntity = client_a_GameController.addEntity(clientEntityFactory.createEntity());

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

        assert !serverGameStore.doesEntityExist(clientEntity.uuid);
        assert !client_b_GameStore.doesEntityExist(clientEntity.uuid);
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

        EventTypeFactory client_b_EventTypeFactory = client_b_Injector.getInstance(EventTypeFactory.class);
        client_b_NetworkHandle.send(
                client_b_EventTypeFactory.createSubscriptionOutgoingEvent(chunkRangeList).toNetworkEvent());

        TimeUnit.SECONDS.sleep(1);
        Entity clientLadder = client_a_GameController.createLadder(coordinates);
        TimeUnit.SECONDS.sleep(1);

        assert serverGameStore.getEntity(clientLadder.uuid).equals(clientLadder);
        assert client_a_GameStore.getEntity(clientLadder.uuid).equals(clientLadder);
        assert client_b_GameStore.getEntity(clientLadder.uuid).equals(clientLadder);
    }

    @Test
    public void testRemoveClientToServer() throws InterruptedException, EntityNotFound {
        GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
        GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        GameController serverGameController = serverInjector.getInstance(GameController.class);
        GameController client_a_GameController = client_a_Injector.getInstance(GameController.class);

        Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
        TimeUnit.SECONDS.sleep(1);

        assert serverGameStore.getEntity(myEntity.uuid).equals(client_a_GameStore.getEntity(myEntity.uuid));
        assert serverGameStore.getEntity(myEntity.uuid).equals(client_b_GameStore.getEntity(myEntity.uuid));
        client_a_GameController.removeEntity(myEntity.uuid);
        TimeUnit.SECONDS.sleep(1);

        assert !serverGameStore.doesEntityExist(myEntity.uuid);
        assert !client_a_GameStore.doesEntityExist(myEntity.uuid);
        assert !client_b_GameStore.doesEntityExist(myEntity.uuid);
    }

    @Test
    public void testRemoveServerToClient() throws InterruptedException, EntityNotFound {
        GameStore client_a_GameStore = client_a_Injector.getInstance(GameStore.class);
        GameStore client_b_GameStore = client_b_Injector.getInstance(GameStore.class);
        GameStore serverGameStore = serverInjector.getInstance(GameStore.class);
        GameController serverGameController = serverInjector.getInstance(GameController.class);

        Entity myEntity = serverGameController.createEntity(new Coordinates(0, 0));
        TimeUnit.SECONDS.sleep(1);

        assert serverGameStore.getEntity(myEntity.uuid).equals(client_a_GameStore.getEntity(myEntity.uuid));
        assert serverGameStore.getEntity(myEntity.uuid).equals(client_b_GameStore.getEntity(myEntity.uuid));
        serverGameController.removeEntity(myEntity.uuid);
        TimeUnit.SECONDS.sleep(1);

        assert !serverGameStore.doesEntityExist(myEntity.uuid);
        assert !client_a_GameStore.doesEntityExist(myEntity.uuid);
        assert !client_b_GameStore.doesEntityExist(myEntity.uuid);
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
        Assert.assertEquals(serverGameStore.getChunk(chunkRangeToTest).getEntityList().size(), GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE);
        assert serverGameStore.getChunk(chunkRangeToTest).equals(client_a_GameStore.getChunk(chunkRangeToTest));
        assert serverGameStore.getChunk(chunkRangeToTest).equals(client_b_GameStore.getChunk(chunkRangeToTest));
        assert client_a_GameStore.getChunk(chunkRangeToTest).equals(client_b_GameStore.getChunk(chunkRangeToTest));
        assert serverGameStore.getBlock(coordinatesToTest).getClass() == SkyBlock.class;

        Block blockToRemove = client_a_GameStore.getBlock(coordinatesToTest);
        Block blockAsReplacement = client_a_BlockFactory.createDirt();
        blockAsReplacement.coordinates = coordinatesToTest;

        Entity ladder = client_a_GameController.createLadder(coordinatesToTest);
        TimeUnit.SECONDS.sleep(1);

        Assert.assertEquals(serverGameStore.getChunk(chunkRangeToTest).getEntityList().size(), (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE) + 1);
        // check chunks equal
        assert serverGameStore.getChunk(chunkRangeToTest).equals(client_a_GameStore.getChunk(chunkRangeToTest));
        assert serverGameStore.getChunk(chunkRangeToTest).equals(client_b_GameStore.getChunk(chunkRangeToTest));
        assert client_a_GameStore.getChunk(chunkRangeToTest).equals(client_b_GameStore.getChunk(chunkRangeToTest));
        // check block is equal
        Assert.assertEquals(client_a_GameStore.getBlock(coordinatesToTest).getClass(), SkyBlock.class);
        Assert.assertEquals(serverGameStore.getBlock(coordinatesToTest).getClass(), SkyBlock.class);
        Assert.assertEquals(client_b_GameStore.getBlock(coordinatesToTest).getClass(), SkyBlock.class);
        // check ladder exists
        assert serverGameStore.getEntity(ladder.uuid).getClass() == Ladder.class;
        assert client_a_GameStore.getEntity(ladder.uuid).equals(serverGameStore.getEntity(ladder.uuid));
        assert client_b_GameStore.getEntity(ladder.uuid).equals(serverGameStore.getEntity(ladder.uuid));

        client_a_GameController.replaceBlock(blockToRemove, blockAsReplacement);
        TimeUnit.SECONDS.sleep(1);

        // check block is equal
        Assert.assertEquals(serverGameStore.getBlock(coordinatesToTest).getClass(), DirtBlock.class);
        Assert.assertEquals(client_a_GameStore.getBlock(coordinatesToTest).getClass(), DirtBlock.class);
        Assert.assertEquals(client_b_GameStore.getBlock(coordinatesToTest).getClass(), DirtBlock.class);

        // check sizes
        Assert.assertEquals(serverGameStore.getChunk(chunkRangeToTest).getEntityList().size(), (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE));
        Assert.assertEquals(client_a_GameStore.getChunk(chunkRangeToTest).getEntityList().size(), (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE));
        Assert.assertEquals(client_b_GameStore.getChunk(chunkRangeToTest).getEntityList().size(), (GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE));

        // check chunks equal
        assert serverGameStore.getChunk(chunkRangeToTest).equals(client_a_GameStore.getChunk(chunkRangeToTest));
        assert serverGameStore.getChunk(chunkRangeToTest).equals(client_b_GameStore.getChunk(chunkRangeToTest));
        assert client_a_GameStore.getChunk(chunkRangeToTest).equals(client_b_GameStore.getChunk(chunkRangeToTest));

        //         check ladder exists
        assert !serverGameStore.doesEntityExist(ladder.uuid);
        assert !client_a_GameStore.doesEntityExist(ladder.uuid);
        assert !client_b_GameStore.doesEntityExist(ladder.uuid);
    }
}