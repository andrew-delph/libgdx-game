package entity.collision;

import chunk.ChunkRange;
import entity.Entity;

public class EntityPoint extends CollisionPoint {

  public EntityPoint(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
