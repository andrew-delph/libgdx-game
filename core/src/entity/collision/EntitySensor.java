package entity.collision;

import chunk.ChunkRange;
import entity.Entity;

public class EntitySensor extends CollisionPoint {

  public EntitySensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
