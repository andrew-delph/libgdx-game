package core.networking.events.consumer.client.incoming;

import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.incoming.ReplaceBlockIncomingEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReplaceBlockIncomingConsumerClient implements Consumer<EventType> {
  final Logger LOGGER = LogManager.getLogger();
  @Inject GameController gameController;
  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockIncomingEventType incoming = (ReplaceBlockIncomingEventType) eventType;
    try {
      gameController.triggerReplaceEntity(incoming.getTarget(), incoming.getReplacementBlock());
    } catch (EntityNotFound e) {
      LOGGER.error(e);
      clientNetworkHandle.initHandshake(incoming.getChunkRange());
    } catch (ChunkNotFound | BodyNotFound | DestroyBodyException e) {
      LOGGER.error(e);
    }
  }
}
