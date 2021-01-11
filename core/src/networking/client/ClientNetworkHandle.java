package networking.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;

import java.util.Scanner;

public class ClientNetworkHandle {

    static ClientNetworkHandle instance;
    static String host = "localhost";
    static int port = 99;

    public static ClientNetworkHandle getInstance() {
        if (instance == null) {
            instance = new ClientNetworkHandle(host, port);
        }
        return instance;
    }

    private final ManagedChannel channel;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockingStub;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;


    StreamObserver<NetworkObject.CreateNetworkObject> createObserver;
    StreamObserver<NetworkObject.UpdateNetworkObject> updateObserver;
    StreamObserver<NetworkObject.RemoveNetworkObject> removeObserver;
    // responders
    StreamObserver<NetworkObject.CreateNetworkObject> createRequest;
    StreamObserver<NetworkObject.UpdateNetworkObject> updateRequest;
    StreamObserver<NetworkObject.RemoveNetworkObject> removeRequest;

    public ClientNetworkHandle(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
        this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
    }

    void connect() {
        // receivers
        createObserver = new CreateObserver();
        updateObserver = new UpdateObserver();
        removeObserver = new RemoveObserver();
        // responders
        createRequest = this.asyncStub.create(createObserver);
        updateRequest = this.asyncStub.update(updateObserver);
        removeRequest = this.asyncStub.remove(removeObserver);
    }

    public static void main(String args[]) throws InterruptedException {
        Scanner myInput = new Scanner(System.in);

        ClientNetworkHandle client = ClientNetworkHandle.getInstance();
        client.connect();
        System.out.println("starting..!");
        while (true) {
            String id = myInput.nextLine();
            NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(id).build();
            client.createRequest.onNext(createRequestObject);
        }
    }

}

