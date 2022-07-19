package core.entity.collision.ladder;

import core.chunk.ChunkRange;
import core.entity.collision.CollisionPoint;
import core.entity.Entity;

public class LadderSensor extends CollisionPoint {

  public LadderSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
