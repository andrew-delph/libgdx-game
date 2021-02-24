package networking.client;

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

    @Inject
    CreateObserver(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject create) {
        EntityData createEntityData = EntityDataFactory.getInstance().createEntityData(create);
        Entity new_entity = EntityFactory.getInstance().create(createEntityData);
        this.entityManager.add(new_entity);
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
