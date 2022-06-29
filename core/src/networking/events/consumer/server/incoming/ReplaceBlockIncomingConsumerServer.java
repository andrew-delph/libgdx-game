package networking.events.consumer.server.incoming;

import app.game.GameController;
import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.server.ServerNetworkHandle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReplaceBlockIncomingConsumerServer implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameController gameController;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockIncomingEventType incoming = (ReplaceBlockIncomingEventType) eventType;
    try {
      gameController.triggerReplaceEntity(incoming.getTarget(), incoming.getReplacementBlock());
    } catch (EntityNotFound e) {
      LOGGER.error(e);
      serverNetworkHandle.initHandshake(incoming.getUserID(), incoming.getChunkRange());
    } catch (ChunkNotFound | BodyNotFound | DestroyBodyException e) {
      LOGGER.error(e);
      return;
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
