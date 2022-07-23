package core.entity.collision.ladder;

import com.google.inject.Inject;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.CollisionPoint;
import core.entity.collision.ContactWrapperCounter;
import core.entity.collision.ground.EntityGroundContact;

public class EntityLadderContact extends ContactWrapperCounter {
  @Inject EntityGroundContact entityGroundContact;

  @Override
  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    super.beginContact(source, target);
    entityGroundContact.setGroundPosition(
        source.getEntity().getUuid(), target.getEntity().getCoordinatesWrapper().getCoordinates());
  }

  public boolean isOnLadder(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) > 0;
  }
}
