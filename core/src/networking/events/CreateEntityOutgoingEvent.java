package networking.events;

import chunk.ChunkRange;
import common.events.Event;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

public class CreateEntityOutgoingEvent extends Event implements SerializeNetworkEvent {

  public static String type = "create_entity_outgoing";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  CreateEntityOutgoingEvent(NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
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
