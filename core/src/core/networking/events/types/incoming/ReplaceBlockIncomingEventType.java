package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.entity.block.Block;
import core.networking.events.types.NetworkEventTypeEnum;
import java.util.UUID;

public class ReplaceBlockIncomingEventType extends EventType {

  public static String type = NetworkEventTypeEnum.REPLACE_ENTITY_INCOMING;

  UserID userID;
  UUID target;
  Block replacementBlock;
  ChunkRange chunkRange;

  public ReplaceBlockIncomingEventType(
      UserID userID, UUID target, Block replacementBlock, ChunkRange chunkRange) {
    this.userID = userID;
    this.target = target;
    this.replacementBlock = replacementBlock;
    this.chunkRange = chunkRange;
  }

  public UUID getTarget() {
    return target;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public Block getReplacementBlock() {
    return replacementBlock;
  }

  public UserID getUserID() {
    return this.userID;
  }

  @Override
  public String getEventType() {
    return type;
  }
}
