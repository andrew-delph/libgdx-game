package entity.collision.ladder;

import chunk.ChunkRange;
import entity.collision.CollisionPoint;
import java.util.UUID;

public class LadderPoint extends CollisionPoint {

  public LadderPoint(UUID uuid, ChunkRange chunkRange) {
    super(uuid, chunkRange);
  }
}
