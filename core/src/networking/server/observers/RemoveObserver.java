package networking.server.observers;

import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.server.connetion.ConnectionStore;
import networking.server.connetion.CreateConnection;
import networking.server.connetion.RemoveConnection;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    public RemoveObserver(EntityManager entityManager, ConnectionStore connectionStore){
        this.entityManager =  entityManager;
        this.connectionStore = connectionStore;
    }

    @Override
    public void onNext(NetworkObject.RemoveNetworkObject removeNetworkObject) {
        this.entityManager.remove(EntityDataFactory.getInstance().createEntityData(removeNetworkObject).getID());

        this.connectionStore.getAll(RemoveConnection.class).forEach(createConnection -> {
            if (createConnection.responseObserver == this){
                return;
            }
            else{
                createConnection.responseObserver.onNext(removeNetworkObject);
            }
        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
