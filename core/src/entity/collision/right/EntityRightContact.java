package entity.collision.right;

import com.google.inject.Inject;
import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.CollisionService;
import entity.collision.ContactWrapperCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityRightContact extends ContactWrapperCounter {
  final Logger LOGGER = LogManager.getLogger();
  @Inject CollisionService collisionService;

  public boolean isRightSpace(Entity entity) throws ChunkNotFound {
    if (this.getContactCount(entity.uuid, entity.getChunk().chunkRange) == null
        || this.getContactCount(entity.uuid, entity.getChunk().chunkRange) > 0) {
      return true;
    } else return false;
  }
}
