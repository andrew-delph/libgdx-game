package networking.events.types.outgoing;

import static networking.events.types.NetworkEventTypeEnum.UPDATE_ENTITY_OUTGOING;

import chunk.ChunkRange;
import common.events.types.EventType;
import entity.attributes.Attribute;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;
import networking.translation.NetworkDataSerializer;

public class UpdateEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {
  public static String type = UPDATE_ENTITY_OUTGOING;

  ChunkRange chunkRange;
  UUID uuid;
  List<Attribute> attributeList;

  public UpdateEntityOutgoingEventType(
      List<Attribute> attributeList, ChunkRange chunkRange, UUID uuid) {
    this.chunkRange = chunkRange;
    this.attributeList = attributeList;
    this.uuid = uuid;
  }

  public UUID getUuid() {
    return uuid;
  }

  public List<Attribute> getAttributeList() {
    return attributeList;
  }

  public String getEventType() {
    return type;
  }

  public ChunkRange getChunkRange() {
    return this.chunkRange;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkDataSerializer.createUpdateEntityOutgoingEventType(this);
  }
}
