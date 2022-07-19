package core.entity.collision;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.configuration.StandAloneConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.BlockFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testRayCastService {

  Injector injector;
  RayCastService rayCastService;

  @Before
  public void setup() {
    injector = Guice.createInjector(new StandAloneConfig());
    rayCastService = injector.getInstance(RayCastService.class);
  }

  @Test
  public void testIntersectionInRange() {
    Assert.assertEquals(Arrays.asList(5, 10), rayCastService.intersectionInRange(3, 13, 5));
    Assert.assertEquals(Arrays.asList(5, 10), rayCastService.intersectionInRange(3.1f, 13.5f, 5));

    Assert.assertEquals(Arrays.asList(-5, 0, 5), rayCastService.intersectionInRange(-7, 6, 5));
    Assert.assertEquals(Arrays.asList(-5, 0, 5), rayCastService.intersectionInRange(6, -7, 5));

    Assert.assertEquals(Arrays.asList(5), rayCastService.intersectionInRange(4.9f, 5.1f, 5));
    Assert.assertEquals(Arrays.asList(-5), rayCastService.intersectionInRange(-4.9f, -5.1f, 5));

    Assert.assertEquals(Arrays.asList(0), rayCastService.intersectionInRange(-1, 1, 5));

    Assert.assertEquals(Arrays.asList(-5, 0), rayCastService.intersectionInRange(-6, 1, 5));
    Assert.assertEquals(Arrays.asList(0, 5), rayCastService.intersectionInRange(-1, 6, 5));
  }

  @Test
  public void testGetChunkRangesOnLine() {
    Set<ChunkRange> answer =
        new HashSet<>(
            Arrays.asList(
                new ChunkRange(new Coordinates(0, 0)),
                new ChunkRange(new Coordinates(5, 0)),
                new ChunkRange(new Coordinates(10, 0)),
                new ChunkRange(new Coordinates(10, 5))));

    Assert.assertEquals(
        answer,
        rayCastService.getChunkRangesOnLine(
            new Coordinates(2.2f, 2.6f), new Coordinates(13.27f, 5.619f)));
  }

  @Test
  public void testRayCast() throws ChunkNotFound {
    GameStore gameStore = injector.getInstance(GameStore.class);
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    RayCastService rayCastService = injector.getInstance(RayCastService.class);

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));

    Entity dirt = blockFactory.createDirt(new Coordinates(2, 1));
    Entity stone = blockFactory.createStone(new Coordinates(4, 1));

    gameStore.addEntity(blockFactory.createSky(new Coordinates(3, 1)));
    gameStore.addEntity(stone);
    gameStore.addEntity(dirt);

    Set<Entity> rayCastResult =
        rayCastService.rayCast(
            (new Coordinates(-1, 1)).getMiddle(), (new Coordinates(5, 1)).getMiddle());

    assert rayCastResult.contains(dirt);
    assert rayCastResult.contains(stone);
    assert rayCastResult.size() == 2;

    rayCastResult =
        rayCastService.rayCast(
            (new Coordinates(4, -5)).getMiddle(), (new Coordinates(4, 5)).getMiddle());

    assert rayCastResult.contains(stone);
    assert rayCastResult.size() == 1;

    gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(5, 0))));

    Entity dirt2 = blockFactory.createDirt(new Coordinates(7, 1));
    gameStore.addEntity(dirt2);

    rayCastResult =
        rayCastService.rayCast(
            (new Coordinates(-1, 1)).getMiddle(), (new Coordinates(11, 1)).getMiddle());

    assert rayCastResult.contains(dirt);
    assert rayCastResult.contains(stone);
    assert rayCastResult.contains(dirt2);
    assert rayCastResult.size() == 3;
  }
}
