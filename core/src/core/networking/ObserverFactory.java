package core.networking;

import com.google.inject.Inject;
import core.common.events.EventService;
import core.networking.events.EventTypeFactory;
import core.networking.translation.NetworkEventHandler;

public class ObserverFactory {
  @Inject
  NetworkEventHandler networkEventHandler;
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
