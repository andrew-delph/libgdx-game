package entity.pathfinding;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import configuration.StandAloneConfig;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.EmptyBlock;
import entity.block.SolidBlock;
import entity.misc.Ladder;
import org.junit.Test;

public class testEntityStructure {
  @Test
  public void testRelativeBlockRegister() throws EntityNotFound, ChunkNotFound {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Block block = blockFactory.createSky(new Coordinates(0, 0));
    gameStore.addEntity(block);

    assert gameStore.getBlock(new Coordinates(0, 0)) != null;

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);

    assert entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), new Coordinates(0, 0));
  }

  @Test
  public void testRelativeBlockRegisterAbove() throws ChunkNotFound {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Block block = blockFactory.createSky(new Coordinates(0, 3));
    gameStore.addEntity(block);

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 3), EmptyBlock.class);

    assert entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), new Coordinates(0, 0));
  }

  @Test
  public void testRelativeBlockRegisterNegative() throws EntityNotFound, ChunkNotFound {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Block block = blockFactory.createSky(new Coordinates(0, 0));
    gameStore.addEntity(block);

    assert gameStore.getBlock(new Coordinates(0, 0)) != null;

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), SolidBlock.class);

    assert !entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), new Coordinates(0, 0));
  }

  @Test
  public void testPathGameStoreOverride() {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Ladder ladder = entityFactory.createLadder(new Coordinates(0, 0));

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), Ladder.class);

    PathGameStoreOverride gameStoreOverride = new PathGameStoreOverride();
    gameStoreOverride.registerEntityTypeOverride(Ladder.class, new Coordinates(0, 0));

    assert entityStructure.verifyEntityStructure(gameStoreOverride, new Coordinates(0, 0));
  }
}
