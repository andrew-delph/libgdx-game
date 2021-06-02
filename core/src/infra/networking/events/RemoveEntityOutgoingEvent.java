package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

public class RemoveEntityOutgoingEvent extends Event implements SerializeNetworkEvent {

  public static String type = "remove_entity_incoming";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  @Inject
  RemoveEntityOutgoingEvent(
      @Assisted NetworkObjects.NetworkData entityData, @Assisted ChunkRange chunkRange) {
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
