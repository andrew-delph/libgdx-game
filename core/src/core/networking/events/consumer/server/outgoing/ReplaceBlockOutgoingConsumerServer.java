package core.networking.events.consumer.server.outgoing;

import core.app.user.UserID;
import core.chunk.ActiveChunkManager;
import com.google.inject.Inject;
import core.common.events.EventService;
import core.common.events.types.EventType;
import java.util.function.Consumer;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import core.networking.server.ServerNetworkHandle;

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
