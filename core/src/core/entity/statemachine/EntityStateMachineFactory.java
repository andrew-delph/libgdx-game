package core.entity.statemachine;

import com.google.common.collect.Sets;
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
import java.util.Set;

public class EntityStateMachineFactory {
  @Inject DefaultItemState defaultItemState;
  @Inject DefaultState defaultState;
  @Inject LeftWalkingState leftWalkingState;
  @Inject RightWalkingState rightWalkingState;
  @Inject JumpState jumpState;

  public EntityStateMachine createEntityStateMachine(Entity entity) {

    Map<AnimationState, EntityStateMachineNodeInterface> stateToNode = new HashMap<>();
    stateToNode.put(AnimationState.DEFAULT, defaultState);
    stateToNode.put(AnimationState.PUNCH_LEFT, defaultItemState);
    stateToNode.put(AnimationState.PUNCH_RIGHT, defaultItemState);
    stateToNode.put(AnimationState.WALKING_LEFT, leftWalkingState);
    stateToNode.put(AnimationState.WALKING_RIGHT, rightWalkingState);
    stateToNode.put(AnimationState.JUMPING, jumpState);

    Map<AnimationState, Set<AnimationState>> transitions = new HashMap<>();

    Set<AnimationState> allStates =
        Sets.newHashSet(
            AnimationState.DEFAULT,
            AnimationState.PUNCH_LEFT,
            AnimationState.PUNCH_RIGHT,
            AnimationState.WALKING_LEFT,
            AnimationState.WALKING_RIGHT,
            AnimationState.JUMPING);

    transitions.put(AnimationState.DEFAULT, allStates);
    transitions.put(AnimationState.WALKING_LEFT, allStates);
    transitions.put(AnimationState.WALKING_RIGHT, allStates);
    transitions.put(AnimationState.JUMPING, allStates);
    transitions.put(AnimationState.PUNCH_LEFT, Sets.newHashSet());
    transitions.put(AnimationState.PUNCH_RIGHT, Sets.newHashSet());

    return new EntityStateMachine(entity, transitions, stateToNode);
  }
}
