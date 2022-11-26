package core.entity.statemachine.states;

import core.app.screen.assets.animations.AnimationState;
import core.common.Direction;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.statemachine.EntityStateMachineNodeInterface;

public class DefaultItemState extends EntityStateMachineNodeInterface {

  @Override
  public void callAnimation(Entity entity) {
    AnimationState state;
    if (entity.getDirectionWrapper().getDirection() == Direction.LEFT) {
      state = AnimationState.PUNCH_LEFT;
    } else {
      state = AnimationState.PUNCH_RIGHT;
    }

    if (!entity.getAnimationStateWrapper().getAnimationState().equals(state)) {
      try {
        gameController.updateEntityAttribute(entity.getUuid(), new AnimationStateWrapper(state));
      } catch (EntityNotFound e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void callAction(Entity entity, long timeInState) {
    if (timeInState > 150) {
      gameController.useItem(entity);
    }

    if (timeInState > 500) {
      entity.getEntityStateMachine().setState(AnimationState.DEFAULT);
    }
  }
}
