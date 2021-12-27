package networking;

import app.Game;
import app.GameController;
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
import entity.Entity;
import entity.EntityFactory;
import generation.ChunkBuilderFactory;
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

public class testDoubleClient {

    Injector client_a_Injector;
    Injector client_b_Injector;

    ClientNetworkHandle client_a_NetworkHandle;
    ClientNetworkHandle client_b_NetworkHandle;

    Game serverGame;
    Game client_a_Game;
    Game client_b_Game;

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
        client_a_GameStore.addChunk(client_a_NetworkHandle.getChunk(chunkRange));
        client_b_GameStore.addChunk(client_b_NetworkHandle.getChunk(chunkRange));

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
}
