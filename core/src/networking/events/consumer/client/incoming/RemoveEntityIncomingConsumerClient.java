package networking.events.consumer.client.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import networking.events.types.incoming.RemoveEntityIncomingEventType;

public class RemoveEntityIncomingConsumerClient implements Consumer<EventType> {
  @Inject GameController gameController;

  @Override
  public void accept(EventType eventType) {
    RemoveEntityIncomingEventType incoming = (RemoveEntityIncomingEventType) eventType;
    try {
      gameController.triggerRemoveEntity(incoming.getTarget());
    } catch (EntityNotFound e) {
      e.printStackTrace();
    }
  }
}
