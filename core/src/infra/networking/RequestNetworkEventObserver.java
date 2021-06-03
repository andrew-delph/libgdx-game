package infra.networking;

import com.google.inject.Inject;
import infra.common.events.EventService;
import infra.networking.events.EventFactory;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class RequestNetworkEventObserver implements StreamObserver<NetworkObjects.NetworkEvent> {

  public StreamObserver<NetworkObjects.NetworkEvent> responseObserver;
  public UUID uuid;
  @Inject NetworkEventHandler networkEventHandler;
  @Inject ConnectionStore connectionStore;
  @Inject EventService eventService;
  @Inject EventFactory eventFactory;

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
    this.eventService.fireEvent(this.eventFactory.createDisconnectionEvent(this.uuid));
  }

  @Override
  public void onCompleted() {
    System.out.println("onCompleted " + this.uuid);
    this.eventService.fireEvent(this.eventFactory.createDisconnectionEvent(this.uuid));
  }
}
