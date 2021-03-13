package networking.server.observers;

import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.entity.factories.EntityFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;
import networking.events.CreateEntityEvent;
import networking.events.DisconnectEvent;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
    EntityManager entityManager;
    ConnectionStore connectionStore;
    EntityFactory entityFactory;
    EventService eventService;
    StreamObserver<NetworkObject.CreateNetworkObject> requestObserver;

    protected CreateObserver(EntityManager entityManager, ConnectionStore connectionStore, EntityFactory entityFactory, EventService eventService, StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.entityFactory = entityFactory;
        this.eventService = eventService;
        this.requestObserver = requestObserver;
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject update) {
        EntityData createData = EntityDataFactory.getInstance().createEntityData(update);

        CreateEntityEvent createEntityEvent = new CreateEntityEvent(createData, requestObserver);

        this.eventService.fireEvent(createEntityEvent);

    }

    @Override
    public void onError(Throwable throwable) {
//        System.out.println("error " + throwable);
//        DisconnectEvent disconnectEvent = new DisconnectEvent(this.requestObserver);
//        this.eventService.fireEvent(disconnectEvent);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE CreateObserver");
        DisconnectEvent disconnectEvent = new DisconnectEvent(this.requestObserver);

        this.eventService.fireEvent(disconnectEvent);
    }
}
