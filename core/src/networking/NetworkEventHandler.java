package networking;

import com.google.inject.Inject;
import common.Coordinates;
import common.events.EventConsumer;
import common.events.EventService;
import common.events.types.CreateAIEntityEventType;
import networking.events.EventFactory;
import networking.events.types.outgoing.*;

public class NetworkEventHandler extends EventConsumer {

  @Inject EventFactory eventFactory;
  @Inject EventService eventService;

  public NetworkEventHandler() {
    super();
  }

  public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
    try {
      String event = networkEvent.getEvent();
      if (event.equals(CreateEntityOutgoingEventType.type)) {
        eventService.fireEvent(eventFactory.createCreateEntityIncomingEvent(networkEvent));
      } else if (event.equals(UpdateEntityOutgoingEventType.type)) {
        eventService.fireEvent(eventFactory.createUpdateEntityIncomingEvent(networkEvent));
      } else if (event.equals(SubscriptionOutgoingEventType.type)) {
        eventService.fireEvent(eventFactory.createSubscriptionIncomingEvent(networkEvent));
      } else if (event.equals(RemoveEntityOutgoingEventType.type)) {
        System.out.println(event);
        eventService.fireEvent(eventFactory.createRemoveEntityIncomingEvent(networkEvent));
      } else if (event.equals(ReplaceBlockOutgoingEventType.type)) {
        System.out.println(event);
        eventService.fireEvent(eventFactory.createReplaceBlockIncomingEvent(networkEvent));
      } else if (event.equals(CreateAIEntityEventType.type)) {
        System.out.println(event);
        eventService.queuePostUpdateEvent(eventFactory.createAIEntityEventType(new Coordinates(0,0)));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
