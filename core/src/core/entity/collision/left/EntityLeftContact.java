package core.entity.collision.left;

import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.ContactWrapperCounter;

public class EntityLeftContact extends ContactWrapperCounter {

  public boolean isLeftSpace(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) == null
        || this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) <= 0;
  }
}
