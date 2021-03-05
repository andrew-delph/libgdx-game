package networking.connetion;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class UpdateConnection extends AbtractConnection {
    public UpdateConnection(StreamObserver<NetworkObject.UpdateNetworkObject> responseObserver) {
        this.responseObserver = responseObserver;
    }
}
