package infra.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.pathfinding.template.AbstractEdge;
import infra.entity.pathfinding.template.EdgeRegistration;
import infra.entity.pathfinding.template.EdgeStore;
import infra.generation.ChunkBuilderFactory;
import org.junit.Test;

public class testEdgeStore {

  @Test
  public void testEdgeStore() throws Exception {
    Injector injector = Guice.createInjector(new SoloConfig());

    EdgeStore edgeStore = injector.getInstance(EdgeStore.class);

    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    ChunkBuilderFactory chunkBuilderFactory = injector.getInstance(ChunkBuilderFactory.class);
    GameStore gameStore = injector.getInstance(GameStore.class);

    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(5, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, 5))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(0, -1))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, 0))).call();
    chunkBuilderFactory.create(new ChunkRange(new Coordinates(-1, -1))).call();

    Entity entity = entityFactory.createEntity();
    entity.coordinates = new Coordinates(0.5f, 1);
    gameStore.addEntity(entity);

    System.out.println(entity.getBody());

    EdgeRegistration edgeRegistration = injector.getInstance(EdgeRegistration.class);
    edgeRegistration.templateEdgeRegistration();

    for (AbstractEdge edge : edgeStore.getEdgeList()) {
      //      System.out.println(edge.isAvailable(entity.getBody()));
      System.out.println(edge);
    }
  }
}
