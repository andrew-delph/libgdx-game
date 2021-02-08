package networking.server.connetion;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class CreateConnection extends AbtractConnection {

    public CreateConnection(StreamObserver<NetworkObject.CreateNetworkObject> responseObserver) {
        this.responseObserver = responseObserver;
    }
}
