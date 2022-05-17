package entity.collision.orb;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class OrbSensor extends CollisionPoint {

  public OrbSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
