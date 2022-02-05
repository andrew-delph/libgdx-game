package networking;

import app.user.UserID;
import common.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.events.EventTypeFactory;
import networking.translation.DataTranslationEnum;
import networking.translation.NetworkEventHandler;

public class RequestNetworkEventObserver implements StreamObserver<NetworkObjects.NetworkEvent> {

    public StreamObserver<NetworkObjects.NetworkEvent> responseObserver;
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
    public synchronized void onNext(NetworkObjects.NetworkEvent networkEvent) {
        if (networkEvent.getEvent().equals(DataTranslationEnum.AUTH)) {
            this.userID = UserID.createUserID(networkEvent.getUser());
            System.out.println("Received authentication: " + this.userID);
            eventService.fireEvent(eventTypeFactory.createAuthenticationIncomingEventType(userID, this));
        } else {
            networkEventHandler.handleNetworkEvent(networkEvent);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError: " + this.userID + " " + throwable);
        this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.userID));
    }

    @Override
    public void onCompleted() {
        System.out.println("onCompleted " + this.userID);
        this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.userID));
    }
}
