package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventService;
import infra.networking.consumer.NetworkConsumer;
import infra.networking.events.*;

public class NetworkEventHandler extends NetworkConsumer {

  @Inject EventFactory eventFactory;
  @Inject EventService eventService;

  public NetworkEventHandler() {
    super();
  }

  public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
    try {
      String event = networkEvent.getEvent();
      if (event.equals(CreateEntityOutgoingEvent.type)) {
        eventService.fireEvent(eventFactory.createCreateEntityIncomingEvent(networkEvent));
      } else if (event.equals(UpdateEntityOutgoingEvent.type)) {
        eventService.fireEvent(eventFactory.createUpdateEntityIncomingEvent(networkEvent));
      } else if (event.equals(SubscriptionOutgoingEvent.type)) {
        System.out.println("sub event");
        eventService.fireEvent(eventFactory.createSubscriptionIncomingEvent(networkEvent));
      } else if (event.equals(RemoveEntityOutgoingEvent.type)) {
        eventService.fireEvent(eventFactory.createRemoveEntityIncomingEvent(networkEvent));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
