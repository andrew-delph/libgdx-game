package networking;

import com.google.inject.Inject;
import common.Coordinates;
import common.events.EventConsumer;
import common.events.EventService;
import common.events.types.CreateAIEntityEventType;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.*;

public class NetworkEventHandler extends EventConsumer {

  @Inject
  EventTypeFactory eventTypeFactory;
  @Inject EventService eventService;

  public NetworkEventHandler() {
    super();
  }

  public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
    try {
      String event = networkEvent.getEvent();
      if (event.equals(CreateEntityOutgoingEventType.type)) {
        eventService.fireEvent(eventTypeFactory.createCreateEntityIncomingEvent(networkEvent));
      } else if (event.equals(UpdateEntityOutgoingEventType.type)) {
        eventService.fireEvent(eventTypeFactory.createUpdateEntityIncomingEvent(networkEvent));
      } else if (event.equals(SubscriptionOutgoingEventType.type)) {
        eventService.fireEvent(eventTypeFactory.createSubscriptionIncomingEvent(networkEvent));
      } else if (event.equals(RemoveEntityOutgoingEventType.type)) {
        System.out.println(event);
        eventService.fireEvent(eventTypeFactory.createRemoveEntityIncomingEvent(networkEvent));
      } else if (event.equals(ReplaceBlockOutgoingEventType.type)) {
        System.out.println(event);
        eventService.fireEvent(eventTypeFactory.createReplaceBlockIncomingEvent(networkEvent));
      } else if (event.equals(CreateAIEntityEventType.type)) {
        System.out.println(event);
        eventService.queuePostUpdateEvent(eventTypeFactory.createAIEntityEventType(new Coordinates(0,0)));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
