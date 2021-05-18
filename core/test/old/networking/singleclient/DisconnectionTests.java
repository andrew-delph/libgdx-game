package old.networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import old.configure.ClientTestApp;
import old.configure.ServerTestApp;
import old.infra.entity.Entity;
import old.infra.entity.EntityFactory;
import old.infra.entity.EntityManager;
import old.infra.events.EventService;
import old.networking.client.ClientNetworkHandle;
import old.networking.connection.AbtractConnection;
import old.networking.connection.ConnectionStore;
import old.networking.events.outgoing.OutgoingCreateEntityEvent;
import old.networking.server.ServerNetworkHandle;
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
    serverInjector = Guice.createInjector(new ServerTestApp());
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
    client_aInjector = Guice.createInjector(new ClientTestApp());
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
    client_aInjector = Guice.createInjector(new ClientTestApp());
    client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
    client_a.connect();
    ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
    TimeUnit.SECONDS.sleep(1);
    List serverConnections = connectionStore.getAll(AbtractConnection.class);
    assertEquals(3, serverConnections.size());
    // client a create
    Entity testEntity = client_aInjector.getInstance(EntityFactory.class).createBasic();

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);
    assertEquals(1, serverInjector.getInstance(EntityManager.class).getAll().length);
    // check it was removed on server
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
    client_aInjector = Guice.createInjector(new ClientTestApp());
    client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
    client_a.connect();

    Injector client_bInjector;
    ClientNetworkHandle client_b;
    client_bInjector = Guice.createInjector(new ClientTestApp());
    client_b = client_bInjector.getInstance(ClientNetworkHandle.class);
    client_b.connect();

    ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
    TimeUnit.SECONDS.sleep(1);
    List serverConnections = connectionStore.getAll(AbtractConnection.class);
    assertEquals(6, serverConnections.size());
    // client a create
    Entity testEntity = client_aInjector.getInstance(EntityFactory.class).createBasic();

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);
    assertEquals(1, serverInjector.getInstance(EntityManager.class).getAll().length);
    assertNotNull(client_b.entityManager.get(testEntity.getID()));

    // check it was removed on server
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
