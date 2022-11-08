package core.entity.statemachine.states;

import core.app.game.GameController;
import core.app.screen.assets.animations.AnimationState;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.statemachine.EntityStateMachineNodeInterface;

public class RightWalkingState implements EntityStateMachineNodeInterface {
  GameController gameController;

  @Override
  public void callAnimation(Entity entity) {
    if (!entity
        .getAnimationStateWrapper()
        .getAnimationState()
        .equals(AnimationState.WALKING_RIGHT)) {
      try {
        gameController.updateEntityAttribute(
            entity.getUuid(), new AnimationStateWrapper(AnimationState.WALKING_RIGHT));
      } catch (EntityNotFound e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void callAction(Entity entity, float timeInState) {}
}
