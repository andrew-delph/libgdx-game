package entity.collision.ground;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class GroundSensorPoint extends CollisionPoint {

  public GroundSensorPoint(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
