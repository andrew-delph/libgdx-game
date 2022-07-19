package core.networking.events.consumer.server.outgoing;

import com.google.inject.Inject;
import core.app.user.UserID;
import core.chunk.ActiveChunkManager;
import core.common.events.types.EventType;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import core.networking.server.ServerNetworkHandle;
import java.util.function.Consumer;
import networking.NetworkObjects;

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
