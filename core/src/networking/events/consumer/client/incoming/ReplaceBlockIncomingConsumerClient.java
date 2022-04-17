package networking.events.consumer.client.incoming;

import app.GameController;
import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
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
