package networking.client.observers;

import infra.entitydata.EntityData;
import infra.entity.EntityManager;
import infra.entitydata.EntityDataFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connection.ConnectionStore;
import networking.events.incoming.IncomingDisconnectEvent;
import networking.events.incoming.IncomingUpdateEntityEvent;

public class ClientUpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {

  EntityManager entityManager;
  ConnectionStore connectionStore;
  EventService eventService;

  protected ClientUpdateObserver(
      EntityManager entityManager, ConnectionStore connectionStore, EventService eventService) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.eventService = eventService;
  }

  @Override
  public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {
    EntityData entityUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
    IncomingUpdateEntityEvent updateEvent = new IncomingUpdateEntityEvent(entityUpdate, null);
    this.eventService.fireEvent(updateEvent);

    //        EntityData entityDataUpdate =
    // EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
    //        Entity target_entity =
    // this.entityManager.get(UUID.fromString(entityDataUpdate.getID()));
    //        if (target_entity == null) {
    //            return;
    //        }
    //        target_entity.updateEntityData(entityDataUpdate);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("UpdateObserver error " + throwable);
    IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(null);
    this.eventService.fireEvent(incomingDisconnectEvent);
  }

  @Override
  public void onCompleted() {
    IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(null);
    this.eventService.fireEvent(incomingDisconnectEvent);
  }
}
