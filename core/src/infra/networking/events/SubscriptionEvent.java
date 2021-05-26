package infra.networking.events;

import infra.chunk.ChunkRange;
import infra.networking.NetworkObjects;

import java.util.List;

public class SubscriptionEvent {
  List<ChunkRange> chunkRangeList;

  public SubscriptionEvent(List<ChunkRange> chunkRangeList) {
    this.chunkRangeList = chunkRangeList;
  }

  public NetworkObjects.NetworkEvent getNetworkEvent() {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent("subscription_event");

    NetworkObjects.NetworkData.Builder eventDataBuilder = NetworkObjects.NetworkData.newBuilder();

    for (ChunkRange chunkRange : this.chunkRangeList) {
      eventDataBuilder.addChildren(chunkRange.toNetworkData());
    }
    eventBuilder.setData(eventDataBuilder.build());

    return eventBuilder.build();
  }
}
