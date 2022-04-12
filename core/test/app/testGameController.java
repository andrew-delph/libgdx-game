package app;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import common.events.EventService;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testGameController {

  Injector injector;
  GameStore gameStore;
  GameController gameController;
  EntityFactory entityFactory;
  ChunkFactory chunkFactory;
  EventService eventService;
  EventConsumer eventConsumer;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    gameStore = injector.getInstance(GameStore.class);
    gameController = injector.getInstance(GameController.class);
    entityFactory = injector.getInstance(EntityFactory.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
    eventService = injector.getInstance(EventService.class);
    eventConsumer = injector.getInstance(EventConsumer.class);
  }

  @Test
  public void testEntitySync() throws EntityNotFound, ChunkNotFound, BodyNotFound {
    eventConsumer.init();
    ChunkRange chunkRange1 = new ChunkRange(new Coordinates(0, 0));
    ChunkRange chunkRange2 = chunkRange1.getRight();
    this.gameStore.addChunk(this.chunkFactory.create(chunkRange1));
    this.gameStore.addChunk(this.chunkFactory.create(chunkRange2));
    Entity entity = this.entityFactory.createEntity(new Coordinates(0, 1));
    this.gameStore.addEntity(entity);
    System.out.println(entity.getBodyPosition());
    assert this.gameStore.getEntityChunk(entity.uuid).chunkRange == chunkRange1;
    Assert.assertEquals(chunkRange1, this.gameStore.getEntityChunk(entity.uuid).chunkRange);
    entity.coordinates = new Coordinates(chunkRange2.bottom_x, chunkRange2.bottom_y);
    this.gameStore.syncEntity(entity);
    eventService.firePostUpdateEvents();
    Assert.assertEquals(chunkRange2, this.gameStore.getEntityChunk(entity.uuid).chunkRange);
  }
}
