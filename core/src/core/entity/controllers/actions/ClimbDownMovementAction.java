package core.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.ladder.EntityLadderContact;

public class ClimbDownMovementAction extends ClimbUpMovementAction {
  public ClimbDownMovementAction(EntityLadderContact entityLadderContact) {
    super(entityLadderContact);
  }

  @Override
  public void apply(Entity entity) throws ChunkNotFound, BodyNotFound {
    entity.applyBody(
        (body -> {
          body.setGravityScale(0);
          float x = body.getLinearVelocity().x;
          body.setLinearVelocity(new Vector2(x, -5));
        }));
  }
}
