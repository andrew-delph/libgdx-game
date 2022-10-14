package core.entity.statemachine;

import java.util.HashMap;
import java.util.Map;

public class EntityStateMachine {
  private Map<String, EntityStateMachineNode> children = new HashMap<>();
  private EntityStateMachineNode current;

  public void attemptTransition(String transition) {
    if (children.containsKey(transition)) {
      current = children.get(transition);
    }
  }
}
