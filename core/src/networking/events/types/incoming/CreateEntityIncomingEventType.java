package networking.events.types.incoming;

import app.user.UserID;
import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.types.NetworkEventTypeEnum;

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
  public String getType() {
    return type;
  }
}
