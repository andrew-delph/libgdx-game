package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

public class CreateEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "create_entity_outgoing";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  public CreateEntityOutgoingEventType(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
    this.entityData = entityData;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder().setData(this.entityData).setEvent(type).build();
  }

  public ChunkRange getChunkRange() {
    return this.chunkRange;
  }

  public String getType() {
    return type;
  }
}
