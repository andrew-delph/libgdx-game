package core.entity.statemachine;

import com.google.inject.Inject;
import core.app.screen.assets.animations.AnimationState;
import core.entity.Entity;
import core.entity.statemachine.states.DefaultItemState;
import core.entity.statemachine.states.DefaultState;
import core.entity.statemachine.states.JumpState;
import core.entity.statemachine.states.LeftWalkingState;
import core.entity.statemachine.states.RightWalkingState;
import java.util.HashMap;
import java.util.Map;

public class EntityStateMachineFactory {
  @Inject DefaultItemState defaultItemState;
  @Inject DefaultState defaultState;
  @Inject LeftWalkingState leftWalkingState;
  @Inject RightWalkingState rightWalkingState;
  @Inject JumpState jumpState;

  public EntityStateMachine createEntityStateMachine(Entity entity) {
    Map<AnimationState, EntityStateMachineNodeInterface> children = new HashMap<>();
    children.put(AnimationState.DEFAULT, defaultState);
    children.put(AnimationState.ATTACKING, defaultItemState);
    children.put(AnimationState.WALKING_LEFT, leftWalkingState);
    children.put(AnimationState.WALKING_RIGHT, rightWalkingState);
    children.put(AnimationState.JUMPING, jumpState);
    return new EntityStateMachine(entity, children);
  }
}
