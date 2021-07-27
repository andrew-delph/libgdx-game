package chunk;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import common.Coordinates;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import org.junit.Test;

public class testChunk {

  @Test
  public void testGetBlock() {
    Injector injector = Guice.createInjector(new ClientConfig());
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    Chunk chunk = chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    Entity entity = entityFactory.createEntity();
    assert entity.coordinates.equals(new Coordinates(0, 0));
    chunk.addEntity(entity);
    assert chunk.getBlock(new Coordinates(0, 0)) == null;
    Block dirtBlock = blockFactory.createDirt();
    assert dirtBlock.coordinates.equals(new Coordinates(0, 0));
    chunk.addEntity(dirtBlock);
    assert chunk.getBlock(new Coordinates(0, 0)).equals(dirtBlock);
  }
}
