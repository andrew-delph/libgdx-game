package networking.client.observers;

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

    protected UpdateObserver(EntityManager entityManager, ConnectionStore connectionStore, EventService eventService) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.eventService = eventService;
    }

    @Override
    public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {
        System.out.println("update");
        EntityData entityUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
        UpdateEntityEvent updateEvent = new UpdateEntityEvent(entityUpdate, null);
        this.eventService.fireEvent(updateEvent);

//        EntityData entityDataUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
//        Entity target_entity = this.entityManager.get(UUID.fromString(entityDataUpdate.getID()));
//        if (target_entity == null) {
//            return;
//        }
//        target_entity.updateEntityData(entityDataUpdate);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("UpdateObserver error " + throwable);
        DisconnectEvent disconnectEvent = new DisconnectEvent(null);
        this.eventService.fireEvent(disconnectEvent);
    }

    @Override
    public void onCompleted() {
        DisconnectEvent disconnectEvent = new DisconnectEvent(null);
        this.eventService.fireEvent(disconnectEvent);
    }
}
