package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;
import networking.events.types.NetworkEventTypeEnum;
import networking.translation.NetworkDataSerializer;

public class CreateEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {

  public static String type = NetworkEventTypeEnum.CREATE_ENTITY_OUTGOING;
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  public CreateEntityOutgoingEventType(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
    this.entityData = entityData;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkDataSerializer.createCreateEntityOutgoingEventType(this);
  }

  public ChunkRange getChunkRange() {
    return this.chunkRange;
  }

  public NetworkObjects.NetworkData getEntityData() {
    return entityData;
  }

  public String getType() {
    return type;
  }
}
