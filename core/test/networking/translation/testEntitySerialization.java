package networking.translation;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.block.BlockFactory;
import java.io.IOException;
import java.util.UUID;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
import org.junit.Before;
import org.junit.Test;

public class testEntitySerialization {

  NetworkDataDeserializer entitySerializationConverter;

  GameStore gameStore;

  Injector injector;
  EntityFactory entityFactory;
  BlockFactory blockFactory;
  ChunkFactory chunkFactory;

  EventService eventService;
  EventTypeFactory eventTypeFactory;
  NetworkEventHandler networkEventHandler;
  EventConsumer eventConsumer;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    entityFactory = injector.getInstance(EntityFactory.class);
    entitySerializationConverter = injector.getInstance(NetworkDataDeserializer.class);
    gameStore = injector.getInstance(GameStore.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
    eventService = injector.getInstance(EventService.class);
    eventTypeFactory = injector.getInstance(EventTypeFactory.class);
    blockFactory = injector.getInstance(BlockFactory.class);
    networkEventHandler = injector.getInstance(NetworkEventHandler.class);
    eventConsumer = injector.getInstance(EventConsumer.class);
    eventConsumer.init();
  }

  @Test
  public void testCreateEntitySerialization() throws SerializationDataMissing {
    Entity entityWrite = entityFactory.createEntity(new Coordinates(2, 3));
    Entity entityRead = entitySerializationConverter.createEntity(entityWrite.toNetworkData());
    assert entityWrite.coordinates.equals(entityRead.coordinates);
    assert entityWrite.uuid.equals(entityRead.uuid);
  }

  @Test
  public void testUpdateEntitySerialization() throws EntityNotFound, SerializationDataMissing {
    Entity entityWrite = entityFactory.createEntity(new Coordinates(2, 3));
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(2, 3))));
    gameStore.addEntity(entityWrite);
    NetworkObjects.NetworkData networkData = entityWrite.toNetworkData();
    entityWrite.coordinates = null;
    entitySerializationConverter.updateEntity(networkData);
    assert entityWrite.coordinates.equals(new Coordinates(2, 3));
  }

  @Test
  public void testCreateEntityNetworkEvent() throws EntityNotFound {
    Entity entityWrite = entityFactory.createEntity(new Coordinates(0, 0));
    UUID uuid = entityWrite.uuid;
    gameStore.addChunk(chunkFactory.create(new ChunkRange(entityWrite.coordinates)));
    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                entityWrite.toNetworkData(), new ChunkRange(entityWrite.coordinates))
            .toNetworkEvent());
    assert uuid.equals(gameStore.getEntity(uuid).uuid);
  }

  @Test
  public void testBlockWrite() throws EntityNotFound {
    Entity block = blockFactory.createDirt(new Coordinates(0, 0));
    UUID uuid = block.uuid;
    gameStore.addChunk(chunkFactory.create(new ChunkRange(block.coordinates)));
    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                block.toNetworkData(), new ChunkRange(block.coordinates))
            .toNetworkEvent());
    assert uuid.equals(gameStore.getEntity(uuid).uuid);
    assert gameStore.getEntity(uuid).getClass().getName().equals(block.getClass().getName());
  }
}
