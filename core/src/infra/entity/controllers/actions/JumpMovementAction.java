package infra.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import infra.entity.collision.contact.EntityGroundContact;

public class JumpMovementAction implements EntityAction {

  @Inject EntityGroundContact entityGroundContact;

  @Inject
  JumpMovementAction() {}

  @Override
  public void apply(Body body) {
    body.setLinearVelocity(new Vector2(0, 9));
  }

  @Override
  public Boolean isValid(Body body) {
    return this.entityGroundContact.isOnGround(body);
  }
}
