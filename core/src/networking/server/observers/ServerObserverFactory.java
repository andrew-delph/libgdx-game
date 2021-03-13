package networking.server.observers;

import com.google.inject.Inject;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;

public class ServerObserverFactory {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    EntityFactory entityFactory;
    EventService eventService;

    @Inject
    ServerObserverFactory(EntityManager entityManager, ConnectionStore connectionStore, EntityFactory entityFactory, EventService eventService) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.entityFactory = entityFactory;
        this.eventService = eventService;
    }

    public CreateObserver createCreateObserver(StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
        return new CreateObserver(this.entityManager, this.connectionStore, this.entityFactory, this.eventService, requestObserver);
    }

    public UpdateObserver createUpdateObserver(StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
        return new UpdateObserver(this.entityManager, this.connectionStore, this.eventService, requestObserver);
    }

    public RemoveObserver createRemoveObserver(StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
        return new RemoveObserver(this.entityManager, this.connectionStore, this.eventService, requestObserver);
    }

}
