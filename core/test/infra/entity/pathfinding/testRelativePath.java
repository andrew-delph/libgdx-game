package infra.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.entity.pathfinding.template.*;
import infra.generation.ChunkBuilderFactory;
import org.junit.Test;

public class testRelativePath {
  @Test
  public void testRelativePath() throws Exception {
    Injector injector = Guice.createInjector(new SoloConfig());

    RelativePathFactory relativePathFactory = injector.getInstance(RelativePathFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(5, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 5))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, -1))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, -1))).call();

    EdgeRegistration edgeRegistration =
            injector.getInstance(EdgeRegistration.class);
    edgeRegistration.edgeRegistration();

    RelativePath relativePath =
        relativePathFactory.create(new Coordinates(0, 1), new Coordinates(5, 1));

    relativePath.search();
    System.out.println("1");
    for (RelativePathNode edge : relativePath.getPathEdgeList()) {
      System.out.println(edge);
      System.out.println(edge.edge.getClass());
    }
    System.out.println("2");
  }
}
