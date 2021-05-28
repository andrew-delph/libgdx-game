package infra.networking.server;

import com.google.inject.Inject;
import infra.networking.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.UUID;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
  @Inject ObserverFactory observerFactory;
  @Inject ConnectionStore connectionStore;
  private Server server;

  public void start() throws IOException {
    server = ServerBuilder.forPort(99).addService(this).build();
    server.start();
  }

  @Override
  public StreamObserver<NetworkObjects.NetworkEvent> networkObjectStream(
      StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {
    RequestNetworkEventObserver requestNetworkEventObserver = observerFactory.create();
    requestNetworkEventObserver.responseObserver = responseObserver;
    return requestNetworkEventObserver;
  }

  public void close() {
    this.server.shutdown();
  }

  public void send(UUID uuid, NetworkObjects.NetworkEvent networkEvent) {
    connectionStore.getConnection(uuid).responseObserver.onNext(networkEvent);
  }
}
