package networking.server.observers;

import infra.entity.EntityManager;
import infra.entitydata.EntityDataFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connection.ConnectionStore;
import networking.events.incoming.IncomingDisconnectEvent;
import networking.events.incoming.IncomingRemoveEntityEvent;

public class ServerRemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    EventService eventService;
    StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver;

    protected ServerRemoveObserver(
            EntityManager entityManager,
            ConnectionStore connectionStore,
            EventService eventService,
            StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.eventService = eventService;
        this.requestObserver = requestObserver;
    }

    @Override
    public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {
        IncomingRemoveEntityEvent removeEvent =
                new IncomingRemoveEntityEvent(
                        EntityDataFactory.getInstance().createEntityData(removeNetworkObject),
                        this.requestObserver);
        this.eventService.fireEvent(removeEvent);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("ServerRemoveObserver error " + throwable);
        IncomingDisconnectEvent incomingDisconnectEvent =
                new IncomingDisconnectEvent(this.requestObserver);
        this.eventService.fireEvent(incomingDisconnectEvent);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE RemoveObserver");
        IncomingDisconnectEvent incomingDisconnectEvent =
                new IncomingDisconnectEvent(this.requestObserver);
        this.eventService.fireEvent(incomingDisconnectEvent);
    }
}
