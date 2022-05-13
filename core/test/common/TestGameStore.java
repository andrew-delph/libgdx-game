package common;

import app.GameController;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.Coordinates;
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
    Entity testEntity = entityFactory.createEntity(new Coordinates(0, 0));
    this.gameStore.addChunk(this.chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
    gameStore.addEntity(testEntity);
    assert testEntity == gameStore.getEntity(testEntity.getUuid());
  }

  @Test
  public void testChunkExistence() {
    Chunk chunk = this.chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));
    this.gameStore.addChunk(chunk);
    assert chunk == gameStore.getChunk(new ChunkRange(new Coordinates(0, 0)));
  }
}
