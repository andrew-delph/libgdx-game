package infra.entity.controllers.actions;

import com.google.inject.Inject;
import infra.entity.collision.ground.EntityGroundContact;

public class EntityActionFactory {
  @Inject EntityGroundContact entityGroundContact;

  @Inject
  EntityActionFactory() {}

  public HorizontalMovementAction createHorizontalMovementAction(int magnitude) {
    return new HorizontalMovementAction(magnitude);
  }

  public JumpMovementAction createJumpMovementAction() {
    return new JumpMovementAction(entityGroundContact);
  }

  public StopMovementAction createStopMovementAction() {
    return new StopMovementAction();
  }
}
