package core.networking.events.types.outgoing;

import com.google.inject.Inject;
import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.networking.events.interfaces.SerializeNetworkEvent;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;

public class SubscriptionOutgoingEventType extends EventType implements SerializeNetworkEvent {
  public static String type = "subscription_outgoing_event";
  List<ChunkRange> chunkRangeList;
  NetworkObjects.NetworkEvent networkEvent;

  @Inject
  public SubscriptionOutgoingEventType(List<ChunkRange> chunkRangeList) {
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
  public String getEventType() {
    return type;
  }
}
