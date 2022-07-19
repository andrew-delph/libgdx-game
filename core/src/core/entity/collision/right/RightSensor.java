package core.entity.collision.right;

import core.chunk.ChunkRange;
import core.entity.collision.CollisionPoint;
import core.entity.Entity;

public class RightSensor extends CollisionPoint {

  public RightSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
