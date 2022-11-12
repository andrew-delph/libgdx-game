package core.entity.statemachine;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.entity.Entity;

public abstract class EntityStateMachineNodeInterface {

  @Inject protected GameController gameController;

  public abstract void callAnimation(Entity entity);
  // renders the animation
  // takes the arguments needed to render the animation

  public abstract void callAction(Entity entity, float timeInState);
  //  calls with the
}
