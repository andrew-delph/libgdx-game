package networking.doubleclient;

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

public class DoubleClientTests {

  Injector serverInjector;
  Injector client_aInjector;
  Injector client_bInjector;

  ServerNetworkHandle server;
  ClientNetworkHandle client_a;
  ClientNetworkHandle client_b;

  @Before
  public void setup() throws IOException {
    serverInjector = Guice.createInjector(new ServerTestApp());
    server = serverInjector.getInstance(ServerNetworkHandle.class);
    server.start();

    client_aInjector = Guice.createInjector(new ClientTestApp());
    client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
    client_a.connect();

    client_bInjector = Guice.createInjector(new ClientTestApp());
    client_b = client_bInjector.getInstance(ClientNetworkHandle.class);
    client_b.connect();
  }

  @After
  public void cleanup() {
    client_a.disconnect();
    client_b.disconnect();
    server.close();
  }

  @Test
  public void doubleClientCreate() throws IOException, InterruptedException {
    UUID testID = UUID.randomUUID();
    int x = 6;
    int y = 7;
    // client a create entity
    EntityFactory serverEntityFactory = client_aInjector.getInstance(EntityFactory.class);
    Entity testEntity = serverEntityFactory.create(testID, x, y, UUID.randomUUID());
    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));
    TimeUnit.SECONDS.sleep(1);
    // check entity on server
    Entity receivedEntity = server.entityManager.get(testID);
    assertNotNull(receivedEntity);
    assertEquals(receivedEntity.getX(), x, 0.1);
    assertEquals(receivedEntity.getY(), y, 0.1);
    TimeUnit.SECONDS.sleep(1);
    // check if it exists in client_b
    Entity entity_b = client_b.entityManager.get(receivedEntity.getID());
    assertEquals(entity_b.getID(), testID);
    assertEquals(entity_b.getX(), x, 0.1);
    assertEquals(entity_b.getY(), y, 0.1);
  }

  @Test
  public void doubleClientCreateUpdate() throws InterruptedException {
    UUID testID = UUID.randomUUID();
    float x = 6;
    float y = 7;
    EntityFactory entityFactory = client_aInjector.getInstance(EntityFactory.class);
    // client a create entity
    Entity testEntity = entityFactory.create(testID, x, y, UUID.randomUUID());
    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));
    TimeUnit.SECONDS.sleep(1);
    // check entity on server
    Entity receivedEntity = server.entityManager.get(testID);
    assertNotNull(receivedEntity);
    assertEquals(receivedEntity.getX(), x, 0.1);
    assertEquals(receivedEntity.getY(), y, 0.1);
    TimeUnit.SECONDS.sleep(1);
    // check if it exists in client_b
    Entity entity_b = client_b.entityManager.get(receivedEntity.getID());
    assertEquals(entity_b.getID(), testID);
    assertEquals(entity_b.getX(), x, 0.1);
    assertEquals(entity_b.getY(), y, 0.1);
    // client a update entity
    testEntity.moveX(2);
    testEntity.moveY(3);
    x = testEntity.getX();
    y = testEntity.getY();

    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingUpdateEntityEvent(testEntity.toEntityData()));

    TimeUnit.SECONDS.sleep(1);
    // check entity move on server
    assertEquals(receivedEntity.getID(), testID);
    assertEquals(receivedEntity.getX(), x, 0.1);
    assertEquals(receivedEntity.getY(), y, 0.1);
    // check entity on client b
    entity_b = client_b.entityManager.get(receivedEntity.getID());
    assertEquals(entity_b.getID(), testID);
    assertEquals(x, entity_b.getX(), 0.1);
  }

  @Test
  public void doubleClientRemove() throws IOException, InterruptedException {
    UUID testID = UUID.randomUUID();
    int x = 6;
    int y = 7;
    // client a create entity
    EntityFactory serverEntityFactory = client_aInjector.getInstance(EntityFactory.class);
    Entity testEntity = serverEntityFactory.create(testID, x, y, UUID.randomUUID());
    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingCreateEntityEvent(testEntity.toEntityData()));
    TimeUnit.SECONDS.sleep(1);
    // check entity on server
    Entity receivedEntity = server.entityManager.get(testID);
    assertNotNull(receivedEntity);
    assertEquals(receivedEntity.getX(), x, .1);
    assertEquals(receivedEntity.getY(), y, .1);
    TimeUnit.SECONDS.sleep(1);
    // check if it exists in client_b
    Entity entity_b = client_b.entityManager.get(receivedEntity.getID());
    assertEquals(entity_b.getID(), testID);
    assertEquals(entity_b.getX(), x, .1);
    assertEquals(entity_b.getY(), y, .1);
    // remove from client a
    client_aInjector
        .getInstance(EventService.class)
        .fireEvent(new OutgoingRemoveEntityEvent(testEntity.toEntityData()));
    TimeUnit.SECONDS.sleep(1);
    assertNull(server.entityManager.get(testEntity.getID()));
    assertNull(client_b.entityManager.get(testEntity.getID()));
  }
}
