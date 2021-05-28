package infra.networking;

import com.google.inject.Inject;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class RequestNetworkEventObserver implements StreamObserver<NetworkObjects.NetworkEvent> {

  public StreamObserver<NetworkObjects.NetworkEvent> responseObserver;
  @Inject NetworkEventHandler networkEventHandler;
  @Inject ConnectionStore connectionStore;

  @Override
  public void onNext(NetworkObjects.NetworkEvent networkEvent) {
    if (networkEvent.getEvent().equals("authentication")) {
      System.out.println("authentication");
      connectionStore.addConnection(UUID.fromString(networkEvent.getUser()), this);
    } else {
      networkEventHandler.handleNetworkEvent(networkEvent);
    }
  }

  @Override
  public void onError(Throwable throwable) {}

  @Override
  public void onCompleted() {}
}
