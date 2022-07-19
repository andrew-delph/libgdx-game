package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.entity.attributes.Attribute;
import core.networking.events.types.NetworkEventTypeEnum;
import java.util.List;
import java.util.UUID;

public class UpdateEntityIncomingEventType extends EventType {

  public static String type = NetworkEventTypeEnum.UPDATE_ENTITY_INCOMING;

  UserID userID;
  ChunkRange chunkRange;
  List<Attribute> attributeList;
  UUID uuid;

  public UpdateEntityIncomingEventType(
      UserID userID, List<Attribute> attributeList, ChunkRange chunkRange, UUID uuid) {
    this.attributeList = attributeList;
    this.chunkRange = chunkRange;
    this.userID = userID;
    this.uuid = uuid;
  }

  public List<Attribute> getAttributeList() {
    return attributeList;
  }

  public UUID getUuid() {
    return uuid;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public UserID getUserID() {
    return this.userID;
  }

  @Override
  public String getEventType() {
    return type;
  }
}
