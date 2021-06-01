package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

public class CreateEntityOutgoingEvent extends Event implements SerializeNetworkEvent {

  public static String type = "create_entity_outgoing";
  NetworkObjects.NetworkData entityData;
  ChunkRange chunkRange;

  @Inject
  CreateEntityOutgoingEvent(
      @Assisted NetworkObjects.NetworkData entityData, @Assisted ChunkRange chunkRange) {
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
