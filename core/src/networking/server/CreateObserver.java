package networking.server;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.entity.factories.EntityFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
    EntityManager entityManager;

    CreateObserver() {
        entityManager = EntityManager.getInstance();
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject update) {
        System.out.println("<<< " + update.getId());

        EntityData createData = EntityDataFactory.getInstance().createEntityData(update);
        for (Object data : createData.data.entrySet()) {
            System.out.println(data);
        }
        Entity createEntity = EntityFactory.getInstance().create(createData);
        this.entityManager.add(createEntity);
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
