package core.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.ladder.EntityLadderContact;

public class ClimbUpMovementAction implements EntityAction {
  EntityLadderContact entityLadderContact;

  public ClimbUpMovementAction(EntityLadderContact entityLadderContact) {
    this.entityLadderContact = entityLadderContact;
  }

  @Override
  public void apply(Body body) {
    float x = body.getLinearVelocity().x;
    body.setLinearVelocity(new Vector2(x, 5));
  }

  @Override
  public Boolean isValid(Entity entity) throws ChunkNotFound {
    return this.entityLadderContact.isOnLadder(entity);
  }
}
