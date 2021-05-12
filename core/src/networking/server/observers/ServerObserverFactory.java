package networking.server.observers;

import com.google.inject.Inject;
import infra.entity.EntityFactory;
import infra.entity.EntityManager;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connection.ConnectionStore;

public class ServerObserverFactory {

  EntityManager entityManager;
  ConnectionStore connectionStore;
  EntityFactory entityFactory;
  EventService eventService;

  @Inject
  ServerObserverFactory(
      EntityManager entityManager,
      ConnectionStore connectionStore,
      EntityFactory entityFactory,
      EventService eventService) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.entityFactory = entityFactory;
    this.eventService = eventService;
  }

  public ServerCreateObserver createCreateObserver(
      StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
    return new ServerCreateObserver(
        this.entityManager,
        this.connectionStore,
        this.entityFactory,
        this.eventService,
        requestObserver);
  }

  public ServerUpdateObserver createUpdateObserver(
      StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
    return new ServerUpdateObserver(
        this.entityManager, this.connectionStore, this.eventService, requestObserver);
  }

  public ServerRemoveObserver createRemoveObserver(
      StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
    return new ServerRemoveObserver(
        this.entityManager, this.connectionStore, this.eventService, requestObserver);
  }
}
