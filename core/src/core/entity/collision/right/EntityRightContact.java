package core.entity.collision.right;

import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.ContactWrapperCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityRightContact extends ContactWrapperCounter {
  final Logger LOGGER = LogManager.getLogger();

  public boolean isRightSpace(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) == null
        || this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) <= 0;
  }
}
