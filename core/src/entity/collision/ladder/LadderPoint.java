package entity.collision.ladder;

import chunk.ChunkRange;
import entity.Entity;
import entity.collision.CollisionPoint;

public class LadderPoint extends CollisionPoint {

  public LadderPoint(Entity entity, ChunkRange chunkRange) {
    super(entity, chunkRange);
  }
}
