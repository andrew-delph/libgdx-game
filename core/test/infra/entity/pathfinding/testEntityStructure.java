package infra.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.EntityFactory;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;
import infra.entity.misc.Ladder;
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

    assert entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), new Coordinates(0, 0));
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

    assert entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), new Coordinates(0, 0));
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

    assert !entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), new Coordinates(0, 0));
  }

  @Test
  public void testPathGameStoreOverride() {
    Injector injector = Guice.createInjector(new SoloConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Ladder ladder = entityFactory.createLadder();
    ladder.coordinates = new Coordinates(0, 0);

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), Ladder.class);

    PathGameStoreOverride gameStoreOverride = new PathGameStoreOverride();
    gameStoreOverride.registerEntityTypeOverride(Ladder.class, new Coordinates(0, 0));

    assert entityStructure.verifyEntityStructure(gameStoreOverride, new Coordinates(0, 0));
  }
}
