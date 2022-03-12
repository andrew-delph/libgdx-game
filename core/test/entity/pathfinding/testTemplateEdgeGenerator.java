package entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import configuration.StandAloneConfig;
import entity.pathfinding.edge.AbstractEdge;
import org.junit.Test;

public class testTemplateEdgeGenerator {
  @Test
  public void testTemplateEdgeGenerator() {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    TemplateEdgeGeneratorFactory templateEdgeGeneratorFactory =
        injector.getInstance(TemplateEdgeGeneratorFactory.class);

    EdgeStore edgeStore = injector.getInstance(EdgeStore.class);

    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    TemplateEdgeGenerator templateEdgeGenerator =
        templateEdgeGeneratorFactory.create(
            new RelativeVertex(
                entityStructureFactory.createEntityStructure(),
                new RelativeCoordinates(0, 0),
                new Vector2(0, 0)));

    templateEdgeGenerator.generate();

    System.out.println(edgeStore.getEdgeList().size());

    for (AbstractEdge edge : edgeStore.getEdgeList()) {
      if (edge.isAvailable(new PathGameStoreOverride(), new Coordinates(0, 0))
          && edge.getTo().getRelativeCoordinates().getRelativeY() < 0) {
        Coordinates to =
            edge.getTo().getRelativeCoordinates().applyRelativeCoordinates(new Coordinates(0, 0));
        System.out.println(to);

        //        System.out.println(edge);
      }
    }
  }
}
