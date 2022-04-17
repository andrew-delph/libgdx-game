package entity.collision.ground;

import chunk.ChunkRange;
import entity.collision.CollisionPoint;
import java.util.UUID;

public class GroundPoint extends CollisionPoint {

  public GroundPoint(UUID uuid, ChunkRange chunkRange) {
    super(uuid, chunkRange);
  }
}
