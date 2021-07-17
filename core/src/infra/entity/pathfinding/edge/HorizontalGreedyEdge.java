package infra.entity.pathfinding.edge;

import com.badlogic.gdx.math.Vector2;
import infra.common.Coordinates;
import infra.entity.Entity;
import infra.entity.pathfinding.EntityStructure;
import infra.entity.pathfinding.RelativeCoordinates;
import infra.entity.pathfinding.RelativePathNode;
import infra.entity.pathfinding.RelativeVertex;

public class HorizontalGreedyEdge extends AbstractEdge {
  RelativeCoordinates currentRelativeCoordinates;

  public HorizontalGreedyEdge(
      EntityStructure entityStructure, RelativeVertex from, RelativeVertex to) {
    super(entityStructure, from, to);
    this.currentRelativeCoordinates = from.relativeCoordinates;
  }

  @Override
  public Coordinates applyTransition(Coordinates sourceCoordinates) {
    return super.applyTransition(sourceCoordinates).getBase();
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode) throws Exception {
    String actionKey;

    if (!entity.coordinates.getBase().equals(relativePathNode.startPosition.getBase())
        && !entity.coordinates.getBase().equals(relativePathNode.getEndPosition().getBase())) {
      System.out.println("NOT ON TRACK");
      System.out.println(entity.coordinates);
      System.out.println(relativePathNode.startPosition.getBase());
      System.out.println(relativePathNode.getEndPosition().getBase());
      System.out.println(relativePathNode.startPosition);
      System.out.println(relativePathNode.getEndPosition());
      System.out.println();
      throw new Exception("not on track");
    }

    if (relativePathNode.getEndPosition().calcDistance(entity.coordinates) < 0.3) {
      Vector2 setBodyPosition = relativePathNode.getEndPosition().toVector2();
      entity.getBody().setTransform(setBodyPosition, 0);
      this.finish();
      return;
    }

    if (relativePathNode.getEndPosition().getXReal() + 0.1 > entity.coordinates.getXReal()) {
      actionKey = "right";
    } else if (relativePathNode.getEndPosition().getXReal() < entity.coordinates.getXReal()) {
      actionKey = "left";
    } else return;

    entity.entityController.applyAction(actionKey, entity.getBody());
  }
}
