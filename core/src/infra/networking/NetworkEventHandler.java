package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventService;
import infra.networking.consumer.NetworkConsumer;
import infra.networking.events.CreateEntityOutgoingEvent;
import infra.networking.events.EventFactory;
import infra.networking.events.SubscriptionEvent;

public class NetworkEventHandler extends NetworkConsumer {

  @Inject EventFactory eventFactory;
  @Inject EventService eventService;

  public NetworkEventHandler() {
    super();
  }

  public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
    String event = networkEvent.getEvent();
    if (event.equals(CreateEntityOutgoingEvent.type)) {
      eventService.fireEvent(eventFactory.createCreateEntityIncomingEvent(networkEvent.getData()));
    } else if (event.equals("update_entity")) {
      eventService.fireEvent(eventFactory.createUpdateEntityIncomingEvent(networkEvent.getData()));
    } else if (event.equals(SubscriptionEvent.type)) {

    }
  }
}
