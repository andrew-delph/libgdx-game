package entity.collision.ladder;

import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.ContactWrapperCounter;

public class EntityLadderContact extends ContactWrapperCounter {

  public boolean isOnLadder(Entity entity) throws ChunkNotFound {
    if (this.getContactCount(entity.uuid, entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.uuid, entity.getChunk().chunkRange) > 0) {
      return true;
    } else return false;
  }
}
