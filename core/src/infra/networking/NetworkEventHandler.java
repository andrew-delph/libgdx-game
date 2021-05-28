package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventService;
import infra.networking.consumer.NetworkConsumer;
import infra.networking.events.CreateEntityOutgoingEvent;
import infra.networking.events.EventFactory;
import infra.networking.events.SubscriptionOutgoingEvent;
import infra.networking.events.UpdateEntityOutgoingEvent;

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
    } else if (event.equals(UpdateEntityOutgoingEvent.type)) {
      eventService.fireEvent(eventFactory.createUpdateEntityIncomingEvent(networkEvent.getData()));
    } else if (event.equals(SubscriptionOutgoingEvent.type)) {
      System.out.println("sub event");
      eventService.fireEvent(eventFactory.createSubscriptionIncomingEvent(networkEvent));
    }
  }
}
