package core.entity.statemachine.states;

import core.app.screen.assets.animations.AnimationState;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.statemachine.EntityStateMachineNodeInterface;

public class DefaultState extends EntityStateMachineNodeInterface {

  @Override
  public void callAnimation(Entity entity) {
    if (!entity.getAnimationStateWrapper().getAnimationState().equals(AnimationState.DEFAULT)) {
      try {
        gameController.updateEntityAttribute(
            entity.getUuid(), new AnimationStateWrapper(AnimationState.DEFAULT));
      } catch (EntityNotFound e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void callAction(Entity entity, float timeInState) {}
}
