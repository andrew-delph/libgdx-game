package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import java.util.function.Consumer;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;

public class UpdateEntityOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityOutgoingEventType realEvent = (UpdateEntityOutgoingEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
