package old.networking.client.observers;

import old.infra.entity.EntityManager;
import old.infra.entitydata.EntityDataFactory;
import old.infra.events.EventService;
import io.grpc.stub.StreamObserver;
import old.networking.NetworkObject;
import old.networking.connection.ConnectionStore;
import old.networking.events.incoming.IncomingDisconnectEvent;
import old.networking.events.incoming.IncomingRemoveEntityEvent;

public class ClientRemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

  EntityManager entityManager;
  ConnectionStore connectionStore;
  EventService eventService;

  protected ClientRemoveObserver(
      EntityManager entityManager, ConnectionStore connectionStore, EventService eventService) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.eventService = eventService;
  }

  @Override
  public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {
    System.out.println("remove");
    IncomingRemoveEntityEvent removeEvent =
        new IncomingRemoveEntityEvent(
            EntityDataFactory.getInstance().createEntityData(removeNetworkObject), null);
    this.eventService.fireEvent(removeEvent);
    //
    // this.entityManager.remove(EntityDataFactory.getInstance().createEntityData(removeNetworkObject).getID());
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("ClientRemoveObserver error " + throwable);
    IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(null);
    this.eventService.fireEvent(incomingDisconnectEvent);
  }

  @Override
  public void onCompleted() {
    IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(null);
    this.eventService.fireEvent(incomingDisconnectEvent);
  }
}
