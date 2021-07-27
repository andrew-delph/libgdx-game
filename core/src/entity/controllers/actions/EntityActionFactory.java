package entity.controllers.actions;

import com.google.inject.Inject;
import entity.collision.ground.EntityGroundContact;
import entity.collision.ladder.EntityLadderContact;

public class EntityActionFactory {
  @Inject EntityGroundContact entityGroundContact;
  @Inject EntityLadderContact entityLadderContact;

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

  public ClimbUpMovementAction createClimbUpMovementAction() {
    return new ClimbUpMovementAction(entityLadderContact);
  }

  public ClimbUpMovementAction createClimbDownMovementAction() {
    return new ClimbDownMovementAction(entityLadderContact);
  }
}
