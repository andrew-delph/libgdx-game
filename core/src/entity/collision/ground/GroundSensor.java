package entity.collision.ground;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class GroundSensor extends CollisionPoint {

  public GroundSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
