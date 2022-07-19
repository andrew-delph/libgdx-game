package core.networking.events.consumer.server.incoming;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.app.user.UserID;
import core.chunk.ActiveChunkManager;
import core.chunk.world.exceptions.BodyNotFound;
import core.chunk.world.exceptions.DestroyBodyException;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.ReplaceBlockIncomingEventType;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import core.networking.server.ServerNetworkHandle;
import java.util.function.Consumer;
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
