package networking.client.observers;

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

    protected RemoveObserver(EntityManager entityManager, ConnectionStore connectionStore, EventService eventService) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.eventService = eventService;
    }

    @Override
    public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {
        System.out.println("remove");
        RemoveEntityEvent removeEvent = new RemoveEntityEvent(EntityDataFactory.getInstance().createEntityData(removeNetworkObject), null);
        this.eventService.fireEvent(removeEvent);
//        this.entityManager.remove(EntityDataFactory.getInstance().createEntityData(removeNetworkObject).getID());
    }

    @Override
    public void onError(Throwable throwable) {
        DisconnectEvent disconnectEvent = new DisconnectEvent(null);
        this.eventService.fireEvent(disconnectEvent);
    }

    @Override
    public void onCompleted() {
        DisconnectEvent disconnectEvent = new DisconnectEvent(null);
        this.eventService.fireEvent(disconnectEvent);
    }
}
