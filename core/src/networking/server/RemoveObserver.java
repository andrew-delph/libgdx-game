package networking.server;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.UUID;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {

    UUID managerID;

    public RemoveObserver(UUID managerID){
        this.managerID = managerID;
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
