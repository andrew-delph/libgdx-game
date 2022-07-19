package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import java.util.function.Consumer;

public class ReplaceBlockOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) eventType;
    this.clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
