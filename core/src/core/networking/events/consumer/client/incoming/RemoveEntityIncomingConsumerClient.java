package core.networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.chunk.world.exceptions.DestroyBodyException;
import core.common.events.types.EventType;
import core.common.exceptions.EntityNotFound;
import core.networking.events.types.incoming.RemoveEntityIncomingEventType;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveEntityIncomingConsumerClient implements Consumer<EventType> {
  final Logger LOGGER = LogManager.getLogger();
  @Inject GameController gameController;

  @Override
  public void accept(EventType eventType) {
    RemoveEntityIncomingEventType incoming = (RemoveEntityIncomingEventType) eventType;
    try {
      gameController.triggerRemoveEntity(incoming.getTarget());
    } catch (EntityNotFound | DestroyBodyException e) {
      LOGGER.error(e);
    }
  }
}
