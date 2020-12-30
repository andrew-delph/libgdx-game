package networking.client;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class UpdateObserver implements StreamObserver<NetworkObject.UpdateNetworkObject> {
    @Override
    public void onNext(NetworkObject.UpdateNetworkObject updateNetworkObject) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
