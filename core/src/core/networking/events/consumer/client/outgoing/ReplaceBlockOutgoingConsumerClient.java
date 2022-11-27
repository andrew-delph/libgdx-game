package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;


public class ReplaceBlockOutgoingConsumerClient implements MyConsumer<EventType> {

  @Inject
  ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) eventType;
    this.clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
