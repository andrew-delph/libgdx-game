package core.networking;

import com.badlogic.gdx.Gdx;
import core.app.user.UserID;
import core.common.GameSettings;
import core.common.events.EventService;
import core.networking.events.EventTypeFactory;
import core.networking.translation.DataTranslationEnum;
import core.networking.translation.NetworkEventHandler;
import io.grpc.stub.StreamObserver;
import networking.NetworkObjects.NetworkEvent;

public class RequestNetworkEventObserver implements StreamObserver<NetworkEvent> {

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
      Gdx.app.log(GameSettings.LOG_TAG, "Received authentication: " + this.userID);
      eventService.fireEvent(EventTypeFactory.createAuthenticationIncomingEventType(userID, this));
    } else {
      networkEventHandler.handleNetworkEvent(networkEvent);
    }
  }

  @Override
  public void onError(Throwable throwable) {
    Gdx.app.error(GameSettings.LOG_TAG, (this.userID + " " + throwable));
    throwable.printStackTrace();
    this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.userID));
  }

  @Override
  public void onCompleted() {
    this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.userID));
  }
}
