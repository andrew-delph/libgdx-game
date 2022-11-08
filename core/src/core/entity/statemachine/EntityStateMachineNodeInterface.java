package core.entity.statemachine;

import core.entity.Entity;

public interface EntityStateMachineNodeInterface {
  void callAnimation(Entity entity);
  // renders the animation
  // takes the arguments needed to render the animation

  void callAction(Entity entity, float timeInState);
  //  calls with the
}
