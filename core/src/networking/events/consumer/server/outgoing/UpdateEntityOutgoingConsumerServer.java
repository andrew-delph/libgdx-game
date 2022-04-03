package networking.events.consumer.server.outgoing;

import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.NetworkObjects;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

public class UpdateEntityOutgoingConsumerServer implements Consumer<EventType> {

  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityOutgoingEventType realEvent = (UpdateEntityOutgoingEventType) eventType;
    NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
    for (UserID userID : activeChunkManager.getChunkRangeUsers(realEvent.getChunkRange())) {
      serverNetworkHandle.send(userID, networkEvent);
    }
  }
}
