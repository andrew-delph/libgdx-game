package entity.collision.projectile;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class ProjectileSensor extends CollisionPoint {

  public ProjectileSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
