package core.entity.statemachine.states;

import core.app.screen.assets.animations.AnimationState;
import core.common.Direction;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.attributes.msc.DirectionWrapper;
import core.entity.statemachine.EntityStateMachineNodeInterface;

public class LeftWalkingState extends EntityStateMachineNodeInterface {

  @Override
  public void callAnimation(Entity entity) {
    if (!entity
        .getAnimationStateWrapper()
        .getAnimationState()
        .equals(AnimationState.WALKING_LEFT)) {
      try {
        this.gameController.updateEntityAttribute(
            entity.getUuid(), new AnimationStateWrapper(AnimationState.WALKING_LEFT));
        this.gameController.updateEntityAttribute(
            entity.getUuid(), new DirectionWrapper(Direction.LEFT));
      } catch (EntityNotFound e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void callAction(Entity entity, long timeInState) {}
}
