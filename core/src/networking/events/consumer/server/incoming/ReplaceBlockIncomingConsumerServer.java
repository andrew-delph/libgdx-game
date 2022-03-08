package networking.events.consumer.server.incoming;

import app.GameController;
import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.server.ServerNetworkHandle;

public class ReplaceBlockIncomingConsumerServer implements Consumer<EventType> {

  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameController gameController;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockIncomingEventType incoming = (ReplaceBlockIncomingEventType) eventType;
    try {
      gameController.triggerReplaceEntity(incoming.getTarget(), incoming.getReplacementBlock());
    } catch (EntityNotFound e) {
      e.printStackTrace();
      serverNetworkHandle.initHandshake(incoming.getUserID(), incoming.getChunkRange());
    }
    ReplaceBlockOutgoingEventType outgoing =
        EventTypeFactory.createReplaceBlockOutgoingEvent(
            incoming.getTarget(), incoming.getReplacementBlock(), incoming.getChunkRange());
    for (UserID userID : activeChunkManager.getChunkRangeUsers(incoming.getChunkRange())) {
      if (incoming.getUserID().equals(userID)) continue;
      serverNetworkHandle.send(userID, outgoing.toNetworkEvent());
    }
  }
}
