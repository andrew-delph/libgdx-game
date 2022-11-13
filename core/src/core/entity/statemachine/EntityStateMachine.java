package core.entity.statemachine;

import core.app.screen.assets.animations.AnimationState;
import core.entity.Entity;
import java.util.Map;
import java.util.Set;

public class EntityStateMachine {
  Map<AnimationState, Set<AnimationState>> transitions;
  Map<AnimationState, EntityStateMachineNodeInterface> stateToNode;
  long stateStartTime = 0;
  private AnimationState currentState;
  private Entity entity;

  public EntityStateMachine(
      Entity entity,
      Map<AnimationState, Set<AnimationState>> transitions,
      Map<AnimationState, EntityStateMachineNodeInterface> stateToNode) {
    this.transitions = transitions;
    this.stateToNode = stateToNode;
    this.entity = entity;
    currentState = AnimationState.DEFAULT;
  }

  public void attemptTransition(AnimationState transition) {
    Set<AnimationState> possibleTransitions = transitions.get(currentState);
    if (possibleTransitions == null) return;
    if (possibleTransitions.contains(transition)) {
      this.setState(transition);
    }
  }

  public void setState(AnimationState transition) {
    currentState = transition;
    stateStartTime = System.currentTimeMillis();
  }

  public void callAnimation() {
    if (currentState == null) return;
    if (stateToNode.get(currentState) == null) return;
    stateToNode.get(currentState).callAnimation(entity);
  }

  public void callAction() {
    if (currentState == null) return;
    if (stateToNode.get(currentState) == null) return;
    long timeInState = System.currentTimeMillis() - stateStartTime;
    stateToNode.get(currentState).callAction(entity, timeInState);
  }
}
