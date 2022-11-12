package core.entity.statemachine;

import core.app.screen.assets.animations.AnimationState;
import core.entity.Entity;
import java.util.Map;

public class EntityStateMachine {
  private Map<AnimationState, EntityStateMachineNodeInterface> children;
  private EntityStateMachineNodeInterface current;
  private float stateStartTime = 0;
  private Entity entity;

  public EntityStateMachine(
      Entity entity, Map<AnimationState, EntityStateMachineNodeInterface> children) {
    this.children = children;
    this.entity = entity;
  }

  public void attemptTransition(AnimationState transition) {
    if (children.containsKey(transition)) {
      current = children.get(transition);
      stateStartTime = System.currentTimeMillis();
    }
  }

  public void callAnimation() {
    if (current == null) return;
    current.callAnimation(entity);
  }

  public void callAction() {
    if (current == null) return;
    float timeInState = stateStartTime - System.currentTimeMillis();
    current.callAction(entity, timeInState);
  }
}
