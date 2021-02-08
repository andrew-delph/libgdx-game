package networking;

import infra.entity.Entity;
import infra.entity.factories.EntityFactory;
import io.grpc.stub.StreamObserver;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class TestEntityNetworking {

    ServerNetworkHandle server;

    @Before
    public void setup() throws IOException {
        server = new ServerNetworkHandle();
        server.start();
    }

    @After
    public void cleanup() throws InterruptedException {
        server.close();

        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void singleClientCreate() throws IOException, InterruptedException {

        ClientNetworkHandle client_a = new ClientNetworkHandle(ClientNetworkHandle.host, ClientNetworkHandle.port);
        client_a.connect();

        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        Entity testEntity = EntityFactory.getInstance().create(testID, x, y);
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

    }

    @Test
    public void doubleClientCreate() throws IOException, InterruptedException {

        ClientNetworkHandle client_a = new ClientNetworkHandle(ClientNetworkHandle.host, ClientNetworkHandle.port);
        client_a.connect();
        ClientNetworkHandle client_b = new ClientNetworkHandle(ClientNetworkHandle.host, ClientNetworkHandle.port);
        client_b.connect();
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        // client a create entity
        Entity testEntity = EntityFactory.getInstance().create(testID, x, y);
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

    }

    @Test
    public void singleClientCreateUpdate() throws IOException, InterruptedException {

        ClientNetworkHandle client_a = new ClientNetworkHandle(ClientNetworkHandle.host, ClientNetworkHandle.port);
        client_a.connect();

        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        Entity testEntity = EntityFactory.getInstance().create(testID, x, y);
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

    }

    @Test
    public void doubleClientCreateUpdate() throws IOException, InterruptedException {

        ClientNetworkHandle client_a = new ClientNetworkHandle(ClientNetworkHandle.host, ClientNetworkHandle.port);
        client_a.connect();
        ClientNetworkHandle client_b = new ClientNetworkHandle(ClientNetworkHandle.host, ClientNetworkHandle.port);
        client_b.connect();
        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        // client a create entity
        Entity testEntity = EntityFactory.getInstance().create(testID, x, y);
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
        assertEquals(y, entity_b.getY());
    }


    @Test
    public void test(){

        String x = "andrew";
        Object y = x;
        assertEquals(x, y);

    }

}
