package networking.server.observer;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import infra.entity.factories.EntityFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.server.connetion.ConnectionStore;
import networking.server.connetion.CreateConnection;

import java.util.UUID;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
    UUID managerID;

    public CreateObserver(UUID managerID){
        this.managerID = managerID;
    }

    @Override
    public void onNext(NetworkObject.CreateNetworkObject update) {
        System.out.println("<<< " + update.getId());
        EntityData createData = EntityDataFactory.getInstance().createEntityData(update);
        Entity createEntity = EntityFactory.getInstance().create(createData);
        EntityManager.getInstance(this.managerID).add(createEntity);
        System.out.println("send out");
        ConnectionStore.getInstance().getAll(CreateConnection.class).forEach(createConnection -> {
            if (createConnection.responseObserver == this){
                return;
            }
            else{
                System.out.println("here");
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
