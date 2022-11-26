package core.common.events.types;

import core.common.ChunkRange;
import core.entity.Entity;
import java.util.UUID;

public class ReplaceEntityEventType extends EventType {

  public static String type = "replace_block";

  UUID target;
  Entity replacementEntity;
  ChunkRange chunkRange;
  Boolean swapVelocity;

  public ReplaceEntityEventType(
      UUID target, Entity replacementBlock, Boolean swapVelocity, ChunkRange chunkRange) {
    this.target = target;
    this.replacementEntity = replacementBlock;
    this.chunkRange = chunkRange;
    this.swapVelocity = swapVelocity;
  }

  public Boolean getSwapVelocity() {
    return swapVelocity;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public UUID getTargetUUID() {
    return target;
  }

  public Entity getReplacementEntity() {
    return replacementEntity;
  }

  @Override
  public String toString() {
    return "ReplaceEntityEventType{"
        + "target="
        + target
        + ", replacementEntity="
        + replacementEntity
        + ", chunkRange="
        + chunkRange
        + ", swapVelocity="
        + swapVelocity
        + '}';
  }

  @Override
  public String getEventType() {
    return type;
  }
}
