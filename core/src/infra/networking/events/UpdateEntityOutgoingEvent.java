package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

public class UpdateEntityOutgoingEvent extends Event implements SerializeNetworkEvent {
  public static String type = "update_entity_outgoing";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  @Inject
  public UpdateEntityOutgoingEvent(
      @Assisted NetworkObjects.NetworkData entityData, @Assisted ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
    this.entityData = entityData;
  }

  public String getType() {
    return type;
  }

  public ChunkRange getChunkRange() {
    return this.chunkRange;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder().setData(this.entityData).setEvent(type).build();
  }
}
