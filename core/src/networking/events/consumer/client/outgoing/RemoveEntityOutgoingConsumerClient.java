package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;

public class RemoveEntityOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    RemoveEntityOutgoingEventType realEvent = (RemoveEntityOutgoingEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
