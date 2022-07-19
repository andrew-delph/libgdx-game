package core.entity.collision.ladder;

import core.common.exceptions.ChunkNotFound;
import core.entity.collision.ContactWrapperCounter;
import core.entity.Entity;

public class EntityLadderContact extends ContactWrapperCounter {

  public boolean isOnLadder(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) > 0;
  }
}
