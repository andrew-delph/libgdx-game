package infra.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import infra.entity.collision.ground.EntityGroundContact;

public class JumpMovementAction implements EntityAction {

  EntityGroundContact entityGroundContact;

  JumpMovementAction(EntityGroundContact entityGroundContact) {
    this.entityGroundContact = entityGroundContact;
  }

  @Override
  public void apply(Body body) {
    body.setLinearVelocity(new Vector2(0, 9));
  }

  @Override
  public Boolean isValid(Body body) {
    return this.entityGroundContact.isOnGround(body);
  }
}
