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

    if (relativePathNode.getEndPosition().calcDistance(entity.coordinates) < 0.1) {
      Vector2 setBodyPosition = relativePathNode.getEndPosition().toVector2();
      entity.getBody().setTransform(setBodyPosition, 0);
      this.finish();
      return;
    }
    //    System.out.println(entity.coordinates + " ... " + relativePathNode.target);
    if (relativePathNode.getEndPosition().getXReal() > entity.coordinates.getXReal()) {
      actionKey = "right";
    } else if (relativePathNode.getEndPosition().getXReal() < entity.coordinates.getXReal()) {
      actionKey = "left";
    } else return;

    entity.entityController.applyAction(actionKey, entity.getBody());
  }
}
