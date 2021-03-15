package networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import infra.entity.Entity;
import infra.entity.factories.EntityFactory;
import modules.App;
import networking.NetworkObject;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SingleClientTests {
    Injector serverInjector;
    Injector client_aInjector;

    ServerNetworkHandle server;
    ClientNetworkHandle client_a;

    @Before
    public void setup() throws IOException {
        serverInjector = Guice.createInjector(new App());
        server = serverInjector.getInstance(ServerNetworkHandle.class);
        server.start();

        client_aInjector = Guice.createInjector(new App());
        client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
        client_a.connect();
    }

    @After
    public void cleanup() {
        client_a.disconnect();
        server.close();
    }

    @Test
    public void test() throws InterruptedException {
        assertNotEquals(client_a.createObserver, client_a.createRequest);
        assertNotEquals(client_a.updateObserver, client_a.updateRequest);
        assertNotEquals(client_a.removeObserver, client_a.removeRequest);
    }

    @Test
    public void singleClientCreate() throws InterruptedException {
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;
        EntityFactory serverEntityFactory = client_aInjector.getInstance(EntityFactory.class);
        Entity testEntity = serverEntityFactory.create(testID, x, y, UUID.randomUUID());
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);
        Entity receivedEntity = server.entityManager.get(testID);
        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(), x);
        assertEquals(receivedEntity.getY(), y);
    }

    @Test
    public void singleClientCreateUpdate() throws InterruptedException {
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;
        EntityFactory entityFactory = client_aInjector.getInstance(EntityFactory.class);
        Entity testEntity = entityFactory.create(testID, x, y, UUID.randomUUID());
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);
        Entity receivedEntity = server.entityManager.get(testID);
        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(), x);
        assertEquals(receivedEntity.getY(), y);
        TimeUnit.SECONDS.sleep(1);
        testEntity.moveX(2);
        testEntity.moveY(3);
        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.UpdateNetworkObject updateRequestObject = NetworkObject.UpdateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
        client_a.updateRequest.onNext(updateRequestObject);
        TimeUnit.SECONDS.sleep(1);
        assertEquals(receivedEntity.getID(), testID);
        assertEquals(x + 2, receivedEntity.getX());
        assertEquals(y + 3, receivedEntity.getY());
    }

    @Test
    public void clientRemoveToServer() throws InterruptedException {
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;
        EntityFactory serverEntityFactory = client_aInjector.getInstance(EntityFactory.class);
        Entity testEntity = serverEntityFactory.create(testID, x, y, UUID.randomUUID());
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);
        Entity receivedEntity = server.entityManager.get(testID);
        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(), x);
        assertEquals(receivedEntity.getY(), y);
        // remove
        NetworkObject.RemoveNetworkObject removeNetworkObject = NetworkObject.RemoveNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).build();
        client_a.removeRequest.onNext(removeNetworkObject);
        TimeUnit.SECONDS.sleep(1);
        assertNull(server.entityManager.get(testEntity.getID()));
    }


}
