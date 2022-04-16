package entity.collision.left;

import chunk.ChunkRange;
import entity.collision.CollisionPoint;
import java.util.UUID;

public class LeftSensorPoint extends CollisionPoint {

  public LeftSensorPoint(UUID uuid, ChunkRange chunkRange) {
    super(uuid, chunkRange);
  }
}
