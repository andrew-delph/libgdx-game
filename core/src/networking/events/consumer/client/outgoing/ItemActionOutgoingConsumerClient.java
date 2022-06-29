package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import common.events.types.ItemActionEventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;

public class ItemActionOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ItemActionEventType realEvent = (ItemActionEventType) eventType;

    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
