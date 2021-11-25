package networking;

import com.google.inject.Inject;
import common.events.EventService;
import networking.events.EventTypeFactory;

public class ObserverFactory {
  @Inject NetworkEventHandler networkEventHandler;
  @Inject ConnectionStore connectionStore;
  @Inject EventService eventService;
  @Inject
  EventTypeFactory eventTypeFactory;

  @Inject
  ObserverFactory() {}

  public RequestNetworkEventObserver create() {
    return new RequestNetworkEventObserver(
        networkEventHandler, connectionStore, eventService, eventTypeFactory);
  }
}
