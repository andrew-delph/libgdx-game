package infra.serialization;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.EntitySerializationConverter;
import infra.entity.block.BlockFactory;
import infra.networking.NetworkEventHandler;
import infra.networking.NetworkObjects;
import infra.networking.events.EntityEventFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class testEntitySerialization {

  EntitySerializationConverter entitySerializationConverter;

  GameStore gameStore;

  Injector injector;
  EntityFactory entityFactory;
  BlockFactory blockFactory;
  ChunkFactory chunkFactory;

  EventService eventService;
  EntityEventFactory entityEventFactory;
  NetworkEventHandler networkEventHandler;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    entityFactory = injector.getInstance(EntityFactory.class);
    entitySerializationConverter = injector.getInstance(EntitySerializationConverter.class);
    gameStore = injector.getInstance(GameStore.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
    eventService = injector.getInstance(EventService.class);
    entityEventFactory = injector.getInstance(EntityEventFactory.class);
    blockFactory = injector.getInstance(BlockFactory.class);
    networkEventHandler = injector.getInstance(NetworkEventHandler.class);
  }

  @Test
  public void testCreateEntitySerialization() {
    Entity entityWrite = entityFactory.createEntity();
    entityWrite.coordinates = new Coordinates(2, 3);
    Entity entityRead = entitySerializationConverter.createEntity(entityWrite.toNetworkData());
    assert entityWrite.coordinates.equals(entityRead.coordinates);
    assert entityWrite.uuid.equals(entityRead.uuid);
  }

  @Test
  public void testUpdateEntitySerialization() {
    Entity entityWrite = entityFactory.createEntity();
    entityWrite.coordinates = new Coordinates(2, 3);
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    gameStore.addEntity(entityWrite);
    NetworkObjects.NetworkData networkData = entityWrite.toNetworkData();
    entityWrite.coordinates = null;
    entitySerializationConverter.updateEntity(networkData);
    assert entityWrite.coordinates.equals(new Coordinates(2, 3));
  }

  @Test
  public void testCreateEntityNetworkEvent() {
    Entity entityWrite = entityFactory.createEntity();
    UUID uuid = entityWrite.uuid;
    networkEventHandler.handleNetworkEvent(entityEventFactory.createCreateEntityIncomingEvent(entityWrite.toNetworkData()).toNetworkEvent());
    assert uuid.equals(gameStore.getEntity(uuid).uuid);
  }

  @Test
  public void testBlockWrite() {
    Entity block = blockFactory.create();
    UUID uuid = block.uuid;
    networkEventHandler.handleNetworkEvent(entityEventFactory.createCreateEntityIncomingEvent(block.toNetworkData()).toNetworkEvent());
    assert uuid.equals(gameStore.getEntity(uuid).uuid);
    assert gameStore.getEntity(uuid).getClass().getName().equals(block.getClass().getName());
  }
}
