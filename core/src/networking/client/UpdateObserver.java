package networking.client;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.UUID;

public class UpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {
    @Override
    public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {

        EntityData entityDataUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);

        Entity target_entity = EntityManager.getInstance().get(UUID.fromString(entityDataUpdate.getID()));

        if (target_entity == null){
            return;
        }
        target_entity.updateEntityData(entityDataUpdate);

        System.out.println("Update entity:" + target_entity.getEntityData().getID().toString());

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
