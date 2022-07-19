package core.entity.collision.projectile;

import core.chunk.ChunkRange;
import core.entity.collision.CollisionPoint;
import core.entity.Entity;

public class ProjectileSensor extends CollisionPoint {

  public ProjectileSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
