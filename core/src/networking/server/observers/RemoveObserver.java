package networking.server.observers;

import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;
import networking.events.DisconnectEvent;
import networking.events.RemoveEntityEvent;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    EventService eventService;
    StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver;

    protected RemoveObserver(EntityManager entityManager, ConnectionStore connectionStore, EventService eventService, StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.eventService = eventService;
        this.requestObserver = requestObserver;
    }

    @Override
    public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {

        RemoveEntityEvent removeEvent = new RemoveEntityEvent(EntityDataFactory.getInstance().createEntityData(removeNetworkObject), this.requestObserver);

        this.eventService.fireEvent(removeEvent);

    }

    @Override
    public void onError(Throwable throwable) {
//        DisconnectEvent disconnectEvent = new DisconnectEvent(this.requestObserver);
//        this.eventService.fireEvent(disconnectEvent);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE RemoveObserver");
        DisconnectEvent disconnectEvent = new DisconnectEvent(this.requestObserver);
        this.eventService.fireEvent(disconnectEvent);
    }
}
