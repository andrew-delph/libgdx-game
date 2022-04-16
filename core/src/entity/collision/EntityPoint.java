package entity.collision;

import chunk.ChunkRange;
import java.util.UUID;

public class EntityPoint extends CollisionPoint {

  public EntityPoint(UUID uuid, ChunkRange chunkRange) {
    super(uuid, chunkRange);
  }
}
