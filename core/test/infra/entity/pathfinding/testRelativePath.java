package infra.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;
import infra.entity.pathfinding.template.EdgeRegistration;
import infra.entity.pathfinding.template.RelativePath;
import infra.entity.pathfinding.template.RelativePathFactory;
import infra.entity.pathfinding.template.RelativePathNode;
import infra.generation.ChunkBuilderFactory;
import org.junit.Test;

public class testRelativePath {
  @Test
  public void testRelativePath() throws Exception {
    Injector injector = Guice.createInjector(new SoloConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    GameStore gameStore = injector.getInstance(GameStore.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(5, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(10, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 5))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, -1))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-6, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, -1))).call();

    System.out.println(gameStore.getBlock(new Coordinates(2, 1)).getClass());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.edgeRegistration();

    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    Coordinates replacementCoordinates = new Coordinates(4,1);
    Block removeBlock = gameStore.getBlock(replacementCoordinates);
    Block replacementBlock = blockFactory.createDirt();
    replacementBlock.coordinates = removeBlock.coordinates;
    gameStore.removeEntity(replacementBlock.uuid);
    gameStore.addEntity(replacementBlock);

    Coordinates replacementCoordinates2 = new Coordinates(6,1);
    Block removeBlock2 = gameStore.getBlock(replacementCoordinates2);
    Block replacementBlock2 = blockFactory.createDirt();
    replacementBlock2.coordinates = removeBlock2.coordinates;
    gameStore.removeEntity(replacementBlock2.uuid);
    gameStore.addEntity(replacementBlock2);

    System.out.println(gameStore.getBlock(replacementCoordinates).getClass());
//
    RelativePath relativePath =
        relativePathFactory.create(new Coordinates(0, 1), new Coordinates(9, 1));

    relativePath.search();
    System.out.println("1");
    for (RelativePathNode pathNode : relativePath.getPathEdgeList()) {
      System.out.println(pathNode);
      System.out.println(pathNode.edge);
      System.out.println(
          pathNode.currentPosition
              + " , "
              + pathNode.edge.applyTransition(pathNode.currentPosition));
      System.out.println(pathNode.getPrevious());

      if (pathNode.getPrevious() != null) {
        RelativePathNode previous = pathNode.getPrevious();
        System.out.println(
            previous
                .edge
                .applyTransition(previous.currentPosition)
                .equals(pathNode.currentPosition));
      }

      System.out.println();
    }
    System.out.println("2");
  }
}
