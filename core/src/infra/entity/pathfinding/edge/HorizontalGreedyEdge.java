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
  public void follow(Entity entity, RelativePathNode relativePathNode) {
    String actionKey;

    if (relativePathNode.getEndPosition().calcDistance(entity.coordinates) < 0.1) {
      Vector2 setBodyPosition = relativePathNode.getEndPosition().toVector2();
      entity.getBody().setTransform(setBodyPosition, 0);
      this.finish();
      return;
    }
    //    System.out.println(entity.coordinates + " ... " + relativePathNode.target);
    if (relativePathNode.getEndPosition().getYReal() + 0.2 < entity.coordinates.getYReal()) {
      return;
    }

    if (relativePathNode.getEndPosition().getXReal() > entity.coordinates.getXReal()) {
      actionKey = "right";
    } else if (relativePathNode.getEndPosition().getXReal() < entity.coordinates.getXReal()) {
      actionKey = "left";
    } else return;

    entity.entityController.applyAction(actionKey, entity.getBody());
  }
}
