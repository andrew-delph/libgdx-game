package infra.networking.events;

import com.google.inject.Inject;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

public class RemoveEntityOutgoingEvent extends Event implements SerializeNetworkEvent {

  public static String type = "remove_entity_outgoing";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  @Inject
  RemoveEntityOutgoingEvent(NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
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
