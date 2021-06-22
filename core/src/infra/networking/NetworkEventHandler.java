package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventConsumer;
import infra.common.events.EventService;
import infra.networking.events.*;

public class NetworkEventHandler extends EventConsumer {

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
        eventService.fireEvent(eventFactory.createSubscriptionIncomingEvent(networkEvent));
      } else if (event.equals(RemoveEntityOutgoingEvent.type)) {
        System.out.println(event);
        eventService.fireEvent(eventFactory.createRemoveEntityIncomingEvent(networkEvent));
      } else if (event.equals(ReplaceBlockOutgoingEvent.type)) {
        System.out.println(event);
        eventService.fireEvent(eventFactory.createReplaceBlockIncomingEvent(networkEvent));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
