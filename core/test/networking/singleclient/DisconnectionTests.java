package networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import infra.entity.Entity;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import modules.App;
import networking.NetworkObject;
import networking.client.ClientNetworkHandle;
import networking.connetion.AbtractConnection;
import networking.connetion.ConnectionStore;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DisconnectionTests {
    Injector serverInjector;
    ServerNetworkHandle server;

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
    public void clientDisconnection() throws InterruptedException {
        Injector client_aInjector;
        ClientNetworkHandle client_a;
        client_aInjector = Guice.createInjector(new App());
        client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
        client_a.connect();
        ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
        TimeUnit.SECONDS.sleep(1);
        List serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(3, serverConnections.size());
        client_a.disconnect();
        TimeUnit.SECONDS.sleep(1);
        serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(0, serverConnections.size());
    }

    @Test
    public void clientDisconnectionAndRemove() throws InterruptedException {
        Injector client_aInjector;
        ClientNetworkHandle client_a;
        client_aInjector = Guice.createInjector(new App());
        client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
        client_a.connect();
        ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
        TimeUnit.SECONDS.sleep(1);
        List serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(3, serverConnections.size());
        // client a create
        Entity testEntity = client_aInjector.getInstance(EntityFactory.class).createBasic();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);
        assertEquals(1, serverInjector.getInstance(EntityManager.class).getAll().length);
        //check it was removed on server
        client_a.disconnect();
        TimeUnit.SECONDS.sleep(1);
        assertEquals(0, serverInjector.getInstance(EntityManager.class).getAll().length);
        serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(0, serverConnections.size());
    }

    @Test
    public void doubleClientDisconnectionAndRemove() throws InterruptedException {
        Injector client_aInjector;
        ClientNetworkHandle client_a;
        client_aInjector = Guice.createInjector(new App());
        client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
        client_a.connect();

        Injector client_bInjector;
        ClientNetworkHandle client_b;
        client_bInjector = Guice.createInjector(new App());
        client_b = client_bInjector.getInstance(ClientNetworkHandle.class);
        client_b.connect();


        ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
        TimeUnit.SECONDS.sleep(1);
        List serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(6, serverConnections.size());
        // client a create
        Entity testEntity = client_aInjector.getInstance(EntityFactory.class).createBasic();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);
        TimeUnit.SECONDS.sleep(1);
        assertEquals(1, serverInjector.getInstance(EntityManager.class).getAll().length);
        assertNotNull(client_b.entityManager.get(testEntity.getID()));

        //check it was removed on server
        client_a.disconnect();
        TimeUnit.SECONDS.sleep(1);
        assertEquals(null, client_b.entityManager.get(testEntity.getID()));
        assertEquals(0, serverInjector.getInstance(EntityManager.class).getAll().length);
        serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(3, serverConnections.size());
        client_b.disconnect();
        TimeUnit.SECONDS.sleep(1);
        serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(0, serverConnections.size());
    }
}
