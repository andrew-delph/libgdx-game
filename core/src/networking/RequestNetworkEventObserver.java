package networking;

import common.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.events.EventTypeFactory;

import java.util.UUID;

public class RequestNetworkEventObserver implements StreamObserver<NetworkObjects.NetworkEvent> {

  public StreamObserver<NetworkObjects.NetworkEvent> responseObserver;
  public UUID uuid;
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
    if (networkEvent.getEvent().equals("authentication")) {
      connectionStore.addConnection(UUID.fromString(networkEvent.getUser()), this);
      this.uuid = UUID.fromString(networkEvent.getUser());
      System.out.println("authentication: " + this.uuid);
    } else {
      networkEventHandler.handleNetworkEvent(networkEvent);
    }
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("onError: " + this.uuid + " " + throwable);
    this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.uuid));
  }

  @Override
  public void onCompleted() {
    System.out.println("onCompleted " + this.uuid);
    this.eventService.fireEvent(this.eventTypeFactory.createDisconnectionEvent(this.uuid));
  }
}
