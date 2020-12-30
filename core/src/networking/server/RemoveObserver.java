package networking.server;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class RemoveObserver implements StreamObserver<NetworkObject.RemoveNetworkObject> {
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
