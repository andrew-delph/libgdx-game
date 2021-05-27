package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SubscriptionEvent extends Event implements SerializeNetworkEvent {
  List<ChunkRange> chunkRangeList;

  public List<ChunkRange> getChunkRangeList() {
    return chunkRangeList;
  }

  public static String type = "subscription_event";

  NetworkObjects.NetworkEvent networkEvent;

  @Inject
  public SubscriptionEvent(@Assisted List<ChunkRange> chunkRangeList) {
    this.chunkRangeList = chunkRangeList;
  }

  @Inject
  public SubscriptionEvent(@Assisted NetworkObjects.NetworkEvent networkEvent) {
    this.networkEvent = networkEvent;
    NetworkObjects.NetworkData data = networkEvent.getData();
    this.chunkRangeList = new LinkedList<>();
    for (NetworkObjects.NetworkData child : data.getChildrenList()) {
      chunkRangeList.add(new ChunkRange(child));
    }
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
