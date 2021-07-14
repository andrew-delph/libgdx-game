package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventService;
import infra.networking.events.EventFactory;

public class ObserverFactory {
  @Inject NetworkEventHandler networkEventHandler;
  @Inject ConnectionStore connectionStore;
  @Inject EventService eventService;
  @Inject EventFactory eventFactory;

  @Inject
  ObserverFactory() {}

  public RequestNetworkEventObserver create() {
    return new RequestNetworkEventObserver(
        networkEventHandler, connectionStore, eventService, eventFactory);
  }
  ;
}
