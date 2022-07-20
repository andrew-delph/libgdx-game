package core.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.common.CommonFactory;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.configuration.StandAloneConfig;
import core.entity.EntityFactory;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.entity.block.EmptyBlock;
import core.entity.block.SolidBlock;
import core.entity.misc.Ladder;
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

    gameStore.addChunk(chunkFactory.create(new ChunkRange(CommonFactory.createCoordinates(0, 0))));

    Block block = blockFactory.createSky(CommonFactory.createCoordinates(0, 0));
    gameStore.addEntity(block);

    assert gameStore.getBlock(CommonFactory.createCoordinates(0, 0)) != null;

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);

    assert entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), CommonFactory.createCoordinates(0, 0));
  }

  @Test
  public void testRelativeBlockRegisterAbove() throws ChunkNotFound {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(CommonFactory.createCoordinates(0, 0))));

    Block block = blockFactory.createSky(CommonFactory.createCoordinates(0, 3));
    gameStore.addEntity(block);

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 3), EmptyBlock.class);

    assert entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), CommonFactory.createCoordinates(0, 0));
  }

  @Test
  public void testRelativeBlockRegisterNegative() throws EntityNotFound, ChunkNotFound {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(CommonFactory.createCoordinates(0, 0))));

    Block block = blockFactory.createSky(CommonFactory.createCoordinates(0, 0));
    gameStore.addEntity(block);

    assert gameStore.getBlock(CommonFactory.createCoordinates(0, 0)) != null;

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), SolidBlock.class);

    assert !entityStructure.verifyEntityStructure(
        new PathGameStoreOverride(), CommonFactory.createCoordinates(0, 0));
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
    gameStore.addChunk(chunkFactory.create(new ChunkRange(CommonFactory.createCoordinates(0, 0))));

    Ladder ladder = entityFactory.createLadder(CommonFactory.createCoordinates(0, 0));

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), Ladder.class);

    PathGameStoreOverride gameStoreOverride = new PathGameStoreOverride();
    gameStoreOverride.registerEntityTypeOverride(
        Ladder.class, CommonFactory.createCoordinates(0, 0));

    assert entityStructure.verifyEntityStructure(
        gameStoreOverride, CommonFactory.createCoordinates(0, 0));
  }
}
