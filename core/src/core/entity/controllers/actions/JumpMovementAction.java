package core.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import core.common.exceptions.ChunkNotFound;
import core.entity.collision.ground.EntityGroundContact;
import core.entity.Entity;

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
  public Boolean isValid(Entity entity) throws ChunkNotFound {
    return this.entityGroundContact.isOnGround(entity);
  }
}
