package entity.collision.left;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class LeftSensorPoint extends CollisionPoint {

  public LeftSensorPoint(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
