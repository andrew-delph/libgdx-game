package networking.client.observers;

import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;

    protected RemoveObserver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {
        this.entityManager.remove(EntityDataFactory.getInstance().createEntityData(removeNetworkObject).getID());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
