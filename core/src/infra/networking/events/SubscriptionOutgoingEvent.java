package infra.networking.events;

import com.google.inject.Inject;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

import java.util.List;
import java.util.UUID;

public class SubscriptionOutgoingEvent extends Event implements SerializeNetworkEvent {
  public static String type = "subscription_outgoing_event";
  List<ChunkRange> chunkRangeList;
  NetworkObjects.NetworkEvent networkEvent;

  @Inject
  public SubscriptionOutgoingEvent(List<ChunkRange> chunkRangeList) {
    this.chunkRangeList = chunkRangeList;
  }

  public List<ChunkRange> getChunkRangeList() {
    return chunkRangeList;
  }

  public UUID getUser() {
    return UUID.fromString(this.networkEvent.getUser());
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(type);

    NetworkObjects.NetworkData.Builder eventDataBuilder = NetworkObjects.NetworkData.newBuilder();

    for (ChunkRange chunkRange : this.chunkRangeList) {
      eventDataBuilder.addChildren(chunkRange.toNetworkData());
    }
    eventBuilder.setData(eventDataBuilder.build());

    return eventBuilder.build();
  }

  @Override
  public String getType() {
    return type;
  }
}
