package networking.events.consumer.server.outgoing;

import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.Set;
import java.util.function.Consumer;
import networking.NetworkObjects;
import networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import networking.server.ServerNetworkHandle;

public class ChunkSwapOutgoingConsumerServer implements Consumer<EventType> {

  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ChunkSwapOutgoingEventType outgoing = (ChunkSwapOutgoingEventType) eventType;
    NetworkObjects.NetworkEvent networkEvent = outgoing.toNetworkEvent();
    Set<UserID> userIDS = activeChunkManager.getChunkRangeUsers(outgoing.getFrom());
    for (UserID userID : userIDS) {
      serverNetworkHandle.send(userID, networkEvent);
    }
  }
}
