package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;

public class CreateEntityOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    CreateEntityOutgoingEventType realEvent = (CreateEntityOutgoingEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
