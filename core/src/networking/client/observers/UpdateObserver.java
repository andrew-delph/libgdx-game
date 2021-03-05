package networking.client.observers;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.UUID;

public class UpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {

    EntityManager entityManager;

    protected UpdateObserver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {
        EntityData entityDataUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
        Entity target_entity = this.entityManager.get(UUID.fromString(entityDataUpdate.getID()));
        if (target_entity == null) {
            return;
        }
        target_entity.updateEntityData(entityDataUpdate);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("UpdateObserver error " + throwable);
    }

    @Override
    public void onCompleted() {

    }
}
