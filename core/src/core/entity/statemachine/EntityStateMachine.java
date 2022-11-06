package core.entity.statemachine;

import java.util.HashMap;
import java.util.Map;

public class EntityStateMachine {
  private Map<String, EntityStateMachineNodeInterface> children = new HashMap<>();
  private EntityStateMachineNodeInterface current;
  private float stateStartTime = 0;

  public void attemptTransition(String transition) {
    if (children.containsKey(transition)) {
      current = children.get(transition);
      stateStartTime = System.currentTimeMillis();
    }
  }

  public void callAnimation() {
    current.callAnimation();
  }

  public void callAction() {
    float timeInState = stateStartTime - System.currentTimeMillis();
    current.callAction(timeInState);
  }
}
