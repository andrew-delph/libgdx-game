package entity.collision.ground;

import com.google.inject.Inject;
import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.ContactWrapperCounter;

public class EntityGroundContact extends ContactWrapperCounter {

  @Inject
  public EntityGroundContact() {}

  public Boolean isOnGround(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) > 0;
  }
}
