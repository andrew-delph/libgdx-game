package infra.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;
import infra.entity.pathfinding.template.EntityStructure;
import infra.entity.pathfinding.template.EntityStructureFactory;
import infra.entity.pathfinding.template.RelativeCoordinates;
import org.junit.Test;

public class testEntityStructure {
  @Test
  public void testRelativeBlockRegister() {
    Injector injector = Guice.createInjector(new SoloConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Block block = blockFactory.createSky();
    block.coordinates = new Coordinates(0, 0);
    gameStore.addEntity(block);

    assert gameStore.getBlock(new Coordinates(0, 0)) != null;

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);

    assert entityStructure.verifyEntityStructure(new Coordinates(0, 0));
  }

  @Test
  public void testRelativeBlockRegisterAbove() {
    Injector injector = Guice.createInjector(new SoloConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Block block = blockFactory.createSky();
    block.coordinates = new Coordinates(0, 3);
    gameStore.addEntity(block);

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 3), EmptyBlock.class);

    assert entityStructure.verifyEntityStructure(new Coordinates(0, 0));
  }

  @Test
  public void testRelativeBlockRegisterNegative() {
    Injector injector = Guice.createInjector(new SoloConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Block block = blockFactory.createSky();
    block.coordinates = new Coordinates(0, 0);
    gameStore.addEntity(block);

    assert gameStore.getBlock(new Coordinates(0, 0)) != null;

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), SolidBlock.class);

    assert !entityStructure.verifyEntityStructure(new Coordinates(0, 0));
  }
}
