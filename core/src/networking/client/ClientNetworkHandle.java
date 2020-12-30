package networking.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;
import org.shareable.ShareableMapServiceGrpc;
import org.shareable.ShareableProto;

import java.util.concurrent.ConcurrentHashMap;

public class ClientNetworkHandle {

    private final ManagedChannel channel;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockingStub;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;

    public ClientNetworkHandle(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
        this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
    }

    void connect(){

        // receivers
        StreamObserver<NetworkObject.CreateNetworkObject> createObserver = new CreateObserver();
        StreamObserver<NetworkObject.UpdateNetworkObject> updateObserver = new UpdateObserver();
        StreamObserver<NetworkObject.RemoveNetworkObject> removeObserver = new RemoveObserver();

        // responders
        StreamObserver<NetworkObject.CreateNetworkObject> createRequest = this.asyncStub.create(createObserver);
        StreamObserver<NetworkObject.UpdateNetworkObject> updateRequest = this.asyncStub.update(updateObserver);
        StreamObserver<NetworkObject.RemoveNetworkObject> removeRequest = this.asyncStub.remove(removeObserver);

    }

}

