package core.entity.collision;

import core.chunk.ChunkRange;
import core.entity.Entity;

public class EntitySensor extends CollisionPoint {

  public EntitySensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
