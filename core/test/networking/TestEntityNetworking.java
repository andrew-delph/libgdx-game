package networking;

import com.google.inject.Guice;
import com.google.inject.Injector;
import infra.entity.Entity;
import infra.entity.factories.EntityFactory;
import modules.App;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TestEntityNetworking {

    ServerNetworkHandle server;
    Injector serverInjector;

    @Before
    public void setup() throws IOException {
        serverInjector = Guice.createInjector(new App());
        server = serverInjector.getInstance(ServerNetworkHandle.class);
        server.start();
    }

    @After
    public void cleanup() {
        server.close();
    }

    @Test
    public void singleClientCreate() throws InterruptedException {

        Injector injector = Guice.createInjector(new App());
        ClientNetworkHandle client_a = injector.getInstance(ClientNetworkHandle.class);
        client_a.connect();

        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;
        EntityFactory serverEntityFactory = injector.getInstance(EntityFactory.class);
        Entity testEntity = serverEntityFactory.create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);

        TimeUnit.SECONDS.sleep(1);

        Entity receivedEntity = server.entityManager.get(testID);

        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);

        TimeUnit.SECONDS.sleep(1);
        client_a.disconnect();

    }

    @Test
    public void doubleClientCreate() throws IOException, InterruptedException {
        Injector injector_a = Guice.createInjector(new App());
        Injector injector_b = Guice.createInjector(new App());

        ClientNetworkHandle client_a = injector_a.getInstance(ClientNetworkHandle.class);
        client_a.connect();
        ClientNetworkHandle client_b = injector_b.getInstance(ClientNetworkHandle.class);
        client_b.connect();
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        // client a create entity
        EntityFactory serverEntityFactory = injector_a.getInstance(EntityFactory.class);
        Entity testEntity = serverEntityFactory.create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);

        // check entity on server
        Entity receivedEntity = server.entityManager.get(testID);
        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);
        TimeUnit.SECONDS.sleep(1);

        // check if it exists in client_b
        Entity entity_b = client_b.entityManager.get(receivedEntity.getID());
        client_b.entityManager.update(e ->System.out.println("cleint b entity"+e.getEntityData().getID()));
        assertEquals(entity_b.getID(),testID);
        assertEquals(entity_b.getX(),x);
        assertEquals(entity_b.getY(),y);

        client_a.disconnect();
        client_b.disconnect();

    }

    @Test
    public void singleClientCreateUpdate() throws IOException, InterruptedException {
        Injector injector_a = Guice.createInjector(new App());

        ClientNetworkHandle client_a = injector_a.getInstance(ClientNetworkHandle.class);
        client_a.connect();

        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        EntityFactory entityFactory = injector_a.getInstance(EntityFactory.class);

        Entity testEntity = entityFactory.create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);

        TimeUnit.SECONDS.sleep(1);

        Entity receivedEntity = server.entityManager.get(testID);

        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);

        TimeUnit.SECONDS.sleep(1);

        testEntity.moveX(2);
        testEntity.moveY(3);

        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.UpdateNetworkObject updateRequestObject = NetworkObject.UpdateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
        client_a.updateRequest.onNext(updateRequestObject);

        TimeUnit.SECONDS.sleep(1);

        assertEquals(receivedEntity.getID(),testID);
        assertEquals(x+2, receivedEntity.getX());
        assertEquals(y+3 ,receivedEntity.getY());
        client_a.disconnect();

    }

    @Test
    public void doubleClientCreateUpdate() throws IOException, InterruptedException {

        Injector injector_a = Guice.createInjector(new App());
        Injector injector_b = Guice.createInjector(new App());

        ClientNetworkHandle client_a = injector_a.getInstance(ClientNetworkHandle.class);
        client_a.connect();
        ClientNetworkHandle client_b = injector_b.getInstance(ClientNetworkHandle.class);
        client_b.connect();
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        EntityFactory entityFactory = injector_a.getInstance(EntityFactory.class);

        // client a create entity
        Entity testEntity = entityFactory.create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);

        // check entity on server
        Entity receivedEntity = server.entityManager.get(testID);
        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);
        TimeUnit.SECONDS.sleep(1);

        // check if it exists in client_b
        Entity entity_b = client_b.entityManager.get(receivedEntity.getID());
        client_b.entityManager.update(e ->System.out.println("cleint b entity"+e.getEntityData().getID()));
        assertEquals(entity_b.getID(),testID);
        assertEquals(entity_b.getX(),x);
        assertEquals(entity_b.getY(),y);

        // client a update entity
        testEntity.moveX(2);
        testEntity.moveY(3);
        x = testEntity.getX();
        y = testEntity.getY();
        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.UpdateNetworkObject updateRequestObject = NetworkObject.UpdateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
        client_a.updateRequest.onNext(updateRequestObject);
        TimeUnit.SECONDS.sleep(1);

        // cehck entity move on server
        assertEquals(receivedEntity.getID(),testID);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);
//
        // check entity on client b
        entity_b = client_b.entityManager.get(receivedEntity.getID());
        client_b.entityManager.update(e ->System.out.println("cleint b entity"+e.getEntityData().getID()));
        assertEquals(entity_b.getID(),testID);
        assertEquals(x, entity_b.getX());

        client_a.disconnect();
        client_b.disconnect();
    }

    @Test
    public void clientRemoveToServer() throws InterruptedException {
        Injector injector = Guice.createInjector(new App());
        ClientNetworkHandle client_a = injector.getInstance(ClientNetworkHandle.class);
        client_a.connect();

        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;
        EntityFactory serverEntityFactory = injector.getInstance(EntityFactory.class);
        Entity testEntity = serverEntityFactory.create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);

        TimeUnit.SECONDS.sleep(1);

        Entity receivedEntity = server.entityManager.get(testID);

        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);

        // remove
        NetworkObject.RemoveNetworkObject removeNetworkObject = NetworkObject.RemoveNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).build();
        client_a.removeRequest.onNext(removeNetworkObject);

        TimeUnit.SECONDS.sleep(1);

        assertNull(server.entityManager.get(testEntity.getID()));

        client_a.disconnect();
    }

    @Test
    public void doubleClientRemove() throws IOException, InterruptedException {
        Injector injector_a = Guice.createInjector(new App());
        Injector injector_b = Guice.createInjector(new App());

        ClientNetworkHandle client_a = injector_a.getInstance(ClientNetworkHandle.class);
        client_a.connect();
        ClientNetworkHandle client_b = injector_b.getInstance(ClientNetworkHandle.class);
        client_b.connect();
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        // client a create entity
        EntityFactory serverEntityFactory = injector_a.getInstance(EntityFactory.class);
        Entity testEntity = serverEntityFactory.create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);

        // check entity on server
        Entity receivedEntity = server.entityManager.get(testID);
        assertNotNull(receivedEntity);
        assertEquals(receivedEntity.getX(),x);
        assertEquals(receivedEntity.getY(),y);
        TimeUnit.SECONDS.sleep(1);

        // check if it exists in client_b
        Entity entity_b = client_b.entityManager.get(receivedEntity.getID());
        client_b.entityManager.update(e ->System.out.println("cleint b entity"+e.getEntityData().getID()));
        assertEquals(entity_b.getID(),testID);
        assertEquals(entity_b.getX(),x);
        assertEquals(entity_b.getY(),y);

        // remove from client a
        NetworkObject.RemoveNetworkObject removeNetworkObject = NetworkObject.RemoveNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).build();
        client_a.removeRequest.onNext(removeNetworkObject);

        TimeUnit.SECONDS.sleep(1);

        assertNull(server.entityManager.get(testEntity.getID()));

        assertNull(client_b.entityManager.get(testEntity.getID()));

        client_a.disconnect();
        client_b.disconnect();

    }
}
