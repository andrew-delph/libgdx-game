package core.entity.statemachine;

import core.entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class EntityStateMachine {
  private Map<String, EntityStateMachineNodeInterface> children = new HashMap<>();
  private EntityStateMachineNodeInterface current;
  private float stateStartTime = 0;
  private Entity entity;

  public void attemptTransition(String transition) {
    if (children.containsKey(transition)) {
      current = children.get(transition);
      stateStartTime = System.currentTimeMillis();
    }
  }

  public void callAnimation() {
    current.callAnimation(entity);
  }

  public void callAction() {
    float timeInState = stateStartTime - System.currentTimeMillis();
    current.callAction(entity, timeInState);
  }
}
