package entity.collision.right;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class RightSensor extends CollisionPoint {

  public RightSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
