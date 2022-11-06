package core.entity.statemachine;

public interface EntityStateMachineNodeInterface {
  void callAnimation();
  // renders the animation
  // takes the arguments needed to render the animation

  void callAction(float timeInState);
  //  calls with the
}
