package core.entity.collision.projectile;

import core.common.ChunkRange;
import core.entity.Entity;
import core.entity.collision.CollisionPoint;

public class ProjectileSensor extends CollisionPoint {

  public ProjectileSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
