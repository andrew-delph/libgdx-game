package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import java.util.function.Consumer;

public class RemoveEntityOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    RemoveEntityOutgoingEventType realEvent = (RemoveEntityOutgoingEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
