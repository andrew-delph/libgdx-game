package entity.collision.ground;

import chunk.ChunkRange;
import entity.collision.CollisionPoint;
import java.util.UUID;

public class GroundSensorPoint extends CollisionPoint {

  public GroundSensorPoint(UUID uuid, ChunkRange chunkRange) {
    super(uuid, chunkRange);
  }
}
