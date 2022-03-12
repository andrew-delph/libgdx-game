package networking.events.types.incoming;

import static networking.events.types.NetworkEventTypeEnum.REMOVE_ENTITY_INCOMING;

import app.user.UserID;
import chunk.ChunkRange;
import common.events.types.EventType;
import java.util.UUID;

public class RemoveEntityIncomingEventType extends EventType {
  public static String type = REMOVE_ENTITY_INCOMING;

  ChunkRange chunkRange;
  UserID userID;
  UUID target;

  public RemoveEntityIncomingEventType(UserID userID, ChunkRange chunkRange, UUID target) {
    this.chunkRange = chunkRange;
    this.userID = userID;
    this.target = target;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public UserID getUserID() {
    return userID;
  }

  public UUID getTarget() {
    return target;
  }

  @Override
  public String getType() {
    return type;
  }
}
