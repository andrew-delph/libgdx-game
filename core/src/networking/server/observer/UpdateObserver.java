package networking.server.observer;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.server.connetion.ConnectionStore;
import networking.server.connetion.UpdateConnection;

import java.util.UUID;

public class UpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {

    UUID managerID;

    public UpdateObserver(UUID managerID){
        this.managerID = managerID;
    }

    @Override
    public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {
        EntityData entityUpdate = EntityDataFactory.getInstance().createEntityData(updateNetworkObject);
        UUID targetUuid = UUID.fromString(entityUpdate.getID());
        Entity target = EntityManager.getInstance(this.managerID).get(targetUuid);
        if (target == null){
            return;
        }
        target.updateEntityData(entityUpdate);
        ConnectionStore.getInstance().getAll(UpdateConnection.class).forEach(updateConnection -> {
            if (updateConnection.responseObserver == this){
                return;
            }
            else{
                updateConnection.responseObserver.onNext(updateNetworkObject);
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
