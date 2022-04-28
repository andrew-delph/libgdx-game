package entity.collision.ladder;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class LadderSensor extends CollisionPoint {

  public LadderSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
