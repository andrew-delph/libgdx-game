package core.entity.collision.ladder;

import core.common.ChunkRange;
import core.entity.Entity;
import core.entity.collision.CollisionPoint;

public class LadderSensor extends CollisionPoint {

  public LadderSensor(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
