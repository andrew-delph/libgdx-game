package core.networking.translation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.ChunkFactory;
import core.common.CommonFactory;
import core.common.GameStore;
import core.common.events.EventConsumer;
import core.common.events.EventService;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.block.BlockFactory;
import core.networking.events.EventTypeFactory;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
    Entity entityWrite = entityFactory.createEntity(CommonFactory.createCoordinates(2, 3));
    Entity entityRead = entitySerializationConverter.createEntity(entityWrite.toNetworkData());
    assert entityWrite
        .getCoordinatesWrapper()
        .getCoordinates()
        .equals(entityRead.getCoordinatesWrapper().getCoordinates());
    assert entityWrite.getUuid().equals(entityRead.getUuid());
  }

  @Test
  public void testCreateEntityNetworkEvent() throws EntityNotFound, InterruptedException {
    Entity entityWrite = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));
    UUID uuid = entityWrite.getUuid();
    gameStore.addChunk(
        chunkFactory.create(
            CommonFactory.createChunkRange(entityWrite.getCoordinatesWrapper().getCoordinates())));
    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                entityWrite.toNetworkData(),
                CommonFactory.createChunkRange(
                    entityWrite.getCoordinatesWrapper().getCoordinates()))
            .toNetworkEvent());
    TimeUnit.SECONDS.sleep(1);
    assert uuid.equals(gameStore.getEntity(uuid).getUuid());
  }

  @Test
  public void testBlockWrite() throws EntityNotFound, InterruptedException {
    Entity block = blockFactory.createDirt(CommonFactory.createCoordinates(0, 0));
    UUID uuid = block.getUuid();
    gameStore.addChunk(
        chunkFactory.create(
            CommonFactory.createChunkRange(block.getCoordinatesWrapper().getCoordinates())));
    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                block.toNetworkData(),
                CommonFactory.createChunkRange(block.getCoordinatesWrapper().getCoordinates()))
            .toNetworkEvent());
    TimeUnit.SECONDS.sleep(1);
    assert uuid.equals(gameStore.getEntity(uuid).getUuid());
    assert gameStore.getEntity(uuid).getClass().getName().equals(block.getClass().getName());
  }

  @Test
  public void testCreateWater() throws EntityNotFound, InterruptedException {
    Entity water = entityFactory.createWater(CommonFactory.createCoordinates(0, 0));
    UUID uuid = water.getUuid();
    gameStore.addChunk(
        chunkFactory.create(
            CommonFactory.createChunkRange(water.getCoordinatesWrapper().getCoordinates())));

    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                water.toNetworkData(),
                CommonFactory.createChunkRange(water.getCoordinatesWrapper().getCoordinates()))
            .toNetworkEvent());

    TimeUnit.SECONDS.sleep(1);
    assert uuid.equals(gameStore.getEntity(uuid).getUuid());
    assert gameStore.getEntity(uuid).getClass().getName().equals(water.getClass().getName());
  }
}
