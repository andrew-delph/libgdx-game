package networking.server.observers;

import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;
import networking.events.DisconnectEvent;
import networking.events.UpdateEntityEvent;

public class UpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    EventService eventService;
    StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver;

    protected UpdateObserver(EntityManager entityManager, ConnectionStore connectionStore, EventService eventService, StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.eventService = eventService;
        this.requestObserver = requestObserver;
    }

    @Override
    public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {
        EntityData entityUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
        UpdateEntityEvent updateEvent = new UpdateEntityEvent(entityUpdate, this.requestObserver);
        this.eventService.fireEvent(updateEvent);

    }

    @Override
    public void onError(Throwable throwable) {
//        DisconnectEvent disconnectEvent = new DisconnectEvent(this.requestObserver);
//        this.eventService.fireEvent(disconnectEvent);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE UpdateObserver");
        DisconnectEvent disconnectEvent = new DisconnectEvent(this.requestObserver);
        this.eventService.fireEvent(disconnectEvent);
    }
}
