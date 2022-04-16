package entity.collision.left;

import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.ContactWrapperCounter;

public class EntityLeftContact extends ContactWrapperCounter {

  public boolean isLeftSpace(Entity entity) throws ChunkNotFound {
    if (this.getContactCount(entity.uuid, entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.uuid, entity.getChunk().chunkRange) > 0) {
      return false;
    } else return true;
  }
}
