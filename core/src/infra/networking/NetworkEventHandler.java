package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventService;
import infra.networking.consumer.NetworkConsumer;
import infra.networking.events.CreateEntityIncomingEvent;
import infra.networking.events.EntityEventFactory;

public class NetworkEventHandler extends NetworkConsumer {

  @Inject EntityEventFactory entityEventFactory;
  @Inject EventService eventService;

  public NetworkEventHandler() {
    super();
  }

  public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
    if (networkEvent.getEvent().equals(CreateEntityIncomingEvent.type)) {
      eventService.fireEvent(
          entityEventFactory.createCreateEntityIncomingEvent(networkEvent.getData()));
    } else if (networkEvent.getEvent().equals("update_entity")) {
      eventService.fireEvent(
          entityEventFactory.createUpdateEntityIncomingEvent(networkEvent.getData()));
    }
  }
}
