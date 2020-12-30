package networking.client;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class CreateObserver implements StreamObserver<NetworkObject.CreateNetworkObject> {
    @Override
    public void onNext(NetworkObject.CreateNetworkObject update) {
        System.out.println("<<< " + update.getId());
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
