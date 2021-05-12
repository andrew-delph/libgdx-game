package networking.connection;

import io.grpc.stub.StreamObserver;

import java.util.UUID;

public abstract class AbtractConnection {
    public UUID id = UUID.randomUUID();
    public StreamObserver responseObserver;
    public StreamObserver requestObserver;

    public void close() {
        this.requestObserver.onCompleted();
        this.responseObserver.onCompleted();
    }
}
