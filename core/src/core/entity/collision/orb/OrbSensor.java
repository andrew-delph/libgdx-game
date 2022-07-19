package core.entity.collision.orb;

import core.chunk.ChunkRange;
import core.entity.collision.CollisionPoint;
import core.entity.Entity;

public class OrbSensor extends CollisionPoint {

  public OrbSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
