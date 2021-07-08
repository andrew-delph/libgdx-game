package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import infra.common.Coordinates;
import infra.entity.Entity;

public class GreedyEdge extends AbstractEdge {
  RelativeCoordinates currentRelativeCoordinates;

  public GreedyEdge(BlockStructure blockStructure, RelativeVertex from, RelativeVertex to) {
    super(blockStructure, from, to);
    this.currentRelativeCoordinates = from.relativeCoordinates;
  }

  @Override
  public Coordinates applyTransition(Coordinates sourceCoordinates) {
    return super.applyTransition(sourceCoordinates).getBase();
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode) {
    String actionKey;

    if (relativePathNode.target.calcDistance(entity.coordinates) < 0.2) {
      Vector2 setBodyPosition = relativePathNode.target.toVector2();
      entity.getBody().setTransform(setBodyPosition, 0);
      this.finish();
      return;
    }

    if (relativePathNode.target.getXReal() > entity.coordinates.getXReal()) {
      actionKey = "right";
    } else {
      actionKey = "left";
    }

    entity.entityController.applyAction(actionKey, entity.getBody());
  }
}
