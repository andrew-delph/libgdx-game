package networking.events.consumer.server.incoming;

import app.GameController;
import app.user.User;
import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.ActiveEntityManager;
import java.util.function.Consumer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

public class RemoveEntityIncomingConsumerServer implements Consumer<EventType> {

  @Inject GameController gameController;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject ActiveChunkManager activeChunkManager;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;

  @Override
  public void accept(EventType eventType) {
    RemoveEntityIncomingEventType incoming = (RemoveEntityIncomingEventType) eventType;

    try {
      gameController.triggerRemoveEntity(incoming.getTarget());
      activeEntityManager.removeActiveEntity(user.getUserID(), incoming.getTarget());
    } catch (EntityNotFound | DestroyBodyException e) {
      e.printStackTrace();
      this.serverNetworkHandle.initHandshake(incoming.getUserID(), incoming.getChunkRange());
    }

    RemoveEntityOutgoingEventType outgoing =
        EventTypeFactory.createRemoveEntityOutgoingEvent(
            incoming.getTarget(), incoming.getChunkRange());

    for (UserID userID : activeChunkManager.getChunkRangeUsers(incoming.getChunkRange())) {
      if (userID.equals(incoming.getUserID())) continue;
      serverNetworkHandle.send(userID, outgoing.toNetworkEvent());
    }
  }
}
