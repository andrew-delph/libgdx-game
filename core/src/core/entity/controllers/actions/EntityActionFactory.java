package core.entity.controllers.actions;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.entity.collision.ground.EntityGroundContact;
import core.entity.collision.ladder.EntityLadderContact;
import core.entity.collision.left.EntityLeftContact;
import core.entity.collision.right.EntityRightContact;

public class EntityActionFactory {
  @Inject EntityGroundContact entityGroundContact;
  @Inject EntityLadderContact entityLadderContact;
  @Inject EntityLeftContact entityLeftContact;
  @Inject EntityRightContact entityRightContact;
  @Inject GameController gameController;

  @Inject
  EntityActionFactory() {}

  public HorizontalMovementAction createHorizontalMovementAction(int magnitude) {
    return new HorizontalMovementAction(
        gameController, entityLeftContact, entityRightContact, magnitude);
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
