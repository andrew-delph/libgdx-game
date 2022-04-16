package entity.collision;

import chunk.ChunkRange;
import java.util.UUID;

public abstract class CollisionPoint {

  private final UUID uuid;
  private final ChunkRange chunkRange;

  public CollisionPoint(UUID uuid, ChunkRange chunkRange) {
    this.uuid = uuid;
    this.chunkRange = chunkRange;
  }

  public UUID getUuid() {
    return uuid;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }
}
