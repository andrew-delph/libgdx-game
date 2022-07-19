package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import networking.NetworkObjects;

public class CreateEntityIncomingEventType extends EventType {

  public static String type = NetworkEventTypeEnum.CREATE_ENTITY_INCOMING;

  UserID userID;
  NetworkObjects.NetworkData networkData;
  ChunkRange chunkRange;

  public CreateEntityIncomingEventType(
      UserID userID, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
    this.userID = userID;
    this.networkData = networkData;
    this.chunkRange = chunkRange;
  }

  public NetworkObjects.NetworkData getData() {
    return networkData;
  }

  public UserID getUserID() {
    return this.userID;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  @Override
  public String getEventType() {
    return type;
  }
}
