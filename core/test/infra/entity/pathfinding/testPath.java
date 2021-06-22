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
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;
import infra.entity.controllers.EntityControllerFactory;
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
    EntityControllerFactory entityControllerFactory =
        injector.getInstance(EntityControllerFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);

    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(5, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0,5))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, -1))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, -1))).call();

    //    System.out.println(gameStore.getBlock(new Coordinates(3,1)).getClass());
    //    System.out.println(gameStore.getBlock(new Coordinates(3,0)).getClass());

    Coordinates start = new Coordinates(0, 1);
    Coordinates end = new Coordinates(3, 1);
    Coordinates block = new Coordinates(2, 1);

    Block removeBlock = gameStore.getBlock(block);
    gameStore.removeEntity(removeBlock.uuid);
    Block newBlock = blockFactory.createDirt();
    newBlock.coordinates = block;
    gameStore.addEntity(newBlock);

    //    System.out.println(gameStore.getBlock(new Coordinates(3,1)).getClass());

    Graph graph = injector.getInstance(Graph.class);
    Entity entity = entityFactory.createEntity();
    entity.setController(entityControllerFactory.createEntityController(entity));
    entity.coordinates = start;

    Vertex startVertex = vertexFactory.createVertex(entity, entity.coordinates, new Vector2(0, 0));
    graph.registerVertex(startVertex);

    Path path =
        pathFactory.createPath(
            startVertex, vertexFactory.createVertex(entity, end, new Vector2(0, 0)));

    path.search();

    for (Edge edge : path.getPathEdgeList()) {
      System.out.println(edge.actionKey+", "+edge.to.position);
    }
  }
}
