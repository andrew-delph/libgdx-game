package networking.events.types.outgoing;

import com.google.inject.Inject;
import chunk.ChunkRange;
import common.events.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

public class RemoveEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "remove_entity_outgoing";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  @Inject
  public RemoveEntityOutgoingEventType(NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    this.entityData = entityData;
    this.chunkRange = chunkRange;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder().setData(this.entityData).setEvent(type).build();
  }
}
