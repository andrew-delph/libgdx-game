package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.types.CreateTurretEventType;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;

public class CreateTurretOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    CreateTurretEventType realEvent = (CreateTurretEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
