package old.networking.server.observers;

import old.infra.entity.EntityManager;
import old.infra.entitydata.EntityData;
import old.infra.entitydata.EntityDataFactory;
import old.infra.events.EventService;
import io.grpc.stub.StreamObserver;
import old.networking.NetworkObject;
import old.networking.connection.ConnectionStore;
import old.networking.events.incoming.IncomingDisconnectEvent;
import old.networking.events.incoming.IncomingUpdateEntityEvent;

public class ServerUpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {

  EntityManager entityManager;
  ConnectionStore connectionStore;
  EventService eventService;
  StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver;

  protected ServerUpdateObserver(
      EntityManager entityManager,
      ConnectionStore connectionStore,
      EventService eventService,
      StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.eventService = eventService;
    this.requestObserver = requestObserver;
  }

  @Override
  public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {
    EntityData entityUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
    IncomingUpdateEntityEvent updateEvent =
        new IncomingUpdateEntityEvent(entityUpdate, this.requestObserver);
    this.eventService.fireEvent(updateEvent);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("ServerUpdateObserver error " + throwable);
    IncomingDisconnectEvent incomingDisconnectEvent =
        new IncomingDisconnectEvent(this.requestObserver);
    this.eventService.fireEvent(incomingDisconnectEvent);
  }

  @Override
  public void onCompleted() {
    System.out.println("COMPLETE UpdateObserver");
    IncomingDisconnectEvent incomingDisconnectEvent =
        new IncomingDisconnectEvent(this.requestObserver);
    this.eventService.fireEvent(incomingDisconnectEvent);
  }
}
