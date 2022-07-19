package core.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.GameStore;
import core.configuration.StandAloneConfig;
import core.entity.pathfinding.edge.HorizontalGreedyEdge;
import org.junit.Test;

public class testAbstractEdge {
  @Test
  public void testAbstractEdgeEqual() {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    EntityStructure entityStructure = new EntityStructure(injector.getInstance(GameStore.class));
    HorizontalGreedyEdge horizontalGreedyEdge1 =
        new HorizontalGreedyEdge(
            entityStructure,
            new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()),
            new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()));

    HorizontalGreedyEdge horizontalGreedyEdge2 =
        new HorizontalGreedyEdge(
            entityStructure,
            new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()),
            new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()));

    assert horizontalGreedyEdge1.equals(horizontalGreedyEdge2);
  }

  @Test
  public void testAbstractEdgeNotEqual() {
    HorizontalGreedyEdge horizontalGreedyEdge1 =
        new HorizontalGreedyEdge(
            null,
            new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()),
            new RelativeVertex(null, new RelativeCoordinates(1f, 0), new Vector2()));

    HorizontalGreedyEdge horizontalGreedyEdge2 =
        new HorizontalGreedyEdge(
            null,
            new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()),
            new RelativeVertex(null, new RelativeCoordinates(1.1f, 0), new Vector2()));

    assert !horizontalGreedyEdge1.equals(horizontalGreedyEdge2);
  }
}
