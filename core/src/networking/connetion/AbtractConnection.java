package networking.connetion;

import io.grpc.stub.StreamObserver;

import java.util.UUID;

public abstract class AbtractConnection {
    UUID id = UUID.randomUUID();
    public StreamObserver responseObserver;
    public StreamObserver requestObserver;
}
