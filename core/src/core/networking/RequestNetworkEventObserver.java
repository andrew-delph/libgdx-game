package core.networking;

import core.app.user.UserID;
import core.common.events.EventService;
import core.networking.events.EventTypeFactory;
import core.networking.translation.DataTranslationEnum;
import core.networking.translation.NetworkEventHandler;
import io.grpc.stub.StreamObserver;
import networking.NetworkObjects.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestNetworkEventObserver implements StreamObserver<NetworkEvent> {
  final Logger LOGGER = LogManager.getLogger();
  public StreamObserver<NetworkEvent> responseObserver;
  public UserID userID;
  NetworkEventHandler networkEventHandler;
  ConnectionStore connectionStore;
  EventService eventService;
  EventTypeFactory eventTypeFactory;

  public RequestNetworkEventObserver(
      NetworkEventHandler networkEventHandler,
      ConnectionStore connectionStore,
      EventService eventService,
      EventTypeFactory eventTypeFactory) {
    this.networkEventHandler = networkEventHandler;
    this.connectionStore = connectionStore;
    this.eventService = eventService;
    this.eventTypeFactory = eventTypeFactory;
  }

  @Override
  public synchronized void onNext(NetworkEvent networkEvent) {
    if (networkEvent.getEvent().equals(DataTranslationEnum.AUTH)) {
      this.userID = UserID.createUserID(networkEvent.getUser());
      LOGGER.info("Received authentication: " + this.userID);
      eventService.fireEvent(EventTypeFactory.createAuthenticationIncomingEventType(userID, this));
    } else {
      networkEventHandler.handleNetworkEvent(networkEvent);
    }
  }

  @Override
  public void onError(Throwable throwable) {
    LOGGER.error(this.userID + " " + throwable);
    throwable.printStackTrace();
    this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.userID));
  }

  @Override
  public void onCompleted() {
    this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.userID));
  }
}
