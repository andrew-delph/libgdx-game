package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import common.events.types.ItemActionEventType;
import entity.attributes.inventory.item.comsumers.ItemActionService;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;

public class ItemActionOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;
  @Inject ItemActionService itemActionService;

  @Override
  public void accept(EventType eventType) {
    ItemActionEventType realEvent = (ItemActionEventType) eventType;

    Boolean gcd = itemActionService.checkTriggerGCD(realEvent.getControleeUUID());
    if (!gcd) return;

    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
