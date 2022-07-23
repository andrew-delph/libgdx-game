package core.entity.collision.orb;

import core.common.ChunkRange;
import core.entity.Entity;
import core.entity.collision.CollisionPoint;

public class OrbSensor extends CollisionPoint {

  public OrbSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
