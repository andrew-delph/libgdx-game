package core.entity.collision.left;

import core.chunk.ChunkRange;
import core.entity.Entity;
import core.entity.collision.CollisionPoint;

public class LeftSensor extends CollisionPoint {

  public LeftSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
