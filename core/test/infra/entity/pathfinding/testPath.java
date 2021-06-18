package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.block.BlockFactory;
import infra.generation.ChunkBuilderFactory;
import org.junit.Test;

public class testPath {

  @Test
  public void testPath() throws Exception {
    Injector injector = Guice.createInjector(new SoloConfig());

    GameStore gameStore = injector.getInstance(GameStore.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    PathFactory pathFactory = injector.getInstance(PathFactory.class);
    VertexFactory vertexFactory = injector.getInstance(VertexFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);

    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(5, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, -1))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, -1))).call();

    // create a wall
    //    Block removeBlock = gameStore.getBlock(new Coordinates(-1,1));
    //    gameStore.removeEntity(removeBlock.uuid);
    //    Block

    Graph graph = injector.getInstance(Graph.class);
    Entity entity = entityFactory.createEntity();
    entity.coordinates = new Coordinates(0, 1);

    graph.registerVertex(
        vertexFactory.createVertex(entity, new Coordinates(0, 1), new Vector2(0, 0)));

    Path path =
        pathFactory.createPath(
            vertexFactory.createVertex(entity, new Coordinates(0, 1), new Vector2(0, 0)),
            vertexFactory.createVertex(entity, new Coordinates(3, 1), new Vector2(0, 0)));

    path.search();
  }
}
