package entity.collision.left;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class LeftSensor extends CollisionPoint {

  public LeftSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
