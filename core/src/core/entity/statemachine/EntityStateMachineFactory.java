package core.entity.statemachine;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import core.app.screen.assets.animations.AnimationState;
import core.entity.Entity;
import core.entity.statemachine.states.DefaultItemState;
import core.entity.statemachine.states.DefaultState;
import core.entity.statemachine.states.DiggingDownState;
import core.entity.statemachine.states.DiggingLeftState;
import core.entity.statemachine.states.DiggingRightState;
import core.entity.statemachine.states.DiggingUpState;
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
  @Inject DiggingLeftState diggingLeftState;
  @Inject DiggingRightState diggingRightState;
  @Inject DiggingDownState diggingDownState;
  @Inject DiggingUpState diggingUpState;

  public EntityStateMachine createEntityStateMachine(Entity entity) {

    Map<AnimationState, EntityStateMachineNodeInterface> stateToNode = new HashMap<>();
    stateToNode.put(AnimationState.DEFAULT, defaultState);
    stateToNode.put(AnimationState.PUNCH_LEFT, defaultItemState);
    stateToNode.put(AnimationState.PUNCH_RIGHT, defaultItemState);
    stateToNode.put(AnimationState.WALKING_LEFT, leftWalkingState);
    stateToNode.put(AnimationState.WALKING_RIGHT, rightWalkingState);
    stateToNode.put(AnimationState.JUMPING, jumpState);
    stateToNode.put(AnimationState.DIGGING_LEFT, diggingLeftState);
    stateToNode.put(AnimationState.DIGGING_RIGHT, diggingRightState);
    stateToNode.put(AnimationState.DIGGING_UP, diggingUpState);
    stateToNode.put(AnimationState.DIGGING_DOWN, diggingDownState);

    Map<AnimationState, Set<AnimationState>> transitions = new HashMap<>();

    Set<AnimationState> allStates =
        Sets.newHashSet(
            AnimationState.DEFAULT,
            AnimationState.PUNCH_LEFT,
            AnimationState.PUNCH_RIGHT,
            AnimationState.WALKING_LEFT,
            AnimationState.WALKING_RIGHT,
            AnimationState.JUMPING,
            AnimationState.DIGGING_LEFT,
            AnimationState.DIGGING_RIGHT,
            AnimationState.DIGGING_DOWN,
            AnimationState.DIGGING_UP);

    transitions.put(AnimationState.DEFAULT, allStates);
    transitions.put(AnimationState.WALKING_LEFT, allStates);
    transitions.put(AnimationState.WALKING_RIGHT, allStates);
    transitions.put(AnimationState.JUMPING, allStates);
    transitions.put(AnimationState.PUNCH_LEFT, Sets.newHashSet());
    transitions.put(AnimationState.PUNCH_RIGHT, Sets.newHashSet());
    transitions.put(AnimationState.DIGGING_LEFT, Sets.newHashSet());
    transitions.put(AnimationState.DIGGING_RIGHT, Sets.newHashSet());
    transitions.put(AnimationState.DIGGING_DOWN, Sets.newHashSet());
    transitions.put(AnimationState.DIGGING_UP, Sets.newHashSet());

    return new EntityStateMachine(entity, transitions, stateToNode);
  }
}
