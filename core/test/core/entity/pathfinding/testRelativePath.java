package core.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.GameStore;
import core.configuration.StandAloneConfig;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.generation.ChunkBuilderFactory;
import java.util.Map;
import org.junit.Test;

public class testRelativePath {
  @Test
  public void testRelativePath() throws Exception {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(5, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(10, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 5)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-6, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1)))
        .call();

    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.edgeRegistration();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    //    Coordinates replacementCoordinates = CommonFactory.createCoordinates(4, 1);
    //    Block removeBlock = gameStore.getBlock(replacementCoordinates);
    //    Block replacementBlock = blockFactory.createDirt();
    //    replacementBlock.coordinates = removeBlock.coordinates;
    //    gameStore.removeEntity(removeBlock.uuid);
    //    gameStore.addEntity(replacementBlock);
    //
    //    Coordinates replacementCoordinates2 = CommonFactory.createCoordinates(5, 2);
    //    Block removeBlock2 = gameStore.getBlock(replacementCoordinates2);
    //    Block replacementBlock2 = blockFactory.createDirt();
    //    replacementBlock2.coordinates = removeBlock2.coordinates;
    //    gameStore.removeEntity(removeBlock2.uuid);
    //    gameStore.addEntity(replacementBlock2);
    //    System.out.println(gameStore.getBlock(replacementCoordinates).getClass());
    //
    RelativePath relativePath =
        relativePathFactory.create(
            CommonFactory.createCoordinates(0.5f, 1), CommonFactory.createCoordinates(6, 1));

    System.out.println(
        ">>>>>>>>"
            + gameStore
                .getBlock(CommonFactory.createCoordinates(4.0742702f, 0.5821669f))
                .getClass());

    relativePath.search();
    System.out.println("1");
    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      //      System.out.println(pathNode);
      //      System.out.println(pathNode.edge);
      //      System.out.println(
      //          pathNode.startPosition
      //              + " , "
      //              + pathNode.edge.applyTransition(pathNode.startPosition)
      //              + ">>>"
      //              + gameStore.getBlock(pathNode.edge.applyTransition(pathNode.startPosition)));
      System.out.println(
          "..."
              + pathNode.edge.entityStructure.verifyEntityStructure(
                  new PathGameStoreOverride(), pathNode.startPosition));
    }
    System.out.println("2");
  }

  @Test
  public void testRelativePath2() throws Exception {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(5, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(10, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 5)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-6, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(6, -1)))
        .call();

    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.edgeRegistration();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    Coordinates replacementCoordinates = CommonFactory.createCoordinates(4, 0);
    Block removeBlock = gameStore.getBlock(replacementCoordinates);
    Block replacementBlock =
        blockFactory.createSky(removeBlock.getCoordinatesWrapper().getCoordinates());
    gameStore.removeEntity(removeBlock.getUuid());
    gameStore.addEntity(replacementBlock);
    //    System.out.println("1" + gameStore.getBlock(replacementCoordinates).getClass());
    //
    //    Coordinates replacementCoordinates2 = CommonFactory.createCoordinates(4, -1);
    //    Block removeBlock2 = gameStore.getBlock(replacementCoordinates2);
    //    Block replacementBlock2 = blockFactory.createSky();
    //    replacementBlock2.coordinates = removeBlock2.coordinates;
    //    gameStore.removeEntity(removeBlock2.uuid);
    //    gameStore.addEntity(replacementBlock2);
    //    System.out.println("2" + gameStore.getBlock(replacementCoordinates2).getClass());

    RelativePath relativePath =
        relativePathFactory.create(
            CommonFactory.createCoordinates(0, 1), CommonFactory.createCoordinates(5, 1));

    relativePath.search();
    System.out.println("1");
    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      //      System.out.println(pathNode);
      //      System.out.println(pathNode.edge);
      System.out.println(
          pathNode.startPosition + " , " + pathNode.edge.applyTransition(pathNode.startPosition));
    }
    System.out.println("2");
  }

  @Test
  public void testRelativePathDown() throws Exception {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(5, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(10, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 5)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-6, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(6, -1)))
        .call();

    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.horizontalGreedyRegisterEdges();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    Coordinates replacementCoordinates = CommonFactory.createCoordinates(2, 0);
    Block removeBlock = gameStore.getBlock(replacementCoordinates);
    Block replacementBlock =
        blockFactory.createSky(removeBlock.getCoordinatesWrapper().getCoordinates());
    gameStore.removeEntity(removeBlock.getUuid());
    gameStore.addEntity(replacementBlock);
    //    System.out.println("1" + gameStore.getBlock(replacementCoordinates).getClass());
    //
    Coordinates replacementCoordinates2 = CommonFactory.createCoordinates(2, -1);
    Block removeBlock2 = gameStore.getBlock(replacementCoordinates2);
    Block replacementBlock2 =
        blockFactory.createSky(removeBlock2.getCoordinatesWrapper().getCoordinates());
    gameStore.removeEntity(removeBlock2.getUuid());
    gameStore.addEntity(replacementBlock2);
    //    System.out.println("2" + gameStore.getBlock(replacementCoordinates2).getClass());

    Coordinates replacementCoordinates3 = CommonFactory.createCoordinates(2, -2);
    Block removeBlock3 = gameStore.getBlock(replacementCoordinates3);
    Block replacementBlock3 =
        blockFactory.createSky(removeBlock3.getCoordinatesWrapper().getCoordinates());
    gameStore.removeEntity(removeBlock3.getUuid());
    gameStore.addEntity(replacementBlock3);
    //    System.out.println("3" + gameStore.getBlock(replacementCoordinates3).getClass());

    RelativePath relativePath =
        relativePathFactory.create(
            CommonFactory.createCoordinates(0, 1), CommonFactory.createCoordinates(2, -2));

    relativePath.search();
    System.out.println(relativePath.finalPathNode.getEndPosition());
    System.out.println(relativePath.target);
    System.out.println(
        relativePath.finalPathNode.getEndPosition().calcDistance(relativePath.target));
    System.out.println("1");
    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      //      System.out.println(pathNode);
      //      System.out.println(pathNode.edge);
      System.out.println(
          pathNode.startPosition + " , " + pathNode.edge.applyTransition(pathNode.startPosition));
    }
    System.out.println("2");
  }

  @Test
  public void testRelativePathLadder() throws Exception {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(5, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(10, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 5)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-6, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(6, -1)))
        .call();

    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.horizontalGreedyRegisterEdges();
    edgeRegistration.ladderGreedyRegisterEdges();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    RelativePath relativePath =
        relativePathFactory.create(
            CommonFactory.createCoordinates(0, 1), CommonFactory.createCoordinates(2, 6));

    relativePath.search();
    System.out.println(relativePath.finalPathNode.getEndPosition());
    System.out.println(relativePath.target);
    System.out.println(
        relativePath.finalPathNode.getEndPosition().calcDistance(relativePath.target));
    System.out.println("1");
    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      //      System.out.println(pathNode);
      System.out.println("---");
      for (Map.Entry entry : pathNode.pathGameStoreOverride.copyMap().entrySet()) {
        System.out.println(entry.getKey());
      }
      System.out.println("***");
      System.out.println(pathNode.edge);
      System.out.println(
          pathNode.startPosition + " , " + pathNode.edge.applyTransition(pathNode.startPosition));
      System.out.println();
    }
    System.out.println("2");
  }

  @Test
  public void testRelativePathClimbLadder() throws Exception {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 6)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(5, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(10, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 5)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-6, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(6, -1)))
        .call();

    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.horizontalGreedyRegisterEdges();
    edgeRegistration.ladderGreedyRegisterEdges();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    for (int i = 0; i < 5; i++) {
      Coordinates replacementCoordinates2 = CommonFactory.createCoordinates(2, i);
      //      Block removeBlock2 = gameStore.getBlock(replacementCoordinates2);
      Block replacementBlock2 = blockFactory.createDirt(replacementCoordinates2);
      //      gameStore.removeEntity(removeBlock2.getUuid());
      gameStore.addEntity(replacementBlock2);
      System.out.println("2" + gameStore.getBlock(replacementCoordinates2).getClass());
    }

    RelativePath relativePath =
        relativePathFactory.create(
            CommonFactory.createCoordinates(0, 1), CommonFactory.createCoordinates(2, 5));

    //    System.out.println("^^^^^^^^^^^^^^");
    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 5)).getClass());
    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 4)).getClass());
    //    System.out.println("^^^^^^^^^^^^^^");

    relativePath.search();

    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      System.out.println(pathNode.edge);
    }
    System.out.println("2");
  }

  @Test
  public void testRelativePathDig() throws Exception {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 6)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(5, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(10, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 5)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-6, 0)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1)))
        .call();
    chunkBuilderFactory
        .create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(6, -1)))
        .call();

    //    System.out.println(gameStore.getBlock(CommonFactory.createCoordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.horizontalGreedyRegisterEdges();
    edgeRegistration.digGreedyRegisterEdges();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    for (int i = 0; i < 5; i++) {
      Coordinates replacementCoordinates2 = CommonFactory.createCoordinates(2, i);
      //      Block removeBlock2 = gameStore.getBlock(replacementCoordinates2);
      Block replacementBlock2 = blockFactory.createDirt(replacementCoordinates2);
      //      gameStore.removeEntity(removeBlock2.getUuid());
      gameStore.addEntity(replacementBlock2);
      System.out.println("2" + gameStore.getBlock(replacementCoordinates2).getClass());
    }

    RelativePath relativePath =
        relativePathFactory.create(
            CommonFactory.createCoordinates(0, 1), CommonFactory.createCoordinates(5, 1));

    relativePath.search();

    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      System.out.println(pathNode.edge);
    }
    System.out.println("2");
  }
}
