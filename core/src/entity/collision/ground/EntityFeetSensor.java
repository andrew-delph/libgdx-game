package entity.collision.ground;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class EntityFeetSensor extends CollisionPoint {

  public EntityFeetSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
