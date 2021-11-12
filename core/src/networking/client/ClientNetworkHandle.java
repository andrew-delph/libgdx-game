package networking.client;

import com.google.inject.Inject;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;
import networking.ObserverFactory;
import networking.RequestNetworkEventObserver;

import java.util.UUID;

public class ClientNetworkHandle {
  public String host = "localhost";
  public int port = 99;
  public UUID uuid;
  RequestNetworkEventObserver requestNetworkEventObserver;
  @Inject ObserverFactory observerFactory;
  private ManagedChannel channel;
  private NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;

  @Inject
  public ClientNetworkHandle() {
    this.uuid = UUID.randomUUID();
    System.out.println("client: " + this.uuid.toString());
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void connect() {
    this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
    requestNetworkEventObserver = observerFactory.create();
    requestNetworkEventObserver.responseObserver =
        this.asyncStub.networkObjectStream(requestNetworkEventObserver);

    NetworkObjects.NetworkEvent authenticationEvent =
        NetworkObjects.NetworkEvent.newBuilder().setEvent("authentication").build();

    this.send(authenticationEvent);
  }

  public synchronized void send(NetworkObjects.NetworkEvent networkEvent) {
    networkEvent = networkEvent.toBuilder().setUser(this.uuid.toString()).build();
    requestNetworkEventObserver.responseObserver.onNext(networkEvent);
  }

  public void close() {
    this.requestNetworkEventObserver.responseObserver.onCompleted();
    this.channel.shutdown();
  }
}
