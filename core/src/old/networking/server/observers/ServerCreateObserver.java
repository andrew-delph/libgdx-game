package old.networking.server.observers;

import old.infra.entity.Entity;
import old.infra.entity.EntityFactory;
import old.infra.entity.EntityManager;
import old.infra.entitydata.EntityData;
import old.infra.entitydata.EntityDataFactory;
import old.infra.events.EventService;
import io.grpc.stub.StreamObserver;
import old.networking.NetworkObject;
import old.networking.connection.ConnectionStore;
import old.networking.events.incoming.IncomingCreateEntityEvent;
import old.networking.events.incoming.IncomingDisconnectEvent;
import old.networking.events.incoming.IncomingRemoveEntityEvent;

import java.util.UUID;

public class ServerCreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
  EntityManager entityManager;
  ConnectionStore connectionStore;
  EntityFactory entityFactory;
  EventService eventService;
  StreamObserver<NetworkObject.CreateNetworkObject> requestObserver;
  UUID ownerID;

  protected ServerCreateObserver(
      EntityManager entityManager,
      ConnectionStore connectionStore,
      EntityFactory entityFactory,
      EventService eventService,
      StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.entityFactory = entityFactory;
    this.eventService = eventService;
    this.requestObserver = requestObserver;
    this.ownerID = UUID.randomUUID();
  }

  @Override
  public void onNext(NetworkObject.CreateNetworkObject update) {
    EntityData createData = EntityDataFactory.getInstance().createEntityData(update);
    createData.setOwner(this.ownerID.toString());
    IncomingCreateEntityEvent incomingCreateEntityEvent =
        new IncomingCreateEntityEvent(createData, requestObserver);
    this.eventService.fireEvent(incomingCreateEntityEvent);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("ServerCreateObserver error " + throwable);
    IncomingDisconnectEvent incomingDisconnectEvent =
        new IncomingDisconnectEvent(this.requestObserver);
    this.eventService.fireEvent(incomingDisconnectEvent);
    this.removeOwned();
  }

  @Override
  public void onCompleted() {
    System.out.println("COMPLETE CreateObserver");
    IncomingDisconnectEvent incomingDisconnectEvent =
        new IncomingDisconnectEvent(this.requestObserver);
    this.eventService.fireEvent(incomingDisconnectEvent);
    this.removeOwned();
  }

  private void removeOwned() {
    for (Entity entity : this.entityManager.getAll()) {
      System.out.println(entity.getOwner().toString().compareTo(this.ownerID.toString()));
      if (entity.getOwner().toString().compareTo(this.ownerID.toString()) == 0) {
        IncomingRemoveEntityEvent removeEvent =
            new IncomingRemoveEntityEvent(entity.toEntityData(), null);
        this.eventService.fireEvent(removeEvent);
      }
    }
  }
}
