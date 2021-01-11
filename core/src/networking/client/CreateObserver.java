package networking.client;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
    @Override
    public void onNext(NetworkObject.CreateNetworkObject update) {
        EntityData entityDataUpdate = EntityDataFactory.getInstance().createEntityData(update);

        Entity target = EntityManager.getInstance().get(entityDataUpdate.getID());
        target.updateEntityData(entityDataUpdate);

        System.out.println("<<< " + update.getId());
    }
    @Override
    public void onError(Throwable throwable) {
        System.out.println("error " + throwable);
    }
    @Override
    public void onCompleted() {
        System.out.println("COMPLETE");
    }
}
