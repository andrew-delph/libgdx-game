package networking.connection;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class UpdateConnection extends AbtractConnection {
  public UpdateConnection(
      StreamObserver<NetworkObject.UpdateNetworkObject> responseObserver,
      StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
    this.responseObserver = responseObserver;
    this.requestObserver = requestObserver;
  }
}
