package entity.collision.ladder;

import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.ContactWrapperCounter;

public class EntityLadderContact extends ContactWrapperCounter {

  public boolean isOnLadder(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) > 0;
  }
}
