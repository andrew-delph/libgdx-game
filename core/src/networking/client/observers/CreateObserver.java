package networking.client.observers;

import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.entity.factories.EntityFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;
import networking.events.incoming.IncomingCreateEntityEvent;
import networking.events.incoming.IncomingDisconnectEvent;

import java.util.UUID;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {

    EntityManager entityManager;
    EntityFactory entityFactory;
    EventService eventService;
    UUID ownerID;

    protected CreateObserver(EntityManager entityManager, ConnectionStore connectionStore, EntityFactory entityFactory, EventService eventService) {
        this.entityManager = entityManager;
        this.entityFactory = entityFactory;
        this.eventService = eventService;
        this.ownerID = UUID.randomUUID();
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject create) {
        System.out.println("create");

        EntityData createData = EntityDataFactory.getInstance().createEntityData(create);
        createData.setOwner(this.ownerID.toString());
        IncomingCreateEntityEvent incomingCreateEntityEvent = new IncomingCreateEntityEvent(createData, null);
        this.eventService.fireEvent(incomingCreateEntityEvent);


//        EntityData createEntityData = EntityDataFactory.getInstance().createEntityData(create);
//        Entity new_entity = entityFactory.create(createEntityData);
//        this.entityManager.add(new_entity);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("CreateObserver error " + throwable);
        IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(null);
        this.eventService.fireEvent(incomingDisconnectEvent);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE andrew");
        IncomingDisconnectEvent incomingDisconnectEvent = new IncomingDisconnectEvent(null);
        this.eventService.fireEvent(incomingDisconnectEvent);
    }
}
