package core.entity.collision.right;

import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.ContactWrapperCounter;



public class EntityRightContact extends ContactWrapperCounter {


  public boolean isRightSpace(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) == null
        || this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) <= 0;
  }
}
