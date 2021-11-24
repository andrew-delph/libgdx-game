package infra.serialization;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.events.EventService;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.EntitySerializationConverter;
import entity.block.BlockFactory;
import networking.NetworkEventHandler;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
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
  EventTypeFactory eventTypeFactory;
  NetworkEventHandler networkEventHandler;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    entityFactory = injector.getInstance(EntityFactory.class);
    entitySerializationConverter = injector.getInstance(EntitySerializationConverter.class);
    gameStore = injector.getInstance(GameStore.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
    eventService = injector.getInstance(EventService.class);
    eventTypeFactory = injector.getInstance(EventTypeFactory.class);
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
    gameStore.addChunk(chunkFactory.create(new ChunkRange(entityWrite.coordinates)));
    networkEventHandler.handleNetworkEvent(
        eventTypeFactory
            .createCreateEntityOutgoingEvent(
                entityWrite.toNetworkData(), new ChunkRange(entityWrite.coordinates))
            .toNetworkEvent());
    assert uuid.equals(gameStore.getEntity(uuid).uuid);
  }

  @Test
  public void testBlockWrite() {
    Entity block = blockFactory.createDirt();
    UUID uuid = block.uuid;
    gameStore.addChunk(chunkFactory.create(new ChunkRange(block.coordinates)));
    networkEventHandler.handleNetworkEvent(
        eventTypeFactory
            .createCreateEntityOutgoingEvent(
                block.toNetworkData(), new ChunkRange(block.coordinates))
            .toNetworkEvent());
    assert uuid.equals(gameStore.getEntity(uuid).uuid);
    assert gameStore.getEntity(uuid).getClass().getName().equals(block.getClass().getName());
  }
}
