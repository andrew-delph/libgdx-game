package networking.server.observers;

import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;
import networking.events.incoming.IncomingDisconnectEvent;
import networking.events.incoming.IncomingUpdateEntityEvent;

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
        IncomingUpdateEntityEvent updateEvent = new IncomingUpdateEntityEvent(entityUpdate, this.requestObserver);
        this.eventService.fireEvent(updateEvent);

    }

    @Override
    public void onError(Throwable throwable) {
        IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(this.requestObserver);
        this.eventService.fireEvent(incomingDisconnectEvent);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE UpdateObserver");
        IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(this.requestObserver);
        this.eventService.fireEvent(incomingDisconnectEvent);
    }
}
