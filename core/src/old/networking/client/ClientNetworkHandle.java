package old.networking.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import old.configure.CoreApp;
import old.infra.entity.EntityFactory;
import old.infra.entity.EntityManager;
import old.infra.events.EventService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import old.networking.NetworkObject;
import old.networking.NetworkObjectServiceGrpc;
import old.networking.client.observers.ClientObserverFactory;
import old.networking.connection.ConnectionStore;
import old.networking.events.EventRegister;

import java.util.Scanner;

public class ClientNetworkHandle {

  public static String host = "localhost";
  public static int port = 99;
  public final EntityManager entityManager;

  private final ManagedChannel channel;
  private final NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockingStub;
  private final NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;

  public StreamObserver<NetworkObject.CreateNetworkObject> createObserver;
  public StreamObserver<NetworkObject.UpdateNetworkObject> updateObserver;
  public StreamObserver<NetworkObject.RemoveNetworkObject> removeObserver;
  // responders
  public StreamObserver<NetworkObject.CreateNetworkObject> createRequest;
  public StreamObserver<NetworkObject.UpdateNetworkObject> updateRequest;
  public StreamObserver<NetworkObject.RemoveNetworkObject> removeRequest;
  public ClientObserverFactory clientObserverFactory;
  EventService eventService;
  EntityFactory entityFactory;

  @Inject
  public ClientNetworkHandle(
      EntityManager entityManager,
      ConnectionStore connectionStore,
      ClientObserverFactory clientObserverFactory,
      EventService eventService,
      EntityFactory entityFactory,
      EventRegister eventRegister) {
    this.clientObserverFactory = clientObserverFactory;
    this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    this.blockingStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
    this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
    this.entityManager = entityManager;
    this.eventService = eventService;
    this.entityFactory = entityFactory;
    System.out.println(eventRegister);
    eventRegister.register();
  }

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new CoreApp());

    Scanner myInput = new Scanner(System.in);

    ClientNetworkHandle client = injector.getInstance(ClientNetworkHandle.class);

    System.out.println("starting..!");
    while (true) {
      String id = myInput.nextLine();
      NetworkObject.CreateNetworkObject createRequestObject =
          NetworkObject.CreateNetworkObject.newBuilder().setId(id).build();
      client.createRequest.onNext(createRequestObject);
    }
  }

  public void connect() {

    createObserver = this.clientObserverFactory.createCreateObserver();
    updateObserver = this.clientObserverFactory.createUpdateObserver();
    removeObserver = this.clientObserverFactory.createRemoveObserver();

    createRequest = this.asyncStub.create(createObserver);
    updateRequest = this.asyncStub.update(updateObserver);
    removeRequest = this.asyncStub.remove(removeObserver);
  }

  public void disconnect() {
    this.createRequest.onCompleted();
    this.updateRequest.onCompleted();
    this.removeRequest.onCompleted();
    this.channel.shutdown();
  }
}
