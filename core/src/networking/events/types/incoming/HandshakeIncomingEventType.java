package networking.events.types.incoming;

import app.user.UserID;
import chunk.ChunkRange;
import common.events.types.EventType;
import networking.events.types.NetworkEventTypeEnum;

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
  public String getType() {
    return NetworkEventTypeEnum.HANDSHAKE_INCOMING;
  }
}
