package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
  public void follow(Entity entity) {
    String actionKey;

    // if difference is too little, just set.
    RelativeCoordinates difference = currentRelativeCoordinates.sub(this.to.relativeCoordinates);
    if (Math.abs(difference.relativeX) < 0.2) {
      Vector2 setBodyPosition = entity.getBody().getPosition().cpy().add(difference.toVector2());
      entity.getBody().setTransform(setBodyPosition, 0);
      this.finish();
      return;
    }

    if (currentRelativeCoordinates.relativeX < to.relativeCoordinates.relativeX) {
      actionKey = "right";
    } else {
      actionKey = "left";
    }

    Vector2 startingPosition = entity.getBody().getPosition().cpy();
    entity.entityController.applyAction(actionKey, entity.getBody());
    Vector2 endingPosition = entity.getBody().getPosition().cpy();

    Vector2 differencePosition = endingPosition.sub(startingPosition);

    currentRelativeCoordinates =
        currentRelativeCoordinates.add(new RelativeCoordinates(differencePosition));
  }
}
