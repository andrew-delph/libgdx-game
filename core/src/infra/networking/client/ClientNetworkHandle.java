package infra.networking.client;

import com.google.inject.Inject;
import infra.networking.NetworkObjectServiceGrpc;
import infra.networking.NetworkObjects;
import infra.networking.ObserverFactory;
import infra.networking.RequestNetworkEventObserver;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.UUID;

public class ClientNetworkHandle {
  public static String host = "localhost";
  public static int port = 99;
  public UUID uuid;
  RequestNetworkEventObserver requestNetworkEventObserver;
  @Inject ObserverFactory observerFactory;
  private ManagedChannel channel;
  private NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;

  @Inject
  public ClientNetworkHandle() {
    this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
    this.uuid = UUID.randomUUID();
  }

  public void connect() {
    requestNetworkEventObserver = observerFactory.create();
    requestNetworkEventObserver.responseObserver =
        this.asyncStub.networkObjectStream(requestNetworkEventObserver);

    NetworkObjects.NetworkEvent authenticationEvent =
        NetworkObjects.NetworkEvent.newBuilder().setEvent("authentication").build();

    this.send(authenticationEvent);
  }

  public void send(NetworkObjects.NetworkEvent networkEvent) {
    networkEvent = networkEvent.toBuilder().setUser(this.uuid.toString()).build();
    requestNetworkEventObserver.responseObserver.onNext(networkEvent);
  }

  public void close() {
    this.channel.shutdown();
  }
}
