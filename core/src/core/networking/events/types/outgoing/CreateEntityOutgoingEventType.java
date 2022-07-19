package core.networking.events.types.outgoing;

import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects;
import core.networking.events.interfaces.SerializeNetworkEvent;

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

  public String getEventType() {
    return type;
  }
}
