package core.entity.collision;

import core.common.ChunkRange;
import core.entity.Entity;

public abstract class CollisionPoint {

  private final Entity entity;
  private final ChunkRange chunkRange;

  public CollisionPoint(Entity entity, ChunkRange chunkRange) {
    this.entity = entity;
    this.chunkRange = chunkRange;
  }

  public Entity getEntity() {
    return entity;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }
}
