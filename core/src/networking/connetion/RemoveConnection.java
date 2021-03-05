package networking.connetion;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class RemoveConnection extends AbtractConnection{

    public RemoveConnection(StreamObserver<NetworkObject.RemoveNetworkObject> responseObserver) {
        this.responseObserver = responseObserver;
    }
}
