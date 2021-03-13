package networking.server.observers;

import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.ConnectionStore;
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
//        UUID targetUuid = UUID.fromString(entityUpdate.getID());
//        Entity target = this.entityManager.get(targetUuid);
//        if (target == null){
//            return;
//        }
//        target.updateEntityData(entityUpdate);
//        this.connectionStore.getAll(UpdateConnection.class).forEach(updateConnection -> {
//            if (updateConnection.responseObserver == this){
//            }
//            else{
//                updateConnection.requestObserver.onNext(updateNetworkObject);
//            }
//        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE UpdateObserver");
    }
}
