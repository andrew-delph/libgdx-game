package networking.client.observers;

import com.google.inject.Inject;
import infra.entity.EntityManager;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.UUID;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;

    public RemoveObserver(EntityManager entityManager){
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
