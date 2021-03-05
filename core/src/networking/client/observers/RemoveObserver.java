package networking.client.observers;

import infra.entity.EntityManager;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;

    public RemoveObserver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
