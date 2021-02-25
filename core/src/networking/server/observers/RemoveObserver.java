package networking.server.observers;

import infra.entity.EntityManager;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.server.connetion.ConnectionStore;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    public RemoveObserver(EntityManager entityManager, ConnectionStore connectionStore){
        this.entityManager =  entityManager;
        this.connectionStore = connectionStore;
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
