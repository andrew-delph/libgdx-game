package networking.events.consumer.client.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;

public class ReplaceBlockIncomingConsumerClient implements Consumer<EventType> {
  @Inject GameController gameController;
  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockIncomingEventType incoming = (ReplaceBlockIncomingEventType) eventType;
    try {
      gameController.triggerReplaceEntity(incoming.getTarget(), incoming.getReplacementBlock());
    } catch (EntityNotFound e) {
      clientNetworkHandle.initHandshake(incoming.getChunkRange());
    }
  }
}
