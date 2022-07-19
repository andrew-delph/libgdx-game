package core.entity.collision.left;

import core.chunk.ChunkRange;
import core.entity.collision.CollisionPoint;
import core.entity.Entity;

public class LeftSensor extends CollisionPoint {

  public LeftSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
