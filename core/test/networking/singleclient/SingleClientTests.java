package networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.ClientTestApp;
import configure.ServerTestApp;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.events.EventService;
import networking.client.ClientNetworkHandle;
import networking.events.outgoing.OutgoingCreateEntityEvent;
import networking.events.outgoing.OutgoingRemoveEntityEvent;
import networking.events.outgoing.OutgoingUpdateEntityEvent;
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
    serverInjector = Guice.createInjector(new ServerTestApp());
    server = serverInjector.getInstance(ServerNetworkHandle.class);
    server.start();

    client_aInjector = Guice.createInjector(new ClientTestApp());
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
    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));
    TimeUnit.SECONDS.sleep(1);
    Entity receivedEntity = server.entityManager.get(testID);
    assertNotNull(receivedEntity);
    assertEquals(receivedEntity.getX(), x, 0.1);
    assertEquals(receivedEntity.getY(), y, 0.1);
  }

  @Test
  public void singleClientCreateUpdate() throws InterruptedException {
    UUID testID = UUID.randomUUID();
    int x = 6;
    int y = 7;
    EntityFactory entityFactory = client_aInjector.getInstance(EntityFactory.class);
    Entity testEntity = entityFactory.create(testID, x, y, UUID.randomUUID());

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);

    Entity receivedEntity = server.entityManager.get(testID);
    assertNotNull(receivedEntity);
    assertEquals(receivedEntity.getX(), x, 0.1);
    assertEquals(receivedEntity.getY(), y, 0.1);
    TimeUnit.SECONDS.sleep(1);

    testEntity.moveX(2);
    testEntity.moveY(2);

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingUpdateEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);
    assertEquals(receivedEntity.getID(), testID);
    assertEquals(x + 2, receivedEntity.getX(), 0.1);
    assertEquals(y + 2, receivedEntity.getY(), 0.1);
  }

  @Test
  public void clientRemoveToServer() throws InterruptedException {
    UUID testID = UUID.randomUUID();
    int x = 6;
    int y = 7;
    EntityFactory serverEntityFactory = client_aInjector.getInstance(EntityFactory.class);
    Entity testEntity = serverEntityFactory.create(testID, x, y, UUID.randomUUID());

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);
    Entity receivedEntity = server.entityManager.get(testID);
    assertNotNull(receivedEntity);
    assertEquals(receivedEntity.getX(), x, 0.1);
    assertEquals(receivedEntity.getY(), y, 0.1);
    // remove

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingRemoveEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);
    assertNull(server.entityManager.get(testEntity.getID()));
  }
}
