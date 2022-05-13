package networking.events.types.incoming;

import static networking.events.types.NetworkEventTypeEnum.UPDATE_ENTITY_INCOMING;

import app.user.UserID;
import chunk.ChunkRange;
import common.events.types.EventType;
import entity.attributes.Attribute;
import java.util.List;
import java.util.UUID;

public class UpdateEntityIncomingEventType extends EventType {

  public static String type = UPDATE_ENTITY_INCOMING;

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
  public String getType() {
    return type;
  }
}
