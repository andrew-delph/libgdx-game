package networking.connetion;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class CreateConnection extends AbtractConnection {

    public CreateConnection(StreamObserver<NetworkObject.CreateNetworkObject> responseObserver, StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
        this.responseObserver = responseObserver;
        this.requestObserver = requestObserver;
    }
}
