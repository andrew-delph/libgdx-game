package core.networking.events.types.outgoing;

import core.common.ChunkRange;
import core.common.events.types.EventType;
import core.entity.attributes.Attribute;
import core.networking.events.interfaces.SerializeNetworkEvent;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.translation.NetworkDataSerializer;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;

public class UpdateEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {
  public static String type = NetworkEventTypeEnum.UPDATE_ENTITY_OUTGOING;

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
