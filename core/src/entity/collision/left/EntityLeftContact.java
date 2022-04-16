package entity.collision.left;

import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.ContactWrapperCounter;

public class EntityLeftContact extends ContactWrapperCounter {

  public boolean isLeftSpace(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) == null
        || this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) <= 0;
  }
}
