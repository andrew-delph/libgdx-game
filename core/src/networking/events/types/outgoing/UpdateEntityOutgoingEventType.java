package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;
import networking.translation.NetworkDataSerializer;

import static networking.events.types.NetworkEventTypeEnum.UPDATE_ENTITY_OUTGOING;

public class UpdateEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {
  public static String type = UPDATE_ENTITY_OUTGOING;
  NetworkObjects.NetworkData entityData;

  ChunkRange chunkRange;

  public UpdateEntityOutgoingEventType(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
    this.entityData = entityData;
  }

  public String getType() {
    return type;
  }

  public ChunkRange getChunkRange() {
    return this.chunkRange;
  }

  public NetworkObjects.NetworkData getEntityData() {
    return entityData;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkDataSerializer.createUpdateEntityOutgoingEventType(this);
  }
}
