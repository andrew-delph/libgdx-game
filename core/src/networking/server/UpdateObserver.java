package networking.server;

import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityDataFactory;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

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
            System.out.println("target is null.");
            return;
        }

        System.out.println(targetUuid.toString()+",,,...!!!"+target+"here");

        target.updateEntityData(entityUpdate);

        for (StreamObserver connection : ConnectionStore.getInstance().getAll(UpdateObserver.class)) {

            if (this == connection) {
                continue;
            }

            ((UpdateObserver)connection).onNext(updateNetworkObject);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
