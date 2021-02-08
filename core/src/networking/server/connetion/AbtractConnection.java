package networking.server.connetion;

import io.grpc.stub.StreamObserver;

public abstract class AbtractConnection {
    public StreamObserver responseObserver;
}
