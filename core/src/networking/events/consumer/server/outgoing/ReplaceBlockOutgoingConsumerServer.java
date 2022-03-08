package networking.events.consumer.server.outgoing;

import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.server.ServerNetworkHandle;

public class ReplaceBlockOutgoingConsumerServer implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) eventType;
    for (UserID userID : activeChunkManager.getChunkRangeUsers(realEvent.getChunkRange())) {
      serverNetworkHandle.send(userID, realEvent.toNetworkEvent());
    }
  }
}
