package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.EntityControllerFactory;
import infra.generation.ChunkBuilderFactory;
import org.junit.Test;

public class testVertex {

  @Test
  public void testVertex() {
    Injector injector = Guice.createInjector(new SoloConfig());

    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Entity entity = entityFactory.createEntity();
    Coordinates coordinates = new Coordinates(0, 0);
    Vector2 vector2 = new Vector2(0, 0);

    assert new Vertex(entity, coordinates, vector2)
        .equals(new Vertex(entity, new Coordinates(0, 0), new Vector2(0, 0)));
  }

  @Test
  public void testGeneration() throws Exception {
    Injector injector = Guice.createInjector(new SoloConfig());

    Graph graph = injector.getInstance(Graph.class);
    VertexFactory vertexFactory = injector.getInstance(VertexFactory.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    EntityControllerFactory entityControllerFactory = injector.getInstance(EntityControllerFactory.class);

    Entity entity = entityFactory.createEntity();
    entity.setController(entityControllerFactory.createEntityController(entity));
    Coordinates coordinates = new Coordinates(0, 1);
    Vector2 vector2 = new Vector2(0, 0);

    Vertex vertex = vertexFactory.createVertex(entity, coordinates, vector2);
    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);

    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, -1))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, -1))).call();

    graph.registerVertex(vertex);
    vertex.exploreEdges();

    assert graph.getEdges(vertex).size() > 0;

    Vertex current = null;
    for (Edge edge : graph.getEdges(vertex)) {
      current = edge.to;
      System.out.println(edge.to.position);
      edge.to.exploreEdges();
    }

    for (Edge edge : graph.getEdges(current)) {
      System.out.println(edge.to.position);
      //      edge.to.exploreEdges();
    }
  }
}
