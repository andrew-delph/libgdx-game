package infra.networking.server;

import com.google.inject.Inject;
import infra.networking.NetworkObjectServiceGrpc;
import infra.networking.NetworkObjects;
import infra.networking.ObserverFactory;
import infra.networking.RequestNetworkEventObserver;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
  private Server server;

  @Inject ObserverFactory observerFactory;

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
}
