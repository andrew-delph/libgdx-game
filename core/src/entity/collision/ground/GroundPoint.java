package entity.collision.ground;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class GroundPoint extends CollisionPoint {

  public GroundPoint(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
