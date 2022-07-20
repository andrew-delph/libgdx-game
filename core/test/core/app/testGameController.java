package core.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.game.GameController;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.CommonFactory;
import core.common.GameStore;
import core.common.events.EventConsumer;
import core.common.events.EventService;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
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
    ChunkRange chunkRange1 = new ChunkRange(CommonFactory.createCoordinates(0, 0));
    ChunkRange chunkRange2 = chunkRange1.getRight();
    this.gameStore.addChunk(this.chunkFactory.create(chunkRange1));
    this.gameStore.addChunk(this.chunkFactory.create(chunkRange2));
    Entity entity = this.entityFactory.createEntity(CommonFactory.createCoordinates(0, 1));
    this.gameStore.addEntity(entity);
    System.out.println(entity.getBodyPosition());
    assert this.gameStore.getEntityChunk(entity.getUuid()).chunkRange == chunkRange1;
    Assert.assertEquals(chunkRange1, this.gameStore.getEntityChunk(entity.getUuid()).chunkRange);
    entity.coordinates =
        CommonFactory.createCoordinates(chunkRange2.bottom_x, chunkRange2.bottom_y);
    this.gameStore.syncEntity(entity);
    eventService.firePostUpdateEvents();
    Assert.assertEquals(chunkRange2, this.gameStore.getEntityChunk(entity.getUuid()).chunkRange);
  }
}
