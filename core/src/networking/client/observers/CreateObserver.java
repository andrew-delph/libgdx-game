package networking.client.observers;

import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.entity.factories.EntityFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.UUID;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {

    EntityManager entityManager;
    EntityFactory entityFactory;

    protected CreateObserver(EntityManager entityManager, EntityFactory entityFactory){
        this.entityManager = entityManager;
        this.entityFactory = entityFactory;
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject create) {
        EntityData createEntityData = EntityDataFactory.getInstance().createEntityData(create);
        Entity new_entity = entityFactory.create(createEntityData);
        this.entityManager.add(new_entity);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("CreateObserver error " + throwable);
    }

    @Override
    public void onCompleted() {
        System.out.println("COMPLETE");
    }
}
