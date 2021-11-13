package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.EventType;
import networking.client.ClientNetworkHandle;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;

import java.util.function.Consumer;

public class UpdateEntityOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityOutgoingEventType realEvent = (UpdateEntityOutgoingEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
