package core.entity.collision.ground;

import core.chunk.ChunkRange;
import core.entity.collision.CollisionPoint;
import core.entity.Entity;

public class EntityFeetSensor extends CollisionPoint {

  public EntityFeetSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
