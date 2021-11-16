package networking.events.types.incoming;

import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SubscriptionIncomingEventType extends EventType {

  public static String type = "subscription_incoming_event";
  List<ChunkRange> chunkRangeList;
  UUID uuid;

  @Inject
  public SubscriptionIncomingEventType(NetworkObjects.NetworkEvent networkEvent) {
    NetworkObjects.NetworkData data = networkEvent.getData();
    this.uuid = UUID.fromString(networkEvent.getUser());
    this.chunkRangeList = new LinkedList<>();
    for (NetworkObjects.NetworkData child : data.getChildrenList()) {
      chunkRangeList.add(new ChunkRange(child));
    }
  }

  public List<ChunkRange> getChunkRangeList() {
    return chunkRangeList;
  }

  public UUID getUser() {
    return this.uuid;
  }

  @Override
  public String getType() {
    return type;
  }
}
