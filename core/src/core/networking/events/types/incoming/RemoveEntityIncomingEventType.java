package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.common.ChunkRange;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import java.util.UUID;

public class RemoveEntityIncomingEventType extends EventType {
  public static String type = NetworkEventTypeEnum.REMOVE_ENTITY_INCOMING;

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
  public String getEventType() {
    return type;
  }
}
