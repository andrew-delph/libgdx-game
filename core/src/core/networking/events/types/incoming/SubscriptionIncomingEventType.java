package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.chunk.ChunkRange;
import com.google.inject.Inject;
import core.common.events.types.EventType;
import java.util.LinkedList;
import java.util.List;
import networking.NetworkObjects;

public class SubscriptionIncomingEventType extends EventType {

  public static String type = "subscription_incoming_event";
  List<ChunkRange> chunkRangeList;
  UserID userID;

  @Inject
  public SubscriptionIncomingEventType(NetworkObjects.NetworkEvent networkEvent) {
    NetworkObjects.NetworkData data = networkEvent.getData();
    this.userID = UserID.createUserID(networkEvent.getUser());
    this.chunkRangeList = new LinkedList<>();
    for (NetworkObjects.NetworkData child : data.getChildrenList()) {
      chunkRangeList.add(new ChunkRange(child));
    }
  }

  public List<ChunkRange> getChunkRangeList() {
    return chunkRangeList;
  }

  public UserID getUserID() {
    return this.userID;
  }

  @Override
  public String getEventType() {
    return type;
  }
}
