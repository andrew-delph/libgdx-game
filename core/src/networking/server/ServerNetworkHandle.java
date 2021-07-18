package networking.server;

import com.google.inject.Inject;
import networking.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.UUID;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
  public UUID uuid;
  @Inject ObserverFactory observerFactory;
  @Inject ConnectionStore connectionStore;
  private Server server;

  @Inject
  public ServerNetworkHandle() {
    this.uuid = UUID.randomUUID();
    System.out.println("server: " + this.uuid.toString());
  }

  public void start() throws IOException {
    System.out.println("server start");
    server = ServerBuilder.forPort(99).addService(this).build();
    server.start();
  }

  @Override
  public StreamObserver<NetworkObjects.NetworkEvent> networkObjectStream(
      StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {
    RequestNetworkEventObserver requestNetworkEventObserver = observerFactory.create();
    requestNetworkEventObserver.responseObserver = responseObserver;
    NetworkObjects.NetworkEvent authenticationEvent =
        NetworkObjects.NetworkEvent.newBuilder()
            .setEvent("authentication")
            .setUser(this.uuid.toString())
            .build();
    requestNetworkEventObserver.responseObserver.onNext(authenticationEvent);
    return requestNetworkEventObserver;
  }

  public void close() {
    this.server.shutdown();
  }

  public synchronized void send(UUID uuid, NetworkObjects.NetworkEvent networkEvent) {
    networkEvent = networkEvent.toBuilder().setUser(this.uuid.toString()).build();
    connectionStore.getConnection(uuid).responseObserver.onNext(networkEvent);
  }
}
