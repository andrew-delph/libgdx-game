package networking.events.consumer.server.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import common.events.types.ItemActionEventType;
import common.exceptions.EntityNotFound;
import entity.attributes.inventory.item.comsumers.ItemActionService;
import java.util.function.Consumer;

public class ItemActionIncomingConsumerServer implements Consumer<EventType> {

  @Inject
  ItemActionService itemActionService;

  @Override
  public void accept(EventType eventType) {
    ItemActionEventType realEvent = (ItemActionEventType) eventType;
    try {
      itemActionService.use(realEvent.getItemActionType(), realEvent.getControleeUUID());
    } catch (EntityNotFound e) {
      e.printStackTrace();
    }
  }
}
