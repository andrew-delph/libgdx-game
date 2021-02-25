package networking.server.observers;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.entity.factories.EntityFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.server.connetion.ConnectionStore;
import networking.server.connetion.CreateConnection;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
    EntityManager entityManager;
    ConnectionStore connectionStore;

    public CreateObserver(EntityManager entityManager, ConnectionStore connectionStore){
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject update) {
        EntityData createData = EntityDataFactory.getInstance().createEntityData(update);
        Entity createEntity = EntityFactory.getInstance().create(createData);
        this.entityManager.add(createEntity);
        this.connectionStore.getAll(CreateConnection.class).forEach(createConnection -> {
            if (createConnection.responseObserver == this){
                return;
            }
            else{
                createConnection.responseObserver.onNext(update);
            }
        });
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
