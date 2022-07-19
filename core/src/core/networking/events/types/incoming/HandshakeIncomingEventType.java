package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import java.util.List;
import java.util.UUID;

public class HandshakeIncomingEventType extends EventType {

  ChunkRange chunkRange;
  List<UUID> listUUID;
  UserID requestUserID;

  public HandshakeIncomingEventType(
      UserID requestUserID, ChunkRange chunkRange, List<UUID> listUUID) {
    this.chunkRange = chunkRange;
    this.listUUID = listUUID;
    this.requestUserID = requestUserID;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public List<UUID> getListUUID() {
    return listUUID;
  }

  public UserID getRequestUserID() {
    return requestUserID;
  }

  @Override
  public String getEventType() {
    return NetworkEventTypeEnum.HANDSHAKE_INCOMING;
  }
}
