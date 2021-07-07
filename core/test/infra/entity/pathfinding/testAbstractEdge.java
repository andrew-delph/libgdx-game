package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import infra.entity.pathfinding.template.GreedyEdge;
import infra.entity.pathfinding.template.RelativeCoordinates;
import infra.entity.pathfinding.template.RelativeVertex;
import org.junit.Test;

public class testAbstractEdge {
  @Test
  public void testAbstractEdgeEqual() {
    GreedyEdge greedyEdge1 =
            new GreedyEdge(
                    null,
                    new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()),
                    new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()));

    GreedyEdge greedyEdge2 =
            new GreedyEdge(
                    null,
                    new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()),
                    new RelativeVertex(null, new RelativeCoordinates(0, 0), new Vector2()));

    assert greedyEdge1.equals(greedyEdge2);
  }
}
