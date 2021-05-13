package networking.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import configure.CoreApp;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.EntityManager;
import infra.entitydata.EntityData;
import infra.events.EventService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectFactory;
import networking.NetworkObjectServiceGrpc;
import networking.connection.ConnectionStore;
import networking.connection.CreateConnection;
import networking.connection.RemoveConnection;
import networking.connection.UpdateConnection;
import networking.events.EventRegister;
import networking.server.observers.ServerCreateObserver;
import networking.server.observers.ServerObserverFactory;
import networking.server.observers.ServerRemoveObserver;
import networking.server.observers.ServerUpdateObserver;

import java.io.IOException;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

  public final EntityManager entityManager;
  public final ConnectionStore connectionStore;
  final ServerObserverFactory serverObserverFactory;
  private final Server server;
  EventService eventService;
  EntityFactory entityFactory;
  NetworkObjectFactory networkObjectFactory;

  @Inject
  public ServerNetworkHandle(
      EntityManager entityManager,
      ConnectionStore connectionStore,
      ServerObserverFactory serverObserverFactory,
      EventService eventService,
      EntityFactory entityFactory,
      NetworkObjectFactory networkObjectFactory,
      EventRegister eventRegister) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.serverObserverFactory = serverObserverFactory;
    this.eventService = eventService;
    this.entityFactory = entityFactory;
    this.networkObjectFactory = networkObjectFactory;
    server = ServerBuilder.forPort(99).addService(this).build();
    eventRegister.register();
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Injector injector = Guice.createInjector(new CoreApp());
    ServerNetworkHandle server = injector.getInstance(ServerNetworkHandle.class);
    server.start();
    server.awaitTermination();
  }

  public void start() throws IOException {

    server.start();
  }

  public void close() {
    server.shutdown();
    // TODO check if server closed
  }

  @Override
  public StreamObserver<NetworkObject.CreateNetworkObject> create(
      StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
    ServerCreateObserver responseObserver =
        this.serverObserverFactory.createCreateObserver(requestObserver);
    CreateConnection connection = new CreateConnection(responseObserver, requestObserver);

    connectionStore.add(connection);
    for (Entity entity : this.entityManager.getAll()) {
      EntityData entityData = entity.toEntityData();
      NetworkObject.NetworkObjectItem networkObjectItem_x =
          NetworkObject.NetworkObjectItem.newBuilder()
              .setKey("x")
              .setValue((entityData.getX() + ""))
              .build();
      NetworkObject.NetworkObjectItem networkObjectItem_y =
          NetworkObject.NetworkObjectItem.newBuilder()
              .setKey("y")
              .setValue((entityData.getY() + ""))
              .build();
      NetworkObject.CreateNetworkObject createRequestObject =
          NetworkObject.CreateNetworkObject.newBuilder()
              .setId(entityData.getID())
              .addItem(networkObjectItem_x)
              .addItem(networkObjectItem_y)
              .build();
      requestObserver.onNext(createRequestObject);
    }
    return responseObserver;
  }

  @Override
  public StreamObserver<NetworkObject.UpdateNetworkObject> update(
      StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
    ServerUpdateObserver responseObserver =
        this.serverObserverFactory.createUpdateObserver(requestObserver);
    UpdateConnection connection = new UpdateConnection(responseObserver, requestObserver);
    connectionStore.add(connection);
    return responseObserver;
  }

  @Override
  public StreamObserver<NetworkObject.RemoveNetworkObject> remove(
      StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
    ServerRemoveObserver responseObserver =
        this.serverObserverFactory.createRemoveObserver(requestObserver);
    connectionStore.add(new RemoveConnection(responseObserver, requestObserver));
    return responseObserver;
  }

  public void awaitTermination() throws InterruptedException {
    this.server.awaitTermination();
  }
}
