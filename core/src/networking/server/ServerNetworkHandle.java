package networking.server;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
    @Override
    public StreamObserver<NetworkObject.CreateNetworkObject> create(StreamObserver<NetworkObject.CreateNetworkObject> responseObserver) {
        return new CreateObserver();
    }

    @Override
    public StreamObserver<NetworkObject.UpdateNetworkObject> update(StreamObserver<NetworkObject.UpdateNetworkObject> responseObserver) {
        return new UpdateObserver();
    }

    @Override
    public StreamObserver<NetworkObject.RemoveNetworkObject> remove(StreamObserver<NetworkObject.RemoveNetworkObject> responseObserver) {
        return new RemoveObserver();
    }
}
