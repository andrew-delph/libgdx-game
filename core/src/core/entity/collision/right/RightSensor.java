package core.entity.collision.right;

import core.chunk.ChunkRange;
import core.entity.Entity;
import core.entity.collision.CollisionPoint;

public class RightSensor extends CollisionPoint {

  public RightSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
