package core.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.game.GameController;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class TestGameStore {

  Injector injector;
  GameStore gameStore;
  GameController gameController;

  EntityFactory entityFactory;

  ChunkFactory chunkFactory;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    gameStore = injector.getInstance(GameStore.class);
    gameController = injector.getInstance(GameController.class);
    entityFactory = injector.getInstance(EntityFactory.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
  }

  @Test
  public void testEntityExistence() throws EntityNotFound, ChunkNotFound {
    Entity testEntity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));
    this.gameStore.addChunk(
        this.chunkFactory.create(new ChunkRange(CommonFactory.createCoordinates(0, 0))));
    gameStore.addEntity(testEntity);
    assert testEntity == gameStore.getEntity(testEntity.getUuid());
  }

  @Test
  public void testChunkExistence() {
    Chunk chunk = this.chunkFactory.create(new ChunkRange(CommonFactory.createCoordinates(0, 0)));
    this.gameStore.addChunk(chunk);
    assert chunk == gameStore.getChunk(new ChunkRange(CommonFactory.createCoordinates(0, 0)));
  }
}
