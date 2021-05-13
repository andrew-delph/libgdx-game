package networking.connection;

import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

public class RemoveConnection extends AbtractConnection {

  public RemoveConnection(
      StreamObserver<NetworkObject.RemoveNetworkObject> responseObserver,
      StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
    this.responseObserver = responseObserver;
    this.requestObserver = requestObserver;
  }
}
